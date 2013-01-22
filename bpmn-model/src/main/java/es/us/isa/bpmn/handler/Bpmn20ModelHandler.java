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
 * @author Ana Belen Sanchez Jerez
 * **/

public class Bpmn20ModelHandler extends ModelHandler implements Bpmn20ModelHandlerInterface {

	private Map<String, TTask> taskList;
	private Map<String, TStartEvent> startEventList;
	private Map<String, TEndEvent> endEventList;
	private Map<String, TDataObject> dataObjectList;
	private Map<String, TSequenceFlow> sequenceFlowList;
	private Map<String, TExclusiveGateway> exclusiveGtwList;
	private Map<String, TGateway> gatewayList;
	private Map<String, TSubProcess> subProcessList;

	public Bpmn20ModelHandler() throws JAXBException {
		
		super();
	}
	
	/** Devuelve la lista de Tareas del modelo del proceso Bpmn2.0**/
	public Map<String, TTask> getTaskMap(){
		return taskList;        	
	}
	
	/** Devuelve la lista de StartEvent del modelo del proceso Bpmn2.0**/
	public Map<String, TStartEvent> getStartEventMap(){
		return startEventList;
	}
	
	/** Devuelve la lista de EndEvent del modelo del proceso Bpmn2.0**/
	public Map<String, TEndEvent> getEndEventMap(){
		return endEventList;
	}
	
	/** Devuelve la lista de DataObject del modelo del proceso Bpmn2.0**/
	public Map<String, TDataObject> getDataObjectMap(){
		return dataObjectList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public Map<String, TSequenceFlow> getSequenceFlowMap(){
		return sequenceFlowList;
	}
	
	/** Devuelve la lista de Gateway del modelo del proceso Bpmn2.0**/
	public Map<String, TGateway> getGatewayMap(){
		return gatewayList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap(){
		return exclusiveGtwList;
	}
	
	/** Devuelve la lista de SubProcess del modelo del proceso Bpmn2.0**/
	public Map<String, TSubProcess> getSubProcessMap(){
		return subProcessList;
	}
	
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
	
	protected ObjectFactory getFactory() {
	
		return (ObjectFactory) super.getFactory();
	}

	@Override
	protected void generateExportElement(String procId) {
		
	}
	
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
	
	private void generateProcessModelLists(TProcess process){
		
		List<?> flowElements= process.getFlowElement();
		
		Iterator<?> itr = flowElements.iterator(); 
		
		//Iteremos sobre todos los elementos obtenidos BPMN20
		while(itr.hasNext()) {
			
			JAXBElement<?> element = (JAXBElement<?>) itr.next();
		    Object contentElement = element.getValue();
		    
		    //Event Elements
		    if (contentElement instanceof TStartEvent) {
		    	
		    	TStartEvent startEvent = (TStartEvent) contentElement;
		    	startEvent.getName();
		    	
		    	//Esto es para obtener obtener el tipo de StartEvent que es y acceder a la clase TTimerEventDefinition en este caso
		    	startEventList.put(startEvent.getId(), (TStartEvent)startEvent);
		    	
		    	
		    }else if(contentElement instanceof TEndEvent){
		    	
		    	TEndEvent endEvent = (TEndEvent) contentElement;
		    	endEvent.getName();
		    	endEventList.put(endEvent.getId(), endEvent);
		    }
		    
		    //Activity Elements
		    else if(contentElement instanceof TTask) {
		    
		    	TTask task = (TTask) contentElement;
		    	task.getName();
		    	taskList.put(task.getId(), task);
		    }
		    
		    //Activity Elements
		    else if(contentElement instanceof TSubProcess) {
		    
		    	TSubProcess subprocess = (TSubProcess) contentElement;
		    	subprocess.getName();
		    	subProcessList.put(subprocess.getId(), subprocess);
		    }
		    
		    
		    //Data Elements
		    else if(contentElement instanceof TDataObject){
		    	TDataObject dataObject = (TDataObject) contentElement;
		    	dataObject.getName();
		    	dataObjectList.put(dataObject.getId(), dataObject);
		    	
		    }
		    
		    //Sequence Elements
		    else if(contentElement instanceof TSequenceFlow){
		    	TSequenceFlow sequenceFlow = (TSequenceFlow) contentElement;
		    	sequenceFlow.getName();
		    	sequenceFlowList.put(sequenceFlow.getId(), sequenceFlow);
		    }
		    
		    //Adela me ha dicho que le interesa diferenciar el XOR Gateways de los demás,
		    //Pero el resto de Gateways se van a tratar igual, independientemente del tipo
		    //TExclusiveGateway Elements
		    else if(contentElement instanceof TExclusiveGateway){
		    	
		    	TExclusiveGateway exclusivegtw = (TExclusiveGateway) contentElement;
		    	exclusivegtw.getName();
		    	exclusiveGtwList.put(exclusivegtw.getId(), exclusivegtw);
		    }
		    
		    //Gateway Elements
		    else if(contentElement instanceof TGateway){
		    	
		    	TGateway gtw = (TGateway) contentElement;
		    	gtw.getName();
		    	gatewayList.put(gtw.getId(), gtw);
		    }
		}
	}

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

	@Override
	public String getProcId() {
		
		return this.getProcess().getId();
	}
}
