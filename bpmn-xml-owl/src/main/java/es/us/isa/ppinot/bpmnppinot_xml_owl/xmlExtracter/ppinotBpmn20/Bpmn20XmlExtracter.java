package es.us.isa.ppinot.bpmnppinot_xml_owl.xmlExtracter.ppinotBpmn20;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TFlowElement;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TParallelGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;

/**
 * @author Ana Belen Sanchez Jerez
 * **/

public class Bpmn20XmlExtracter {
//Clase para extraer todos los elementos de BPMN20 necesarios
	
	public static List <TTask> taskList;
	public static List <TStartEvent> startEventList;
	public static List <TEndEvent> endEventList;
	public static List <TDataObject> dataObjectList;
	public static List <TSequenceFlow> sequenceFlowList;
	public static List <TExclusiveGateway> exclusiveGtwList;
	public static List <TGateway> gatewayList;
	public static List <TSubProcess> subProcessList;
	
	/** Devuelve la lista de Tareas del modelo del proceso Bpmn2.0**/
	public static List<TTask> getTaskList(){
		return taskList;        	
	}
	
	/** Devuelve la lista de StartEvent del modelo del proceso Bpmn2.0**/
	public static List<TStartEvent> getStartEventList(){
		return startEventList;
	}
	
	/** Devuelve la lista de EndEvent del modelo del proceso Bpmn2.0**/
	public static List<TEndEvent> getEndEventList(){
		return endEventList;
	}
	
	/** Devuelve la lista de DataObject del modelo del proceso Bpmn2.0**/
	public static List<TDataObject> getDataObjectList(){
		return dataObjectList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public static List<TSequenceFlow> getSequenceFlowList(){
		return sequenceFlowList;
	}
	
	/** Devuelve la lista de Gateway del modelo del proceso Bpmn2.0**/
	public static List<TGateway> getGatewayList(){
		return gatewayList;
	}
	
	/** Devuelve la lista de SequenceFlow del modelo del proceso Bpmn2.0**/
	public static List<TExclusiveGateway> getExclusiveGatewayList(){
		return exclusiveGtwList;
	}
	
	/** Devuelve la lista de SubProcess del modelo del proceso Bpmn2.0**/
	public static List<TSubProcess> getSubProcessList(){
		return subProcessList;
	}
	
	public static void getBpmn20elements(JAXBElement jaxbElement){
		
		//Initialize lists
		
		taskList = new ArrayList<TTask>();
		startEventList = new ArrayList<TStartEvent>();
		endEventList = new ArrayList<TEndEvent>();
		dataObjectList = new ArrayList<TDataObject>();
		sequenceFlowList = new ArrayList<TSequenceFlow>();
		exclusiveGtwList = new ArrayList<TExclusiveGateway>();
		gatewayList = new ArrayList<TGateway>();
		subProcessList = new ArrayList<TSubProcess>();
		
		TProcess process= (TProcess) ((TDefinitions) jaxbElement.getValue()).getRootElement().get(0).getValue();
		
		List<?> flowElements= process.getFlowElement();
		
		Iterator<?> itr = flowElements.iterator(); 
		
		//Iteremos sobre todos los elementos obtenidos BPMN20
		while(itr.hasNext()) {
			JAXBElement<?> element = (JAXBElement<?>) itr.next();
		    //System.out.println(itr.next()); 
		    System.out.print("Tipo Elemento" +element.getValue() + " ");
		    Object contentElement = element.getValue();
		    
		    //Event Elements
		    if (contentElement instanceof TStartEvent) {
			   System.out.println( ((TStartEvent) contentElement).getEventDefinition());
		    	TStartEvent startEvent = (TStartEvent) contentElement;
		    	startEvent.getName();
		    	
		    	System.out.println("Start Event: "+ startEvent.getName());
		    	/*Esto es para obtener obtener el tipo de StartEvent que es y acceder a la clase TTimerEventDefinition en este caso
		    	System.out.println("Start Event: "+startEvent.getEventDefinition().get(0).getValue().getClass() );
		    	*/
		    	startEventList.add((TStartEvent)startEvent);
		    	
		    	
		    }else if(contentElement instanceof TEndEvent){
		    	TEndEvent endEvent = (TEndEvent) contentElement;
		    	endEvent.getName();
		    	System.out.println("SequenceFlow: "+ endEvent.getName());
		    	endEventList.add(endEvent);
		    }
		    
		    //Activity Elements
		    else if(contentElement instanceof TTask) {
		    
		    	TTask task = (TTask) contentElement;
		    	task.getName();
		    	System.out.println("Task: "+ task.getName());
		    	taskList.add(task);
		    }
		    
		  //Activity Elements
		    else if(contentElement instanceof TSubProcess) {
		    
		    	TSubProcess subprocess = (TSubProcess) contentElement;
		    	subprocess.getName();
		    	System.out.println("subprocess: "+ subprocess.getName());
		    	subProcessList.add(subprocess);
		    }
		    
		    
		   //Data Elements
		    else if(contentElement instanceof TDataObject){
		    	TDataObject dataObject = (TDataObject) contentElement;
		    	dataObject.getName();
		    	System.out.println("DataObject: "+ dataObject.getName());
		    	dataObjectList.add(dataObject);
		    	
		    }
		    
		    //Sequence Elements
		    else if(contentElement instanceof TSequenceFlow){
		    	TSequenceFlow sequenceFlow = (TSequenceFlow) contentElement;
		    	sequenceFlow.getName();
		    	System.out.println("SequenceFlow: "+ sequenceFlow.getName());
		    	sequenceFlowList.add(sequenceFlow);
		    }
		   //Adela me ha dicho que le interesa diferenciar el XOR Gateways de los demás,
		    //Pero el resto de Gateways se van a tratar igual, independientemente del tipo
		  //TExclusiveGateway Elements
		    else if(contentElement instanceof TExclusiveGateway){
		    	TExclusiveGateway exclusivegtw = (TExclusiveGateway) contentElement;
		    	exclusivegtw.getName();
		    	System.out.println("exclusivegtw: "+ exclusivegtw.getName());
		    	exclusiveGtwList.add(exclusivegtw);
		    }
		    
		  //Gateway Elements
		    else if(contentElement instanceof TGateway){
		    	TGateway gtw = (TGateway) contentElement;
		    	gtw.getName();
		    	System.out.println("Gateways: "+ gtw.getName());
		    	gatewayList.add(gtw);
		    }
		}
		
		
	}
}
