package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * TimeInstantMatcher
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeInstantMatcher {

    private TimeInstantCondition condition;

    public TimeInstantMatcher(TimeInstantCondition instantCondition) {
        this.condition = instantCondition;
    }

    public boolean matches(LogEntry entry) {
        return matchesName(entry) && matchesState(entry);
    }

    private boolean matchesState(LogEntry entry) {
        RuntimeState state = condition.getChangesToState();
        return StateMatcher.matches(entry.getEventType(), state);
    }

    private boolean matchesName(LogEntry entry) {
        return condition.getAppliesTo().equals(entry.getBpElement());
    }
}
