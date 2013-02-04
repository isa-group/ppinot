package es.us.isa.ppinot.owl.engine;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.coode.owlapi.manchesterowlsyntax.OntologyAxiomPair;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Set;

public class DLQueryParser {
    private OWLOntology rootOntology;
    private BidirectionalShortFormProvider bidiShortFormProvider;

    public DLQueryParser(OWLOntology rootOntology) {
        this(rootOntology, new DefaultPrefixManager());
    }

    /** Constructs a DLQueryParser using the specified ontology and short form
     * provider to map entity IRIs to short names.
     * 
     * @param rootOntology
     *            The root ontology. This essentially provides the domain
     *            vocabulary for the query.
     * @param shortFormProvider
     *            A short form provider to be used for mapping back and forth
     *            between entities and their short names (renderings). */
    public DLQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
        this.rootOntology = rootOntology;
        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        // Create a bidirectional short form provider to do the actual mapping.
        // It will generate names using the input
        // short form provider.
        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
                importsClosure, shortFormProvider);
    }

    /** Parses a class expression string to obtain a class expression.
     * 
     * @param classExpressionString
     *            The class expression string
     * @return The corresponding class expression */
    public OWLClassExpression parseClassExpression(String classExpressionString) {
        ManchesterOWLSyntaxEditorParser parser = createParser(classExpressionString);
        OWLClassExpression classExpression = null;
        // Do the actual parsing
        try {
            classExpression = parser.parseClassExpression();
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }

        return classExpression;
    }

	private ManchesterOWLSyntaxEditorParser createParser(String expressionString) {
		OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
                .getOWLDataFactory();
        // Set up the real parser
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(
                dataFactory, expressionString);
        parser.setDefaultOntology(rootOntology);
        // Specify an entity checker that will be used to check a class
        // expression contains the correct names.
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
		return parser;
	}

	public Set<OntologyAxiomPair> parseIndividualFrame(String string) {
		ManchesterOWLSyntaxEditorParser parser = createParser(string);
		try {
			return parser.parseIndividualFrame();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	public OWLIndividual parseIndividual(String string) {
		ManchesterOWLSyntaxEditorParser parser = createParser(string);
		try {
			return parser.parseIndividual();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}
	
	public OWLAxiom parseAxiom(String string) {
		ManchesterOWLSyntaxEditorParser parser = createParser(string);
		try {
			return parser.parseAxiom();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}		
	}

}
