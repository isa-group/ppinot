package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataPropertyConditionMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.xml.TAppliesToDataConnector;
import es.us.isa.ppinot.xml.TDataPropertyConditionMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * XMLToDataPropertyConditionMeasure
 *
 * @author resinas
 */
public class XMLToDataPropertyConditionMeasure implements XMLToMeasureDefinition<TDataPropertyConditionMeasure> {
    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        TAppliesToDataConnector connector = connectors.findConnector(xml, TAppliesToDataConnector.class);
        DataPropertyCondition condition = connectors.createDataCondition(connector);
        return new DataPropertyConditionMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                condition );
    }
}
