package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.LogProvider;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

import java.util.List;
import java.util.logging.Logger;

/**
 * LogMeasureEvaluator
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class LogMeasureEvaluator implements MeasureEvaluator {
    private static final Logger log = Logger.getLogger(LogMeasureEvaluator.class.getName());

    private LogProvider logProvider;

    public LogMeasureEvaluator(LogProvider logProvider) {
        this.logProvider = logProvider;
    }

    @Override
    public List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter) {
        MeasureComputer computer = new MeasureComputerFactory().create(definition, filter);
        logProvider.registerListener(computer);
        logProvider.registerEntryTransformer(new PreviousDataStateTransformer());

        logProvider.processLog();

        return (List<Measure>) computer.compute();
    }

}