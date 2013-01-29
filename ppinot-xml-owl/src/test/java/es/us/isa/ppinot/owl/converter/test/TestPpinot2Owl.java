package es.us.isa.ppinot.owl.converter.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverterInterface;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverter;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverterInterface;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Clase con los casos de prueba del proyecto
 * 
 * @author Edelia
 *
 */
public class TestPpinot2Owl {

	private String bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
	private String ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
	private String bpmnOntologyURI;

    private OWLOntology ppinotOntology;

	/**
	 * Genera la ontología OWL con la URI especificada a partir de un xml
	 * 
	 * @param baseIRI URI de la ontología creada
	 * @param sourceFile XML a partir del cual se crea la ontología
	 * @return
	 */
	private Boolean ppinot2Owl(String sourceFile) {
		
		String caminoOrigen = "D:/eclipse-appweb-indigo/ppinot-repository/ppinot-xml-owl/target/test-classes/xml/";
		String caminoDestino = "D:/eclipse-appweb-indigo/ppinot-repository/ppinot-xml-owl/target/test-classes/owl/";
		
		try {
            OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
//            InputStream sourceStream = getClass().getResourceAsStream(sourceFile);

			// ----- BPMN

			// importa el xml
			Bpmn20ModelHandlerInterface bpmnModelHandler = new Bpmn20ModelHandler();
			bpmnModelHandler.load(caminoOrigen, sourceFile);
//            bpmnModelHandler.load( sourceStream );

			// convierte a owl
            BPMN2OWLConverterInterface bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, owlManager);
			bpmnConverter.convertToOwlOntology(bpmnModelHandler);
			bpmnOntologyURI = bpmnConverter.getOntologyURI();
			
			// ----- PPINOT

			// importa el xml
			PpiNotModelHandlerInterface ppinotModelHandler = new PpiNotModelHandler();
			ppinotModelHandler.load(caminoOrigen, sourceFile);
//			ppinotModelHandler.load( sourceStream );

			// convierte a owl
            PPINOT2OWLConverterInterface ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, owlManager);
			ppinotConverter.setBpmnData( bpmnOntologyURI, bpmnModelHandler);
			ppinotOntology = ppinotConverter.convertToOwlOntology(ppinotModelHandler);

			// guarda la ontologia generada
			ppinotConverter.saveOntology(caminoDestino, "ExpressionsOWLPpinot "+sourceFile.substring(0, sourceFile.indexOf("."))+".owl");

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
		String testFile = "base.bpmn20.xml";

