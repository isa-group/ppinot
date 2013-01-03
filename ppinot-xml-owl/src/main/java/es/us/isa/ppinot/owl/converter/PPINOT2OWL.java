package es.us.isa.ppinot.owl.converter;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 14:52
 */
public interface PPINOT2OWL {
    public OWLOntology convertToOwlOntology(TDefinitions bpmnDefinitions);
}
