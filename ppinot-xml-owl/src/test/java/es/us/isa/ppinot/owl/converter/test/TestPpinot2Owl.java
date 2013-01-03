package es.us.isa.ppinot.owl.converter.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.owl.converter.InObjectsOutOWLPpinot;
import es.us.isa.ppinot.xmlExtracter.PpiNotXmlExtracter;

public class TestPpinot2Owl {

	private String orgbpmn;
	
	private String caminoOrigen;
	private String caminoDestino;
	private String nomFichOrigen;
	private String bpmnFilename;
	private String ppinotFilename;
	private String orgppinot;
	
	private Boolean bpmn2Owl() {
		
		Bpmn20XmlExtracter bpmn20XmlExtracter;
		PpiNotXmlExtracter ppiNotXmlExtracter;
		try {
			
			// BPMN
			bpmn20XmlExtracter = new Bpmn20XmlExtracter();
			bpmn20XmlExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			
			bpmn20XmlExtracter.generateModelLists();
			List <TTask> taskList = bpmn20XmlExtracter.getTaskList();
			List <TDataObject> dataObjectList = bpmn20XmlExtracter.getDataObjectList();
			List <TSequenceFlow> sequenceFlowList = bpmn20XmlExtracter.getSequenceFlowList();
			List <TSubProcess> subProcessList = bpmn20XmlExtracter.getSubProcessList();
			List<TExclusiveGateway> exclusiveGateways = bpmn20XmlExtracter.getExclusiveGatewayList();
			List<TGateway> generalGwtList = bpmn20XmlExtracter.getGatewayList();
			List <TStartEvent> startEventList = bpmn20XmlExtracter.getStartEventList();
			List<TEndEvent> endEventList = bpmn20XmlExtracter.getEndEventList();
			
			// convierte a owl
			OWLOntologyManager bpmnMan = OWLManager.createOWLOntologyManager();
			BPMN2OWLConverter bpmnInout = new BPMN2OWLConverter(caminoDestino, bpmnFilename, orgbpmn, bpmnMan, sequenceFlowList);
			bpmnInout.getDeclarationIndividualsTask(taskList, dataObjectList);
			bpmnInout.getDeclarationIndividualsSubProcess(subProcessList, dataObjectList);
			bpmnInout.getDeclarationIndividualsStartEvent(startEventList);
			bpmnInout.getDeclarationIndividualsEndEvent(endEventList);
			bpmnInout.getDeclarationIndividualsXorGateways(exclusiveGateways);
			bpmnInout.getDeclarationIndividualsGateways(generalGwtList);
			bpmnInout.getDeclarationIndividualsDataObject(dataObjectList);

			// guarda la ontologia generada
			bpmnInout.getSaveOWL();
			
			// PPINOT
			ppiNotXmlExtracter = new PpiNotXmlExtracter();
			ppiNotXmlExtracter.unmarshall(caminoOrigen, nomFichOrigen);

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
			
			// convierte a owl
			OWLOntologyManager ppinotMan = OWLManager.createOWLOntologyManager();
			InObjectsOutOWLPpinot ppinotInout = new InObjectsOutOWLPpinot(caminoDestino, ppinotFilename, orgppinot, orgbpmn, bpmnInout.getOntologyURI(), ppinotMan);

			ppinotInout.getDeclarationIndividualsCountInstanceMeasure(countInstanceMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsTimeInstanceMeasure(timeInstanceMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsStateConditionInstanceMeasure(stateConditionInstanceMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(dataConditionInstanceMeasure);
			ppinotInout.getDeclarationIndividualsDataInstanceMeasure(dataInstanceMeasure);
			
			ppinotInout.getDeclarationIndividualsCountAggregatedMeasure(countAggregatedMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsTimeAggregatedMeasure(timeAggregatedMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsStateConditionAggregatedMeasure(stateConditionAggregatedMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(dataConditionAggregatedMeasure);
			ppinotInout.getDeclarationIndividualsDataAggregatedMeasure(dataAggregatedMeasure);
			ppinotInout.getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(derivedSingleInstanceAggregatedMeasure, bpmn20XmlExtracter);

			ppinotInout.getDeclarationIndividualsDerivedMultiInstanceMeasure(derivedProcessMeasure, bpmn20XmlExtracter);
			ppinotInout.getDeclarationIndividualsDerivedSingleInstanceMeasure(derivedInstanceMeasure, bpmn20XmlExtracter);
			
			// guarda la ontologia generada
			ppinotInout.getSaveOWL();

			return true;
		} catch (JAXBException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return false;
	}

	@Test
	public void testBase() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/bpmn.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";

		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba base.bpmn20.xml";
		bpmnFilename = "ExpressionsOWLBpmn base.owl";
		ppinotFilename = "ExpressionsOWLPpinot base.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregated() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/bpmn.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated.bpmn20.xml";
		bpmnFilename = "ExpressionsOWLBpmn aggregated.owl";
		ppinotFilename = "ExpressionsOWLPpinot aggregated.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testDerived() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/bpmn.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba derived.bpmn20.xml";
		bpmnFilename = "ExpressionsOWLBpmn derived.owl";
		ppinotFilename = "ExpressionsOWLPpinot derived.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregatedConnector() {
		
		orgbpmn = "http://www.isa.us.es/ontologies/bpmn.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		bpmnFilename = "ExpressionsOWLBpmn aggregated connector.owl";
		ppinotFilename = "ExpressionsOWLPpinot aggregated connector.owl";
		
		assertTrue(bpmn2Owl());
	}

}
