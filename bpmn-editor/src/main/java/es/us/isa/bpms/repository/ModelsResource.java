package es.us.isa.bpms.repository;

import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.transformation.Diagram2XmlConverter;
import es.us.isa.bpms.editor.EditorResource;
import es.us.isa.bpms.process.ProcessElementsResource;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.resource.PPINOTResource;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 08:55
 */
@Path("/service")
public class ModelsResource {

    private static final Logger log = Logger.getLogger(ModelsResource.class
            .getName());

    private ProcessRepository processRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ServletContext context;



    public ModelsResource() {
        log.info("Loaded ModelsResource");
    }

    public ProcessRepository getProcessRepository() {
        return processRepository;
    }

    public void setProcessRepository(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @Path("/models")
    @GET
    @Produces("application/json")
    public List<ProcessInfo> getProcesses(@Context UriInfo uriInfo) {
        List<ProcessInfo> result = new ArrayList<ProcessInfo>();
        Set<String> processes = new HashSet<String>(processRepository.listProcesses());
        List<String> jsonProcesses = processRepository.listJsonProcesses();
        processes.addAll(jsonProcesses);

        for (String modelId : processes) {

            ProcessInfo processInfo = createProcessInfo(modelId, uriInfo);

            result.add(processInfo);
        }

        return result;
    }

    private ProcessInfo createProcessInfo(String modelId, UriInfo uriInfo) {
        UriBuilder ub = uriInfo.getBaseUriBuilder().path(this.getClass()).path(this.getClass(), "getProcess");
        URI uri = ub.build(modelId);

        ProcessInfo processInfo = new ProcessInfo(modelId, uri.toString());

        try {
            Model m = processRepository.getProcessModelInfo(modelId);
            processInfo.setName(m.getName());
            processInfo.setDescription(m.getDescription());

            if (m.getModel() != null) {
                UriBuilder ubEditor = uriInfo.getBaseUriBuilder().path(EditorResource.class).queryParam("id", modelId);
                URI uriEditor = ubEditor.build();
                processInfo.setEditor(uriEditor.toString());
            }

        } catch (Exception e) {
            log.warning("Error processing model info of "+modelId);
            log.warning(e.toString());
        }

        return processInfo;
    }

    @Path("/model/{id}")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getProcess(@PathParam("id") String id) {
        String process;
        try {
            process = getProcessXml(id);
        } catch (Exception e) {
            try {
                InputStream processReader = processRepository.getProcessReader(id);
                process = IOUtils.toString(processReader);
            } catch(Exception ex) {
                throw new org.jboss.resteasy.spi.NotFoundException("Process not found", ex);
            }
        }

        return process;
    }

    @Path("/model/{id}/json")
    @Produces("application/json")
    @GET
    public InputStream getProcessJson(@PathParam("id") String id) {
        return processRepository.getProcessJsonReader(id);
    }


    @Path("/model/{id}/xml")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getProcessXml(@PathParam("id") String id) {
        Model m = processRepository.getProcessModelInfo(id);

        try {
            JSONObject jsonModel = m.getModel();
            if (jsonModel == null) {
                throw new RuntimeException("Model not valid");
            }

            Diagram2XmlConverter converter = new Diagram2XmlConverter(BasicDiagramBuilder.parseJson(jsonModel), context.getRealPath("/WEB-INF/xsd/BPMN20.xsd"));
            StringWriter xml = converter.getXml();
            return xml.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (BpmnConverterException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Path("/model/{id}")
    @DELETE
    public Response removeProcess(@PathParam("id") String id) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Response r;
        if (processRepository.removeProcess(id))
            r = Response.ok().build();
        else
            r = Response.status(Response.Status.NOT_FOUND).build();

        return r;
    }

    @Path("/models")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProcess(@Context UriInfo uriInfo, ProcessInfo info) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Response r;

        if (info.getModelId() == null || ! info.getModelId().matches("\\w+")) {
            throw new BadRequestException("Invalid modelId");
        }

        Model model = new Model(info.getModelId(), info.getName());
        model.setDescription(info.getDescription());

        if (processRepository.addProcess(model)) {
            ProcessInfo processInfo = createProcessInfo(info.getModelId(), uriInfo);
            r = Response.ok(processInfo, MediaType.APPLICATION_JSON_TYPE).build();
        }
        else {
            r = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return r;
    }


    @Path("/model/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public InputStream getProcessJson(@PathParam("id") String id, @FormParam("json_xml") String jsonXml, @FormParam("name") String name, @FormParam("type") String type, @FormParam("description") String description) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        OutputStream processWriter = processRepository.getProcessJsonWriter(id);

        Model m = new Model();
        m.setName(name);
        m.setDescription(description);
        m.setModelId(id);
        try {
            m.setModel(new JSONObject(jsonXml));
        } catch (JSONException e) {
            throw new RuntimeException("The submitted model is not valid", e);
        }

        log.info(jsonXml);
        if (jsonXml != null) {
            try {
                IOUtils.write(m.getJSON(), processWriter);
                processWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException("The model metadata is not valid", e);
            }
        }

        return processRepository.getProcessJsonReader(id);
    }


    @Path("/model/{id}")
    public ProcessElementsResource getProcessInfo(@PathParam("id") String id) {
        InputStream processReader = processRepository.getProcessReader(id);
        return new ProcessElementsResource(processReader);
    }


    @Path("/model/{id}/ppis")
    public PPINOTResource getPPIs(@PathParam("id") String id) {
        return new PPINOTResource(id, userService, processRepository);
    }
}
