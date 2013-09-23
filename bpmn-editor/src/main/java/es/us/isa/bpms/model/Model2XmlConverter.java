package es.us.isa.bpms.model;

import org.json.JSONObject;

import java.io.StringWriter;

/**
 * Model2XmlConverter
 *
 * @author resinas
 */
public interface Model2XmlConverter {
    boolean canTransform(String type);
    StringWriter transformToXml(JSONObject jsonModel);
}
