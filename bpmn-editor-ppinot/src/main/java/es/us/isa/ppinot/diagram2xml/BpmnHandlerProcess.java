package es.us.isa.ppinot.diagram2xml;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExtensionElements;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.xml.ObjectFactory;
import es.us.isa.ppinot.xml.TPpiset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BpmnHandlerProcess
 *
 * @author resinas
 */
public class BpmnHandlerProcess implements ProcessHandler {
    private Bpmn20ModelHandler handler;

    public BpmnHandlerProcess(Bpmn20ModelHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getProcessIdOfElement(Object element) {
        String id = "";
        if (element instanceof TBaseElement) {
            String targetId = ((TBaseElement) element).getId();
            id =handler.getProcessOfElement(targetId).getId();
        }
        return id;
    }

    @Override
    public List<String> getProcessIds() {
        List<String> ids = new ArrayList<String>();
        for (TProcess p : handler.getProcesses()) {
            ids.add(p.getId());
        }

        return ids;
    }

    @Override
    public Object getProcessElement(String id) {
        return handler.getElement(id);
    }

    @Override
    public void addPPISet(Map<String, TPpiset> ppisetMap) {
        ObjectFactory factory = new ObjectFactory();
        for (TProcess process : handler.getProcesses()) {
            TPpiset ppiset = ppisetMap.get(process.getId());
            if (ppiset != null) {
                TExtensionElements elements = process.getExtensionElements();
                if (elements == null) {
                    elements = new TExtensionElements();
                    process.setExtensionElements(elements);
                }
                elements.getAny().add(factory.createPpiset(ppiset));
            }
        }
    }
}
