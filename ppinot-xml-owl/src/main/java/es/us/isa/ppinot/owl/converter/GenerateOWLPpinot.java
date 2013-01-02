package es.us.isa.ppinot.owl.converter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;
//import org.apache.bcel.generic.NEW;
import es.us.isa.bpmn.owl.converter.UtilsObjectOWLBpmn;
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

import es.us.isa.isabpm.ppinot.model.MeasureDefinition;
import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.state.GenericState;
import es.us.isa.ppinot.owl.notation.Vocabulary;
import es.us.isa.isabpm.ppinot.model.condition.StateCondition;
import es.us.isa.isabpm.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;
/**
 * @author Ana Belen Sanchez Jerez
 * 
 * Clase donde se van a definir las funciones que convierten los datos pasados por parametros
 * en instancias en owl 
**/
@SuppressWarnings("rawtypes")
public class GenerateOWLPpinot {
	
	static OWLOntologyManager ppinotMan;
	static OWLOntology ppinotOnt;
	static String orgppinot; 

	static String orgbpmn; 
	
	static OWLDataFactory factory;

	static String fileOWLBpmn; 
	static String fileOWLPpinot;
	
	static Hashtable<String, String>funcAggr; 
	
	/**Constructor de GenerateOWL **/
	public GenerateOWLPpinot(OWLDataFactory factoryIn, 	OWLOntologyManager ppinotManIn, OWLOntology ppinotOntIn, String orgppinotIn,
			String orgbpmnIn,
			String orgbpmnExpr, String orgppinotExpr){
		
		ppinotMan = ppinotManIn;
		ppinotOnt = ppinotOntIn;
		orgppinot = orgppinotIn;

		factory = factoryIn;
		
		orgbpmn = orgbpmnIn;

		fileOWLBpmn = orgbpmnExpr;
		fileOWLPpinot = orgppinotExpr;
		funcAggr = new Hashtable<String, String>();
	}
	
	
	
	/**Inicializacion de datos de Medidas**/
	public void initializeFuncsMeasure(){
		funcAggr.put("Sum", "SumAM");
		funcAggr.put("Minimum", "MinAM");
		funcAggr.put("Maximum", "MaxAM");
		funcAggr.put("Average", "AvgAM");
	}

