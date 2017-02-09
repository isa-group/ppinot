package es.us.isa.ppinot.evaluation.logs;

import es.us.isa.ppinot.evaluation.matchers.FlowElementStateMatcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class StartEndDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Set<String> instances;
    private Set<String> endElements;

    public StartEndDecoratorLog(LogProvider sourceLog, String... endElements) {
        this.instances = new HashSet<String>();
        this.sourceLog = sourceLog;
        this.endElements = new HashSet<String>();
        Collections.addAll(this.endElements, endElements);

        sourceLog.registerListener(this);
    }

    @Override
    public void processLog() {
        sourceLog.processLog();
    }

    @Override
    public void update(LogEntry entry) {
        if (! instances.contains(entry.getInstanceId())) {
            LogEntry startEvent = LogEntry.instance(entry.getProcessId(), entry.getInstanceId(), LogEntry.EventType.ready, entry.getTimeStamp());
            updateListeners(startEvent);
            instances.add(entry.getInstanceId());
        }

        updateListeners(entry);

        if (matchesEndElements(entry)) {
            LogEntry endEvent = LogEntry.instance(entry.getProcessId(), entry.getInstanceId(), LogEntry.EventType.complete, entry.getTimeStamp());
            updateListeners(endEvent);
        }
    }

    private boolean matchesEndElements(LogEntry entry) {
        return LogEntry.ElementType.flowElement.equals(entry.getElementType()) &&
                LogEntry.EventType.complete.equals(entry.getEventType()) &&
                endElements.contains(entry.getBpElement());
    }
}
