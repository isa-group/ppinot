package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.xml.TDerivedMultiInstanceMeasure;
import es.us.isa.ppinot.xml.TMeasure;

import java.util.Map;

/**
 * DerivedMultiInstanceMeasureToXML
 *
 * @author resinas
 */
public class DerivedMultiInstanceMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<DerivedMultiInstanceMeasure> {
    private MeasureDefinitionToXML<MeasureDefinition> converter;

    public DerivedMultiInstanceMeasureToXML(MeasureDefinitionToXML<MeasureDefinition> converter) {
        this.converter = converter;
    }

    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TDerivedMultiInstanceMeasure xml = new TDerivedMultiInstanceMeasure();
        loadBaseAttributes(measure, xml);

        DerivedMultiInstanceMeasure derivedMultiInstanceMeasure = (DerivedMultiInstanceMeasure) measure;

        Map<String, MeasureDefinition> uses = derivedMultiInstanceMeasure.getUsedMeasureMap();

        for (String var : uses.keySet()) {
            TMeasure used = converter.create(uses.get(var), connectors);
            connectors.addUses(var, used, xml);
        }

        return xml;
    }
}
