package es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
//import org.apache.bcel.generic.NEW;
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
import es.us.isa.ppinot.bpmnppinot_xml_owl.notation.Translator;
import es.us.isa.ppinot.bpmnppinot_xml_owl.notation.Vocabulary;

/**
 * @author Ana Belen Sanchez Jerez
 * 
 * Clase donde se van a definir las funciones que convierten los datos pasados por parametros
 * en instancias en owl 
**/
public class GenerateOWL {
	
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
	
	/**Constructor de GenerateOWL **/
	public GenerateOWL(OWLDataFactory factoryIn, 
			OWLOntologyManager bpmnManIn, OWLOntology bpmnOntIn, String orgbpmnIn,
			OWLOntologyManager ppinotManIn, OWLOntology ppinotOntIn, String orgppinotIn,
			String orgppinot_extraIn,
			String orgbpmnExpr, String orgppinotExpr){
		
		ppinotMan = ppinotManIn;
		ppinotOnt = ppinotOntIn;
		orgppinot = orgppinotIn;
		orgppinot_extra = orgppinot_extraIn;

		bpmnMan = bpmnManIn;
		bpmnOnt = bpmnOntIn;
		orgbpmn = orgbpmnIn;

		factory = factoryIn;

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
	
	
	/**Funcion para convertir elementos de tipo Activity a individuals en codigo owl
	 * @param nameDataObj 
	 * @param elementsDirectlyPrecedes **/
	public void converterActivityOWL(String nameActivity, String nameDataObj, List<String> elementsDirectlyPrecedes){
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameActivity);
 		IRI iri3 = IRI.create(fileOWLBpmn+"#"+nameDataObj);
		
        OWLNamedIndividual taskNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass activityClass = factory.getOWLClass(IRI.create(orgbpmn + "#Activity"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(activityClass, taskNameIndividual);
        
        if(nameDataObj != null){
        	OWLObjectPropertyExpression output = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#DataOutputAssociation"));
        	OWLNamedIndividual dataObjNameIndividualMeasure = factory.getOWLNamedIndividual(iri3);
        	OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(output, taskNameIndividual, dataObjNameIndividualMeasure);
        	bpmnMan.addAxiom(bpmnOnt, propertyAssertion);
        }
        
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, taskNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);
        
	}

	/**Funcion para convertir elementos de tipo StartEvent a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterStartEventOWL(String nameEvent, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameEvent);
 		
        OWLNamedIndividual EventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass StartEvent = factory.getOWLClass(IRI.create(orgbpmn + "#StartEvent"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(StartEvent, EventNameIndividual);
       
        if(elementsDirectlyPrecedes.size() > 0){
        	
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, EventNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);
	}
	
	/**Funcion para convertir elementos de tipo EndEvent a individuals en codigo owl**/
	public void converterEndEventOWL(String nameEvent) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameEvent);
 		
        OWLNamedIndividual EventNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Event = factory.getOWLClass(IRI.create(orgbpmn + "#EndEvent"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Event, EventNameIndividual);
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo XorGateway a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterXorGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameGtw);
 		
        OWLNamedIndividual GtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Gtw = factory.getOWLClass(IRI.create(orgbpmn + "#XorGateway"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Gtw, GtwNameIndividual);
        
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, GtwNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo Gateway a individuals en codigo owl
	 * @param elementsDirectlyPrecedes **/
	public void converterGatewayOWL(String nameGtw, List<String> elementsDirectlyPrecedes) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameGtw);
 		
        OWLNamedIndividual GtwNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass Gtw = factory.getOWLClass(IRI.create(orgbpmn + "#Gateway"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(Gtw, GtwNameIndividual);
        if(elementsDirectlyPrecedes.size() > 0){
        	Iterator<String> it = elementsDirectlyPrecedes.iterator();
        	while(it.hasNext()){
        		String elementDirectly = it.next();
        		OWLObjectPropertyExpression directlyPrecedes = factory.getOWLObjectProperty(IRI.create(orgbpmn+"#directlyPrecedes"));
        		OWLNamedIndividual DataObjNameElementdirectly = factory.getOWLNamedIndividual(IRI.create(fileOWLBpmn+"#"+elementDirectly));
        		OWLObjectPropertyAssertionAxiom propertyAssertion2 = factory.getOWLObjectPropertyAssertionAxiom(directlyPrecedes, GtwNameIndividual, DataObjNameElementdirectly);
        		bpmnMan.addAxiom(bpmnOnt, propertyAssertion2);
        	}
        }
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}

	/**Funcion para convertir elementos de tipo DataObject a individuals en codigo owl**/
	public void converterDataObjectOWL(String nameDataObj) {
		
		IRI iri2 = IRI.create(fileOWLBpmn+"#"+nameDataObj);
 		
        OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual(iri2);
        OWLClass DataObj = factory.getOWLClass(IRI.create(orgbpmn + "#DataObject"));
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividual);
        bpmnMan.addAxiom(bpmnOnt, classAssertion);	
	}
	
/********************************************************************************/

