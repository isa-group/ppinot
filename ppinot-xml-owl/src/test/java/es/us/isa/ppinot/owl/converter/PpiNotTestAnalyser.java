package es.us.isa.ppinot.owl.converter;

import es.us.isa.ppinot.owl.notation.Vocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.Set;


/**
 * Clase que comprueba si se cumplen ciertas condiciones en una ontolog�a OWL para PPINOT
 * 
 * @author Edelia
 *
 */
public class PpiNotTestAnalyser {

	// URI de la ontologia para PPINOT
	private String ppinotOntologyURI;
	// URI de la ontologia para BPMN 2.0 relacionada con el mismo proceso que la de PPINOT
	private String bpmnOntologyURI;
	// Ontolog�a a verificar
	private OWLOntology ppinotOntology;
	// Objeto factory utilizado para crear objetos OWL
	private OWLDataFactory factory;

	/**
	 * Constructor de la clase
	 *  
	 * @param ontology Ontolog�a a verificar
	 * @param bpmnOntologyURI URI de la ontolog�a para BPMN 2.0 relacionada con el mismo proceso que la de PPINOT
	 */
	public PpiNotTestAnalyser(OWLOntology ontology, String bpmnOntologyURI){

        this.ppinotOntology = ontology;
        this.ppinotOntologyURI = ontology.getOntologyID().getOntologyIRI().toString();
        this.bpmnOntologyURI = bpmnOntologyURI;

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
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI + "#" + objectId));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(ppinotOntology);

        b = individuals.contains(individual);
        
        return b;
	}

	/**
	 * Verifica si un objeto es de una clase 
	 * 
	 * @param objectUri URI del objeto
	 * @param objectId Id del objeto
	 * @param classUri URI de la clase
	 * @param classId Id de la clase
	 * @return
	 */
	protected Boolean checkObjectClass(String objectUri, String objectId, String classUri, String classId) {
		
		
		Boolean b = false;
        OWLClass taskClass = factory.getOWLClass(IRI.create(classUri + "#" + classId));
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(objectUri + "#" + objectId));

        Set<OWLIndividual> individuals = taskClass.getIndividuals(ppinotOntology);

        b = individuals.contains(individual);
        
        return b;
	}
	
	/**
	 * Verifica si un objeto tiene una propiedad dada con un valor dado y ambos est�n en la ontolog�a para PPINOT
	 * 
	 * @param objectIdA Objeto que tiene la propiedad
	 * @param objectIdB Valor de la propiedad
	 * @param propertyId Id de la propiedad
	 * @return
	 */
	protected Boolean checkObjectProperty(String objectIdA, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(ppinotOntologyURI+"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ppinotOntology);
        b = individuals.contains(individualB);

        return b;
	}
	/**
	 * Verifica si un objeto tiene una propiedad dada con un valor dado
	 * 
	 * @param uriA URI del objeto que tiene la propiedad
	 * @param objectIdA Objeto que tiene la propiedad
	 * @param uriB URI del valor de la propiedad
	 * @param objectIdB Valor de la propiedad
	 * @param propertyId Id de la propiedad
	 * @return
	 */
	protected Boolean checkObjectProperty(String uriA, String objectIdA, String uriB, String objectIdB, String propertyId) {
		
        Boolean b = false;
        OWLObjectProperty directlyPrecedesProperty = factory.getOWLObjectProperty(IRI.create( propertyId ));
        OWLNamedIndividual individualA = factory.getOWLNamedIndividual(IRI.create(uriA + "#" + objectIdA));
        OWLNamedIndividual individualB = factory.getOWLNamedIndividual(IRI.create(uriB +"#"+objectIdB));

        Set<OWLIndividual> individuals = individualA.getObjectPropertyValues(directlyPrecedesProperty, ppinotOntology);
        b = individuals.contains(individualB);

        return b;
	}

	/**
	 * Verifica si un objeto es un aggregatedMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isAggregatedMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.AVGAM_URI) ||
				this.checkObjectClass(measureId, Vocabulary.SUMAM_URI) ||
				this.checkObjectClass(measureId, Vocabulary.MINAM_URI) ||
				this.checkObjectClass(measureId, Vocabulary.MAXAM_URI) ||
				this.checkObjectClass(measureId, Vocabulary.AVGAM_URI);
	}

	/**
	 * Verifica si un objeto es un linearTimeIntanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isLinearTimeIntanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.LINEARTIMEMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un cyclicTimeIntanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isCyclicTimeIntanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.CYCLICTIMEMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un stateConditionInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isStateConditionInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.STATECONDITIONMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un countInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isCountInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.COUNTMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un dataInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isDataInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DATAMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un dataPropertyConditionInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isDataPropertyConditionInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DATAPROPERTYCONDITIONMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un derivedMultiInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isDerivedMultiInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DERIVEDMULTIINSTANCEMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un derivedSingleInstanceMeasure
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isDerivedSingleInstanceMeasure(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.DERIVEDSINGLEINSTANCEMEASURE_URI);
	}

	/**
	 * Verifica si un objeto es un ppi
	 * 
	 * @param measureId Id del objeto
	 * @return
	 */
	public Boolean isPpi(String measureId) {
		
		return this.checkObjectClass(measureId, Vocabulary.PPI_URI);
	}

	/**
	 * Verifica si un objeto es un eventTrigger
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isEventTrigger(String id) {
		
		return this.checkObjectClass(this.ppinotOntologyURI, id, es.us.isa.bpmn.owl.notation.Vocabulary.ABSTRACTBP_URI, Vocabulary.EVENTTRIGGER);
	}

	/**
	 * Verifica si un objeto es un activityStart
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isActivityStart(String id) {
		
		return this.checkObjectClass(id, Vocabulary.ACTIVITYSTART_URI);
	}

	/**
	 * Verifica si un objeto es un activityEnd
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isActivityEnd(String id) {
		
		return this.checkObjectClass(id, Vocabulary.ACTIVITYEND_URI);
	}

	/**
	 * Verifica si un objeto es un funtionalProperty
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isFuntionalProperty(String id) {
		
		return this.checkObjectClass(id, Vocabulary.FUNCTIONALPROPERTY_URI);
	}

	/**
	 * Verifica si un objeto es un stateCondition
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isStateCondition(String id) {
		
		return this.checkObjectClass(id, Vocabulary.STATECONDITION_URI);
	}

	/**
	 * Verifica si un objeto es un dataPropertyCondition
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isDataPropertyCondition(String id) {
		
		return this.checkObjectClass(id, Vocabulary.DATAPROPERTYCONDITION_URI);
	}

	/**
	 * Verifica si un objeto es un DataContentSelection
	 * 
	 * @param id Id del objeto
	 * @return
	 */
	public Boolean isDataContentSelection(String id) {
		
		return this.checkObjectClass(id, Vocabulary.DATACONTENTSELECTION_URI);
	}
	
	/**
	 * Verifica si una medida tiene una relacion from con un elemento
	 * 
	 * @param measureId Id de la medida
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isFrom(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.FROM_URI);
	}
	
	/**
	 * Verifica si una medida tiene una relacion to con un elemento
	 * 
	 * @param measureId Id de la medida
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isTo(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.TO_URI);
	}
	
	/**
	 * Verifica si una medida tiene una relacion when con un elemento
	 * 
	 * @param measureId Id de la medida
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isWhen(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.WHEN_URI);
	}
	
	/**
	 * Verifica si una medida tiene una relacion meets con un elemento
	 * 
	 * @param measureId Id de la medida
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isMeets(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.MEETS_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion appliesto con un elemento
	 * 
	 * @param measureId Id del objeto
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isAppliedTo(String measureId, String elementId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, measureId, bpmnOntologyURI, elementId, Vocabulary.APPLIESTO_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion measuresdata con un dataobject
	 * 
	 * @param measureId Id del objeto
	 * @param dataobjectId Id del elemento
	 * @return
	 */
	public Boolean isMeasuresData(String measureId, String dataobjectId) {
		
		return this.checkObjectProperty(measureId, dataobjectId, Vocabulary.MEASURESDATA_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion aggregates con un elemento
	 * 
	 * @param measureId Id del objeto
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isAggregates(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.AGGREGATES_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion iscalculated con un elemento
	 * 
	 * @param measureId Id del objeto
	 * @param elementId Id del elemento
	 * @return
	 */
	public Boolean isCalculated(String measureId, String elementId) {
		
		return this.checkObjectProperty(measureId, elementId, Vocabulary.ISCALCULATED_URI);
	}
	
	/**
	 * Verifica si un ppi tiene una relacion definition con una medida
	 * 
	 * @param ppiId Id del ppi
	 * @param measureId Id de la medida
	 * @return
	 */
	public Boolean isDefinition(String ppiId, String measureId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, ppiId, ppinotOntologyURI, measureId, Vocabulary.DEFINITION_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion isgroupedby con un dataobject
	 * 
	 * @param measureId Id del objeto
	 * @param dataobjectId Id del elemento
	 * @return
	 */
	public Boolean isGroupedBy(String measureId, String dataobjectId) {
		
		return this.checkObjectProperty(measureId, dataobjectId, Vocabulary.ISGROUPEDBY_URI);
	}
	
	/**
	 * Verifica si un objeto tiene una relacion data con un dataobject
	 * 
	 * @param measureId Id del objeto
	 * @param dataobjectId Id del elemento
	 * @return
	 */
	public Boolean isData(String measureId, String dataobjectId) {
		
		return this.checkObjectProperty(ppinotOntologyURI, measureId, bpmnOntologyURI, dataobjectId, Vocabulary.DATA_URI);
	}
}
