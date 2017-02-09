package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;

/**
 * Agrupa los matchers en una unica interfaz
 * @author feserafim
 */
public interface Matcher {
    
    public boolean matches(LogEntry entry);
    
}
