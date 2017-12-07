package es.us.isa.ppinot.evaluation.selectors;

import es.us.isa.ppinot.evaluation.logs.LogEntry;

/**
 * DataContentSelector
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public interface DataContentSelector {
    Object select(LogEntry entry);
}
