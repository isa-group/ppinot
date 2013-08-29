package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xml.TDerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xml.TMeasure;

import java.util.Map;

/**
 * DerivedSingleInstanceMeasureToXML
 *
 * @author resinas
 */
public class DerivedSingleInstanceMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<DerivedSingleInstanceMeasure> {
    private MeasureDefinitionToXML<MeasureDefinition> converter;

    public DerivedSingleInstanceMeasureToXML(MeasureDefinitionToXML<MeasureDefinition> converter) {
        this.converter = converter;
    }

    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TDerivedSingleInstanceMeasure xml = new TDerivedSingleInstanceMeasure();
        loadBaseAttributes(measure, xml);

        DerivedSingleInstanceMeasure derivedSingleInstanceMeasure = (DerivedSingleInstanceMeasure) measure;

        Map<String, MeasureDefinition> uses = derivedSingleInstanceMeasure.getUsedMeasureMap();

        for (String var : uses.keySet()) {
            TMeasure used = converter.create(uses.get(var), connectors);
            connectors.addUses(var, used, xml);
        }

        return xml;

    }
}
