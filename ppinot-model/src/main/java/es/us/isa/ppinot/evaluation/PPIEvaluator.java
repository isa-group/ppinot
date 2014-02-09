package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.PPI;

import java.util.Collection;

/**
 * PPIEvaluator
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public interface PPIEvaluator {
    public Collection<Evaluation> eval(PPI ppi);
}
