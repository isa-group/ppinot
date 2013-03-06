package es.us.isa.bpmn.owl.converter;

import java.util.Iterator;
import java.util.List;
//import org.apache.bcel.generic.NEW;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.owl.notation.Vocabulary;

/**
 * Clase mediante la cual se generan los axiomas que se adicionan a una ontologia OWL
 * 
 * @author Edelia
 *
 */
class GenerateBpmnAxioms {

	private OWLDataFactory factory;		// Factory utilizada para generar los elementos owl
	private OWLOntologyManager manager;	// OWLOntologyManager utilizado
	private OWLOntology ontology;		// Ontologia a la que se adicionan los axiomas
	private String processId;			// id del proceso
	
	private OWLIndividual processIndividual; // Individual del proceso
	
	private String generatedOntologyURI; 	// URI de la ontologia generada
	
	/**
	 * Constructor de la clase
	 * 
	 * @param factory Factory utilizada para generar los elementos owl
	 * @param manager OWLOntologyManager utilizado
	 * @param ontology Ontologia a la que se adicionan los axiomas
	 * @param generatedOntologyURI URI de la ontologia generada
	 */
	GenerateBpmnAxioms(OWLDataFactory factory, OWLOntologyManager manager, OWLOntology ontology, 
			String generatedOntologyURI, String processId){

		this.factory = factory;
		this.manager = manager;
		this.ontology = ontology;
		this.processId = processId;

		this.generatedOntologyURI = generatedOntologyURI;
		
		this.converterProcess();
	}
	
	/**
	 * Adiciona el axioma del proceso
	 */
	private void converterProcess() {
		
		IRI processIRI = IRI.create(generatedOntologyURI+"#"+processId);
		
        processIndividual = factory.getOWLNamedIndividual(processIRI);
        OWLClass processClass = factory.getOWLClass(IRI.create(Vocabulary.PROCESS_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(processClass, processIndividual);
        manager.addAxiom(ontology, classAssertion);
	}
	
	private void converterProcessIncludes(OWLIndividual individual) {
		
    	OWLObjectPropertyExpression includes = factory.getOWLObjectProperty(IRI.create(Vocabulary.INCLUDES_URI));
    	OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(includes, processIndividual, individual);
    	manager.addAxiom(ontology, propertyAssertion);
	}
	
	/**
	 * Genera los axiomas correspondientes a una tarea
	 * 
	 * @param nameActivity Id de la actividad
	 * @param nameInputDataObjList Los dataobjects que son entrada de datos a la tarea
	 * @param nameOutputDataObj Los dataobjects que son salida de datos de la tarea
	 * @param elementsDirectlyPrecedes Elementos a los que precede directamente la actividad
	 */
	void converterActivityOWL(String nameActivity, List<String> nameInputDataObjList, List<String> nameOutputDataObjList, List<String> elementsDirectlyPrecedes){
		
		IRI activityIRI = IRI.create(generatedOntologyURI+"#"+nameActivity);
		
		// adiciona el axioma de la clase de la tarea
        OWLNamedIndividual taskNameIndividual = factory.getOWLNamedIndividual(activityIRI);
        OWLClass activityClass = factory.getOWLClass(IRI.create(Vocabulary.ACTIVITY_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(activityClass, taskNameIndividual);
        manager.addAxiom(ontology, classAssertion);
        
        this.converterProcessIncludes(taskNameIndividual);
        
        // adiciona los axiomas que indican los dataObjects que son entradas de datos de la tarea
        for(String nameInputDataObj : nameInputDataObjList){
     		IRI dataInputObjectIRI = IRI.create(generatedOntologyURI+"#"+nameInputDataObj);
        	OWLObjectPropertyExpression input = factory.getOWLObjectProperty(IRI.create(Vocabulary.DATAINPUT_URI));
        	OWLNamedIndividual dataObjNameIndividualMeasure = factory.getOWLNamedIndividual(dataInputObjectIRI);
        	OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(input, taskNameIndividual, dataObjNameIndividualMeasure);
        	manager.addAxiom(ontology, propertyAssertion);
        }
        
        // adiciona los axiomas que indican los dataObjects que son salidas de datos de la tarea
        for(String nameOutputDataObj : nameOutputDataObjList){
     		IRI dataOutputObjectIRI = IRI.create(generatedOntologyURI+"#"+nameOutputDataObj);
     		OWLObjectPropertyExpression output = factory.getOWLObjectProperty(IRI.create(Vocabulary.DATAOUTPUT_URI));
        	OWLNamedIndividual dataObjNameIndividualMeasure = factory.getOWLNamedIndividual(dataOutputObjectIRI);
        	OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(output, taskNameIndividual, dataObjNameIndividualMeasure);
        	manager.addAxiom(ontology, propertyAssertion);
        }
        
        // adiciona los axiomas que indican los elementos que son directamente precedidos por la tarea
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(Vocabulary.DIRECTLYPRECEDES_URI));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(generatedOntologyURI+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, taskNameIndividual, DataObjNameElementdirectly);
        		manager.addAxiom(ontology, propertyAssertion2);
        	}
        }
        
	}

	/**
	 * Genera los axiomas correspondientes a una startEvent
	 * 
	 * @param nameEvent Id del startEvent
	 * @param elementsDirectlyPrecedes Elementos a los que precede directamente la actividad
	 */
	void converterStartEventOWL(String nameEvent, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(generatedOntologyURI+"#"+nameEvent);
 		
		// adiciona el axioma de la clase del startEvent
        OWLNamedIndividual eventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass startEvent = factory.getOWLClass(IRI.create(Vocabulary.STARTEVENT_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(startEvent, eventNameIndividual);
        manager.addAxiom(ontology, classAssertion);
        
        this.converterProcessIncludes(eventNameIndividual);
       
        // adiciona los axiomas que indican los elementos que son directamente precedidos por el startEvent
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(Vocabulary.DIRECTLYPRECEDES_URI));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(generatedOntologyURI+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, eventNameIndividual, DataObjNameElementdirectly);
        		manager.addAxiom(ontology, propertyAssertion2);
        	}
        }
	}
	
