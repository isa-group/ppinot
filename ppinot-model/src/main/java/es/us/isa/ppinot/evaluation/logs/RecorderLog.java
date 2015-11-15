package es.us.isa.ppinot.evaluation.logs;

import java.util.ArrayList;
import java.util.List;

/**
 * RecorderLog
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class RecorderLog extends AbstractLogProvider implements LogProvider {
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
}
