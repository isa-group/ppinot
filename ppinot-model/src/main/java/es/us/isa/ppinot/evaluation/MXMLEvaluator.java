package es.us.isa.ppinot.evaluation;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogListener;
import es.us.isa.ppinot.evaluation.logs.MXMLLog;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * MXMLEvaluator
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MXMLEvaluator implements PPIEvaluator {

    private static final Logger log = Logger.getLogger(MXMLEvaluator.class.getName());

    private InputStream stream;
    private Bpmn20ModelHandler bpmn20ModelHandler;

    public MXMLEvaluator(InputStream inputStream, Bpmn20ModelHandler bpmn20ModelHandler) {
        this.stream = inputStream;
        this.bpmn20ModelHandler = bpmn20ModelHandler;
    }

    @Override
    public Collection<Evaluation> eval(PPI ppi) {
        List<Evaluation> evaluations = new ArrayList<Evaluation>();

        MeasureDefinition definition = ppi.getMeasuredBy();

        MeasureComputer computer = new MeasureComputerFactory().create(definition, ppi.getScope());
        MXMLLog mxmlLog = new MXMLLog(stream, computer, bpmn20ModelHandler);

        mxmlLog.processLog();

        List<? extends Measure> measures = computer.compute();
        for (Measure m : measures) {
            evaluations.add(new Evaluation(ppi, m.getValue(), m.getMeasureScope()));
        }

        return evaluations;
    }

}