	/**Funcion que se encarga de convertir las medidas de tipo countInstanceMeasure en su correspondiente codigo owl 
	 * @return **/
	public OWLNamedIndividual converterCountInstanceMeasureOWL(String nameCountMeasure, String[] timeInstant, Boolean endActivity) 
	{
		
		// adiciona el axioma con la medida
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameCountMeasure) );
        OWLClass measureClass = factory.getOWLClass(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.COUNTMEASURE)));
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionAxiom);

		// adiciona el axioma que indica el momento en que se aplica la medida
        OWLNamedIndividual whenPropertyIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.TIMEINSTANCE)+nameCountMeasure) );
        OWLObjectPropertyExpression whenObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.WHEN)));
        OWLObjectPropertyAssertionAxiom whenObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(whenObjectProperty, measureIndividual, whenPropertyIndividual);
        ppinotMan.addAxiom(ppinotOnt, whenObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica la clase del momento en que se aplica la medida
        String type = timeInstant[1];
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
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
        OWLObjectPropertyExpression appliesToObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesToObjectProperty, whenPropertyIndividual, bpmnElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
        
        // adiciona el axioma que indica que la medida depende directamente de bpmnElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, measureIndividual, bpmnElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
        // devuelve el individual de la medida
        return measureIndividual;
	}

	/**Funcion que se encarga de convertir las medidas de tipo TimeInstanceMeasure en su correspondiente codigo owl 
	 * @return **/
	public OWLIndividual converterTimeInstanceMeasureOWL(String nameTimeMeasure, String[] timeInstantActivityFrom, 
			String[] timeInstantActivityTo,Boolean conectorEndFrom, Boolean conectorEndTo,
			String timeMeasureType, String singleInstanceAggFunction) 
	
	{
		
        // adiciona el axioma que indica que la medida es de la clase CyclicTimeMeasure o LinearTimeMeasure
        OWLNamedIndividual measureIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeMeasure) );
        IRI classIri;
        if (timeMeasureType!=null && timeMeasureType.toLowerCase().contentEquals("cyclic"))
        	classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.CYCLICTIMEMEASURE));
        else
        	classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.LINEARTIMEMEASURE));
        OWLClass measureClass = factory.getOWLClass( classIri );
        OWLClassAssertionAxiom classAssertionAxiom = factory.getOWLClassAssertionAxiom(measureClass, measureIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionAxiom);
        
        // adiciona el axioma que indica el valor de la propiedad from de la medida
        String nameActivityFrom = timeInstantActivityFrom[0];
        OWLObjectPropertyExpression fromObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FROM)));
        OWLNamedIndividual fromIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.START)+nameActivityFrom) );
        OWLObjectPropertyAssertionAxiom fromObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromObjectProperty, measureIndividual, fromIndividual);
        ppinotMan.addAxiom(ppinotOnt, fromObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad desde la cual se aplica la medida
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameActivityFrom) );
        OWLObjectPropertyAssertionAxiom fromAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromIndividual, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromAppliesToObjectPropertyAssertionAxiom);
        
        
        // adiciona el axioma que indica el valor de la propiedad to de la medida
        String nameActivityTo = timeInstantActivityTo[0];
        OWLObjectPropertyExpression toObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.TO)));
        OWLNamedIndividual toIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.END)+nameActivityTo) );
        OWLObjectPropertyAssertionAxiom toObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toObjectProperty, measureIndividual, toIndividual);
        ppinotMan.addAxiom(ppinotOnt, toObjectPropertyAssertionAxiom);
        
        // adiciona el  axioma que indica la actividad hasta la cual se aplica la medida
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameActivityTo) );
        OWLObjectPropertyAssertionAxiom toAppliesToObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toIndividual, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toAppliesToObjectPropertyAssertionAxiom);
 
        //Procesar el tipo
        String typeActivityFrom = timeInstantActivityFrom[1];
        IRI fromIri;
        if(typeActivityFrom == "Activity"){
        	if(conectorEndFrom){
        		fromIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		fromIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
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
        		toIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		toIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
        	}
        }else{
        	toIri = IRI.create(orgbpmn+"#"+typeActivityFrom);
        }
        OWLClass classTimeInstance = factory.getOWLClass(toIri);
        OWLClassAssertionAxiom classAssertionTimeInstTo = factory.getOWLClassAssertionAxiom(classTimeInstance, toIndividual);
        ppinotMan.addAxiom(ppinotOnt, classAssertionTimeInstTo);	
        
        // adiciona el axioma que indica que la medida depende directamente de fromDataObjectElement
        OWLObjectPropertyExpression fromDependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom fromDependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromDependsDirectlyOnObjectProperty, measureIndividual, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromDependsDirectlyOnObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica que la medida depende directamente de toDataObjectElement
        OWLObjectPropertyExpression toDependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom toDependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toDependsDirectlyOnObjectProperty, measureIndividual, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toDependsDirectlyOnObjectPropertyAssertionAxiom);
        
        // devuelve el individual de la medida
		return measureIndividual;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLNamedIndividual converterStateConditionInstanceMeasureOWL(String nameElementCondMeasure, String[] elementConditionActivity, String restriction) 
	{

        // adiciona el axioma de la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual(IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure));
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.STATECONDITIONMEASURE)) );
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);

        // adiciona el axioma con la restriccion de la medida
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEETS)));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FUNCTIONALPROPERTY)) );
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);

        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElement = elementConditionActivity[0];
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(orgbpmn+"#"+nameElement) );
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividualMeasure, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataPropertyConditionInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLIndividual converterDataPropertyConditionInstanceMeasureOWL(String nameDataCondMeasure,String dataObject, String restriction) 
	{
		
		// adiciona el axioma que indica la clase de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DATAPROPERTYCONDITIONMEASURE)) );
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEETS)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FUNCTIONALPROPERTY)) );
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividualMeasure, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
        return DataObjNameIndividualMeasure;
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataInstanceMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public OWLNamedIndividual converterDataInstanceMeasureOWL(String nameElementMeasure, String dataObject, String restriction) 
	{

		// adiciona el axioma que indica la clase de la medida
        OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
        OWLClass classCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DATAMEASURE)) );
        OWLClassAssertionAxiom classAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classCountMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionCountMeasure);
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEASURESDATA)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividualMeasure, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
        return DataObjNameIndividualMeasure;
	}
	
