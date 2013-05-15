package es.us.isa.ppinot.repository;

import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.transformation.Diagram2XmlConverter;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import es.us.isa.ppinot.model.PPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 08:55
 */
@Path("/service")
public class RepositoryResource {

    private static final Logger log = Logger.getLogger(RepositoryResource.class
            .getName());

    private ProcessRepository processRepository;
    private String resourcesDirectory;
    private String editorUrl;

    public RepositoryResource() {
        log.info("Loaded RepositoryResource");
    }

    public ProcessRepository getProcessRepository() {
        return processRepository;
    }

    public void setProcessRepository(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public String getEditorUrl() {
        return editorUrl;
    }

    public void setEditorUrl(String editorUrl) {
        this.editorUrl = editorUrl;
    }

    public String getResourcesDirectory() {
        return resourcesDirectory;
    }

    public void setResourcesDirectory(String resourcesDirectory) {
        this.resourcesDirectory = resourcesDirectory;
    }

    @Path("/models")
    @GET
    @Produces("application/json")
    public List<ProcessInfo> getProcesses(@Context UriInfo uriInfo) {
        List<ProcessInfo> result = new ArrayList<ProcessInfo>();
        for (String processName : processRepository.listProcesses()) {

            UriBuilder ub = uriInfo.getBaseUriBuilder().path(this.getClass()).path(this.getClass(), "getProcess");
            URI uri = ub.build(processName);

            String editor = editorUrl + "/p/editor?id=root-directory%3B" + processName + ".signavio.xml";
            result.add(new ProcessInfo(processName, uri.toString(), editor));

        }

        return result;
    }

    @Path("/model/{id}")
    @Produces("application/xml")
    @GET
    public String getProcess(@PathParam("id") String id) {
        InputStream processReader = processRepository.getProcessReader(id);
        String process;
        try {
            process = IOUtils.toString(processReader);
        } catch (IOException e) {
            throw new org.jboss.resteasy.spi.NotFoundException("Process not found", e);
        }

        return process;
    }

    @Path("/model/{id}/json")
    @Produces("application/json")
    @GET
    public InputStream getProcessJson(@PathParam("id") String id) {
        return processRepository.getProcessJsonReader(id);
    }


    @Autowired
    private ServletContext context;

    @Path("/model/{id}/xml")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getProcessXml(@PathParam("id") String id) {
        InputStream processReader = processRepository.getProcessJsonReader(id);
        try {
            String jsonModel = IOUtils.toString(processReader);

            Diagram2XmlConverter converter = new Diagram2XmlConverter(BasicDiagramBuilder.parseJson(jsonModel), context.getRealPath("/WEB-INF/xsd/BPMN20.xsd"));
            StringWriter xml = converter.getXml();
            return xml.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public InputStream getProcessJson(@PathParam("id") String id, @FormParam("json_xml") String jsonXml, @FormParam("name") String name, @FormParam("type") String type, @FormParam("description") String description) {
        OutputStream processWriter = processRepository.getProcessJsonWriter(id);
        //String jsonXml = form.getFirst("json_xml");
        //ObjectMapper om = new ObjectMapper();
        StringBuilder sb = new StringBuilder("{")
                .append("\"name\":\"").append(name).append("\",")
                .append("\"description\":\"").append(description).append("\",")
                .append("\"modelId\":\"").append(id).append("\",")
                .append("\"revision\":").append(1).append(",")
                .append("\"model\":").append(jsonXml).append("}");

        //Model m = new Model(name, description, id, jsonXml);
        log.info(jsonXml);
        if (jsonXml != null) {
            try {
                //om.writeValue(processWriter, m);
                IOUtils.write(sb, processWriter);
                processWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return processRepository.getProcessJsonReader(id);
    }


    @Path("/processes/{id}")
    public ProcessInfoResource getProcessInfo(@PathParam("id") String id) {
        InputStream processReader = processRepository.getProcessReader(id);
        return new ProcessInfoResource(processReader);
    }


    @Path("/processes/{id}/ppis")
    @Produces("application/json")
    @GET
    public Collection<PPI> getPPIs(@PathParam("id") String id) {
        try {
            PpiNotModelHandlerInterface handler = new PpiNotModelHandler();
            handler.load(processRepository.getProcessReader(id));
            return handler.getPpiModelMap().values();
        } catch (Exception e) {
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }

    }

    @Path("/processes/{id}/ppis")
    @Consumes("application/json")
    @POST
    public String storePPIs(@PathParam("id") String id, List<PPI> ppis) {
        PpiNotModelHandlerInterface ppinotModelHandler;
        try {
            ppinotModelHandler = new PpiNotModelHandler();
            ppinotModelHandler.load(processRepository.getProcessReader(id));
        } catch (Exception e) {
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }

        Map<String, PPI> ppiModelMap = new HashMap<String, PPI>();
        for (PPI p: ppis) {
            ppiModelMap.put(p.getId(), p);
        }

        ppinotModelHandler.setPpiModelMap(ppiModelMap);

        try {
            String procId = ppinotModelHandler.getProcId();
            ppinotModelHandler.save(processRepository.getProcessWriter(id+"-copy-"), procId);

            return "Ok";
        } catch (Exception e) {
            throw new RuntimeException("unable to create file", e);
        }


    }

    private File getResourcesFile(String id) {
        String filename = resourcesDirectory + File.separator + id + ".raci.json";

        return new File(filename);
    }



    @Path("/processes/{id}/raci")
    @Produces("application/json")
    @GET
    public String getResources(@PathParam("id") String id) {
        File resourcesFile = getResourcesFile(id);
        String raci = "";
        try {
            raci = FileUtils.readFileToString(resourcesFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ("".equals(raci))
            throw new org.jboss.resteasy.spi.NotFoundException("RACI not found");

        return raci;

    }

    @Path("/processes/{id}/raci")
    @POST
    public void saveRaci(@PathParam("id") String id, @FormParam("raci") String raci) {
        File resourcesFile = getResourcesFile(id);
        try {
            FileUtils.writeStringToFile(resourcesFile, raci);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