	/**Funcion que se encarga de convertir las medidas de tipo countInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public OWLNamedIndividual converterCountInstanceMeasureOWL(CountInstanceMeasure element, JAXBElement jaxbElement) throws Exception 
	{

		// obtiene el nombre de la actividad
		String nameCountMeasure= UtilsObjectOWLBpmn.getCleanName(element.getName());
		// obtiene si la medida se aplica al inicio o al final de la actividad
		Boolean endActivity = element.getWhen().getChangesToState().getState()==GenericState.END;
		// obtiene el nombre y el tipo de la actividad a la que se aplica la medida
		String[] timeInstant = UtilsObjectOWLBpmn.getNameTypeActivity(element.getWhen().getAppliesTo(), jaxbElement);
		
		// adiciona el axioma con la medida
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameCountMeasure) );
        OWLClass measureClass = factory.getOWLClass(IRI.create(orgppinot+"#"+Vocabulary.COUNTMEASURE));
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionAxiom);

		// adiciona el axioma que indica el momento en que se aplica la medida
        OWLNamedIndividual whenPropertyIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.TIMEINSTANCE+nameCountMeasure) );
        OWLObjectPropertyExpression whenObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.WHEN));
        OWLObjectPropertyAssertionAxiom whenObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(whenObjectProperty, measureIndividual, whenPropertyIndividual);
        ppinotMan.addAxiom(ppinotOnt, whenObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica la clase del momento en que se aplica la medida
        String type = timeInstant[1];
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		classIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	classIri = IRI.create(orgbpmn+"#"+type);
        }
        OWLClass timeInstantClass = factory.getOWLClass(classIri);
        OWLClassAssertionAxiom timeInstantClassAssertionAxiom = factory.getOWLClassAssertionAxiom(timeInstantClass, whenPropertyIndividual);
        ppinotMan.addAxiom(ppinotOnt, timeInstantClassAssertionAxiom);	
       
        // adiciona el axioma que indica que la propiedad appliesTo del individual whenPropertyIndividual tiene el valor bpmnElement
        String elementName = timeInstant[0];
        OWLNamedIndividual bpmnElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+elementName) );
        OWLObjectPropertyExpression appliesToObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesToObjectProperty, whenPropertyIndividual, bpmnElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
        
        // devuelve el individual de la medida
        return measureIndividual;
	}

	/**Funcion que se encarga de convertir las medidas de tipo TimeInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public OWLIndividual converterTimeInstanceMeasureOWL(TimeInstanceMeasure element, JAXBElement jaxbElement) throws Exception 
	
	{
		
		String nameTimeMeasure= UtilsObjectOWLBpmn.getCleanName(element.getName());
		
		Boolean conectorEndFrom = element.getFrom().getChangesToState().getState()==GenericState.END;
		Boolean conectorEndTo = element.getTo().getChangesToState().getState()==GenericState.END;
		String ActivityFrom = element.getFrom().getAppliesTo();
		String ActivityTo = element.getTo().getAppliesTo();
		
		String timeMeasureType = element.getTimeMeasureType();
		
		String[] timeInstantActivityFrom = UtilsObjectOWLBpmn.getNameTypeActivity(ActivityFrom,jaxbElement);
		String[] timeInstantActivityTo = UtilsObjectOWLBpmn.getNameTypeActivity(ActivityTo,jaxbElement);
		
        // adiciona el axioma que indica que la medida es de la clase CyclicTimeMeasure o LinearTimeMeasure
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeMeasure) );
        IRI classIri;
        if (timeMeasureType!=null && timeMeasureType.toLowerCase().contentEquals("cyclic"))
        	classIri = IRI.create(orgppinot+"#"+Vocabulary.CYCLICTIMEMEASURE);
        else
        	classIri = IRI.create(orgppinot+"#"+Vocabulary.LINEARTIMEMEASURE);
        OWLClass measureClass = factory.getOWLClass( classIri );
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionAxiom);
        
        // adiciona el axioma que indica el valor de la propiedad from de la medida
        String nameActivityFrom = timeInstantActivityFrom[0];
        OWLObjectPropertyExpression fromObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.FROM));
        OWLNamedIndividual fromIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.START+nameActivityFrom) );
        OWLObjectPropertyAssertionAxiom fromObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromObjectProperty, measureIndividual, fromIndividual);
        ppinotMan.addAxiom(ppinotOnt, fromObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad desde la cual se aplica la medida
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameActivityFrom) );
        OWLObjectPropertyAssertionAxiom fromAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromIndividual, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromAppliesToObjectPropertyAssertionAxiom);
        
        
        // adiciona el axioma que indica el valor de la propiedad to de la medida
        String nameActivityTo = timeInstantActivityTo[0];
        OWLObjectPropertyExpression toObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.TO));
        OWLNamedIndividual toIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.END+nameActivityTo) );
        OWLObjectPropertyAssertionAxiom toObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toObjectProperty, measureIndividual, toIndividual);
        ppinotMan.addAxiom(ppinotOnt, toObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad hasta la cual se aplica la medida
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameActivityTo) );
        OWLObjectPropertyAssertionAxiom toAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toIndividual, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toAppliesToObjectPropertyAssertionAxiom);
 
        //Procesar el tipo
        String typeActivityFrom = timeInstantActivityFrom[1];
        IRI fromIri;
        if(typeActivityFrom == "Activity"){
        	if(conectorEndFrom){
        		fromIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		fromIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	fromIri = IRI.create(orgbpmn+"#"+typeActivityFrom);
        }
        OWLClass classCountTimeInstance = factory.getOWLClass(fromIri);
        OWLClassAssertionAxiom classAssertionTimeInst = factory.getOWLClassAssertionAxiom(classCountTimeInstance, fromIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionTimeInst);	
        
        String typeActivityTo = timeInstantActivityTo[1];
        IRI toIri;
        if(typeActivityTo == "Activity"){
        	if(conectorEndTo){
        		toIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		toIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	toIri = IRI.create(orgbpmn+"#"+typeActivityFrom);
        }
        OWLClass classTimeInstance = factory.getOWLClass(toIri);
        OWLClassAssertionAxiom classAssertionTimeInstTo = factory.getOWLClassAssertionAxiom(classTimeInstance, toIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionTimeInstTo);	
        
        // devuelve el individual de la medida
		return measureIndividual;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception 
	 **/
	public OWLNamedIndividual converterStateConditionInstanceMeasureOWL(StateConditionInstanceMeasure element, JAXBElement jaxbElement) throws Exception 
	{
		
		String nameElementCondMeasure= UtilsObjectOWLBpmn.getCleanName(element.getName());

		String Activity = ((StateCondition) element.getCondition()).getAppliesTo();
		String restriction = UtilsObjectOWLBpmn.getCleanName( ((StateCondition) element.getCondition()).getState().getStateString() );
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";

		String[] elementConditionActivity = UtilsObjectOWLBpmn.getNameTypeActivity(Activity,jaxbElement);

        // adiciona el axioma de la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual(IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure));
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.STATECONDITIONMEASURE)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);

        // adiciona el axioma con la restriccion de la medida
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEETS));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.FUNCTIONALPROPERTY)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);

        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElement = elementConditionActivity[0];
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(orgbpmn+"#"+nameElement) );
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
         
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataPropertyConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLIndividual converterDataPropertyConditionInstanceMeasureOWL(DataPropertyConditionInstanceMeasure element, JAXBElement jaxbElement) 
	{
		
		String nameDataCondMeasure= UtilsObjectOWLBpmn.getCleanName(element.getName());

		String dataObject = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element.getCondition()).getAppliesTo() );

		String restriction = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element.getCondition()).getRestriction() );	
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";
		
		// adiciona el axioma que indica la clase de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DATAPROPERTYCONDITIONMEASURE)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEETS));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.FUNCTIONALPROPERTY)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	
        
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLNamedIndividual converterDataInstanceMeasureOWL(DataInstanceMeasure element, JAXBElement jaxbElement) 
	{
		
		String nameElementMeasure= UtilsObjectOWLBpmn.getCleanName(element.getName());

		String dataObject = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element.getCondition()).getAppliesTo() );

		String restriction = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element.getCondition()).getRestriction() );
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";

		// adiciona el axioma que indica la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DATAMEASURE)) ;
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEASURESDATA));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        return DataObjNameIndividualMeasure;
	}
	
