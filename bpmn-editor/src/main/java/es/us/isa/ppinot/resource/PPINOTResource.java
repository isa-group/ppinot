package es.us.isa.ppinot.resource;

import es.us.isa.bpms.model.Model;
import es.us.isa.bpms.repository.ModelRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.handler.PPINotModelHandler;
import es.us.isa.ppinot.handler.PPINotModelHandlerImpl;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.model2diagram.PPI2DiagramUpdater;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;

import javax.ws.rs.*;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * User: resinas
 * Date: 20/05/13
 * Time: 22:06
 */
public class PPINOTResource {

    private static final Logger log = Logger.getLogger(PPINOTResource.class.getName());


    private UserService userService;
    private ModelRepository modelRepository;
    private String id;
    private InputStream processStream;
    private es.us.isa.bpms.model.Model2XmlConverter converter;

    public PPINOTResource(InputStream processStream, String id, UserService userService, es.us.isa.bpms.model.Model2XmlConverter converter, ModelRepository modelRepository) {
        this.processStream = processStream;
        this.id = id;
        this.userService = userService;
        this.converter = converter;
        this.modelRepository = modelRepository;
    }

    @Produces("application/json")
    @GET
    public Collection<PPISet> getPPIs(@PathParam("id") String id) {
        PPINotModelHandler handler = getPpiNotModelHandler(id);
        return handler.getPPISets();
    }

    @Consumes("application/json")
    @PUT
    public Collection<PPISet> storePPIs(Collection<PPISet> ppiSets) {
//        if (! userService.isLogged())
//            throw new UnauthorizedException("User not logged");
        Model m = modelRepository.getModelInfo(id);

        try {
            BasicDiagram diagram = BasicDiagramBuilder.parseJson(m.getModel());

            new PPI2DiagramUpdater().update(ppiSets, diagram);

            JSONObject diagramJSON = diagram.getJSON();
            m.setModel(diagramJSON);
            m.setXml(converter.transformToXml(diagramJSON).toString());
            modelRepository.saveModel(m);
        } catch (JSONException e) {
            log.warning("Could not load model of  " + id);
            log.warning(e.toString());
            throw new RuntimeException("Could not load model of " + id, e);
        }


        try {
            return ppiSets;
        } catch (Exception e) {
            throw new RuntimeException("unable to create file", e);
        }


    }


    private PPINotModelHandler getPpiNotModelHandler(String id) {
        PPINotModelHandler ppinotModelHandler;
        try {
            ppinotModelHandler = new PPINotModelHandlerImpl();
            ppinotModelHandler.load(processStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problem loading PPIs of process " + id, e);
        }
        return ppinotModelHandler;
    }
}
