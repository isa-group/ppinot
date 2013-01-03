package es.us.isa.ppinot.owl.converter;

import es.us.isa.bpmn.owl.converter.BPMN2OWL;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;

import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import uk.ac.manchester.cs.owl.owlapi.OWLImportsDeclarationImpl;

/**
 * User: resinas
 * Date: 02/01/13
 * Time: 16:37
 */
public class PPINOT2OWLConverter implements PPINOT2OWL {

    private OWLOntologyManager manager;
    private String baseIRI;

    public PPINOT2OWLConverter(OWLOntologyManager manager, String baseIRI) {
        this.manager = manager;
        this.baseIRI = baseIRI;
    }

    @Override
    public OWLOntology convertToOwlOntology(TDefinitions bpmnDefinitions) {
        BPMN2OWL bpmn2OWL = new BPMN2OWLConverter(manager, baseIRI);
        OWLOntology bpmnOntology = bpmn2OWL.convertToOwlOntology(bpmnDefinitions);
        return null;
    }
}
