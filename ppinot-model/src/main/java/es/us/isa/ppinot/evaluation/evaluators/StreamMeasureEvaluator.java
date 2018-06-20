package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.Overrides;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * StreamMeasureEvaluator
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public interface StreamMeasureEvaluator {
    Iterator<Map<MeasureDefinition, List<Measure>>> evalStream(List<MeasureDefinition> definitions, ProcessInstanceFilter filter);
    Iterator<List<Measure>> evalStreamWithEvidences(MeasureDefinition aggregated, List<MeasureDefinition> evidences, ProcessInstanceFilter filter);
    Iterator<Map<MeasureDefinition, List<Measure>>> evalStream(List<MeasureDefinition> definitions, ProcessInstanceFilter filter, Overrides overrides);
    Iterator<List<Measure>> evalStreamWithEvidences(MeasureDefinition aggregated, List<MeasureDefinition> evidences, ProcessInstanceFilter filter, Overrides overrides);
}
