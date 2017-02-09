package es.us.isa.ppinot.evaluation.logs;

import java.util.HashMap;
import java.util.Map;

/**
 * PreviousDataStateTransformer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class PreviousDataStateTransformer implements LogEntryTransformer {
    public final static String EXTENSION = "isa:PreviousDataState";

    private Map<String, Map<String, Object>> currentDataState;

    public PreviousDataStateTransformer() {
        currentDataState = new HashMap<String, Map<String, Object>>();
    }

    @Override
    public LogEntry transform(LogEntry entry) {
        String instanceId = entry.getInstanceId();

        Map<String, Object> prevInstanceState = getCurrentState(instanceId);

        entry.saveExtension(EXTENSION, prevInstanceState);

        updateCurrentState(entry, instanceId);

        return entry;
    }

    private Map<String, Object> getCurrentState(String instanceId) {
        Map<String, Object> prevInstanceState = currentDataState.get(instanceId);
        if (prevInstanceState == null) {
            prevInstanceState = new HashMap<String, Object>();
        }
        return prevInstanceState;
    }

    private void updateCurrentState(LogEntry entry, String instanceId) {
        Map<String, Object> prevInstanceState;
        prevInstanceState = new HashMap<String, Object>(entry.getData());
        currentDataState.put(instanceId, prevInstanceState);
    }
}
