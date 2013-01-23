package es.us.isa.ppinot.owl.converter.test;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import es.us.isa.ppinot.owl.notation.Vocabulary;


public class PpiNotTestAnalyser {

	private String ppinotOntologyURI;
	private String bpmnOntologyURI;
	private OWLOntology ppinotOntology;
	private OWLDataFactory factory;

	public PpiNotTestAnalyser(OWLOntology ontology, String bpmnOntologyURI){

        this.ppinotOntology = ontology;
        this.ppinotOntologyURI = ontology.getOntologyID().getOntologyIRI().toString();
        this.bpmnOntologyURI = bpmnOntologyURI;

		factory = ontology.getOWLOntologyManager().getOWLDataFactory();
	}

	protected Boolean checkObjectClass(String objectId, String classId) {
		
		
		Boolean b = false;
        OWLClass taskClass = factory.getOWLClass(IRI.create(classId));
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI + "#" + objectId));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(ppinotOntology);

        b = individuals.contains(individual);
        
        return b;
	}
	
	protected Boolean checkObjectProperty(String objectIdA, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI+"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ppinotOntology);
        b = individuals.contains(individualB);

        return b;
	}
	protected Boolean checkObjectProperty(String uriA, String objectIdA, String uriB, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(uriA + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(uriB +"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ppinotOntology);
        b = individuals.contains(individualB);

        return b;
	}

	public Boolean isLinearTimeIntanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.LINEARTIMEMEASURE_URI);
	}

	public Boolean isCyclicTimeIntanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.CYCLICTIMEMEASURE_URI);
	}

	public Boolean isStateConditionInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.STATECONDITIONMEASURE_URI);
	}

	public Boolean isCountInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.COUNTMEASURE_URI);
	}

	public Boolean isDataInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DATAMEASURE_URI);
	}

	public Boolean isDataPropertyConditionInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DATAPROPERTYCONDITIONMEASURE_URI);
	}

	public Boolean isDerivedMultiInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DERIVEDMULTIINSTANCEMEASURE_URI);
	}

	public Boolean isDerivedSingleInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DERIVEDSINGLEINSTANCEMEASURE_URI);
	}

	public Boolean isPpi(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.PPI_URI);
	}

	public Boolean isActivityStart(String id) {
		
		return this.checkObjectClass(id, Vocabulary.ACTIVITYSTART_URI);
	}

	public Boolean isActivityEnd(String id) {
		
		return this.checkObjectClass(id, Vocabulary.ACTIVITYEND_URI);
	}

	public Boolean isFuntionalProperty(String id) {
		
		return this.checkObjectClass(id, Vocabulary.FUNCTIONALPROPERTY_URI);
	}

	public Boolean isStateCondition(String id) {
		
		return this.checkObjectClass(id, Vocabulary.STATECONDITION_URI);
	}

	public Boolean isDataPropertyCondition(String id) {
		
		return this.checkObjectClass(id, Vocabulary.DATAPROPERTYCONDITION_URI);
	}
	
	public Boolean isFrom(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.FROM_URI);
	}
	
	public Boolean isTo(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.TO_URI);
	}
	
	public Boolean isWhen(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.WHEN_URI);
	}
	
	public Boolean isMeets(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.MEETS_URI);
	}
	
	public Boolean isAppliedTo(String measureId, String elementId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, measureId, bpmnOntologyURI, elementId, Vocabulary.APPLIESTO_URI);
	}
	
	public Boolean isMeasuresData(String measureId, String dataobjectId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, measureId, bpmnOntologyURI, dataobjectId, Vocabulary.MEASURESDATA_URI);
	}
	
	public Boolean isAggregates(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.AGGREGATES_URI);
	}
	
	public Boolean isCalculated(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.ISCALCULATED_URI);
	}
	
	public Boolean isDefinition(String ppiId, String measureId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, ppiId, ppinotOntologyURI, measureId, Vocabulary.DEFINITION_URI);
	}
	
	public Boolean isGroupedBy(String measureId, String dataobjectId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, measureId, bpmnOntologyURI, dataobjectId, Vocabulary.ISGROUPEDBY_URI);
	}
}
