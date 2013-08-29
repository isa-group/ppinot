package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.xml.TAppliesToElementConnector;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TStateConditionMeasure;

/**
 * XMLToStateConditionMeasure
 *
 * @author resinas
 */
public class XMLToStateConditionMeasure implements XMLToMeasureDefinition<TStateConditionMeasure> {
    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        TAppliesToElementConnector connector = connectors.findConnector(xml, TAppliesToElementConnector.class);
        StateCondition condition = connectors.createStateCondition(connector);

        return new StateConditionMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                condition);
    }

}
