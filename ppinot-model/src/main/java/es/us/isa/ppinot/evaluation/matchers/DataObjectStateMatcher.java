package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.state.DataObjectState;
import es.us.isa.ppinot.model.state.RuntimeState;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * DataObjectStateMatcher
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataObjectStateMatcher {
    public static boolean matches(LogEntry entry, RuntimeState state) {
        Serializable expression = getConditionExpression((DataObjectState) state);
        return ! matches(previousState(entry), expression) && matches(currentState(entry), expression);
    }

    private static Map<String, Object> previousState(LogEntry entry) {
        return (Map<String, Object>) entry.getExtension(PreviousDataStateTransformer.EXTENSION);
    }

    private static Serializable getConditionExpression(DataObjectState state) {
        return MVEL.compileExpression(state.getName());
    }

    private static boolean matches(Map<String, Object> data, Serializable expression) {
        boolean matches = false;
        try {
            Object value = MVEL.executeExpression(expression, data);
            if (value instanceof Boolean) {
                matches = (Boolean) value;
            }
        } catch (Exception e) {
            matches = false;
        }

        return matches;
    }

    public static boolean supports(RuntimeState state) {
        return state instanceof DataObjectState;
    }

    private static Map<String, Object> currentState(LogEntry entry) {
        return entry.getData();
    }
}
