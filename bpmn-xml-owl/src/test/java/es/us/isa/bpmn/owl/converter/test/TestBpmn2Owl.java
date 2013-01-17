package es.us.isa.bpmn.owl.converter.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;

public class TestBpmn2Owl {

	private OWLOntology bpmnOntology;
	
	private Boolean bpmn2Owl(String baseIRI, String sourceFile) {
		
		try {
			
			String caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
			String caminoDestino = "D:/tmp-nuevo/";

			// importa el xml
			Bpmn20ModelHandlerInterface modelHandler = new Bpmn20ModelHandler();
			modelHandler.load(caminoOrigen, "prueba "+sourceFile);
//			modelHandler.load( getClass().getResourceAsStream("xml/"+sourceFile) );
			
			// convierte a owl
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			BPMN2OWLConverter converter = new BPMN2OWLConverter(baseIRI, manager);
			bpmnOntology = converter.convertToOwlOntology(modelHandler);
	
			// guarda la ontologia generada
			converter.saveOntology(caminoDestino, "ExpressionsOWLBpmn "+sourceFile.substring(0, sourceFile.indexOf("."))+".owl");
			
			return true;
		} catch (JAXBException e) {

			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Test
	public void testBase() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "base.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2"));
        assertTrue("Task 1 is there", analyser.isTask("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue("Task 2 is there", analyser.isTask("sid-0B0B61E3-89F3-4028-9746-D122688C2E93") );
        assertTrue("End event is there", analyser.isEndEvent("sid-7AA7F58F-7769-4E20-831C-E3051C61D97C"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-7AA7F58F-7769-4E20-831C-E3051C61D97C"));
        assertTrue("Data object is there", analyser.isDataObject("sid-9832DD51-05CF-4B45-BB61-C46740621C4F"));
        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-9832DD51-05CF-4B45-BB61-C46740621C4F", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
	}

	@Test
	public void testAggregated() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "aggregated.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);

        assertTrue("Start event is there", analyser.isStartEvent("sid-77FA586A-AD73-421A-B2BC-E29925E21D2B"));
        assertTrue("Task 1 is there", analyser.isTask("sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue("Task 2 is there", analyser.isTask("sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD") );
        assertTrue("End event is there", analyser.isEndEvent("sid-1D03CA3E-434B-4D11-815A-B541B4451FFF"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-77FA586A-AD73-421A-B2BC-E29925E21D2B", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-D5274D42-55B0-46A0-8EB6-B99B186D3873", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD", "sid-1D03CA3E-434B-4D11-815A-B541B4451FFF"));
        assertTrue("Data object is there", analyser.isDataObject("sid-E55E3010-5D7F-4D21-B968-D4D109D625CA"));
        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-E55E3010-5D7F-4D21-B968-D4D109D625CA", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

	}
	
	@Test
	public void testDerived() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "derived.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-E7DD4D31-3A0D-45FF-8187-52A3265DCD3A"));
        assertTrue("Task 1 is there", analyser.isTask("sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));
        assertTrue("Task 2 is there", analyser.isTask("sid-CC51D716-391D-4FC4-8196-3A0078B8344A") );
        assertTrue("End event is there", analyser.isEndEvent("sid-430DC7A0-6819-4AD7-AAB4-8227A9DCA6FD"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-E7DD4D31-3A0D-45FF-8187-52A3265DCD3A", "sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-CC51D716-391D-4FC4-8196-3A0078B8344A", "sid-430DC7A0-6819-4AD7-AAB4-8227A9DCA6FD"));
        assertTrue("Data object is there", analyser.isDataObject("sid-8B684F86-01DC-4296-AC51-86D38637873C"));
        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-8B684F86-01DC-4296-AC51-86D38637873C", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
	}

	@Test
	public void testAggregatedConnector() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "aggregated connector.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-0B3CFFD6-68F2-4E7D-BDCA-769B546204F2"));
        assertTrue("Task 1 is there", analyser.isTask("sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));
        assertTrue("Task 2 is there", analyser.isTask("sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A") );
        assertTrue("End event is there", analyser.isEndEvent("sid-E19E267A-C3A9-48DB-AA17-64353BEB7875"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-0B3CFFD6-68F2-4E7D-BDCA-769B546204F2", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6", "sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A", "sid-E19E267A-C3A9-48DB-AA17-64353BEB7875"));

	}

}
