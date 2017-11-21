package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.*;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.matchers.FlowElementStateMatcher;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.base.DurationMeasure;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.*;

import java.util.*;

/**
 * TimeMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class DurationMeasureComputer implements MeasureComputer {

    private DurationMeasure definition;
    private Map<String, MeasureInstanceTimer> measures;
    private TimeInstantMatcher startMatcher;
    private TimeInstantMatcher endMatcher;

    public DurationMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof DurationMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (DurationMeasure) definition;
        this.measures = new HashMap<String, MeasureInstanceTimer>();
        this.startMatcher = new TimeInstantMatcher(this.definition.getFrom());
        this.endMatcher = new TimeInstantMatcher(this.definition.getTo());
    }

    private boolean endsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                FlowElementStateMatcher.matches(entry.getEventType(), GenericState.END);
    }


    @Override
    public List<? extends Measure> compute() {
        List<MeasureInstance> computation = new ArrayList<MeasureInstance>();
        for (MeasureInstanceTimer timer : measures.values()) {
            computation.add(new MeasureInstance(timer.getDefinition(), timer.getMeasureScope(), timer.computeValue()));
        }
        return computation;
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstanceTimer m = getOrCreateTimer(entry);

        if (startMatcher.matches(entry)) {
            startTimer(m, entry);
        } else if (endMatcher.matches(entry)) {
            endTimer(m, entry);
        }

        if (endsProcess(entry)) {
            processFinished(m);
        }
    }

    private void processFinished(MeasureInstanceTimer m) {
        m.processFinished();
    }

    private void startTimer(MeasureInstanceTimer m, LogEntry entry) {
        m.starts(entry.getTimeStamp());
    }

    private void endTimer(MeasureInstanceTimer m, LogEntry entry) {
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

    private abstract class MeasureInstanceTimer {
        private boolean processFinished = false;
        protected DurationMeasure definition;
        private MeasureScope scope;

        public DurationMeasure getDefinition() {
            return definition;
        }

        public MeasureScope getMeasureScope() {
            return scope;
        }

        public abstract void starts(DateTime start);
        public abstract void ends(DateTime ends);
        protected abstract DateTime computeValue();

        public MeasureInstanceTimer(DurationMeasure definition, String processId, String instanceId) {
            this.definition = definition;
            this.scope = new MeasureScopeImpl(processId, Arrays.asList(instanceId));
        }

        protected boolean isProcessFinished() {
            return processFinished;
        }

        public void processFinished() {
            this.processFinished = true;
        }

    }

    private class LinearMeasureInstanceTimer extends MeasureInstanceTimer {

        private DateTime start;
        private IntervalsWithExclusion interval;
        private double durationDeadline;

        public LinearMeasureInstanceTimer(DurationMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
            durationDeadline = definition.getMinDuration();
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;


            this.interval = null;
        }

        @Override
        protected DateTime computeValue() {
            DateTime value = null;

            if (interval != null) {
                List<Interval> intervals = interval.getIntervals();
                Collections.sort(intervals, new IntervalStartComparator());

                double currentDuration = 0;

                for (Interval i: intervals) {
                    double intervalDuration = TimeUnit.toTimeUnit(i.toDuration(), definition.getUnitOfMeasure());

                    if (currentDuration + intervalDuration > durationDeadline) {
                        double extraDuration = durationDeadline - currentDuration;
                        value = i.getStart().plus(TimeUnit.toTimeUnit(extraDuration, definition.getUnitOfMeasure()));
                        break;
                    }  else {
                        currentDuration += intervalDuration;
                    }

                }
            }

            return value;
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                interval = new IntervalsWithExclusion(start, end, definition.getConsiderOnly());
            }
        }

        private boolean isRunning() {
            return start != null;
        }
    }

    private class IntervalStartComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval x, Interval y) {
            return x.getStart().compareTo(y.getStart());
        }
    }

    private class CyclicMeasureInstanceTimer extends MeasureInstanceTimer {
        private DateTime start;
        private Collection<IntervalsWithExclusion> intervals;
        private double durationDeadline;

        public CyclicMeasureInstanceTimer(DurationMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
            intervals = new ArrayList<IntervalsWithExclusion>();
            durationDeadline = definition.getMinDuration();
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;
        }

        @Override
        protected DateTime computeValue() {
            DateTime value = null;
            SortedSet<Interval> allIntervals = new TreeSet<Interval>(new IntervalStartComparator());

            for (IntervalsWithExclusion d : intervals) {
                allIntervals.addAll(d.getIntervals());
            }

            double currentDuration = 0;

            for (Interval i: allIntervals) {
                double intervalDuration = TimeUnit.toTimeUnit(i.toDuration(), definition.getUnitOfMeasure());

                if (currentDuration + intervalDuration > durationDeadline) {
                    double extraDuration = durationDeadline - currentDuration;
                    value = i.getStart().plus(TimeUnit.toTimeUnit(extraDuration, definition.getUnitOfMeasure()));
                    break;
                }  else {
                    currentDuration += intervalDuration;
                }

            }

            return value;
        }


        public void ends(DateTime end) {
            if (isRunning()) {
                intervals.add(new IntervalsWithExclusion(start, end,definition.getConsiderOnly()));
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


    private class IntervalsWithExclusion {
        private DateTime start;
        private DateTime end;
        private Schedule schedule;

        public IntervalsWithExclusion(DateTime start, DateTime end, Schedule schedule) {
            this.start = start;
            this.end = end;
            this.schedule = schedule;
        }

        public List<Interval> getIntervals() {
            List<Interval> intervals = new ArrayList<Interval>();

            if (schedule != null) {
                DateTime startInterval = start;
                while ((schedule.dayOfWeekExcluded(startInterval.getDayOfWeek()) || schedule.dayOfHolidayExcluded(startInterval)) && ! sameDay(startInterval, end) ) {
                    startInterval = start.withTimeAtStartOfDay().plusDays(1);
                }

                DateTime endInterval = end;
                while ((schedule.dayOfWeekExcluded(endInterval.getDayOfWeek()) || schedule.dayOfHolidayExcluded(endInterval)) && !sameDay(startInterval, endInterval)) {
                    endInterval = end.minusDays(1).millisOfDay().withMaximumValue();
                }

                if (sameDay(startInterval, endInterval)) {
                    if (schedule.dayOfWeekExcluded(startInterval.getDayOfWeek()) || schedule.dayOfHolidayExcluded(startInterval)) {
                        // No interval
                    } else {
                        intervals.add(oneDayInterval(startInterval, endInterval));
                    }
                } else {
                    intervals.add(oneDayInterval(startInterval, startInterval.millisOfDay().withMaximumValue()));
                    DateTime nextDay = startInterval.withTimeAtStartOfDay().plusDays(1);

                    while (!sameDay(nextDay, endInterval)) {
                        if (! schedule.dayOfWeekExcluded(nextDay.getDayOfWeek()) && ! schedule.dayOfHolidayExcluded(nextDay)) {
                            intervals.add(oneDayInterval(nextDay, nextDay.millisOfDay().withMaximumValue()));
                        }
                        nextDay.plusDays(1);
                    }

                    intervals.add(oneDayInterval(endInterval.withTimeAtStartOfDay(), endInterval));
                }

            } else {
                intervals.add(new Interval(start, end));
            }


            return intervals;
        }



        private boolean sameDay(DateTime oneDay, DateTime anotherDay) {
            return oneDay.withTimeAtStartOfDay().equals(anotherDay.withTimeAtStartOfDay());
        }

        private Interval oneDayInterval(DateTime start, DateTime end) {
            DateTime beginInDay = start.withFields(schedule.getBeginTime());
            DateTime endInDay = start.withFields(schedule.getEndTime());
            Interval interval = null;


            if (end.isBefore(beginInDay) || start.isAfter(endInDay)) {
                // No interval in this case
            } else {
                DateTime startInterval = start;
                DateTime endInterval = end;
                if (start.isBefore(beginInDay)) {
                    startInterval = beginInDay;
                }
                if (end.isAfter(endInDay)) {
                    endInterval = endInDay;
                }
                interval = new Interval(startInterval, endInterval);
            }

            return interval;

        }



    }
}
