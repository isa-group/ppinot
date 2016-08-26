package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TTimeConnectorType;
import es.us.isa.ppinot.xml.TTimeMeasure;
import es.us.isa.ppinot.xml.TTimeMeasureType;

/**
 * TimeMeasureToXML
 *
 * @author resinas
 */
public class TimeMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<TimeMeasure> {
    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TTimeMeasure xml = new TTimeMeasure();
        loadBaseAttributes(measure, xml);

        TimeMeasure timeMeasure = (TimeMeasure) measure;
        xml.setTimeMeasureType(TTimeMeasureType.valueOf(timeMeasure.getTimeMeasureType().toString()));
        xml.setSingleInstanceAggFunction(timeMeasure.getSingleInstanceAggFunction());

        connectors.addTimeCondition(timeMeasure.getFrom(), TTimeConnectorType.FROM, xml);
        connectors.addTimeCondition(timeMeasure.getTo(), TTimeConnectorType.TO, xml);

        return xml;
    }

}
