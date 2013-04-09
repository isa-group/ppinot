package es.us.isa.ppinot.templates.resources;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TRootElement;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.logging.Logger;

@Path("/bp")
public class ModelResource {
	
	@Autowired
	private IModelRepository repository;

	private static final Logger log = Logger.getLogger(ModelResource.class
			.getName());

    public ModelResource() {
        log.info("Loaded ModelResource");
    }

	@POST
	@Path("/models")
	@Consumes("application/xml")
	public void postModel(TDefinitions definitions) {

		TProcess process;
		List<JAXBElement<? extends TRootElement>> rootElements = definitions
				.getRootElement();

		for (JAXBElement<? extends TRootElement> elem : rootElements) {
			if (elem.getDeclaredType() == TProcess.class) {
				process = (TProcess) elem.getValue();
				log.info(process.getId());
				
				repository.store(definitions);
			}
			else {
				log.info("Bad luck");
			}
		}
	}
	
	@GET
	@Path("/models/{id}")
	@Produces("application/xml")
	public TDefinitions getModel(@PathParam("id") String id) {
		TDefinitions definitions = repository.getModel(id);
		if (definitions == null) throw new org.jboss.resteasy.spi.NotFoundException(id + " model not found");
		
		return definitions;
	}
}