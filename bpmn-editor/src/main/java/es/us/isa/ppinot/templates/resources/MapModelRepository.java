package es.us.isa.ppinot.templates.resources;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MapModelRepository implements IModelRepository {
	
	private Map<String, TDefinitions> models;
	
	public MapModelRepository() {
		models = new HashMap<String, TDefinitions>();
	}

	@Override
	public void store(TDefinitions definitions) {
		models.put(definitions.getId(), definitions);

	}

	@Override
	public TDefinitions getModel(String id) {		
		return models.get(id);
	}

}
