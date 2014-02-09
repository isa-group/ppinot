package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.logs.LogListener;

import java.util.Collection;
import java.util.List;

/**
 * MeasureComputer
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public interface MeasureComputer extends LogListener {
    public List<? extends Measure> compute();
}
