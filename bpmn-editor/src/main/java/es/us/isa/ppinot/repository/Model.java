package es.us.isa.ppinot.repository;

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
    private String model;

    public Model() {
        revision = 1;
    }

    public Model(String name, String description, String modelId, String model) {
        this.name = name;
        this.description = description;
        this.modelId = modelId;
        this.model = model;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
