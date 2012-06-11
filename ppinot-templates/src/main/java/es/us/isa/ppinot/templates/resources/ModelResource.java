package es.us.isa.ppinot.templates.resources;

import java.util.List;
import java.util.logging.Logger;

import javassist.NotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import ppinotBpmn20.xmlClasses.bpmn20.TDefinitions;
import ppinotBpmn20.xmlClasses.bpmn20.TProcess;
import ppinotBpmn20.xmlClasses.bpmn20.TRootElement;

@Path("/bp")
public class ModelResource {
	
	@Autowired
	private IModelRepository repository;

	private static final Logger log = Logger.getLogger(ModelResource.class
			.getName());

	@POST
	@Path("/models")
	@Consumes("application/xml")
	public void postModel(TDefinitions definitions) {

		TProcess process;
		List<JAXBElement<? extends TRootElement>> rootElements = definitions
				.getRootElement();

		for (JAXBElement<? extends TRootElement> elem : rootElements) {
			if (elem.getDeclaredType() == ppinotBpmn20.xmlClasses.bpmn20.TProcess.class) {
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