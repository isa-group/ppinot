package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogListener;
import es.us.isa.ppinot.evaluation.logs.MXMLLog;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * MXMLLogTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MXMLLogTest {
    @Test
    public void testProcessLog() throws Exception {
        LogChecker logChecker = new LogChecker();
        MXMLLog log = new MXMLLog(getClass().getResourceAsStream("rfc_log2.mxml"), logChecker);

        log.processLog();

        logChecker.checkProcessesAreOneOf(new HashSet(Arrays.asList("in_8029559475398663243.bpmn")));
        logChecker.checkInstancesAreOneOf(new HashSet(Arrays.asList("113", "12")));
        logChecker.existsEntry("in_8029559475398663243.bpmn",
                "113",
                "Analyse RFC",
                LogEntry.EventType.complete,
                "2013-06-07T16:20:50.099+03:00");

        logChecker.existsEntry("in_8029559475398663243.bpmn",
                "12",
                "Report RFC approved",
                LogEntry.EventType.assign,
                "2013-06-04T10:21:42.669+03:00");

        logChecker.existsInstanceEntry("in_8029559475398663243.bpmn",
                "12",
                LogEntry.EventType.assign,
                "2013-06-03T19:59:10.428+03:00");

        logChecker.existsInstanceEntry("in_8029559475398663243.bpmn",
                "113",
                LogEntry.EventType.complete,
                "2013-06-07T16:26:03.797+03:00");
    }

    private class LogChecker implements LogListener {

        private List<LogEntry> entries;
        private Set<String> processes;
        private Set<String> instances;
        private DateTimeFormatter fmt = ISODateTimeFormat.dateTime();


        private LogChecker() {
            entries = new ArrayList<LogEntry>();
            processes = new HashSet<String>();
            instances = new HashSet<String>();
        }

        @Override
        public void update(LogEntry entry) {
            processes.add(entry.getProcessId());
            instances.add(entry.getInstanceId());
            entries.add(entry);
        }

        public void checkProcessesAreOneOf(Set<String> processes) {
            Assert.assertEquals("Processes", processes, this.processes);
        }

        public void checkInstancesAreOneOf(Set<String> instances) {
            Assert.assertEquals("Instances", instances, this.instances);
        }

        public void existsEntry(String process, String instance, String bpElement, LogEntry.EventType eventType, String timestamp) {
            existsEntry(process, instance, bpElement, eventType, timestamp, null);
        }

        public void existsEntry(String process, String instance, String bpElement, LogEntry.EventType eventType, String timestamp, String resource) {
            LogEntry entry = LogEntry.flowElement(process, instance, bpElement, eventType, fmt.parseDateTime(timestamp));
            if (resource != null)
                entry.setResource(resource);

            Assert.assertTrue("Entry", entries.contains(entry));
        }

        public void existsInstanceEntry(String process, String instance, LogEntry.EventType eventType, String timestamp) {
            LogEntry entry = LogEntry.instance(process, instance, eventType, fmt.parseDateTime(timestamp));
            Assert.assertTrue("Entry", entries.contains(entry));
        }
    }

}
