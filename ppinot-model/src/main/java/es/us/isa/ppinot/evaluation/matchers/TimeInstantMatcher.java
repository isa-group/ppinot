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
        boolean matches = false;
        RuntimeState state = condition.getChangesToState();

        if (FlowElementStateMatcher.supports(state)) {
            matches = matchesFlowName(entry) && matchesFlowState(entry);
        } else {
            matches = matchesDataState(entry);
        }

        return matches;
    }

    private boolean matchesDataState(LogEntry entry) {
        RuntimeState state = condition.getChangesToState();
        return DataObjectStateMatcher.matches(entry, state);
    }

    private boolean matchesFlowState(LogEntry entry) {
        RuntimeState state = condition.getChangesToState();
        return FlowElementStateMatcher.matches(entry.getEventType(), state);
    }

    private boolean matchesFlowName(LogEntry entry) {
        return condition.getAppliesTo().equals(entry.getBpElement());
    }
}
