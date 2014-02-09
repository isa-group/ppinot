package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * StateConditionMatcher
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class StateConditionMatcher {

    private StateCondition condition;

    public StateConditionMatcher(StateCondition condition) {
        this.condition = condition;
    }

    public boolean matches(LogEntry entry) {
        return matchesName(entry) && matchesState(entry);
    }

    public boolean matchesState(LogEntry entry) {
        RuntimeState state = condition.getState();
        return StateMatcher.matches(entry.getEventType(), state);
    }

    public boolean matchesName(LogEntry entry) {
        return condition.getAppliesTo().equals(entry.getBpElement());
    }
}
