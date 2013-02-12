package es.us.isa.bpmn.owl.notation;

/**
 * Vocabulary used in ontologies AbstractBP.owl and AbstractBP-relationships.owl
 * @author Edelia
 */
public final class Vocabulary {
	
	public static final String ABSTRACTBP_URI = "http://www.isa.us.es/ontologies/AbstractBP.owl";
    public static final String ABSTRACTBP_RELATIONSHIPS_BP = "http://www.isa.us.es/ontologies/AbstractBP-relationships.owl";

    private static final String URINT = ABSTRACTBP_URI + "#";
    private static final String RURINT = ABSTRACTBP_RELATIONSHIPS_BP + "#";
	
	public static final String ACTIVITY_URI = URINT + "Activity"; 
	public static final String DATAINPUT_URI = URINT + "dataInput"; 
	public static final String DATAOBJECT_URI = URINT + "DataObject";
	public static final String DATAOUTPUT_URI = URINT + "dataOutput"; 
	public static final String DIRECTLYPRECEDES_URI = URINT + "directlyPrecedes";
    public static final String DIRECTLYSUCCEEDS_URI = URINT + "directlySucceeds";
    public static final String INCLUDES_URI = URINT + "includes";
    public static final String STARTEVENT_URI = URINT + "StartEvent";
    public static final String STARTSTATE_URI = URINT + "StartState";
	public static final String ENDEVENT_URI = URINT + "EndEvent";
    public static final String ENDSTATE_URI = URINT + "EndState";
    public static final String PROCESS_URI = URINT + "Process";
	public static final String XORGATEWAY_URI = URINT + "XorGateway";
	public static final String GATEWAY_URI = URINT + "Gateway"; 

    public static final String PREC_URI = URINT + "precedes";
    public static final String SUCC_URI = URINT + "succeeds";
    public static final String WEAKORDER_URI = URINT + "weakOrder";
}
