package es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.state.GenericState;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.isabpm.ppinot.model.condition.StateCondition;
import es.us.isa.isabpm.ppinot.model.MeasureDefinition;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataOutputAssociation;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;

/**
 * @author Ana Belen Sanchez Jerez
 * **/
@SuppressWarnings({ "unchecked", "rawtypes" })
public class inObjectsOutOWL {

	static OWLOntologyManager bpmnMan;
	static OWLOntologyManager ppinotMan;
	
	static File bpmnFile;
	static File ppinotFile;
	
	static OWLOntology bpmnOnt;
	static OWLOntology ppinotOnt;
	
	static String orgppinot;
	static String orgppinot_extra;
	static String orgbpmn;
	static GenerateOWL converter;
	static AddQueriesOWL queries;
	static List<TSequenceFlow> sequenceFlows;
	
	/**Constructor de la clase inObjectsOutOWL que inicializa todos los objetos
	 * necesarios de la api OWL para poder trabajar con ellos. Inicializa 
	 * OWLManager, OWLOntology,OWLDataFactory, las urls a ficheros owl. **/
	public inObjectsOutOWL(String path, List<TSequenceFlow> sequenceFlowList){
		
		bpmnFile = new File(path+"ExpressionsOWLBpmn.owl");
		try {
			bpmnFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ppinotFile = new File(path+"ExpressionsOWLPpinot.owl");
		try {
			ppinotFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		sequenceFlows = sequenceFlowList; 
		//Build Ontology
		bpmnMan = OWLManager.createOWLOntologyManager();
		ppinotMan = OWLManager.createOWLOntologyManager();
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		orgppinot_extra = "http://fjtoscano.webfactional.com/isa/ppinot/ppinot-extras.owl";
		String orgbpmnExpr = bpmnFile.toURI().toString();
		String orgppinotExpr = ppinotFile.toURI().toString();
		
		try {
	            // Let's load an ontology from the web.  We load the ontology from a document IRI
//	            ppinotOnt = ppinotMan.loadOntologyFromOntologyDocument( IRI.create(orgppinot) );
//				bpmnOnt = bpmnMan.loadOntologyFromOntologyDocument( IRI.create(orgbpmn) );

				bpmnOnt = bpmnMan.createOntology();
	            ppinotOnt = ppinotMan.createOntology();

				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgbpmn) )));
				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgppinot) )));
				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgppinot_extra) )));
				ppinotMan.applyChange(new AddImport(ppinotOnt, ppinotMan.getOWLDataFactory().getOWLImportsDeclaration( IRI.create(orgbpmnExpr) )));
	            
				converter = new GenerateOWL(bpmnMan.getOWLDataFactory(), bpmnMan, bpmnOnt, orgbpmn, ppinotMan, ppinotOnt, orgppinot, orgppinot_extra, orgbpmnExpr, orgppinotExpr); //Objeto para luego convertir a owl
	            queries = new AddQueriesOWL(ppinotMan.getOWLDataFactory(), bpmnMan, bpmnOnt, orgbpmn, ppinotMan, ppinotOnt, orgppinot, orgppinot_extra, orgbpmnExpr, orgppinotExpr);

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	/***Declaraciones en owl de taskList de BPMN2.0 ***/
	public void getDeclarationIndividualsTask (List<TTask> taskList){
		//Función para crear los individuals de los elementos Task
		Iterator<?> itr = taskList.iterator(); 
		while(itr.hasNext()) {
			Object obj = itr.next();
			TTask element = (TTask) obj;
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameTask = element.getName();
			nameTask = nameTask.replaceAll(" ", "");
			nameTask = nameTask.replaceAll("\r\n", "").replaceAll("\n", "");
			//Tambien voy a necesitar el dataobject al que se conecta en el caso de hacerlo
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				nameDataObj = UtilsObjectOWL.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart());
System.out.println("dataOutput de "+nameTask+" con su objeto"+nameDataObj);
			}
		    List<String> elementsDirectlyPrecedes = UtilsObjectOWL.getDirectlyPrecedes(sequenceFlows,obj);
			converter.converterActivityOWL(nameTask, nameDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de subProcess de BPMN2.0 ***/
	public void getDeclarationIndividualsSubProcess (List<TSubProcess> subprocessList){
		
		Iterator<?> itr = subprocessList.iterator(); 
		while(itr.hasNext()) {
			Object obj = itr.next();
			TSubProcess element = (TSubProcess) obj;
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameActivity = element.getName();
			nameActivity = nameActivity.replaceAll(" ", "");
			nameActivity = nameActivity.replaceAll("\r\n", "").replaceAll("\n", "");
			String nameDataObj = null;
			List<TDataOutputAssociation> dataOutput = element.getDataOutputAssociation();
			//Se supone que solo hay un dataobject por actividad
			for (TDataOutputAssociation tDataOutputAssociation : dataOutput) {
				nameDataObj = UtilsObjectOWL.getNameDataObject(((QName) tDataOutputAssociation.getTargetRef()).getLocalPart());
				System.out.println("dataOutput de "+nameActivity+" con su objeto"+nameDataObj);
			}
			 List<String> elementsDirectlyPrecedes = UtilsObjectOWL.getDirectlyPrecedes(sequenceFlows,obj);
		    converter.converterActivityOWL(nameActivity, nameDataObj, elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de startEvents de BPMN2.0 ***/
	public void getDeclarationIndividualsStartEvent (List<TStartEvent> startEventList){
		//Función para crear los individuals de los elementos startEvent
		Iterator<?> itr = startEventList.iterator(); 
		while(itr.hasNext()) {
			Object obj = itr.next();
			TStartEvent element = (TStartEvent) obj;
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameEvent = element.getName();
			nameEvent = nameEvent.replaceAll(" ", "");
			nameEvent = nameEvent.replaceAll("\r\n", "").replaceAll("\n", "");
			
			List<String> elementsDirectlyPrecedes = UtilsObjectOWL.getDirectlyPrecedes(sequenceFlows,obj);
		    converter.converterStartEventOWL(nameEvent, elementsDirectlyPrecedes);
		    
		}
	}
	
	/***Declaraciones en owl de EndEvents de BPMN2.0 ***/
	public void getDeclarationIndividualsEndEvent (List<TEndEvent> endEventList){
		
		Iterator<?> itr = endEventList.iterator(); 
		while(itr.hasNext()) {
			TEndEvent element = (TEndEvent) itr.next();
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameEvent = element.getName();
			nameEvent = nameEvent.replaceAll(" ", "");
			nameEvent = nameEvent.replaceAll("\r\n", "").replaceAll("\n", "");
		    converter.converterEndEventOWL(nameEvent);
		}
	}
	
	/***Declaraciones en owl de exclusiveGateways de BPMN2.0 ***/
	public void getDeclarationIndividualsXorGateways(List<TExclusiveGateway> exclusiveGateways) {
		
		Iterator<?> itr = exclusiveGateways.iterator(); 
		while(itr.hasNext()) {
			Object obj = itr.next();
			TExclusiveGateway element = (TExclusiveGateway) obj;
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameGtw = element.getName();
			nameGtw = nameGtw.replaceAll(" ", "");
			nameGtw = nameGtw.replaceAll("\r\n", "").replaceAll("\n", "");
			
			List<String> elementsDirectlyPrecedes = UtilsObjectOWL.getDirectlyPrecedes(sequenceFlows,obj);
		    converter.converterXorGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
		
	}
	
	/***Declaraciones en owl de Gateways generales de BPMN2.0 ***/
	public void getDeclarationIndividualsGateways(List<TGateway> TGatewaysList) {
		
		Iterator<?> itr = TGatewaysList.iterator(); 
		while(itr.hasNext()) {
			Object obj = itr.next();
			TGateway element = (TGateway) obj;
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameGtw = element.getName();
			nameGtw = nameGtw.replaceAll(" ", "");
			nameGtw = nameGtw.replaceAll("\r\n", "").replaceAll("\n", "");
			List<String> elementsDirectlyPrecedes = UtilsObjectOWL.getDirectlyPrecedes(sequenceFlows,obj);
		    converter.converterGatewayOWL(nameGtw,elementsDirectlyPrecedes);
		}
	}
	
	/***Declaraciones en owl de DataObjects de BPMN2.0 ***/
	public void getDeclarationIndividualsDataObject(List<TDataObject> dataObjectList) {
		
		Iterator<?> itr = dataObjectList.iterator(); 
		while(itr.hasNext()) {
			TDataObject element = (TDataObject) itr.next();
			//Por cada tarea tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameDataObj = element.getName();
			nameDataObj = nameDataObj.replaceAll(" ", "");
			nameDataObj = nameDataObj.replaceAll("\r\n", "").replaceAll("\n", "");
		    converter.converterDataObjectOWL(nameDataObj);
		}
	}

//***********************************************************************/
	
	/***Declaraciones en owl de la medida countInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsCountInstanceMeasure(List<CountInstanceMeasure> countInstanceMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = countInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			CountInstanceMeasure element = (CountInstanceMeasure) itr.next();
			//Por cada medida tengo que ir convirtiendo a su declaracion de instanciacion en OWL

			// obtiene el nombre de la actividad
			String nameCountMeasure= element.getName();
			nameCountMeasure = nameCountMeasure.replaceAll(" ", "");
			nameCountMeasure = nameCountMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			// obtiene si la medida se aplica al inicio o al final de la actividad
			Boolean endActivity = element.getWhen().getChangesToState().getState()==GenericState.END;
			// obtiene el nombre y el tipo de la actividad a la que se aplica la medida
			String[] timeInstant = UtilsObjectOWL.getNameTypeActivity(element.getWhen().getAppliesTo(),jaxbElement);
			
System.out.println("kekaEndActivity"+endActivity+ " Name"+nameCountMeasure);
		    OWLIndividual countIndividual = converter.converterCountInstanceMeasureOWL(nameCountMeasure,timeInstant,endActivity);
		    queries.addQueriesToCountInstanceMeasure(nameCountMeasure,countIndividual);
		    
		    getDeclarationMeasureDependOn(nameCountMeasure);
		}
		
	}
	
	/***Declaraciones en owl de la medida timeInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsTimeInstanceMeasure(List<TimeInstanceMeasure> timeMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = timeMeasure.iterator(); 
		while(itr.hasNext()) {
			TimeInstanceMeasure element = (TimeInstanceMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameTimeMeasure= element.getName();
			nameTimeMeasure = nameTimeMeasure.replaceAll(" ", "");
			nameTimeMeasure = nameTimeMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			
			Boolean ConectorEndFrom = element.getFrom().getChangesToState().getState()==GenericState.END;
			Boolean ConectorEndTo = element.getTo().getChangesToState().getState()==GenericState.END;
			String ActivityFrom = element.getFrom().getAppliesTo();
			String ActivityTo = element.getTo().getAppliesTo();
			
			String timeMeasureType = element.getTimeMeasureType();
			String singleInstanceAggFunction = element.getSingleInstanceAggFunction();
			
			String[] timeInstantActivityFrom = UtilsObjectOWL.getNameTypeActivity(ActivityFrom,jaxbElement);
			String[] timeInstantActivityTo = UtilsObjectOWL.getNameTypeActivity(ActivityTo,jaxbElement);
			
			OWLIndividual timeIndividual = converter.converterTimeInstanceMeasureOWL(nameTimeMeasure,timeInstantActivityFrom,timeInstantActivityTo,ConectorEndFrom,ConectorEndTo,timeMeasureType, singleInstanceAggFunction);
		    queries.addQueriesToTimeInstanceMeasure(nameTimeMeasure,timeIndividual);
		    
		    getDeclarationMeasureDependOn(nameTimeMeasure);
		}	
	}
	
	/***Declaraciones en owl de la medida StateConditionInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsStateConditionInstanceMeasure(List<StateConditionInstanceMeasure> elementConditionMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = elementConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			StateConditionInstanceMeasure element = (StateConditionInstanceMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameElementCondMeasure= element.getName();
			nameElementCondMeasure = nameElementCondMeasure.replaceAll(" ", "");
			nameElementCondMeasure = nameElementCondMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			String Activity = ((StateCondition) element.getCondition()).getAppliesTo();
			String restriction = ((StateCondition) element.getCondition()).getState().getStateString();
					//element.getRestriction();	
			//Eliminamos el simbolo de comparacion 
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";

			String[] timeInstantActivity = UtilsObjectOWL.getNameTypeActivity(Activity,jaxbElement);
			
System.out.println("kekaName "+nameElementCondMeasure+"Activity:"+timeInstantActivity[0]+"Restricction:"+restriction);
		    OWLIndividual elementCondition = converter.converterStateConditionInstanceMeasureOWL(nameElementCondMeasure,timeInstantActivity,restriction);
		    queries.addQueriesToConditionInstanceMeasure(nameElementCondMeasure,elementCondition);
		    
		    getDeclarationMeasureDependOn(nameElementCondMeasure);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataPropertyConditionInstanceMeasure(List<DataPropertyConditionInstanceMeasure> dataConditionMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = dataConditionMeasure.iterator(); 
		while(itr.hasNext()) {
			DataPropertyConditionInstanceMeasure element = (DataPropertyConditionInstanceMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameDataCondMeasure= element.getName();
			nameDataCondMeasure = nameDataCondMeasure.replaceAll(" ", "");
			nameDataCondMeasure = nameDataCondMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			String dataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();
			dataObject = dataObject.replaceAll(" ", "");
			dataObject = dataObject.replaceAll("\r\n", "").replaceAll("\n", "");
			String restriction = ((DataPropertyCondition) element.getCondition()).getRestriction();	
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";
	
			OWLIndividual DataPropertyConditionInstance =  converter.converterDataPropertyConditionInstanceMeasureOWL(nameDataCondMeasure,dataObject,restriction);
			queries.addQueriesToConditionInstanceMeasure(nameDataCondMeasure, DataPropertyConditionInstance);
		    
		    getDeclarationMeasureDependOn(nameDataCondMeasure);
		}	
	}
	
	/***Declaraciones en owl de la medida DataInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataInstanceMeasure(List<DataInstanceMeasure> dataInstanceMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = dataInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			DataInstanceMeasure element = (DataInstanceMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String nameElementMeasure= element.getName();
			nameElementMeasure = nameElementMeasure.replaceAll(" ", "");
			nameElementMeasure = nameElementMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			//Devuelve el nombre del dataObject
			String DataObject = ((DataPropertyCondition) element.getCondition()).getAppliesTo();
			DataObject = DataObject.replaceAll(" ", "");
			DataObject = DataObject.replaceAll("\r\n", "").replaceAll("\n", "");
			String restriction = ((DataPropertyCondition) element.getCondition()).getRestriction();
			//Eliminamos el simbolo de comparacion 
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";
			
System.out.println("kekaName "+nameElementMeasure+"Activity:"+DataObject+"Restricction:"+restriction);
		    OWLIndividual DataInstance = converter.converterDataInstanceMeasureOWL(nameElementMeasure,DataObject,restriction);
		    queries.addQueriesToDataInstanceMeasure(nameElementMeasure, DataInstance);
		    
		    getDeclarationMeasureDependOn(nameElementMeasure);
		}	
	}

	/***Declaraciones en owl de medidas de tipo countAggregatedMeasure 
	 * @param jaxbElement 
	 * @param taskList 
	 * @throws Exception ***/
	
	/***Declaraciones en owl de la medida countAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsCountAggregatedMeasure(List<AggregatedMeasure> countAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = countAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL

			String nameCountAggregatedMeasure= element.getBaseMeasure().getName();
			nameCountAggregatedMeasure = nameCountAggregatedMeasure.replaceAll(" ", "");
			nameCountAggregatedMeasure = nameCountAggregatedMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			String functionAgg = element.getAggregationFunction();
			
			CountInstanceMeasure element2 = (CountInstanceMeasure) element.getBaseMeasure();
			Boolean endActivity = element2.getWhen().getChangesToState().getState()==GenericState.END;
					//element2.getAtEnd();
			
			//System.out.println("PruebaRestriction"+element2.getRestriction());
			String[] timeInstant = UtilsObjectOWL.getNameTypeActivity(element2.getWhen().getAppliesTo(),jaxbElement);
			ArrayList<Object> dataQueries = converter.converterCountAggregatedMeasureOWL(nameCountAggregatedMeasure,functionAgg,timeInstant,endActivity);
		   
		    String nameCountAggregated = (String) dataQueries.get(0);
		    OWLIndividual countAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameCountInstance = (String) dataQueries.get(2);
		    OWLIndividual CountInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToCountAggregatedMeasure(nameCountAggregated,countAggregated,nameCountInstance);
		    queries.addQueriesToCountInstanceMeasure(nameCountInstance, CountInstance);
		    
		    getDeclarationMeasureDependOn(nameCountAggregatedMeasure);
		}
		
	}

//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida timeAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsTimeAggregatedMeasure(List<AggregatedMeasure> timeAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = timeAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
				
			String nameTimeAggregatedMeasure= element.getBaseMeasure().getName();
			nameTimeAggregatedMeasure = nameTimeAggregatedMeasure.replaceAll(" ", "");
			nameTimeAggregatedMeasure = nameTimeAggregatedMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			String functionAgg = element.getAggregationFunction();
			
			TimeInstanceMeasure element2 = (TimeInstanceMeasure) element.getBaseMeasure();
			Boolean ConectorEndFrom = element2.getFrom().getChangesToState().getState()==GenericState.END;
			Boolean ConectorEndTo = element2.getTo().getChangesToState().getState()==GenericState.END;
			String ActivityFrom = element2.getFrom().getAppliesTo();
			String ActivityTo = element2.getTo().getAppliesTo();
System.out.println("kekaName "+nameTimeAggregatedMeasure + "funcion "+functionAgg+" ConectorEndFrom:"+ConectorEndFrom+"ConectorEndTo:"+ConectorEndTo+"ActivityFrom:"+ActivityFrom+" ActivityTo:"+ActivityTo);
			
			String[] timeInstantActivityFrom = UtilsObjectOWL.getNameTypeActivity(ActivityFrom,jaxbElement);
			String[] timeInstantActivityTo = UtilsObjectOWL.getNameTypeActivity(ActivityTo,jaxbElement);
	
		    ArrayList<Object> dataQueries = converter.converterTimeAggregatedMeasureOWL(nameTimeAggregatedMeasure,functionAgg,timeInstantActivityFrom,timeInstantActivityTo,ConectorEndFrom,ConectorEndTo);
		    String nameTimeAggregated = (String) dataQueries.get(0);
		    OWLIndividual timeAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameTimeInstance = (String) dataQueries.get(2);
		    OWLIndividual timeInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToTimeAggregatedMeasure(nameTimeAggregated,timeAggregated,nameTimeInstance);
		    queries.addQueriesToTimeInstanceMeasure(nameTimeInstance, timeInstance);
		    
		    getDeclarationMeasureDependOn(nameTimeAggregatedMeasure);
		}
		
	}
	
	
	/***Declaraciones en owl de la medida StateConditionAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsStateConditionAggregatedMeasure(List<AggregatedMeasure> elementConditionAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = elementConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionAgg = element.getAggregationFunction();
			StateConditionInstanceMeasure element2 = (StateConditionInstanceMeasure) element.getBaseMeasure();
			String nameElementCondMeasure= element2.getName();
			nameElementCondMeasure = nameElementCondMeasure.replaceAll(" ", "");
			nameElementCondMeasure = nameElementCondMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			String Activity = ((StateCondition) element2.getCondition()).getAppliesTo();
			String restriction = ((StateCondition) element2.getCondition()).getState().getStateString();
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");	
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";
			
			String[] timeInstantActivity = UtilsObjectOWL.getNameTypeActivity(Activity,jaxbElement);
			
System.out.println("kekaName "+nameElementCondMeasure+"Activity:"+timeInstantActivity[0]+"Restricction:"+restriction+ " funcion"+functionAgg);
			ArrayList<Object> dataQueries = converter.converterStateConditionAggregatedMeasureOWL(nameElementCondMeasure,timeInstantActivity,functionAgg,restriction);
		    
		    String nameEleConditionAggregated = (String) dataQueries.get(0);
		    OWLIndividual EleConditionAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameEleConditionInstance = (String) dataQueries.get(2);
		    OWLIndividual EleConditionInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToConditionAggregatedMeasure(nameEleConditionAggregated,EleConditionAggregated,nameEleConditionInstance);
		    queries.addQueriesToConditionInstanceMeasure(nameEleConditionInstance, EleConditionInstance);
		    
		    getDeclarationMeasureDependOn(nameElementCondMeasure);
		}	
	}
	
	/***Declaraciones en owl de la medida DataPropertyConditionAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(List<AggregatedMeasure> dataConditionAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = dataConditionAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionAgg = element.getAggregationFunction();
			DataPropertyConditionInstanceMeasure element2 = (DataPropertyConditionInstanceMeasure) element.getBaseMeasure();
			String nameElementCondMeasure= element2.getName();
			nameElementCondMeasure = nameElementCondMeasure.replaceAll(" ", "");
			nameElementCondMeasure = nameElementCondMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			//Devuelve el nombre del dataObject
			String DataObject = ((DataPropertyCondition) element2.getCondition()).getAppliesTo();
			DataObject = DataObject.replaceAll(" ", "");
			DataObject = DataObject.replaceAll("\r\n", "").replaceAll("\n", "");
			String restriction = ((DataPropertyCondition) element2.getCondition()).getRestriction();
			//Eliminamos el simbolo de comparacion 
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";
			
System.out.println("kekaName "+nameElementCondMeasure+"Activity:"+DataObject+"Restricction:"+restriction+ " funcion"+functionAgg);
			ArrayList<Object> dataQueries = converter.converterDataPropertyConditionAggregatedMeasureOWL(nameElementCondMeasure,DataObject,functionAgg,restriction);
		    String nameDataPropertyConditionAggregated = (String) dataQueries.get(0);
		    OWLIndividual DataPropertyConditionAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameDataPropertyConditionInstance = (String) dataQueries.get(2);
		    OWLIndividual DataPropertyConditionInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToConditionAggregatedMeasure(nameDataPropertyConditionAggregated,DataPropertyConditionAggregated,nameDataPropertyConditionInstance);
		    queries.addQueriesToConditionInstanceMeasure(nameDataPropertyConditionInstance, DataPropertyConditionInstance);
		    
		    getDeclarationMeasureDependOn(nameElementCondMeasure);
		}	
	}
	
	/***Declaraciones en owl de la medida DataAggregatedMeasure de PPINOT ***/
	public void getDeclarationIndividualsDataAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionAgg = element.getAggregationFunction();
			DataInstanceMeasure element2 = (DataInstanceMeasure) element.getBaseMeasure();
			String nameElementMeasure= element2.getName();
			nameElementMeasure = nameElementMeasure.replaceAll(" ", "");
			nameElementMeasure = nameElementMeasure.replaceAll("\r\n", "").replaceAll("\n", "");
			//Devuelve el nombre del dataObject
			String DataObject = ((DataPropertyCondition) element2.getCondition()).getAppliesTo();
			DataObject = DataObject.replaceAll(" ", "");
			DataObject = DataObject.replaceAll("\r\n", "").replaceAll("\n", "");
			String restriction = ((DataPropertyCondition) element2.getCondition()).getRestriction();
			//Eliminamos el simbolo de comparacion 
			restriction = restriction.replace("=", "");
			restriction = restriction.replaceAll(" ", "");
			restriction = restriction.replaceAll("\r\n", "").replaceAll("\n", "");
			if (restriction.isEmpty())
				restriction = "Restriction";
			
System.out.println("kekaName "+nameElementMeasure+"Activity:"+DataObject+"Restricction:"+restriction+ " funcion"+functionAgg);
			ArrayList<Object> dataQueries = converter.converterDataAggregatedMeasureOWL(nameElementMeasure,DataObject,functionAgg,restriction);
		    
		    String nameDataAggregated = (String) dataQueries.get(0);
		    OWLIndividual DataAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameDataInstance = (String) dataQueries.get(2);
		    OWLIndividual DataInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToDataAggregatedMeasure(nameDataAggregated,DataAggregated,nameDataInstance);
		    queries.addQueriesToDataInstanceMeasure(nameDataInstance, DataInstance);
		    
		    getDeclarationMeasureDependOn(nameElementMeasure);
		}	
	}
	
	public void getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(List<AggregatedMeasure> dataAggregatedMeasure, JAXBElement jaxbElement) throws Exception {
		
		Iterator<?> itr = dataAggregatedMeasure.iterator(); 
		while(itr.hasNext()) {
			AggregatedMeasure element = (AggregatedMeasure) itr.next();
			
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionAgg = element.getAggregationFunction();
			DerivedSingleInstanceMeasure element2 = (DerivedSingleInstanceMeasure) element.getBaseMeasure();
			String nameElementMeasure= element2.getName();
			nameElementMeasure = nameElementMeasure.replaceAll(" ", "");
			nameElementMeasure = nameElementMeasure.replaceAll("\r\n", "").replaceAll("\n", "");

			String nameMeasureA = ""; 
			String nameMeasureB = "";
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionDeriv = element2.getFunction();
			
			Map<String, MeasureDefinition> mapaMedidas = element2.getUsedMeasureMap();
			Set keys = mapaMedidas.keySet();
			Object akeys[] = keys.toArray();
			String keymedA = (String) akeys[0];
			String keymedB = (String) akeys[1];
			
			MeasureDefinition medidaA = mapaMedidas.get(keymedA);
			List medidasA = new ArrayList();
			medidasA.add(medidaA);
			MeasureDefinition medidaB = mapaMedidas.get(keymedB);
			List medidasB = new ArrayList();
			medidasB.add(medidaB);
			
			//MedidasA
			if(medidaA instanceof TimeInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameTimeInstanceMeasure(medidaA);
				this.getDeclarationIndividualsTimeInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof CountInstanceMeasure){	
				nameMeasureA = UtilsObjectOWL.getNameCountInstanceMeasure(medidaA);
				this.getDeclarationIndividualsCountInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof StateConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameStateConditionInstanceMeasure(medidaA);
				this.getDeclarationIndividualsStateConditionInstanceMeasure(medidasA, jaxbElement);
			
			}else if(medidaA instanceof DataInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameDataInstanceMeasure(medidaA);
				this.getDeclarationIndividualsDataInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getDataPropertyConditionInstanceMeasure(medidaA);
				this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(medidasA, jaxbElement);
			}
			//MedidasB
			if(medidaB instanceof TimeInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameTimeInstanceMeasure(medidaB);
				this.getDeclarationIndividualsTimeInstanceMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof CountInstanceMeasure){	
				nameMeasureB = UtilsObjectOWL.getNameCountInstanceMeasure(medidaB);
				this.getDeclarationIndividualsCountInstanceMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof StateConditionInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameStateConditionInstanceMeasure(medidaB);
				this.getDeclarationIndividualsStateConditionInstanceMeasure(medidasB, jaxbElement);
			
			}else if(medidaB instanceof DataInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameDataInstanceMeasure(medidaB);
				this.getDeclarationIndividualsDataInstanceMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getDataPropertyConditionInstanceMeasure(medidaB);
				this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(medidasB, jaxbElement);
			}
			
			ArrayList<Object> dataQueries = converter.converterDerivedSingleInstanceAggregatedMeasureOWL(nameElementMeasure, functionDeriv, nameMeasureA, nameMeasureB, functionAgg);
		    
		    getDeclarationMeasureDependOn(nameMeasureA);
		    getDeclarationMeasureDependOn(nameMeasureB);
		    
		    String nameDataAggregated = (String) dataQueries.get(0);
		    OWLIndividual DataAggregated = (OWLIndividual) dataQueries.get(1);
		    String nameDataInstance = (String) dataQueries.get(2);
		    OWLIndividual DataInstance = (OWLIndividual) dataQueries.get(3);
		    queries.addQueriesToDataAggregatedMeasure(nameDataAggregated,DataAggregated,nameDataInstance);
		    queries.addQueriesToDataInstanceMeasure(nameDataInstance, DataInstance);
		    
		    getDeclarationMeasureDependOn(nameElementMeasure);
		}	
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida DerivedMultiInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDerivedMultiInstanceMeasure(List<DerivedMultiInstanceMeasure> derivedProcessMeasure, JAXBElement jaxbElement) throws Exception {
		//Para las derivadas voy a procesar las medidas aggregated que contiene por separado utilizando 
		//las funciones definidas anteriormente
		
		Iterator<?> itr = derivedProcessMeasure.iterator(); 
		while(itr.hasNext()) {
			String nameMeasureA = ""; 
			String nameMeasureB = "";
			DerivedMultiInstanceMeasure element = (DerivedMultiInstanceMeasure) itr.next();
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionDeriv = element.getFunction();
			
			Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
			Set keys = mapaMedidas.keySet();
			Object akeys[] = keys.toArray();
			String keymedA = (String) akeys[0];
			String keymedB = (String) akeys[1];
			
			MeasureDefinition medidaA = mapaMedidas.get(keymedA);
			List medidasA = new ArrayList();
			medidasA.add(medidaA);
			MeasureDefinition medidaB = mapaMedidas.get(keymedB);
			List medidasB = new ArrayList();
			medidasB.add(medidaB);
			
			//Llamados nameMedidaA+Intermediate1
			if(medidaA instanceof AggregatedMeasure && ((AggregatedMeasure) medidaA).getBaseMeasure() instanceof TimeInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameTimeAggregatedMeasure(medidaA);
				this.getDeclarationIndividualsTimeAggregatedMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaA).getBaseMeasure() instanceof CountInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameCountAggregatedMeasure(medidaA);
				this.getDeclarationIndividualsCountAggregatedMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaA).getBaseMeasure() instanceof StateConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameStateConditionAggregatedMeasure(medidaA);
				this.getDeclarationIndividualsStateConditionAggregatedMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaA).getBaseMeasure() instanceof DataInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameDataAggregatedMeasure(medidaA);
				this.getDeclarationIndividualsDataAggregatedMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaA).getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameDataPropertyConditionAggregatedMeasure(medidaA);
				this.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(medidasA, jaxbElement);
			}
			//Llamados nameMedidaB+Intermediate1
			if(medidaB instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaB).getBaseMeasure() instanceof TimeInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameTimeAggregatedMeasure(medidaB);
				this.getDeclarationIndividualsTimeAggregatedMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaB).getBaseMeasure() instanceof CountInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameCountAggregatedMeasure(medidaB);
				this.getDeclarationIndividualsCountAggregatedMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaB).getBaseMeasure() instanceof StateConditionInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameStateConditionAggregatedMeasure(medidaB);
				this.getDeclarationIndividualsStateConditionAggregatedMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaB).getBaseMeasure() instanceof DataInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameDataAggregatedMeasure(medidaB);
				this.getDeclarationIndividualsDataAggregatedMeasure(medidasB, jaxbElement);
				
			}else if(medidaB instanceof  AggregatedMeasure && ((AggregatedMeasure) medidaB).getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure){
				nameMeasureB = UtilsObjectOWL.getNameDataPropertyConditionAggregatedMeasure(medidaB);
				this.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(medidasB, jaxbElement);
			}
			
			//String nameElementMeasure= element.getName();
			String nameDerivedMultiInstance = element.getName()+"Intermediate1";
			nameMeasureA = nameMeasureA + "Intermediate1";
			nameMeasureB = nameMeasureB + "Intermediate1";
			System.out.println("NombreDerivada "+nameDerivedMultiInstance+" Funcion:"+functionDeriv+"Name medA:"+nameMeasureA+ " Name medB:"+nameMeasureB);
		    converter.converterDerivedMultiInstanceMeasureOWL(nameDerivedMultiInstance,functionDeriv,nameMeasureA,nameMeasureB);
		    queries.addQueriesToDerivedMeasure(nameDerivedMultiInstance, nameMeasureA,nameMeasureB);
		    
		    getDeclarationMeasureDependOn(nameMeasureA);
		    getDeclarationMeasureDependOn(nameMeasureB);
		}
	}

