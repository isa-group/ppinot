package es.us.isa.ppinot.owl.analyser;

import es.us.isa.ppinot.owl.engine.DLQueryBuilder;
import es.us.isa.ppinot.owl.engine.DLQueryParser;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 03/02/13
 * Time: 21:53
 */
public abstract class AbstractMeasureAxiomBuilder {
    public Set<OWLAxiom> buildAxioms(OWLOntology analysisOntology, OWLOntology ppinotOntology) {
        DLQueryParser parser = new DLQueryParser(analysisOntology);
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();

        Collection<OWLIndividual> individuals = getIndividuals(analysisOntology, ppinotOntology);

        for (OWLIndividual i: individuals) {
            if (i.isNamed()) {
                OWLNamedIndividual namedIndividual = i.asOWLNamedIndividual();
                DLQueryBuilder queryBuilder = getQueryBuilder();
                queryBuilder.setParameter("i", namedIndividual.getIRI().toString());

                axioms.add(parser.parseAxiom(queryBuilder.toString()));
            }
        }
        return axioms;
    }

    /**
     * Returns the query builder that shall be used to create the axioms for each individual. The DLQueryBuilder must
     * use the param ?i to refer to the individual that shall be used in its place.
     * @return DLQueryBuilder It must use ?i to refer to the individual
     */
    protected abstract DLQueryBuilder getQueryBuilder();

    /**
     * Returns the individuals for which the axioms shall be built
     * @return
     * @param analysisOntology
     * @param ppinotOntology
     */
    protected abstract Collection<OWLIndividual> getIndividuals(OWLOntology analysisOntology, OWLOntology ppinotOntology);


}
