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
 * Clases que convierten a owl, a partir de los objetos del modelo en un ModelHandleInterface para BPMN
 * 
 * @author Edelia
 * 
 */
public class BPMN2OWLConverter extends ToOWLConverter implements BPMN2OWLConverterInterface {

	// objeto mediante el cual se generan los axiomas que se adicionan a la ontología creada
	private GenerateBpmnAxioms generator;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param baseIRI IRI de la ontologia creada
	 * @param manager OWLOntologyManager utilizado
	 */
	public BPMN2OWLConverter(String baseIRI, OWLOntologyManager manager){
		
		super( baseIRI, manager);
	}
	
	/**
	 * Ejecuta las operaciones propias de cada subclase para generar la ontología a partir de un ModelHandleInterface
	 * 
	 * @param modelHandler Objeto ModelHandleInterface a partir del cual se genera la ontología
	 * @throws OWLOntologyCreationException
	 */
    @Override
	protected void generateOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException {
    	
    	// adiciona las declaraciones que indican las ontologías importadas en la ontología generada
    	String[] uris = { Vocabulary.URI };
    	this.addOntologyImports(uris);
    	
    	// crea el objeto mediante el cual se generan los axiomas que se adicionan a la ontología creada
		generator = new GenerateBpmnAxioms(
				this.getManager().getOWLDataFactory(), this.getManager(), this.getOntology(), this.getOntologyURI()); 
		
    	// inicializaciones
    	Bpmn20ModelHandler bpmn20ModelHandler = (Bpmn20ModelHandler) modelHandler;
		Map<String, TSequenceFlow> sequenceFlows = bpmn20ModelHandler.getSequenceFlowMap();
		Map<String, TDataObject> dataObjectList = bpmn20ModelHandler.getDataObjectMap();
		
		// adiciona a la ontología cada uno de los tipos de elementos BPMN
		this.getDeclarationIndividualsTask( bpmn20ModelHandler.getTaskMap(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsSubProcess( bpmn20ModelHandler.getSubProcessMap(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsStartEvent( bpmn20ModelHandler.getStartEventMap(), sequenceFlows);
		this.getDeclarationIndividualsEndEvent( bpmn20ModelHandler.getEndEventMap() );
		this.getDeclarationIndividualsXorGateways( bpmn20ModelHandler.getExclusiveGatewayMap(), sequenceFlows);
		this.getDeclarationIndividualsGateways( bpmn20ModelHandler.getGatewayMap(), sequenceFlows);
		this.getDeclarationIndividualsDataObject(dataObjectList);
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo task
	 * 
	 * @param taskList Map de las tareas
	 * @param dataObjectList Mapa de los dataobjects en el proceso
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 */
	private void getDeclarationIndividualsTask (Map<String, TTask> taskList, Map<String, TDataObject> dataObjectList, Map<String, TSequenceFlow> sequenceFlows){

		// para cada una de las tareas
		Iterator<Entry<String, TTask>> itInst = taskList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TTask> pairs = (Map.Entry<String, TTask>)itInst.next();
	        TTask element = pairs.getValue();
			
			// obtiene el id de la tarea
			String nameTask = element.getId();
			
			// obtiene los dataobjects con los que se relaciona la tarea
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

			// obtiene los elementos que son directamente precedidos por la tarea
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			
			// genera los axiomas correspondientes a la tarea
			generator.converterActivityOWL(nameTask, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo subprocess
	 * 
	 * @param subprocessList Mapa de subprocess
	 * @param dataObjectList Mapa de los dataobjects en el proceso
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 */
	private void getDeclarationIndividualsSubProcess (Map<String, TSubProcess> subprocessList, Map<String, TDataObject> dataObjectList, Map<String, TSequenceFlow> sequenceFlows){
		
		// para cada uno de los subprocesos
		Iterator<Entry<String, TSubProcess>> itInst = subprocessList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TSubProcess> pairs = (Map.Entry<String, TSubProcess>)itInst.next();
	        TSubProcess element = pairs.getValue();
			
			// obtiene el id del subproceso
			String nameActivity = element.getId();
			
			
			// obtiene los dataobjects relacionados con el subproceso
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

			// obtiene los elementos que son directamente precedidos por el subproceso
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			
			// genera los axiomas correspondientes al subproceso
		    generator.converterActivityOWL(nameActivity, nameInputDataObjList, nameOutputDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo startEvent
	 * 
	 * @param startEventList Mapa de startEvent
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 */
	private void getDeclarationIndividualsStartEvent (Map<String, TStartEvent> startEventList, Map<String, TSequenceFlow> sequenceFlows){
		
		// para cada uno de los startEvent
		Iterator<Entry<String, TStartEvent>> itInst = startEventList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TStartEvent> pairs = (Map.Entry<String, TStartEvent>)itInst.next();
	        TStartEvent element = pairs.getValue();
			
			// obtiene el id del startEvent
			String nameEvent = element.getId();
			
			// obtiene los elementos que son directamente precedidos por el startEvent
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			
			// genera los axiomas correspondientes al startEvent
		    generator.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo endEvent
	 * 
	 * @param endEventList Mapa de endEvent
	 */
	private void getDeclarationIndividualsEndEvent (Map<String, TEndEvent> endEventList){
		
		// para cada uno de los endEvent
		Iterator<Entry<String, TEndEvent>> itInst = endEventList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TEndEvent> pairs = (Map.Entry<String, TEndEvent>)itInst.next();
	        TEndEvent element = pairs.getValue();
			
			// obtiene el id del endEvent
			String nameEvent = element.getId();
			
			// genera los axiomas correspondientes al endEvent
		    generator.converterEndEventOWL(nameEvent);
		}
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo xorGateways
	 * 
	 * @param exclusiveGateways Mapa de xorGateways
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 */
	private void getDeclarationIndividualsXorGateways(Map<String, TExclusiveGateway> exclusiveGateways, Map<String, TSequenceFlow> sequenceFlows) {
		
		// para cada uno de los xorGateway
		Iterator<Entry<String, TExclusiveGateway>> itInst = exclusiveGateways.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TExclusiveGateway> pairs = (Map.Entry<String, TExclusiveGateway>)itInst.next();
	        TExclusiveGateway element = pairs.getValue();
			
			// obtiene el id del xorGateway
			String nameGtw = element.getId();
			
			// obtiene los elementos que son directamente precedidos por el xorGateway
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			
			// genera los axiomas correspondientes al xorGateway
		    generator.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo gateway
	 * 
	 * @param gatewaysList Mapa de gateways
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 */
	private void getDeclarationIndividualsGateways(Map<String, TGateway> gatewaysList, Map<String, TSequenceFlow> sequenceFlows) {
		
		// para cada uno de los gateway
		Iterator<Entry<String, TGateway>> itInst = gatewaysList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TGateway> pairs = (Map.Entry<String, TGateway>)itInst.next();
	        TGateway element = pairs.getValue();
			
			// obtiene el id del gateway
			String nameGtw = element.getId();
			
			// obtiene los elementos que son directamente precedidos por el gateway
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, element);
			
			// genera los axiomas correspondientes al gateway
		    generator.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/**
	 * Adiciona a la ontología los elementos del tipo dataObject
	 * 
	 * @param dataObjectList Mapa de dataObject
	 */
	private void getDeclarationIndividualsDataObject(Map<String, TDataObject> dataObjectList) {
		
		// para cada uno de los dataObject
		Iterator<Entry<String, TDataObject>> itInst = dataObjectList.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, TDataObject> pairs = (Map.Entry<String, TDataObject>)itInst.next();
	        TDataObject element = pairs.getValue();
			
			// obtiene el id del dataObject
			String nameDataObj = element.getId();
			
			// genera los axiomas correspondientes al dataObject
		    generator.converterDataObjectOWL(nameDataObj);
		}
	}
	
	/**
	 * Obtiene los identificadores de los elementos BPMN a los que precede un elemento dado
	 * 
	 * @param sequenceFlows Mapa de los conectores sequenceFlow del proceso
	 * @param task Elemento del que se desea conocer los elementos que precede
	 * @return Lista de identificadores de los elementos BPMN identificados
	 */
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
	
	/**
	 * Obtiene el identificador de un elemento BPMN
	 * 
	 * @param obj Objeto del modelo BPMN
	 * @return Identificador
	 */
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
