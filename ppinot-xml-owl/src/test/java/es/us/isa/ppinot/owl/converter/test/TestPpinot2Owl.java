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

public class TestPpinot2Owl {

	private String bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
	private String ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
	private String bpmnOntologyURI;

    private OWLOntology ppinotOntology;

	private Boolean ppinot2Owl(String sourceFile) {
		
		String caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		String caminoDestino = "D:/tmp-nuevo/";
		
		try {
            OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
//            InputStream sourceStream = getClass().getResourceAsStream(sourceFile);

			// ----- BPMN

			// importa el xml
			Bpmn20ModelHandlerInterface bpmnModelHandler = new Bpmn20ModelHandler();
			bpmnModelHandler.load(caminoOrigen, "prueba "+sourceFile);
//            bpmnModelHandler.load( sourceStream );

			// convierte a owl
            BPMN2OWLConverterInterface bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, owlManager);
			bpmnConverter.convertToOwlOntology(bpmnModelHandler);
			bpmnOntologyURI = bpmnConverter.getOntologyURI();
			
			// ----- PPINOT

			// importa el xml
			PpiNotModelHandlerInterface ppinotModelHandler = new PpiNotModelHandler();
			ppinotModelHandler.load(caminoOrigen, "prueba "+sourceFile);
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
        
        // time measure
        assertTrue(analyser.isTimeIntanceMeasure("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97"));
        assertTrue(analyser.isFrom("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isTo("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isActivityStart("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isActivityEnd("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isAppliedTo("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isAppliedTo("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // count measure
        assertTrue(analyser.isCountInstanceMeasure("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isWhen("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "TimeInstancesid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isActivityStart("TimeInstancesid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isAppliedTo("TimeInstancesid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        
        // state condition measure
        assertTrue(analyser.isStateConditionInstanceMeasure("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F"));
        assertTrue(analyser.isMeets("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F", "sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isFuntionalProperty("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isAppliedTo("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // data intansce measure
        assertTrue(analyser.isDataInstanceMeasure("sid-98764615-A771-4C7D-8619-16008DAE4679"));
        assertTrue(analyser.isMeasuresData("sid-98764615-A771-4C7D-8619-16008DAE4679", "Dataobject1"));
        
        // data condition instance measure
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3"));
        assertTrue(analyser.isMeets("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3", "sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isFuntionalProperty("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction", "Dataobject1"));
	}

	@Test
	public void testAggregated() {
		String nomFichOrigen = "aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time measure
        assertTrue(analyser.isAggregates("sid-0c306446-8c69-449a-8da4-d2780209b03bIntermediate1", "sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isTimeIntanceMeasure("sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isFrom("sid-0c306446-8c69-449a-8da4-d2780209b03b", "TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bFrom"));
        assertTrue(analyser.isTo("sid-0c306446-8c69-449a-8da4-d2780209b03b", "TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bTo"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bFrom"));
        assertTrue(analyser.isActivityEnd("TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bTo"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bFrom", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-0c306446-8c69-449a-8da4-d2780209b03bTo", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

        // count measure
        assertTrue(analyser.isAggregates("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340Intermediate1", "sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isCountInstanceMeasure("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isWhen("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        
        // state condition measure
        assertTrue(analyser.isAggregates("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Intermediate1", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
       	assertTrue(analyser.isStateConditionInstanceMeasure("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
        assertTrue(analyser.isMeets("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isFuntionalProperty("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isAppliedTo("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

        // data intansce measure
        assertTrue(analyser.isAggregates("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fdIntermediate1", "sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isDataInstanceMeasure("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isMeasuresData("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd", "Dataobject1"));
        
        // data condition instance measure
        assertTrue(analyser.isAggregates("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Intermediate1", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isMeets("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isFuntionalProperty("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction", "Dataobject1"));

	}

	@Test
	public void testDerived() {
		String nomFichOrigen = "derived.bpmn20.xml";
        
		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // derived single instance measure
        assertTrue(analyser.isDerivedSingleInstanceMeasure("DerivedSingleInstance1"));
        assertTrue(analyser.isCalculated("DerivedSingleInstance1", "sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isCalculated("DerivedSingleInstance1", "sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
        	// count measure sid-23A454B9-10BF-4B1A-B8EA-513192F6E188
        assertTrue(analyser.isCountInstanceMeasure("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isWhen("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "TimeInstancesid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isActivityEnd("TimeInstancesid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isAppliedTo("TimeInstancesid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
	    	// count measure sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC
	    assertTrue(analyser.isCountInstanceMeasure("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isWhen("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "TimeInstancesid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isActivityStart("TimeInstancesid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isAppliedTo("TimeInstancesid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));

        // derived multinstance measure
        assertTrue(analyser.isDerivedMultiInstanceMeasure("DerivedMultiInstance1"));
        assertTrue(analyser.isCalculated("DerivedMultiInstance1", "sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE"));
        assertTrue(analyser.isCalculated("DerivedMultiInstance1", "sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B"));

// ME QUEDE AQUI no se adiciona a la ontologia las medidas involucradas en DerivedMultiInstance1
	}

	@Test
	public void testAggregatedConnector() {
		String nomFichOrigen = "aggregated connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
	}

}
