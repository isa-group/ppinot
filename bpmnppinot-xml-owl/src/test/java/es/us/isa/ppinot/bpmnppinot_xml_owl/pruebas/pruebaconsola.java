package es.us.isa.ppinot.bpmnppinot_xml_owl.pruebas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl.Analyser;
import es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl.UtilsObjectOWL;
import es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl.inObjectsOutOWL;
import es.us.isa.isabpm.ppinot.model.aggregated.*;
import es.us.isa.isabpm.ppinot.model.base.*;
import es.us.isa.isabpm.ppinot.model.derived.*;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;
import es.us.isa.ppinot.bpmnppinot_xml_owl.xmlExtracter.ppinotBpmn20.Bpmn20XmlExtracter;
import es.us.isa.isabpm.ppinot.xmlExtracter.PpiNotXmlExtracter;

public class pruebaconsola {

	public static void main(String[] args) throws Exception {
		
		String camino = "D:/eclipse-appweb-indigo/repository_isa/";
//		String nomFichOrigen = "prueba base.bpmn20.xml";
//		String nomFichOrigen = "prueba aggregated.bpmn20.xml";
//		String nomFichOrigen = "prueba derived.bpmn20.xml";
		String nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		PpiNotXmlExtracter ppiNotXmlExtracter = new PpiNotXmlExtracter();
		
		// importa el xml
		ppiNotXmlExtracter.unmarshall(camino, nomFichOrigen);
		
		//Aqui ya tenemos todos las clases con las instancias y demás en JAXB, ahora
		//toca convertir dichas instancias a instancias de otras clases.
		
		List<TimeInstanceMeasure> timeInstanceMeasure = ppiNotXmlExtracter.getTimeInstanceMeasure();
		List<CountInstanceMeasure> countInstanceMeasure = ppiNotXmlExtracter.getCountInstanceMeasure();
		List<StateConditionInstanceMeasure> stateConditionInstanceMeasure = ppiNotXmlExtracter.getStateConditionInstanceMeasure();
		List<DataInstanceMeasure> dataInstanceMeasure = ppiNotXmlExtracter.getDataInstanceMeasure();
		List<DataPropertyConditionInstanceMeasure> dataConditionInstanceMeasure = ppiNotXmlExtracter.getDataPropertyConditionInstanceMeasure();

		List<AggregatedMeasure> timeAggregatedMeasure = ppiNotXmlExtracter.getTimeAggregatedMeasure();
		List<AggregatedMeasure> countAggregatedMeasure = ppiNotXmlExtracter.getCountAggregatedMeasure();
		List<AggregatedMeasure> stateConditionAggregatedMeasure = ppiNotXmlExtracter.getStateConditionAggregatedMeasure();
		List<AggregatedMeasure> dataAggregatedMeasure = ppiNotXmlExtracter.getDataAggregatedMeasure();
		List<AggregatedMeasure> dataConditionAggregatedMeasure = ppiNotXmlExtracter.getDataPropertyConditionAggregatedMeasure();
		List<AggregatedMeasure> derivedSingleInstanceAggregatedMeasure = ppiNotXmlExtracter.getDerivedSingleInstanceAggregatedMeasure();
		
		List<DerivedSingleInstanceMeasure> derivedInstanceMeasure = ppiNotXmlExtracter.getDerivedSingleInstanceMeasure();
		List<DerivedMultiInstanceMeasure> derivedProcessMeasure = ppiNotXmlExtracter.getDerivedMultiInstanceMeasure();
		
		//ANA BELEN:Bpmn20
		Bpmn20XmlExtracter.getBpmn20elements(ppiNotXmlExtracter.getImportElement());
		List <TTask> taskList = Bpmn20XmlExtracter.getTaskList();
		List <TDataObject> dataObjectList = Bpmn20XmlExtracter.getDataObjectList();
		List <TSequenceFlow> sequenceFlowList = Bpmn20XmlExtracter.getSequenceFlowList();
		List <TSubProcess> subProcessList = Bpmn20XmlExtracter.getSubProcessList();
		List<TExclusiveGateway> exclusiveGateways = Bpmn20XmlExtracter.getExclusiveGatewayList();
		List<TGateway> generalGwtList = Bpmn20XmlExtracter.getGatewayList();
		List <TStartEvent> startEventList = Bpmn20XmlExtracter.getStartEventList();
		List<TEndEvent> endEventList = Bpmn20XmlExtracter.getEndEventList();
		
		inObjectsOutOWL inout = new inObjectsOutOWL("D:/tmp/", sequenceFlowList);
		inout.getDeclarationIndividualsTask(taskList);
		inout.getDeclarationIndividualsSubProcess(subProcessList);
		inout.getDeclarationIndividualsStartEvent(startEventList);
		inout.getDeclarationIndividualsEndEvent(endEventList);
		inout.getDeclarationIndividualsXorGateways(exclusiveGateways);
		inout.getDeclarationIndividualsGateways(generalGwtList);
		inout.getDeclarationIndividualsDataObject(dataObjectList);

		inout.getDeclarationIndividualsCountInstanceMeasure(countInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsTimeInstanceMeasure(timeInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsStateConditionInstanceMeasure(stateConditionInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(dataConditionInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDataInstanceMeasure(dataInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		
		inout.getDeclarationIndividualsCountAggregatedMeasure(countAggregatedMeasure,ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsTimeAggregatedMeasure(timeAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsStateConditionAggregatedMeasure(stateConditionAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(dataConditionAggregatedMeasure,  ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDataAggregatedMeasure(dataAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(derivedSingleInstanceAggregatedMeasure, ppiNotXmlExtracter.getImportElement());

		inout.getDeclarationIndividualsDerivedMultiInstanceMeasure(derivedProcessMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getDeclarationIndividualsDerivedSingleInstanceMeasure(derivedInstanceMeasure, ppiNotXmlExtracter.getImportElement());
		inout.getSaveOWL();
		
		//Como parametros al metodo main entrara el nombre de una medida, y el tipo para el caso 
		//de medidas no derivadas. Para el caso de medidas derivadas debera entrar, el nombre de 
		//medida (que sera null), el tipo, y el nombre medida1, nombre medida2.
		//Supongamos parámetros de entrada
		String nameMeasure = args[0];
		String typeMeasure = args[1];
		String plugin = args[2];

System.out.println("***** Analyser *****");
System.out.println("NameMeasure: "+ nameMeasure+", TypeMeasure: "+typeMeasure+", plugin: "+plugin);
		
		String orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		String orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		String orgppinot_extra = "http://fjtoscano.webfactional.com/isa/ppinot/ppinot-extras.owl";
		String bpmnFileName = "D:/tmp/ExpressionsOWLBpmn.owl";
		String ppinotFileName = "D:/tmp/ExpressionsOWLPpinot.owl";
		Analyser analyser = new Analyser(orgbpmn, orgppinot, orgppinot_extra, bpmnFileName, ppinotFileName);


		//Depends on the type of plugin
		ArrayList<String> individuals = new ArrayList<String>();
		if(plugin.equals("ppinotPluginServlet")){

			String completedNameMeasure = UtilsObjectOWL.getNameImpliedFlowElement(nameMeasure,typeMeasure);
			individuals = analyser.analyze(completedNameMeasure);
			
		}
		else if(plugin.equals("ppinotActivitiesServlet")){
			
			nameMeasure = nameMeasure.replaceAll(" ", "");
			individuals = analyser.analyzeActivityMeasures(nameMeasure);
		}
		else if(plugin.equals("ppinotRelationshipServlet")){

			nameMeasure = nameMeasure.replaceAll(" ", "");
			individuals = analyser.analyzeRelationshipMeasures(nameMeasure);
		}
		
		int tam = individuals.size();
		String[]classesIndividuals = new String[tam];
		int n = 0;
		Iterator<String> it = individuals.iterator();
	    while(it.hasNext()){
	    	String element = it.next();
	    	classesIndividuals[n] = element;
	    	n++;
	    }
	    
		if(classesIndividuals.length > 0){
    		System.out.println(" Individuals who comply with the ppinot query: ");
    		for (int i = 0; i < classesIndividuals.length; i++) {
    			System.out.print(" "+classesIndividuals[i]);
    			if(i != classesIndividuals.length-1){
    				System.out.print(", ");
    			}else{
    				System.out.println(". ");
    			}
    		}
    	}else{
    		
    		System.out.println(" No individuals who comply with the PPINOT query.");
    	}
		System.out.println("Terminado");

	}
}
