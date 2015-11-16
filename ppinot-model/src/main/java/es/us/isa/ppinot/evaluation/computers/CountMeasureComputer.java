package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.matchers.TimeInstantMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CountMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class CountMeasureComputer extends AbstractBaseMeasureComputer<CountMeasure> {

    private TimeInstantMatcher matcher;

    public CountMeasureComputer(MeasureDefinition definition) {
        super(definition);
        this.matcher = new TimeInstantMatcher(this.definition.getWhen());
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstance m = getOrCreateMeasure(entry, 0);

        if (matcher.matches(entry))
            m.setValue(m.getValue() + 1);
    }

}
