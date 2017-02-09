package es.us.isa.ppinot.evaluation.logs;

import java.util.*;

/**
 * StartEndDecoratorLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class EndDecoratorLog extends AbstractLogProvider implements LogProvider, LogListener {
    private LogProvider sourceLog;
    private Map<String, LogEntry> finished;
    private EndMatcher endMatcher;
    private boolean unknownEnd;

    public EndDecoratorLog(LogProvider sourceLog, EndMatcher endMatcher) {
        this.sourceLog = sourceLog;
        this.endMatcher = endMatcher;
        this.unknownEnd = false;
        this.finished = new HashMap<String, LogEntry>();

        sourceLog.registerListener(this);
    }

    public EndDecoratorLog withUnknownEnd() {
        this.unknownEnd = true;
        return this;
    }

    @Override
    public void processLog() {
        sourceLog.processLog();

        if (unknownEnd) {
            for (LogEntry entry : finished.values()) {
                generateEventAndNotify(entry);
            }
        }
    }

    @Override
    public void update(LogEntry entry) {
        updateListeners(entry);

        if (unknownEnd) {
            if (endMatcher.matches(entry)) {
                finished.put(entry.getInstanceId(), entry);
            } else {
                finished.remove(entry.getInstanceId());
            }
        } else {
            if (endMatcher.matches(entry)) {
                generateEventAndNotify(entry);
            }
        }

    }

    private void generateEventAndNotify(LogEntry entry) {
        LogEntry endEvent = LogEntry.instance(entry.getProcessId(), entry.getInstanceId(), LogEntry.EventType.complete, entry.getTimeStamp()).withData(entry.getData());
        updateListeners(endEvent);
    }

}
