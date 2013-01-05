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
	
	private Boolean bpmn2Owl() {
		
		try {
			
			// ----- BPMN

			// importa el xml
			Bpmn20XmlExtracter bpmnExtracter = new Bpmn20XmlExtracter();
			bpmnExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			bpmnExtracter.generateModelLists();
			
			// convierte a owl
			BPMN2OWLConverter bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, OWLManager.createOWLOntologyManager());
			bpmnConverter.convertToOwlOntology(bpmnExtracter);
	
			// guarda la ontologia generada
			bpmnConverter.saveOntology(caminoDestino, bpmnNomFichDestino);
			
			
			// ----- PPINOT

			// importa el xml
			PpiNotXmlExtracter ppinotExtracter = new PpiNotXmlExtracter();
			ppinotExtracter.unmarshall(caminoOrigen, nomFichOrigen);
			ppinotExtracter.generateModelLists();
			
			// convierte a owl
			PPINOT2OWLConverter ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, OWLManager.createOWLOntologyManager());
			ppinotConverter.setBpmnData( bpmnConverter.getOntologyURI(), bpmnExtracter);
			ppinotConverter.convertToOwlOntology(ppinotExtracter);
	
			// guarda la ontologia generada
			ppinotConverter.saveOntology(caminoDestino, ppinotNomFichDestino);
			
			return true;
		} catch (JAXBException e) {

			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {

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
		
		assertTrue(bpmn2Owl());
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
		
		assertTrue(bpmn2Owl());
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
		
		assertTrue(bpmn2Owl());
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
		
		assertTrue(bpmn2Owl());
	}

}
