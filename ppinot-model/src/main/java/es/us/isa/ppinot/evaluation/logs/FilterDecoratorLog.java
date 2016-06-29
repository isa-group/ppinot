package es.us.isa.ppinot.evaluation.logs;

import java.util.Set;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class FilterDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Set<String> filteredInstances;

    public FilterDecoratorLog(LogProvider sourceLog, Set<String> filteredInstances) {
        this.filteredInstances = filteredInstances;
        this.sourceLog = sourceLog;

        sourceLog.registerListener(this);
    }

    @Override
    public void processLog() {
        sourceLog.processLog();
    }

    @Override
    public void update(LogEntry entry) {
        if (! filteredInstances.contains(entry.getInstanceId())) {
            updateListeners(entry);
        }
    }
}
