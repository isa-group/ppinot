package es.us.isa.bpms.model;


import es.us.isa.bpms.editor.EditorResource;
import es.us.isa.bpms.process.ProcessElementsResource;
import es.us.isa.bpms.repository.ModelRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.resource.PPINOTModel2XmlConverter;
import es.us.isa.ppinot.resource.PPINOTResource;
import org.apache.batik.transcoder.AbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private ModelRepository modelRepository;

    @Autowired
    private Model2XmlConverter model2XmlConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private ServletContext context;


    public ModelsResource(UserService userService, ServletContext context) {
        this();
        this.userService = userService;
        this.context = context;
        model2XmlConverter = new PPINOTModel2XmlConverter(context.getRealPath("/WEB-INF/xsd/BPMN20.xsd"));
    }

    public ModelsResource() {
        super();
        log.info("Loaded ModelsResource");

    }

    public ModelRepository getModelRepository() {
        return modelRepository;
    }

    public void setModelRepository(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Path("/models")
    @GET
    @Produces("application/json")
    public List<ModelInfo> getProcesses(@Context UriInfo uriInfo) {
        List<ModelInfo> result = new ArrayList<ModelInfo>();
        List<String> processes = modelRepository.listModels();

        for (String modelId : processes) {

            ModelInfo modelInfo = createModelInfo(modelId, uriInfo);

            result.add(modelInfo);
        }

        return result;
    }

    private ModelInfo createModelInfo(String modelId, UriInfo uriInfo) {
        UriBuilder ub = uriInfo.getBaseUriBuilder().path(this.getClass()).path(this.getClass(), "getProcess");
        URI uri = ub.build(modelId);

        ModelInfo modelInfo = new ModelInfo(modelId, uri.toString());

        try {
            Model m = modelRepository.getModel(modelId);
            modelInfo.setName(m.getName());
            modelInfo.setDescription(m.getDescription());
            modelInfo.setShared(m.getShared());
            modelInfo.setOwner(modelId.equals(m.getModelId()));

            if (m.getModel() != null) {
                UriBuilder ubEditor = uriInfo.getBaseUriBuilder().path(EditorResource.class).queryParam("id", modelId);
                URI uriEditor = ubEditor.build();
                modelInfo.setEditor(uriEditor.toString());
            }

        } catch (Exception e) {
            log.warning("Error processing model info of "+modelId);
            log.warning(e.toString());
        }

        return modelInfo;
    }

    @Path("/model/{id}")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getProcess(@PathParam("id") String id) {
        return getProcessXml(id);
    }

    @Path("/model/{id}/json")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public InputStream getProcessJson(@PathParam("id") String id) {
        return modelRepository.getModelReader(id);
    }

    @Path("/model/{id}/svg")
    @GET
    @Produces("image/svg+xml")
    public String getProcessSvg(@PathParam("id") String id) {
        Model m = modelRepository.getModel(id);

        return m.getSvg();
    }

    @Path("/model/{id}/pdf")
    @GET
    @Produces("application/pdf")
    public InputStream getProcessPdf(@PathParam("id") String id) {
        PDFTranscoder transcoder = new PDFTranscoder();

        return transcode(id, transcoder);
    }

    @Path("/model/{id}/png")
    @GET
    @Produces("image/png")
    public InputStream getProcessPng(@PathParam("id") String id) {
        PNGTranscoder transcoder = new PNGTranscoder();
        return transcode(id, transcoder);
    }

    private InputStream transcode(String id, AbstractTranscoder transcoder) {
        String svg = getProcessSvg(id);
        if (svg == null)
            throw new NotFoundException("No image of model");

        TranscoderInput input = new TranscoderInput(new StringReader(svg));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(out);

        try {
            transcoder.transcode(input, output);
        } catch (TranscoderException e) {
            throw new RuntimeException("Transcoder error", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Path("/model/{id}/xml")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getProcessXml(@PathParam("id") String id) {
        Model m = modelRepository.getModel(id);

        if (m == null) {
            throw new org.jboss.resteasy.spi.NotFoundException("Model not found");
        }

        String xml = m.getXml();

        if (xml == null || xml.isEmpty()) {
            try {
                xml = createAndStoreXml(m);
            } catch (Exception e) {
                log.warning("Error while transforming model to XML");
                log.warning(e.toString());
                throw new RuntimeException("Error while transforming model to XML", e);
            }
        }

        return xml;
    }

    private String createAndStoreXml(Model m) {
        String xml;
        JSONObject jsonModel = m.getModel();
        if (jsonModel == null) {
            throw new RuntimeException("Model not valid");
        }

        xml = model2XmlConverter.transformToXml(jsonModel).toString();
        m.setXml(xml);

        try {
            modelRepository.saveModel(m.getModelId(), m);
        } catch (Exception e) {
            log.warning("Error saving model");
            log.warning(e.toString());
        }

        return xml;
    }


    @Path("/models")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProcess(@Context UriInfo uriInfo, ModelInfo info) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Response r;

        if (info.getModelId() == null || ! info.getModelId().matches("\\w+")) {
            throw new BadRequestException("Invalid modelId");
        }

        Model model = new Model(info.getModelId(), info.getName());

        if (info.hasClone()) {
            model.cloneFrom(modelRepository.getModel(info.getCloneFrom()));
        }

        model.setDescription(info.getDescription());

        if (modelRepository.addModel(model)) {
            ModelInfo modelInfo = createModelInfo(info.getModelId(), uriInfo);
            r = Response.ok(modelInfo, MediaType.APPLICATION_JSON_TYPE).build();
        }
        else {
            r = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return r;
    }

    @Path("/model/{id}")
    @DELETE
    public Response removeProcess(@PathParam("id") String id) {
        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Response r;
        if (modelRepository.removeModel(id))
            r = Response.ok().build();
        else
            r = Response.status(Response.Status.NOT_FOUND).build();

        return r;
    }


    @Path("/model/{id}/json")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public InputStream updateModel(@PathParam("id") String id, @FormParam("json_xml") String jsonXml, @FormParam("name") String name, @FormParam("type") String type, @FormParam("description") String description, @FormParam("svg_xml") String svgXml) {
        log.info("Saving name: "+name);
        log.info("Saving jsonXML: " + jsonXml);

        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Model m = modelRepository.getModel(id);
        if (m == null) {
            throw new org.jboss.resteasy.spi.NotFoundException("Model not found");
        }

        m.setName(name);
        m.setDescription(description);
        m.setSvg(svgXml);
        try {
            JSONObject jsonObject = new JSONObject(jsonXml);
            m.setModel(jsonObject);
            m.setXml(model2XmlConverter.transformToXml(jsonObject).toString());
        } catch (JSONException e) {
            throw new RuntimeException("The submitted model is not valid", e);
        } catch (Exception e) {
            log.warning(e.toString());
        }

        if (jsonXml != null) {
            modelRepository.saveModel(id, m);
        }

        return modelRepository.getModelReader(id);
    }

    @Path("/model/{id}/share")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public List<String> updateShare(@PathParam("id") String id, List<String> shares) {
        log.info("Updating share " + id);

        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");

        Model m = modelRepository.getModel(id);
        if (m == null) {
            throw new org.jboss.resteasy.spi.NotFoundException("Model not found");
        }

        if (!id.equals(m.getModelId())) {
            throw new UnauthorizedException("Not allowed to change shared");
        }

        m.setShared(new HashSet<String>(shares));

        modelRepository.saveModel(id, m);

        return shares;

    }


    @Path("/model/{id}")
    public ProcessElementsResource getProcessInfo(@PathParam("id") String id) {
        InputStream processReader = IOUtils.toInputStream(getProcess(id));
        return new ProcessElementsResource(processReader);
    }


    @Path("/model/{id}/ppis")
    public PPINOTResource getPPIs(@PathParam("id") String id) {
        InputStream processReader = IOUtils.toInputStream(getProcess(id));
        return new PPINOTResource(processReader, id, userService, model2XmlConverter, modelRepository);
    }
}
