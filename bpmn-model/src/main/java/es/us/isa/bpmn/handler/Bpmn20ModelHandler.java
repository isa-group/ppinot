package es.us.isa.bpmn.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;

/**
 * Clase que importa y exporta xmls de BPMN 2.0
 * 
 * @author Edelia
 *
 */
public class Bpmn20ModelHandler extends ModelHandler implements Bpmn20ModelHandlerInterface {

	private Map<String, TTask> taskList;
	private Map<String, TStartEvent> startEventList;
	private Map<String, TEndEvent> endEventList;
	private Map<String, TDataObject> dataObjectList;
	private Map<String, TSequenceFlow> sequenceFlowList;
	private Map<String, TExclusiveGateway> exclusiveGtwList;
	private Map<String, TGateway> gatewayList;
	private Map<String, TSubProcess> subProcessList;

	/**
	 * Constructor de la clase
	 * 
	 * @throws JAXBException
	 */
	public Bpmn20ModelHandler() throws JAXBException {
		
		super();
	}
	
	/** 
	 * Devuelve el mapa de TTask en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TTask> getTaskMap(){
		return taskList;        	
	}
	
	/** 
	 * Devuelve el mapa de StartEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TStartEvent> getStartEventMap(){
		return startEventList;
	}
	
	/**
	 * Devuelve el mapa de EndEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TEndEvent> getEndEventMap(){
		return endEventList;
	}
	
	/**
	 * Devuelve el mapa de DataObject en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TDataObject> getDataObjectMap(){
		return dataObjectList;
	}
	
	/**
	 * Devuelve el mapa de SequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSequenceFlow> getSequenceFlowMap(){
		return sequenceFlowList;
	}
	
	/**
	 * Devuelve el mapa de Gateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TGateway> getGatewayMap(){
		return gatewayList;
	}
	
	/**
	 * Devuelve el mapa de SequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap(){
		return exclusiveGtwList;
	}
	
	/**
	 * Devuelve el mapa de SubProcess en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSubProcess> getSubProcessMap(){
		return subProcessList;
	}
	
	/**
	 * Realiza las inicializaciones.
	 * 
	 * @throws JAXBException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void iniLoader() throws JAXBException {
		
		taskList = new HashMap<String, TTask>();
		startEventList = new HashMap<String, TStartEvent>();
		endEventList = new HashMap<String, TEndEvent>();
		dataObjectList = new HashMap<String, TDataObject>();
		sequenceFlowList = new HashMap<String, TSequenceFlow>();
		exclusiveGtwList = new HashMap<String, TExclusiveGateway>();
		gatewayList = new HashMap<String, TGateway>();
		subProcessList = new HashMap<String, TSubProcess>();

		// configura las clases para leer y guardar como xml
		Class[] classList = {es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class, es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class};
		this.xmlConfig( classList, es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class );
	}
	
	/**
	 * Devuelve la factory utilizada 
	 * 
	 * @return Objeto factory
	 */
	protected ObjectFactory getFactory() {
	
		return (ObjectFactory) super.getFactory();
	}

	/**
	 * No está implementado
	 * 
	 * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
	 */
	@Override
	protected void generateExportElement(String procId) {
		
	}
	
	/**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 */
	@Override
	protected void generateModelLists(){
		
		Object object = ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		if (object instanceof TProcess) {
			
			this.generateProcessModelLists((TProcess) object);
		} else {
			
			for (JAXBElement<?> element : ((TDefinitions) this.getImportElement().getValue()).getRootElement()) {
				
				Object participant = element.getValue();
				if (participant instanceof TProcess) {
					
					this.generateProcessModelLists((TProcess) participant);
				}
			}
		}
		
	}
	
	/**
	 * Genera las instancias de clases del modelo, del proceso indicado
	 * 
	 * @param process Clase Jaxb del proceso
	 */
	private void generateProcessModelLists(TProcess process){
		
		List<?> flowElements= process.getFlowElement();
		
		Iterator<?> itr = flowElements.iterator(); 
		
		// itera por todos los elementos en el proceso, y de acuerdo a su tipo lo sitúa en el mapa correspondiente
		while(itr.hasNext()) {
			
			JAXBElement<?> element = (JAXBElement<?>) itr.next();
		    Object contentElement = element.getValue();
		    
		    if (contentElement instanceof TStartEvent) {
		    	
		    	TStartEvent startEvent = (TStartEvent) contentElement;
		    	startEvent.getName();
		    	
		    	startEventList.put(startEvent.getId(), (TStartEvent)startEvent);
		    	
		    	
		    }else if(contentElement instanceof TEndEvent){
		    	
		    	TEndEvent endEvent = (TEndEvent) contentElement;
		    	endEvent.getName();
		    	endEventList.put(endEvent.getId(), endEvent);
		    }
		    
		    else if(contentElement instanceof TTask) {
		    
		    	TTask task = (TTask) contentElement;
		    	task.getName();
		    	taskList.put(task.getId(), task);
		    }
		    
		    else if(contentElement instanceof TSubProcess) {
		    
		    	TSubProcess subprocess = (TSubProcess) contentElement;
		    	subprocess.getName();
		    	subProcessList.put(subprocess.getId(), subprocess);
		    }
		    
		    
		    else if(contentElement instanceof TDataObject){
		    	TDataObject dataObject = (TDataObject) contentElement;
		    	dataObject.getName();
		    	dataObjectList.put(dataObject.getId(), dataObject);
		    	
		    }
		    
		    else if(contentElement instanceof TSequenceFlow){
		    	TSequenceFlow sequenceFlow = (TSequenceFlow) contentElement;
		    	sequenceFlow.getName();
		    	sequenceFlowList.put(sequenceFlow.getId(), sequenceFlow);
		    }
		    
		    else if(contentElement instanceof TExclusiveGateway){
		    	
		    	TExclusiveGateway exclusivegtw = (TExclusiveGateway) contentElement;
		    	exclusivegtw.getName();
		    	exclusiveGtwList.put(exclusivegtw.getId(), exclusivegtw);
		    }
		    
		    else if(contentElement instanceof TGateway){
		    	
		    	TGateway gtw = (TGateway) contentElement;
		    	gtw.getName();
		    	gatewayList.put(gtw.getId(), gtw);
		    }
		}
	}

	/**
	 * Devuelve el objeto Jaxb del proceso
	 * 
	 * @return Objeto TProcess
	 */
	public TProcess getProcess() {
		
		TProcess process = null;
		Object object = ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		if (object instanceof TProcess) {
			
			process = (TProcess) object;
		} else {
			
			for (JAXBElement<?> element : ((TDefinitions) this.getImportElement().getValue()).getRootElement()) {
				
				Object participant = element.getValue();
				if (participant instanceof TProcess) {
					
					process = (TProcess) participant;
					break;
				}
			}
		}
		
		return process;
	}

	/**
	 * Devuelve el id del proceso involucrado en el xml
	 * 
	 * @return Id del proceso
	 */
	@Override
	public String getProcId() {
		
		return this.getProcess().getId();
	}
}
