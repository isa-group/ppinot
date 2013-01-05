package es.us.isa.ppinot.owl.converter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;

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
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.owl.notation.Vocabulary;
/**
 * @author Edelia
 * 
 * Clase donde se van a definir las funciones que convierten los datos pasados por parametros
 * en instancias en owl 
**/
@SuppressWarnings("rawtypes")
public class GeneratePpinotAxioms {
	
	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private OWLOntology ontology;

	private String bpmnOntologyURI; 
	private String bpmnGeneratedOntologyURI; 
	private String ppinotGeneratedOntologyURI;
	
	private Hashtable<String, String>funcAggr; 
	
	/**Constructor de GenerateOWL **/
	public GeneratePpinotAxioms(OWLDataFactory factory,	OWLOntologyManager manager, OWLOntology ontology, 
			String bpmnOntologyURI, String bpmnGeneratedOntologyURI, String ppinotGeneratedOntologyURI){
		
		this.factory = factory;
		this.manager = manager;
		this.ontology = ontology;
		
		this.funcAggr = new Hashtable<String, String>();
		funcAggr.put(Vocabulary.SUM, Vocabulary.SUMAM_URI);
		funcAggr.put(Vocabulary.MINIMUM, Vocabulary.MINAM_URI);
		funcAggr.put(Vocabulary.MAXIMUM, Vocabulary.MAXAM_URI);
		funcAggr.put(Vocabulary.AVERAGE, Vocabulary.AVGAM_URI);
		
		this.bpmnOntologyURI = bpmnOntologyURI;
		
		this.bpmnGeneratedOntologyURI = bpmnGeneratedOntologyURI;
		this.ppinotGeneratedOntologyURI = ppinotGeneratedOntologyURI;
		
	}

	/**Funcion que se encarga de convertir las medidas de tipo countInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public OWLNamedIndividual converterCountInstanceMeasureOWL(CountInstanceMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception 
	{

		// obtiene el nombre de la actividad
		String nameCountMeasure= element.getId();
		// obtiene si la medida se aplica al inicio o al final de la actividad
		Boolean endActivity = element.getWhen().getChangesToState().getState()==GenericState.END;
		// obtiene el nombre y el tipo de la actividad a la que se aplica la medida
        String elementName = element.getWhen().getAppliesTo();
        String type = this.getNameTypeActivity(elementName, bpmn20XmlExtracter);
		
		// adiciona el axioma con la medida
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameCountMeasure) );
        OWLClass measureClass = factory.getOWLClass(IRI.create(Vocabulary.COUNTMEASURE_URI));
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        manager.addAxiom(ontology, classAssertionAxiom);

		// adiciona el axioma que indica el momento en que se aplica la medida
        OWLNamedIndividual whenPropertyIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.TIMEINSTANCE+nameCountMeasure) );
        OWLObjectPropertyExpression whenObjectProperty = factory.getOWLObjectProperty(IRI.create(Vocabulary.WHEN_URI));
        OWLObjectPropertyAssertionAxiom whenObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(whenObjectProperty, measureIndividual, whenPropertyIndividual);
        manager.addAxiom(ontology, whenObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica la clase del momento en que se aplica la medida
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		classIri = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	classIri = IRI.create(bpmnOntologyURI+"#"+type);
        }
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

	/**Funcion que se encarga de convertir las medidas de tipo TimeInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public OWLIndividual converterTimeInstanceMeasureOWL(TimeInstanceMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception 
	
	{
		
		String nameTimeMeasure= element.getId();
		
		Boolean conectorEndFrom = element.getFrom().getChangesToState().getState()==GenericState.END;
		Boolean conectorEndTo = element.getTo().getChangesToState().getState()==GenericState.END;
		
		String activityFrom = element.getFrom().getAppliesTo();
		String activityTo = element.getTo().getAppliesTo();
		
		String timeMeasureType = element.getTimeMeasureType();
		
		String typeActivityFrom = this.getNameTypeActivity(activityFrom, bpmn20XmlExtracter);
		String typeActivityTo = this.getNameTypeActivity(activityTo, bpmn20XmlExtracter);
		
        // adiciona el axioma que indica que la medida es de la clase CyclicTimeMeasure o LinearTimeMeasure
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameTimeMeasure) );
        IRI classIri;
        if (timeMeasureType!=null && timeMeasureType.toLowerCase().contentEquals("cyclic"))
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
        IRI fromIri;
        if(typeActivityFrom == "Activity"){
        	if(conectorEndFrom){
        		fromIri = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		fromIri = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	fromIri = IRI.create(bpmnOntologyURI+"#"+typeActivityFrom);
        }
        OWLClass classCountTimeInstance = factory.getOWLClass(fromIri);
        OWLClassAssertionAxiom classAssertionTimeInst = factory.getOWLClassAssertionAxiom(classCountTimeInstance, fromIndividual);
        manager.addAxiom(ontology, classAssertionTimeInst);	
        
        IRI toIri;
        if(typeActivityTo == "Activity"){
        	if(conectorEndTo){
        		toIri = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		toIri = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	toIri = IRI.create(bpmnOntologyURI+"#"+typeActivityFrom);
        }
        OWLClass classTimeInstance = factory.getOWLClass(toIri);
        OWLClassAssertionAxiom classAssertionTimeInstTo = factory.getOWLClassAssertionAxiom(classTimeInstance, toIndividual);
        manager.addAxiom(ontology, classAssertionTimeInstTo);	
        
        // devuelve el individual de la medida
		return measureIndividual;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception 
	 **/
	public OWLNamedIndividual converterStateConditionInstanceMeasureOWL(StateConditionInstanceMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception 
	{
		
		String nameElementCondMeasure= element.getId();

		String activity = ((StateCondition) element.getCondition()).getAppliesTo();
		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((StateCondition) element.getCondition()).getState().getStateString() );

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
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.FUNCTIONALPROPERTY_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);

        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnOntologyURI+"#"+activity) );
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);
         
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataPropertyConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLIndividual converterDataPropertyConditionInstanceMeasureOWL(DataPropertyConditionInstanceMeasure element) 
	{
		
		String nameDataCondMeasure = element.getId();

		String dataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();

		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((DataPropertyCondition) element.getCondition()).getRestriction() );	
		
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
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.FUNCTIONALPROPERTY_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);	
        
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLNamedIndividual converterDataInstanceMeasureOWL(DataInstanceMeasure element) 
	{
		
		String nameElementMeasure = element.getId();

		String dataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();

		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((DataPropertyCondition) element.getCondition()).getRestriction() );

		// adiciona el axioma que indica la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(Vocabulary.DATAMEASURE_URI)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertionCountMeasure);
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEASURESDATA_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividualMeasure, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        return DataObjNameIndividualMeasure;
	}
	
