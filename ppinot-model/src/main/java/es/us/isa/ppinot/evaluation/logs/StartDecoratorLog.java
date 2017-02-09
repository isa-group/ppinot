package es.us.isa.ppinot.evaluation.logs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class StartDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Set<String> instances;

    public StartDecoratorLog(LogProvider sourceLog) {
        this.instances = new HashSet<String>();
        this.sourceLog = sourceLog;

        sourceLog.registerListener(this);
    }

    @Override
    public void processLog() {
        sourceLog.processLog();
    }

    @Override
    public void update(LogEntry entry) {
        if (! instances.contains(entry.getInstanceId())) {
            LogEntry startEvent = LogEntry.instance(entry.getProcessId(), entry.getInstanceId(), LogEntry.EventType.ready, entry.getTimeStamp()).withData(entry.getData());
            updateListeners(startEvent);
            instances.add(entry.getInstanceId());
        }

        updateListeners(entry);
    }
}
