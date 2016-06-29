package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractBaseMeasureComputer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class AbstractBaseMeasureComputer<T extends MeasureDefinition> implements MeasureComputer {
    protected Map<String,MeasureInstance> measures;
    protected T definition;

    public AbstractBaseMeasureComputer(MeasureDefinition definition) {
        measures = new HashMap<String, MeasureInstance>();
        try {
            this.definition = (T) definition;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<? extends Measure> compute() {
        return new ArrayList<Measure>(measures.values());
    }

    @Override
    public abstract void update(LogEntry entry);

    protected MeasureInstance getMeasure(LogEntry entry) {
        String measureId = entry.getProcessId() + "#" + entry.getInstanceId();
        return measures.get(measureId);
    }

    protected MeasureInstance getOrCreateMeasure(LogEntry entry, Object defaultValue) {
        String measureId = entry.getProcessId() + "#" + entry.getInstanceId();
        MeasureInstance m = measures.get(measureId);

        if (m == null) {
            m = new MeasureInstance(definition, defaultValue, entry.getProcessId(), entry.getInstanceId());
            measures.put(measureId, m);
        }
        return m;
    }
}
