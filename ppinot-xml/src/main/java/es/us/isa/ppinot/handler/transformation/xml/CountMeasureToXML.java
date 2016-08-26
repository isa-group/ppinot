package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.xml.TCountMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * CountMeasureToXML
 *
 * @author resinas
 */
public class CountMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<CountMeasure> {
    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TCountMeasure xml = new TCountMeasure();
        loadBaseAttributes(measure, xml);

        CountMeasure timeMeasure = (CountMeasure) measure;

        connectors.addTimeCondition(timeMeasure.getWhen(), xml);

        return xml;
    }
}
