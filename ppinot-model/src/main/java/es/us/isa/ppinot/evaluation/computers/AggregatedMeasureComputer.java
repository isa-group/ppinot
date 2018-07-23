package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import org.apache.commons.lang3.StringUtils;

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
    private ComputerConfig computerConfig;
    private OverriddenMeasures overrides;
    private ScopeClassifier classifier;
    private Aggregator agg;
    private final List<MeasureComputer> listGroupByMeasureComputer;

    public AggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        this(definition, new ComputerConfig(filter));
    }

    public AggregatedMeasureComputer(MeasureDefinition definition, ComputerConfig computerConfig) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;

        this.listGroupByMeasureComputer = new ArrayList<MeasureComputer>();
        final MeasureComputerFactory measureComputerFactory = new MeasureComputerFactory();

        if (this.definition.getGroupedBy() != null) {
            for (DataMeasure dm : this.definition.getGroupedBy()) {
                listGroupByMeasureComputer.add(measureComputerFactory.create(dm, computerConfig));
            }
        }

        this.baseComputer = measureComputerFactory.create(this.definition.getBaseMeasure(), computerConfig);
        this.agg = new Aggregator(this.definition.getAggregationFunction());

        if (this.definition.getFilter() != null) {
            this.filterComputer = measureComputerFactory.create(this.definition.getFilter(), computerConfig);
        }

        this.classifier = new ScopeClassifierFactory().create(computerConfig.getFilter(), this.definition.getPeriodReferencePoint());

        this.overrides = computerConfig.getOverrides(this.definition.getBaseMeasure());
        this.computerConfig = computerConfig;
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

            Map<String, Measure> overridedValuesForScope = overrides.getOverriddenValuesContainedInScope(scope);

            Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap, overridedValuesForScope);
            if (!toAggregate.isEmpty()) {
                double val = agg.aggregate(toAggregate);
                Measure measure = new Measure(definition, scope, val);
                result.add(measure);

                if (computerConfig.includeEvidences()) {

                    addEvidences(measure, measureMap, filterMap, scope, overridedValuesForScope);
                }
            }
        }


        return result;
    }

    private void addEvidences(Measure measure, Map<String, MeasureInstance> measureMap, Map<String, MeasureInstance> filterMap, MeasureScope scope, Map<String, Measure> overridedValuesForScope) {
        for (String instance : scope.getInstances()) {
            Map<String, Measure> evidence = new HashMap<String, Measure>();

            inheritEvidence(instance, evidence, measureMap.get(instance));

            String baseId = getId(definition.getBaseMeasure(), "base");

            if (! StringUtils.isBlank(baseId)) {
                if (overridedValuesForScope.containsKey(instance)) {
                    evidence.put(baseId, overridedValuesForScope.get(instance));
                    evidence.put(baseId+"#overridded", measureMap.get(instance));
                } else {
                    evidence.put(baseId, measureMap.get(instance));
                }
            }

            if (filterComputer != null) {
                inheritEvidence(instance, evidence, filterMap.get(instance));

                String filterId = getId(definition.getFilter(), "filter");

                if (!StringUtils.isBlank(filterId)) {
                    evidence.put(filterId, filterMap.get(instance));
                }
            }

            measure.addEvidence(instance, evidence);
        }
    }

    private void inheritEvidence(String instance, Map<String, Measure> evidence, Measure m) {
        if (computerConfig.isFlattenedEvidences()) {
            if (m.getEvidences() != null) {
                Map<String, Measure> inheritedEvidences = m.getEvidences().get(instance);
                if (inheritedEvidences != null) {
                    evidence.putAll(inheritedEvidences);
                }
                m.removeEvidence(instance);

            }
        }
    }

    private String getId(MeasureDefinition measureDefinition, String defaultValue) {
        String id = "";

        if (ComputerConfig.Evidences.ID.equals(computerConfig.getEvidences())) {
            id = measureDefinition.getId();
        } else if (ComputerConfig.Evidences.ALL.equals(computerConfig.getEvidences())) {
            if (StringUtils.isBlank(measureDefinition.getId())) {
                id = defaultValue + "$" + measureDefinition.hashCode();
            } else {
                id = measureDefinition.getId();
            }
        }

        return id;
    }


    private Collection<Double> chooseMeasuresToAggregate(MeasureScope scope, Map<String, MeasureInstance> measureMap, Map<String, Measure> overridedValues) {
        Collection<Double> toAggregate = new ArrayList<Double>();
        for (String instance : scope.getInstances()) {
            if (overridedValues.containsKey(instance)) {
                toAggregate.add(overridedValues.get(instance).getValue());
            } else  if (measureMap.containsKey(instance)) {
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
