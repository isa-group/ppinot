package es.us.isa.bpms.model;

import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;

import java.io.StringWriter;

/**
 * Model2XmlConverter
 *
 * @author resinas
 */
public interface Model2XmlConverter {
    StringWriter transformToXml(JSONObject jsonModel);

    StringWriter transformToXml(BasicDiagram diagram);
}
