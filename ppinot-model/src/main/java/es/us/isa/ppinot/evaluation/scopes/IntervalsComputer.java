package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.TemporalMeasureScope;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

/**
 * IntervalsComputer
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class IntervalsComputer {
    public static List<Interval> listIntervals(SimpleTimeFilter filter) {

        if (filter.isRelative()) {
            return listRelativeIntervals(filter);
        } else {
            return listAbsoluteIntervals(filter);
        }
    }

    public static boolean measuresProcessEnd(SimpleTimeFilter filter, MeasureDefinition measure) {
        AggregatedMeasure aggregatedMeasure = extractAggregatedFrom(measure);
        if (aggregatedMeasure != null) {
            filter = completeFilter(filter, aggregatedMeasure);
        }

        return !(filter.isIncludeUnfinished() || filter.getReferencePoint() != null);

    }

    public static List<? extends Measure> filterByInterval(List<? extends Measure> measures, Interval interval) {
        List<Measure> filteredMeasures = new ArrayList<Measure>();
        for (Measure m : measures) {
            if (m.getMeasureScope() instanceof TemporalMeasureScope) {
                TemporalMeasureScope scope = (TemporalMeasureScope) m.getMeasureScope();
                if (scope.getStart().equals(interval.getStart()) && scope.getEnd().equals(interval.getEnd())) {
                    filteredMeasures.add(m);
                }
            }
        }

        return filteredMeasures;
    }

    private static List<Interval> listAbsoluteIntervals(SimpleTimeFilter filter) {
        List<Interval> intervals = new ArrayList<Interval>();
        Period period = es.us.isa.ppinot.model.scope.Period.toJodaPeriod(filter.getPeriod(), filter.getFrequency());

        DateTime currentDate = buildStartDate(filter);
        DateTime lastDate = filter.getUntil();

        Interval interval = new Interval(currentDate, currentDate.plus(period).minusMillis(1));
        while (lastDate.isAfter(interval.getEnd())) {
            intervals.add(interval);
            currentDate = currentDate.plus(period);
            interval = new Interval(currentDate, currentDate.plus(period).minusMillis(1));
        }
        intervals.add(interval);

        return intervals;
    }

    private static DateTime buildStartDate(SimpleTimeFilter filter) {
        DateTime firstInstance = filter.getFrom();
        DateTime startDate;
        DateTime firstInstanceInZone = firstInstance.withZone(filter.getTimeZone());

        if (es.us.isa.ppinot.model.scope.Period.DAILY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstanceInZone.getHourOfDay()) {
                firstInstanceInZone = firstInstanceInZone.minusDays(1);
            }
            startDate = firstInstanceInZone.withHourOfDay(filter.getAbsoluteStart());
        } else if (es.us.isa.ppinot.model.scope.Period.WEEKLY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstanceInZone.getDayOfWeek()) {
                firstInstanceInZone = firstInstanceInZone.minusWeeks(1);
            }
            startDate = firstInstanceInZone.withDayOfWeek(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        } else if (es.us.isa.ppinot.model.scope.Period.MONTHLY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstanceInZone.getDayOfMonth()) {
                firstInstanceInZone = firstInstanceInZone.minusMonths(1);
            }
            startDate = firstInstanceInZone.withDayOfMonth(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        } else {
            if (filter.getAbsoluteStart() > firstInstanceInZone.getYear()) {
                firstInstanceInZone = firstInstanceInZone.minusYears(1);
            }
            startDate = firstInstanceInZone.withDayOfYear(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        }

        return startDate;
    }

    private static List<Interval> listRelativeIntervals(SimpleTimeFilter filter) {
        throw new IllegalArgumentException();
    }

    private static SimpleTimeFilter completeFilter(SimpleTimeFilter filter, AggregatedMeasure aggregatedMeasure) {
        SimpleTimeFilter referenceFilter = filter.copy();
        if (aggregatedMeasure.getPeriodReferencePoint() != null) {
            referenceFilter.setReferencePoint(aggregatedMeasure.getPeriodReferencePoint());
        }
        referenceFilter.setIncludeUnfinished(aggregatedMeasure.isIncludeUnfinished());

        return referenceFilter;
    }

    private static AggregatedMeasure extractAggregatedFrom(MeasureDefinition measure) {
        AggregatedMeasure aggregatedMeasure = null;

        if (measure instanceof AggregatedMeasure) {
            aggregatedMeasure = (AggregatedMeasure) measure;
        } else if (measure instanceof DerivedMultiInstanceMeasure) {
            DerivedMultiInstanceMeasure derived = (DerivedMultiInstanceMeasure) measure;
            for (String key : derived.getUsedMeasureMap().keySet()) {
                aggregatedMeasure = extractAggregatedFrom(derived.getUsedMeasureId(key));
                break;
            }
        }

        return aggregatedMeasure;
    }
}
