package es.us.isa.ppinot.owl.converter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;

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

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.owl.notation.Vocabulary;

class GeneratePpinotAxioms {
	
	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private OWLOntology ontology;

	private String bpmnOntologyURI; 
	private String bpmnGeneratedOntologyURI; 
	private String ppinotGeneratedOntologyURI;
	
	private Hashtable<String, String>funcAggr; 
	
	GeneratePpinotAxioms(OWLDataFactory factory,	OWLOntologyManager manager, OWLOntology ontology, 
			String bpmnOntologyURI, String bpmnGeneratedOntologyURI, String ppinotGeneratedOntologyURI){
		
		this.factory = factory;
		this.manager = manager;
		this.ontology = ontology;
		
		this.funcAggr = new Hashtable<String, String>();
		funcAggr.put(Vocabulary.SUM, Vocabulary.SUMAM_URI);
		funcAggr.put(Vocabulary.MINIMUM, Vocabulary.MINAM_URI);
		funcAggr.put(Vocabulary.MAXIMUM, Vocabulary.MAXAM_URI);
		funcAggr.put(Vocabulary.AVERAGE, Vocabulary.AVGAM_URI);
		funcAggr.put("", Vocabulary.SUMAM_URI);
		
		this.bpmnOntologyURI = bpmnOntologyURI;
		
		this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
		this.ppinotGeneratedOntologyURI = ppinotGeneratedOntologyURI;
		
	}

//***********************************************************************************/

	OWLNamedIndividual converterCountInstanceMeasureOWL(CountInstanceMeasure element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception 
	{

		String nameCountMeasure= element.getId();

		Boolean endActivity = element.getWhen().getChangesToState().getState()==GenericState.END;

        String elementName = element.getWhen().getAppliesTo();
        String type = this.getNameTypeActivity(elementName, bpmn20ModelHandler);
		
		// adiciona el axioma con la medida
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameCountMeasure) );
        OWLClass measureClass = factory.getOWLClass(IRI.create(Vocabulary.COUNTMEASURE_URI));
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        manager.addAxiom(ontology, classAssertionAxiom);

