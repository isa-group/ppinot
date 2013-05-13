package es.us.isa.ppinot.templates.resources;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;

public interface IModelRepository {

	public void store(TDefinitions definitions);
	public TDefinitions getModel(String id);
}