/**********************************************************************/
/**********************************************************************/

	/**Funcion que se encarga de convertir las medidas de tipo countAggregatedMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public ArrayList<Object> converterCountAggregatedMeasureOWL(AggregatedMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {

		String nameCountAggregatedMeasure= element.getBaseMeasure().getId();

		String functionAgg = element.getAggregationFunction();
		
		CountInstanceMeasure element2 = (CountInstanceMeasure) element.getBaseMeasure();
		Boolean endActivity = element2.getWhen().getChangesToState().getState()==GenericState.END;
		
		String elementId = element2.getWhen().getAppliesTo();
        String type = this.getNameTypeActivity(elementId, bpmn20XmlExtracter);

		/* Las entradas de las individuals para definir las medidas, los nombres de estas entradas, salvo el nombre de la medida, 
		 * deben ser generados aleatoriamente porque no hay forma de obtener esos datos del modelo y ni interesan. 
		 * Sin embargo, el project del final tengo que obtenerlo del conector isGroupBy de su campo condition.*/
	       
        // adiciona el axioma que indica la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameCountAggregatedMeasure+"Intermediate1") );
        OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertion);	
		
		// adiciona el axioma que indica la medida que esta siendo agregada
        OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameCountAggregatedMeasure) );
        OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
        manager.addAxiom(ontology, propertyAssertion);
         
        // adiciona el axioma que indica la clase de la medida que esta siendo agregada
        OWLClass classIntermediateCountMeasure = factory.getOWLClass( IRI.create(Vocabulary.COUNTMEASURE_URI)) ;
        OWLClassAssertionAxiom classIntermediateAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classIntermediateCountMeasure, DataObjNameIndividual);
        manager.addAxiom(ontology, classIntermediateAssertionCountMeasure);
        //--------------------------------------------
        
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression meets = factory.getOWLObjectProperty(IRI.create(Vocabulary.WHEN_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.TIMEINSTANT+nameCountAggregatedMeasure) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meets, DataObjNameIndividual, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+elementId) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		classIri = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	classIri = IRI.create(bpmnOntologyURI+"#"+type);
        }
        OWLClass classCountTimeInstance = factory.getOWLClass(classIri);
        OWLClassAssertionAxiom classAssertionTimeInst = factory.getOWLClassAssertionAxiom(classCountTimeInstance, dataObjectInstant);
        manager.addAxiom(ontology, classAssertionTimeInst);	
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameCountAggregatedMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameCountAggregatedMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
        
	}

	/**Funcion que se encarga de convertir las medidas de tipo TimeAggregatedMeasure en su correspondiente codigo owl **/
	public  ArrayList<Object> converterTimeAggregatedMeasureOWL(AggregatedMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception 
	{
			
		String nameTimeAggregatedMeasure = element.getBaseMeasure().getId();
	
		String functionAgg = element.getAggregationFunction();
		
		TimeInstanceMeasure element2 = (TimeInstanceMeasure) element.getBaseMeasure();
		Boolean conectorEndFrom = element2.getFrom().getChangesToState().getState()==GenericState.END;
		Boolean conectorEndTo = element2.getTo().getChangesToState().getState()==GenericState.END;
		String activityFrom = element2.getFrom().getAppliesTo();
		String activityTo = element2.getTo().getAppliesTo();
		
        String typeFrom = this.getNameTypeActivity(activityFrom, bpmn20XmlExtracter);
        String typeTo = this.getNameTypeActivity(activityTo, bpmn20XmlExtracter);
		       
	    // adiciona el axioma que indica la clase de la medida
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameTimeAggregatedMeasure+"Intermediate1") );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    manager.addAxiom(ontology, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameTimeAggregatedMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(Vocabulary.TIMEMEASURE_URI)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, classIntermediateAssertionMeasure);
	    //--------------------------------------------

	    // FROM
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression from = factory.getOWLObjectProperty(IRI.create(Vocabulary.FROM_URI));
        OWLNamedIndividual fromDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.TIMEINSTANT+nameTimeAggregatedMeasure+"From") );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(from, DataObjNameIndividual, fromDataObjectInstant);
        manager.addAxiom(ontology, fromPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+activityFrom) );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromDataObjectInstant, fromDataObjectElement);
        manager.addAxiom(ontology, fromPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        IRI classIriFrom;
        if(typeFrom == "Activity"){
        	if(conectorEndFrom){
        		classIriFrom = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		classIriFrom = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	classIriFrom = IRI.create(bpmnOntologyURI+"#"+typeFrom);
        }
        OWLClass fromClassCountTimeInstance = factory.getOWLClass(classIriFrom);
        OWLClassAssertionAxiom fromClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(fromClassCountTimeInstance, fromDataObjectInstant);
        manager.addAxiom(ontology, fromClassAssertionTimeInst);	
 
	    // TO
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression to = factory.getOWLObjectProperty(IRI.create(Vocabulary.TO_URI));
        OWLNamedIndividual toDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+Vocabulary.TIMEINSTANT+nameTimeAggregatedMeasure+"To") );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(to, DataObjNameIndividual, toDataObjectInstant);
        manager.addAxiom(ontology, toPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+activityTo) );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toDataObjectInstant, toDataObjectElement);
        manager.addAxiom(ontology, toPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        IRI classIriTo;
        if(typeTo == "Activity"){
        	if(conectorEndTo){
        		classIriTo = IRI.create(Vocabulary.ACTIVITYEND_URI);
        	}else{
        		classIriTo = IRI.create(Vocabulary.ACTIVITYSTART_URI);
        	}
        }else{
        	classIriTo = IRI.create(bpmnOntologyURI+"#"+typeFrom);
        }
        OWLClass toClassCountTimeInstance = factory.getOWLClass(classIriTo);
        OWLClassAssertionAxiom toClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(toClassCountTimeInstance, toDataObjectInstant);
        manager.addAxiom(ontology, toClassAssertionTimeInst);	
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameTimeAggregatedMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameTimeAggregatedMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionAggregatedMeasure en su correspondiente codigo owl **/
	public ArrayList<Object> converterStateConditionAggregatedMeasureOWL(AggregatedMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception 
	{
		
		String functionAgg = element.getAggregationFunction();
		StateConditionInstanceMeasure element2 = (StateConditionInstanceMeasure) element.getBaseMeasure();
		String nameElementCondMeasure = element2.getId();

		String activity = ((StateCondition) element2.getCondition()).getAppliesTo();

		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((StateCondition) element2.getCondition()).getState().getStateString() );
		
		String elementConditionType = this.getNameTypeActivity(activity, bpmn20XmlExtracter);
	       
	    // adiciona el axioma que indica la clase de la medida
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementCondMeasure+"Intermediate1") );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    manager.addAxiom(ontology, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(Vocabulary.STATECONDITIONMEASURE_URI)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEETS_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.FUNCTIONALPROPERTY_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+elementConditionType) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);	

        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementCondMeasure+Vocabulary.INTERMEDIATE1);
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementCondMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
		
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataPropertyConditionAggregatedeMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public ArrayList<Object> converterDataPropertyConditionAggregatedMeasureOWL(AggregatedMeasure element) 
	{
		
		String functionAgg = element.getAggregationFunction();
		DataPropertyConditionInstanceMeasure element2 = (DataPropertyConditionInstanceMeasure) element.getBaseMeasure();
		String nameDataCondMeasure= element2.getId();

		String dataObject = ((DataPropertyCondition) element2.getCondition()).getAppliesTo();

		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((DataPropertyCondition) element2.getCondition()).getRestriction() );
	       
	    // adiciona el axioma que indica la clase de la medida
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDataCondMeasure+"Intermediate1") );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    manager.addAxiom(ontology, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDataCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(Vocabulary.DATAPROPERTYCONDITIONMEASURE_URI)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, classIntermediateAssertionMeasure);
	    //--------------------------------------------

		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEETS_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(Vocabulary.FUNCTIONALPROPERTY_URI)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        manager.addAxiom(ontology, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.APPLIESTO_URI));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(bpmnGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        manager.addAxiom(ontology, propertyAssertionappliesTo);	
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameDataCondMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameDataCondMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
        
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataAggregatedMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public ArrayList<Object> converterDataAggregatedMeasureOWL(AggregatedMeasure element) 
	{
		
		String functionAgg = element.getAggregationFunction();
		DataInstanceMeasure element2 = (DataInstanceMeasure) element.getBaseMeasure();
		String nameElementMeasure= element2.getId();

		String dataObject = ((DataPropertyCondition) element2.getCondition()).getAppliesTo();

		String restriction = GeneratePpinotAxioms.getCleanRestriction( ((DataPropertyCondition) element2.getCondition()).getRestriction() );
	       
	    // adiciona el axioma que indica la clase de la medida
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure+"Intermediate1") );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    manager.addAxiom(ontology, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(Vocabulary.DATAMEASURE_URI)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(Vocabulary.MEASURESDATA_URI));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividual, dataObjectInstant);
        manager.addAxiom(ontology, propertyAssertionmeets);
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementMeasure+Vocabulary.INTERMEDIATE1);
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}
	
	public ArrayList<Object> converterDerivedSingleInstanceAggregatedMeasureOWL(AggregatedMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		String functionAgg = element.getAggregationFunction();
		DerivedSingleInstanceMeasure element2 = (DerivedSingleInstanceMeasure) element.getBaseMeasure();
		String nameElementMeasure= element2.getId();
		
		Map<String, MeasureDefinition> mapaMedidas = element2.getUsedMeasureMap();
		Set keys = mapaMedidas.keySet();
		Object akeys[] = keys.toArray();
		String keymedA = (String) akeys[0];
		String keymedB = (String) akeys[1];
		
		MeasureDefinition medidaA = mapaMedidas.get(keymedA);
		MeasureDefinition medidaB = mapaMedidas.get(keymedB);
		
		String measureIdA = medidaA.getId(); 
		String measureIdB = medidaB.getId();
		
		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA);
		}
		
		//MedidasB
		if(medidaB instanceof TimeInstanceMeasure){
			
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof CountInstanceMeasure){	
			
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof StateConditionInstanceMeasure){
			
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof DataInstanceMeasure){
			
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB);
		}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
			
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB);
		}
		
	       
	    // adiciona el axioma que indica la clase de la medida
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure+"Intermediate1") );
	    OWLClass DataObj = factory.getOWLClass(IRI.create( funcAggr.get(functionAgg) ));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    manager.addAxiom(ontology, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(Vocabulary.AGGREGATES_URI));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE_URI));
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    manager.addAxiom(ontology, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(Vocabulary.ISCALCULATED_URI));
		OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdA) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividual, measureA);
		manager.addAxiom(ontology, propertyAssertionmeetsA);
		
		// adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdB) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividual, measureB);
		manager.addAxiom(ontology, propertyAssertionmeetsB);
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}
	
