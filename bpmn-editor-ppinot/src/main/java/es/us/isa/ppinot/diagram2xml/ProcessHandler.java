package es.us.isa.ppinot.diagram2xml;

import es.us.isa.ppinot.xml.TPpiset;

import java.util.List;
import java.util.Map;

/**
 * ProcessHandler
 *
 * @author resinas
 */
public interface ProcessHandler {
    String getProcessIdOfElement(Object element);

    List<String> getProcessIds();

    Object getProcessElement(String id);

    void addPPISet(Map<String, TPpiset> ppisetMap);
}
