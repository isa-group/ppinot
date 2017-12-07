package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.logs.AbstractLogProvider;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * LogEntryHelper
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class LogEntryHelper extends AbstractLogProvider {

    public static final String DEFAULT_INSTANCE = "iid";
    public static final int DEFAULT_DISTANCE = 10000;

    private int millisDistance;
    private DateTime timestamp;
    private List<LogEntry> entries = null;

    public LogEntryHelper() {
        this(DEFAULT_DISTANCE);
    }

    public LogEntryHelper(int millisDistance) {
        this.millisDistance = millisDistance;
        this.timestamp = DateTime.now();
    }

    public LogEntryHelper asLogProvider() {
        this.entries = new ArrayList<LogEntry>();
        return this;
    }


    @Override
    public void processLog() {
        if (entries != null) {
            for (LogEntry entry : entries) {
                super.updateListeners(entry);
            }
        }
    }

    // ---- Assign entries

    public LogEntry newAssignEntry(String activity) {
        return newEntry(activity, LogEntry.EventType.assign);
    }

    public LogEntry newAssignEntry(String activity, String instance) {
        return newEntry(activity, LogEntry.EventType.assign, instance);
    }

    public LogEntry newAssignEntry(String activity, String instance, DateTime timestamp) {
        return newEntry(activity, LogEntry.EventType.assign, instance, timestamp);
    }

    // ---- Complete entries

    public LogEntry newCompleteEntry(String activity) {
        return newEntry(activity, LogEntry.EventType.complete);
    }

    public LogEntry newCompleteEntry(String activity, String instance) {
        return newEntry(activity, LogEntry.EventType.complete, instance);
    }

    public LogEntry newCompleteEntry(String activity, String instance, DateTime timestamp) {
        return newEntry(activity, LogEntry.EventType.complete, instance, timestamp);
    }

    // ---- Generic entries

    public LogEntry newEntry(String activity, LogEntry.EventType eventType) {
        return newEntry(activity, eventType, DEFAULT_INSTANCE);
    }

    public LogEntry newEntry(String activity, LogEntry.EventType eventType, String instance) {
        DateTime time = timestamp;
        timestamp = timestamp.plusMillis(millisDistance);
        return newEntry(activity, eventType, instance, time);
    }

    public LogEntry newEntry(String activity, LogEntry.EventType eventType, String instance, DateTime timestamp) {
        LogEntry entry = LogEntry.flowElement("pid", instance, activity, eventType, timestamp);
        if (entries != null) {
            entries.add(entry);
        }
        return entry;
    }

	public LogEntry newInstance(String instance, LogEntry.EventType eventType) {
		DateTime time = timestamp;
        return newInstance(instance, eventType, time);
	}
	
	public LogEntry newInstance(String instance, LogEntry.EventType eventType, DateTime timestamp) {
        LogEntry entry = LogEntry.instance("pid", instance, eventType, timestamp);
        if (entries != null) {
            entries.add(entry);
        }
        return entry;
	}

}
