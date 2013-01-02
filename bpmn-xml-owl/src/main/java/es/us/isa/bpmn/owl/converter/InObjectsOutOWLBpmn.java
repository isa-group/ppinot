package es.us.isa.bpmn.owl.converter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataOutputAssociation;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;

/**
 * @author Ana Belen Sanchez Jerez
 */
public class InObjectsOutOWLBpmn {

	private OWLOntologyManager bpmnMan;	// OWLOntologyManager utilizado
	private OWLOntology bpmnOnt;		// Ontologia a la que se adicionan los axiomas
	private String orgbpmn; 			// URI de la ontologia BPMN
	private String orgbpmnExpr;

	File bpmnFile;

	GenerateOWLBpmn converter;
	List<TSequenceFlow> sequenceFlows;
	
	/**
	 * Constructor de la clase inObjectsOutOWL que inicializa todos los objetos
	 * necesarios de la api OWL para poder trabajar con ellos. Inicializa 
	 * OWLOntology,OWLDataFactory, las urls a ficheros owl. 
	 */
	public InObjectsOutOWLBpmn(String caminoDestino, String bpmnFilename, String orgbpmnIn, OWLOntologyManager bpmnManIn, List<TSequenceFlow> sequenceFlowList){
		
		orgbpmn = orgbpmnIn;
		bpmnMan = bpmnManIn;
		sequenceFlows = sequenceFlowList; 
		
		try {

			bpmnOnt = bpmnMan.createOntology();
			
			bpmnFile = new File(caminoDestino+bpmnFilename);
			bpmnFile.createNewFile();
			
			orgbpmnExpr = bpmnFile.toURI().toString();
			converter = new GenerateOWLBpmn(bpmnMan.getOWLDataFactory(), bpmnMan, bpmnOnt, orgbpmn, orgbpmnExpr);
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		

	}
	
	public String getOrgbpmnExpr() {
		return orgbpmnExpr;
	}
	
	/***Declaraciones en owl de taskList de BPMN2.0 ***/
	public void getDeclarationIndividualsTask (List<TTask> taskList){
		
		//Funcion para crear los individuals de los elementos Task
		Iterator<?> itr = taskList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TTask element = (TTask) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameTask = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameDataObj = UtilsObjectOWLBpmn.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart());
			}
		    List<String> elementsDirectlyPrecedes = UtilsObjectOWLBpmn.getDirectlyPrecedes(sequenceFlows, obj);
			converter.converterActivityOWL(nameTask, nameDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de subProcess de BPMN2.0 ***/
	public void getDeclarationIndividualsSubProcess (List<TSubProcess> subprocessList){
		
		Iterator<?> itr = subprocessList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TSubProcess element = (TSubProcess) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameActivity = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				
				nameDataObj = UtilsObjectOWLBpmn.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart());
			}
			 List<String> elementsDirectlyPrecedes = UtilsObjectOWLBpmn.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterActivityOWL(nameActivity, nameDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de startEvents de BPMN2.0 ***/
	public void getDeclarationIndividualsStartEvent (List<TStartEvent> startEventList){
		
		//Funcion para crear los individuals de los elementos startEvent
		Iterator<?> itr = startEventList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TStartEvent element = (TStartEvent) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
			List<String> elementsDirectlyPrecedes = UtilsObjectOWLBpmn.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/***Declaraciones en owl de EndEvents de BPMN2.0 ***/
	public void getDeclarationIndividualsEndEvent (List<TEndEvent> endEventList){
		
		Iterator<?> itr = endEventList.iterator(); 
		while(itr.hasNext()) {
			
			TEndEvent element = (TEndEvent) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameEvent = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
		    converter.converterEndEventOWL(nameEvent);
		}
	}
	
	/***Declaraciones en owl de exclusiveGateways de BPMN2.0 ***/
	public void getDeclarationIndividualsXorGateways(List<TExclusiveGateway> exclusiveGateways) {
		
		Iterator<?> itr = exclusiveGateways.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TExclusiveGateway element = (TExclusiveGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
			List<String> elementsDirectlyPrecedes = UtilsObjectOWLBpmn.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/***Declaraciones en owl de Gateways generales de BPMN2.0 ***/
	public void getDeclarationIndividualsGateways(List<TGateway> TGatewaysList) {
		
		Iterator<?> itr = TGatewaysList.iterator(); 
		while(itr.hasNext()) {
			
			Object obj = itr.next();
			TGateway element = (TGateway) obj;
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameGtw = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
			List<String> elementsDirectlyPrecedes = UtilsObjectOWLBpmn.getDirectlyPrecedes(sequenceFlows, obj);
		    converter.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de DataObjects de BPMN2.0 ***/
	public void getDeclarationIndividualsDataObject(List<TDataObject> dataObjectList) {
		
		Iterator<?> itr = dataObjectList.iterator(); 
		while(itr.hasNext()) {
			
			TDataObject element = (TDataObject) itr.next();
			
			//Por cada tarea tengo que ir convirtiendo a su declaracion de instanciacion en OWL
			String nameDataObj = UtilsObjectOWLBpmn.getCleanName(element.getName());
			
		    converter.converterDataObjectOWL(nameDataObj);
		}
	}
	
	public void getSaveOWL() {
		
		try {
			
			bpmnMan.saveOntology(bpmnOnt,IRI.create(bpmnFile.toURI()));
		} catch (OWLOntologyStorageException e) {

			e.printStackTrace();
		}
		
	}
	
}
