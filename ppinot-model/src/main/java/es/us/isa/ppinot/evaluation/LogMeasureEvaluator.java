package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.LogConfigurer;
import es.us.isa.ppinot.evaluation.logs.LogProvider;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

import java.util.*;
import java.util.logging.Logger;

/**
 * LogMeasureEvaluator Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class LogMeasureEvaluator implements MeasureEvaluator {

    private static final Logger log = Logger.getLogger(LogMeasureEvaluator.class.getName());

    private LogProvider logProvider;
    private LogConfigurer configurer;
    private MeasureComputerFactory factory;

    public LogMeasureEvaluator(LogProvider logProvider) {
        this.logProvider = logProvider;
        this.factory = new MeasureComputerFactory();
    }

    public LogConfigurer getConfigurer() {
        return configurer;
    }

    public LogMeasureEvaluator setConfigurer(LogConfigurer configurer) {
        this.configurer = configurer;
        return this;
    }

    @Override
    public List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter) {
        return eval(Arrays.asList(definition), filter).get(definition);
    }

    public Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ProcessInstanceFilter filter) {
        LogProvider logToAnalyse;
        Map<MeasureDefinition, List<Measure>> measures = new HashMap<MeasureDefinition, List<Measure>>();
        Map<MeasureComputer, MeasureDefinition> computers = new HashMap<MeasureComputer, MeasureDefinition>();

        if (configurer == null) {
            logToAnalyse = logProvider;
        } else {
            logToAnalyse = configurer.configure(logProvider);
        }

        for (MeasureDefinition definition : definitions) {
            MeasureComputer computer = factory.create(definition, filter);
            computers.put(computer, definition);
            logToAnalyse.registerListener(computer);
            measures.put(definition, new ArrayList<Measure>());
        }

        log.info("Processing log...");
        logToAnalyse.processLog();

        log.info("Computing measures...");
        for (MeasureComputer computer : computers.keySet()) {
            measures.get(computers.get(computer)).addAll(computer.compute());
        }

        return measures;
    }

}