		// adiciona el axioma que indica el momento en que se aplica la medida
        OWLNamedIndividual whenPropertyIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.TIMEINSTANT+nameCountMeasure) );
        OWLObjectPropertyExpression whenObjectProperty = factory.getOWLObjectProperty( IRI.create(Vocabulary.WHEN_URI) );
        OWLObjectPropertyAssertionAxiom whenObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(whenObjectProperty, measureIndividual, whenPropertyIndividual);
        manager.addAxiom(ontology, whenObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica la clase del momento en que se aplica la medida
        IRI classIri = this.timeInstantClassIRI(type, endActivity);
        OWLClass timeInstantClass = factory.getOWLClass(classIri);
        OWLClassAssertionAxiom timeInstantClassAssertionAxiom = factory.getOWLClassAssertionAxiom(timeInstantClass, whenPropertyIndividual);
        manager.addAxiom(ontology, timeInstantClassAssertionAxiom);	
       
        // adiciona el axioma que indica que la propiedad appliesTo del individual whenPropertyIndividual tiene el valor bpmnElement
        OWLNamedIndividual bpmnElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+elementName) );
        OWLObjectPropertyExpression appliesToObjectProperty = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesToObjectProperty, whenPropertyIndividual, bpmnElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);
        
        // devuelve el individual de la medida
        return measureIndividual;
	}

	OWLIndividual converterTimeInstanceMeasureOWL(TimeInstanceMeasure element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception 
	
	{
		
		String nameTimeMeasure= element.getId();
		
		Boolean conectorEndFrom = element.getFrom().getChangesToState().getState()==GenericState.END;
		Boolean conectorEndTo = element.getTo().getChangesToState().getState()==GenericState.END;
		
		String activityFrom = element.getFrom().getAppliesTo();
		String activityTo = element.getTo().getAppliesTo();
		
		TimeMeasureType timeMeasureType = element.getTimeMeasureType();
		
		String typeActivityFrom = this.getNameTypeActivity(activityFrom, bpmn20ModelHandler);
		String typeActivityTo = this.getNameTypeActivity(activityTo, bpmn20ModelHandler);
		
        // adiciona el axioma que indica que la medida es de la clase CyclicTimeMeasure o LinearTimeMeasure
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameTimeMeasure) );
        IRI classIri;
        if (timeMeasureType==TimeMeasureType.CYCLIC)
        	classIri = IRI.create(Vocabulary.CYCLICTIMEMEASURE_URI);
        else
        	classIri = IRI.create(Vocabulary.LINEARTIMEMEASURE_URI);
        OWLClass measureClass = factory.getOWLClass( classIri );
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        manager.addAxiom(ontology, classAssertionAxiom);
        
        // adiciona el axioma que indica el valor de la propiedad from de la medida
        OWLObjectPropertyExpression fromObjectProperty = factory.getOWLObjectProperty(IRI.create(Vocabulary.FROM_URI));
        OWLNamedIndividual fromIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.START+activityFrom) );
        OWLObjectPropertyAssertionAxiom fromObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromObjectProperty, measureIndividual, fromIndividual);
        manager.addAxiom(ontology, fromObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad desde la cual se aplica la medida
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+activityFrom) );
        OWLObjectPropertyAssertionAxiom fromAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromIndividual, fromDataObjectElement);
        manager.addAxiom(ontology, fromAppliesToObjectPropertyAssertionAxiom);
        
        
        // adiciona el axioma que indica el valor de la propiedad to de la medida
        OWLObjectPropertyExpression toObjectProperty = factory.getOWLObjectProperty(IRI.create(Vocabulary.TO_URI));
        OWLNamedIndividual toIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.END+activityTo) );
        OWLObjectPropertyAssertionAxiom toObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toObjectProperty, measureIndividual, toIndividual);
        manager.addAxiom(ontology, toObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad hasta la cual se aplica la medida
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+activityTo) );
        OWLObjectPropertyAssertionAxiom toAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toIndividual, toDataObjectElement);
        manager.addAxiom(ontology, toAppliesToObjectPropertyAssertionAxiom);
 
        //Procesar el tipo
        IRI fromIri = this.timeInstantClassIRI(typeActivityFrom, conectorEndFrom);
        OWLClass classCountTimeInstance = factory.getOWLClass(fromIri);
        OWLClassAssertionAxiom classAssertionTimeInst = factory.getOWLClassAssertionAxiom(classCountTimeInstance, fromIndividual);
        manager.addAxiom(ontology, classAssertionTimeInst);	
        
        IRI toIri = this.timeInstantClassIRI(typeActivityTo, conectorEndTo);
        OWLClass classTimeInstance = factory.getOWLClass(toIri);
        OWLClassAssertionAxiom classAssertionTimeInstTo = factory.getOWLClassAssertionAxiom(classTimeInstance, toIndividual);
        manager.addAxiom(ontology, classAssertionTimeInstTo);	
        
        // devuelve el individual de la medida
		return measureIndividual;
	}

	OWLNamedIndividual converterStateConditionInstanceMeasureOWL(StateConditionInstanceMeasure element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception 
	{
		
		String nameElementCondMeasure= element.getId();

		String activity = ((StateCondition) element.getCondition()).getAppliesTo();
		String restriction = this.getCleanRestriction( ((StateCondition) element.getCondition()).getState().getStateString() );

        // adiciona el axioma de la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual(IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementCondMeasure));
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(Vocabulary.STATECONDITIONMEASURE_URI)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertionCountMeasure);

        // adiciona el axioma con la restriccion de la medida
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEETS_URI));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.STATECONDITION_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);

        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+activity) );
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);
         
        return DataObjNameIndividualMeasure;
	}

	OWLIndividual converterDataPropertyConditionInstanceMeasureOWL(DataPropertyConditionInstanceMeasure element) 
	{
		
		String nameDataCondMeasure = element.getId();

		String dataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();

		String restriction = this.getCleanRestriction( ((DataPropertyCondition) element.getCondition()).getRestriction() );	
		
		// adiciona el axioma que indica la clase de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDataCondMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(Vocabulary.DATAPROPERTYCONDITIONMEASURE_URI)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertionCountMeasure);
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEETS_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.DATAPROPERTYCONDITION_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);	
        
        return DataObjNameIndividualMeasure;
	}

	OWLNamedIndividual converterDataInstanceMeasureOWL(DataInstanceMeasure element) 
	{
		
		String nameElementMeasure = element.getId();

		String dataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();

		// adiciona el axioma que indica la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(Vocabulary.DATAMEASURE_URI)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertionCountMeasure);
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEASURESDATA_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividualMeasure, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        return DataObjNameIndividualMeasure;
	}
	
//**********************************************************************/
//**********************************************************************/

	void converterAggregatedMeasureOWL(AggregatedMeasure element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception 
	{
		
		// adiciona la medida base
		MeasureDefinition baseMeasure = element.getBaseMeasure();
		
		if (baseMeasure instanceof TimeInstanceMeasure)
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) baseMeasure, bpmn20ModelHandler);
		else
		if (baseMeasure instanceof CountInstanceMeasure)
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) baseMeasure, bpmn20ModelHandler);
		else
		if (baseMeasure instanceof StateConditionInstanceMeasure)
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) baseMeasure, bpmn20ModelHandler);
		else
		if (baseMeasure instanceof DataInstanceMeasure)
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) baseMeasure);
		else
		if (baseMeasure instanceof DataPropertyConditionInstanceMeasure)
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) baseMeasure);
		else
		if (baseMeasure instanceof DerivedSingleInstanceMeasure)
			this.converterDerivedMeasureOWL((DerivedSingleInstanceMeasure) baseMeasure, bpmn20ModelHandler);
			
        // adiciona la medida agregada
		String aggMeasureId = element.getId();
		String aggFuntion = element.getAggregationFunction();
		String baseMeasureId = baseMeasure.getId();
		
	    	// adiciona el axioma que indica la clase de la medida de la medida agregada
	    OWLNamedIndividual aggMeasureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+aggMeasureId) );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(aggFuntion) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, aggMeasureIndividual);
	    manager.addAxiom(ontology, classAssertion);	
			
			// adiciona el axioma que indica la medida base que esta siendo agregada
	    OWLNamedIndividual baseMeasureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+baseMeasureId) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, aggMeasureIndividual, baseMeasureIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	       
        // adiciona el axioma con la propiedad isgroupedby
	    String dataObject = element.getGroupedBy().getDataobject();
	    if (dataObject!=null && !dataObject.contentEquals("")) {
	        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.ISGROUPEDBY_URI));
	        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+dataObject) );
	        OWLObjectPropertyAssertionAxiom propertyAssertionGroupBy = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, aggMeasureIndividual, dataObjectElement);
	        manager.addAxiom(ontology, propertyAssertionGroupBy);	
	    }
	}

