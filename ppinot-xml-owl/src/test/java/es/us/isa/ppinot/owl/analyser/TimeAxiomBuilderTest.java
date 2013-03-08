package es.us.isa.ppinot.owl.analyser;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CommonBaseIRIMapper;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Set;

/**
 * User: resinas
 * Date: 06/02/13
 * Time: 17:28
 */
public class TimeAxiomBuilderTest {

    private static final String BASE_BPMN_OWL = "sid-4936fb97-f79a-4a2e-8958-dfc71d0bc848.owl";
    private static final String BASE_BPMN_OWL_URI = DLPPINotAnalyser.BPMN_BASE_IRI + BASE_BPMN_OWL;
    private OWLOntology ontology;
    private OWLOntologyManager manager;

    public void loader(String sourceFile) throws OWLOntologyCreationException, URISyntaxException {
        manager = OWLManager.createOWLOntologyManager();
        CommonBaseIRIMapper ontologyMapper = new CommonBaseIRIMapper(IRI.create(getClass().getResource("")));

        ontologyMapper.addMapping(IRI.create(BASE_BPMN_OWL_URI), BASE_BPMN_OWL);
        manager.addIRIMapper(ontologyMapper);

        ontology = manager.loadOntologyFromOntologyDocument(getClass().getResourceAsStream(sourceFile));
    }

    @Test
    public void shouldReturnAllTimeMeasures() throws OWLOntologyCreationException, URISyntaxException {
        loader("base.owl");
        TimeAxiomBuilder b = new TimeAxiomBuilder();
        Collection<OWLIndividual> axioms = b.getIndividuals(null, ontology);
        Assert.assertEquals(1, axioms.size());
        Assert.assertEquals(DLPPINotAnalyser.PPINOT_BASE_IRI + BASE_BPMN_OWL +"#sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97",axioms.iterator().next().toStringID());
    }

    @Test
    public void shouldBuildAxioms() throws OWLOntologyCreationException, URISyntaxException {
        loader("base.owl");
        OWLOntology analysisOntology = manager.createOntology();
        AnalysisOntologyBuilder builder = new AnalysisOntologyBuilder(manager);
        builder.addImports(analysisOntology, ontology);

        TimeAxiomBuilder axiomBuilder = new TimeAxiomBuilder();

        Set<OWLAxiom> axioms = axiomBuilder.buildAxioms(analysisOntology, ontology);
        Assert.assertEquals(1, axioms.size());
        Assert.assertTrue(axioms.iterator().next().isOfType(AxiomType.EQUIVALENT_CLASSES));
    }
}
