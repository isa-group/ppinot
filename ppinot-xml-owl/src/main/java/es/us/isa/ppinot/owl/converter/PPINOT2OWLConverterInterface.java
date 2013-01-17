package es.us.isa.ppinot.owl.converter;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.ToOWLConverterInterface;

public interface PPINOT2OWLConverterInterface extends ToOWLConverterInterface {

	public abstract void setBpmnData(String bpmnGeneratedOntologyURI, Bpmn20ModelHandlerInterface bpmn20ModelHandler);
}
