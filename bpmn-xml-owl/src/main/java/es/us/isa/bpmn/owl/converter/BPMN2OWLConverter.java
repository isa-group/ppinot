package es.us.isa.bpmn.owl.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		
		Map<String, TSequenceFlow> sequenceFlows = bpmn20ModelHandler.getSequenceFlowMap();
		Map<String, TDataObject> dataObjectList = bpmn20ModelHandler.getDataObjectMap();
		
		this.getDeclarationIndividualsTask( bpmn20ModelHandler.getTaskMap(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsSubProcess( bpmn20ModelHandler.getSubProcessMap(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsStartEvent( bpmn20ModelHandler.getStartEventMap(), sequenceFlows);
		this.getDeclarationIndividualsEndEvent( bpmn20ModelHandler.getEndEventMap() );
		this.getDeclarationIndividualsXorGateways( bpmn20ModelHandler.getExclusiveGatewayMap(), sequenceFlows);
		this.getDeclarationIndividualsGateways( bpmn20ModelHandler.getGatewayMap(), sequenceFlows);
		this.getDeclarationIndividualsDataObject(dataObjectList);
	}
	
	/***Declaraciones en owl de taskList de BPMN2.0 ***/
	private void getDeclarationIndividualsTask (Map<String, TTask> taskList, Map<String, TDataObject> dataObjectList, Map<String, TSequenceFlow> sequenceFlows){

		Iterator<Entry<String, TTask>> itInst = taskList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TTask> pairs = (Map.Entry<String, TTask>)itInst.next();
	        TTask element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameTask = element.getId();
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameOutputDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameOutputDataObj = ((QName) tDataOutputAssociation.getTargetRef()).getLocalPart();
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

			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			generator.converterActivityOWL(nameTask, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de subProcess de BPMN2.0 ***/
	private void getDeclarationIndividualsSubProcess (Map<String, TSubProcess> subprocessList, Map<String, TDataObject> dataObjectList, Map<String, TSequenceFlow> sequenceFlows){
		
		Iterator<Entry<String, TSubProcess>> itInst = subprocessList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TSubProcess> pairs = (Map.Entry<String, TSubProcess>)itInst.next();
	        TSubProcess element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameActivity = element.getId();
			
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameOutputDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameOutputDataObj = ((QName) tDataOutputAssociation.getTargetRef()).getLocalPart();
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

			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
		    generator.converterActivityOWL(nameActivity, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de startEvents de BPMN2.0 ***/
	private void getDeclarationIndividualsStartEvent (Map<String, TStartEvent> startEventList, Map<String, TSequenceFlow> sequenceFlows){
		
		Iterator<Entry<String, TStartEvent>> itInst = startEventList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TStartEvent> pairs = (Map.Entry<String, TStartEvent>)itInst.next();
	        TStartEvent element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = element.getId();
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
		    generator.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/***Declaraciones en owl de EndEvents de BPMN2.0 ***/
	private void getDeclarationIndividualsEndEvent (Map<String, TEndEvent> endEventList){
		
		Iterator<Entry<String, TEndEvent>> itInst = endEventList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TEndEvent> pairs = (Map.Entry<String, TEndEvent>)itInst.next();
	        TEndEvent element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = element.getId();
			
		    generator.converterEndEventOWL(nameEvent);
		}
	}
	
	/***Declaraciones en owl de exclusiveGateways de BPMN2.0 ***/
	private void getDeclarationIndividualsXorGateways(Map<String, TExclusiveGateway> exclusiveGateways, Map<String, TSequenceFlow> sequenceFlows) {
		
		Iterator<Entry<String, TExclusiveGateway>> itInst = exclusiveGateways.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TExclusiveGateway> pairs = (Map.Entry<String, TExclusiveGateway>)itInst.next();
	        TExclusiveGateway element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = element.getId();
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
		    generator.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/***Declaraciones en owl de Gateways generales de BPMN2.0 ***/
	private void getDeclarationIndividualsGateways(Map<String, TGateway> TGatewaysList, Map<String, TSequenceFlow> sequenceFlows) {
		
		Iterator<Entry<String, TGateway>> itInst = TGatewaysList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TGateway> pairs = (Map.Entry<String, TGateway>)itInst.next();
	        TGateway element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = element.getId();
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
		    generator.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de DataObjects de BPMN2.0 ***/
	private void getDeclarationIndividualsDataObject(Map<String, TDataObject> dataObjectList) {
		
		Iterator<Entry<String, TDataObject>> itInst = dataObjectList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TDataObject> pairs = (Map.Entry<String, TDataObject>)itInst.next();
	        TDataObject element = pairs.getValue();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameDataObj = element.getId();
			
		    generator.converterDataObjectOWL(nameDataObj);
		}
	}
	
	private List<String> getDirectlyPrecedes(Map<String, TSequenceFlow> sequenceFlowList, Object task) {
		
		List<String> targetList = new ArrayList<String>();
		
		Iterator<Entry<String, TSequenceFlow>> itInst = sequenceFlowList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TSequenceFlow> pairs = (Map.Entry<String, TSequenceFlow>)itInst.next();
	        TSequenceFlow element = pairs.getValue();
			
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
