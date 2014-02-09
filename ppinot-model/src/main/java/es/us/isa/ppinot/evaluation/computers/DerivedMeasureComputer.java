package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
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
        Map<String, List<? extends Measure>> measures = new HashMap<String, List<? extends Measure>>();

        int size = 0;
        for (String varName : computers.keySet()) {
            List<? extends Measure> compute = computers.get(varName).compute();
            measures.put(varName, compute);
            size = compute.size();
        }

        for (int i = 0; i < size; i++) {
            MeasureScope scope = null;
            Map<String, Double> variables = new HashMap<String, Double>();

            for (String varName : measures.keySet()) {
                Measure measure = measures.get(varName).get(i);
                variables.put(varName, measure.getValue());
                if (scope == null)
                    scope = new MeasureScope(measure.getProcessId(), measure.getInstances());
            }

            double value = (Double) MVEL.executeExpression(expression, variables);

            results.add(new Measure(definition, scope, value));
        }

        return results;
    }

    @Override
    public void update(LogEntry entry) {
        for (MeasureComputer computer : computers.values()) {
            computer.update(entry);
        }
    }
}