        assertTrue(ppinot2Owl(testFile));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time instance measure
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97"));
        assertTrue(analyser.isFrom("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isTo("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isActivityStart("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isActivityEnd("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isAppliedTo("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isAppliedTo("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // count instance measure
        assertTrue(analyser.isCountInstanceMeasure("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isWhen("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        
        // state condition instance measure
        assertTrue(analyser.isStateConditionInstanceMeasure("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F"));
        assertTrue(analyser.isMeets("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F", "sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isStateCondition("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isAppliedTo("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // data instance measure
        assertTrue(analyser.isDataInstanceMeasure("sid-98764615-A771-4C7D-8619-16008DAE4679"));
        assertTrue(analyser.isMeasuresData("sid-98764615-A771-4C7D-8619-16008DAE4679", "Dataobject1"));
        
        // data condition instance measure
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3"));
        assertTrue(analyser.isMeets("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3", "sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction", "Dataobject1"));
	}

	@Test
	public void testAggregated() {
		String nomFichOrigen = "aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time aggregated measure
        assertTrue(analyser.isAggregates("sid-C11F5344-C7B6-43EF-B6CD-4064C1AE8BA9", "sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isCyclicTimeIntanceMeasure("sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isFrom("sid-0c306446-8c69-449a-8da4-d2780209b03b", "startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isTo("sid-0c306446-8c69-449a-8da4-d2780209b03b", "endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        assertTrue(analyser.isActivityStart("startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isActivityEnd("endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        assertTrue(analyser.isAppliedTo("startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isAppliedTo("endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

        // count aggregated measure
        assertTrue(analyser.isAggregates("sid-85ADE8CD-C32A-4F59-B519-F9A59D9FF38A", "sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isCountInstanceMeasure("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isWhen("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        
        // state condition aggregated measure
        assertTrue(analyser.isAggregates("sid-8A0FCBF6-2486-4855-9D33-1921E646BB6C", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
       	assertTrue(analyser.isStateConditionInstanceMeasure("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
        assertTrue(analyser.isMeets("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isStateCondition("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isAppliedTo("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

        // data aggregated measure
        assertTrue(analyser.isAggregates("sid-58F97E64-1E94-4F39-A597-5854A11428B5", "sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isDataInstanceMeasure("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isMeasuresData("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd", "Dataobject1"));
        
        // data condition aggregated measure
        assertTrue(analyser.isAggregates("sid-06FF961A-1AF5-4EAB-A72F-E5B5B6356629", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isMeets("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction", "Dataobject1"));
	}

	@Test
	public void testDerived() {
		String nomFichOrigen = "derived.bpmn20.xml";
        
		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // derived single instance measure
        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B"));
        assertTrue(analyser.isCalculated("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B", "sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isCalculated("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B", "sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
        	// count measure sid-23A454B9-10BF-4B1A-B8EA-513192F6E188
        assertTrue(analyser.isCountInstanceMeasure("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isWhen("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isActivityEnd("TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
	    	// count measure sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC
	    assertTrue(analyser.isCountInstanceMeasure("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isWhen("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isActivityStart("TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isAppliedTo("TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));

        // derived multinstance measure
        assertTrue(analyser.isDerivedMultiInstanceMeasure("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C"));
        assertTrue(analyser.isCalculated("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C", "sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE"));
        assertTrue(analyser.isCalculated("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C", "sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B"));
        	// data property condition aggregated measure sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE
        assertTrue(analyser.isAggregates("sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE", "sid-9e4da4af-5526-4feb-9932-2b063feee420"));
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-9e4da4af-5526-4feb-9932-2b063feee420"));
        assertTrue(analyser.isMeets("sid-9e4da4af-5526-4feb-9932-2b063feee420", "sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction"));
        assertTrue(analyser.isAppliedTo("sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction", "Dataobject1"));
        	// data aggregated measure sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B
        assertTrue(analyser.isAggregates("sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B", "sid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isDataInstanceMeasure("sid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isMeasuresData("sid-46ee571c-7158-4cff-ad97-07e96fe59627", "Dataobject1"));
	}
	
	@Test
	public void testIsgroupedbyConnector() {
		String nomFichOrigen = "isgroupedby-connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count aggregated measure sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC
        assertTrue(analyser.isAggregates("sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC", "sid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isCountInstanceMeasure("sid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isWhen("sid-13863188-2979-4fcd-90bf-7af4054ccc5b", "TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b", "sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E"));
        assertTrue(analyser.isGroupedBy("sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC", "Dataobject1"));

        // count aggregated measure sid-905C337C-96E4-4D41-8465-62DD8A36221C
        assertTrue(analyser.isAggregates("sid-905C337C-96E4-4D41-8465-62DD8A36221C", "sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isCountInstanceMeasure("sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isWhen("sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339", "TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339", "sid-DB049C83-0C6F-4643-993D-9087B1C0D0CB"));
        assertTrue(analyser.isGroupedBy("sid-905C337C-96E4-4D41-8465-62DD8A36221C", "Dataobject1"));
	}

	@Test
	public void testAggregatedConnector() {
		String nomFichOrigen = "aggregated-connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count measure
        assertTrue(analyser.isAggregates("sid-C87139EB-DB41-4515-96F7-12200A024FE4", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isCountInstanceMeasure("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isWhen("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));

        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        assertTrue(analyser.isCalculated("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
	}
	
	@Test
	public void testLanes() {
		String nomFichOrigen = "lanes.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time measure
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F"));
        assertTrue(analyser.isFrom("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F", "startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isTo("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F", "endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));
        assertTrue(analyser.isActivityStart("startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isActivityEnd("endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));
        assertTrue(analyser.isAppliedTo("startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC", "sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isAppliedTo("endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124", "sid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));

        // count measure
        assertTrue(analyser.isCountInstanceMeasure("sid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isWhen("sid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44", "TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44", "sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
	}

	@Test
	public void testPpi() {
		String nomFichOrigen = "ppi.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // PPI de count measure
        assertTrue(analyser.isPpi("sid-eaf8645a-d548-4e9f-8a39-bf3aa78504fd"));
        assertTrue(analyser.isDefinition("sid-eaf8645a-d548-4e9f-8a39-bf3aa78504fd", "sid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isCountInstanceMeasure("sid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isWhen("sid-8F97D7AD-B211-429E-911B-F278B5D12D98", "TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98", "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        
        // PPI de time aggregated measure
        assertTrue(analyser.isPpi("sid-ef26d078-4a10-41d5-98f4-7dda5539df30"));
        assertTrue(analyser.isDefinition("sid-ef26d078-4a10-41d5-98f4-7dda5539df30", "sid-CC7889D8-3920-4A37-B90B-6BE0A8923F08"));

        assertTrue(analyser.isAggregates("sid-CC7889D8-3920-4A37-B90B-6BE0A8923F08", "sid-9a4caf47-3141-4370-9cfc-4902aabf53f3"));
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3"));
        assertTrue(analyser.isFrom("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3", "startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isTo("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3", "endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
        assertTrue(analyser.isActivityStart("startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isActivityEnd("endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
        assertTrue(analyser.isAppliedTo("startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78", "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isAppliedTo("endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B", "sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
	}

	@Test
	public void testSingleInstanceDerivedAggregated() {
		String nomFichOrigen = "single-instance-derived-aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count measure
        assertTrue(analyser.isAggregates("sid-C87139EB-DB41-4515-96F7-12200A024FE4", "sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        
        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        assertTrue(analyser.isCalculated("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        
        assertTrue(analyser.isCountInstanceMeasure("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isWhen("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));

	}

}
