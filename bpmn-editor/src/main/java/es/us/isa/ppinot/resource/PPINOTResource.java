package es.us.isa.ppinot.resource;

import es.us.isa.bpms.model.Model;
import es.us.isa.bpms.repository.ModelRepository;
import es.us.isa.bpms.users.UserService;
import es.us.isa.ppinot.handler.PPINotModelHandler;
import es.us.isa.ppinot.handler.PPINotModelHandlerImpl;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.oryxeditor.server.diagram.generic.GenericShape;

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

            for (PPISet ppiSet : ppiSets) {
                for (PPI p : ppiSet.getPpis()) {
                    updateDiagramWithPPI(p, diagram);
                }
            }

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

    private void updateDiagramWithPPI(PPI ppi, BasicDiagram diagram) {
        GenericShape shape = getShapeById(ppi.getId(), "ppi", diagram);
        if (shape != null) {
            updatePPI(ppi, shape);
        }
    }

    private void updatePPI(PPI ppi, GenericShape shape) {
        shape.setProperty("name", ppi.getName());
        shape.setProperty("responsible", ppi.getResponsible());
        shape.setProperty("comments", ppi.getComments());
        shape.setProperty("goals", buildArray(ppi.getGoals(), "goal"));
        shape.setProperty("informed", buildArray(ppi.getInformed(), "inform"));

    }

    private JSONObject buildArray(Collection<String> collection, String property) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("totalCount", collection.size());
            JSONArray items = new JSONArray();
            for (String goal : collection) {
                JSONObject g = new JSONObject();
                g.put(property, goal);
                items.put(g);
            }
            obj.put("items", items);
        } catch (JSONException e) {
            log.severe("Error building json");
            log.severe(e.toString());
        }

        return obj;
    }

    private GenericShape getShapeById(String resourceId, String stencilId, GenericShape<?,?> shape) {
        GenericShape result = null;
        if (resourceId.equals(shape.getResourceId()) && stencilId.equals(shape.getStencilId())) {
            result = shape;
        } else {
            for (GenericShape<?,?> child : shape.getChildShapesReadOnly()) {
                result = getShapeById(resourceId, stencilId, child);
                if (result != null) {
                    break;
                }
            }
        }

        return result;
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
