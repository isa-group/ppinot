package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.computers.ComputerConfig;
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
    List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter);
    List<Measure> eval(MeasureDefinition definition, ComputerConfig computerConfig);
    
    Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ProcessInstanceFilter filter);
    Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ComputerConfig computerConfig);
}
