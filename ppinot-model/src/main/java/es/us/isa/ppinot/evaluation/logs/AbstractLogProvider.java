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
    private List<LogEntryTransformer> transfomers;

    public AbstractLogProvider() {
        listeners = new ArrayList<LogListener>();
        transfomers = new ArrayList<LogEntryTransformer>();
    }

    @Override
    public void registerListener(LogListener listener) {
        listeners.add(listener);
    }

    @Override
    public void registerEntryTransformer(LogEntryTransformer transformer) {
        transfomers.add(transformer);
    }

    protected LogEntry updateListeners(LogEntry entry) {
        LogEntry transformedEntry = entry;


        for (LogEntryTransformer transformer : transfomers) {
            transformedEntry = transformer.transform(transformedEntry);
        }
      

        for (LogListener listener : listeners) {
            listener.update(transformedEntry);
        }

        return transformedEntry;
    }
}
