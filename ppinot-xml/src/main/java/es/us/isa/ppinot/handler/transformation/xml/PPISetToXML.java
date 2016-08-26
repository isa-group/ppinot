package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.xml.ObjectFactory;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TPpi;
import es.us.isa.ppinot.xml.TPpiset;

import javax.xml.bind.JAXBElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PPISetToXML
 *
 * @author resinas
 */
public class PPISetToXML {

    private MeasureDefinitionToXML<MeasureDefinition> converter;
    private Map<String, Object> elementIds;
    private ObjectFactory factory;

    public PPISetToXML(MeasureDefinitionToXML<MeasureDefinition> converter, Map<String, Object> elementIds, ObjectFactory factory) {
        this.converter = converter;
        this.elementIds = elementIds;
        this.factory = factory;
    }

    public TPpiset create(PPISet ppiSet) {
        TPpiset xml = null;
        if (ppiSet != null) {
            xml = new TPpiset();
            ConnectorsBuilder connectors = new ConnectorsBuilder(xml.getMeasureConnector(), factory, elementIds);
            Map<MeasureDefinition, TMeasure> measureMap = createMeasures(xml.getMeasure(), ppiSet.getMeasures(), connectors);
            createPPIs(xml.getPpi(), ppiSet.getPpis(), measureMap);
        }

        return xml;
    }

    private Map<MeasureDefinition, TMeasure> createMeasures(List<JAXBElement<? extends TMeasure>> tMeasures, List<MeasureDefinition> measures, ConnectorsBuilder connectors) {
        Map<MeasureDefinition, TMeasure> measureMap = new HashMap<MeasureDefinition, TMeasure>();
        if (measures != null) {
            for (MeasureDefinition m : measures) {
                TMeasure tMeasure = converter.create(m, connectors);
                measureMap.put(m, tMeasure);
                tMeasures.add(factory.createMeasure(tMeasure));
            }
        }
        return measureMap;
    }

    private void createPPIs(List<TPpi> tPpis, List<PPI> ppis, Map<MeasureDefinition, TMeasure> measureMap) {
        if (ppis != null) {
            PPIToXML ppiToXML = new PPIToXML(factory);
            for (PPI p : ppis) {
                tPpis.add(ppiToXML.create(p, measureMap));
            }
        }
    }

}
