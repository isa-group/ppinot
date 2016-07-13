package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.matchers.DataPropertyMatcher;
import es.us.isa.ppinot.evaluation.matchers.FlowElementStateMatcher;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.DataObjectState;
import es.us.isa.ppinot.model.state.GenericState;
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

    private boolean hasPrecondition;
    private TimeInstantMatcher startConditionMatcher;
    private TimeInstantMatcher endConditionMatcher;
    private DataPropertyMatcher propertyMatcher;

    private DateTime now;

    public TimeMeasureComputer(MeasureDefinition definition, ProcessInstanceFilter filter) {
        if (!(definition instanceof TimeMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (TimeMeasure) definition;
        this.measures = new HashMap<String, MeasureInstanceTimer>();
        this.startMatcher = new TimeInstantMatcher(this.definition.getFrom());
        this.endMatcher = new TimeInstantMatcher(this.definition.getTo());

        this.hasPrecondition = ((TimeMeasure) definition).getPrecondition() != null;

        if (hasPrecondition) {
            if (! TimeMeasureType.LINEAR.equals(((TimeMeasure) definition).getTimeMeasureType())) {
                throw new IllegalArgumentException("Preconditions can only be used with linear measures");
            }
            this.startConditionMatcher = new TimeInstantMatcher(new TimeInstantCondition("Data", new DataObjectState(((TimeMeasure) definition).getPrecondition().getRestriction())));
            this.endConditionMatcher = new TimeInstantMatcher(new TimeInstantCondition("Data", new DataObjectState("!("+((TimeMeasure) definition).getPrecondition().getRestriction()+")")));
            this.propertyMatcher = new DataPropertyMatcher(this.definition.getPrecondition());

        }

        if (filter != null && filter instanceof SimpleTimeFilter) {
            this.now = ((SimpleTimeFilter) filter).getUntil();
        } else {
            this.now = DateTime.now();
        }
    }

    public DateTime getNow() {
        return now;
    }

    public TimeMeasureComputer setNow(DateTime now) {
        this.now = now;
        return this;
    }

    private boolean endsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                FlowElementStateMatcher.matches(entry.getEventType(), GenericState.END);
    }


    @Override
    public List<? extends Measure> compute() {
        List<MeasureInstance> computation = new ArrayList<MeasureInstance>();
        for (MeasureInstanceTimer timer : measures.values()) {
            computation.add(new MeasureInstance(timer.getDefinition(), timer.getMeasureScope(), timer.getValue()));
        }
        return computation;
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstanceTimer m = getOrCreateTimer(entry);

        if (startMatcher.matches(entry)) {
            startTimer(m, entry);
        }
        if (endMatcher.matches(entry)) {
            endTimer(m, entry);
        }

        if (hasPrecondition) {
            updateTimer((PreconditionMeasureInstanceTimer) m, entry, startConditionMatcher.matches(entry), endConditionMatcher.matches(entry));
        }


        if (endsProcess(entry)) {
            processFinished(m);
        }
    }

    private void updateTimer(PreconditionMeasureInstanceTimer m, LogEntry entry, boolean startMatches, boolean endMatches) {
        m.update(entry, startMatches, endMatches);
    }

    private void processFinished(MeasureInstanceTimer m) {
        m.processFinished();
    }

    private void startTimer(MeasureInstanceTimer m, LogEntry entry) {
        m.starts(entry.getTimeStamp(), entry);
    }

    private void endTimer(MeasureInstanceTimer m, LogEntry entry) {
        m.ends(entry.getTimeStamp());
    }

    private MeasureInstanceTimer getOrCreateTimer(LogEntry entry) {
        String measureId = measureIdOf(entry);
        MeasureInstanceTimer m = measures.get(measureId);

        if (m == null) {
            if (hasPrecondition) {
                m = new PreconditionMeasureInstanceTimer(definition, entry.getProcessId(), entry.getInstanceId(), propertyMatcher);
            } else if (TimeMeasureType.CYCLIC.equals(definition.getTimeMeasureType())) {
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

    private abstract class MeasureInstanceTimer extends MeasureInstance  {
        private boolean processFinished = false;

        public MeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, Double.NaN, processId, instanceId);
        }

        public abstract void starts(DateTime start, LogEntry entry);
        public abstract void ends(DateTime ends);
        protected abstract double computeValue();

        protected boolean isProcessFinished() {
            return processFinished;
        }

        public void processFinished() {
            this.processFinished = true;
        }

        @Override
        public double getValue() {
            double value = computeValue();

            if (!Double.isNaN(value)) {
                long millisValue = (long) value;
                Duration duration = Duration.millis(millisValue);
                value = TimeUnit.toTimeUnit(duration, definition.getUnitOfMeasure());
            }

            return value;
        }

    }

    private class PreconditionMeasureInstanceTimer extends MeasureInstanceTimer {

        private DateTime start;
        private CyclicMeasureInstanceTimer current;
        private CyclicMeasureInstanceTimer backup = null;
        private DataPropertyMatcher matcher;


        public PreconditionMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId, DataPropertyMatcher matcher) {
            super(definition, processId, instanceId);
            current = new CyclicMeasureInstanceTimer(definition, processId, instanceId);
            this.matcher = matcher;
        }

        public void starts(DateTime start, LogEntry entry) {
            if (! isRunning()) {
                this.start = start;
                if (matcher.matches(entry)) {
                    current.starts(start, entry);
                }
            }
        }

        @Override
        protected double computeValue() {
            double value;

            if (backup != null)
                value = backup.computeValue();
            else {
                if (isRunning() && !isProcessFinished() && ((TimeMeasure) definition).isComputeUnfinished()) {
                    value = current.computeValue();
                } else {
                    value = Double.NaN;
                }
            }

            return value;
        }

        public void processFinished() {
            super.processFinished();
            current.processFinished();
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                backup = current.copy();
                backup.ends(end);
            }
        }

        private boolean isRunning() {
            return start != null;
        }

        public void update(LogEntry entry, boolean startMatches, boolean endMatches) {
            if (isRunning()) {
                if (startMatches) {
                    current.starts(entry.getTimeStamp(), entry);
                }

                if (endMatches) {
                    current.ends(entry.getTimeStamp());
                }
            }

        }

    }

    private class LinearMeasureInstanceTimer extends MeasureInstanceTimer {

        private DateTime start;
        private DurationWithExclusion duration;

        public LinearMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
        }

        public void starts(DateTime start, LogEntry entry) {
            if (! isRunning())
                this.start = start;

            this.duration = null;
        }

        @Override
        protected double computeValue() {
            double value;

            if (duration != null)
                value = duration.getMillis();
            else {
                if (isRunning() && !isProcessFinished() && ((TimeMeasure) definition).isComputeUnfinished()) {
                    value = new DurationWithExclusion(start, now, ((TimeMeasure)definition).getConsiderOnly()).getMillis();
                } else {
                    value = Double.NaN;
                }
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

        public void starts(DateTime start, LogEntry entry) {
            if (! isRunning())
                this.start = start;
        }

        @Override
        protected double computeValue() {
            double value;
            Collection<Double> measures = new ArrayList<Double>();
            for (DurationWithExclusion d : durations) {
                measures.add((double) d.getMillis());
            }

            if (isRunning()) {
                if (!isProcessFinished() && ((TimeMeasure) definition).isComputeUnfinished()) {
                    measures.add((double) new DurationWithExclusion(start, now, ((TimeMeasure) definition).getConsiderOnly()).getMillis());
                } else {
                    measures.clear();
                }
            }

            if (measures.isEmpty()) {
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

        public CyclicMeasureInstanceTimer copy() {
            CyclicMeasureInstanceTimer copy = new CyclicMeasureInstanceTimer((TimeMeasure) getDefinition(), getProcessId(), getInstanceId());
            if (isProcessFinished()) {
                copy.processFinished();
            }

            copy.start = this.start;
            copy.aggregator = new Aggregator(((TimeMeasure)getDefinition()).getSingleInstanceAggFunction());
            for (DurationWithExclusion d : durations) {
                copy.durations.add(d.copy());
            }

            return copy;
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

        public DurationWithExclusion copy() {
            return new DurationWithExclusion(start, end, schedule);
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
            long fullDay = DateTimeConstants.MILLIS_PER_DAY;
            long exclusionPerDay = Duration.standardDays(1).minus(Seconds.secondsBetween(schedule.getBeginTime(), schedule.getEndTime()).toStandardDuration()).getMillis();
            long exclusion = 0;

            DateTime nextDay = start.withTimeAtStartOfDay().plusDays(1);
            DateTime endDay = end.withTimeAtStartOfDay();
            int days = Math.max(Days.daysBetween(nextDay, endDay).getDays(), 0);

            int dayOfWeek = nextDay.getDayOfWeek();
            for (int i = 1; i <= days; i++) {
                if (schedule.dayOfWeekExcluded(dayOfWeek) || schedule.dayOfHolidayExcluded(nextDay.plusDays(i-1))) {
                    exclusion += fullDay;
                } else {
                    exclusion += exclusionPerDay;
                }
                dayOfWeek++;
                if (dayOfWeek > DateTimeConstants.DAYS_PER_WEEK) {
                    dayOfWeek = dayOfWeek % 7;
                }
            }

            return exclusion;
        }

        private long hourExclusion() {
            long exclusion = 0;

            if (sameDay(start, end)) {
                if (schedule.dayOfWeekExcluded(start.getDayOfWeek()) || schedule.dayOfHolidayExcluded(start)) {
                    exclusion = new Duration(start, end).getMillis();
                } else {
                    exclusion = oneDayExclusion(start, end);
                }
            } else {
                if (schedule.dayOfWeekExcluded(start.getDayOfWeek()) || schedule.dayOfHolidayExcluded(start)) {
                    exclusion += new Duration(start, start.withTimeAtStartOfDay().plusDays(1)).getMillis();
                } else {
                    exclusion += oneDayExclusion(start, start.withTimeAtStartOfDay().plusDays(1));
                }

                if (schedule.dayOfWeekExcluded(end.getDayOfWeek()) || schedule.dayOfHolidayExcluded(end)) {
                    exclusion += new Duration(end.withTimeAtStartOfDay(), end).getMillis();
                } else {
                    exclusion += oneDayExclusion(end.withTimeAtStartOfDay(), end);
                }
            }

            return exclusion;
        }

        private boolean sameDay(DateTime oneDay, DateTime anotherDay) {
            return oneDay.withTimeAtStartOfDay().equals(anotherDay.withTimeAtStartOfDay());
        }

        private long oneDayExclusion(DateTime start, DateTime end) {
            DateTime beginInDay = start.toDateTime(schedule.getTimeZone()).toLocalDate().toDateTime(schedule.getBeginTime(), schedule.getTimeZone());
            DateTime endInDay = start.toDateTime(schedule.getTimeZone()).toLocalDate().toDateTime(schedule.getEndTime(), schedule.getTimeZone());
            Duration exclusionHours = Duration.millis(0);

            if (end.isBefore(beginInDay) || start.isAfter(endInDay)) {
                exclusionHours = new Duration(start, end);
            } else {
                if (start.isBefore(beginInDay)) {
                    exclusionHours = exclusionHours.plus(new Duration(start, beginInDay));
                }
                if (end.isAfter(endInDay)) {
                    exclusionHours = exclusionHours.plus(new Duration(endInDay, end));
                }
            }

            return exclusionHours.getMillis();

        }



    }
}
