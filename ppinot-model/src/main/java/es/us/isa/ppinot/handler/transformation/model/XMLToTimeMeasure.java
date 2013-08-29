package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TTimeConnector;
import es.us.isa.ppinot.xml.TTimeConnectorType;
import es.us.isa.ppinot.xml.TTimeMeasure;

import java.util.List;

/**
 * XMLToTimeMeasure
 *
 * @author resinas
 */
public class XMLToTimeMeasure implements XMLToMeasureDefinition<TTimeMeasure> {

    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        List<TTimeConnector> conns = connectors.findConnectors(xml, TTimeConnector.class);
        TimeInstantCondition from = null;
        TimeInstantCondition to = null;
        for (TTimeConnector c : conns) {
            if (TTimeConnectorType.FROM.equals(c.getType())) {
                from = connectors.createTimeCondition(c);
            } else {
                to = connectors.createTimeCondition(c);
            }
        }

        TTimeMeasure tTimeMeasure = (TTimeMeasure) xml;

        return new TimeMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                from,
                to,
                TimeMeasureType.valueOf(tTimeMeasure.getTimeMeasureType().value().toUpperCase()),
                tTimeMeasure.getSingleInstanceAggFunction());
    }

}
