package es.us.isa.bpmn.owl.converter.test;

import es.us.isa.bpmn.owl.notation.Vocabulary;
import org.semanticweb.owlapi.model.*;


public class BpmnTestAnalyser extends TestAnalyser {

	public BpmnTestAnalyser(OWLOntology ontology){

		super(ontology);
	}
	
	public Boolean isTask(String objectId) {

        return this.checkObjectClass(objectId, Vocabulary.ACTIVITY_URI);
	}
	
	public Boolean isStartEvent(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.STARTEVENT_URI);
	}
	
	public Boolean isEndEvent(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.ENDEVENT_URI);
	}
	
	public Boolean isDataObject(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.DATAOBJECT_URI);
	}
	
	public Boolean isDirectlyPreceding(String objectIdA, String objectIdB) {

        return checkObjectProperty(objectIdA, objectIdB, Vocabulary.DIRECTLYPRECEDES_URI);
	}
	
	public Boolean isDataInputOf(String objectIdA, String objectIdB) {

        return checkObjectProperty(objectIdB, objectIdA, Vocabulary.DATAINPUT_URI);
	}

}
