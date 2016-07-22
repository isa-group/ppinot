package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

import java.util.List;
import java.util.Map;

/**
 * MeasureEvaluator
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public interface MeasureEvaluator {
    public List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter);
    public Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ProcessInstanceFilter filter);
}
