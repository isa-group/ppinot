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
import es.us.isa.ppinot.evaluation.logs.XESLog;

/**
 * MXMLLogTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class XESLogTest {
	
    @Test
    public void testProcessLogBasicExample() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("testXes.xes"), logChecker, null);

        log.processLog();
        
        logChecker.existsEntryComplete("excercise1.mxml", "Case3.0", "A", "UNDEFINED", LogEntry.EventType.complete,
                "2008-12-09T08:20:01.527+01:00");
        logChecker.existsEntryComplete("excercise1.mxml", "Case2.0", "C", "UNDEFINED", LogEntry.EventType.complete,
                "2008-12-09T08:21:01.527+01:00");
        logChecker.existsEntryComplete("excercise1.mxml", "Case1.0", "D", "UNDEFINED", LogEntry.EventType.complete,
                "2008-12-09T08:23:01.600+01:00");
    }
    
    
    @Test
    public void testProcessLogTypeA1() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelA1.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkALevel("Filtered A1 log", "1", "Register+complete");
        logChecker.checkALevel("Filtered A1 log", "10", "Register+complete");
        logChecker.checkALevel("Filtered A1 log", "100", "Test Repair+start");
    }
    
    
    @Test
    public void testProcessLogTypeA2() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelA2.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkA2Level("Filtered A2 log", "1", "Register", EventType.complete);
        logChecker.checkA2Level("Filtered A2 log", "1", "Analyze Defect", EventType.start);
        logChecker.checkA2Level("Filtered A2 log", "10", "Repair (Simple)", EventType.complete);
    }
    
    
    @Test
    public void testProcessLogTypeB1() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelB1.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkB1Level("Filtered B1 log", "1", "Register", EventType.complete, "1970-01-02T12:25:02.046+01:00");
        logChecker.checkB1Level("Filtered B1 log", "1", "Analyze Defect", EventType.start, "1970-01-02T12:28:44.008+01:00");
        logChecker.checkB1Level("Filtered B1 log", "10", "Repair (Simple)", EventType.complete, "1970-01-01T12:29:02.130+01:00");
    }
    
    
    @Test
    public void testProcessLogTypeB2() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelB2.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkB2Level("Filtered B2 log", "1", "Register", EventType.complete, "1970-01-02T12:24:57.621+01:00");
        logChecker.checkB2Level("Filtered B2 log", "1", "Analyze Defect", EventType.start, "1970-01-02T12:28:23.091+01:00");
        logChecker.checkB2Level("Filtered B2 log", "10", "Repair (Simple)", EventType.complete, "1970-01-01T12:22:40.570+01:00");
    }
    
    
    @Test
    public void testProcessLogTypeC1() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelC1.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkC1Level("Filtered C1 log", "999", "Register+complete", "System");
        logChecker.checkC1Level("Filtered C1 log", "731", "Test Repair+complete", "Tester2");
        logChecker.checkC1Level("Filtered C1 log", "388", "Analyze Defect+complete", "Tester6");
    }
    
    
    @Test
    public void testProcessLogTypeC2() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelC2.xes"), logChecker, null);

        log.processLog();
        
        logChecker.checkC2Level("Filtered C2 log", "381", "Analyze Defect", "Tester1", EventType.complete);
        logChecker.checkC2Level("Filtered C2 log", "386", "Analyze Defect", "Tester6", EventType.start);
        logChecker.checkC2Level("Filtered C2 log", "614", "Repair (Simple)", "SolverS3", EventType.start);
    }
    
    
    @Test
    public void testProcessLogTypeD1() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelD1.xes"), logChecker, null);

        log.processLog();
        
        Map<String, Object> data1 = new HashMap<String, Object>();
        Map<String, Object> data2 = new HashMap<String, Object>();
        Map<String, Object> data3 = new HashMap<String, Object>();
        
        data1.put("org:group", "Group -");
        data1.put("concept:instance", "instance 1");
        
        data2.put("org:group", "Groups 2 and 4");
        data2.put("concept:instance", "instance 1");
        data2.put("org:role", "Role 9");
        
        data3.put("org:group", "Group -");
        data3.put("concept:instance", "instance 1");
        
        logChecker.checkD1Level("Filtered D1 log", "1", "Register", "System", EventType.complete, "1970-01-02T12:24:45.453+01:00", data1);
        logChecker.checkD1Level("Filtered D1 log", "316", "Test Repair", "Tester3", EventType.start, "1970-01-15T08:38:06.619+01:00", data2);
        logChecker.checkD1Level("Filtered D1 log", "822", "Archive Repair", "System", EventType.complete, "1970-02-05T01:45:29.492+01:00", data3);
    }
    
    
    @Test
    public void testProcessLogTypeD2() throws Exception {
        LogChecker logChecker = new LogChecker();
        XESLog log = new XESLog(getClass().getResourceAsStream("LevelD1.xes"), logChecker, null);

        log.processLog();
        
        Map<String, Object> data1 = new HashMap<String, Object>();
        Map<String, Object> data2 = new HashMap<String, Object>();
        Map<String, Object> data3 = new HashMap<String, Object>();
        
        data1.put("org:group", "Group -");
        data1.put("concept:instance", "instance 1");
        
        data2.put("org:group", "Groups 2 and 4");
        data2.put("concept:instance", "instance 1");
        data2.put("org:role", "Role 9");
        
        data3.put("org:group", "Group -");
        data3.put("concept:instance", "instance 1");
        
        logChecker.checkD2Level("Filtered D1 log", "1", "Register", "System", EventType.complete, "1970-01-02T12:24:45.453+01:00", data1);
        logChecker.checkD2Level("Filtered D1 log", "316", "Test Repair", "Tester3", EventType.start, "1970-01-15T08:38:06.619+01:00", data2);
        logChecker.checkD2Level("Filtered D1 log", "822", "Archive Repair", "System", EventType.complete, "1970-02-05T01:45:29.492+01:00", data3);
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

        public void checkALevel(String process, String instance, String bpElement) {

        	Boolean exists = false;
              	
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        public void checkA2Level(String process, String instance, String bpElement, EventType eventType) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement) &&
        				en.getEventType().equals(eventType)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        public void checkB1Level(String process, String instance, String bpElement, EventType eventType, String timestamp) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement) &&
        				en.getEventType().equals(eventType) &&
        				en.getTimeStamp().equals(fmt.parseDateTime(timestamp))) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        
        public void checkB2Level(String process, String instance, String bpElement, EventType eventType, String timestamp) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement) &&
        				en.getEventType().equals(eventType) &&
        				en.getTimeStamp().equals(fmt.parseDateTime(timestamp))) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        
        public void checkC1Level(String process, String instance, String bpElement, String resource) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement) &&
        				en.getResource().equals(resource)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        
        public void checkC2Level(String process, String instance, String bpElement, String resource, EventType assign) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		if(en.getBpElement().equals(bpElement) && 
        				en.getInstanceId().equals(instance) && 
        				en.getProcessId().equals(process)&& 
        				en.getResource().equals(resource) && 
        				en.getEventType().equals(assign)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        
        public void checkD1Level(String process, String instance, String bpElement, String resource, EventType assign, String timestamp, Map<String, Object> data) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		
        		if(en.getBpElement().equals(bpElement) && 
        				en.getInstanceId().equals(instance) && 
        				en.getProcessId().equals(process)&& 
        				en.getResource().equals(resource) && 
        				en.getEventType().equals(assign) &&
        				en.getData().equals(data)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        
        public void checkD2Level(String process, String instance, String bpElement, String resource, EventType assign, String timestamp, Map<String, Object> data) {

        	Boolean exists = false;
        
        	for(LogEntry en : entries) {
        		
        		if(en.getBpElement().equals(bpElement) && 
        				en.getInstanceId().equals(instance) && 
        				en.getProcessId().equals(process)&& 
        				en.getResource().equals(resource) && 
        				en.getEventType().equals(assign) &&
        				en.getData().equals(data)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }
        
        public void existsEntryComplete(String process, String instance, String bpElement, String resource, EventType assign, String timestamp) {
        	
        	Boolean exists = false;
   
        	for(LogEntry en : entries) {
        		
        		if(en.getBpElement().equals(bpElement) && 
        				en.getInstanceId().equals(instance) && 
        				en.getProcessId().equals(process)&& 
        				en.getResource().equals(resource) && 
        				en.getTimeStamp().equals(fmt.parseDateTime(timestamp)) &&
        				en.getEventType().equals(assign)) {
        			exists = true;
        			break;
        		}
        	}
        	Assert.assertTrue(exists);
        }

        @Override
        public void update(LogEntry entry) {
        	
            processes.add(entry.getProcessId());
            instances.add(entry.getInstanceId());
            entries.add(entry);
        }

    }

}
