package es.us.isa.bpmn.owl.notation;

/**
 * Clase con el vocabulario utilizado en una ontología BPMN
 * 
 * @author Edelia
 *
 */
public final class Vocabulary {
	
	public static final String URI = "http://www.isa.us.es/ontologies/bpmn.owl";
	
	private static final String URINT = URI + "#";
	
	public static final String ACTIVITY_URI = URINT + "Activity"; 
	public static final String DATAINPUT_URI = URINT + "dataInput"; 
	public static final String DATAOUTPUT_URI = URINT + "dataOutput"; 
	public static final String DIRECTLYPRECEDES_URI = URINT + "directlyPrecedes"; 
	public static final String STARTEVENT_URI = URINT + "StartEvent"; 
	public static final String ENDEVENT_URI = URINT + "EndEvent"; 
	public static final String XORGATEWAY_URI = URINT + "XorGateway"; 
	public static final String GATEWAY_URI = URINT + "Gateway"; 
	public static final String DATAOBJECT_URI = URINT + "DataObject"; 
}
