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
    private final List<MeasureComputer> listGroupByMeasureComputer;

    public AggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;

        this.listGroupByMeasureComputer = new ArrayList<MeasureComputer>();
        final MeasureComputerFactory measureComputerFactory = new MeasureComputerFactory();
        if (this.definition.getGroupedBy() != null) {
            for (DataContentSelection s : this.definition.getGroupedBy()) {
                DataMeasure dm = new DataMeasure();
                dm.setDataContentSelection(s);
                listGroupByMeasureComputer.add(measureComputerFactory.create(dm, filter));
            }
        }

        this.baseComputer = measureComputerFactory.create(this.definition.getBaseMeasure(), filter);
        this.classifier = new ScopeClassifierFactory().create(filter);
        this.agg = new Aggregator(this.definition.getAggregationFunction());
    }

    @Override
    public void update(LogEntry entry) {
        classifier.update(entry);
        for (MeasureComputer c : listGroupByMeasureComputer) {
            c.update(entry);
        }
        baseComputer.update(entry);
    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> result = new ArrayList<Measure>();
        Collection<? extends Measure> measures = baseComputer.compute();
        Map<String, MeasureInstance> measureMap = buildMeasureMap(measures);

        if (listGroupByMeasureComputer != null && listGroupByMeasureComputer.size() > 0) { // agrupar entrada del log

            // Mapa de instance y groupBy construido en función de los parámetros que se quieren agrupar
            Map<String, Map<String, String>> instanceGroupBy = new HashMap<String, Map<String, String>>(); // string -> instanceId
            for (MeasureComputer mc : listGroupByMeasureComputer) {
                DataMeasureComputer dmc = (DataMeasureComputer) mc;
                for (Measure measure : mc.compute()) {
                    MeasureInstance mi = (MeasureInstance) measure;
                    Map<String, String> measureGroup = new HashMap<String, String>(); // groupbyscope
                    measureGroup.put(dmc.definition.getDataContentSelection().getSelection(), mi.getValueAsString());
                    if (instanceGroupBy.containsKey(mi.getInstanceId())) {
                        instanceGroupBy.get(mi.getInstanceId()).putAll(measureGroup);
                    } else {
                        instanceGroupBy.put(mi.getInstanceId(), measureGroup);
                    }
                }
            }

            // Reverted map. De esta forma que consiguen las instancias de una agrupación
            Map<Map<String, String>, List<String>> groupByInstances = new HashMap<Map<String, String>, List<String>>();
            for (String instance : instanceGroupBy.keySet()) {
                List<String> values = new ArrayList<String>();
                if (groupByInstances.containsKey(instanceGroupBy.get(instance))) {
                    values = groupByInstances.get(instanceGroupBy.get(instance));
                    values.add(instance);
                    groupByInstances.put(instanceGroupBy.get(instance), values);
                } else {
                    values.add(instance);
                    groupByInstances.put(instanceGroupBy.get(instance), values);
                }
            }

            // Asocia un scope a cada instancia
            List<MeasureScope> listScopes = (List<MeasureScope>) classifier.listScopes();
            Map<String, MeasureScope> instanceMeasureScope = new HashMap<String, MeasureScope>();
            for (MeasureScope scope : listScopes) {
                for (String instance : scope.getInstances()) {
                    instanceMeasureScope.put(instance, scope);
                }
            }

            // Agrupa medidas usando GroupByMeasureScope
            for (Map<String, String> group : groupByInstances.keySet()) {
                for (MeasureScope scope : listScopes) {

                    List<String> groupedInstances = new ArrayList<String>(groupByInstances.get(group));
                    groupedInstances.retainAll(scope.getInstances());

                    if (!groupedInstances.isEmpty()) {
                        String instance = groupedInstances.iterator().next();
                        TemporalMeasureScope tempScope = (TemporalMeasureScope) instanceMeasureScope.get(instance);
                        GroupByMeasureScope groupByScope = new GroupByTemporalMeasureScopeImpl(tempScope.getProcessId(), groupedInstances, tempScope.getStart(), tempScope.getEnd());
                        groupByScope.setGroupParameters(group);

                        Collection<Double> toAggregate = new ArrayList<Double>();
                        for (String gi : groupedInstances) {
                            toAggregate.add(measureMap.get(gi).getValue());
                        }

                        double val = agg.aggregate(toAggregate);
                        result.add(new Measure(definition, groupByScope, val));
                    }

                }
            }

        } else {
            Collection<MeasureScope> scopes = classifier.listScopes();
            for (MeasureScope scope : scopes) {
                Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap);
                double val = agg.aggregate(toAggregate);
                result.add(new Measure(definition, scope, val));
            }
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