/**********************************************************************/
/**********************************************************************/

	/**Funcion que se encarga de convertir las medidas de tipo countAggregatedMeasure en su correspondiente codigo owl 
	 * @return **/
	public ArrayList<Object> converterCountAggregatedMeasureOWL(String nameCountAggregatedMeasure, String functionAgg, String[] timeInstant, Boolean endActivity) {

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
        OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
         
        // adiciona el axioma que indica la clase de la medida que esta siendo agregada
        OWLClass classIntermediateCountMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.COUNTMEASURE)) );
        OWLClassAssertionAxiom classIntermediateAssertionCountMeasure = factory.getOWLClassAssertionAxiom(classIntermediateCountMeasure, DataObjNameIndividual);
        ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionCountMeasure);
        //--------------------------------------------
        
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression meets = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.WHEN)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.TIMEINSTANT)+nameCountAggregatedMeasure) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meets, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElement = timeInstant[0];
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElement) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String type = timeInstant[1];
        IRI classIri;
        if(type == "Activity"){
        	if(endActivity){
        		classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		classIri = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
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
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividual, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
     
        return dataQueries;
        
	}

	/**Funcion que se encarga de convertir las medidas de tipo TimeAggregatedMeasure en su correspondiente codigo owl 
	 * @param <K>
	 * @return **/
	public  ArrayList<Object> converterTimeAggregatedMeasureOWL(String nameTimeAggregatedMeasure, String functionAgg,
			String[] timeInstantActivityFrom, String[] timeInstantActivityTo,Boolean conectorEndFrom, Boolean conectorEndTo) 
	{
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeAggregatedMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameTimeAggregatedMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.TIMEMEASURE)) );
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------

	    // FROM
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression from = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FROM)));
        OWLNamedIndividual fromDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.TIMEINSTANT)+nameTimeAggregatedMeasure+"From") );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(from, DataObjNameIndividual, fromDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, fromPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElementFrom = timeInstantActivityFrom[0];
        OWLObjectPropertyExpression fromAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual fromDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElementFrom) );
        OWLObjectPropertyAssertionAxiom fromPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(fromAppliesTo, fromDataObjectInstant, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String typeFrom = timeInstantActivityFrom[1];
        IRI classIriFrom;
        if(typeFrom == "Activity"){
        	if(conectorEndFrom){
        		classIriFrom = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		classIriFrom = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
        	}
        }else{
        	classIriFrom = IRI.create(orgbpmn+"#"+typeFrom);
        }
        OWLClass fromClassCountTimeInstance = factory.getOWLClass(classIriFrom);
        OWLClassAssertionAxiom fromClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(fromClassCountTimeInstance, fromDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, fromClassAssertionTimeInst);	
 
	    // TO
        // adiciona el axioma que indica en que momento se aplica la medida
        OWLObjectPropertyExpression to = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.TO)));
        OWLNamedIndividual toDataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+Translator.translate(Vocabulary.TIMEINSTANT)+nameTimeAggregatedMeasure+"To") );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(to, DataObjNameIndividual, toDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, toPropertyAssertionmeets);
        
        // adiciona el axioma que indica a que elemento se aplica la medida
        String nameElementTo = timeInstantActivityTo[0];
        OWLObjectPropertyExpression toAppliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual toDataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+nameElementTo) );
        OWLObjectPropertyAssertionAxiom toPropertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(toAppliesTo, toDataObjectInstant, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toPropertyAssertionappliesTo);
        
        // adiciona la clase del momento en que se aplica la medida
        String typeTo = timeInstantActivityTo[1];
        IRI classIriTo;
        if(typeTo == "Activity"){
        	if(conectorEndFrom){
        		classIriTo = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYEND));
        	}else{
        		classIriTo = IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ACTIVITYSTART));
        	}
        }else{
        	classIriTo = IRI.create(orgbpmn+"#"+typeFrom);
        }
        OWLClass toClassCountTimeInstance = factory.getOWLClass(classIriTo);
        OWLClassAssertionAxiom toClassAssertionTimeInst = factory.getOWLClassAssertionAxiom(toClassCountTimeInstance, toDataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, toClassAssertionTimeInst);	
	     
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression fromDependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom fromDependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(fromDependsDirectlyOnObjectProperty, DataObjNameIndividual, fromDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, fromDependsDirectlyOnObjectPropertyAssertionAxiom);
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression toDependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom toDependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(toDependsDirectlyOnObjectProperty, DataObjNameIndividual, toDataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, toDependsDirectlyOnObjectPropertyAssertionAxiom);
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameTimeAggregatedMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameTimeAggregatedMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}

	/**Funcion que se encarga de convertir las medidas de tipo StateConditionAggregatedMeasure en su correspondiente codigo owl 
	 * @param functionAgg 
	 * @param restriction 
	 * @return **/
	public ArrayList<Object> converterStateConditionAggregatedMeasureOWL( String nameElementCondMeasure, String[] elementConditionActivity,
			String functionAgg, String restriction) 
	{
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.STATECONDITIONMEASURE)) );
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEETS)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FUNCTIONALPROPERTY)) );
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+elementConditionActivity[0]) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividual, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);

        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementCondMeasure+Translator.translate(Vocabulary.INTERMEDIATE1));
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementCondMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
		
	}

	/**Funcion que se encarga de convertir las medidas de tipo DataPropertyConditionAggregatedeMeasure en su correspondiente codigo owl 
	 * @return 
	 **/
	public ArrayList<Object> converterDataPropertyConditionAggregatedMeasureOWL(String nameDataCondMeasure, String dataObject, String functionAgg,String restriction) 
	{
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DATAPROPERTYCONDITIONMEASURE)) );
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------

		// adiciona el axioma de la restriccion de la medida
        OWLObjectPropertyExpression meetsIC = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEETS)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDataCondMeasure+restriction) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(meetsIC, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma con la clase de la restriccion
        OWLClass restrictionClass = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.FUNCTIONALPROPERTY)) );
        OWLClassAssertionAxiom restrictionClassAssertionAxiom = factory.getOWLClassAssertionAxiom(restrictionClass, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, restrictionClassAssertionAxiom);
       
        // adiciona el axioma que indica el elemento al que se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.APPLIESTO)));
        OWLNamedIndividual dataObjectElement = factory.getOWLNamedIndividual( IRI.create(fileOWLBpmn+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionappliesTo = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, dataObjectInstant, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionappliesTo);	
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividual, dataObjectElement);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
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
	public ArrayList<Object> converterDataAggregatedMeasureOWL(String nameElementMeasure, String dataObject, String functionAgg, String restriction) 
	{
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DATAMEASURE)) );
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
        // adiciona el axioma que indica a que elemento se aplica la medida
        OWLObjectPropertyExpression appliesTo = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.MEASURESDATA)));
        OWLNamedIndividual dataObjectInstant = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+dataObject) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeets = factory.getOWLObjectPropertyAssertionAxiom(appliesTo, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeets);
        
        // adiciona el axioma que indica que la medida depende directamente de dataObjectElement
        OWLObjectPropertyExpression dependsDirectlyOnObjectProperty = factory.getOWLObjectProperty(IRI.create(orgppinot_extra+"#"+Translator.translate(Vocabulary.DEPENDSDIRECTLYON)));
        OWLObjectPropertyAssertionAxiom dependsDirectlyOnObjectPropertyAssertionAxiom = factory.getOWLObjectPropertyAssertionAxiom(dependsDirectlyOnObjectProperty, DataObjNameIndividual, dataObjectInstant);
        ppinotMan.addAxiom(ppinotOnt, dependsDirectlyOnObjectPropertyAssertionAxiom);
        
        ArrayList<Object> dataQueries = new ArrayList<Object>();
        dataQueries.add(nameElementMeasure+"Intermediate1");
        dataQueries.add(DataObjNameIndividualMeasure);
        dataQueries.add(nameElementMeasure);
        dataQueries.add(DataObjNameIndividual);
     
        return dataQueries;
	}
	
	public ArrayList<Object> converterDerivedSingleInstanceAggregatedMeasureOWL(String nameElementMeasure, String functionDeriv, String nameMeasureA, String nameMeasureB, String functionAgg) {
	       
	    // adiciona el axioma que indica la clase de la medida
		initializeFuncsMeasure();
	    OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure+"Intermediate1") );
		String AggregationFunction = funcAggr.get(functionAgg);
	    OWLClass DataObj = factory.getOWLClass(IRI.create(orgppinot + "#"+AggregationFunction));
	    OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(DataObj, DataObjNameIndividualMeasure);
	    ppinotMan.addAxiom(ppinotOnt, classAssertion);	
			
		// adiciona el axioma que indica la medida que esta siendo agregada
	    OWLNamedIndividual DataObjNameIndividual = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameElementMeasure) );
	    OWLObjectPropertyExpression aggregates = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.AGGREGATES)));
	    OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(aggregates, DataObjNameIndividualMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, propertyAssertion);
	      
	    // adiciona el axioma que indica la clase de la medida que esta siendo agregada
	    OWLClass classIntermediateMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE)) );
	    OWLClassAssertionAxiom classIntermediateAssertionMeasure = factory.getOWLClassAssertionAxiom(classIntermediateMeasure, DataObjNameIndividual);
	    ppinotMan.addAxiom(ppinotOnt, classIntermediateAssertionMeasure);
	    //--------------------------------------------
		
		// adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ISCALCULATED)));
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
	 * en su correspondiente codigo owl **/
	public void converterDerivedMultiInstanceMeasureOWL(String nameDerivedMultiInstance,String functionDeriv, String nameMeasureA, String nameMeasureB) {
		
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDerivedMultiInstance) );
        OWLClass classDerivedMeasure = factory.getOWLClass(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DERIVEDMULTIINSTANCEMEASURE)));
        OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
        ppinotMan.addAxiom(ppinotOnt, classAssertionDerivedMeasure);
        
        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ISCALCULATED)));
        OWLNamedIndividual measureA = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureA ));
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsA = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureA);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsA);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
        OWLNamedIndividual measureB = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameMeasureB) );
        OWLObjectPropertyAssertionAxiom propertyAssertionmeetsB = factory.getOWLObjectPropertyAssertionAxiom(isCalculated, DataObjNameIndividualMeasure, measureB);
        ppinotMan.addAxiom(ppinotOnt, propertyAssertionmeetsB);
	}

	/**Funcion que se encarga de convertir las medidas de tipo DerivedSingleInstanceMeasure 
	 * en su correspondiente codigo owl **/
	public void converterDerivedSingleInstanceMeasureOWL(String nameDerivedSingleInstance,String functionDeriv, String nameMeasureA, String nameMeasureB) {
		        
		// adiciona el axioma de la medida
		OWLNamedIndividual DataObjNameIndividualMeasure = factory.getOWLNamedIndividual( IRI.create(fileOWLPpinot+"#"+nameDerivedSingleInstance) );
		OWLClass classDerivedMeasure = factory.getOWLClass( IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE)) );
		OWLClassAssertionAxiom classAssertionDerivedMeasure = factory.getOWLClassAssertionAxiom(classDerivedMeasure, DataObjNameIndividualMeasure);
		ppinotMan.addAxiom(ppinotOnt, classAssertionDerivedMeasure);

        // adiciona el axioma con la relacion de la medida A y la medida derivada
		OWLObjectPropertyExpression isCalculated = factory.getOWLObjectProperty(IRI.create(orgppinot+"#"+Translator.translate(Vocabulary.ISCALCULATED)));
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
