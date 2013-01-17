package es.us.isa.bpmn.owl.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.ModelHandleInterface;
import es.us.isa.bpmn.owl.notation.Vocabulary;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataInputAssociation;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataOutputAssociation;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TFlowElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TFlowNode;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;

/**
 * 
 * @author Edelia
 * 
 */
public class BPMN2OWLConverter extends ToOWLConverter implements BPMN2OWLConverterInterface {

	private GenerateBpmnAxioms generator;
	
	public BPMN2OWLConverter(String baseIRI, OWLOntologyManager manager){
		
		super( baseIRI, manager);
	}
	
    @Override
	protected void generateOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException {
    	
    	String[] uris = { Vocabulary.URI };
    	this.addOntologyImports(uris);
    	
		generator = new GenerateBpmnAxioms(
				this.getManager().getOWLDataFactory(), this.getManager(), this.getOntology(), this.getOntologyURI()); 
    	Bpmn20ModelHandler bpmn20ModelHandler = (Bpmn20ModelHandler) modelHandler;
		
		List<TSequenceFlow> sequenceFlows = bpmn20ModelHandler.getSequenceFlowList();
		List <TDataObject> dataObjectList = bpmn20ModelHandler.getDataObjectList();
		
		this.getDeclarationIndividualsTask( bpmn20ModelHandler.getTaskList(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsSubProcess( bpmn20ModelHandler.getSubProcessList(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsStartEvent( bpmn20ModelHandler.getStartEventList(), sequenceFlows);
		this.getDeclarationIndividualsEndEvent( bpmn20ModelHandler.getEndEventList() );
		this.getDeclarationIndividualsXorGateways( bpmn20ModelHandler.getExclusiveGatewayList(), sequenceFlows);
		this.getDeclarationIndividualsGateways( bpmn20ModelHandler.getGatewayList(), sequenceFlows);
		this.getDeclarationIndividualsDataObject(dataObjectList);
	}
	
	/***Declaraciones en owl de taskList de BPMN2.0 ***/
	private void getDeclarationIndividualsTask (List<TTask> taskList, List <TDataObject> dataObjectList, List<TSequenceFlow> sequenceFlows){
		
		//Funcion para crear los individuals de los elementos Task
		Iterator<?> itr = taskList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TTask element = (TTask) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameTask = this.getBpmnId(element);
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameOutputDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameOutputDataObj = this.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart(), dataObjectList);
			}

			List<String> nameInputDataObjList = new ArrayList<String>();
			List<TDataInputAssociation> dataInput = element.getDataInputAssociation();
			for (TDataInputAssociation tDataInputAssociation : dataInput) {
				
				List<JAXBElement<Object>> XXX = tDataInputAssociation.getSourceRef();

				Iterator<JAXBElement<Object>> itObj = XXX.iterator();
				while (itObj.hasNext()) {
					
					JAXBElement<Object> tdataobject = (JAXBElement<Object>) itObj.next();
					nameInputDataObjList.add( ((TDataObject) tdataobject.getValue()).getId() );
				}
			}

			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
			generator.converterActivityOWL(nameTask, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de subProcess de BPMN2.0 ***/
	private void getDeclarationIndividualsSubProcess (List<TSubProcess> subprocessList, List <TDataObject> dataObjectList, List<TSequenceFlow> sequenceFlows){
		
		Iterator<?> itr = subprocessList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TSubProcess element = (TSubProcess) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameActivity = this.getBpmnId(element);
			
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameOutputDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameOutputDataObj = this.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart(), dataObjectList);
			}

			List<String> nameInputDataObjList = new ArrayList<String>();
			List<TDataInputAssociation> dataInput = element.getDataInputAssociation();
			for (TDataInputAssociation tDataInputAssociation : dataInput) {
				
				List<JAXBElement<Object>> XXX = tDataInputAssociation.getSourceRef();

				Iterator<JAXBElement<Object>> itObj = XXX.iterator();
				while (itObj.hasNext()) {
					
					JAXBElement<Object> tdataobject = (JAXBElement<Object>) itObj.next();
					nameInputDataObjList.add( ((TDataObject) tdataobject.getValue()).getId() );
				}
			}

			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    generator.converterActivityOWL(nameActivity, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de startEvents de BPMN2.0 ***/
	private void getDeclarationIndividualsStartEvent (List<TStartEvent> startEventList, List<TSequenceFlow> sequenceFlows){
		
		//Funcion para crear los individuals de los elementos startEvent
		Iterator<?> itr = startEventList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TStartEvent element = (TStartEvent) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = this.getBpmnId(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    generator.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/***Declaraciones en owl de EndEvents de BPMN2.0 ***/
	private void getDeclarationIndividualsEndEvent (List<TEndEvent> endEventList){
		
		Iterator<?> itr = endEventList.iterator(); 
		while(itr.hasNext()) {
			
			TEndEvent element = (TEndEvent) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = this.getBpmnId(element);
			
		    generator.converterEndEventOWL(nameEvent);
		}
	}
	
	/***Declaraciones en owl de exclusiveGateways de BPMN2.0 ***/
	private void getDeclarationIndividualsXorGateways(List<TExclusiveGateway> exclusiveGateways, List<TSequenceFlow> sequenceFlows) {
		
		Iterator<?> itr = exclusiveGateways.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TExclusiveGateway element = (TExclusiveGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = this.getBpmnId(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    generator.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/***Declaraciones en owl de Gateways generales de BPMN2.0 ***/
	private void getDeclarationIndividualsGateways(List<TGateway> TGatewaysList, List<TSequenceFlow> sequenceFlows) {
		
		Iterator<?> itr = TGatewaysList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TGateway element = (TGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = this.getBpmnId(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    generator.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de DataObjects de BPMN2.0 ***/
	private void getDeclarationIndividualsDataObject(List<TDataObject> dataObjectList) {
		
		Iterator<?> itr = dataObjectList.iterator(); 
		while(itr.hasNext()) {
			
			TDataObject element = (TDataObject) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameDataObj = this.getBpmnId(element);
			
		    generator.converterDataObjectOWL(nameDataObj);
		}
	}
	
	private String getNameDataObject(String idDataObject, List <TDataObject> dataObjectList){

		Iterator<TDataObject> itObj = dataObjectList.iterator();
		Boolean enc = false;
		String name = null;
		while (itObj.hasNext() && !enc) {
			
			TDataObject tdataobject = (TDataObject) itObj.next();
			if(tdataobject.getId().trim().equals(idDataObject.trim())){
				
				name = this.getBpmnId(tdataobject);
				enc = true;
			}
		}
		return name;
	}
	
	private List<String> getDirectlyPrecedes(List<TSequenceFlow> sequenceFlowList, Object task) {
		
		Iterator<?> itr = sequenceFlowList.iterator(); 
		List<String> targetList = new ArrayList<String>();
		
		while(itr.hasNext()) {
			
			TSequenceFlow element = (TSequenceFlow) itr.next();
			if(element.getSourceRef().equals(task)){
				
				targetList.add( this.getBpmnId( element.getTargetRef() ) );
			}
		}
		
		return targetList;
	}
	
	private String getBpmnId(Object obj) {
		
		String id = "";
		if (obj instanceof TFlowNode) {
			
			TFlowNode node = (TFlowNode) obj;
			id = node.getId();
		} else
		if (obj instanceof TFlowElement) {
			
			TFlowElement node = (TFlowElement) obj;
			id = node.getId();
		}
		return id;
	}
	
}
