package es.us.isa.ppinot.resource;

import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.model.extension.PropertyListItem;
import de.hpi.bpmn2_0.transformation.Bpmn2XmlConverter;
import de.hpi.bpmn2_0.transformation.Constants;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import es.us.isa.bpms.model.Model2XmlConverter;
import es.us.isa.ppinot.diagram2xml.Diagram2PPINotConverter;
import es.us.isa.ppinot.diagram2xml.PPINotFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * PPINOTModel2XmlConverter
 *
 * @author resinas
 */
public class PPINOTModel2XmlConverter implements Model2XmlConverter {

    private static final Logger log = Logger.getLogger(PPINOTModel2XmlConverter.class.getName());

    private String bpmn20XsdPath;

    public PPINOTModel2XmlConverter() {
        super();
        Diagram2BpmnConverter.setConstants(new Constants() {
            @Override
            public List<Class<? extends AbstractBpmnFactory>> getAdditionalFactoryClasses() {
                return new ArrayList<Class<? extends AbstractBpmnFactory>>();
            }

            @Override
            public List<Class<? extends PropertyListItem>> getAdditionalPropertyItemClasses() {
                return new ArrayList<Class<? extends PropertyListItem>>();
            }

            @Override
            public Map<String, String> getCustomNamespacePrefixMappings() {
                Map<String, String> n = new HashMap<String, String>();
                n.put("http://www.isa.us.es/ppinot", "ppinot");

                return n;
            }
        });
    }

    public PPINOTModel2XmlConverter(String bpmn20XsdPath) {
        this();
        this.bpmn20XsdPath = bpmn20XsdPath;
    }

    public void setBpmn20Xsd(Resource bpmn20Xsd) {
        try {
            this.bpmn20XsdPath = bpmn20Xsd.getFile().getAbsolutePath();
        } catch (IOException e) {
            log.severe("BPMN XSD file could not be found");
            log.severe(e.toString());
        }
    }


    @Override
    public StringWriter transformToXml(JSONObject jsonModel) {
        try {
            BasicDiagram diagram = BasicDiagramBuilder.parseJson(jsonModel);
            return transformToXml(diagram);
        } catch (JSONException e) {
            log.warning(e.toString());
            throw new RuntimeException("Error transforming json model to XML", e);
        }
    }

    @Override
    public StringWriter transformToXml(BasicDiagram diagram) {
        try {
            Diagram2BpmnConverter diagram2BpmnConverter = new Diagram2BpmnConverter(diagram, AbstractBpmnFactory.getFactoryClasses());
            Definitions def = diagram2BpmnConverter.getDefinitionsFromDiagram();
            Diagram2PPINotConverter ppiNotConverter = new Diagram2PPINotConverter(diagram, diagram2BpmnConverter, new PPINotFactory());
            ppiNotConverter.transform();

            Bpmn2XmlConverter xmlConverter = new Bpmn2XmlConverter(def, bpmn20XsdPath);
            return xmlConverter.getXml();
        } catch(Exception e) {
            log.warning(e.toString());
            throw new RuntimeException("Error transforming json model to XML", e);
        }
    }
}
