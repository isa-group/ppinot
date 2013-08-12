package es.us.isa.bpms.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * User: resinas
 * Date: 13/05/13
 * Time: 13:32
 */
public class Model {
    private String name;
    private String description;
    private String modelId;
    private int revision;
    private JSONObject model;
    private String svg;

    public Model() {
        revision = 1;
    }

    public Model(String modelId, String name) {
        this.name = name;
        this.modelId = modelId;
    }

    public String getJSON() throws JSONException {
        JSONObject jsonModel = new JSONObject();
        jsonModel.put("modelId", modelId);
        jsonModel.put("name", name);
        jsonModel.put("description", description);
        jsonModel.put("revision", revision);
        jsonModel.put("model", model);
        jsonModel.put("svg", svg);

        return jsonModel.toString();
    }

    public void setEmptyModel() {
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

        if (jsonModel.has("svg"))
            m.setSvg(jsonModel.getString("svg"));

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

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}
