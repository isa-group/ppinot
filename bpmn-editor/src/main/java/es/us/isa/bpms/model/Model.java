package es.us.isa.bpms.model;

import es.us.isa.bpms.editor.EditorResource;
import es.us.isa.bpms.repository.Storeable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.*;

/**
 * User: resinas
 * Date: 13/05/13
 * Time: 13:32
 */
public class Model implements Storeable{

    public static final String BPMN20 = "BPMN 2.0";
    public static final String ORG = "Organization";

    private String name;
    private String description;
    private String modelId;
    private int revision;
    private JSONObject model;
    private JSONObject extensions;
    private String xml;
    private String svg;
    private String type;

    private Set<String> shared;

    private Model() {
        this.revision = 1;
        this.shared = new HashSet<String>();
        this.model = new JSONObject();
        this.extensions = new JSONObject();
    }

    public Model(String modelId, String name, String type) {
        this.name = name;
        this.modelId = modelId;
        this.type = type;
        this.shared = new HashSet<String>();

        loadEmptyModel();
        this.extensions = new JSONObject();
    }

    public void cloneFrom(Model clone) {
        this.description = clone.description;
        try {
            this.model = new JSONObject(clone.model.toString());
            this.extensions = new JSONObject(clone.extensions.toString());
        } catch (JSONException e) {
            throw new RuntimeException("Error cloning JSON Model", e);
        }
        this.xml = clone.xml;
        this.svg = clone.svg;
        this.type = clone.type;
    }

    @Override
    public String getJSON() throws JSONException {
        JSONObject jsonModel = new JSONObject();
        jsonModel.put("modelId", modelId);
        jsonModel.put("name", name);
        jsonModel.put("description", description);
        jsonModel.put("revision", revision);
        jsonModel.put("model", model);
        jsonModel.put("extensions", extensions);
        jsonModel.put("xml", xml);
        jsonModel.put("svg", svg);
        jsonModel.put("type", type);
        jsonModel.put("shared", shared);

        return jsonModel.toString();
    }

    public void loadEmptyModel() {
        JSONObject jsonModel = new JSONObject();

        if (BPMN20.equals(type)) {
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

        if (jsonModel.has("extensions"))
            m.setExtensions(jsonModel.getJSONObject("extensions"));

        if (jsonModel.has("xml")) {
            m.setXml(jsonModel.getString("xml"));
        }

        if (jsonModel.has("svg"))
            m.setSvg(jsonModel.getString("svg"));

        if (jsonModel.has("type"))
            m.setType(jsonModel.getString("type"));

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

    public JSONObject getExtensions() {
        return extensions;
    }

    public void setExtensions(JSONObject extensions) {
        this.extensions = extensions;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Map<String, URI> createLinks(UriBuilder builder) {
        Map<String, URI> links = new HashMap<String, URI>();

        UriBuilder basic = builder.clone().path("{html}").fragment("{id}");

        if (ORG.equals(type)) {
            links.put("editor", basic.build("organizational.html", "/" + modelId));
        } else {
            links.put("editor", builder.clone().path(EditorResource.class).queryParam("id", modelId).build());
            links.put("View PPIs", basic.build("ppi-template.html", "/" + modelId));
            links.put("Resource assignment", basic.build("resource-assignment.html", "/" + modelId));
        }

        return links;
    }

    public Map<String, URI> createExports(UriBuilder builder) {
        Map<String, URI> exports = new HashMap<String, URI>();

        UriBuilder base = builder.clone().path(ModelsResource.class);

        if (ORG.equals(type)) {
            exports.put("JSON", base.clone().path(ModelsResource.class, "getModelJson").build(modelId));
        } else {
            exports.put("XML", base.clone().path(ModelsResource.class, "getModelXml").build(modelId));
            exports.put("SVG", base.clone().path(ModelsResource.class, "getModelSvg").build(modelId));
            exports.put("PNG", base.clone().path(ModelsResource.class, "getModelPng").build(modelId));
            exports.put("PDF", base.clone().path(ModelsResource.class, "getModelPdf").build(modelId));
        }

        return exports;
    }
}
