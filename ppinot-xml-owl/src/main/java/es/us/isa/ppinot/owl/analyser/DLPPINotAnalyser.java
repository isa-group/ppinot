package es.us.isa.ppinot.owl.analyser;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverterInterface;
import es.us.isa.ppinot.analyser.PPINotAnalyser;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverter;
import es.us.isa.ppinot.owl.converter.PPINOT2OWLConverterInterface;
import es.us.isa.ppinot.owl.engine.DLQueryBuilder;
import es.us.isa.ppinot.owl.engine.DLQueryEngine;
import es.us.isa.ppinot.owl.engine.IdOwlConversor;
import es.us.isa.ppinot.owl.notation.RelationshipsVocabulary;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.Set;

/**
 * User: resinas
 * Date: 29/01/13
 * Time: 09:25
 */
public class DLPPINotAnalyser implements PPINotAnalyser {

    private static final String bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
    private static final String ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";

    private Bpmn20ModelHandlerInterface bpmnModelHandler;
    private PpiNotModelHandlerInterface ppinotModelHandler;

    private IdOwlConversor conversor;

    private OWLOntologyManager owlManager;

    private OWLOntology bpmnOntology;
    private OWLOntology ppinotOntology;
    private OWLOntology analysisOntology;

    private DLQueryEngine engine;

    public DLPPINotAnalyser(Bpmn20ModelHandlerInterface bpmnModelHandler, PpiNotModelHandlerInterface ppinotModelHandler, OWLReasonerFactory reasonerFactory) {
        this.bpmnModelHandler = bpmnModelHandler;
        this.ppinotModelHandler = ppinotModelHandler;
        owlManager = OWLManager.createOWLOntologyManager();
        conversor = new IdOwlConversor();

        try {
            BPMN2OWLConverterInterface bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, owlManager);
            bpmnOntology = bpmnConverter.convertToOwlOntology(bpmnModelHandler);
            String bpmnOntologyURI = bpmnConverter.getOntologyURI();

            PPINOT2OWLConverterInterface ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, owlManager);
            ppinotConverter.setBpmnData(bpmnOntologyURI, bpmnModelHandler);
            ppinotOntology = ppinotConverter.convertToOwlOntology(ppinotModelHandler);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        AnalysisOntologyBuilder builder = new AnalysisOntologyBuilder(owlManager);
        String analysisURI = ppinotBaseIRI + bpmnModelHandler.getProcId() + "-analysis.owl";
        analysisOntology = builder.buildAnalysisOntology(analysisURI, ppinotOntology);
        engine = new DLQueryEngine(reasonerFactory.createReasoner(analysisOntology));

    }

    public DLPPINotAnalyser(Bpmn20ModelHandlerInterface bpmnModelHandler, PpiNotModelHandlerInterface ppinotModelHandler) {
        this(bpmnModelHandler,ppinotModelHandler,new Reasoner.ReasonerFactory());
    }


    @Override
    public Set<String> measuredBPElements(Set<String> ppiId) {
        DLQueryBuilder builder = new DLQueryBuilder("?measuredBy some ?ppis")
                .setParameter("measuredBy", RelationshipsVocabulary.MEASUREDBY_URI)
                .setParameter("ppis", conversor.toOwlId(ppinotOntology.getOntologyID().getOntologyIRI(), ppiId));

        Set<OWLNamedIndividual> result = engine.getInstances(builder.toString(), false);
        return conversor.toId(result);
    }

    @Override
    public Set<String> measuredPPIs(String bpElementId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> involvedBPElements(Set<String> ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> notInvolvedBPElements(Set<String> ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> involvedInAllBPElements(Set<String> ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> associatedPPIs(String bpElementId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> ppiSameElements(String ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> ppiSubsetElements(String ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<String> ppiSupersetElements(String ppiId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
