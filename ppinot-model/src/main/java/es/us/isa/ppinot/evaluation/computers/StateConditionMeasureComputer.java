package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.matchers.StateConditionMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.StateConditionMeasure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StateConditionMeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class StateConditionMeasureComputer implements MeasureComputer {

    private Map<String,MeasureInstance> measures;
    private StateConditionMeasure definition;
    private StateConditionMatcher matcher;

    public StateConditionMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof StateConditionMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (StateConditionMeasure) definition;
        this.matcher = new StateConditionMatcher(this.definition.getCondition());
        this.measures = new HashMap<String, MeasureInstance>();
    }

    @Override
    public List<? extends Measure> compute() {
        return new ArrayList<Measure>(measures.values());
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstance m = getOrCreateMeasure(entry);

        if (matcher.matchesName(entry)) {
            if (matcher.matchesState(entry)) {
                m.setValue(1);
            } else {
                m.setValue(0);
            }
        }
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
