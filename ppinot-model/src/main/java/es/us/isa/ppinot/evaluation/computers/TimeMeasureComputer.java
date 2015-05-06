package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import org.joda.time.*;

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
        private DurationWithExclusion duration;

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
            double value;

            if (duration != null)
                value = duration.getMillis();
            else {
                value = Double.NaN;
            }

            return value;
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                duration = new DurationWithExclusion(start, end, ((TimeMeasure)definition).getConsiderOnly());
            }
        }

        private boolean isRunning() {
            return start != null;
        }
    }

    private class CyclicMeasureInstanceTimer extends MeasureInstanceTimer {
        private DateTime start;
        private Collection<DurationWithExclusion> durations;
        private Aggregator aggregator;

        public CyclicMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
            durations = new ArrayList<DurationWithExclusion>();
            aggregator = new Aggregator(definition.getSingleInstanceAggFunction());
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;
        }

        @Override
        public double getValue() {
            double value;
            Collection<Double> measures = new ArrayList<Double>();
            for (DurationWithExclusion d : durations) {
                measures.add((double) d.getMillis());
            }

            if (isRunning() || measures.isEmpty()) {
                value = Double.NaN;
            } else {
                value = aggregator.aggregate(measures);
            }

            return value;
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                durations.add(new DurationWithExclusion(start, end, ((TimeMeasure) definition).getConsiderOnly()));
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


    private class DurationWithExclusion {
        private DateTime start;
        private DateTime end;
        private Schedule schedule;

        public DurationWithExclusion(DateTime start, DateTime end, Schedule schedule) {
            this.start = start;
            this.end = end;
            this.schedule = schedule;
        }

        public long getMillis() {
            long millis = new Duration(start, end).getMillis();

            if (schedule != null) {
                millis = millis - exclusion();
            }

            return millis;
        }

        private long exclusion() {
            return hourExclusion() + dayExclusion();
        }

        private long dayExclusion() {
            int daysPerWeek = 7 - (schedule.getEndDay() - schedule.getBeginDay() + 1);
            Weeks weeks = Weeks.weeksBetween(start, end);
            long exclusion = Days.days(daysPerWeek * weeks.getWeeks()).toStandardDuration().getMillis();

            if (schedule.dayOfWeekExcluded(start.getDayOfWeek())) {
                exclusion += Hours.hoursBetween(start, start.withTimeAtStartOfDay().plusDays(1)).toStandardDuration().getMillis();
            }

            if (schedule.dayOfWeekExcluded(end.getDayOfWeek())) {
                exclusion += Hours.hoursBetween(end.withTimeAtStartOfDay(), end).toStandardDuration().getMillis();
            }


            return exclusion;
        }

        private long hourExclusion() {
            return startDayExclusion() + endDayExclusion() + middleDaysExclusion();
        }

        private long startDayExclusion() {
            DateTime beginInDay = start.withFields(schedule.getBeginTime());
            DateTime endInDay = start.withFields(schedule.getEndTime());
            DateTime midnight = start.withTimeAtStartOfDay();
            Hours exclusionHours;

            if (start.isBefore(endInDay)) {
                exclusionHours = Hours.hoursBetween(endInDay, midnight.plusDays(1));
                if (start.isBefore(beginInDay)) {
                    exclusionHours.plus(Hours.hoursBetween(start, beginInDay));
                }
            } else {
                exclusionHours = Hours.hoursBetween(start, midnight.plusDays(1));
            }

            return exclusionHours.toStandardDuration().getMillis();
        }

        private long endDayExclusion() {
            DateTime beginInDay = end.withFields(schedule.getBeginTime());
            DateTime endInDay = end.withFields(schedule.getEndTime());
            DateTime midnight = end.withTimeAtStartOfDay();
            Hours exclusionHours;

            if (end.isBefore(beginInDay)) {
                exclusionHours = Hours.hoursBetween(midnight, end);
            } else {
                exclusionHours = Hours.hoursBetween(midnight, beginInDay);
                if (end.isAfter(endInDay)) {
                    exclusionHours.plus(Hours.hoursBetween(endInDay, end));
                }
            }

            return exclusionHours.toStandardDuration().getMillis();
        }

        private long middleDaysExclusion() {
            Hours exclusionHoursPerDay = Days.ONE.toStandardHours().minus(Hours.hoursBetween(schedule.getBeginTime(), schedule.getEndTime()));

            DateTime nextDay = start.withTimeAtStartOfDay().plusDays(1);
            DateTime endDay = end.withTimeAtStartOfDay();

            int days = Math.max(Days.daysBetween(nextDay, endDay).getDays(), 0);

            return days * exclusionHoursPerDay.toStandardDuration().getMillis();
        }

    }
}
