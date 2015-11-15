package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * LogEntry
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class LogEntry {
    public enum EventType {
        ready, assign, complete
    }

    public enum ElementType {
        process, flowElement, data
    }

    private String processId;
    private String instanceId;
    private String bpElement;
    private ElementType elementType;
    private EventType eventType;
    private DateTime timeStamp;
    private String resource;
    private Map<String, Object> data;
    private Map<String, Object> extensions;

    public LogEntry(String processId, String instanceId, String bpElement, ElementType elementType, EventType eventType, DateTime timeStamp) {
        this.processId = processId;
        this.instanceId = instanceId;
        this.bpElement = bpElement;
        this.elementType = elementType;
        this.eventType = eventType;
        this.timeStamp = timeStamp;
        this.data = new HashMap<String, Object>();
        this.extensions = new HashMap<String, Object>();
    }

    public LogEntry(String processId, String instanceId, String bpElement, ElementType elementType, EventType eventType, DateTime timeStamp, Map<String, Object> data) {
        this(processId, instanceId, bpElement, elementType, eventType, timeStamp);
        this.data.putAll(data);
    }

    public static LogEntry flowElement(String processId, String instanceId, String bpElement, EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, bpElement, ElementType.flowElement, eventType, timeStamp);
    }

    public static LogEntry instance(String processId, String instanceId, EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, processId, ElementType.process, eventType, timeStamp);
    }
    
    public static LogEntry instance(String processId, String instanceId, String bpElement ,EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, bpElement, ElementType.process, eventType, timeStamp);
    }

    public static LogEntry data(String processId, String instanceId, String data, EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, data, ElementType.data, eventType, timeStamp);
    }

    public String getProcessId() {
        return processId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getBpElement() {
        return bpElement;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public LogEntry withData(Map<String,Object> newData) {
        data.putAll(newData);
        return this;
    }

    public LogEntry withData(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Object getExtension(String key) {
        return extensions.get(key);
    }

    public void saveExtension(String key, Object value) {
        extensions.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry)) return false;

        LogEntry logEntry = (LogEntry) o;

        if (!processId.equals(logEntry.processId)) return false;
        if (!instanceId.equals(logEntry.instanceId)) return false;
        if (!bpElement.equals(logEntry.bpElement)) return false;
        if (elementType != logEntry.elementType) return false;
        if (eventType != logEntry.eventType) return false;
        if (!timeStamp.equals(logEntry.timeStamp)) return false;
        if (resource != null ? !resource.equals(logEntry.resource) : logEntry.resource != null) return false;
        return data.equals(logEntry.data);

    }

    @Override
    public int hashCode() {
        int result = processId.hashCode();
        result = 31 * result + instanceId.hashCode();
        result = 31 * result + bpElement.hashCode();
        result = 31 * result + elementType.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + timeStamp.hashCode();
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + data.hashCode();
        return result;
    }

    public String format() {
        StringBuilder sb = new StringBuilder();

        sb.append(getProcessId()).append(", ")
                .append(getInstanceId()).append(" - ")
                .append(getBpElement())
                .append(" (").append(getEventType().toString()).append(") ")
                .append(" - ").append(getTimeStamp());

        if (resource != null)
            sb.append(" [").append(resource).append("] ");

        if (!data.isEmpty()) {
            sb.append(" {");
            for (String key : data.keySet()) {
                sb.append(key).append(": ").append(data.get(key)).append(", ");
            }

            sb.append("} ");
        }

        return sb.toString();
    }
}
