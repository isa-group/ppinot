package es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import es.us.isa.ppinot.bpmnppinot_xml_owl.notation.Translator;
import es.us.isa.ppinot.bpmnppinot_xml_owl.notation.Vocabulary;


/**
 * @author Ana Belen Sanchez Jerez
 * Clase donde se van a definir las funciones que añaden las queries
 * para cada una de las medidas definidas en OWL
**/
public class AddQueriesOWL {

	
	static OWLOntologyManager ppinotMan;
	static OWLOntology ppinotOnt;
	static String orgppinot; 
	static String orgppinot_extra; 

	static OWLOntologyManager bpmnMan;
	static OWLOntology bpmnOnt;
	static String orgbpmn; //Ontologia de entrada
	
	static OWLDataFactory factory;

	static String fileOWLBpmn; //Fichero de salida con las declaraciones de individuals.
	static String fileOWLPpinot; //Fichero de salida con las declaraciones de individuals.
	static Hashtable<String, String>funcAggr; 
	
	static String nameQuery;
	static String BPElement;
	
	
	AddQueriesOWL(OWLDataFactory factoryIn, 
			OWLOntologyManager bpmnManIn, OWLOntology bpmnOntIn, String orgbpmnIn,
			OWLOntologyManager ppinotManIn, OWLOntology ppinotOntIn, String orgppinotIn,
			String orgppinot_extraIn,
			String orgbpmnExpr, String orgppinotExpr){
		
		ppinotMan = ppinotManIn;
		ppinotOnt = ppinotOntIn;
		orgppinot = orgppinotIn;
		orgppinot_extra = orgppinot_extraIn;

		ppinotMan = ppinotManIn;
		ppinotOnt = ppinotOntIn;
		orgbpmn = orgbpmnIn;

		factory = factoryIn;

		fileOWLBpmn = orgbpmnExpr;
		fileOWLPpinot = orgppinotExpr;
		BPElement = "BPElement";
		nameQuery = "ImpliedBPFlowElement";
	}

