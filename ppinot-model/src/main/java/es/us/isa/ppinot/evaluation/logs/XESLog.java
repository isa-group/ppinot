package es.us.isa.ppinot.evaluation.logs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MXMLLog
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class XESLog extends AbstractLogProvider {
    private static final Logger log = Logger.getLogger(XESLog.class.getName());

    
    private static final String TAG_TRACE = "trace";
	private static final String ATTRIBUTE_ID = "concept:name";
	private static final String TAG_EVENT = "event";
	private static final String TAG_LOG = "string";
	private static final String TAG_TIME = "date";
	private static final String TAG_TIMESTAMP = "time:timestamp";
    private static final String TAG_EVENTTYPE = "lifecycle:transition";
    private static final String TAG_ORIGINATOR = "org:resource";
    private static final String TAG_LIST = "list";
    
    private XMLStreamReader reader;
    
 
    public enum ElementName {
        UNKNOWN, ID, NAME
    }

    public XESLog(InputStream inputStream) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            log.log(Level.SEVERE, "Error loading inputstream", e);
            throw new RuntimeException(e);
        }
    }

    public XESLog(InputStream inputStream, LogListener listener) {
        this(inputStream);
        registerListener(listener);
    }

    @Override
    public void processLog() {
    	String processId = "";
    	try {
            while (reader.hasNext()) {
                int type = reader.next();
                if (isStartElement(type) && hasName(reader, TAG_TRACE)) {
	                evalProcess(processId);
 
                }else if(isStartElement(type) && hasName(reader, TAG_LOG)) {
                	if(reader.getAttributeValue(0).equals(ATTRIBUTE_ID)) {
                		processId = reader.getAttributeValue(1);
                	}
                }
            }
        } catch (XMLStreamException e) {
        	log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        }
    }

    private void evalProcess(String processId) {
        boolean endProcess = false;
        String instanceId = "";
        try {
            while (reader.hasNext() && !endProcess) {
                int type = reader.next();
                if (isEndElement(type) && hasName(reader, TAG_TRACE)) {
                	endProcess = true;
                }else if(isStartElement(type) && hasName(reader, TAG_LOG)) {
                	if(reader.getAttributeValue(0).equals(ATTRIBUTE_ID)) {
                		instanceId = reader.getAttributeValue(1);
                		reader.next();
                	}
                }else if (isStartElement(type) && hasName(reader, TAG_EVENT)) {
                    evalProcessInstance(processId, instanceId);
                }
            }
        } catch (XMLStreamException e) {
        	log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        }
    }

    private void evalProcessInstance(String processId, String instanceId) {

        boolean endInstance = false;
        
        LogEntry lastLogEntry = null;
        Map<String, Object> instanceData = null;

        try {
            while (reader.hasNext() && !endInstance) {
            	int type = reader.next();
            	if (isEndElement(type) && hasName(reader, TAG_EVENT)) {
            		endInstance = true;
            	}if(isStartElement(type) && hasName(reader, TAG_LOG)) {	
	            	if(reader.getAttributeValue(0).equals(ATTRIBUTE_ID)) {
	                		instanceId = reader.getAttributeValue(1);
	                		reader.next();
	            	}
                }else {
	            	lastLogEntry = createLogEntry(processId, instanceId, instanceData);
	            	updateListeners(lastLogEntry);
	            	break;
                }
            }
        } catch (XMLStreamException e) {
        	log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        }
    }
    

    private LogEntry createLogEntry(String processId, String instanceId, Map<String, Object> instanceData) {
        boolean endEntry = false;
        String bpElement = "";
        LogEntry.EventType eventType = null;
        String originator = null;
        DateTime timeStamp = null;
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        Map<String, Object> data = new HashMap<String,Object>();
        Map<String, Object> thisData = new HashMap<String, Object>();

        try {
            while (reader.hasNext() && !endEntry) {
                int type = reader.next();
                
                if (isEndElement(type) && (hasName(reader, TAG_EVENT) || hasName(reader, TAG_LIST))) {
                    endEntry = true;
                }else if(isStartElement(type) && hasName(reader, TAG_LOG)) {
                	String att = reader.getAttributeValue(0);    
                	
                	if(att.equals(ATTRIBUTE_ID)) {
                		bpElement = reader.getAttributeValue(1);
                	}else if(att.equals(TAG_EVENTTYPE)){
                		eventType = LogEntry.EventType.valueOf(reader.getAttributeValue(1));
                	}else if(att.equals(TAG_ORIGINATOR)) {
                		originator = reader.getAttributeValue(1);
                	}else{
                		data = createData(data, reader, type);
                	}
                	reader.next();
                	
                }else if(isStartElement(type) && hasName(reader, TAG_TIME)) {
                	if(reader.getAttributeValue(0).equals(TAG_TIMESTAMP)) {
                		timeStamp = fmt.parseDateTime(reader.getAttributeValue(1));
                	}else{
                		data = createData(data, reader, type);
                	}
                	reader.next();
                	
                }else if(isStartElement(type) && hasName(reader, TAG_LIST)){
                	String key = reader.getAttributeValue(0);
                	reader.next();
                	data = evalDataList(data, key, thisData);
                	
                }else if(isStartElement(type)){
            		data = createData(data, reader, type);
            		reader.next();

                }  
            }
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        } catch (Exception e) {
        	log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        }

        LogEntry entry = LogEntry.flowElement(processId, instanceId, bpElement, eventType, timeStamp);
        
        if (instanceData != null) {
            entry = entry.withData(instanceData);
        }
        
        if (data != null) {
            entry = entry.withData(data);
        }

        if (originator != null)
            entry.setResource(originator);
       
        return entry;
    }
    
    private Map<String, Object> evalDataList(Map<String, Object> data, String key, Map<String, Object> thisData) throws XMLStreamException {
    	
        boolean endInstance = false;
    	
        try {
            while (reader.hasNext() && !endInstance) {
            	int typeA = reader.next();
            	if (isEndElement(typeA) && hasName(reader, TAG_LIST)) {
            		endInstance = true;
            	}else {
            		thisData = createData(thisData, reader, typeA);
                	data.put(key, thisData);
                	reader.next();
            	}
            }
            
        } catch (XMLStreamException e) {
        	log.log(Level.WARNING, "Error processsing entry: " + reader.getLocation(), e);
        }
        reader.next();
        return data;
    }

	private Map<String, Object> createData(Map<String, Object> data, XMLStreamReader reader, int type) {
    	DateTimeFormatter fmt = ISODateTimeFormat.dateTime(); 
    	String key;
        try {
        	
            key = reader.getAttributeValue(0);
            switch (reader.getName().getLocalPart()) {	            
	            case "boolean":
	                Boolean bolVal = Boolean.parseBoolean(reader.getAttributeValue(1));
	                data.put(key, bolVal);
	                break;
	            case "string":
	            case "id":
	                String strVal = reader.getAttributeValue(1);
	                data.put(key, strVal);
	                break;
	            case "date":
	            	DateTime dateVal  = fmt.parseDateTime(reader.getAttributeValue(1));
	            	data.put(key, dateVal);
	                break;
	            case "int":
	                Integer intVal = Integer.parseInt(reader.getAttributeValue(1));
	                data.put(key, intVal);
	                break;
	            case "float":
	                Float floatVal = Float.parseFloat(reader.getAttributeValue(1));
	                data.put(key, floatVal);
	                break;
	            case "list":
	            	break;
	            default:
	               break;
            }
        	
            reader.next();
        	    
        } catch (XMLStreamException e) {
            log.log(Level.WARNING, "Error processsing data attributes: " + reader.getLocation(), e);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error processing data attributes: " + reader.getLocation(), e);
        }

        return data;
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
