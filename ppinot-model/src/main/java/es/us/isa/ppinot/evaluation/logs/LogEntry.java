package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.DateTime;

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

    public LogEntry(String processId, String instanceId, String bpElement, ElementType elementType, EventType eventType, DateTime timeStamp) {
        this.processId = processId;
        this.instanceId = instanceId;
        this.bpElement = bpElement;
        this.elementType = elementType;
        this.eventType = eventType;
        this.timeStamp = timeStamp;
    }

    public static LogEntry flowElement(String processId, String instanceId, String bpElement, EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, bpElement, ElementType.flowElement, eventType, timeStamp);
    }

    public static LogEntry instance(String processId, String instanceId, EventType eventType, DateTime timeStamp) {
        return new LogEntry(processId, instanceId, processId, ElementType.process, eventType, timeStamp);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry entry = (LogEntry) o;

        if (!bpElement.equals(entry.bpElement)) return false;
        if (elementType != entry.elementType) return false;
        if (eventType != entry.eventType) return false;
        if (!instanceId.equals(entry.instanceId)) return false;
        if (!processId.equals(entry.processId)) return false;
        if (resource != null ? !resource.equals(entry.resource) : entry.resource != null) return false;
        if (!timeStamp.equals(entry.timeStamp)) return false;

        return true;
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

        return sb.toString();
    }
}
