package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.Overrides;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DerivedMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class DerivedMeasureComputer implements MeasureComputer {
    private DerivedMeasure definition;
    private Map<String, MeasureComputer> computers;
    private Map<String, Overrides.OverriddenMeasures> overrides;
    private Serializable expression;

    public DerivedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        this(definition, filter, new Overrides());
    }

    public DerivedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter, Overrides overrides) {
        if (!(definition instanceof DerivedMeasure)) {
            throw new IllegalArgumentException();
        }

        MeasureComputerFactory computerFactory = new MeasureComputerFactory();

        this.definition = (DerivedMeasure) definition;
        this.expression = MVEL.compileExpression(this.definition.getFunction());
        this.computers = new HashMap<String, MeasureComputer>();
        this.overrides = new HashMap<String, Overrides.OverriddenMeasures>();
        for (Map.Entry<String, MeasureDefinition> entry : this.definition.getUsedMeasureMap().entrySet()) {
            this.computers.put(entry.getKey(), computerFactory.create(entry.getValue(), filter));
            if (overrides != null) {
                this.overrides.put(entry.getKey(), overrides.getOverrides(entry.getValue()));
            }

        }

    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> results = new ArrayList<Measure>();
        Map<String, Map<MeasureScope, Measure>> variables = new HashMap<String, Map<MeasureScope, Measure>>();

        int size = Integer.MAX_VALUE;
        String oneVar = null;
        for (String varName : computers.keySet()) {
            Map<MeasureScope, Measure> measures = new HashMap<MeasureScope, Measure>();
            List<? extends Measure> compute = computers.get(varName).compute();
            for (Measure m : compute) {
                measures.put(m.getMeasureScope(), m);
            }

            variables.put(varName, measures);
            size = Math.min(size, compute.size());
            oneVar = varName;
        }

        if (oneVar == null) throw new RuntimeException("No variables defined");

        for (MeasureScope scope : variables.get(oneVar).keySet()) {
            Map<String, Object> expressionVariables = new HashMap<String, Object>();
            Map<String, Measure> expressionMeasures = new HashMap<String,Measure>();
            boolean ignoreScope = false;

            for (String varName : variables.keySet()) {
                Measure measure = variables.get(varName).get(scope);
                if (measure == null) {
                    ignoreScope = true;
                    break;
                }

                Measure overriddenValues = null;
                Overrides.OverriddenMeasures overriddenMeasures = overrides.get(varName);
                if (overriddenMeasures != null) {
                    overriddenValues = overriddenMeasures.getOverriddenValueForScope(scope);
                }

                if (overriddenValues != null) {
                    expressionVariables.put(varName, overriddenValues.getValueAsObject());
                    expressionMeasures.put(varName, overriddenValues);
                    expressionMeasures.put(varName + "-overriden", measure);
                } else {
                    expressionVariables.put(varName, measure.getValueAsObject());
                    expressionMeasures.put(varName, measure);
                }
            }

            if (ignoreScope) {
                continue;
            }

            Object value = MVEL.executeExpression(expression, expressionVariables);
            results.add(buildMeasure(scope, value, expressionMeasures));
        }

//        for (int i = 0; i < size; i++) {
//            MeasureScope scope = null;
//            Map<String, Double> expressionVariables = new HashMap<String, Double>();
//
//            for (String varName : variables.keySet()) {
//                Measure measure = variables.get(varName).get(i);
//                expressionVariables.put(varName, measure.getValue());
//                if (scope == null)
//                    scope = new MeasureScope(measure.getProcessId(), measure.getInstances());
//            }
//
//            Object value = MVEL.executeExpression(expression, expressionVariables);
//
//            results.add(buildMeasure(scope, value));
//        }

        return results;
    }

    private Measure buildMeasure(MeasureScope scope, Object value, Map<String, Measure> expressionVariables) {
        Measure measure;

        if (definition instanceof DerivedSingleInstanceMeasure) {
            measure = new MeasureInstance(definition, scope, value);
            if (definition.isIncludeEvidences()) {
                measure.addEvidence(((MeasureInstance) measure).getInstanceId(), expressionVariables);
            }
        } else {
            measure = new Measure(definition, scope, value);
            if (definition.isIncludeEvidences()) {
                measure.addEvidence(measure.getMeasureScope().getScopeInfo().toString(), expressionVariables);
            }
        }

        return measure;
    }


    @Override
    public void update(LogEntry entry) {
        for (MeasureComputer computer : computers.values()) {
            computer.update(entry);
        }
    }
}
