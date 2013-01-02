package es.us.isa.bpmn.owl.converter;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDefinitions;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 14:40
 */
public interface BPMN2OWL {

    public OWLOntology convertToOwlOntology(TDefinitions bpmnDefinitions);

}
