package es.us.isa.ppinot.evaluation.logs;

import java.util.*;

/**
 * DataEndMatcher
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataEndMatcher implements EndMatcher {
    private Map<String, Set<Object>> endElements;

    public DataEndMatcher() {
        endElements = new HashMap<String, Set<Object>>();
    }

    public DataEndMatcher(String key, Object... elements) {
        this();
        add(key, elements);
    }

    public DataEndMatcher add(String key, Object... elements) {
        return add(key, Arrays.asList(elements));
    }

    public DataEndMatcher add(String key, Collection<Object> elements) {
        endElements.put(key, new HashSet<Object>(elements));
        return this;
    }

    @Override
    public boolean matches(LogEntry entry) {
        boolean matches = true;
        for (Map.Entry<String, Set<Object>> entries : endElements.entrySet()) {
            Object currentData = entry.getData().get(entries.getKey());
            matches = matches && currentData != null && entries.getValue().contains(currentData);
        }

        return matches;
    }

}
