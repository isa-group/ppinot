package es.us.isa.ppinot.owl.converter.test;

import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.HermiT.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;


public class TestAnalyser {

	private String bpmnOntologyURI;
	private String ppinotOntologyURI;
	
	private OWLReasoner reasoner;
	private OWLDataFactory factory;
	private OWLObjectRenderer renderer;
	
	public TestAnalyser(String bpmnOntologyURI, String ppinotOntologyURI, OWLOntology ontology){

        this.bpmnOntologyURI = bpmnOntologyURI;
		this.ppinotOntologyURI = ppinotOntologyURI;
		
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
		reasoner = reasonerFactory.createReasoner(ontology, config);
		reasoner.precomputeInferences();
		
		factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

		renderer = new DLSyntaxObjectRenderer();
	}
	
	public Boolean isTask(String id) {
		
		Boolean b = false;
		
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+id));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individual, false);

        return b;
	}
	
	public Boolean isStartEvent(String id) {
		
		Boolean b = false;
		
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+id));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individual, false);

        return b;
	}
	
	public Boolean isEndEvent(String id) {
		
		Boolean b = false;
		
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+id));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individual, false);

        return b;
	}
	
	public Boolean isDataObject(String id) {
		
		Boolean b = false;
		
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+id));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individual, false);

        return b;
	}
	
	public Boolean idDirectlyPreceding(String idA, String idB) {
		
		
		Boolean b = false;
		
		OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+idA));
		OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+idB));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individualA, false);

        return b;
	}
	
	public Boolean idDataInputOf(String idA, String idB) {
		
		
		Boolean b = false;
		
		OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+idA));
		OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(bpmnOntologyURI+"#"+idB));

        // Ask the reasoner for the instances of taskName
        NodeSet<OWLClass> classesNodeSet = reasoner.getTypes(individualA, false);

        return b;
	}

}
