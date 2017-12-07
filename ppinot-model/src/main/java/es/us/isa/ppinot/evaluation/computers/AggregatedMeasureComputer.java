package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
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
    private MeasureComputer filterComputer;
    private ScopeClassifier classifier;
    private Aggregator agg;
    private final List<MeasureComputer> listGroupByMeasureComputer;

    public AggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;

        this.listGroupByMeasureComputer = new ArrayList<MeasureComputer>();
        final MeasureComputerFactory measureComputerFactory = new MeasureComputerFactory();
        if (this.definition.getGroupedBy() != null) {
            for (DataMeasure dm : this.definition.getGroupedBy()) {
                listGroupByMeasureComputer.add(measureComputerFactory.create(dm, filter));
            }
        }

        this.baseComputer = measureComputerFactory.create(this.definition.getBaseMeasure(), filter);
        if (this.definition.getFilter() != null) {
            this.filterComputer = measureComputerFactory.create(this.definition.getFilter(), filter);
        }
        this.classifier = new ScopeClassifierFactory().create(filter, this.definition.getPeriodReferencePoint());
        this.agg = new Aggregator(this.definition.getAggregationFunction());
    }

    @Override
    public void update(LogEntry entry) {
        classifier.update(entry);
        for (MeasureComputer c : listGroupByMeasureComputer) {
            c.update(entry);
        }
        baseComputer.update(entry);
        if (filterComputer != null) {
            filterComputer.update(entry);
        }
    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> result = new ArrayList<Measure>();
        Collection<? extends Measure> measures = baseComputer.compute();
        List<? extends Measure> filters = new ArrayList<Measure>();
        if (filterComputer != null) {
            filters = filterComputer.compute();
        }

        Map<String, MeasureInstance> measureMap = buildMeasureMap(measures);
        Map<String, MeasureInstance> filterMap = buildMeasureMap(filters);
        Collection<MeasureScope> allScopes;

        if (listGroupByMeasureComputer != null && listGroupByMeasureComputer.size() > 0) {

            Map<String, Map<String, String>> instanceGroupBy = new HashMap<String, Map<String, String>>(); // string -> instanceId
            
            for (MeasureComputer mc : listGroupByMeasureComputer) {
                DataMeasureComputer dmc = (DataMeasureComputer) mc;
                for (Measure measure : mc.compute()) {
                    MeasureInstance mi = (MeasureInstance) measure;

                    if (!instanceGroupBy.containsKey(mi.getInstanceId())) {
                        instanceGroupBy.put(mi.getInstanceId(), new HashMap<String, String>());
                    }
                    
                    instanceGroupBy.get(mi.getInstanceId()).put(dmc.definition.getDataContentSelection().getId(), mi.getValueAsString());
                }
            }

            Collection<MeasureScope> temporalScopes = classifier.listScopes(definition.isIncludeUnfinished());

            allScopes = new ArrayList<MeasureScope>();
            for (MeasureScope temporalScope : temporalScopes) {
                Map<Map<String, String>, List<String>> instancesByGroup = new HashMap<Map<String, String>, List<String>>();
                for (String instanceId : temporalScope.getInstances()) {
                    Map<String, String> group = instanceGroupBy.get(instanceId);

                    if (group != null) {
                        List<String> instances = instancesByGroup.get(group);
                        if (instances == null) {
                            instances = new ArrayList<String>();
                            instancesByGroup.put(group, instances);
                        }

                        instances.add(instanceId);
                    }
                }

                for (Map<String, String> groupScope : instancesByGroup.keySet()) {
                    TemporalMeasureScope tScope = (TemporalMeasureScope) temporalScope;
                    List<String> instances = instancesByGroup.get(groupScope);
                    GroupByTemporalMeasureScopeImpl groupByTemporalMeasureScope = new GroupByTemporalMeasureScopeImpl(tScope.getProcessId(), instances, tScope.getStart(), tScope.getEnd());
                    groupByTemporalMeasureScope.setGroupParameters(groupScope);

                    allScopes.add(groupByTemporalMeasureScope);
                }
            }
        } else {
            allScopes = classifier.listScopes(definition.isIncludeUnfinished());
        }


        for (MeasureScope scope : allScopes) {
            if (filterComputer != null) {
                Collection<String> filterScopeInstances = filterTrueInstances(scope.getInstances(), filterMap);
                scope.getInstances().retainAll(filterScopeInstances);
            }

            Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap);
            if (!toAggregate.isEmpty()) {
                double val = agg.aggregate(toAggregate);
                Measure measure = new Measure(definition, scope, val);
                result.add(measure);
                if (definition.isIncludeEvidences()) {
                    for (String instance : scope.getInstances()) {
                        Map<String, Measure> evidence = new HashMap<String, Measure>();
                        evidence.put("base", measureMap.get(instance));
                        if (filterComputer != null) {
                            evidence.put("filter", filterMap.get(instance));
                        }
                        measure.addEvidence(instance, evidence);
                    }
                }
            }
        }


        return result;
    }

    private Collection<Double> chooseMeasuresToAggregate(MeasureScope scope, Map<String, MeasureInstance> measureMap) {
        Collection<Double> toAggregate = new ArrayList<Double>();
        for (String instance : scope.getInstances()) {
            if (measureMap.containsKey(instance)) {
                toAggregate.add(measureMap.get(instance).getValue());
            }
        }
        return toAggregate;
    }

    private Map<String, MeasureInstance> buildMeasureMap(Collection<? extends Measure> measures) {
        return MeasureInstance.buildMeasureMap(measures);
    }

    // Filter instances whose filter value is false
    private Collection<String> filterTrueInstances(Collection<String> instances, Map<String, MeasureInstance> filterMap) {
        Collection<String> found = new ArrayList<String>();
        if (filterMap.size() > 0) {
            for (String instance : instances) {
                if (filterMap.get(instance).getValueAsBoolean()) {
                    found.add(instance);
                }
            }
        }
        return found;
    }

}
