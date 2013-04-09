package es.us.isa.ppinot.owl.converter;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.ToOWLConverterInterface;

/**
 * Interfaz de las clases que convierten a owl, a partir de los objetos del modelo en un ModelHandleInterface para PPINOT
 * 
 * @author Edelia
 *
 */
public interface PPINOT2OWLConverterInterface extends ToOWLConverterInterface {

	/**
	 * Da valor a propiedades de la clase que tienen informacion sobre el BPMN relacionado con el mismo proceso
	 * 
	 * @param bpmnGeneratedOntologyURI URI de la ontologia BPMN relacionada con el proceso
	 * @param bpmn20ModelHandler Objeto que maneja el modelo con la informacion del BPMN relacionado con el proceso
	 */
	public abstract void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20ModelHandlerInterface bpmn20ModelHandler);
}
