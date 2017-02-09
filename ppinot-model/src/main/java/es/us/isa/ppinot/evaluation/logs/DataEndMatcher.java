package es.us.isa.ppinot.evaluation.logs;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DataEndMatcher
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataEndMatcher implements EndMatcher {
    private Set<Object> endElements;
    private String data;

    public DataEndMatcher(String key, Object... elements) {
        this.data = key;
        this.endElements = new HashSet<Object>();
        Collections.addAll(this.endElements, elements);
    }

    @Override
    public boolean matches(LogEntry entry) {
        Object currentData = entry.getData().get(data);
        return currentData != null && endElements.contains(currentData);
    }

}
