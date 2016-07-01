package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.TemporalMeasureScopeImpl;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.*;

/**
 * TimeScopeClassifier
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeScopeClassifier extends ScopeClassifier {

    private SimpleTimeFilter filter;
    private SortedSet<ProcessInstance> instancesSet;

    public TimeScopeClassifier(SimpleTimeFilter filter) {
        super();
        this.filter = filter;
        this.instancesSet = new TreeSet<ProcessInstance>(new EndInstanceComparator());
    }

    @Override
    public Collection<MeasureScope> listScopes(boolean isIncludeUnfinished) {
        Collection<MeasureScope> scopes;

        if (filter.isIncludeUnfinished() || isIncludeUnfinished) {
            addUnfinishedInstances();
        }

        if (instancesSet.isEmpty()) {
            scopes = Collections.EMPTY_LIST;
        } else if (filter.isRelative()) {
            scopes = listRelativeScopes();
        } else {
            scopes = listAbsoluteScopes();
        }

        return scopes;
    }

    private void addUnfinishedInstances() {
        DateTime now = filter.getUntil();

        for (ProcessInstance pi : getUnfinishedInstances()) {
            ProcessInstance adaptedPi = new ProcessInstance(pi.getProcessId(), pi.getInstanceId(), pi.getStart());
            adaptedPi.ends(now);
            instancesSet.add(adaptedPi);
        }
    }

    private Collection<MeasureScope> listRelativeScopes() {
        Collection<MeasureScope> scopes = new ArrayList<MeasureScope>();
        Period period = buildPeriod();
        DateTime currentDate = instancesSet.first().getEnd();
        Interval currentInterval = new Interval(currentDate, period);
        List<ProcessInstance> current = new ArrayList<ProcessInstance>();
        
        for (ProcessInstance instance : instancesSet) {
            DateTime ends = instance.getEnd();
            if (currentInterval.isBefore(ends)) {
                scopes.add(new TemporalMeasureScopeImpl(
                        instance.getProcessId(),
                        instanceToId(current),
                        currentInterval.getStart(),
                        currentInterval.getEnd()));

                while (currentInterval.isBefore(ends)) {
                    currentDate = currentDate.plus(period);
                    currentInterval = new Interval(currentDate, period);
                }

                while (!current.isEmpty() && currentInterval.isAfter(current.get(0).getEnd())) {
                    current.remove(0);
                }
            }
            current.add(instance);
        }
        
        scopes.add(new TemporalMeasureScopeImpl(instancesSet.first().getProcessId(),instanceToId(current),currentInterval.getStart(),currentInterval.getEnd()));

        return scopes;
    }

    private Collection<MeasureScope> listAbsoluteScopes() {
        Collection<MeasureScope> scopes = new ArrayList<MeasureScope>();
        Period period = buildPeriod();
        DateTime currentDate = buildStartDate(instancesSet.first().getEnd());
        DateTime lastDate = instancesSet.last().getEnd();
        String processId = instancesSet.first().getProcessId();

        if (lastDate.isBefore(currentDate.plus(period))) {
            scopes.add(new TemporalMeasureScopeImpl(processId, instanceToId(instancesSet),currentDate,currentDate.plus(period)));
        } else {
            Interval i = new Interval(currentDate, period);
            Collection<String> current = new ArrayList<String>();

            for (ProcessInstance instance : instancesSet) {
                if (i.contains(instance.getEnd())) {
                    current.add(instance.getInstanceId());
                } else {
                    if (!current.isEmpty()) {
                    	scopes.add(new TemporalMeasureScopeImpl(processId, current,currentDate,currentDate.plus(period)));
                        current = new ArrayList<String>();
                    }

                    while (i.isBefore(instance.getEnd())) {
                        currentDate = currentDate.plus(period);
                        i = new Interval(currentDate, period);
                    }

                    current.add(instance.getInstanceId());
                }
            }
            if (!current.isEmpty()) {
            	scopes.add(new TemporalMeasureScopeImpl(processId, current,currentDate,currentDate.plus(period)));
                current = new ArrayList<String>();
            }
        }

        return scopes;
    }

    private Period buildPeriod() {
        Period period;

        if (es.us.isa.ppinot.model.scope.Period.DAILY.equals(filter.getPeriod())) {
            period = Period.days(filter.getFrequency());
        } else if (es.us.isa.ppinot.model.scope.Period.WEEKLY.equals(filter.getPeriod())) {
            period = Period.weeks(filter.getFrequency());
        } else if (es.us.isa.ppinot.model.scope.Period.MONTHLY.equals(filter.getPeriod())) {
            period = Period.months(filter.getFrequency());
        } else {
            period = Period.years(filter.getFrequency());
        }

        return period.minusMillis(1);
    }

    private DateTime buildStartDate(DateTime firstInstance) {
        DateTime startDate;

        if (es.us.isa.ppinot.model.scope.Period.DAILY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstance.getHourOfDay()) {
                firstInstance.minusDays(1);
            }
            startDate = firstInstance.withHourOfDay(filter.getAbsoluteStart());
        } else if (es.us.isa.ppinot.model.scope.Period.WEEKLY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstance.getDayOfWeek()) {
                firstInstance.minusWeeks(1);
            }
            startDate = firstInstance.withDayOfWeek(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        } else if (es.us.isa.ppinot.model.scope.Period.MONTHLY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstance.getMonthOfYear()) {
                firstInstance.minusMonths(1);
            }
            startDate = firstInstance.withDayOfMonth(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        } else {
            if (filter.getAbsoluteStart() > firstInstance.getYear()) {
                firstInstance.minusYears(1);
            }
            startDate = firstInstance.withDayOfYear(filter.getAbsoluteStart()).withTimeAtStartOfDay();
        }

        return startDate;
    }

    private Collection<String> instanceToId(Collection<ProcessInstance> instances) {
        Collection<String> ids = new ArrayList<String>(instances.size());
        for (ProcessInstance i : instances) {
            ids.add(i.getInstanceId());
        }
        return ids;
    }

    @Override
    protected void instanceEnded(ProcessInstance instance) {
        instancesSet.add(instance);
    }

    private class EndInstanceComparator implements Comparator<ProcessInstance>  {
        @Override
        public int compare(ProcessInstance instance, ProcessInstance instance2) {
            DateTime end1 = instance.getEnd();
            DateTime end2 = instance2.getEnd();
            //if there are a instance with the same date add the new isntaces after
            if (end1.compareTo(end2)==0)
            	return 1;
            else
            	return end1.compareTo(end2);
        }
    }

}
