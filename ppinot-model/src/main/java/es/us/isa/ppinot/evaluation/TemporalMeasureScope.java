package es.us.isa.ppinot.evaluation;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * TemporalMeasureScope
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TemporalMeasureScope extends MeasureScope {
    private DateTime start;
    private DateTime end;

    public TemporalMeasureScope(String processId, DateTime start, DateTime end) {
        super(processId);
        this.start = start;
        this.end = end;
    }

    public TemporalMeasureScope(String processId, Collection<String> instances, DateTime start, DateTime end) {
        super(processId, instances);
        this.start = start;
        this.end = end;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }
}
