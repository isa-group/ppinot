package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;

import java.util.*;

/**
 * AggregatedMeasureComputer Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class AggregatedMeasureComputer implements MeasureComputer {

    private AggregatedMeasure definition;
    private MeasureComputer baseComputer;
    private ScopeClassifier classifier;
    private Aggregator agg;
    private final List<MeasureComputer> listDataMeasure;

    public AggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;

        this.listDataMeasure = new ArrayList<MeasureComputer>();
        final MeasureComputerFactory measureComputerFactory = new MeasureComputerFactory();
        if (this.definition.getGroupedBy() != null) {
            for (DataContentSelection s : this.definition.getGroupedBy()) {
                DataMeasure dm = new DataMeasure();
                dm.setDataContentSelection(s);
                listDataMeasure.add(measureComputerFactory.create(dm, filter));
            }
        }
        this.baseComputer = measureComputerFactory.create(this.definition.getBaseMeasure(), filter);

        this.classifier = new ScopeClassifierFactory().create(filter);
        this.agg = new Aggregator(this.definition.getAggregationFunction());
    }

    @Override
    public void update(LogEntry entry) {
        classifier.update(entry);
        for (MeasureComputer c : listDataMeasure) {
            c.update(entry);
        }
        baseComputer.update(entry);
    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> result = new ArrayList<Measure>();
        Collection<? extends Measure> measures = baseComputer.compute();
        Map<String, MeasureInstance> measureMap = buildMeasureMap(measures);

        Collection<MeasureScope> scopes = classifier.listScopes();
        for (MeasureScope scope : scopes) {

//            Collection<Double> toAggregate = Collections.EMPTY_LIST;
//            if (listDataMeasure != null) {
//                for (MeasureComputer md : listDataMeasure) {
//                    if (md.compute().contains(scope.getInstances().iterator().next())) {
//                        toAggregate = chooseMeasuresToAggregate(scope, measureMap);
//                    }
//                }
//            } else {
//                toAggregate = chooseMeasuresToAggregate(scope, measureMap);
//            }
            
            Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap);
            
            
//            Map<MeasureInstance, Map<String, String>> measureInstanceGroup = new HashMap<MeasureInstance, Map<String, String>>();

            double val = agg.aggregate(toAggregate);

            result.add(new Measure(definition, scope, val));
        }

        return result;
    }

    private class XX {

        private Map<String, MeasureInstance> measureMap;
        private Map<MeasureInstance, Map<String, String>> measureInstanceGroup;

        public XX(Map<String, MeasureInstance> measureMap, Map<MeasureInstance, Map<String, String>> measureInstanceGroup) {
            this.measureMap = measureMap;
            this.measureInstanceGroup = measureInstanceGroup;
        }

        public Map<String, MeasureInstance> getMeasureMap() {
            return measureMap;
        }

        public Map<MeasureInstance, Map<String, String>> getMeasureInstanceGroup() {
            return measureInstanceGroup;
        }
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
