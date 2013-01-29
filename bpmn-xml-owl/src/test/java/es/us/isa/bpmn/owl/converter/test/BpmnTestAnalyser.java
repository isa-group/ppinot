package es.us.isa.bpmn.owl.converter.test;

import es.us.isa.bpmn.owl.notation.Vocabulary;
import org.semanticweb.owlapi.model.*;


/**
 * Clase que verifica si se cumplen ciertas condiciones en una ontología OWL de BPMN 2.0
 * 
 * @author Edelia
 *
 */
public class BpmnTestAnalyser extends TestAnalyser {

	/**
	 * Constructor de la clase
	 * 
	 * @param ontology Ontología a verificar
	 */
	public BpmnTestAnalyser(OWLOntology ontology){

		super(ontology);
	}
	
	/**
	 * Verifica si un objeto es una tarea
	 * 
	 * @param objectId Id del objeto
	 * @return
	 */
	public Boolean isTask(String objectId) {

        return this.checkObjectClass(objectId, Vocabulary.ACTIVITY_URI);
	}
	
	/**
	 * Verifica si un objeto es un startEvent
	 * 
	 * @param objectId Id del objeto
	 * @return
	 */
	public Boolean isStartEvent(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.STARTEVENT_URI);
	}
	
	/**
	 * Verifica si un objeto es un endEvent
	 * 
	 * @param objectId Id del objeto
	 * @return
	 */
	public Boolean isEndEvent(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.ENDEVENT_URI);
	}
	
	/**
	 * Verifica si un objeto es un dataObject
	 * 
	 * @param objectId Id del objeto
	 * @return
	 */
	public Boolean isDataObject(String objectId) {
		
        return this.checkObjectClass(objectId, Vocabulary.DATAOBJECT_URI);
	}
	
	/**
	 * Verifica si un objeto precede directamente a otro
	 * 
	 * @param objectIdA Id del objeto que precede
	 * @param objectIdB Id del objeto precedido
	 * @return
	 */
	public Boolean isDirectlyPreceding(String objectIdA, String objectIdB) {

        return checkObjectProperty(objectIdA, objectIdB, Vocabulary.DIRECTLYPRECEDES_URI);
	}
	
	/**
	 * Verifica si un objeto es la entrada de datos de otro
	 * 
	 * @param objectIdA Id del objeto
	 * @param objectIdB Id del dataobject
	 * @return
	 */
	public Boolean isDataInputOf(String objectIdA, String objectIdB) {

        return checkObjectProperty(objectIdB, objectIdA, Vocabulary.DATAINPUT_URI);
	}

}
