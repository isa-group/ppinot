package es.us.isa.ppinot.owl.converter.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverter;
import es.us.isa.ppinot.xmlExtracter.PpiNotXmlExtracter;

public class TestPpinot2Owl {

	private String bpmnBaseIRI;				
	private String ppinotBaseIRI;				
	
	private String caminoOrigen;
	private String caminoDestino;
	
	private String nomFichOrigen;
	private String bpmnNomFichDestino;
	private String ppinotNomFichDestino;
	
	private BPMN2OWLConverter bpmnConverter;
	private PPINOT2OWLConverter ppinotConverter;
	
	private Boolean ppinot2Owl() {
		
		try {
			
			// ----- BPMN

			// importa el xml
			Bpmn20XmlExtracter bpmnExtracter = new Bpmn20XmlExtracter();
			bpmnExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			bpmnExtracter.generateModelLists();
			
			// convierte a owl
			bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, OWLManager.createOWLOntologyManager());
			bpmnConverter.convertToOwlOntology(bpmnExtracter);
	
			// guarda la ontologia generada
			bpmnConverter.saveOntology(caminoDestino, bpmnNomFichDestino);
			
			
			// ----- PPINOT

			// importa el xml
			PpiNotXmlExtracter ppinotExtracter = new PpiNotXmlExtracter();
			ppinotExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			ppinotExtracter.generateModelLists();
			
			// convierte a owl
			ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, OWLManager.createOWLOntologyManager());
			ppinotConverter.setBpmnData( bpmnConverter.getOntologyURI(), bpmnExtracter);
			ppinotConverter.convertToOwlOntology(ppinotExtracter);
	
			// guarda la ontologia generada
			ppinotConverter.saveOntology(caminoDestino, ppinotNomFichDestino);

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
		
		bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
		ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";

		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba base.bpmn20.xml";
		bpmnNomFichDestino = "ExpressionsOWLBpmn base.owl";
		ppinotNomFichDestino = "ExpressionsOWLPpinot base.owl";
		
		if (ppinot2Owl()) {
		
			TestAnalyser analyser = new TestAnalyser(bpmnConverter.getOntologyURI(), ppinotConverter.getOntologyURI(), ppinotConverter.getOntology());
			assertTrue(
					analyser.isTask("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C") &&	// Task 1
					analyser.isTask("sid-0B0B61E3-89F3-4028-9746-D122688C2E93") &&	// Task 2
					analyser.isStartEvent("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2") &&
					analyser.isEndEvent("sid-7AA7F58F-7769-4E20-831C-E3051C61D97C") &&
					analyser.isDataObject("sid-9832DD51-05CF-4B45-BB61-C46740621C4F") &&
					analyser.idDirectlyPreceding("sid-922FB2BD-6FB9-4C99-A0FF-B29F3BBC2FB2", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C") &&
					analyser.idDirectlyPreceding("sid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93") &&
					analyser.idDirectlyPreceding("sid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-7AA7F58F-7769-4E20-831C-E3051C61D97C") &&
					analyser.idDataInputOf("sid-9832DD51-05CF-4B45-BB61-C46740621C4F", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93")
					);
		} else
			assertTrue(false);
	}

	@Test
	public void testAggregated() {
		
		bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
		ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated.bpmn20.xml";
		bpmnNomFichDestino = "ExpressionsOWLBpmn aggregated.owl";
		ppinotNomFichDestino = "ExpressionsOWLPpinot aggregated.owl";
		
		assertTrue(ppinot2Owl());
	}

	@Test
	public void testDerived() {
		
		bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
		ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba derived.bpmn20.xml";
		bpmnNomFichDestino = "ExpressionsOWLBpmn derived.owl";
		ppinotNomFichDestino = "ExpressionsOWLPpinot derived.owl";
		
		assertTrue(ppinot2Owl());
	}

	@Test
	public void testAggregatedConnector() {
		
		bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
		ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		bpmnNomFichDestino = "ExpressionsOWLBpmn aggregated connector.owl";
		ppinotNomFichDestino = "ExpressionsOWLPpinot aggregated connector.owl";
		
		assertTrue(ppinot2Owl());
	}

}
