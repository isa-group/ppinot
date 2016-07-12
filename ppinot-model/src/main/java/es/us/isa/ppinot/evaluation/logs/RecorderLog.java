package es.us.isa.ppinot.evaluation.logs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * RecorderLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class RecorderLog extends AbstractLogProvider implements LogProvider, LogListener {
    private List<LogEntry> entries;

    public RecorderLog() {
        this.entries = new ArrayList<LogEntry>();
    }

    public void cleanLog() {
        this.entries = new ArrayList<LogEntry>();
    }

    public void addEvent(LogEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void processLog() {
        for (LogEntry entry: entries) {
            updateListeners(entry);
        }
    }

    @Override
    public void update(LogEntry entry) {
        addEvent(entry);
    }

    public void filter(Set<String> instances) {
        List<LogEntry> filteredLog = new ArrayList<LogEntry>();
        for (LogEntry e : entries) {
            if (instances.contains(e.getInstanceId())) {
                filteredLog.add(e);
            }
        }

        entries = filteredLog;
    }
}
