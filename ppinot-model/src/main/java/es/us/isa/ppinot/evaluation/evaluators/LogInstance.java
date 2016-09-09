package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.handler.json.DateTimeDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * LogInstance
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class LogInstance {
    public interface Condition {
        boolean test(LogInstance instance);
    }

    private String instanceId;
    private List<LogEntry> entries;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime start;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime end;

    public LogInstance() {
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LogEntry> entries) {
        this.entries = entries;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }
}
