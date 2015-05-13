package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;

import java.io.Serializable;
import java.util.*;

/**
 * DerivedMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class DerivedMeasureComputer implements MeasureComputer {
    private DerivedMeasure definition;
    private Map<String, MeasureComputer> computers;
    private Serializable expression;

    public DerivedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof DerivedMeasure)) {
            throw new IllegalArgumentException();
        }

        MeasureComputerFactory computerFactory = new MeasureComputerFactory();

        this.definition = (DerivedMeasure) definition;
        this.expression = MVEL.compileExpression(this.definition.getFunction());
        this.computers = new HashMap<String, MeasureComputer>();
        for (Map.Entry<String, MeasureDefinition> entry : this.definition.getUsedMeasureMap().entrySet()) {
            this.computers.put(entry.getKey(), computerFactory.create(entry.getValue(), filter));
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
            Map<String, Double> expressionVariables = new HashMap<String, Double>();
            boolean ignoreScope = false;

            for (String varName : variables.keySet()) {
                Measure measure = variables.get(varName).get(scope);
                if (measure == null) {
                    ignoreScope = true;
                    break;
                }
                expressionVariables.put(varName, measure.getValue());
            }

            if (ignoreScope) {
                continue;
            }

            Object value = MVEL.executeExpression(expression, expressionVariables);
            results.add(buildMeasure(scope, value));
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

    private Measure buildMeasure(MeasureScope scope, Object value) {
        Measure measure;

        if (definition instanceof DerivedSingleInstanceMeasure) {
            measure = new MeasureInstance(definition, scope, value);
        } else {
            measure = new Measure(definition, scope, value);
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
