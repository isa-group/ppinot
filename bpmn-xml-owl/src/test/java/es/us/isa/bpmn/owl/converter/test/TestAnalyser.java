package es.us.isa.bpmn.owl.converter.test;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

public class TestAnalyser {

	private String ontologyURI;
	private OWLOntology ontology;
	private OWLDataFactory factory;

	public TestAnalyser(OWLOntology ontology){

        this.ontology = ontology;
        this.ontologyURI = ontology.getOntologyID().getOntologyIRI().toString();

		factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	}

	protected Boolean checkObjectClass(String objectId, String classId) {
		
		
		Boolean b = false;
        OWLClass taskClass = factory.getOWLClass(IRI.create(classId));
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(ontologyURI + "#" + objectId));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(ontology);

        b = individuals.contains(individual);
        
        return b;
	}
	
	protected Boolean checkObjectProperty(String objectIdA, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(ontologyURI + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(ontologyURI+"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ontology);
        b = individuals.contains(individualB);

        return b;
	}

	protected String getOntologyURI() {
		return ontologyURI;
	}

	protected void setOntologyURI(String ontologyURI) {
		this.ontologyURI = ontologyURI;
	}

	protected OWLOntology getOntology() {
		return ontology;
	}

	protected void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	protected OWLDataFactory getFactory() {
		return factory;
	}

	protected void setFactory(OWLDataFactory factory) {
		this.factory = factory;
	}
	
}
