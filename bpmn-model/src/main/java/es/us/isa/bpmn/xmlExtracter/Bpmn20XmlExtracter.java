package es.us.isa.bpmn.xmlExtracter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
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

public class Bpmn20XmlExtracter extends XmlExtracter {

	private List <TTask> taskList;
	private List <TStartEvent> startEventList;
	private List <TEndEvent> endEventList;
	private List <TDataObject> dataObjectList;
	private List <TSequenceFlow> sequenceFlowList;
	private List <TExclusiveGateway> exclusiveGtwList;
	private List <TGateway> gatewayList;
	private List <TSubProcess> subProcessList;

	public Bpmn20XmlExtracter() throws JAXBException {
		
		super();
	}
	
	/** Devuelve la lista de Tareas del modelo del proceso Bpmn2.0**/
	public List<TTask> getTaskList(){
		return taskList;        	
	}
	
	/** Devuelve la lista de StartEvent del modelo del proceso Bpmn2.0**/
	public List<TStartEvent> getStartEventList(){
		return startEventList;
	}
	
	/** Devuelve la lista de EndEvent del modelo del proceso Bpmn2.0**/
	public List<TEndEvent> getEndEventList(){
		return endEventList;
	}
	
	/** Devuelve la lista de DataObject del modelo del proceso Bpmn2.0**/
	public List<TDataObject> getDataObjectList(){
		return dataObjectList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public List<TSequenceFlow> getSequenceFlowList(){
		return sequenceFlowList;
	}
	
	/** Devuelve la lista de Gateway del modelo del proceso Bpmn2.0**/
	public List<TGateway> getGatewayList(){
		return gatewayList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public List<TExclusiveGateway> getExclusiveGatewayList(){
		return exclusiveGtwList;
	}
	
	/** Devuelve la lista de SubProcess del modelo del proceso Bpmn2.0**/
	public List<TSubProcess> getSubProcessList(){
		return subProcessList;
	}
	
	@Override
	protected void iniExtracter() throws JAXBException {
		
		taskList = new ArrayList<TTask>();
		startEventList = new ArrayList<TStartEvent>();
		endEventList = new ArrayList<TEndEvent>();
		dataObjectList = new ArrayList<TDataObject>();
		sequenceFlowList = new ArrayList<TSequenceFlow>();
		exclusiveGtwList = new ArrayList<TExclusiveGateway>();
		gatewayList = new ArrayList<TGateway>();
		subProcessList = new ArrayList<TSubProcess>();

		// crea el JAXBContext para hacer marshall y unmarshall
		this.setJc( JAXBContext.newInstance( 
				es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class, 
				es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class ) );

		// crea un objeto de la clase ObjectFactory para Bpmn
        this.setFactory( new es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory() );
	}
	
	public ObjectFactory getFactory() {
	
		return (ObjectFactory) super.getFactory();
	}

	@Override
	protected void generateExportElement(String procId) {
		
	}
	
	@Override
	public void generateModelLists(){
		
		TProcess process= (TProcess) ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		
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
		    	startEventList.add((TStartEvent)startEvent);
		    	
		    	
		    }else if(contentElement instanceof TEndEvent){
		    	
		    	TEndEvent endEvent = (TEndEvent) contentElement;
		    	endEvent.getName();
		    	endEventList.add(endEvent);
		    }
		    
		    //Activity Elements
		    else if(contentElement instanceof TTask) {
		    
		    	TTask task = (TTask) contentElement;
		    	task.getName();
		    	taskList.add(task);
		    }
		    
		    //Activity Elements
		    else if(contentElement instanceof TSubProcess) {
		    
		    	TSubProcess subprocess = (TSubProcess) contentElement;
		    	subprocess.getName();
		    	subProcessList.add(subprocess);
		    }
		    
		    
		    //Data Elements
		    else if(contentElement instanceof TDataObject){
		    	TDataObject dataObject = (TDataObject) contentElement;
		    	dataObject.getName();
		    	dataObjectList.add(dataObject);
		    	
		    }
		    
		    //Sequence Elements
		    else if(contentElement instanceof TSequenceFlow){
		    	TSequenceFlow sequenceFlow = (TSequenceFlow) contentElement;
		    	sequenceFlow.getName();
		    	sequenceFlowList.add(sequenceFlow);
		    }
		    
		    //Adela me ha dicho que le interesa diferenciar el XOR Gateways de los demás,
		    //Pero el resto de Gateways se van a tratar igual, independientemente del tipo
		    //TExclusiveGateway Elements
		    else if(contentElement instanceof TExclusiveGateway){
		    	
		    	TExclusiveGateway exclusivegtw = (TExclusiveGateway) contentElement;
		    	exclusivegtw.getName();
		    	exclusiveGtwList.add(exclusivegtw);
		    }
		    
		    //Gateway Elements
		    else if(contentElement instanceof TGateway){
		    	
		    	TGateway gtw = (TGateway) contentElement;
		    	gtw.getName();
		    	gatewayList.add(gtw);
		    }
		}
	}
	
	/** Devuelve el tipo de la Actividad**/
	public TTask isTask(String idActivity) throws Exception{
		
		Iterator<TTask> it = this.getTaskList().iterator();
		TTask obj = null;
		while (it.hasNext()) {
			
			TTask element = (TTask) it.next();
			if(element.getId()== idActivity){
				obj = element;
				break;
			}
		}

		return obj;
	}
	
	public TSubProcess isSubProcess(String idActivity) throws Exception{
		
		Iterator<TSubProcess> itsub = this.getSubProcessList().iterator();
		TSubProcess obj = null;
		while (itsub.hasNext()) {
			
			TSubProcess element = (TSubProcess) itsub.next();
			if(element.getId()== idActivity){
				obj = element;
				break;
			}
		}
		return obj;
	}
	
	public TDataObject isDataObject(String idActivity) throws Exception{
		
		Iterator<TDataObject> itObj = this.getDataObjectList().iterator();
		TDataObject obj = null;
		while (itObj.hasNext()) {
			
			TDataObject element = (TDataObject) itObj.next();
			if(element.getId()== idActivity){
				obj = element;
				break;
			}
		}
		return obj;
	}
	
	public TStartEvent isStartEvent(String idActivity) throws Exception{
		
		Iterator<TStartEvent> itstart = this.getStartEventList().iterator();
		TStartEvent obj = null;
		while (itstart.hasNext()) {
			
			TStartEvent element = (TStartEvent) itstart.next();
			if(element.getId()== idActivity){
				obj = element;
				break;
			}
		}
		return obj;
	}
	
	public TEndEvent isEndEvent(String idActivity) throws Exception{
		
		Iterator<TEndEvent> itend = this.getEndEventList().iterator();
		TEndEvent obj = null;
		while (itend.hasNext()) {
			
			TEndEvent element = (TEndEvent) itend.next();
			if(element.getId()== idActivity){
				obj = element;
				break;
			}
		}
		return obj;
	}
}
