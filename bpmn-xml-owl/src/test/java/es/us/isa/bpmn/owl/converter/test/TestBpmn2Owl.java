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

/**
 * Clase con los casos de prueba del proyecto
 * 
 * @author Edelia
 *
 */
public class TestBpmn2Owl {

	// ontología creada
	private OWLOntology bpmnOntology;
	
	/**
	 * Genera la ontología OWL con la URI especificada a partir de un xml
	 * 
	 * @param baseIRI URI de la ontología creada
	 * @param sourceFile XML a partir del cual se crea la ontología
	 * @return
	 */
	private Boolean bpmn2Owl(String baseIRI, String sourceFile) {
		
		try {
			
			String caminoOrigen = "D:/eclipse-appweb-indigo/ppinot-repository/bpmn-xml-owl/target/test-classes/xml/";
			String caminoDestino = "D:/eclipse-appweb-indigo/ppinot-repository/bpmn-xml-owl/target/test-classes/owl/";

			// importa el xml
			Bpmn20ModelHandlerInterface modelHandler = new Bpmn20ModelHandler();
			modelHandler.load(caminoOrigen, sourceFile);
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
			
			e.printStackTrace();
		}
		
		return false;
	}

	@Test
	public void testBase() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "base.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2"));
        assertTrue("Task 1 is there", analyser.isActivity("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-0B0B61E3-89F3-4028-9746-D122688C2E93") );
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
        assertTrue("Task 1 is there", analyser.isActivity("sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD") );
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
        assertTrue("Task 1 is there", analyser.isActivity("sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-CC51D716-391D-4FC4-8196-3A0078B8344A") );
        assertTrue("End event is there", analyser.isEndEvent("sid-430DC7A0-6819-4AD7-AAB4-8227A9DCA6FD"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-E7DD4D31-3A0D-45FF-8187-52A3265DCD3A", "sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-CC51D716-391D-4FC4-8196-3A0078B8344A", "sid-430DC7A0-6819-4AD7-AAB4-8227A9DCA6FD"));
        assertTrue("Data object is there", analyser.isDataObject("sid-8B684F86-01DC-4296-AC51-86D38637873C"));
        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-8B684F86-01DC-4296-AC51-86D38637873C", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
	}

	@Test
	public void testAggregatedConnector() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "aggregated-connector.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-0B3CFFD6-68F2-4E7D-BDCA-769B546204F2"));
        assertTrue("Task 1 is there", analyser.isActivity("sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A") );
        assertTrue("End event is there", analyser.isEndEvent("sid-E19E267A-C3A9-48DB-AA17-64353BEB7875"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-0B3CFFD6-68F2-4E7D-BDCA-769B546204F2", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6", "sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-93E91437-3D1C-4BFA-AB2F-03CD15543C0A", "sid-E19E267A-C3A9-48DB-AA17-64353BEB7875"));

	}

	@Test
	public void testIsgroupedbyConnector() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "isgroupedby-connector.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-872B1F52-741D-4E46-A10F-90BB305157DE"));
        assertTrue("Task 1 is there", analyser.isActivity("sid-DB049C83-0C6F-4643-993D-9087B1C0D0CB"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E") );
        assertTrue("End event is there", analyser.isEndEvent("sid-CAD45C6B-7093-4FAA-A303-B76B21D335C4"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-872B1F52-741D-4E46-A10F-90BB305157DE", "sid-DB049C83-0C6F-4643-993D-9087B1C0D0CB"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-DB049C83-0C6F-4643-993D-9087B1C0D0CB", "sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E", "sid-CAD45C6B-7093-4FAA-A303-B76B21D335C4"));
        assertTrue("Data object is there", analyser.isDataObject("sid-58AEF45E-1C11-4923-9071-290DB1DD0F0D"));
        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-58AEF45E-1C11-4923-9071-290DB1DD0F0D", "sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E"));
	}

	@Test
	public void testLanes() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "lanes.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-5874CAE4-A5D5-42E9-9E3C-FF970E86587F"));
        assertTrue("Task 1 is there", analyser.isActivity("sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-95F066C0-7680-421B-A18D-2FFDF98D44CD") );
        assertTrue("Task 3 is there", analyser.isActivity("sid-E7776982-9DCD-48D1-BF8E-BEC23728B124") );
        assertTrue("End event is there", analyser.isEndEvent("sid-02D283F8-3695-438E-A507-CB8FD28581BC"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-5874CAE4-A5D5-42E9-9E3C-FF970E86587F", "sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC", "sid-95F066C0-7680-421B-A18D-2FFDF98D44CD"));
        assertTrue("Task 2 precedes Task 3", analyser.isDirectlyPreceding("sid-95F066C0-7680-421B-A18D-2FFDF98D44CD", "sid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));
        assertTrue("Task 3 precedes End Event", analyser.isDirectlyPreceding("sid-E7776982-9DCD-48D1-BF8E-BEC23728B124", "sid-02D283F8-3695-438E-A507-CB8FD28581BC"));
	}

	@Test
	public void testPpi() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "ppi.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-9A9977D6-5D29-4FE5-B999-BBA1DF17B4C1"));
        assertTrue("Task 1 is there", analyser.isActivity("sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue("Task 2 is there", analyser.isActivity("sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B") );
        assertTrue("End event is there", analyser.isEndEvent("sid-7E04B962-CCB7-400E-A820-8A065602111F"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-9A9977D6-5D29-4FE5-B999-BBA1DF17B4C1", "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-680678C2-CDEE-4852-85E7-CAA68E08DF78", "sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B", "sid-7E04B962-CCB7-400E-A820-8A065602111F"));
	}

	@Test
	public void testMany() {
		
		assertTrue(bpmn2Owl("http://www.isa.us.es/ontologies/bpmn/", "RFC-simplified.bpmn20.xml"));

        BpmnTestAnalyser analyser = new BpmnTestAnalyser(bpmnOntology);
        assertTrue("Start event is there", analyser.isStartEvent("sid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("Activity Analyse RFC is there", analyser.isActivity("sid-202DF64D-1397-4599-9D60-3B42B7224F48"));
        assertTrue("Activity Approve RFC is there", analyser.isActivity("sid-B48C4D43-F606-4F50-A925-4CED784356C2") );
        assertTrue("Activity Cancel RFC is there", analyser.isActivity("sid-623CF5C2-B74D-4A14-B383-B24AEE08D474") );
        assertTrue("Activity Elevate decision to committee is there", analyser.isActivity("sid-686E2694-70C4-4E43-A2B3-9770C470454D") );
        assertTrue("Activity Analyse in committee is there", analyser.isActivity("sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8") );
        assertTrue("Merge XOR gateway there", analyser.isXorGateway("sid-6C688D02-D89F-4877-9EFF-F1BD555598D6") );
        assertTrue("DecisionBasedAnalysis XOR gateway there", analyser.isXorGateway("sid-044342F7-8040-463C-B1F8-E75906B55945") );
        assertTrue("End event Report RFC cancelled is there", analyser.isEndEvent("sid-FACB9322-D0A3-4BDF-A9AB-5F9FB4DC940A"));
        assertTrue("End event Report RFC approved is there", analyser.isEndEvent("sid-FCD39DEF-F581-4553-AD79-770A075A2992"));
        assertTrue("Dataobject RFC registered is there", analyser.isDataObject("sid-C59544E2-20D8-42E5-AB67-A1471BAC4D24"));
        assertTrue("Dataobject RFC approved is there", analyser.isDataObject("sid-BF1BF36F-651C-4F36-806B-2A763E8B51A7"));
        assertTrue("Dataobject RFC cancelled is there", analyser.isDataObject("sid-4C915DC6-3144-4441-AC2D-80AE41435C1E"));
        assertTrue("Start event precedes Merge XOR gateway", analyser.isDirectlyPreceding("sid-2DB95E04-AB48-43FE-A67B-B0169885AB4F", "sid-6C688D02-D89F-4877-9EFF-F1BD555598D6"));
        assertTrue("Merge XOR gateway precedes Activity Analyse RFC", analyser.isDirectlyPreceding("sid-6C688D02-D89F-4877-9EFF-F1BD555598D6", "sid-202DF64D-1397-4599-9D60-3B42B7224F48"));
        assertTrue("Activity Analyse RFC precedes DecisionBasedAnalysis XOR gateway", analyser.isDirectlyPreceding("sid-202DF64D-1397-4599-9D60-3B42B7224F48", "sid-044342F7-8040-463C-B1F8-E75906B55945"));
        assertTrue("DecisionBasedAnalysis XOR gateway precedes Activity Approve RFC", analyser.isDirectlyPreceding("sid-044342F7-8040-463C-B1F8-E75906B55945", "sid-B48C4D43-F606-4F50-A925-4CED784356C2"));
        assertTrue("DecisionBasedAnalysis XOR gateway precedes Activity Cancel RFC", analyser.isDirectlyPreceding("sid-044342F7-8040-463C-B1F8-E75906B55945", "sid-623CF5C2-B74D-4A14-B383-B24AEE08D474"));
        assertTrue("DecisionBasedAnalysis XOR gateway precedes Activity Elevate decision to committee", analyser.isDirectlyPreceding("sid-044342F7-8040-463C-B1F8-E75906B55945", "sid-686E2694-70C4-4E43-A2B3-9770C470454D"));
        assertTrue("Activity Elevate decision to committee precedes Activity Analyse in committee", analyser.isDirectlyPreceding("sid-686E2694-70C4-4E43-A2B3-9770C470454D", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("Activity Analyse in committee precedes Merge XOR gateway", analyser.isDirectlyPreceding("sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8", "sid-6C688D02-D89F-4877-9EFF-F1BD555598D6"));
        assertTrue("Activity Cancel RFC precedes end event Report RFC cancelled", analyser.isDirectlyPreceding("sid-623CF5C2-B74D-4A14-B383-B24AEE08D474", "sid-FACB9322-D0A3-4BDF-A9AB-5F9FB4DC940A"));
        assertTrue("Activity Approve RFC precedes end event Report RFC approved", analyser.isDirectlyPreceding("sid-B48C4D43-F606-4F50-A925-4CED784356C2", "sid-FCD39DEF-F581-4553-AD79-770A075A2992"));
        assertTrue("Data object RFC approved is output of  Activity Approve RFC", analyser.isDataOutputOf("sid-BF1BF36F-651C-4F36-806B-2A763E8B51A7", "sid-B48C4D43-F606-4F50-A925-4CED784356C2"));
        assertTrue("Data object RFC cancelled is output of  Activity Cancel RFC", analyser.isDataOutputOf("sid-4C915DC6-3144-4441-AC2D-80AE41435C1E", "sid-623CF5C2-B74D-4A14-B383-B24AEE08D474"));
        
        assertTrue("Process RFC Management is there", analyser.isProcess("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287"));
        assertTrue("Process RFC Management includes Start event", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("Process RFC Management includes Activity Analyse RFC", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-202DF64D-1397-4599-9D60-3B42B7224F48"));
        assertTrue("Process RFC Management includes Activity Approve RFC", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-B48C4D43-F606-4F50-A925-4CED784356C2"));
        assertTrue("Process RFC Management includes Activity Cancel RFC", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-623CF5C2-B74D-4A14-B383-B24AEE08D474"));
        assertTrue("Process RFC Management includes Activity Elevate decision to committee", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-686E2694-70C4-4E43-A2B3-9770C470454D"));
        assertTrue("Process RFC Management includes Activity Analyse in committee", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("Process RFC Management includes Merge XOR gateway", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-6C688D02-D89F-4877-9EFF-F1BD555598D6"));
        assertTrue("Process RFC Management includes DecisionBasedAnalysis XOR gateway", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-044342F7-8040-463C-B1F8-E75906B55945"));
        assertTrue("Process RFC Management includes End event Report RFC cancelled", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-FACB9322-D0A3-4BDF-A9AB-5F9FB4DC940A"));
        assertTrue("Process RFC Management includes End event Report RFC approved", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-FCD39DEF-F581-4553-AD79-770A075A2992"));
        assertTrue("Process RFC Management includes Dataobject RFC registered", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-C59544E2-20D8-42E5-AB67-A1471BAC4D24"));
        assertTrue("Process RFC Management includes Dataobject RFC approved", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-BF1BF36F-651C-4F36-806B-2A763E8B51A7"));
        assertTrue("Process RFC Management includes Dataobject RFC cancelled", analyser.includes("sid-86a3f2d3-5064-4589-87bc-9b18dd0d1287", "sid-4C915DC6-3144-4441-AC2D-80AE41435C1E"));
	}

}
