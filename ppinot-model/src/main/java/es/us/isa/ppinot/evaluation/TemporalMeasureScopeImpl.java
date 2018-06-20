package es.us.isa.ppinot.evaluation;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Map;

/**
 * TemporalMeasureScope
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TemporalMeasureScopeImpl extends MeasureScopeImpl implements TemporalMeasureScope {
    private DateTime start;
    private DateTime end;

    public TemporalMeasureScopeImpl(String processId, DateTime start, DateTime end) {
        super(processId);
        this.start = start;
        this.end = end;
    }

    public TemporalMeasureScopeImpl(String processId, Collection<String> instances, DateTime start, DateTime end) {
        super(processId, instances);
        this.start = start;
        this.end = end;
    }

    @Override
    public Map<String, Object> getScopeInfo() {
        Map<String,Object> scope = super.getScopeInfo();
        scope.put("start", start);
        scope.put("end", end);

        return scope;
    }

    @Override
    public boolean equivalentTo(MeasureScope scope) {
        if (! getInstances().equals(scope.getInstances())) return false;
        if (! getScopeInfo().keySet().equals(scope.getScopeInfo())) return false;

        for (Map.Entry<String, Object> entry : getScopeInfo().entrySet()) {
            if (! existsIdenticalEntry(entry, scope)) return false;
        }

        return true;
    }


    @Override
    public boolean isContainedIn(MeasureScope scope) {
        for (Map.Entry<String, Object> entry : getScopeInfo().entrySet()) {
            if (! scope.getScopeInfo().containsKey(entry.getKey())) return false;
            if (! existsIdenticalEntry(entry, scope)) return false;
        }

        if (!scope.getInstances().containsAll(getInstances())) return false;

        return true;
    }

    private boolean existsIdenticalEntry(Map.Entry<String, Object> entry, MeasureScope scope) {
        Object otherValue = scope.getScopeInfo().get(entry.getKey());
        if (entry.getValue() == null &&  otherValue != null) return false;
        if (entry.getValue() != null) {
            if ("start".equals(entry.getKey()) || "end".equals(entry.getKey())) {
                try {
                    DateTime other = (DateTime) otherValue;
                    DateTime mine = (DateTime) entry.getValue();
                    if (!mine.isEqual(other)) return false;
                } catch (Exception e) {
                    return false;
                }
            } else {
                if (!entry.getValue().equals(otherValue)) return false;

            }
        }
        return true;
    }

    @Override
    public DateTime getStart() {
        return start;
    }

    @Override
    public DateTime getEnd() {
        return end;
    }

    public String toString(){
        return "{start: " + start + ", end: " + end + " + , inst:" + getInstances() +"}";
    }
}
