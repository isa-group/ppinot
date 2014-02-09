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
public class CountMeasureComputer implements MeasureComputer {

    private Map<String,MeasureInstance> measures;
    private CountMeasure definition;
    private TimeInstantMatcher matcher;

    public CountMeasureComputer(MeasureDefinition definition) {
        if (! (definition instanceof CountMeasure)) {
            throw new IllegalArgumentException();
        }
        this.definition = (CountMeasure) definition;
        measures = new HashMap<String, MeasureInstance>();
        this.matcher = new TimeInstantMatcher(this.definition.getWhen());
    }

    @Override
    public List<? extends Measure> compute() {
        return new ArrayList<Measure>(measures.values());
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstance m = getOrCreateMeasure(entry);

        if (matcher.matches(entry))
            m.setValue(m.getValue() + 1);
    }

    private MeasureInstance getOrCreateMeasure(LogEntry entry) {
        String measureId = entry.getProcessId() + "#" + entry.getInstanceId();
        MeasureInstance m = measures.get(measureId);

        if (m == null) {
            m = new MeasureInstance(definition, 0, entry.getProcessId(), entry.getInstanceId());
            measures.put(measureId, m);
        }
        return m;
    }

}
