package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TMeasureConnector;
import es.us.isa.ppinot.xml.TPpi;
import es.us.isa.ppinot.xml.TPpiset;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XMLToPPISet
 *
 * @author resinas
 */
public class XMLToPPISet {
    private XMLToMeasureDefinition<TMeasure> converter;

    public XMLToPPISet(XMLToMeasureDefinition<TMeasure> converter) {
        this.converter = converter;
    }

    public PPISet create(TPpiset tPpiset, String processId) {
        PPISet ppiSet = null;
        if (tPpiset != null) {
            Map<String, MeasureDefinition> measures = createMeasures(tPpiset.getMeasure(), tPpiset.getMeasureConnector());
            List<PPI> ppis = createPPIs(tPpiset.getPpi(), measures);

            ppiSet = new PPISet(processId, ppis, measures.values());
        }

        return ppiSet;
    }

    private Map<String, MeasureDefinition> createMeasures(List<JAXBElement<? extends TMeasure>> tMeasures, List<JAXBElement<? extends TMeasureConnector>> measureConnectors) {
        ConnectorsHandler connectorsHandler = new ConnectorsHandler(measureConnectors);
        Map<String, MeasureDefinition> measures = new HashMap<String, MeasureDefinition>();

        for (JAXBElement<? extends TMeasure> m : tMeasures) {
            String id = m.getValue().getId();
            if (measures.get(id) == null) {
                measures.put(id, converter.create(m.getValue(), connectorsHandler));
            }
        }

        return measures;
    }

    private List<PPI> createPPIs(List<TPpi> tPpis, Map<String, MeasureDefinition> measures) {
        List<PPI> ppis = new ArrayList<PPI>();
        XMLToPPI conversor = new XMLToPPI();
        for (TPpi ppi : tPpis) {
            ppis.add(conversor.create(ppi, measures));
        }

        return ppis;
    }
}
