package es.us.isa.ppinot.evaluation.computers;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

/**
 * MeasureComputerFactory
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MeasureComputerFactory {
    public MeasureComputer create(MeasureDefinition definition, ProcessInstanceFilter filter) {
        MeasureComputer computer = null;

        if (definition instanceof TimeMeasure) {
            computer = new TimeMeasureComputer(definition);
        } else if (definition instanceof CountMeasure) {
            computer = new CountMeasureComputer(definition);
        } else if (definition instanceof StateConditionMeasure) {
            computer = new StateConditionMeasureComputer(definition);
        } else if (definition instanceof DataMeasure) {
            computer = new DataMeasureComputer(definition);
        } else if (definition instanceof AggregatedMeasure) {
            computer = new AggregatedMeasureComputer(definition, filter);
        } else if (definition instanceof DerivedMeasure) {
            computer = new DerivedMeasureComputer(definition, filter);
        }

        return computer;

    }
}
