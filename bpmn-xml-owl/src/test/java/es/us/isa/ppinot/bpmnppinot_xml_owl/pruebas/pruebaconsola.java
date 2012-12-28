package es.us.isa.ppinot.bpmnppinot_xml_owl.pruebas;

import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.ppinot.bpmnppinot_xml_owl.OntologyOwl.InObjectsOutOWLBpmn;
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

		String orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		
		String caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		String caminoDestino = "D:/tmp-nuevo/";
		
//		String nomFichOrigen = "prueba base.bpmn20.xml";
//		String bpmnOwlFilename = "ExpressionsOWLBpmn base.owl";

//		String nomFichOrigen = "prueba aggregated.bpmn20.xml";
//		String bpmnOwlFilename = "ExpressionsOWLBpmn aggregated.owl";

//		String nomFichOrigen = "prueba derived.bpmn20.xml";
//		String bpmnOwlFilename = "ExpressionsOWLBpmn derived.owl";

		String nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		String bpmnOwlFilename = "ExpressionsOWLBpmn aggregated connector.owl";

		
		OWLOntologyManager bpmnMan = OWLManager.createOWLOntologyManager();
		
		PpiNotXmlExtracter ppiNotXmlExtracter = new PpiNotXmlExtracter();
		
		// importa el xml
		ppiNotXmlExtracter.unmarshall(caminoOrigen, nomFichOrigen);
		Bpmn20XmlExtracter.getBpmn20elements(ppiNotXmlExtracter.getImportElement());
		List <TTask> taskList = Bpmn20XmlExtracter.getTaskList();
		List <TDataObject> dataObjectList = Bpmn20XmlExtracter.getDataObjectList();
		List <TSequenceFlow> sequenceFlowList = Bpmn20XmlExtracter.getSequenceFlowList();
		List <TSubProcess> subProcessList = Bpmn20XmlExtracter.getSubProcessList();
		List<TExclusiveGateway> exclusiveGateways = Bpmn20XmlExtracter.getExclusiveGatewayList();
		List<TGateway> generalGwtList = Bpmn20XmlExtracter.getGatewayList();
		List <TStartEvent> startEventList = Bpmn20XmlExtracter.getStartEventList();
		List<TEndEvent> endEventList = Bpmn20XmlExtracter.getEndEventList();
		
		// convierte a owl
		InObjectsOutOWLBpmn inout = new InObjectsOutOWLBpmn(caminoDestino, bpmnOwlFilename, orgbpmn, bpmnMan, sequenceFlowList);
		inout.getDeclarationIndividualsTask(taskList);
		inout.getDeclarationIndividualsSubProcess(subProcessList);
		inout.getDeclarationIndividualsStartEvent(startEventList);
		inout.getDeclarationIndividualsEndEvent(endEventList);
		inout.getDeclarationIndividualsXorGateways(exclusiveGateways);
		inout.getDeclarationIndividualsGateways(generalGwtList);
		inout.getDeclarationIndividualsDataObject(dataObjectList);

		// guarda la ontologia generada
		inout.getSaveOWL();
	}
}