	/**
	 * Genera los axiomas correspondientes a una endEvent
	 * 
	 * @param nameEvent Id del endEvent
	 */
	void converterEndEventOWL(String nameEvent) {
		
		IRI iri2 = IRI.create(generatedOntologyURI+"#"+nameEvent);
 		
		// adiciona el axioma de la clase del endEvent
        OWLNamedIndividual eventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass event = factory.getOWLClass(IRI.create(Vocabulary.ENDEVENT_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(event, eventNameIndividual);
        manager.addAxiom(ontology, classAssertion);	
        
        this.converterProcessIncludes(eventNameIndividual);
	}

	/**
	 * Genera los axiomas correspondientes a una xorGateway
	 * 
	 * @param nameGtw Id del xorGateway
	 * @param elementsDirectlyPrecedes Elementos a los que precede directamente la actividad
	 */
	void converterXorGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(generatedOntologyURI+"#"+nameGtw);
 		
		// adiciona el axioma de la clase del xorGateway
        OWLNamedIndividual gtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass gtw = factory.getOWLClass(IRI.create(Vocabulary.XORGATEWAY_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(gtw, gtwNameIndividual);
        manager.addAxiom(ontology, classAssertion);	
        
        this.converterProcessIncludes(gtwNameIndividual);
        
        // adiciona los axiomas que indican los elementos que son directamente precedidos por el xorGateway
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(Vocabulary.DIRECTLYPRECEDES_URI));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(generatedOntologyURI+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, gtwNameIndividual, DataObjNameElementdirectly);
        		manager.addAxiom(ontology, propertyAssertion2);
        	}
        }
	}

	/**
	 * Genera los axiomas correspondientes a una gateway
	 * 
	 * @param nameGtw Id del gateway
	 * @param elementsDirectlyPrecedes Elementos a los que precede directamente la actividad
	 */
	void converterGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(generatedOntologyURI+"#"+nameGtw);
 		
		// adiciona el axioma de la clase del gateway
        OWLNamedIndividual gtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass gtw = factory.getOWLClass(IRI.create(Vocabulary.GATEWAY_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(gtw, gtwNameIndividual);
        manager.addAxiom(ontology, classAssertion);	
        
        this.converterProcessIncludes(gtwNameIndividual);
        
        // adiciona los axiomas que indican los elementos que son directamente precedidos por el gateway
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(Vocabulary.DIRECTLYPRECEDES_URI));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(generatedOntologyURI+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, gtwNameIndividual, DataObjNameElementdirectly);
        		manager.addAxiom(ontology, propertyAssertion2);
        	}
        }
	}

	/**
	 * Genera los axiomas correspondientes a una dataObject
	 * 
	 * @param nameGtw Id del dataObject
	 */
	void converterDataObjectOWL(String nameDataObj) {
		
		IRI iri2 = IRI.create(generatedOntologyURI+"#"+nameDataObj);
 		
		// adiciona el axioma de la clase del dataObject
        OWLNamedIndividual dataObjNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass dataObj = factory.getOWLClass(IRI.create(Vocabulary.DATAOBJECT_URI));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(dataObj, dataObjNameIndividual);
        manager.addAxiom(ontology, classAssertion);	
        
        this.converterProcessIncludes(dataObjNameIndividual);
	}

	
}
