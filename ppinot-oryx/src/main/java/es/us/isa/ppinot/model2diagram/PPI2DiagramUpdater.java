package es.us.isa.ppinot.model2diagram;

import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.generic.GenericShape;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * PPI2DiagramUpdater
 *
 * @author resinas
 */
public class PPI2DiagramUpdater {

    private static final Logger log = Logger.getLogger(PPI2DiagramUpdater.class.getName());

    public void update(Collection<PPISet> ppiSets, BasicDiagram diagram) {
        for (PPISet ppiSet : ppiSets) {
            for (PPI p : ppiSet.getPpis()) {
                updateDiagramWithPPI(p, diagram);
            }
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
        if (!ppi.getGoals().isEmpty()) {
            shape.setProperty("goals", buildArray(ppi.getGoals(), "goal"));
        }
        if (!ppi.getInformed().isEmpty()) {
            shape.setProperty("informed", buildArray(ppi.getInformed(), "inform"));
        }
        if (ppi.getTarget() != null) {
            shape.setProperty("target", buildTarget(ppi.getTarget()));
        }
        if (ppi.getScope() != null) {
            buildScope(ppi.getScope(), shape);
        }

    }

    private void buildScope(ProcessInstanceFilter scope, GenericShape shape) {
        shape.removeProperty("lastinstancesscope");
        shape.removeProperty("timescope");

        if (scope instanceof LastInstancesFilter) {
            shape.setProperty("lastinstancesscope", ((LastInstancesFilter) scope).getNumberOfInstances());
        }
        else if (scope instanceof SimpleTimeFilter) {
            SimpleTimeFilter filter = (SimpleTimeFilter) scope;
            JSONArray items = new JSONArray();
            JSONObject timeFilter = new JSONObject();
            try {
                timeFilter.put("scopePeriod", filter.getPeriod().toString().toLowerCase());
                timeFilter.put("frequencyScope", filter.getFrequency());
                timeFilter.put("relativeScope", filter.isRelative());
                items.put(timeFilter);
                shape.setProperty("timescope", buildWrapperObject(items));
            } catch (JSONException e) {
                log.severe("Error building json");
                log.severe(e.toString());
                throw new RuntimeException("Error building json", e);
            }

        }
    }

    private JSONObject buildTarget(Target target) {
        try {
            JSONArray items = new JSONArray();

            JSONObject jsonTarget = new JSONObject();
            jsonTarget.put("lowerBound", target.getRefMin());
            jsonTarget.put("upperBound", target.getRefMax());
            items.put(jsonTarget);

            return buildWrapperObject(items);
        } catch (JSONException e) {
            log.severe("Error building json");
            log.severe(e.toString());
            throw new RuntimeException("Error building json", e);
        }
    }

    private JSONObject buildWrapperObject(JSONArray items) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("totalCount", items.length());
        obj.put("items", items);
        return obj;
    }

    private JSONObject buildArray(Collection<String> collection, String property) {
        try {
            JSONArray items = new JSONArray();
            for (String goal : collection) {
                JSONObject g = new JSONObject();
                g.put(property, goal);
                items.put(g);
            }
            return buildWrapperObject(items);
        } catch (JSONException e) {
            log.severe("Error building json");
            log.severe(e.toString());
            throw new RuntimeException("Error building json", e);
        }
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

}
