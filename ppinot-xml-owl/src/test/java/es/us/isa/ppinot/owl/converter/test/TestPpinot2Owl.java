package es.us.isa.ppinot.owl.converter.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import es.us.isa.bpmn.owl.converter.ToOWLConverterInterface;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverter;
import es.us.isa.ppinot.xmlExtracter.PpiNotXmlExtracter;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class TestPpinot2Owl {

	private String bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
	private String ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
	

	private OWLOntology bpmnOntology;
    private OWLOntology ppinotOntology;

	private Boolean ppinot2Owl(String sourceFile) {
		
		try {
            OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();

			// ----- BPMN

			// importa el xml
			Bpmn20XmlExtracter bpmnExtracter = new Bpmn20XmlExtracter();
            bpmnExtracter.unmarshall(
                    getClass().getResourceAsStream(sourceFile));

			// convierte a owl
            BPMN2OWLConverter bpmnConverter;
			bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, owlManager);
			bpmnOntology = bpmnConverter.convertToOwlOntology(bpmnExtracter);

			// guarda la ontologia generada
//			bpmnConverter.saveOntology("", "ejemplo.owl");
			
			
			// ----- PPINOT

			// importa el xml
//			PpiNotXmlExtracter ppinotExtracter = new PpiNotXmlExtracter();
//			ppinotExtracter.unmarshall(sourceStream);
//
//
//			// convierte a owl
//
//            PPINOT2OWLConverter ppinotConverter;
//            ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, owlManager);
//			ppinotConverter.setBpmnData( bpmnConverter.getOntologyURI(), bpmnExtracter);
//			ppinotOntology = ppinotConverter.convertToOwlOntology(ppinotExtracter);

			// guarda la ontologia generada
			//ppinotConverter.saveOntology(caminoDestino, ppinotNomFichDestino);

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
		String testFile = "/base.bpmn20.xml";

        assertTrue(ppinot2Owl(testFile));

        TestAnalyser analyser = new TestAnalyser(bpmnOntology);
//        assertTrue("Start event not present", analyser.isStartEvent("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2"));
        assertTrue("Task 1 not present", analyser.isTask("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue("Task 2 not present", analyser.isTask("sid-0B0B61E3-89F3-4028-9746-D122688C2E93") );
//        assertTrue("End event not present", analyser.isEndEvent("sid-7AA7F58F-7769-4E20-831C-E3051C61D97C"));
        assertTrue("Start event precedes Task 1", analyser.isDirectlyPreceding("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue("Task 1 precedes Task 2", analyser.isDirectlyPreceding("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue("Task 2 precedes end event", analyser.isDirectlyPreceding("sid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-7AA7F58F-7769-4E20-831C-E3051C61D97C"));
//        assertTrue("Data object not present", analyser.isDataObject("sid-9832DD51-05CF-4B45-BB61-C46740621C4F"));
//        assertTrue("Data object is input of Task 2", analyser.isDataInputOf("sid-9832DD51-05CF-4B45-BB61-C46740621C4F", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
	}

	@Test
	public void testAggregated() {
		String nomFichOrigen = "aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
	}

	@Test
	public void testDerived() {
		String nomFichOrigen = "derived.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
	}

	@Test
	public void testAggregatedConnector() {
		String nomFichOrigen = "aggregated-connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
	}

}
