package es.us.isa.ppinot.evaluation.matchers;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.PreviousDataStateTransformer;
import es.us.isa.ppinot.model.state.DataObjectState;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DataObjectStateMatcherTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataObjectStateMatcherTest {

    private LogEntry entry;

    @Before
    public void setup() {
        PreviousDataStateTransformer transformer = new PreviousDataStateTransformer();
        transformer.transform(createLogEntry1());
        entry = transformer.transform(createLogEntry2());
    }

    @Test
    public void testMatches() throws Exception {
        DataObjectState state = new DataObjectState("B == \"a\"");
        assertTrue(DataObjectStateMatcher.matches(entry, state));
    }

    @Test
    public void testNotMatchesNotExists() {
        DataObjectState state = new DataObjectState("J == \"v\"");
        assertFalse(DataObjectStateMatcher.matches(entry, state));
    }

    @Test
    public void testNotMatchesRemainsTheSame() {
        DataObjectState state = new DataObjectState("A == \"v\"");
        assertFalse(DataObjectStateMatcher.matches(entry, state));
    }

    @Test
    public void testNotMatchesNotValidState() {
        DataObjectState state = new DataObjectState("B == \"c\"");
        assertFalse(DataObjectStateMatcher.matches(entry, state));
    }

    private LogEntry createLogEntry1() {
        LogEntry logEntry = LogEntry.flowElement("0", "0", "0", LogEntry.EventType.complete, DateTime.now());
        logEntry.withData("A", "v").withData("B", "d");
        return logEntry;
    }

    private LogEntry createLogEntry2() {
        LogEntry logEntry = LogEntry.flowElement("0", "0", "1", LogEntry.EventType.complete, DateTime.now());
        logEntry.withData("A", "v").withData("B", "a").withData("C", "d");
        return logEntry;
    }

}