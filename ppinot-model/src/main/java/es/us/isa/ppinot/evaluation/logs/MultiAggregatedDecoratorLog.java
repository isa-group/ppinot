package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.Interval;

import java.util.HashMap;
import java.util.Map;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class MultiAggregatedDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Map<Interval, LogListener> listenersByInterval;

    public MultiAggregatedDecoratorLog(LogProvider sourceLog) {
        this.sourceLog = sourceLog;
        this.listenersByInterval = new HashMap<Interval, LogListener>();
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
