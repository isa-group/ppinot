package es.us.isa.bpmn.owl.converter;

import es.us.isa.bpmn.xmlExtracter.Bpmn20XmlExtracter;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 14:40
 */
public interface BPMN2OWL {

    public OWLOntology convertToOwlOntology(Bpmn20XmlExtracter bpmn20XmlExtracter) throws OWLOntologyCreationException;

}
