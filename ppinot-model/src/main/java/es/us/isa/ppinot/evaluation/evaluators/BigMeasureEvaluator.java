package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.computers.AggregatedMeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.LogConfigurer;
import es.us.isa.ppinot.evaluation.logs.LogProvider;
import es.us.isa.ppinot.evaluation.scopes.IntervalsComputer;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.joda.time.Interval;

import java.util.*;
import java.util.logging.Logger;

/**
 * BigMeasureEvaluator
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class BigMeasureEvaluator implements MeasureEvaluator, StreamMeasureEvaluator {
    private static final Logger log = Logger.getLogger(BigMeasureEvaluator.class.getName());
    private BigLogProvider bigLogProvider;

    private LogConfigurer configurer;
    private MeasureComputerFactory factory;

    private class BigComputerFactory extends MeasureComputerFactory {
        @Override
        public MeasureComputer create(MeasureDefinition definition, ProcessInstanceFilter filter) {
            if (definition instanceof AggregatedMeasure) {
                return new AggregatedMeasureComputer(definition, filter);
            } else {
                return super.create(definition, filter);
            }
        }
    }

    public BigMeasureEvaluator(BigLogProvider bigLogProvider) {
        this.bigLogProvider = bigLogProvider;
        this.factory = new BigComputerFactory();
        bigLogProvider.preprocessLog();
    }

    public LogConfigurer getConfigurer() {
        return configurer;
    }

    public BigMeasureEvaluator setConfigurer(LogConfigurer configurer) {
        this.configurer = configurer;
        return this;
    }

    @Override
    public List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter) {
        return eval(Arrays.asList(definition), filter).get(definition);
    }

    public Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ProcessInstanceFilter filter) {
        Map<MeasureDefinition, List<Measure>> measures = new HashMap<MeasureDefinition, List<Measure>>();
        for (MeasureDefinition def : definitions) {
            measures.put(def, new ArrayList<Measure>());
        }


        Iterator<Map<MeasureDefinition, List<Measure>>> iterator = new IntervalIterator(definitions, (SimpleTimeFilter) filter);

        while (iterator.hasNext()) {
            Map<MeasureDefinition, List<Measure>> intervalMeasures = iterator.next();
            for (Map.Entry<MeasureDefinition, List<Measure>> entry : measures.entrySet()) {
                entry.getValue().addAll(intervalMeasures.get(entry.getKey()));
            }
        }


        return measures;
    }

    public void close() {
        bigLogProvider.close();
    }

    @Override
    public Iterator<Map<MeasureDefinition, List<Measure>>> evalStream(List<MeasureDefinition> definitions, ProcessInstanceFilter filter) {
        return  new IntervalIterator(definitions, (SimpleTimeFilter) filter);
    }

    @Override
    public Iterator<List<Measure>> evalStreamWithEvidences(MeasureDefinition aggregated, List<MeasureDefinition> evidences, ProcessInstanceFilter filter) {
        return new EvidenceIterator(aggregated, new IntervalIterator(aggregated, evidences, (SimpleTimeFilter) filter));
    }

    public static class EvidenceIterator implements Iterator<List<Measure>> {
        private Iterator<Map<MeasureDefinition, List<Measure>>> iterator;
        private MeasureDefinition aggregated;

        public EvidenceIterator(MeasureDefinition aggregated, Iterator<Map<MeasureDefinition, List<Measure>>> iterator) {
            this.iterator = iterator;
            this.aggregated = aggregated;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public List<Measure> next() {
            Map<MeasureDefinition, List<Measure>> measuresMap = iterator.next();
            Map<String, Map<String, Measure>> evidences = new HashMap<String, Map<String, Measure>>();
            for (MeasureDefinition definition : measuresMap.keySet()) {
                if (definition != aggregated) {
                    List<Measure> measures = measuresMap.get(definition);
                    String id = definition.getId();
                    if (id == null || id.isEmpty()) {
                        id = Integer.toString(definition.hashCode());
                    }

                    for (Measure m : measures) {
                        String instance = m.getInstances().iterator().next();
                        Map<String, Measure> values = evidences.get(instance);
                        if (values == null) {
                            values = new HashMap<String, Measure>();
                            evidences.put(instance, values);
                        }
                        values.put(id, m);
                    }

                }
            }

            List<Measure> aggregatedMeasures = measuresMap.get(aggregated);
            for (Measure m : aggregatedMeasures) {
                for (String instance : m.getInstances()) {
                    final Map<String, Measure> evidence = evidences.get(instance);
                    if (evidence == null) {
                        log.warning("No evidences for instance: " + instance);
                    }
                    m.addEvidence(instance, evidence);
                }
            }

            return aggregatedMeasures;
        }
    }

    public class IntervalIterator implements Iterator<Map<MeasureDefinition, List<Measure>>> {
        private Iterator<Interval> intervals;
        private final List<MeasureDefinition> measuresClose;
        private final List<MeasureDefinition> measuresActive;
        private SimpleTimeFilter filter;

        public IntervalIterator(MeasureDefinition aggregated, List<MeasureDefinition> evidences, SimpleTimeFilter filter) {
            this.intervals = IntervalsComputer.listIntervals(filter).iterator();
            this.filter = filter;
            if (IntervalsComputer.measuresProcessEnd(filter, aggregated)) {
                measuresClose = new ArrayList<MeasureDefinition>(evidences);
                measuresClose.add(aggregated);
                measuresActive = new ArrayList<MeasureDefinition>();
            } else {
                measuresActive = new ArrayList<MeasureDefinition>(evidences);
                measuresActive.add(aggregated);
                measuresClose = new ArrayList<MeasureDefinition>();
            }

        }

        public IntervalIterator(List<MeasureDefinition> definitions, SimpleTimeFilter filter) {
            this.intervals = IntervalsComputer.listIntervals(filter).iterator();
            this.filter = filter;
            measuresClose = filterClosedInInterval(definitions, filter);
            measuresActive = new ArrayList<MeasureDefinition>(definitions);
            measuresActive.removeAll(measuresClose);
        }

        @Override
        public boolean hasNext() {
            return intervals.hasNext();
        }

        @Override
        public Map<MeasureDefinition, List<Measure>> next() {
            Map<MeasureDefinition, List<Measure>> measures = new HashMap<MeasureDefinition, List<Measure>>();
            Interval i = intervals.next();
            computeInterval(i, BigLogProvider.IntervalCondition.END, measuresClose, filter, measures);
            computeInterval(i, BigLogProvider.IntervalCondition.ACTIVE, measuresActive, filter, measures);

            return measures;
        }
    }


    private void computeInterval(Interval i, BigLogProvider.IntervalCondition condition, List<MeasureDefinition> definitions, ProcessInstanceFilter filter, Map<MeasureDefinition, List<Measure>> measures) {
        LogProvider logToAnalyse;
        if (definitions.isEmpty()) {
            return;
        }

        logToAnalyse = bigLogProvider.create(i, condition);
        if (configurer != null) {
            logToAnalyse = configurer.configure(logToAnalyse);
        }

        log.info("Analysing log for interval: "+i.toString());
        Map<MeasureComputer, MeasureDefinition> computers = analyseLog(definitions, filter, logToAnalyse, i);

        computeMeasures(measures, computers, i);
    }

    private Map<MeasureComputer, MeasureDefinition> analyseLog(List<MeasureDefinition> definitions, ProcessInstanceFilter filter, LogProvider logToAnalyse, Interval i) {
        Map<MeasureComputer, MeasureDefinition> computers = new HashMap<MeasureComputer, MeasureDefinition>();
        SimpleTimeFilter intervalFilter = ((SimpleTimeFilter) filter).copy();
        intervalFilter.setUntil(i.getEnd());
        for (MeasureDefinition definition : definitions) {
            MeasureComputer computer = factory.create(definition, intervalFilter);
            computers.put(computer, definition);
            logToAnalyse.registerListener(computer);
        }

        logToAnalyse.processLog();
        return computers;
    }

    private void computeMeasures(Map<MeasureDefinition, List<Measure>> measures, Map<MeasureComputer, MeasureDefinition> computers, Interval i) {
        log.info("Computing measures for interval: " + i.toString());
        for (MeasureComputer computer : computers.keySet()) {
            MeasureDefinition definition = computers.get(computer);
            List<Measure> currentMeasures = measures.get(definition);
            if (currentMeasures == null) {
                currentMeasures = new ArrayList<Measure>();
                measures.put(definition, currentMeasures);
            }

            List<? extends Measure> computedMeasures = computer.compute();

            if (isAggregated(definition)) {
                computedMeasures = IntervalsComputer.filterByInterval(computedMeasures, i);
            }

            currentMeasures.addAll(computedMeasures);
        }
    }

    private List<MeasureDefinition> filterClosedInInterval(List<MeasureDefinition> definitions, SimpleTimeFilter filter) {
        List<MeasureDefinition> filtered = new ArrayList<MeasureDefinition>();
        for (MeasureDefinition def : definitions) {
            if (! isAggregated(def) || IntervalsComputer.measuresProcessEnd(filter, def)) {
                filtered.add(def);
            }
        }
        return filtered;
    }

    private boolean isAggregated(MeasureDefinition definition) {
        return (definition instanceof AggregatedMeasure || definition instanceof DerivedMultiInstanceMeasure);
    }

}
