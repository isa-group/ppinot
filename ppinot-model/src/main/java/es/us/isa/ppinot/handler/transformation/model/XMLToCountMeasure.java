package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.xml.TAppliesToElementConnector;
import es.us.isa.ppinot.xml.TCountMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * XMLToCountMeasure
 *
 * @author resinas
 */
public class XMLToCountMeasure implements XMLToMeasureDefinition<TCountMeasure> {
    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        TAppliesToElementConnector connector = connectors.findConnector(xml, TAppliesToElementConnector.class);
        TimeInstantCondition condition = connectors.createTimeCondition(connector);

        return new CountMeasure(
                    xml.getId(),
                    xml.getName(),
                    xml.getDescription(),
                    xml.getScale(),
                    xml.getUnitOfMeasure(),
                    condition);
    }
}

