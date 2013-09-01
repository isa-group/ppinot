package es.us.isa.bpms.model;


import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.model.extension.PropertyListItem;
import de.hpi.bpmn2_0.transformation.Bpmn2XmlConverter;
import de.hpi.bpmn2_0.transformation.Constants;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import es.us.isa.bpms.editor.EditorResource;
import es.us.isa.bpms.process.ProcessElementsResource;
import es.us.isa.bpms.repository.ModelRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.diagram2xml.Diagram2PPINotConverter;
import es.us.isa.ppinot.diagram2xml.PPINotFactory;
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
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private UserService userService;

    @Autowired
    private ServletContext context;



    public ModelsResource() {
        log.info("Loaded ModelsResource");
        Diagram2BpmnConverter.setConstants(new Constants() {
            @Override
            public List<Class<? extends AbstractBpmnFactory>> getAdditionalFactoryClasses() {
                return new ArrayList<Class<? extends AbstractBpmnFactory>>();
            }

            @Override
            public List<Class<? extends PropertyListItem>> getAdditionalPropertyItemClasses() {
                return new ArrayList<Class<? extends PropertyListItem>>();
            }

            @Override
            public Map<String, String> getCustomNamespacePrefixMappings() {
                Map<String, String> n = new HashMap<String, String>();
                n.put("http://www.isa.us.es/ppinot", "ppinot");

                return n;
            }
        });
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

            ModelInfo modelInfo = createProcessInfo(modelId, uriInfo);

            result.add(modelInfo);
        }

        return result;
    }

    private ModelInfo createProcessInfo(String modelId, UriInfo uriInfo) {
        UriBuilder ub = uriInfo.getBaseUriBuilder().path(this.getClass()).path(this.getClass(), "getProcess");
        URI uri = ub.build(modelId);

        ModelInfo modelInfo = new ModelInfo(modelId, uri.toString());

        try {
            Model m = modelRepository.getModelInfo(modelId);
            modelInfo.setName(m.getName());
            modelInfo.setDescription(m.getDescription());

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
        Model m = modelRepository.getModelInfo(id);

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
        Model m = modelRepository.getModelInfo(id);

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

        xml = transformToXml(jsonModel);
        m.setXml(xml);
        modelRepository.saveModel(m);

        return xml;
    }

    private String transformToXml(JSONObject jsonModel) {
        try {
            BasicDiagram diagram = BasicDiagramBuilder.parseJson(jsonModel);
            Diagram2BpmnConverter diagram2BpmnConverter = new Diagram2BpmnConverter(diagram, AbstractBpmnFactory.getFactoryClasses());
            Definitions def = diagram2BpmnConverter.getDefinitionsFromDiagram();
            Diagram2PPINotConverter ppiNotConverter = new Diagram2PPINotConverter(diagram, diagram2BpmnConverter, new PPINotFactory());
            ppiNotConverter.transform();

            Bpmn2XmlConverter xmlConverter = new Bpmn2XmlConverter(def, context.getRealPath("/WEB-INF/xsd/BPMN20.xsd"));
            StringWriter x = xmlConverter.getXml();

            return x.toString();
        } catch(Exception e) {
            throw new RuntimeException("Error transforming json model to XML", e);
        }
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
        model.setDescription(info.getDescription());

        if (modelRepository.addModel(model)) {
            ModelInfo modelInfo = createProcessInfo(info.getModelId(), uriInfo);
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


    @Path("/model/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public InputStream getProcessJson(@PathParam("id") String id, @FormParam("json_xml") String jsonXml, @FormParam("name") String name, @FormParam("type") String type, @FormParam("description") String description, @FormParam("svg_xml") String svgXml) {
        log.info("Saving name: "+name);
        log.info("Saving jsonXML: " + jsonXml);

        if (! userService.isLogged())
            throw new UnauthorizedException("User not logged");


        Model m = new Model();
        m.setName(name);
        m.setDescription(description);
        m.setModelId(id);
        m.setSvg(svgXml);
        try {
            JSONObject jsonObject = new JSONObject(jsonXml);
            m.setModel(jsonObject);
            m.setXml(transformToXml(jsonObject));
        } catch (JSONException e) {
            throw new RuntimeException("The submitted model is not valid", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonXml != null) {
            modelRepository.saveModel(m);
        }

        return modelRepository.getModelReader(id);
    }


    @Path("/model/{id}")
    public ProcessElementsResource getProcessInfo(@PathParam("id") String id) {
        InputStream processReader = IOUtils.toInputStream(getProcess(id));
        return new ProcessElementsResource(processReader);
    }


    @Path("/model/{id}/ppis")
    public PPINOTResource getPPIs(@PathParam("id") String id) {
        InputStream processReader = IOUtils.toInputStream(getProcess(id));
        return new PPINOTResource(processReader, id, userService, modelRepository);
    }
}
