package es.us.isa.ppinot.evaluation.logs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * FinalActivityEndMatcher
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class FinalActivityEndMatcher implements EndMatcher {
    private Set<String> endElements;

    public FinalActivityEndMatcher(String... elements) {
        this.endElements = new HashSet<String>();
        Collections.addAll(this.endElements, elements);
    }

    @Override
    public boolean matches(LogEntry entry) {
        return LogEntry.ElementType.flowElement.equals(entry.getElementType()) &&
                LogEntry.EventType.complete.equals(entry.getEventType()) &&
                endElements.contains(entry.getBpElement());
    }
}
