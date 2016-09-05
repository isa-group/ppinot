package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.logs.LogProvider;
import org.joda.time.Interval;

/**
 * BigLogProvider
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
interface BigLogProvider {

    enum IntervalCondition {START, END, ACTIVE}

    void preprocessLog();
    LogProvider create(final Interval i, IntervalCondition condition);
    void close();
}
