package es.us.isa.bpmn.owl.converter.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;

public class TestBpmn2Owl {

	private String baseIRI;				
	
	private String caminoOrigen;
	private String caminoDestino;
	private String nomFichOrigen;
	private String nomFichDestino;
	
	private Boolean bpmn2Owl() {
		
		Bpmn20XmlExtracter extracter;
		try {
			
			// importa el xml
			extracter = new Bpmn20XmlExtracter();
			extracter.unmarshall(caminoOrigen, nomFichOrigen);
			extracter.generateModelLists();
			
			// convierte a owl
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			BPMN2OWLConverter converter = new BPMN2OWLConverter(baseIRI, manager);
			converter.convertToOwlOntology(extracter);
	
			// guarda la ontologia generada
			converter.saveOntology(caminoDestino, nomFichDestino);
			
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
		
		baseIRI = "http://www.isa.us.es/ontologies/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba base.bpmn20.xml";
		nomFichDestino = "ExpressionsOWLBpmn base.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregated() {
		
		baseIRI = "http://www.isa.us.es/ontologies/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated.bpmn20.xml";
		nomFichDestino = "ExpressionsOWLBpmn aggregated.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testDerived() {
		
		baseIRI = "http://www.isa.us.es/ontologies/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba derived.bpmn20.xml";
		nomFichDestino = "ExpressionsOWLBpmn derived.owl";
		
		assertTrue(bpmn2Owl());
	}

	@Test
	public void testAggregatedConnector() {
		
		baseIRI = "http://www.isa.us.es/ontologies/";
		
		caminoOrigen = "D:/eclipse-appweb-indigo/repository_isa/";
		caminoDestino = "D:/tmp-nuevo/";

		nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
		nomFichDestino = "ExpressionsOWLBpmn aggregated connector.owl";
		
		assertTrue(bpmn2Owl());
	}

}
