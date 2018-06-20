package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.TemporalMeasureScopeImpl;
import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
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
    private MeasureComputer computer;

    public TimeScopeClassifier(SimpleTimeFilter filter) {
        super();
        this.filter = filter;
        this.instancesSet = new TreeSet<ProcessInstance>(new ReferenceInstanceComparator());
        if (filter.getReferencePoint() != null) {
            this.computer = new MeasureComputerFactory().create(filter.getReferencePoint(), filter.copy().setReferencePoint(null));
        }
    }

    @Override
    public Collection<MeasureScope> listScopes(boolean isIncludeUnfinished) {
        Collection<MeasureScope> scopes;

        if (filter.isIncludeUnfinished() || isIncludeUnfinished) {
            addUnfinishedInstances();
        }

        if (this.computer != null) {
            mergeReferencePoints(this.computer.compute());
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

    private void mergeReferencePoints(List<? extends Measure> measures) {
        SortedSet<ProcessInstance> instances = new TreeSet<ProcessInstance>(new ReferenceInstanceComparator());
        Map<String, MeasureInstance> measureMap = MeasureInstance.buildMeasureMap(measures);
        for (ProcessInstance pi : instancesSet) {
            DateTime reference = measureMap.get(pi.getInstanceId()).getValueAsDateTime();
            pi.setReference(reference);
            instances.add(pi);
        }

        this.instancesSet = instances;
    }

    @Override
    protected void updateEntry(LogEntry entry) {
        super.updateEntry(entry);

        if (this.computer != null) {
            this.computer.update(entry);
        }
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
        DateTime currentDate = instancesSet.first().getReference();
        Interval currentInterval = new Interval(currentDate, period);
        List<ProcessInstance> current = new ArrayList<ProcessInstance>();
        
        for (ProcessInstance instance : instancesSet) {
            DateTime ends = instance.getReference();
            if (currentInterval.isBefore(ends)) {
                scopes.add(new TemporalMeasureScopeImpl(
                        instance.getProcessId(),
                        instanceToId(current),
                        currentInterval.getStart(),
                        currentInterval.getEnd().minusMillis(1)));

                while (currentInterval.isBefore(ends)) {
                    currentDate = currentDate.plus(period);
                    currentInterval = new Interval(currentDate, period);
                }

                while (!current.isEmpty() && currentInterval.isAfter(current.get(0).getReference())) {
                    current.remove(0);
                }
            }
            current.add(instance);
        }
        
        scopes.add(new TemporalMeasureScopeImpl(instancesSet.first().getProcessId(),instanceToId(current),currentInterval.getStart(),currentInterval.getEnd().minusMillis(1)));

        return scopes;
    }

    private Collection<MeasureScope> listAbsoluteScopes() {
        Collection<MeasureScope> scopes = new ArrayList<MeasureScope>();
        Period period = buildPeriod();
        DateTime currentDate = filter.getFrom() == null ?
                buildStartDate(instancesSet.first().getReference()) :
                buildStartDate(filter.getFrom());

        DateTime lastDate = instancesSet.last().getReference();
        String processId = instancesSet.first().getProcessId();

        if (lastDate.isBefore(currentDate.plus(period))) {
            scopes.add(new TemporalMeasureScopeImpl(processId, instanceToId(instancesSet),currentDate,currentDate.plus(period).minusMillis(1)));
        } else {
            Interval i = new Interval(currentDate, period);
            Collection<String> current = new ArrayList<String>();

            for (ProcessInstance instance : instancesSet) {
                if (i.isAfter(instance.getReference())) {
                    continue;
                } else if (i.contains(instance.getReference())) {
                    current.add(instance.getInstanceId());
                } else {
                    if (!current.isEmpty()) {
                    	scopes.add(new TemporalMeasureScopeImpl(processId, current,currentDate,currentDate.plus(period).minusMillis(1)));
                        current = new ArrayList<String>();
                    }

                    while (i.isBefore(instance.getReference())) {
                        currentDate = currentDate.plus(period);
                        i = new Interval(currentDate, period);
                    }

                    current.add(instance.getInstanceId());
                }
            }
            if (!current.isEmpty()) {
            	scopes.add(new TemporalMeasureScopeImpl(processId, current,currentDate,currentDate.plus(period).minusMillis(1)));
            }
        }

        return scopes;
    }

    private Period buildPeriod() {
        return es.us.isa.ppinot.model.scope.Period.toJodaPeriod(filter.getPeriod(), filter.getFrequency());
    }

    private DateTime buildStartDate(DateTime firstInstance) {
        DateTime startDate;
        DateTime firstInstanceInZone = firstInstance.withZone(filter.getTimeZone());

        if (es.us.isa.ppinot.model.scope.Period.DAILY.equals(filter.getPeriod())) {
            if (filter.getAbsoluteStart() > firstInstanceInZone.getHourOfDay()) {
                firstInstanceInZone = firstInstanceInZone.minusDays(1);
            }
            startDate = firstInstanceInZone.withHourOfDay(filter.getAbsoluteStart()).withTimeAtStartOfDay();
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

    private class ReferenceInstanceComparator implements Comparator<ProcessInstance>  {
        @Override
        public int compare(ProcessInstance instance, ProcessInstance instance2) {
            DateTime ref1 = instance.getReference();
            DateTime ref2 = instance2.getReference();
            //if there are a instance with the same date add the new isntaces after
            if (ref1.compareTo(ref2)==0)
            	return 1;
            else
            	return ref1.compareTo(ref2);
        }
    }

}
