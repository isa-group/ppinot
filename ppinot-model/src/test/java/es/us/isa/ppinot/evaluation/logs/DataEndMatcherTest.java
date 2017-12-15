package es.us.isa.ppinot.evaluation.logs;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * DataEndMatcherTest
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataEndMatcherTest {

    @Test
    public void testMatches() throws Exception {
        DataEndMatcher endMatcher = new DataEndMatcher("state", "Close", "Cancel");
        LogEntry entry1 = createLogEntry().withData("state", "Open").withData("value", "x");
        LogEntry entry2 = createLogEntry().withData("state", "Close").withData("value", "x");
        LogEntry entry3 = createLogEntry().withData("state", "Cancel").withData("value", "x");

        assertFalse(endMatcher.matches(entry1));
        assertTrue(endMatcher.matches(entry2));
        assertTrue(endMatcher.matches(entry2));

    }

    @Test
    public void testDobuleMatches() {
        DataEndMatcher endMatcher = new DataEndMatcher().add("state", "Close", "Cancel").add("value", "y", "z");
        LogEntry entry1 = createLogEntry().withData("state", "Open").withData("value", "y");
        LogEntry entry2 = createLogEntry().withData("state", "Close").withData("value", "x");
        LogEntry entry3 = createLogEntry().withData("state", "Cancel").withData("value", "x");
        LogEntry entry4 = createLogEntry().withData("state", "Close").withData("value", "y");
        LogEntry entry5 = createLogEntry().withData("state", "Cancel").withData("value", "z");
        LogEntry entry6 = createLogEntry().withData("state", "Cancel").withData("value", "y");

        assertFalse(endMatcher.matches(entry1));
        assertFalse(endMatcher.matches(entry2));
        assertFalse(endMatcher.matches(entry3));
        assertTrue(endMatcher.matches(entry4));
        assertTrue(endMatcher.matches(entry5));
        assertTrue(endMatcher.matches(entry6));

    }

    @NotNull
    private LogEntry createLogEntry() {
        return LogEntry.flowElement("p", "1", "Task", LogEntry.EventType.assign, new DateTime());
    }
}