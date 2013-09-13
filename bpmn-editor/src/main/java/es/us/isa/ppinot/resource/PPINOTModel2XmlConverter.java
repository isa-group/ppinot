package es.us.isa.ppinot.resource;

import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import es.us.isa.bpms.model.BPMNModel2XmlConverter;
import es.us.isa.ppinot.diagram2xml.Diagram2PPINotConverter;
import es.us.isa.ppinot.diagram2xml.PPINotFactory;
import org.oryxeditor.server.diagram.basic.BasicDiagram;

import java.util.logging.Logger;

/**
 * PPINOTModel2XmlConverter
 *
 * @author resinas
 */
public class PPINOTModel2XmlConverter extends BPMNModel2XmlConverter {

    private static final Logger log = Logger.getLogger(PPINOTModel2XmlConverter.class.getName());

    public PPINOTModel2XmlConverter() {
        super();
        Diagram2BpmnConverter.setConstants(Diagram2PPINotConverter.getConstants());
    }

    public PPINOTModel2XmlConverter(String bpmn20XsdPath) {
        super(bpmn20XsdPath);
        Diagram2BpmnConverter.setConstants(Diagram2PPINotConverter.getConstants());
    }

    @Override
    protected void extendedTransformToXml(BasicDiagram diagram, Diagram2BpmnConverter diagram2BpmnConverter) {
        try {
            Diagram2PPINotConverter ppiNotConverter = new Diagram2PPINotConverter(diagram, diagram2BpmnConverter, new PPINotFactory());
            ppiNotConverter.transform();
        } catch (Exception e) {
            log.warning(e.toString());
            throw new RuntimeException("Error transforming PPIs of json model to XML", e);
        }
    }
}
