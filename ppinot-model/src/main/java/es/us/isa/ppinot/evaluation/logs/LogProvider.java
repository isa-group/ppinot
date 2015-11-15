package es.us.isa.ppinot.evaluation.logs;

/**
 * LogProvider
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public interface LogProvider {
    public void registerListener(LogListener listener);
    public void registerEntryTransformer(LogEntryTransformer transformer);
    public void processLog();
}
