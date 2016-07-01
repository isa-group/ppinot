package es.us.isa.ppinot.evaluation.logs;

import es.us.isa.ppinot.evaluation.matchers.FlowElementStateMatcher;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.Interval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class MultiAggregatedDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Map<Interval, LogListener> listenersByInterval;
    private Set<String> unfinishedInstances;

    public MultiAggregatedDecoratorLog(LogProvider sourceLog) {
        this.sourceLog = sourceLog;
        this.listenersByInterval = new HashMap<Interval, LogListener>();
        this.unfinishedInstances = new HashSet<String>();
        sourceLog.registerListener(this);
    }

    public void registerListener(Interval i, LogListener l) {
        listenersByInterval.put(i, l);
    }

    @Override
    public void processLog() {
        sourceLog.processLog();
    }

    @Override
    public void update(LogEntry entry) {
        updateListeners(entry);
    }

    private boolean startsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                FlowElementStateMatcher.matches(entry.getEventType(), GenericState.START);
    }

    private boolean endsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                FlowElementStateMatcher.matches(entry.getEventType(), GenericState.END);
    }


    @Override
    public void registerListener(LogListener listener) {
        throw new IllegalArgumentException();
    }

    @Override
    protected LogEntry updateListeners(LogEntry entry) {
        LogEntry transformedEntry = super.updateListeners(entry);

        for (Interval i : listenersByInterval.keySet()) {
            if (transformedEntry.getTimeStamp().isBefore(i.getEnd())) {
                listenersByInterval.get(i).update(entry);
            }
        }

        return transformedEntry;
    }
}
