package es.us.isa.ppinot.bpmnppinot_xml_owl.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TGateway;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;
import es.us.isa.isabpm.ppinot.xmlExtracter.PpiNotXmlExtracter;
import es.us.isa.bpmn.owl.converter.InObjectsOutOWLBpmn;
import es.us.isa.ppinot.bpmnppinot_xml_owl.xmlExtracter.ppinotBpmn20.Bpmn20XmlExtracter;

public class TestBpmn2Owl {

	private String orgbpmn;
	
	private String caminoOrigen;
	private String caminoDestino;
	private String nomFichOrigen;
	private String bpmnOwlFilename;
	
	private Boolean bpmn2Owl() {
		
		OWLOntologyManager bpmnMan = OWLManager.createOWLOntologyManager();
		
		PpiNotXmlExtracter ppiNotXmlExtracter;
		try {
			
			ppiNotXmlExtracter = new PpiNotXmlExtracter();
			
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
			return true;
		} catch (JAXBException e) {

			e.printStackTrace();
		}
		
		return false;
	}

	@Test
	public void testBase() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba base.bpmn20.xml";
		bpmnOwlFilename = "ExpressionsOWLBpmn base.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregated() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated.bpmn20.xml";
		bpmnOwlFilename = "ExpressionsOWLBpmn aggregated.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testDerived() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba derived.bpmn20.xml";
		bpmnOwlFilename = "ExpressionsOWLBpmn derived.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregatedConnector() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		bpmnOwlFilename = "ExpressionsOWLBpmn aggregated connector.owl";
		
		assertTrue(bpmn2Owl());
	}

}
