package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * PreviousDataStateTransformerTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class PreviousDataStateTransformerTest {

    @Test
    public void testTransformFirstLogEntry() throws Exception {
        PreviousDataStateTransformer transformer = new PreviousDataStateTransformer();
        LogEntry transformed = transformer.transform(createLogEntry1());

        Object extension = transformed.getExtension(PreviousDataStateTransformer.EXTENSION);

        assertNotNull(extension);
        assertTrue(extension instanceof Map);

        Map<String, Object> extendedMap = (Map<String, Object>) extension;

        assertTrue(extendedMap.isEmpty());

    }

    private Map<String, Object> getExtension(LogEntry transformed) {
        Object extension = transformed.getExtension(PreviousDataStateTransformer.EXTENSION);
        return (Map<String, Object>) extension;
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

    private LogEntry createLogEntryOtherInstance() {
        LogEntry logEntry = LogEntry.flowElement("0", "1", "1", LogEntry.EventType.complete, DateTime.now());
        logEntry.withData("A", "otherA").withData("B", "otherB").withData("C", "otherC");
        return logEntry;
    }

    @Test
    public void testTransformSecondLogEntry() throws Exception {
        PreviousDataStateTransformer transformer = new PreviousDataStateTransformer();
        transformer.transform(createLogEntry1());
        LogEntry entry = transformer.transform(createLogEntry2());
        Map<String, Object> extension = getExtension(entry);

        assertEquals("v", extension.get("A"));
        assertEquals("d", extension.get("B"));
    }

    @Test
    public void testTransformIgnoresOtherInstances() throws Exception {
        PreviousDataStateTransformer transformer = new PreviousDataStateTransformer();
        transformer.transform(createLogEntry1());
        LogEntry otherInstance = transformer.transform(createLogEntryOtherInstance());
        LogEntry entry = transformer.transform(createLogEntry2());

        Map<String, Object> extension = getExtension(entry);

        assertEquals("v", extension.get("A"));
        assertEquals("d", extension.get("B"));

        extension = getExtension(otherInstance);
        assertTrue(extension.isEmpty());
    }
}