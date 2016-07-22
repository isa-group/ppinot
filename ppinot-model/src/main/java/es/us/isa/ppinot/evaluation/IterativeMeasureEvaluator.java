package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.computers.MeasureComputer;
import es.us.isa.ppinot.evaluation.computers.MeasureComputerFactory;
import es.us.isa.ppinot.evaluation.logs.*;
import es.us.isa.ppinot.evaluation.matchers.FlowElementStateMatcher;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.*;
import java.util.logging.Logger;

/**
 * LogMeasureEvaluator Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class IterativeMeasureEvaluator implements MeasureEvaluator, LogListener {

    private static final Logger log = Logger.getLogger(IterativeMeasureEvaluator.class.getName());

    private LogProvider logProvider;
    private LogConfigurer configurer;
    private DateTime start;
    private MeasureComputerFactory factory;

    private RecorderLog currentLog;
    private Interval currentInterval;
    private Period period;
    private List<MeasureDefinition> definitions;
    private SimpleTimeFilter timeFilter;
    private Map<MeasureDefinition, List<Measure>> results;

    private Set<String> unfinishedInstances;

    public IterativeMeasureEvaluator(LogProvider logProvider, DateTime start) {
        this.logProvider = logProvider;
        logProvider.registerListener(this);
        this.start = start;
        this.factory = new MeasureComputerFactory();

    }

    public LogConfigurer getConfigurer() {
        return configurer;
    }

    public IterativeMeasureEvaluator setConfigurer(LogConfigurer configurer) {
        this.configurer = configurer;
        return this;
    }

    @Override
    public List<Measure> eval(MeasureDefinition definition, ProcessInstanceFilter filter) {
        return eval(Arrays.asList(definition), filter).get(definition);
    }

    public Map<MeasureDefinition, List<Measure>> eval(List<MeasureDefinition> definitions, ProcessInstanceFilter filter) {
        if (! (filter instanceof SimpleTimeFilter)){
            throw new IllegalArgumentException();
        }

        this.timeFilter = (SimpleTimeFilter) filter;
        this.period = es.us.isa.ppinot.model.scope.Period.toJodaPeriod(timeFilter.getPeriod(), timeFilter.getFrequency());
        this.definitions = definitions;

        this.currentLog = new RecorderLog();
        this.currentInterval = new Interval(start, start.plus(period).minusMillis(1));
        this.unfinishedInstances = new HashSet<String>();
        this.results = initResults(definitions);

        logProvider.registerListener(this);

        logProvider.processLog();

        return results;
    }

    private Map<MeasureDefinition, List<Measure>> initResults(List<MeasureDefinition> definitions) {
        Map<MeasureDefinition, List<Measure>> results = new HashMap<MeasureDefinition, List<Measure>>();
        for (MeasureDefinition d : definitions) {
            results.put(d, new ArrayList<Measure>());
        }

        return results;
    }

    private boolean endsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                FlowElementStateMatcher.matches(entry.getEventType(), GenericState.END);
    }

    @Override
    public void update(LogEntry entry) {
        if (entry.getTimeStamp().isBefore(currentInterval.getEnd())) {
            currentLog.addEvent(entry);
        } else {
            RecorderLog unfinishedLog = processCurrentInterval();
            prepareNextInterval(unfinishedLog);

            currentLog.addEvent(entry);
        }
    }

    private void prepareNextInterval(RecorderLog unfinishedLog) {
        DateTime newStart = currentInterval.getEnd().plusMillis(1);
        currentInterval = new Interval(newStart, newStart.plus(period).minusMillis(1));
        currentLog = unfinishedLog;

    }

    private RecorderLog processCurrentInterval() {
        LogProvider logToAnalyse;
        if (configurer == null) {
            logToAnalyse = currentLog;
        } else {
            logToAnalyse = configurer.configure(currentLog);
        }

        Map<MeasureComputer, MeasureDefinition> computers = registerDefinitions(logToAnalyse);
        logToAnalyse.registerListener(new LogListener() {
            @Override
            public void update(LogEntry entry) {
                if (endsProcess(entry)) {
                    unfinishedInstances.remove(entry.getInstanceId());
                } else {
                    unfinishedInstances.add(entry.getInstanceId());
                }
            }
        });
        RecorderLog unfinishedLog = new RecorderLog();
        logToAnalyse.registerListener(unfinishedLog);

        logToAnalyse.processLog();

        storeResults(computers);

        unfinishedLog.filter(unfinishedInstances);
        return unfinishedLog;

    }

    private void storeResults(Map<MeasureComputer, MeasureDefinition> computers) {
        for (MeasureComputer computer : computers.keySet()) {
            results.get(computers.get(computer)).addAll(computer.compute());
        }
    }

    private Map<MeasureComputer, MeasureDefinition> registerDefinitions(LogProvider logToAnalyse) {
        Map<MeasureComputer, MeasureDefinition> computers = new HashMap<MeasureComputer, MeasureDefinition>();
        for (MeasureDefinition definition : definitions) {
            MeasureComputer computer = factory.create(definition, timeFilter);
            computers.put(computer, definition);
            logToAnalyse.registerListener(computer);
        }

        return computers;
    }


}