	/** This function add owl queries to CountMeasures
	 * @param nameCountMeasure 
	 * @param countIndividual **/
	public void addQueriesToCountInstanceMeasure(String nameCountMeasure,OWLIndividual countIndividual) {

        /**First Class**/
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+nameCountMeasure+nameQuery);
	    OWLClass classCount = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty isCountIn = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISCOUNTIN)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(isCountIn,countIndividual);
        OWLObjectProperty isUsedInCond = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISUSEDINCONDITION)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(isUsedInCond,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.IMPLIEDXORGATEWAYFOR)+nameCountMeasure);
        OWLClassExpression ImpliedXorGatewayFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(ImpliedXorGatewayFor,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classCount, hasUnion);
        IRI iri3 = IRI.create(orgbpmn+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classCount, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
        
        /**Second Class**/
	    OWLClass classImpliedXorGatewayFor = factory.getOWLClass(iri2);
	   //Properties
	   
	    OWLObjectProperty precedes = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.PRECEDES)));
	    OWLClassExpression SomeValues3 = factory.getOWLObjectSomeValuesFrom(precedes,SomeValues);
	    //Intersection
	    IRI iri4 = IRI.create(orgbpmn+"#XorGateway");
        OWLClassExpression XorGateway = factory.getOWLClass(iri4);
        OWLClassExpression hasIntersection5 = factory.getOWLObjectIntersectionOf(XorGateway,SomeValues3);
      //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent2 = factory.getOWLEquivalentClassesAxiom(classImpliedXorGatewayFor, hasIntersection5);
        OWLSubClassOfAxiom axSubClass2 = factory.getOWLSubClassOfAxiom(classImpliedXorGatewayFor, XorGateway);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent2);
        ppinotMan.addAxiom(ppinotOnt, axSubClass2);
	}
	
	/** This function add owl queries to TimeMeasures
	 * @param nameTimeMeasure 
	 * @param timeIndividual **/
	public void addQueriesToTimeInstanceMeasure(String nameTimeMeasure, OWLIndividual timeIndividual){
		
		/**first Class**/
		String TimeQuery = nameTimeMeasure + nameQuery;
        OWLClass classTime = factory.getOWLClass( IRI.create(fileOWLPpinot+"#"+TimeQuery) );
        
        OWLClass fromClass = factory.getOWLClass( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.ISFROMFOR)+nameTimeMeasure) );
        OWLClass isInPathForClass = factory.getOWLClass( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.ISINPATHFOR)+nameTimeMeasure) );
        OWLClass toClass = factory.getOWLClass( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.ISTOFOR)+nameTimeMeasure) );
        
        //Union
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(fromClass, isInPathForClass, toClass);
        
        //Intersection
        OWLClassExpression BPFlowElement = factory.getOWLClass( IRI.create(orgbpmn+"#"+BPElement) );
        OWLClassExpression hasIntersection = factory.getOWLObjectIntersectionOf(BPFlowElement, hasUnion);
        
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classTime, hasIntersection);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classTime, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
        
        /**Second Class isFromFor**/
        //Properties
        OWLObjectProperty isFromFor = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISFROMFOR)));
        OWLClassExpression HasValueisFromFor= factory.getOWLObjectHasValue(isFromFor, timeIndividual);
        OWLObjectProperty isUsedInCondition = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISUSEDINCONDITION)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(isUsedInCondition, HasValueisFromFor);
        //Intersection
        OWLClassExpression hasIntersection2 = factory.getOWLObjectIntersectionOf(BPFlowElement,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent2 = factory.getOWLEquivalentClassesAxiom(fromClass, hasIntersection2);
        OWLSubClassOfAxiom axSubClass2 = factory.getOWLSubClassOfAxiom(fromClass, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent2);
        ppinotMan.addAxiom(ppinotOnt, axSubClass2);
        
        /**Third Class isToFor**/
        //Properties
        OWLObjectProperty isToFor = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISTOFOR)));
        OWLClassExpression HasValueisToFor= factory.getOWLObjectHasValue(isToFor,timeIndividual);
        OWLClassExpression SomeValues3= factory.getOWLObjectSomeValuesFrom(isUsedInCondition, HasValueisToFor);
        //Intersection
        OWLClassExpression hasIntersection3 = factory.getOWLObjectIntersectionOf(BPFlowElement,SomeValues3);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent3 = factory.getOWLEquivalentClassesAxiom(toClass, hasIntersection3);
        OWLSubClassOfAxiom axSubClass3 = factory.getOWLSubClassOfAxiom(toClass, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent3);
        ppinotMan.addAxiom(ppinotOnt, axSubClass3);
         
        /**Fourth Class IsInPathForTimeMeasure**/
        //Properties
        OWLObjectProperty precedes = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.PRECEDES)));
        OWLClassExpression SomeValues4= factory.getOWLObjectSomeValuesFrom(precedes, toClass);
        OWLObjectProperty succeeds = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.SUCCEEDS)));
        OWLClassExpression SomeValues5= factory.getOWLObjectSomeValuesFrom(succeeds, fromClass);
        //Intersection
        OWLClassExpression hasIntersection4 = factory.getOWLObjectIntersectionOf(SomeValues4,SomeValues5);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent4 = factory.getOWLEquivalentClassesAxiom(isInPathForClass, hasIntersection4);
        OWLSubClassOfAxiom axSubClass4 = factory.getOWLSubClassOfAxiom(isInPathForClass, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent4);
        ppinotMan.addAxiom(ppinotOnt, axSubClass4);
	}
	
