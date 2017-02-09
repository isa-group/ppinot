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
public class StateConditionMeasureComputer extends AbstractBaseMeasureComputer<StateConditionMeasure> implements MeasureComputer {
    private StateConditionMatcher matcher;

    public StateConditionMeasureComputer(MeasureDefinition definition) {
        super(definition);
        this.matcher = new StateConditionMatcher(this.definition.getCondition());
    }

    @Override
    public void update(LogEntry entry) {
        MeasureInstance m = getOrCreateMeasure(entry, 0);

        if (matcher.matchesName(entry)) {
            if (matcher.matchesState(entry)) {
                m.setValue(1);
            } else {
                m.setValue(0);
            }
        }
    }

}
