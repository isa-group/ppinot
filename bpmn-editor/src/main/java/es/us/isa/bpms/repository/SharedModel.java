package es.us.isa.bpms.repository;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SharedModel
 *
 * @author resinas
 */
public class SharedModel implements Storeable {
    private static final String TYPE = "shared";

    private final String modelId;
    private final String owner;

    public SharedModel(String modelId, String owner) {
        this.modelId = modelId;
        this.owner = owner;
    }

    public String getModelId() {
        return modelId;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String getJSON() throws JSONException {
        JSONObject jsonModel = new JSONObject();
        jsonModel.put("modelId", modelId);
        jsonModel.put("owner", owner);
        jsonModel.put("type", TYPE);

        return jsonModel.toString();
    }

    public static boolean is(JSONObject jsonObject) {
        boolean isShared = false;
        try {
            if (jsonObject.has("type") && TYPE.equals(jsonObject.getString("type"))) {
                isShared = true;
            }
        } catch (JSONException e) {
            // If "type" is not a string, then it is not of type SharedModel
        }
        return isShared;
    }

    public static SharedModel createFrom(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null)
            throw new RuntimeException("Model could not be read");

        SharedModel m = null;

        if (SharedModel.is(jsonObject)) {
            String modelId = jsonObject.getString("modelId");
            String owner = jsonObject.getString("owner");
            m = new SharedModel(modelId, owner);
        }

        return m;
    }

    public boolean represents(String modelId, String owner) {
        return
                this.modelId.equals(modelId) &&
                this.owner.equals(owner);
    }
}
