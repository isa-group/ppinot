package es.us.isa.bpms.model;

import es.us.isa.bpms.repository.Storeable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 13/05/13
 * Time: 13:32
 */
public class Model implements Storeable{
    private String name;
    private String description;
    private String modelId;
    private int revision;
    private JSONObject model;
    private String xml;
    private String svg;

    private Set<String> shared;

    private Model() {
        this.revision = 1;
        this.shared = new HashSet<String>();
    }

    public Model(String modelId, String name) {
        this.name = name;
        this.modelId = modelId;
        this.shared = new HashSet<String>();
        loadEmptyModel();
    }

    public void cloneFrom(Model clone) {
        this.description = clone.description;
        try {
            this.model = new JSONObject(clone.model.toString());
        } catch (JSONException e) {
            throw new RuntimeException("Error cloning JSON Model", e);
        }
        this.xml = clone.xml;
        this.svg = clone.svg;
    }

    @Override
    public String getJSON() throws JSONException {
        JSONObject jsonModel = new JSONObject();
        jsonModel.put("modelId", modelId);
        jsonModel.put("name", name);
        jsonModel.put("description", description);
        jsonModel.put("revision", revision);
        jsonModel.put("model", model);
        jsonModel.put("xml", xml);
        jsonModel.put("svg", svg);
        jsonModel.put("shared", shared);

        return jsonModel.toString();
    }

    public void loadEmptyModel() {
        JSONObject jsonModel = new JSONObject();
        try {
            jsonModel.put("ssextensions", Arrays.asList("http://www.isa.us.es/ppiontology/stencilsets/extensions/bpmnppi#"));
            JSONObject stencilset = new JSONObject();
            stencilset.put("url", "../stencilsets/bpmn2.0/bpmn2.0.json");
            stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            jsonModel.put("stencilset", stencilset);
            JSONObject stencil = new JSONObject();
            stencil.put("id", "BPMNDiagram");
            jsonModel.put("stencil", stencil);
        } catch (JSONException e) {
            throw new RuntimeException("Unexpected error", e);
        }

        this.setModel(jsonModel);
    }

    public static Model createModel(JSONObject jsonModel) throws JSONException {
        if (jsonModel == null)
            throw new RuntimeException("Model could not be read");

        Model m = new Model();
        if (jsonModel.has("modelId"))
            m.setModelId(jsonModel.getString("modelId"));

        if (jsonModel.has("name"))
            m.setName(jsonModel.getString("name"));

        if (jsonModel.has("description"))
            m.setDescription(jsonModel.getString("description"));

        if (jsonModel.has("revision"))
            m.setRevision(jsonModel.getInt("revision"));

        if (jsonModel.has("model"))
            m.setModel(jsonModel.getJSONObject("model"));

        if (jsonModel.has("xml")) {
            m.setXml(jsonModel.getString("xml"));
        }

        if (jsonModel.has("svg"))
            m.setSvg(jsonModel.getString("svg"));

        if (jsonModel.has("shared")) {
            JSONArray shared = jsonModel.getJSONArray("shared");
            for (int i = 0; i < shared.length(); i++) {
                m.shared.add(shared.getString(i));
            }
        }

        return m;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public JSONObject getModel() {
        return model;
    }

    public void setModel(JSONObject model) {
        this.model = model;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public Set<String> getShared() {
        return shared;
    }

    public void setShared(Set<String> shared) {
        this.shared = shared;
    }

    public Set<String> differenceShared(Model m) {
        Set<String> difference = new HashSet<String>(this.shared);
        if (m != null) {
            difference.removeAll(m.shared);
        }

        return difference;
    }

    public boolean sameSharedAs(Model m) {
        return m != null && shared.equals(m.shared);
    }
}