/**********************************************************************/
/**********************************************************************/

	/**Funcion que se encarga de convertir las medidas de tipo countAggregatedMeasure en su correspondiente codigo owl 
	 * @return 
	 * @throws Exception **/
	public ArrayList<Object> converterCountAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) throws Exception {

		String nameCountAggregatedMeasure= UtilsObjectOWLBpmn.getCleanName(element.getBaseMeasure().getName());

		String functionAgg = element.getAggregationFunction();
		
		CountInstanceMeasure element2 = (CountInstanceMeasure) element.getBaseMeasure();
		Boolean endActivity = element2.getWhen().getChangesToState().getState()==GenericState.END;
		String[] timeInstant = UtilsObjectOWLBpmn.getNameTypeActivity(element2.getWhen().getAppliesTo(),jaxbElement);

		/* Las entradas de las individuals para definir las medidas, los nombres de estas entradas, salvo el nombre de la medida, 
		 * deben ser generados aleatoriamente porque no hay forma de obtener esos datos del modelo y ni interesan. 
		 * Sin embargo, el project del final tengo que obtenerlo del conector isGroupBy de su campo condition.*/
	       
        // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameCountAggregatedMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
        OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertion);	
		
		// adiciona el axioma que indica la medida que esta siendo agregada
        OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameCountAggregatedMeasure) );
        OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
         
        // adiciona el axioma que indica la clase de la medida que esta siendo agregada
        OWLClass classIntermediateCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.COUNTMEASURE)) ;
        OWLClassAssertionAxiom classIntermediateAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classIntermediateCountMeasure, DataObjNameIndividual);
        ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionCountMeasure);
        //--------------------------------------------
        
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression meets = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.WHEN));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.TIMEINSTANT+nameCountAggregatedMeasure) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meets, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElement = timeInstant[0];
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElement) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String type = timeInstant[1];
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		classIri = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	classIri = IRI.create(orgbpmn+"#"+type);
        }
        OWLClass classCountTimeInstance = factory.getOWLClass(classIri);
        OWLClassAssertionAxiom classAssertionTimeInst = factory.getOWLClassAssertionAxiom(classCountTimeInstance, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, classAssertionTimeInst);	
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameCountAggregatedMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameCountAggregatedMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
        
	}

	/**Funcion que se encarga de convertir las medidas de tipo TimeAggregatedMeasure en su correspondiente codigo owl **/
	public  ArrayList<Object> converterTimeAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) throws Exception 
	{
			
		String nameTimeAggregatedMeasure= UtilsObjectOWLBpmn.getCleanName(element.getBaseMeasure().getName());
	
		String functionAgg = element.getAggregationFunction();
		
		TimeInstanceMeasure element2 = (TimeInstanceMeasure) element.getBaseMeasure();
		Boolean conectorEndFrom = element2.getFrom().getChangesToState().getState()==GenericState.END;
		Boolean conectorEndTo = element2.getTo().getChangesToState().getState()==GenericState.END;
		String activityFrom = element2.getFrom().getAppliesTo();
		String activityTo = element2.getTo().getAppliesTo();
		
		String[] timeInstantActivityFrom = UtilsObjectOWLBpmn.getNameTypeActivity(activityFrom,jaxbElement);
		String[] timeInstantActivityTo = UtilsObjectOWLBpmn.getNameTypeActivity(activityTo,jaxbElement);
		       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeAggregatedMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeAggregatedMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.TIMEMEASURE)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------

	    // FROM
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression from = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.FROM));
        OWLNamedIndividual fromDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.TIMEINSTANT+nameTimeAggregatedMeasure+"From") );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(from, DataObjNameIndividual, fromDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, fromPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElementFrom = timeInstantActivityFrom[0];
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElementFrom) );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromDataObjectInstant, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String typeFrom = timeInstantActivityFrom[1];
        IRI classIriFrom;
        if(typeFrom == "Activity"){
        	if(conectorEndFrom){
        		classIriFrom = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		classIriFrom = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	classIriFrom = IRI.create(orgbpmn+"#"+typeFrom);
        }
        OWLClass fromClassCountTimeInstance = factory.getOWLClass(classIriFrom);
        OWLClassAssertionAxiom fromClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(fromClassCountTimeInstance, fromDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, fromClassAssertionTimeInst);	
 
	    // TO
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression to = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.TO));
        OWLNamedIndividual toDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Vocabulary.TIMEINSTANT+nameTimeAggregatedMeasure+"To") );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(to, DataObjNameIndividual, toDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, toPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElementTo = timeInstantActivityTo[0];
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElementTo) );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toDataObjectInstant, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String typeTo = timeInstantActivityTo[1];
        IRI classIriTo;
        if(typeTo == "Activity"){
        	if(conectorEndTo){
        		classIriTo = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYEND);
        	}else{
        		classIriTo = IRI.create(orgppinot+"#"+Vocabulary.ACTIVITYSTART);
        	}
        }else{
        	classIriTo = IRI.create(orgbpmn+"#"+typeFrom);
        }
        OWLClass toClassCountTimeInstance = factory.getOWLClass(classIriTo);
        OWLClassAssertionAxiom toClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(toClassCountTimeInstance, toDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, toClassAssertionTimeInst);	
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameTimeAggregatedMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameTimeAggregatedMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionAggregatedMeasure en su correspondiente codigo owl **/
	public ArrayList<Object> converterStateConditionAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) throws Exception 
	{
		
		String functionAgg = element.getAggregationFunction();
		StateConditionInstanceMeasure element2 = (StateConditionInstanceMeasure) element.getBaseMeasure();
		String nameElementCondMeasure= UtilsObjectOWLBpmn.getCleanName(element2.getName());

		String Activity = ((StateCondition) element2.getCondition()).getAppliesTo();

		String restriction = UtilsObjectOWLBpmn.getCleanName( ((StateCondition) element2.getCondition()).getState().getStateString() );
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";
		
		String[] elementConditionActivity = UtilsObjectOWLBpmn.getNameTypeActivity(Activity,jaxbElement);
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.STATECONDITIONMEASURE)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEETS));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.FUNCTIONALPROPERTY)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+elementConditionActivity[0]) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	

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
	public ArrayList<Object> converterDataPropertyConditionAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) 
	{
		
		String functionAgg = element.getAggregationFunction();
		DataPropertyConditionInstanceMeasure element2 = (DataPropertyConditionInstanceMeasure) element.getBaseMeasure();
		String nameDataCondMeasure= UtilsObjectOWLBpmn.getCleanName( element2.getName() );

		String dataObject = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element2.getCondition()).getAppliesTo() );

		String restriction = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element2.getCondition()).getRestriction() );
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DATAPROPERTYCONDITIONMEASURE)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------

		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEETS));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.FUNCTIONALPROPERTY)) ;
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.APPLIESTO));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	
        
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
	public ArrayList<Object> converterDataAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) 
	{
		
		String functionAgg = element.getAggregationFunction();
		DataInstanceMeasure element2 = (DataInstanceMeasure) element.getBaseMeasure();
		String nameElementMeasure= UtilsObjectOWLBpmn.getCleanName(element2.getName());

		String dataObject = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element2.getCondition()).getAppliesTo() );

		String restriction = UtilsObjectOWLBpmn.getCleanName( ((DataPropertyCondition) element2.getCondition()).getRestriction() );
		restriction = restriction.replace("=", "");
		if (restriction.isEmpty())
			restriction = "Restriction";
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DATAMEASURE)) ;
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.MEASURESDATA));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}
	
	public ArrayList<Object> converterDerivedSingleInstanceAggregatedMeasureOWL(AggregatedMeasure element, JAXBElement jaxbElement) throws Exception {
		
		String functionAgg = element.getAggregationFunction();
		DerivedSingleInstanceMeasure element2 = (DerivedSingleInstanceMeasure) element.getBaseMeasure();
		String nameElementMeasure= UtilsObjectOWLBpmn.getCleanName(element2.getName());
		
		Map<String, MeasureDefinition> mapaMedidas = element2.getUsedMeasureMap();
		Set keys = mapaMedidas.keySet();
		Object akeys[] = keys.toArray();
		String keymedA = (String) akeys[0];
		String keymedB = (String) akeys[1];
		
		MeasureDefinition medidaA = mapaMedidas.get(keymedA);
		MeasureDefinition medidaB = mapaMedidas.get(keymedB);
		
		String nameMeasureA = ""; 
		String nameMeasureB = "";
		
		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaA);
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaA);
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaA);
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaA);
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaA);
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA, jaxbElement);
		}
		
		//MedidasB
		if(medidaB instanceof TimeInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaB);
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof CountInstanceMeasure){	
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaB);
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof StateConditionInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaB);
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof DataInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaB);
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaB);
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB, jaxbElement);
		}
		
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.AGGREGATES));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DERIVEDSINGLEINSTANCEMEASURE));
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.ISCALCULATED));
		OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureA) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividual, measureA);
		ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsA);
		
		// adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureB) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividual, measureB);
		ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsB);
        
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
	public void converterDerivedMultiInstanceMeasureOWL(DerivedMultiInstanceMeasure element, JAXBElement jaxbElement) throws Exception {
		
		Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
		Set keys = mapaMedidas.keySet();
		Object akeys[] = keys.toArray();
		String keymedA = (String) akeys[0];
		String keymedB = (String) akeys[1];
		
		MeasureDefinition medidaA = mapaMedidas.get(keymedA);
		MeasureDefinition medidaB = mapaMedidas.get(keymedB);
		
		String nameMeasureA = ""; 
		String nameMeasureB = "";

		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaA);
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaA);
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaA);
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaA);
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaA);
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA, jaxbElement);
		}
		
		//MedidasB
		if(medidaB instanceof TimeInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaB);
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof CountInstanceMeasure){	
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaB);
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof StateConditionInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaB);
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof DataInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaB);
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB, jaxbElement);
		}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
			
			nameMeasureB = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaB);
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB, jaxbElement);
		}
		
		String nameDerivedMultiInstance = element.getName()+"Intermediate1";
		nameMeasureA = nameMeasureA + "Intermediate1";
		nameMeasureB = nameMeasureB + "Intermediate1";
		
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDerivedMultiInstance) );
        OWLClass classDerivedMeasure = factory.getOWLClass(IRI.create(orgppinot+"#"+Vocabulary.DERIVEDMULTIINSTANCEMEASURE));
        OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionDerivedMeasure);
        
        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.ISCALCULATED));
        OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureA ));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureA);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsA);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureB) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureB);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsB);
	}

	/**Funcion que se encarga de convertir las medidas de tipo DerivedSingleInstanceMeasure 
	 * en su correspondiente codigo owl 
	 * @throws Exception **/
	public void converterDerivedSingleInstanceMeasureOWL(DerivedSingleInstanceMeasure element, JAXBElement jaxbElement) throws Exception {

		String nameMeasureA = ""; 
		String nameMeasureB = "";
		
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
		//MedidasA
		if(medidaA instanceof TimeInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaA);
			this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof CountInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaA);
			this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof StateConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaA);
			this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaA);
			this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaA, jaxbElement);
		}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
			
			nameMeasureA = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaA);
			this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaA, jaxbElement);
		}
		
		//MedidasB
		if (medidaB!=null) {
			
			if(medidaB instanceof TimeInstanceMeasure){
				
				nameMeasureB = UtilsObjectOWLPpinot.getNameTimeInstanceMeasure(medidaB);
				this.converterTimeInstanceMeasureOWL((TimeInstanceMeasure) medidaB, jaxbElement);
			}else if(medidaB instanceof CountInstanceMeasure){	
				
				nameMeasureB = UtilsObjectOWLPpinot.getNameCountInstanceMeasure(medidaB);
				this.converterCountInstanceMeasureOWL((CountInstanceMeasure) medidaB, jaxbElement);
			}else if(medidaB instanceof StateConditionInstanceMeasure){
				
				nameMeasureB = UtilsObjectOWLPpinot.getNameStateConditionInstanceMeasure(medidaB);
				this.converterStateConditionInstanceMeasureOWL((StateConditionInstanceMeasure) medidaB, jaxbElement);
			}else if(medidaB instanceof DataInstanceMeasure){
				
				nameMeasureA = UtilsObjectOWLPpinot.getNameDataInstanceMeasure(medidaB);
				this.converterDataInstanceMeasureOWL((DataInstanceMeasure) medidaB, jaxbElement);
			}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
				
				nameMeasureB = UtilsObjectOWLPpinot.getDataPropertyConditionInstanceMeasure(medidaB);
				this.converterDataPropertyConditionInstanceMeasureOWL((DataPropertyConditionInstanceMeasure) medidaB, jaxbElement);
			}
		}

		String nameDerivedSingleInstance = element.getName();
		        
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDerivedSingleInstance) );
		OWLClass classDerivedMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Vocabulary.DERIVEDSINGLEINSTANCEMEASURE));
		OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
		ppinotMan.addAxiom(ppinotOnt, classAssertionDerivedMeasure);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Vocabulary.ISCALCULATED));
		OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureA) );
		OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureA);
		ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsA);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
		if (!nameMeasureB.isEmpty()) {
			OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureB) );
			OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureB);
			ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsB);
		}
		
	}

	
}
