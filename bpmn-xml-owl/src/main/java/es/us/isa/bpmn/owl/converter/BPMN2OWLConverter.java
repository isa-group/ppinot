package es.us.isa.bpmn.owl.converter;

import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDefinitions;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 14:43
 */
public class BPMN2OWLConverter implements BPMN2OWL {

    private OWLOntologyManager manager;
    private String baseIRI;

    public BPMN2OWLConverter(OWLOntologyManager manager, String baseIRI) {
        this.manager = manager;
        this.baseIRI = baseIRI;
    }

    @Override
    public OWLOntology convertToOwlOntology(TDefinitions bpmnDefinitions) {
        String ontologyIRI = baseIRI + bpmnDefinitions.getId()+".owl";
        try {
            OWLOntology bpmnOntology = manager.createOntology(IRI.create(ontologyIRI));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
