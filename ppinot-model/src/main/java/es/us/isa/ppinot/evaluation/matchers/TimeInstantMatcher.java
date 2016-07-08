package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.ComplexState;
import es.us.isa.ppinot.model.state.RuntimeState;

import java.util.HashMap;
import java.util.Map;

/**
 * TimeInstantMatcher
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeInstantMatcher implements Matcher {

    private TimeInstantCondition condition;
    private DataPropertyMatcher precondition = null;
    private Matcher matcher;

    public TimeInstantMatcher(TimeInstantCondition instantCondition) {
        this.condition = instantCondition;
        if (this.condition.getPrecondition() != null) {
            precondition = new DataPropertyMatcher(this.condition.getPrecondition());
        }

        RuntimeState state = condition.getChangesToState();

        if (FlowElementStateMatcher.supports(state)) {
            matcher = new FlowMatcher(state,condition.getAppliesTo());
        } else if (DataObjectStateMatcher.supports(state)){
            matcher = new DataMatcher(state);
        } else {
            matcher = new ComplexMatcher(state);
        }


    }


    @Override
    public boolean matches(LogEntry entry) {
        boolean result = matcher.matches(entry);

        if (precondition != null && !precondition.matches(entry)) {
            return false;
        }

        return result;
    }

    private class FlowMatcher implements Matcher {
        private RuntimeState state;
        private String flowName;

        public FlowMatcher(RuntimeState state, String flowName) {
            this.state = state;
            this.flowName = flowName;
        }

        @Override
        public boolean matches(LogEntry entry) {
            return matchesFlowName(entry) && matchesFlowState(entry);
        }

        private boolean matchesFlowState(LogEntry entry) {
            return FlowElementStateMatcher.matches(entry.getEventType(), state);
        }

        private boolean matchesFlowName(LogEntry entry) {
            return flowName.equals(entry.getBpElement());
        }
    }

    private class DataMatcher implements  Matcher {
        private RuntimeState state;

        public DataMatcher(RuntimeState state) {
            this.state = state;
        }

        @Override
        public boolean matches(LogEntry entry) {
            return matchesDataState(entry);
        }

        private boolean matchesDataState(LogEntry entry) {
            return DataObjectStateMatcher.matches(entry, state);
        }
    }


    private class ComplexMatcher implements Matcher {
        private ComplexState state;
        private TimeInstantMatcher first;
        private TimeInstantMatcher second;
        private Map<String, Boolean> mapStates;

        public ComplexMatcher(RuntimeState state) {
            this.state = (ComplexState) state;
            this.mapStates = new HashMap<String, Boolean>();
            this.first = new TimeInstantMatcher(this.state.getFirst());
            this.second = new TimeInstantMatcher(this.state.getLast());
        }

        @Override
        public boolean matches(LogEntry entry) {
            boolean result = false;
            boolean firstMatches = first.matches(entry);
            boolean secondMatches = second.matches(entry);
            Boolean currentState = mapStates.get(entry.getInstanceId());

            if (currentState != null && currentState && secondMatches) {
                result = true;
                if (state.getType().equals(ComplexState.Type.LEADSTOCYCLIC)) {
                    mapStates.put(entry.getInstanceId(), false);
                }
            }

            if (firstMatches) {
                mapStates.put(entry.getInstanceId(), true);
            } else {
                if (state.getType().equals(ComplexState.Type.FOLLOWS)) {
                    mapStates.put(entry.getInstanceId(), false);
                }
            }

            return result;
        }
    }
}
