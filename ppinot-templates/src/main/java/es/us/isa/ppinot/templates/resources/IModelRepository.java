package es.us.isa.ppinot.templates.resources;

import ppinotBpmn20.xmlClasses.bpmn20.TDefinitions;

public interface IModelRepository {

	public void store(TDefinitions definitions);
	public TDefinitions getModel(String id);
}
