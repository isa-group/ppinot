package es.us.isa.bpmn.owl.converter;

import es.us.isa.bpmn.handler.ModelHandleInterface;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 14:40
 */
public interface ToOWLConverterInterface {

    public OWLOntology convertToOwlOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException;
    public String getOntologyURI();
    public void saveOntology(String caminoDestino, String bpmnFilename);
}