//**********************************************************************/
//**********************************************************************/
//**********************************************************************/

	void converterDerivedMeasureOWL(DerivedMeasure element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception {

		String derivedMeasureId = element.getId();
		        
		// adiciona el axioma de la medida derivada
		OWLNamedIndividual derivedMeasureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+derivedMeasureId) );
		OWLClass classDerivedMeasure;
		if (element instanceof DerivedMultiInstanceMeasure)
			classDerivedMeasure = factory.getOWLClass( IRI.create(Vocabulary.DERIVEDMULTIINSTANCEMEASURE_URI));
		else
			classDerivedMeasure = factory.getOWLClass( IRI.create(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE_URI));
		OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, derivedMeasureIndividual);
		manager.addAxiom(ontology, classAssertionDerivedMeasure);

		// adiciona las relaciones entre la medida derivada y las medidas utilizadas en el calculo
		Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
		
		Iterator<Entry<String, MeasureDefinition>> itInst = mapaMedidas.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
	        MeasureDefinition measureA = pairs.getValue();
			
			String measureIdA = measureA.getId(); 

			// adiciona el axioma con la relacion de la medida A y la medida derivada
			OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(Vocabulary.ISCALCULATED_URI));
			OWLNamedIndividual measureIndividualA = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdA) );
			OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, derivedMeasureIndividual, measureIndividualA);
			manager.addAxiom(ontology, propertyAssertionmeetsA);
	    }
	}

//**********************************************************************/
//**********************************************************************/
//**********************************************************************/
//**********************************************************************/
	
	void converterPpiOWL(PPI element, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception {
		
		String ppiId = element.getId();
		
		MeasureDefinition measuredBy = element.getMeasuredBy();
		String measureId = measuredBy.getId();
		
		// adiciona el axioma que indica la clase del PPI
	    OWLNamedIndividual ppiIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+ppiId) );
	    OWLClass ppiClass = factory.getOWLClass( IRI.create(Vocabulary.PPI_URI));
	    OWLClassAssertionAxiom ppiClassAxiom = factory.getOWLClassAssertionAxiom(ppiClass, ppiIndividual);
	    manager.addAxiom(ontology, ppiClassAxiom);
		
		// adiciona el axioma con la relacion entre el PPI y la medida
		OWLObjectPropertyExpression definition = factory.getOWLObjectProperty(IRI.create(Vocabulary.DEFINITION_URI));
		OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureId) );
		OWLObjectPropertyAssertionAxiom definitionAxiom = factory.getOWLObjectPropertyAssertionAxiom(definition, ppiIndividual, measureIndividual);
		manager.addAxiom(ontology, definitionAxiom);
	}

	
	private String getNameTypeActivity(String id, Bpmn20ModelHandlerInterface bpmn20ModelHandler) throws Exception{
		
		String type = null;
		if (bpmn20ModelHandler.getTaskMap().containsKey(id))
			
			type = Vocabulary.ACTIVITY;
		else {
			
			if (bpmn20ModelHandler.getSubProcessMap().containsKey(id))
				
				type = Vocabulary.ACTIVITY;
			else {
				
				if (bpmn20ModelHandler.getDataObjectMap().containsKey(id))
					
					type = Vocabulary.DATASTATECHANGE;
				else {
					
					if (bpmn20ModelHandler.getStartEventMap().containsKey(id) || bpmn20ModelHandler.getEndEventMap().containsKey(id))
						
						type = Vocabulary.EVENTTRIGGER;
				}
			}
		}
		
		return type; 		
	}
	
	private IRI timeInstantClassIRI(String type, Boolean endActivity ) {
		
        IRI classIri;
        if(type.contentEquals(Vocabulary.ACTIVITY)){
        	if(endActivity){
        		classIri = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		classIri = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	classIri = IRI.create(bpmnOntologyURI+"#"+type);
        }
        
        return classIri;
	}

	private String getCleanRestriction(String restriction) {
		
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = Vocabulary.RESTRICTION;
		return restriction;
	}
}
