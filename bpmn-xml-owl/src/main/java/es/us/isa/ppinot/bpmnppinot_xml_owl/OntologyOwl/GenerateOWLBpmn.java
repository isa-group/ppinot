package es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl;

import java.util.Iterator;
import java.util.List;
//import org.apache.bcel.generic.NEW;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Ana Belen Sanchez Jerez
 * 
 * Clase donde se van a definir las funciones que convierten los datos pasados por parametros
 * en instancias en owl 
**/
public class GenerateOWLBpmn {

	private OWLOntologyManager bpmnMan;	// OWLOntologyManager utilizado
	private OWLOntology bpmnOnt;		// Ontologia a la que se adicionan los axiomas
	private OWLDataFactory factory;		// Factory utilizada para generar los elementos owl
	private String orgbpmn; 			// URI de la ontologia BPMN
	private String fileOWLBpmn; 		// URI de la ontologia generada
	
	/**Constructor de GenerateOWL **/
	public GenerateOWLBpmn(OWLDataFactory factoryIn, OWLOntologyManager bpmnManIn, OWLOntology bpmnOntIn, String orgbpmnIn,
			String orgbpmnExpr){

		factory = factoryIn;
		bpmnMan = bpmnManIn;
		bpmnOnt = bpmnOntIn;
		orgbpmn = orgbpmnIn;

		fileOWLBpmn = orgbpmnExpr;
	}
	
	/**Funcion para convertir elementos de tipo Activity a individuals en codigo owl
	 * @param nameDataObj 
	 * @param elementsDirectlyPrecedes **/
	public void converterActivityOWL(String nameActivity, String nameDataObj, List<String> elementsDirectlyPrecedes){
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameActivity);
 		IRI iri3 = IRI.create(fileOWLBpmn+"#"+nameDataObj);
		
        OWLNamedIndividual taskNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass activityClass = factory.getOWLClass(IRI.create(orgbpmn + "#Activity"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(activityClass, taskNameIndividual);
        
        if(nameDataObj != null){
        	OWLObjectPropertyExpression output = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#DataOutputAssociation"));
        	OWLNamedIndividual dataObjNameIndividualMeasure = factory.getOWLNamedIndividual(iri3);
        	OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(output, taskNameIndividual, dataObjNameIndividualMeasure);
        	bpmnMan.addAxiom(bpmnOnt, propertyAssertion);
        }
        
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, taskNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);
        
	}

	/**Funcion para convertir elementos de tipo StartEvent a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterStartEventOWL(String nameEvent, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameEvent);
 		
        OWLNamedIndividual EventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass StartEvent = factory.getOWLClass(IRI.create(orgbpmn + "#StartEvent"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(StartEvent, EventNameIndividual);
       
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, EventNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);
	}
	
	/**Funcion para convertir elementos de tipo EndEvent a individuals en codigo owl**/
	public void converterEndEventOWL(String nameEvent) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameEvent);
 		
        OWLNamedIndividual EventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Event = factory.getOWLClass(IRI.create(orgbpmn + "#EndEvent"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Event, EventNameIndividual);
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo XorGateway a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterXorGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameGtw);
 		
        OWLNamedIndividual GtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Gtw = factory.getOWLClass(IRI.create(orgbpmn + "#XorGateway"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Gtw, GtwNameIndividual);
        
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, GtwNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo Gateway a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameGtw);
 		
        OWLNamedIndividual GtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Gtw = factory.getOWLClass(IRI.create(orgbpmn + "#Gateway"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Gtw, GtwNameIndividual);
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, GtwNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo DataObject a individuals en codigo owl**/
	public void converterDataObjectOWL(String nameDataObj) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameDataObj);
 		
        OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass DataObj = factory.getOWLClass(IRI.create(orgbpmn + "#DataObject"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividual);
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	
}
