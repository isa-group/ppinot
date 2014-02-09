package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.*;

/**
 * TimeMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeMeasureComputer implements MeasureComputer {

    private TimeMeasure definition;
    private Map<String, MeasureInstanceTimer> measures;
    private TimeInstantMatcher startMatcher;
    private TimeInstantMatcher endMatcher;

    public TimeMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof TimeMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (TimeMeasure) definition;
        this.measures = new HashMap<String, MeasureInstanceTimer>();
        this.startMatcher = new TimeInstantMatcher(this.definition.getFrom());
        this.endMatcher = new TimeInstantMatcher(this.definition.getTo());
    }

    @Override
    public List<? extends Measure> compute() {
        List<MeasureInstance> computation = new ArrayList<MeasureInstance>(measures.values());
        return computation;
    }

    @Override
    public void update(LogEntry entry) {
        if (startMatcher.matches(entry)) {
            startTimer(entry);
        } else if (endMatcher.matches(entry)) {
            endTimer(entry);
        }
    }

    private void startTimer(LogEntry entry) {
        MeasureInstanceTimer m = getOrCreateTimer(entry);
        m.starts(entry.getTimeStamp());
    }

    private void endTimer(LogEntry entry) {
        MeasureInstanceTimer m = getOrCreateTimer(entry);
        m.ends(entry.getTimeStamp());
    }

    private MeasureInstanceTimer getOrCreateTimer(LogEntry entry) {
        String measureId = measureIdOf(entry);
        MeasureInstanceTimer m = measures.get(measureId);

        if (m == null) {
            if (TimeMeasureType.CYCLIC.equals(definition.getTimeMeasureType())) {
                m = new CyclicMeasureInstanceTimer(definition, entry.getProcessId(), entry.getInstanceId());
            } else {
                m = new LinearMeasureInstanceTimer(definition, entry.getProcessId(), entry.getInstanceId());
            }
            measures.put(measureId, m);
        }
        return m;
    }

    private String measureIdOf(LogEntry entry) {
        return entry.getProcessId() + "#" + entry.getInstanceId();
    }

    private abstract class MeasureInstanceTimer extends MeasureInstance {
        public abstract void starts(DateTime start);
        public abstract void ends(DateTime ends);

        public MeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, Double.NaN, processId, instanceId);
        }
    }

    private class LinearMeasureInstanceTimer extends MeasureInstanceTimer {

        private DateTime start;
        private Duration duration;

        public LinearMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;

            this.duration = null;
        }

        @Override
        public double getValue() {
            Duration value;

            if (duration != null)
                value = duration;
            else {
                value = new Duration(start, DateTime.now());
            }

            return value.getMillis();
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                duration = new Duration(start, end);
            }
        }

        private boolean isRunning() {
            return start != null;
        }
    }

    private class CyclicMeasureInstanceTimer extends MeasureInstanceTimer {
        private DateTime start;
        private Collection<Duration> durations;
        private Aggregator aggregator;

        public CyclicMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
            durations = new ArrayList<Duration>();
            aggregator = new Aggregator(definition.getSingleInstanceAggFunction());
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;
        }

        @Override
        public double getValue() {
            Collection<Double> measures = new ArrayList<Double>();
            for (Duration d : durations) {
                measures.add((double) d.getMillis());
            }

            if (isRunning()) {
                measures.add((double) new Duration(start, DateTime.now()).getMillis());
            }

            return aggregator.aggregate(measures);
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                durations.add(new Duration(start, end));
                reset();
            }
        }

        private void reset() {
            start = null;
        }

        private boolean isRunning() {
            return start != null;
        }

    }
}
