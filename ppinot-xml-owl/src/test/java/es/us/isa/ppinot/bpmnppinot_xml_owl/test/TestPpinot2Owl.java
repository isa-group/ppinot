package es.us.isa.ppinot.bpmnppinot_xml_owl.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;
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
import es.us.isa.ppinot.owl.converter.InObjectsOutOWLPpinot;
import es.us.isa.ppinot.bpmnppinot_xml_owl.xmlExtracter.ppinotBpmn20.Bpmn20XmlExtracter;

public class TestPpinot2Owl {

	private String orgbpmn;
	
	private String caminoOrigen;
	private String caminoDestino;
	private String nomFichOrigen;
	private String bpmnFilename;
	private String ppinotFilename;
	private String orgppinot;
	
	private Boolean bpmn2Owl() {
		
		PpiNotXmlExtracter ppiNotXmlExtracter;
		try {
			
			ppiNotXmlExtracter = new PpiNotXmlExtracter();
			
			// importa el xml
			ppiNotXmlExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			
			// BPMN
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
			OWLOntologyManager bpmnMan = OWLManager.createOWLOntologyManager();
			InObjectsOutOWLBpmn bpmnInout = new InObjectsOutOWLBpmn(caminoDestino, bpmnFilename, orgbpmn, bpmnMan, sequenceFlowList);
			bpmnInout.getDeclarationIndividualsTask(taskList);
			bpmnInout.getDeclarationIndividualsSubProcess(subProcessList);
			bpmnInout.getDeclarationIndividualsStartEvent(startEventList);
			bpmnInout.getDeclarationIndividualsEndEvent(endEventList);
			bpmnInout.getDeclarationIndividualsXorGateways(exclusiveGateways);
			bpmnInout.getDeclarationIndividualsGateways(generalGwtList);
			bpmnInout.getDeclarationIndividualsDataObject(dataObjectList);

			// guarda la ontologia generada
			bpmnInout.getSaveOWL();
			
			// PPINOT
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
			InObjectsOutOWLPpinot ppinotInout = new InObjectsOutOWLPpinot(caminoDestino, ppinotFilename, orgppinot, orgbpmn, bpmnInout.getOrgbpmnExpr(), ppinotMan);

			ppinotInout.getDeclarationIndividualsCountInstanceMeasure(countInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsTimeInstanceMeasure(timeInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsStateConditionInstanceMeasure(stateConditionInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDataPropertyConditionInstanceMeasure(dataConditionInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDataInstanceMeasure(dataInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			
			ppinotInout.getDeclarationIndividualsCountAggregatedMeasure(countAggregatedMeasure,ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsTimeAggregatedMeasure(timeAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsStateConditionAggregatedMeasure(stateConditionAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDataPropertyConditionAggregatedMeasure(dataConditionAggregatedMeasure,  ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDataAggregatedMeasure(dataAggregatedMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDerivedSingleInstanceAggregatedMeasure(derivedSingleInstanceAggregatedMeasure, ppiNotXmlExtracter.getImportElement());

			ppinotInout.getDeclarationIndividualsDerivedMultiInstanceMeasure(derivedProcessMeasure, ppiNotXmlExtracter.getImportElement());
			ppinotInout.getDeclarationIndividualsDerivedSingleInstanceMeasure(derivedInstanceMeasure, ppiNotXmlExtracter.getImportElement());
			
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
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
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
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
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
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
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
		
		orgbpmn = "http://www.isa.us.es/ontologies/AbstractBP.owl";
		orgppinot = "http://www.isa.us.es/ontologies/ppinot.owl";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		bpmnFilename = "ExpressionsOWLBpmn aggregated connector.owl";
		ppinotFilename = "ExpressionsOWLPpinot aggregated connector.owl";
		
		assertTrue(bpmn2Owl());
	}

}
