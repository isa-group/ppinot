package es.us.isa.ppinot.evaluation.logs;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TFlowElement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MXMLLog
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MXMLLog  {
    private static final Logger log = Logger.getLogger(MXMLLog.class.getName());

    private static final String TAG_PROCESS = "Process";
    private static final String TAG_PROCESSINSTANCE = "ProcessInstance";
    private static final String TAG_AUDITTRAILENTRY = "AuditTrailEntry";
    private static final String TAG_WORKFLOWMODELELEMENT = "WorkflowModelElement";
    private static final String TAG_EVENTTYPE = "EventType";
    private static final String TAG_TIMESTAMP = "Timestamp";
    private static final String ATTRIBUTE_ID = "id";
    private static final String TAG_ORIGINATOR = "Originator";

    private XMLStreamReader reader;
    private LogListener listener;

    private ElementName elementName;
    private Bpmn20ModelHandler bpmn20ModelHandler;

    public enum ElementName {
        UNKNOWN, ID, NAME
    }

    public MXMLLog(InputStream inputStream, LogListener listener, Bpmn20ModelHandler bpmn20ModelHandler) {
        this.listener = listener;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            log.log(Level.SEVERE, "Error loading inputstream", e);
            throw new RuntimeException(e);
        }
        this.elementName = ElementName.UNKNOWN;
        this.bpmn20ModelHandler = bpmn20ModelHandler;
    }

    public void processLog() {
        try {
            while (reader.hasNext()) {
                int type = reader.next();
                if (isStartElement(type) && hasName(reader, TAG_PROCESS)) {
                    evalProcess();
                }
            }
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processing log: " + reader.getLocation(), e);
        }
    }

    private void evalProcess() {
        String processId = reader.getAttributeValue(null, ATTRIBUTE_ID);
        boolean endProcess = false;

        try {
            while (reader.hasNext() && !endProcess) {
                int type = reader.next();
                if (isEndElement(type) && hasName(reader, TAG_PROCESS)) {
                    endProcess = true;
                } else if (isStartElement(type) && hasName(reader, TAG_PROCESSINSTANCE)) {
                    evalProcessInstance(processId);
                }
            }
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processing process: " + reader.getLocation(), e);
        }
    }

    private void evalProcessInstance(String processId) {
        String instanceId = reader.getAttributeValue(null, ATTRIBUTE_ID);
        boolean endInstance = false;
        boolean isFirstElement = true;
        LogEntry lastLogEntry = null;

        try {
            while (reader.hasNext() && !endInstance) {
                int type = reader.next();
                if (isEndElement(type) && hasName(reader, TAG_PROCESSINSTANCE)) {
                    endInstance = true;
                    if (lastLogEntry != null) {
                        listener.update(createInstanceEntry(lastLogEntry, LogEntry.EventType.complete));
                    }

                } else if (isStartElement(type) && hasName(reader, TAG_AUDITTRAILENTRY)) {
                    lastLogEntry = createLogEntry(processId, instanceId);

                    if (isFirstElement) {
                        listener.update(createInstanceEntry(lastLogEntry, LogEntry.EventType.assign));
                        isFirstElement = false;
                    }

                    listener.update(lastLogEntry);
                }
            }
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processing instance: " + reader.getLocation(), e);
        }
    }

    private LogEntry createInstanceEntry(LogEntry entry, LogEntry.EventType event) {
        return LogEntry.instance(entry.getProcessId(),
                entry.getInstanceId(),
                event,
                entry.getTimeStamp());
    }

    private LogEntry createLogEntry(String processId, String instanceId) {
        boolean endEntry = false;
        String bpElement = "";
        LogEntry.EventType eventType = null;
        String originator = null;
        DateTime timeStamp = null;
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        try {
            while (reader.hasNext() && !endEntry) {
                int type = reader.next();
                if (isEndElement(type) && hasName(reader, TAG_AUDITTRAILENTRY)) {
                    endEntry = true;
                } else if (isStartElement(type) && hasName(reader, TAG_WORKFLOWMODELELEMENT)) {
                    bpElement = elementName();
                } else if (isStartElement(type) && hasName(reader, TAG_EVENTTYPE)) {
                    eventType = LogEntry.EventType.valueOf(reader.getElementText());
                } else if (isStartElement(type) && hasName(reader, TAG_TIMESTAMP)) {
                    timeStamp = fmt.parseDateTime(reader.getElementText());
                } else if (isStartElement(type) && hasName(reader, TAG_ORIGINATOR)) {
                    originator = reader.getElementText();
                }
            }
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error processing entry: " + reader.getLocation(), e);
        }

        LogEntry entry = LogEntry.flowElement(processId, instanceId, bpElement, eventType, timeStamp);

        if (originator != null)
            entry.setResource(originator);

        return entry;
    }

    private String elementName() throws XMLStreamException {
        String name = reader.getElementText();
        if (bpmn20ModelHandler != null) {
            TFlowElement element = bpmn20ModelHandler.getElementById(name);
            if (element == null) {
                element = bpmn20ModelHandler.getElementByName(name);
                if (element != null) {
                    name = element.getId();
                }
            }
        }
        return name;
//        if (elementName != ElementName.ID && bpmn20ModelHandler != null) {
//            TFlowElement element = bpmn20ModelHandler.getElementByName(name);
//            if (element != null) {
//                name = element.getName();
//                elementName = ElementName.NAME;
//            } else {
//                elementName = ElementName.ID;
//            }
//        }
    }

    private boolean hasName(XMLStreamReader reader, String name) {
        return name != null && name.equals(reader.getName().getLocalPart());
    }

    private boolean isStartElement(int type) {
        return type == XMLEvent.START_ELEMENT;
    }

    private boolean isEndElement(int type) {
        return type == XMLEvent.END_ELEMENT;
    }


}
