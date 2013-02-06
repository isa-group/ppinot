package es.us.isa.ppinot.owl.analyser;

import es.us.isa.ppinot.owl.engine.DLQueryBuilder;
import es.us.isa.ppinot.owl.notation.RelationshipsVocabulary;
import es.us.isa.ppinot.owl.notation.Vocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.Set;

/**
 * User: resinas
 * Date: 30/01/13
 * Time: 21:29
 */
public class TimeAxiomBuilder extends AbstractMeasureAxiomBuilder {

    @Override
    protected Collection<OWLIndividual> getIndividuals(OWLOntology analysisOntology, OWLOntology ppinotOntology) {

        OWLDataFactory factory = ppinotOntology.getOWLOntologyManager().getOWLDataFactory();
        OWLClass cyclicTimeMeasure = factory.getOWLClass(IRI.create(Vocabulary.CYCLICTIMEMEASURE_URI));
        OWLClass linearTimeMeasure = factory.getOWLClass(IRI.create(Vocabulary.LINEARTIMEMEASURE_URI));
        OWLClass timeMeasure = factory.getOWLClass(IRI.create(Vocabulary.TIMEMEASURE_URI));

        Set<OWLIndividual> individuals = cyclicTimeMeasure.getIndividuals(ppinotOntology);
        individuals.addAll(linearTimeMeasure.getIndividuals(ppinotOntology));
        individuals.addAll(timeMeasure.getIndividuals(ppinotOntology));

        return individuals;
    }

    @Override
    protected DLQueryBuilder getQueryBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("?inv some {?i}");
        sb.append(" EquivalentTo: ");
        sb.append(" inverse(?appliesTo) some (inverse(?from) some {?i} and not(?changesToState some ?endState))");
        sb.append(" or ");
        sb.append(" inverse(?appliesTo) some (inverse(?to) some {?i} and not(?changesToState some ?startState))");
        sb.append(" or ");
        sb.append(" ( ?succ some (inverse(?appliesTo) some (inverse(?from) some {?i})) and ");
        sb.append("   ?prec some (inverse(?appliesTo) some (inverse(?to) some {?i})) ) ");

        DLQueryBuilder queryBuilder = new DLQueryBuilder(sb.toString());
        queryBuilder.setParameter("inv", RelationshipsVocabulary.INV_URI);
        queryBuilder.setParameter("appliesTo", Vocabulary.APPLIESTO_URI);
        queryBuilder.setParameter("from", Vocabulary.FROM_URI);
        queryBuilder.setParameter("changesToState", Vocabulary.CHANGESTOSTATE_URI);
        queryBuilder.setParameter("endState", es.us.isa.bpmn.owl.notation.Vocabulary.ENDSTATE_URI);
        queryBuilder.setParameter("to", Vocabulary.TO_URI);
        queryBuilder.setParameter("startState", es.us.isa.bpmn.owl.notation.Vocabulary.STARTSTATE_URI);
        queryBuilder.setParameter("succ", es.us.isa.bpmn.owl.notation.Vocabulary.SUCC_URI);
        queryBuilder.setParameter("prec", es.us.isa.bpmn.owl.notation.Vocabulary.PREC_URI);

        return queryBuilder;
    }
}
