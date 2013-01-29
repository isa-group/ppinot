package es.us.isa.bpmn.handler;

import java.util.Map;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;


/**
 * Interfaz de las clases que permiten exportar e importar a XMLs de BPMN 2.0
 * 
 * @author Edelia
 *
 */
public interface Bpmn20ModelHandlerInterface extends ModelHandleInterface {
	
	/**
	 * Devuelve la instancia de clase Jaxb del proceso
	 * 
	 * @return Objeto TProcess
	 */
	public TProcess getProcess();
	
	/** 
	 * Devuelve el mapa de TTask en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TTask> getTaskMap();
	/** 
	 * Devuelve el mapa de TStartEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TStartEvent> getStartEventMap();
	/** 
	 * Devuelve el mapa de TEndEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TEndEvent> getEndEventMap();
	/** 
	 * Devuelve el mapa de TDataObject en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TDataObject> getDataObjectMap();
	/** 
	 * Devuelve el mapa de TSequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSequenceFlow> getSequenceFlowMap();
	/** 
	 * Devuelve el mapa de TGateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TGateway> getGatewayMap();
	/** 
	 * Devuelve el mapa de TExclusiveGateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap();
	/** 
	 * Devuelve el mapa de TSubProcess en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSubProcess> getSubProcessMap();
}
