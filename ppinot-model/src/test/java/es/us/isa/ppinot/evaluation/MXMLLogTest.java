package es.us.isa.ppinot.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogEntry.EventType;
import es.us.isa.ppinot.evaluation.logs.LogListener;
import es.us.isa.ppinot.evaluation.logs.MXMLLog;

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
        MXMLLog log = new MXMLLog(getClass().getResourceAsStream("rfc_log2.mxml"), logChecker, null);


        log.processLog();

        logChecker.checkProcessesAreOneOf(new HashSet(Arrays.asList("in_8029559475398663243.bpmn")));
        logChecker.checkInstancesAreOneOf(new HashSet(Arrays.asList("113", "12")));

        Map<String, Object> data = new HashMap<String,Object>();
        data.put("LogType", "MXML.EnactmentLog");

        logChecker.existsEntry("in_8029559475398663243.bpmn",
                "113",
                "Analyse RFC",
                LogEntry.EventType.complete,
                "2013-06-07T16:20:50.099+03:00",
                data
                );

        logChecker.existsEntry("in_8029559475398663243.bpmn",
                "12",
                "Report RFC approved",
                LogEntry.EventType.assign,
                "2013-06-04T10:21:42.669+03:00",
                data);

        logChecker.existsInstanceEntry("in_8029559475398663243.bpmn",
                "12",
                LogEntry.EventType.assign,
                "2013-06-03T19:59:10.428+03:00",
                data);

        logChecker.existsInstanceEntry("in_8029559475398663243.bpmn",
                "113",
                LogEntry.EventType.complete,
                "2013-06-07T16:26:03.797+03:00",
                data);
    }

    @Test
    public void testProcessLogWithEventData() throws Exception {
        LogChecker logChecker = new LogChecker();
        MXMLLog log = new MXMLLog(getClass().getResourceAsStream("TEST.mxml"), logChecker, null);


        log.processLog();

        logChecker.checkProcessesAreOneOf(new HashSet(Arrays.asList("All_Severity_codes.mxml")));
        logChecker.checkInstancesAreOneOf(new HashSet(Arrays.asList("PSHAP00120160004527-296402")));

        Map<String, Object> caseData = new HashMap<String,Object>();
        caseData.put("Variant", "Variant 17");
        caseData.put("Variant index", "17");

        Map<String,Object> data = new HashMap<String,Object>(caseData);
        data.put("ED_ADMISSION_DATE", "01/02/2016");
        data.put("TYPE_OF_DISCHARGE", "Discharge to home");
        data.put("ACTIVITY_ATTRIBUTE", "Null");

        logChecker.existsInstanceEntry("All_Severity_codes.mxml",
            "PSHAP00120160004527-296402",
            LogEntry.EventType.assign,
            "2016-02-01T23:28:57.000+01:00",
            data
            );

        logChecker.existsEntry("All_Severity_codes.mxml",
                "PSHAP00120160004527-296402",
                "TRIAGE",
                LogEntry.EventType.complete,
                "2016-02-01T23:28:57.000+01:00",
                data
                );

        logChecker.existsInstanceEntry("All_Severity_codes.mxml",
                "PSHAP00120160004527-296402",
                LogEntry.EventType.complete,
                "2016-02-02T03:35:00.000+01:00",
                data);


        data = new HashMap<String,Object>(caseData);
        data.put("ED_ADMISSION_DATE", "01/02/2016");
        data.put("TYPE_OF_DISCHARGE", "Discharge to home");
        data.put("ACTIVITY_ATTRIBUTE", "Lab Exam");
        logChecker.existsEntry("All_Severity_codes.mxml",
                "PSHAP00120160004527-296402",
                "LAB REQUEST",
                LogEntry.EventType.complete,
                "2016-02-02T01:37:39.000+01:00",
                data);



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

        public void existsEntry(String process, String instance, String bpElement, EventType assign, String timestamp,
                Map<String, Object> data) {
            
            existsEntry(process, instance, bpElement, assign, timestamp, null, data);

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

        public void existsEntry(String process, String instance, String bpElement, LogEntry.EventType eventType, String timestamp, String resource, Map<String,Object> data) {
            LogEntry entry = LogEntry.flowElement(process, instance, bpElement, eventType, fmt.parseDateTime(timestamp));
            if (data != null) {
                entry = entry.withData(data);
            }
            if (resource != null)
                entry.setResource(resource);

            Assert.assertTrue("Entry", entries.contains(entry));
        }

        public void existsInstanceEntry(String process, String instance, LogEntry.EventType eventType, String timestamp, Map<String, Object> data) {
            LogEntry entry = LogEntry.instance(process, instance, eventType, fmt.parseDateTime(timestamp));
            if (data != null) {
                entry = entry.withData(data);
            }
            Assert.assertTrue("Entry", entries.contains(entry));
        }
    }

}