/**********************************************************************/
/**********************************************************************/
/**********************************************************************/

	/**Funcion que se encarga de convertir las medidas de tipo DerivedMultiInstanceMeasure 
	 * en su correspondiente codigo owl 
	 * @throws Exception **/
	public void converterDerivedMultiInstanceMeasureOWL(DerivedMultiInstanceMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
		Set keys = mapaMedidas.keySet();
		Object akeys[] = keys.toArray();
		String keymedA = (String) akeys[0];
		String keymedB = (String) akeys[1];
		
		MeasureDefinition medidaA = mapaMedidas.get(keymedA);
		MeasureDefinition medidaB = mapaMedidas.get(keymedB);
		
		String measureIdA = medidaA.getId(); 
		String measureIdB = medidaB.getId();

		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA);
		}
		
		//MedidasB
		if(medidaB instanceof TimeInstanceMeasure){
			
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof CountInstanceMeasure){	
			
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof StateConditionInstanceMeasure){
			
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, bpmn20XmlExtracter);
		}else if(medidaB instanceof DataInstanceMeasure){
			
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB);
		}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
			
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB);
		}
		
		String nameDerivedMultiInstance = element.getName()+"Intermediate1";
		measureIdA = measureIdA + "Intermediate1";
		measureIdB = measureIdB + "Intermediate1";
		
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDerivedMultiInstance) );
        OWLClass classDerivedMeasure = factory.getOWLClass(IRI.create(Vocabulary.DERIVEDMULTIINSTANCEMEASURE_URI));
        OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
        manager.addAxiom(ontology, classAssertionDerivedMeasure);
        
        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(Vocabulary.ISCALCULATED_URI));
        OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdA ));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureA);
        manager.addAxiom(ontology, propertyAssertionmeetsA);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdB) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureB);
        manager.addAxiom(ontology, propertyAssertionmeetsB);
	}

	/**Funcion que se encarga de convertir las medidas de tipo DerivedSingleInstanceMeasure 
	 * en su correspondiente codigo owl 
	 * @throws Exception **/
	public void converterDerivedSingleInstanceMeasureOWL(DerivedSingleInstanceMeasure element, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception {
		
		Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
		Set keys = mapaMedidas.keySet();
		Object akeys[] = keys.toArray();
		String keymedA = (String) akeys[0];
		String keymedB = "";
		if (akeys.length>1)
			keymedB = (String) akeys[1];
		
		MeasureDefinition medidaA = mapaMedidas.get(keymedA);
		MeasureDefinition medidaB = null;
		if (akeys.length>1) {
			medidaB = mapaMedidas.get(keymedB);
		}

		String measureIdA = medidaA.getId(); 
		String measureIdB = medidaB.getId();

		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, bpmn20XmlExtracter);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA);
		}
		
		//MedidasB
		if (medidaB!=null) {
			
			if(medidaB instanceof TimeInstanceMeasure){
				
				this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, bpmn20XmlExtracter);
			}else if(medidaB instanceof CountInstanceMeasure){	
				
				this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, bpmn20XmlExtracter);
			}else if(medidaB instanceof StateConditionInstanceMeasure){
				
				this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, bpmn20XmlExtracter);
			}else if(medidaB instanceof DataInstanceMeasure){
				
				this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB);
			}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
				
				this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB);
			}
		}

		String nameDerivedSingleInstance = element.getName();
		        
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+nameDerivedSingleInstance) );
		OWLClass classDerivedMeasure = factory.getOWLClass( IRI.create(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE_URI));
		OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
		manager.addAxiom(ontology, classAssertionDerivedMeasure);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(Vocabulary.ISCALCULATED_URI));
		OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdA) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureA);
		manager.addAxiom(ontology, propertyAssertionmeetsA);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
		if (!measureIdB.isEmpty()) {
			OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(ppinotGeneratedOntologyURI+"#"+measureIdB) );
			OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureB);
			manager.addAxiom(ontology, propertyAssertionmeetsB);
		}
		
	}

	
	/** Devuelve el tipo de la Actividad**/
	private String getNameTypeActivity(String id, Bpmn20XmlExtracter bpmn20XmlExtracter) throws Exception{
		
		String type = null;
		Object obj = bpmn20XmlExtracter.isTask(id);
		if (obj!=null)
			
			type = Vocabulary.ACTIVITY;
		else {
			
			obj = bpmn20XmlExtracter.isSubProcess(id);
			if (obj!=null)
				
				type = Vocabulary.ACTIVITY;
			else {
				
				obj = bpmn20XmlExtracter.isDataObject(id);
				if (obj!=null)
					
					type = Vocabulary.DATASTATECHANGE;
				else {
					
					obj = bpmn20XmlExtracter.isStartEvent(id);
					if (obj!=null)
						
						type = Vocabulary.EVENTTRIGGER;
					else {
						
						obj = bpmn20XmlExtracter.isEndEvent(id);
						if (obj!=null)
							type = Vocabulary.EVENTTRIGGER;
					}
				}
			}
		}
		
		return type; 		
	}

	public static String getCleanRestriction(String restriction) {
		
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = Vocabulary.RESTRICTION;
		return restriction;
	}
}
