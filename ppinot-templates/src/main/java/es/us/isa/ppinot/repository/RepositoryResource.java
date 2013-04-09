package es.us.isa.ppinot.repository;

import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import es.us.isa.ppinot.model.PPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 08:55
 */
@Path("/api/repository")
public class RepositoryResource {

    private static final Logger log = Logger.getLogger(RepositoryResource.class
            .getName());

    private ProcessRepository processRepository;
    private String resourcesDirectory;
    private String editorUrl;

    private Bpmn20ModelHandlerInterface bpmnModelHandler;
    private PpiNotModelHandlerInterface ppinotModelHandler;


    public RepositoryResource() {
        log.info("Loaded RepositoryResource");
    }

    public ProcessRepository getProcessRepository() {
        return processRepository;
    }

    public void setProcessRepository(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public Bpmn20ModelHandlerInterface getBpmnModelHandler() {
        return bpmnModelHandler;
    }

    public void setBpmnModelHandler(Bpmn20ModelHandlerInterface bpmnModelHandler) {
        this.bpmnModelHandler = bpmnModelHandler;
    }

    public PpiNotModelHandlerInterface getPpinotModelHandler() {
        return ppinotModelHandler;
    }

    public void setPpinotModelHandler(PpiNotModelHandlerInterface ppinotModelHandler) {
        this.ppinotModelHandler = ppinotModelHandler;
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

    @Path("/processes")
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

    @Path("/processes/{id}")
    @Produces("application/xml")
    @GET
    public String getProcess(@PathParam("id") String id) {
        InputStream processReader = processRepository.getProcessReader(id);
        String process = "";
        try {
            process = IOUtils.toString(processReader);
        } catch (IOException e) {
            throw new org.jboss.resteasy.spi.NotFoundException("Process not found", e);
        }

        return process;

    }

    private File getResourcesFile(String id) {
        String filename = resourcesDirectory + File.separator + id + ".raci.json";
        File resourcesFile = new File(filename);

        return resourcesFile;
    }

    @Path("/processes/{id}/activities")
    @Produces("application/json")
    @GET
    public List<String> getActivityNames(@PathParam("id") String id) {
        List<String> activities = new ArrayList<String>();

        try {
            bpmnModelHandler.load(processRepository.getProcessReader(id));
        } catch (Exception e) {
            throw new RuntimeException("Problem loading process " + id, e);
        }

        Collection<TTask> tasks = bpmnModelHandler.getTaskMap().values();
        for (TTask task : tasks) {
            String name = task.getName();
            if (name == null || "".equals(name))
                name = task.getId();

            activities.add(name);
        }

        return activities;
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
    public Collection<PPI> getPPIs(@PathParam("id") String id, List<PPI> ppis) {
        try {
            ppinotModelHandler.load(processRepository.getProcessReader(id));
        } catch (Exception e) {
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }

        log.info(ppis.toString());

        return ppinotModelHandler.getPpiModelMap().values();
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
