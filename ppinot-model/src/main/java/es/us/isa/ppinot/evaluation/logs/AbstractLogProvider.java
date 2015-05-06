package es.us.isa.ppinot.evaluation.logs;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractLogProvider
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class AbstractLogProvider implements LogProvider {
    private List<LogListener> listeners;

    public AbstractLogProvider() {
        listeners = new ArrayList<LogListener>();
    }

    @Override
    public void registerListener(LogListener listener) {
        listeners.add(listener);
    }

    protected void updateListeners(LogEntry entry) {
        for (LogListener listener : listeners) {
            listener.update(entry);
        }
    }
}
