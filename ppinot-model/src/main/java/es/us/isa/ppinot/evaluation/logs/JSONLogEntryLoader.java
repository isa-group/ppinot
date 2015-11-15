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

    private String instanceIdField;
    private String bpElementField;
    private String timestampField;

    private DateTimeFormatter dateTimeFormatter;

    private String defaultProcessId = "0";
    private LogEntry.EventType defaultEventType = LogEntry.EventType.complete;

    public JSONLogEntryLoader(String instanceIdField, String bpElementField, String timestampField) {
        mapper = new ObjectMapper();
        this.instanceIdField = instanceIdField;
        this.bpElementField = bpElementField;
        this.timestampField = timestampField;
        this.dateTimeFormatter = ISODateTimeFormat.dateOptionalTimeParser();
    }

    public JSONLogEntryLoader setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    public LogEntry loadEvent(String jsonEvent) {
        try {
            JsonNode rootNode = mapper.readValue(jsonEvent, JsonNode.class);
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.getFields();

            String instanceId = null;
            DateTime date = null;
            String bpElement = null;
            Map<String, Object> data = new HashMap<String, Object>();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().equals(instanceIdField)) {
                    instanceId = field.getValue().asText();
                } else if (field.getKey().equals(bpElementField)) {
                    bpElement = field.getValue().asText();
                } else if (field.getKey().equals(timestampField)) {
                    date = DateTime.parse(field.getValue().asText(), dateTimeFormatter);
                } else {
                    data.put(field.getKey(), field.getValue().asText());
                }

            }

            return LogEntry.flowElement(defaultProcessId, instanceId, bpElement, defaultEventType, date).withData(data);

        } catch (IOException e) {
            throw new RuntimeException("Invalid event format: " + jsonEvent, e);
        }
    }

}
