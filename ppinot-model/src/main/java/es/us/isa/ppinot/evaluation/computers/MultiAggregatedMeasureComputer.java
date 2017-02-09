package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.FilterDecoratorLog;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.MultiAggregatedDecoratorLog;
import es.us.isa.ppinot.evaluation.logs.RecorderLog;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.joda.time.Interval;

import java.util.*;

/**
 * AggregatedMeasureComputer Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MultiAggregatedMeasureComputer implements MeasureComputer {

    private RecorderLog log;

    private AggregatedMeasure definition;
    private MeasureComputer filterComputer;
    private ScopeClassifier classifier;
    private SimpleTimeFilter filter;

    private Set<String> allInstanceId;

    public MultiAggregatedMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
        }

        if (!(filter instanceof SimpleTimeFilter)) {
            throw new IllegalArgumentException();
        }

        this.definition = (AggregatedMeasure) definition;
        this.filter = (SimpleTimeFilter) filter;

        this.allInstanceId = new HashSet<String>();
        final MeasureComputerFactory measureComputerFactory = new MeasureComputerFactory();
        if (this.definition.getFilter() != null) {
            this.filterComputer = measureComputerFactory.create(this.definition.getFilter(), filter);
        }
        this.classifier = new ScopeClassifierFactory().create(filter, this.definition.getPeriodReferencePoint());

        this.log = new RecorderLog();
    }

    @Override
    public void update(LogEntry entry) {
        log.addEvent(entry);
        classifier.update(entry);
        allInstanceId.add(entry.getInstanceId());

        if (filterComputer != null) {
            filterComputer.update(entry);
        }
    }

    @Override
    public List<? extends Measure> compute() {
        List<Measure> result = new ArrayList<Measure>();

        Set<String> filteredInstances = new HashSet<String>();

        if (filterComputer != null) {
            List<? extends Measure> filters = filterComputer.compute();
            Map<String, MeasureInstance> filterMap = buildMeasureMap(filters);
            filteredInstances = filterTrueInstances(allInstanceId, filterMap);
        }

        Collection<MeasureScope> temporalScopes = classifier.listScopes(definition.isIncludeUnfinished());
        Map<Interval, AggregatedMeasureComputer> computers = new HashMap<Interval, AggregatedMeasureComputer>();
        MultiAggregatedDecoratorLog aggregatedDecoratorLog = new MultiAggregatedDecoratorLog(new FilterDecoratorLog(log, filteredInstances));

        for (MeasureScope scope : temporalScopes) {
            TemporalMeasureScope temporalScope = (TemporalMeasureScope) scope;
            Interval i = new Interval(temporalScope.getStart(), temporalScope.getEnd());
            SimpleTimeFilter intervalFilter = filter.copy();
            intervalFilter.setUntil(i.getEnd().minus(1));
            AggregatedMeasureComputer computer = new AggregatedMeasureComputer(definition, intervalFilter);
            computers.put(i, computer);
            aggregatedDecoratorLog.registerListener(i, computer);
        }

        aggregatedDecoratorLog.processLog();

        for (Interval i : computers.keySet()) {
            MeasureComputer computer = computers.get(i);
            List<? extends Measure> measures = computer.compute();

            for (Measure m : measures) {
                TemporalMeasureScope measureScope = (TemporalMeasureScope) m.getMeasureScope();
                if (i.getStart().equals(measureScope.getStart()) && i.getEnd().equals(measureScope.getEnd())) {
                    result.add(m);
                }
            }
        }

        return result;
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

    // Filter instances whose filter value is true
    private Set<String> filterTrueInstances(Collection<String> instances, Map<String, MeasureInstance> filterMap) {
        Set<String> found = new HashSet<String>();
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
