package es.us.isa.bpmn.owl.converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import es.us.isa.bpmn.owl.notation.Vocabulary;
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
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;

/**
 * @author Edelia Garcia
 */
public class BPMN2OWLConverter implements BPMN2OWL {

	private OWLOntologyManager manager;	// OWLOntologyManager utilizado
	private String baseIRI;				// IRI de la ontologia creada

	private OWLOntology ontology;		// Ontologia a la que se adicionan los axiomas
	private GenerateBpmnOWL converter;
	private String ontologyURI;			// URI de la ontologia creada
	
	public BPMN2OWLConverter(String baseIRI, OWLOntologyManager manager){
		
		this.baseIRI = baseIRI;
		this.manager = manager;
	}
	
    @Override
	public OWLOntology convertToOwlOntology(Bpmn20XmlExtracter bpmn20XmlExtracter) throws OWLOntologyCreationException {

    	ontologyURI = baseIRI + bpmn20XmlExtracter.getProcess().getId() + ".owl";
    	
		ontology = manager.createOntology(IRI.create(ontologyURI));
		manager.applyChange(new AddImport(ontology, manager.getOWLDataFactory().getOWLImportsDeclaration( IRI.create( Vocabulary.URI ) )));

		converter = new GenerateBpmnOWL(manager.getOWLDataFactory(), manager, ontology, ontologyURI);
		
		List<TSequenceFlow> sequenceFlows = bpmn20XmlExtracter.getSequenceFlowList();
		List <TDataObject> dataObjectList = bpmn20XmlExtracter.getDataObjectList();
		
		this.getDeclarationIndividualsTask( bpmn20XmlExtracter.getTaskList(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsSubProcess( bpmn20XmlExtracter.getSubProcessList(), dataObjectList, sequenceFlows);
		this.getDeclarationIndividualsStartEvent( bpmn20XmlExtracter.getStartEventList(), sequenceFlows);
		this.getDeclarationIndividualsEndEvent( bpmn20XmlExtracter.getEndEventList() );
		this.getDeclarationIndividualsXorGateways( bpmn20XmlExtracter.getExclusiveGatewayList(), sequenceFlows);
		this.getDeclarationIndividualsGateways( bpmn20XmlExtracter.getGatewayList(), sequenceFlows);
		this.getDeclarationIndividualsDataObject(dataObjectList);

		return this.ontology;
	}
	
	public String getOntologyURI() {
		return ontologyURI;
	}
	
	public void saveOntology(String caminoDestino, String bpmnFilename) {
		
		try {
			
			File bpmnFile = new File(caminoDestino + bpmnFilename);
			bpmnFile.createNewFile();
			
			manager.saveOntology(ontology, IRI.create(bpmnFile.toURI()));
		} catch (OWLOntologyStorageException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/***Declaraciones en owl de taskList de BPMN2.0 ***/
	private void getDeclarationIndividualsTask (List<TTask> taskList, List <TDataObject> dataObjectList, List<TSequenceFlow> sequenceFlows){
		
		//Funcion para crear los individuals de los elementos Task
		Iterator<?> itr = taskList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TTask element = (TTask) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameTask = this.getCleanName(element);
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameDataObj = this.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart(), dataObjectList);
			}
		    List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
			converter.converterActivityOWL(nameTask, nameDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de subProcess de BPMN2.0 ***/
	private void getDeclarationIndividualsSubProcess (List<TSubProcess> subprocessList, List <TDataObject> dataObjectList, List<TSequenceFlow> sequenceFlows){
		
		Iterator<?> itr = subprocessList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TSubProcess element = (TSubProcess) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameActivity = this.getCleanName(element);
			
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameDataObj = this.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart(), dataObjectList);
			}
			 List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterActivityOWL(nameActivity, nameDataObj, elementsDirectlyPrecedes);
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
			String nameEvent = this.getCleanName(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/***Declaraciones en owl de EndEvents de BPMN2.0 ***/
	private void getDeclarationIndividualsEndEvent (List<TEndEvent> endEventList){
		
		Iterator<?> itr = endEventList.iterator(); 
		while(itr.hasNext()) {
			
			TEndEvent element = (TEndEvent) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = this.getCleanName(element);
			
		    converter.converterEndEventOWL(nameEvent);
		}
	}
	
	/***Declaraciones en owl de exclusiveGateways de BPMN2.0 ***/
	private void getDeclarationIndividualsXorGateways(List<TExclusiveGateway> exclusiveGateways, List<TSequenceFlow> sequenceFlows) {
		
		Iterator<?> itr = exclusiveGateways.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TExclusiveGateway element = (TExclusiveGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = this.getCleanName(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/***Declaraciones en owl de Gateways generales de BPMN2.0 ***/
	private void getDeclarationIndividualsGateways(List<TGateway> TGatewaysList, List<TSequenceFlow> sequenceFlows) {
		
		Iterator<?> itr = TGatewaysList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TGateway element = (TGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = this.getCleanName(element);
			
			List<String> elementsDirectlyPrecedes = this.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de DataObjects de BPMN2.0 ***/
	private void getDeclarationIndividualsDataObject(List<TDataObject> dataObjectList) {
		
		Iterator<?> itr = dataObjectList.iterator(); 
		while(itr.hasNext()) {
			
			TDataObject element = (TDataObject) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameDataObj = this.getCleanName(element);
			
		    converter.converterDataObjectOWL(nameDataObj);
		}
	}
	
	private String getNameDataObject(String idDataObject, List <TDataObject> dataObjectList){

		Iterator<TDataObject> itObj = dataObjectList.iterator();
		Boolean enc = false;
		String name = null;
		while (itObj.hasNext() && !enc) {
			
			TDataObject tdataobject = (TDataObject) itObj.next();
			if(tdataobject.getId().trim().equals(idDataObject.trim())){
				name = tdataobject.getName();
				name = name.replaceAll(" ", "");
				enc = true;
			}
		}
		return name;
	}
	
	private String getCleanName(Object obj) {
		
		String name = "";
		if (obj instanceof TFlowNode) {
			
			TFlowNode node = (TFlowNode) obj;
			name = node.getName();
			if (name.contentEquals(""))
				name = node.getId();
			else
				name = name.replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "");
		} else
		if (obj instanceof TFlowElement) {
			
			TFlowElement node = (TFlowElement) obj;
			name = node.getName();
			if (name.contentEquals(""))
				name = node.getId();
			else
				name = name.replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "");
		}
		return name;
	}
	
	private List<String> getDirectlyPrecedes(List<TSequenceFlow> sequenceFlowList, Object task) {
		
		Iterator<?> itr = sequenceFlowList.iterator(); 
		List<String> targetList = new ArrayList<String>();
		
		while(itr.hasNext()) {
			
			TSequenceFlow element = (TSequenceFlow) itr.next();
			if(element.getSourceRef().equals(task)){
				
				targetList.add( this.getCleanName( element.getTargetRef() ) );
			}
		}
		
		return targetList;
	}
	
}