//***********************************************************************************/
//***********************************************************************************/
//***********************************************************************************/
	
	/***Declaraciones en owl de la medida DataInstanceMeasure de PPINOT ***/
	public void getDeclarationIndividualsDerivedSingleInstanceMeasure(List<DerivedSingleInstanceMeasure> derivedInstanceMeasure, JAXBElement jaxbElement) throws Exception {
		//Para las derivadas voy a procesar las medidas aggregated que contiene por separado utilizando 
		//las funciones definidas anteriormente
		
		Iterator<?> itr = derivedInstanceMeasure.iterator(); 
		while(itr.hasNext()) {
			String nameMeasureA = ""; 
			String nameMeasureB = "";
			DerivedSingleInstanceMeasure element = (DerivedSingleInstanceMeasure) itr.next();
			//Por cada medida tengo que ir convirtiendo a su declaración de instanciación en OWL
			String functionDeriv = element.getFunction();
			
			Map<String, MeasureDefinition> mapaMedidas = element.getUsedMeasureMap();
			Set keys = mapaMedidas.keySet();
			Object akeys[] = keys.toArray();
			String keymedA = (String) akeys[0];
			String keymedB = "";
			if (akeys.length>1)
				keymedB = (String) akeys[1];
			
			MeasureDefinition medidaA = mapaMedidas.get(keymedA);
			List medidasA = new ArrayList();
			medidasA.add(medidaA);
			MeasureDefinition medidaB = null;
			List medidasB = null;
			if (akeys.length>1) {
				medidaB = mapaMedidas.get(keymedB);
				medidasB = new ArrayList();
				medidasB.add(medidaB);
			}
			
			//MedidasA
			if(medidaA instanceof TimeInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameTimeInstanceMeasure(medidaA);
				this.getDeclarationIndividualsTimeInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof CountInstanceMeasure){	
				nameMeasureA = UtilsObjectOWL.getNameCountInstanceMeasure(medidaA);
				this.getDeclarationIndividualsCountInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof StateConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameStateConditionInstanceMeasure(medidaA);
				this.getDeclarationIndividualsStateConditionInstanceMeasure(medidasA, jaxbElement);
			
			}else if(medidaA instanceof DataInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getNameDataInstanceMeasure(medidaA);
				this.getDeclarationIndividualsDataInstanceMeasure(medidasA, jaxbElement);
				
			}else if(medidaA instanceof DataPropertyConditionInstanceMeasure){
				nameMeasureA = UtilsObjectOWL.getDataPropertyConditionInstanceMeasure(medidaA);
				this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(medidasA, jaxbElement);
			}
			//MedidasB
			if (medidaB!=null) {
				if(medidaB instanceof TimeInstanceMeasure){
					nameMeasureB = UtilsObjectOWL.getNameTimeInstanceMeasure(medidaB);
					this.getDeclarationIndividualsTimeInstanceMeasure(medidasB, jaxbElement);
					
				}else if(medidaB instanceof CountInstanceMeasure){	
					nameMeasureB = UtilsObjectOWL.getNameCountInstanceMeasure(medidaB);
					this.getDeclarationIndividualsCountInstanceMeasure(medidasB, jaxbElement);
					
				}else if(medidaB instanceof StateConditionInstanceMeasure){
					nameMeasureB = UtilsObjectOWL.getNameStateConditionInstanceMeasure(medidaB);
					this.getDeclarationIndividualsStateConditionInstanceMeasure(medidasB, jaxbElement);
				
				}else if(medidaB instanceof DataInstanceMeasure){
					nameMeasureA = UtilsObjectOWL.getNameDataInstanceMeasure(medidaB);
					this.getDeclarationIndividualsDataInstanceMeasure(medidasB, jaxbElement);
					
				}else if(medidaB instanceof DataPropertyConditionInstanceMeasure){
					nameMeasureB = UtilsObjectOWL.getDataPropertyConditionInstanceMeasure(medidaB);
					this.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(medidasB, jaxbElement);
				}
			}
			//String nameDerivedSingleInstance = nameMeasureA+nameMeasureB+"DerivedSingleInstance";
			String nameDerivedSingleInstance = element.getName();
			System.out.println("NombreDerivadaInstance "+nameDerivedSingleInstance+" Funcion:"+functionDeriv+"Name medA:"+nameMeasureA+ " Name medB:"+nameMeasureB);
		    converter.converterDerivedSingleInstanceMeasureOWL(nameDerivedSingleInstance,functionDeriv,nameMeasureA,nameMeasureB);
		    queries.addQueriesToDerivedMeasure(nameDerivedSingleInstance, nameMeasureA,nameMeasureB);
		    
		    getDeclarationMeasureDependOn(nameMeasureA);
		    getDeclarationMeasureDependOn(nameMeasureB);
		}
	}
	
	/**Esta funcion ha sido añadida para crear las queries para indicar la dependencia directa
	e indirecta de una medida con las demas.**/
	public void getDeclarationMeasureDependOn(String nameMeasure) throws Exception {
		queries.addQueriesToDirectlyDependOnMeasure(nameMeasure);
		queries.addQueriesToIndirectlyDependOnMeasure(nameMeasure);
	}
	
	
	public void getSaveOWL() {
		
		try {
			bpmnMan.saveOntology(bpmnOnt,IRI.create(bpmnFile.toURI()));
			ppinotMan.saveOntology(ppinotOnt,IRI.create(ppinotFile.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
}
