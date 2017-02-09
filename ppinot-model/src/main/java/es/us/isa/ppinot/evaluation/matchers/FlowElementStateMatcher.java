package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.state.BPMNState;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FlowElementStateMatcher
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class FlowElementStateMatcher {
    private static Map<LogEntry.EventType, List<BPMNState>> BPMNMATCH;
    private static Map<LogEntry.EventType, List<GenericState>> GENERICMATCH;

    static {
        BPMNMATCH = new HashMap<LogEntry.EventType, List<BPMNState>>();
        BPMNMATCH.put(LogEntry.EventType.assign, Arrays.asList(BPMNState.ACTIVE, BPMNState.EXECUTING));
        BPMNMATCH.put(LogEntry.EventType.complete, Arrays.asList(
                BPMNState.COMPENSATED,
                BPMNState.COMPLETED,
                BPMNState.DELETED,
                BPMNState.FAILED,
                BPMNState.WITHDRAWN));

        GENERICMATCH = new HashMap<LogEntry.EventType, List<GenericState>>();
        GENERICMATCH.put(LogEntry.EventType.ready, Arrays.asList(GenericState.START));
        GENERICMATCH.put(LogEntry.EventType.assign, Arrays.asList(GenericState.START));
        GENERICMATCH.put(LogEntry.EventType.complete, Arrays.asList(GenericState.END));
    }

    public static boolean supports(RuntimeState state) {
        return state instanceof BPMNState || state instanceof GenericState;
    }

    public static boolean matches(LogEntry.EventType eventType, RuntimeState state) {
        boolean matches = false;
        if (state instanceof BPMNState)
            matches = BPMNMATCH.get(eventType).contains(state);
        else if (state instanceof GenericState)
        	matches = GENERICMATCH.get(eventType).contains(state);
        return matches;
    }
}
