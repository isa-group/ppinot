package es.us.isa.ppinot.diagram2xml;

import de.hpi.bpmn2_0.factory.BPMNElement;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.model.FlowElement;
import de.hpi.bpmn2_0.model.Process;
import de.hpi.bpmn2_0.model.extension.ExtensionElements;
import de.hpi.bpmn2_0.transformation.BPMNPrefixMapper;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;
import es.us.isa.ppinot.xml.ObjectFactory;
import es.us.isa.ppinot.xml.TPpiset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.dom.DOMResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BpmnConverterProcess
 *
 * @author resinas
 */
public class BpmnConverterProcess implements ProcessHandler {
    private Diagram2BpmnConverter bpmnConverter;

    public BpmnConverterProcess(Diagram2BpmnConverter bpmnConverter) {
        this.bpmnConverter = bpmnConverter;
    }

    @Override
    public String getProcessIdOfElement(Object element) {
        String id = "";
        if (element instanceof FlowElement) {
            Process process = ((FlowElement) element).getProcess();
            if (process != null) {
                id = process.getId();
            }
        }
        return id;
    }

    @Override
    public List<String> getProcessIds() {
        List<String> ids = new ArrayList<String>();
        for (de.hpi.bpmn2_0.model.Process p : bpmnConverter.getProcesses()) {
            ids.add(p.getId());
        }
        return ids;
    }

    @Override
    public Object getProcessElement(String id) {
        BPMNElement el = bpmnConverter.getBpmnElements().get(id);
        if (el != null) {
            return el.getNode();
        }

        return el;
    }

    @Override
    public void addPPISet(Map<String, TPpiset> ppisetMap) {
        ObjectFactory factory = new ObjectFactory();
        for (Process process : bpmnConverter.getProcesses()) {
            TPpiset ppiset = ppisetMap.get(process.getId());
            if (ppiset != null) {
                ExtensionElements ee = process.getOrCreateExtensionElements();
                JAXBElement<TPpiset> elem = factory.createPpiset(ppiset);

                DOMResult result = new DOMResult();

                try {
                    JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class, Definitions.class);
                    Marshaller m = jc.createMarshaller();
                    m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new BPMNPrefixMapper());
                    m.marshal(elem, result);
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }

                Document document = (Document) result.getNode();
                Element element = document.getDocumentElement();

                ee.getAnyExternal().add(element);
            }
        }
    }
}
