package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JSONLogEntryLoaderTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONLogEntryLoaderTest {
    private String jsonEvent = "{\n" +
            "    \"Id\": \"1\",\n" +
            "    \"Fecha\": \"12:00:00 1/6/2016\",\n" +
            "    \"Estado\": \"Abierta\",\n" +
            "    \"Criticidad\": \"P1\",\n" +
            "    \"Grupo Asignado\": \"proveedor 1\",\n" +
            "    \"Acciones\": \"Registrar\"\n" +
            "}";

    @Test
    public void testLoadEvent() throws Exception {
        JSONLogEntryLoader loader = new JSONLogEntryLoader("Id", "Acciones", "Fecha");
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss d/M/YYYY");
        loader.setDateTimeFormatter(dateTimeFormatter);

        LogEntry entry = loader.loadEvent(jsonEvent);

        Assert.assertEquals("1", entry.getInstanceId());
        Assert.assertEquals("Registrar", entry.getBpElement());
        Assert.assertEquals(new DateTime(2016, 6, 1, 12, 0, 0), entry.getTimeStamp());
        Assert.assertEquals("P1", entry.getData().get("Criticidad"));
        Assert.assertEquals("proveedor 1", entry.getData().get("Grupo Asignado"));
        Assert.assertEquals("Abierta", entry.getData().get("Estado"));
    }


    @Test
    public void testLoadFullEvent() throws Exception {
        String jsonEvent = "{\n" +
                "\"ProcessId\": \"in_8029559475398663243.bpmn\",\n" +
                "\"Id\": \"1\",\n" +
                "\"Fecha\": \"12:00:00 1/6/2016\",\n" +
                "\"EventType\": \"complete\",\n" +
                "\"Acciones\": \"Registrar\",\n" +
                "\"ElementType\": \"flowElement\",\n" +
                "\"Resource\": \"\"\n" +
                "}\n";

        JSONLogEntryLoader loader = new JSONLogEntryLoader("Id", "Acciones", "Fecha")
                .setProcessIdField("ProcessId")
                .setEventField("EventType")
                .setResourceField("Resource")
                .setElementTypeField("ElementType");
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss d/M/YYYY");
        loader.setDateTimeFormatter(dateTimeFormatter);

        LogEntry entry = loader.loadEvent(jsonEvent);

        Assert.assertEquals("in_8029559475398663243.bpmn", entry.getProcessId());
        Assert.assertEquals("1", entry.getInstanceId());
        Assert.assertEquals("Registrar", entry.getBpElement());
        Assert.assertEquals(new DateTime(2016, 6, 1, 12, 0, 0), entry.getTimeStamp());
        Assert.assertEquals(LogEntry.EventType.complete, entry.getEventType());
        Assert.assertEquals(LogEntry.ElementType.flowElement, entry.getElementType());
        Assert.assertEquals("", entry.getResource());
    }
}