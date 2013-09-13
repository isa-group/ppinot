package es.us.isa.bpms.model;

import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.transformation.Bpmn2XmlConverter;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * BPMNModel2XmlConverter
 *
 * @author resinas
 */
public abstract class BPMNModel2XmlConverter implements Model2XmlConverter {
    private static final Logger log = Logger.getLogger(BPMNModel2XmlConverter.class.toString());

    protected String bpmn20XsdPath;

    public BPMNModel2XmlConverter() {
        super();
    }

    public BPMNModel2XmlConverter(String bpmn20XsdPath) {
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

            extendedTransformToXml(diagram, diagram2BpmnConverter);

            Bpmn2XmlConverter xmlConverter = new Bpmn2XmlConverter(def, bpmn20XsdPath);
            return xmlConverter.getXml();
        } catch(Exception e) {
            log.warning(e.toString());
            throw new RuntimeException("Error transforming json model to XML", e);
        }
    }

    protected abstract void extendedTransformToXml(BasicDiagram diagram, Diagram2BpmnConverter diagram2BpmnConverter);
}