/**********************************************************************/

	/** This function add owl queries to TimeAggregatedMeasure
	 * @param nameTimeAggregatedMeasure 
	 * @param timeAggregated 
	 * @param nameTimeInstance **/
	public void addQueriesToTimeAggregatedMeasure(String nameTimeAggregatedMeasure, OWLIndividual timeAggregated, String nameTimeInstance) {
		/**first Class**/
		String TimeAggQuery =nameTimeAggregatedMeasure+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+TimeAggQuery);
        OWLClass classTimeAgg = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty groups = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.GROUPS)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(groups,timeAggregated);
        OWLObjectProperty dataProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAPROPERTY)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(dataProperty,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+nameTimeInstance+Translator.translate(Vocabulary.IMPLIEDBPFLOWELEMENT));
        OWLClassExpression timeInstanceImpliedBPFlowElement = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(timeInstanceImpliedBPFlowElement,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classTimeAgg, hasUnion);
        IRI iri3 = IRI.create(orgbpmn+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classTimeAgg, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
		
	}

	/** This function add owl queries to CountAggregatedMeasure
	 * @param nameCountAggregatedMeasure 
	 * @param countAggregated 
	 * @param nameCountInstance **/
	public void addQueriesToCountAggregatedMeasure(String nameCountAggregated,OWLIndividual countAggregated, String nameCountInstance) {
		
		/**first Class**/
		String CountQuery =nameCountAggregated+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+CountQuery);
        OWLClass classCount = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty groups = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.GROUPS)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(groups,countAggregated);
        OWLObjectProperty dataProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAPROPERTY)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(dataProperty,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+nameCountInstance+Translator.translate(Vocabulary.IMPLIEDBPFLOWELEMENT));
        OWLClassExpression ImpliedXorGatewayFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(ImpliedXorGatewayFor,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classCount, hasUnion);
        IRI iri3 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classCount, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
        
	}

	/** This function add owl queries to DataConditionAggregatedMeasure  and ElementConditionAggregatedMeasure**/
	public void addQueriesToConditionAggregatedMeasure(String nameDataConditionAggregated,OWLIndividual dataConditionAggregated,String nameDataConditionInstance) {
		
		/**first Class**/
		String CountQuery =nameDataConditionAggregated+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+CountQuery);
        OWLClass classCount = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty groups = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.GROUPS)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(groups,dataConditionAggregated);
        OWLObjectProperty dataProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAPROPERTY)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(dataProperty,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+nameDataConditionInstance+Translator.translate(Vocabulary.IMPLIEDBPFLOWELEMENT));
        OWLClassExpression ImpliedXorGatewayFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(ImpliedXorGatewayFor,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classCount, hasUnion);
        IRI iri3 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classCount, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
	}

	
	/** This function add owl queries to DataConditionMeasures and ElementConditionMeasure**/
	public void addQueriesToConditionInstanceMeasure(String nameDataConditionInstance, OWLIndividual dataConditionInstance) {
		/**first Class**/
		String DataCondQuery =nameDataConditionInstance+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+DataCondQuery);
        OWLClass classDataCond = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty isMetBy = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISMETBY)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(isMetBy,dataConditionInstance);
        OWLObjectProperty isUsedInCondition = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISUSEDINCONDITION)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(isUsedInCondition,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.WRITERACTIVITYFOR)+nameDataConditionInstance);
        OWLClassExpression WriterActivityFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(WriterActivityFor,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classDataCond, hasUnion);
        IRI iri3 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classDataCond, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
        
        /**Second Class**/
        OWLClass WriterActivityForClass = factory.getOWLClass(iri2);
        //Properties
        OWLObjectProperty isMeasuredIn = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISMEASUREDIN)));
        OWLClassExpression Hasvalue2= factory.getOWLObjectHasValue(isMeasuredIn,dataConditionInstance);
        //Union
        OWLClassExpression hasUnion2 = factory.getOWLObjectUnionOf(SomeValues,Hasvalue2);
        //Intersection
	    IRI iri4 = IRI.create(orgbpmn+"#DataObject");
        OWLClassExpression DataObject = factory.getOWLClass(iri4);
        OWLClassExpression hasIntersection1 = factory.getOWLObjectIntersectionOf(DataObject,hasUnion2);
        //Properties
        OWLObjectProperty DataOutputAssociation = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAOUPUTASSOCIATION)));
        OWLClassExpression SomeValues2= factory.getOWLObjectSomeValuesFrom(DataOutputAssociation,hasIntersection1);
        //Intersection
	    IRI iri5 = IRI.create(orgbpmn+"#Activity");
        OWLClassExpression Activity = factory.getOWLClass(iri5);
        OWLClassExpression hasIntersection2 = factory.getOWLObjectIntersectionOf(Activity,SomeValues2);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent2 = factory.getOWLEquivalentClassesAxiom(WriterActivityForClass, hasIntersection2);
        OWLSubClassOfAxiom axSubClass2 = factory.getOWLSubClassOfAxiom(WriterActivityForClass, Activity);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent2);
        ppinotMan.addAxiom(ppinotOnt, axSubClass2);
	}
	
	/** This function add owl queries to DataAggregatedMeasure**/
	public void addQueriesToDataAggregatedMeasure(String nameDataAggregated,OWLIndividual dataAggregated, String nameDataInstance) {
		
		/**first Class**/
		String CountQuery =nameDataAggregated+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+CountQuery);
        OWLClass classCount = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty groups = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.GROUPS)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(groups,dataAggregated);
        OWLObjectProperty dataProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAPROPERTY)));
        OWLClassExpression SomeValues= factory.getOWLObjectSomeValuesFrom(dataProperty,Hasvalue);
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+nameDataInstance+Translator.translate(Vocabulary.IMPLIEDBPFLOWELEMENT));
        OWLClassExpression ImpliedXorGatewayFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(ImpliedXorGatewayFor,SomeValues);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classCount, hasUnion);
        IRI iri3 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classCount, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
	}

	/** This function add owl queries to DataInstanceMeasure**/
	public void addQueriesToDataInstanceMeasure(String nameDataInstance, OWLIndividual dataInstance) {
		// TODO Auto-generated method stub
		
		/**first Class**/
		String CountQuery = nameDataInstance+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+CountQuery);
        OWLClass classData = factory.getOWLClass(iri1);
        //Properties
        OWLObjectProperty isMeasuredIn = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISMEASUREDIN)));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(isMeasuredIn,dataInstance);
        
        //Union
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.WRITERACTIVITYFOR)+nameDataInstance);
        OWLClassExpression WriterActivityFor = factory.getOWLClass(iri2);
        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(WriterActivityFor,Hasvalue);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classData, hasUnion);
        IRI iri3 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri3);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classData, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
        
        /**Second Class**/
        OWLClass WriterActivityForClass = factory.getOWLClass(iri2);
        //Properties
        OWLObjectProperty isMetBy = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISMETBY)));
        OWLClassExpression Hasvalue2= factory.getOWLObjectHasValue(isMetBy,dataInstance);
        OWLObjectProperty isUsedInCondition = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.ISUSEDINCONDITION)));
        OWLClassExpression SomeValues2= factory.getOWLObjectSomeValuesFrom(isUsedInCondition,Hasvalue2);
        OWLClassExpression Hasvalue3= factory.getOWLObjectHasValue(isMeasuredIn,dataInstance);
        //Union
        OWLClassExpression hasUnion2 = factory.getOWLObjectUnionOf(SomeValues2,Hasvalue3);
        //Intersection
	    IRI iri4 = IRI.create(orgbpmn+"#DataObject");
        OWLClassExpression DataObject = factory.getOWLClass(iri4);
        OWLClassExpression hasIntersection1 = factory.getOWLObjectIntersectionOf(DataObject,hasUnion2);
        //Properties
        OWLObjectProperty DataOutputAssociation = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DATAOUPUTASSOCIATION)));
        OWLClassExpression SomeValues3= factory.getOWLObjectSomeValuesFrom(DataOutputAssociation,hasIntersection1);
        //Intersection
	    IRI iri5 = IRI.create(orgbpmn+"#Activity");
        OWLClassExpression Activity = factory.getOWLClass(iri5);
        OWLClassExpression hasIntersection2 = factory.getOWLObjectIntersectionOf(Activity,SomeValues3);
        //EquivalentClass
        OWLEquivalentClassesAxiom axEquivalent2 = factory.getOWLEquivalentClassesAxiom(WriterActivityForClass, hasIntersection2);
        OWLSubClassOfAxiom axSubClass2 = factory.getOWLSubClassOfAxiom(WriterActivityForClass, Activity);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent2);
        ppinotMan.addAxiom(ppinotOnt, axSubClass2);
		
	}
	/** This function add owl queries to DerivedInstanceMeasure and DerivedInstanceMeasure**/
	public void addQueriesToDerivedMeasure(String nameDerivedInstance,String nameMeasureA, String nameMeasureB) {
		
		/**first Class**/
		String DerivedQuery = nameDerivedInstance+nameQuery;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+DerivedQuery);
        OWLClass classDerived = factory.getOWLClass(iri1);
        //Union
        String MeasureAQuery = nameMeasureA+nameQuery;
        IRI iri2 = IRI.create(fileOWLPpinot+"#"+MeasureAQuery);
        OWLClassExpression MeasureA = factory.getOWLClass(iri2);
		if (!nameMeasureB.isEmpty()) {
	        String MeasureBQuery = nameMeasureB+nameQuery;
	        IRI iri3 = IRI.create(fileOWLPpinot+"#"+MeasureBQuery);
	        OWLClassExpression MeasureB = factory.getOWLClass(iri3);
	        OWLClassExpression hasUnion = factory.getOWLObjectUnionOf(MeasureA,MeasureB);
	        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(classDerived, hasUnion);
	        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
		}
        //EquivalentClass
        IRI iri4 = IRI.create(orgppinot+"#"+BPElement);
        OWLClassExpression BPFlowElement = factory.getOWLClass(iri4);
        OWLSubClassOfAxiom axSubClass = factory.getOWLSubClassOfAxiom(classDerived, BPFlowElement);
        ppinotMan.addAxiom(ppinotOnt, axSubClass);
	}
	
	/** This function add owl queries to a measure to get the directly elements that depend on this measure**/
	public void addQueriesToDirectlyDependOnMeasure(String nameMeasure){
		
		String MeasureQuery = UtilsObjectOWL.getNameQueriesDirectlyDependOn(nameMeasure);
        OWLClassExpression MeasureQueryclass = factory.getOWLClass( IRI.create(fileOWLPpinot+"#"+MeasureQuery) );

        OWLObjectPropertyExpression dependsDirectlyOn = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyExpression inverseOfdependson = dependsDirectlyOn.getInverseProperty();
        
        OWLNamedIndividual Measure = factory.getOWLNamedIndividual(IRI.create(fileOWLPpinot+"#"+nameMeasure));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(inverseOfdependson,Measure);
        
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(MeasureQueryclass,Hasvalue);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
	}
	
	/** This function add owl queries to a measure to get the directly elements that depend on this measure**/
	public void addQueriesToIndirectlyDependOnMeasure(String nameMeasure){
		
		String MeasureQuery = UtilsObjectOWL.getNameQueriesIndirectlyDependOn(nameMeasure);;
		IRI iri1 = IRI.create(fileOWLPpinot+"#"+MeasureQuery);
        OWLClassExpression MeasureQueryclass = factory.getOWLClass(iri1);
        OWLObjectPropertyExpression dependsDirectlyOn = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSINDIRECTLYON)));
        OWLObjectPropertyExpression inverseOfdependson = dependsDirectlyOn.getInverseProperty();
        
        OWLNamedIndividual Measure = factory.getOWLNamedIndividual(IRI.create(fileOWLPpinot+"#"+nameMeasure));
        OWLClassExpression Hasvalue= factory.getOWLObjectHasValue(inverseOfdependson,Measure);
        
        OWLEquivalentClassesAxiom axEquivalent = factory.getOWLEquivalentClassesAxiom(MeasureQueryclass,Hasvalue);
        ppinotMan.addAxiom(ppinotOnt, axEquivalent);
	}
	
	
}
