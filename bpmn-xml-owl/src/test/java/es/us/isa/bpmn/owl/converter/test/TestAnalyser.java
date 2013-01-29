package es.us.isa.bpmn.owl.converter.test;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Clase que comprueba si se cumplen ciertas condiciones en una ontología OWL
 * 
 * @author Edelia
 *
 */
public class TestAnalyser {

	// URI de la ontología
	private String ontologyURI;
	// Ontología a verificar
	private OWLOntology ontology;
	// Objeto factory utilizado para crear objetos OWL
	private OWLDataFactory factory;

	/**
	 * Constructor de la clase
	 *  
	 * @param ontology Ontología a verificar
	 */
	public TestAnalyser(OWLOntology ontology){

        this.ontology = ontology;
        this.ontologyURI = ontology.getOntologyID().getOntologyIRI().toString();

		factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	}

	/**
	 * Verifica si un objeto es de una clase 
	 * 
	 * @param objectId Id del objeto
	 * @param classId Id de la clase
	 * @return
	 */
	protected Boolean checkObjectClass(String objectId, String classId) {
		
		
		Boolean b = false;
        OWLClass taskClass = factory.getOWLClass(IRI.create(classId));
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(ontologyURI + "#" + objectId));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(ontology);

        b = individuals.contains(individual);
        
        return b;
	}
	
	/**
	 * Verifica si un objeto tiene una propiedad dada con un valor dado
	 * 
	 * @param objectIdA Objeto que tiene la propiedad
	 * @param objectIdB Valor de la propiedad
	 * @param propertyId Id de la propiedad
	 * @return
	 */
	protected Boolean checkObjectProperty(String objectIdA, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(ontologyURI + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(ontologyURI+"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ontology);
        b = individuals.contains(individualB);

        return b;
	}

	/**
     * Devuelve el atributo ontologyURI:
     * URI de la ontología
     * 
     * @return Valor del atributo
     */
	protected String getOntologyURI() {
		return ontologyURI;
	}

    /**
     * Da valor al atributo ontologyURI:
     * URI de la ontología
     * 
     * @param value Valor del atributo
     */
	protected void setOntologyURI(String ontologyURI) {
		this.ontologyURI = ontologyURI;
	}

	/**
     * Devuelve el atributo ontology:
     * Ontología a verificar
     * 
     * @return Valor del atributo
     */
	protected OWLOntology getOntology() {
		return ontology;
	}

    /**
     * Da valor al atributo ontology:
     * Ontología a verificar
     * 
     * @param value Valor del atributo
     */
	protected void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	/**
     * Devuelve el atributo factory:
     * Objeto factory utilizado para crear objetos OWL
     * 
     * @return Valor del atributo
     */
	protected OWLDataFactory getFactory() {
		return factory;
	}

    /**
     * Da valor al atributo factory:
     * Objeto factory utilizado para crear objetos OWL
     * 
     * @param value Valor del atributo
     */
	protected void setFactory(OWLDataFactory factory) {
		this.factory = factory;
	}
	
}
