package es.us.isa.ppinot.owl.analyser;

import es.us.isa.ppinot.owl.notation.RelationshipsVocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnalysisOntologyBuilder {

    private OWLOntologyManager owlManager;
    private final List<AbstractMeasureAxiomBuilder> abstractMeasureAxiomBuilders;

    public AnalysisOntologyBuilder(OWLOntologyManager owlManager) {
        this.owlManager = owlManager;
        abstractMeasureAxiomBuilders = new ArrayList<AbstractMeasureAxiomBuilder>();
        abstractMeasureAxiomBuilders.add(new TimeAxiomBuilder());
    }

    public OWLOntology buildAnalysisOntology(String analysisIRI, OWLOntology ppinotOntology) {
        OWLOntology analysisOntology;

        try {
            analysisOntology = owlManager.createOntology(IRI.create(analysisIRI));
            addImports(analysisOntology, ppinotOntology);
            addMeasureAxioms(analysisOntology, ppinotOntology);

        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        return analysisOntology;
    }

    private List<AbstractMeasureAxiomBuilder> getAbstractMeasureAxiomBuilders() {
        return abstractMeasureAxiomBuilders;
    }

    private void addImports(OWLOntology analysisOntology, OWLOntology ppinotOntology) {
        owlManager.applyChange(new AddImport(analysisOntology,
                owlManager.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(RelationshipsVocabulary.PPINOT_RELATIONSHIPS_URI))));
        owlManager.applyChange(new AddImport(analysisOntology,
                owlManager.getOWLDataFactory().getOWLImportsDeclaration(ppinotOntology.getOntologyID().getOntologyIRI())));
    }

    private void addMeasureAxioms(OWLOntology analysisOntology, OWLOntology ppinotOntology) {
        List<AbstractMeasureAxiomBuilder> builders = getAbstractMeasureAxiomBuilders();
        for (AbstractMeasureAxiomBuilder builder : builders) {
            owlManager.addAxioms(analysisOntology, builder.buildAxioms(analysisOntology, ppinotOntology));
        }
    }

}