package es.us.isa.ppinot.diagram2xml.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractPPINotFactory
 *
 * @author resinas
 */
public abstract class AbstractPPINotFactory {
    protected es.us.isa.ppinot.xml.ObjectFactory factory = new es.us.isa.ppinot.xml.ObjectFactory();

    protected String valueOrNull(String property) {
        String value = null;
        if (notEmpty(property)) {
            value = property;
        }

        return value;
    }

    protected boolean notEmpty(String property) {
        return property != null && !property.isEmpty();
    }

    protected JSONObject getFirstObject(JSONObject jsonObject) {
        JSONObject firstObject = null;

        if (jsonObject != null) {
            try {
                JSONArray array = jsonObject.getJSONArray("items");
                if (array.length() > 0) {
                    firstObject = array.getJSONObject(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return firstObject;
    }

    protected List<String> getStringList(JSONObject jsonObject, String property) {
        List<String> result = new ArrayList<String>();
        if (jsonObject != null && jsonObject.has("items")) {
            try {
                JSONArray array = jsonObject.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject elem = array.getJSONObject(i);
                    result.add(elem.getString(property));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
