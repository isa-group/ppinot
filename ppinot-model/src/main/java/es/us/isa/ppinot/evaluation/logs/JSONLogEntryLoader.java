package es.us.isa.ppinot.evaluation.logs;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSONLogEntryLoader
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONLogEntryLoader {

    private ObjectMapper mapper;

    private String processIdField;
    private String instanceIdField;
    private String bpElementField;
    private String timestampField;
    private String eventField;
    private String resourceField;
    private String elementTypeField;

    private DateTimeFormatter dateTimeFormatter;
    private String defaultProcessId = "0";
    private LogEntry.EventType defaultEventType = LogEntry.EventType.complete;
    private LogEntry.ElementType defaultElementType = LogEntry.ElementType.flowElement;
    private String defaultResource = null;

    public JSONLogEntryLoader(String instanceIdField, String bpElementField, String timestampField) {
        mapper = new ObjectMapper();
        this.processIdField = null;
        this.eventField = null;
        this.resourceField = null;
        this.elementTypeField = null;
        this.instanceIdField = instanceIdField;
        this.bpElementField = bpElementField;
        this.timestampField = timestampField;
        this.dateTimeFormatter = ISODateTimeFormat.dateOptionalTimeParser();
    }

    public JSONLogEntryLoader setElementTypeField(String elementTypeField) {
        this.elementTypeField = elementTypeField;
        return this;
    }

    public JSONLogEntryLoader setResourceField(String resourceField) {
        this.resourceField = resourceField;
        return this;
    }

    public JSONLogEntryLoader setProcessIdField(String processIdField) {
        this.processIdField = processIdField;
        return this;
    }

    public JSONLogEntryLoader setEventField(String eventField) {
        this.eventField = eventField;
        return this;
    }

    public JSONLogEntryLoader setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    private boolean matches(String key, String fieldName) {
        return fieldName != null && fieldName.equals(key);
    }

    public LogEntry loadEvent(String jsonEvent) {
        try{
            JsonNode rootNode = mapper.readValue(jsonEvent, JsonNode.class);
            return loadEvent(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Invalid event format: " + jsonEvent, e);
        }
    }

    public LogEntry loadEvent(JsonNode jsonEvent) {
        Iterator<Map.Entry<String, JsonNode>> fields = jsonEvent.getFields();

        String instanceId = null;
        DateTime date = null;
        String bpElement = null;
        String processId = defaultProcessId;
        LogEntry.EventType eventType = defaultEventType;
        String resource = defaultResource;
        LogEntry.ElementType elementType = defaultElementType;

        Map<String, Object> data = new HashMap<String, Object>();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String value = null;
//            if (!field.getValue().isNull()) {
                value = field.getValue().asText();
//            }

            if (matches(field.getKey(), instanceIdField)) {
                instanceId = value;
            } else if (matches(field.getKey(), bpElementField)) {
                bpElement = value;
            } else if (matches(field.getKey(), timestampField)) {
                date = DateTime.parse(value, dateTimeFormatter);
            } else if (matches(field.getKey(), processIdField)) {
                processId = value;
            } else if (matches(field.getKey(), eventField)) {
                eventType = LogEntry.EventType.valueOf(value);
            } else if (matches(field.getKey(), resourceField)) {
                resource = value;
            } else if (matches(field.getKey(), elementTypeField)) {
                elementType = LogEntry.ElementType.valueOf(value);
            } else {
                data.put(field.getKey(), value);
            }

        }

        return new LogEntry(processId, instanceId, bpElement, elementType, eventType, date)
                .setResource(resource)
                .withData(data);

    }

}
