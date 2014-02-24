package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;

import java.util.*;

/**
 * AggregatedMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class AggregatedMeasureComputer implements MeasureComputer {

    private AggregatedMeasure definition;
    private MeasureComputer baseComputer;
    private ScopeClassifier classifier;
    private Aggregator agg;

    public AggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;
        this.baseComputer = new MeasureComputerFactory().create(this.definition.getBaseMeasure(), filter);
        this.classifier = new ScopeClassifierFactory().create(filter);
        this.agg = new Aggregator(this.definition.getAggregationFunction());
    }

    @Override
    public void update(LogEntry entry) {
        classifier.update(entry);
        baseComputer.update(entry);
    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> result = new ArrayList<Measure>();
        Collection<? extends Measure> measures = baseComputer.compute();
        Map<String, MeasureInstance> measureMap = buildMeasureMap(measures);

        Collection<MeasureScope> scopes = classifier.listScopes();
        for (MeasureScope scope : scopes) {
            Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap);
            double val = agg.aggregate(toAggregate);

            result.add(new Measure(definition, scope, val));
        }
        for (Measure m:result){
        	System.out.println(m.getInstances()+" "+m.getValue());
        }
        return result;
    }

    private Collection<Double> chooseMeasuresToAggregate(MeasureScope scope, Map<String, MeasureInstance> measureMap) {
        Collection<Double> toAggregate = new ArrayList<Double>();
        for (String instance : scope.getInstances()) {
            toAggregate.add(measureMap.get(instance).getValue());
        }
        return toAggregate;
    }

    private Map<String, MeasureInstance> buildMeasureMap(Collection<? extends Measure> measures) {
        Map<String, MeasureInstance> measureMap = new HashMap<String, MeasureInstance>();
        for (Measure m : measures) {
            if (m instanceof MeasureInstance) {
                MeasureInstance mi = (MeasureInstance) m;
                measureMap.put(mi.getInstanceId(), mi);
            }
        }
        return measureMap;
    }

}
