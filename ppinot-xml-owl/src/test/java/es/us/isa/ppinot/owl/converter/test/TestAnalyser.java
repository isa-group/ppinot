package es.us.isa.ppinot.owl.converter.test;

import java.util.ArrayList;
import java.util.Set;

import es.us.isa.bpmn.owl.notation.Vocabulary;
import org.semanticweb.HermiT.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;


public class TestAnalyser {

	private String bpmnOntologyURI;
    private OWLOntology bpmnOntology;
	private OWLDataFactory factory;

	public TestAnalyser(OWLOntology bpmnOntology){

        this.bpmnOntology = bpmnOntology;
        this.bpmnOntologyURI = bpmnOntology.getOntologyID().getOntologyIRI().toString();

		factory = bpmnOntology.getOWLOntologyManager().getOWLDataFactory();
	}
	
	public Boolean isTask(String id) {
		
		Boolean b = false;
        OWLClass taskClass = factory.getOWLClass(IRI.create(Vocabulary.ACTIVITY_URI));
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI + "#" + id));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(bpmnOntology);

        b = individuals.contains(individual);

        return b;
	}
	
	public Boolean isStartEvent(String id) {
		
		Boolean b = false;

        return b;
	}
	
	public Boolean isEndEvent(String id) {
		
		Boolean b = false;

        return b;
	}
	
	public Boolean isDataObject(String id) {
		
		Boolean b = false;

        return b;
	}
	
	public Boolean isDirectlyPreceding(String idA, String idB) {
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create(Vocabulary.DIRECTLYPRECEDES_URI));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI + "#" + idA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+idB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, bpmnOntology);
        b = individuals.contains(individualB);

        return b;
	}
	
	public Boolean isDataInputOf(String idA, String idB) {
		
		
		Boolean b = false;
		

        return b;
	}

}
