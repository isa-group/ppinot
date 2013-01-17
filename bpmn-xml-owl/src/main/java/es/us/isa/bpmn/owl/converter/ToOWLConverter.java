package es.us.isa.bpmn.owl.converter;

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import es.us.isa.bpmn.handler.ModelHandleInterface;

/**
 * 
 * @author Edelia
 *
 */
public abstract class ToOWLConverter {

	private OWLOntologyManager manager;	// OWLOntologyManager utilizado
	private String baseIRI;				// IRI de la ontologia creada

	private OWLOntology ontology;		// Ontologia a la que se adicionan los axiomas

	private String ontologyURI;			// URI de la ontologia creada
	
	public ToOWLConverter(String baseIRI, OWLOntologyManager manager){
		
		this.setBaseIRI(baseIRI);
		this.setManager(manager);
	}

	public OWLOntology convertToOwlOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException {

    	setOntologyURI(getBaseIRI() + modelHandler.getProcId() + ".owl");
    	
		setOntology(getManager().createOntology(IRI.create(getOntologyURI())));

		this.generateOntology(modelHandler);
		
		return this.getOntology();
	}
	
	protected void addOntologyImports(String[] uris) {
		
		for (int i=0; i<uris.length; i++)
			getManager().applyChange(new AddImport(getOntology(), getManager().getOWLDataFactory().getOWLImportsDeclaration( IRI.create( uris[i] ) )));
	}
	
	protected abstract void generateOntology(ModelHandleInterface modelHandler) throws OWLOntologyCreationException;
	
	public void saveOntology(String caminoDestino, String bpmnFilename) {
		
		try {
			
			File bpmnFile = new File(caminoDestino + bpmnFilename);
			bpmnFile.createNewFile();
			
			getManager().saveOntology(getOntology(), IRI.create(bpmnFile.toURI()));
		} catch (OWLOntologyStorageException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	public String getBaseIRI() {
		return baseIRI;
	}

	public void setBaseIRI(String baseIRI) {
		this.baseIRI = baseIRI;
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public String getOntologyURI() {
		return ontologyURI;
	}

	public void setOntologyURI(String ontologyURI) {
		this.ontologyURI = ontologyURI;
	}

}
