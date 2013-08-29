package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.xml.TAppliesToDataConnector;
import es.us.isa.ppinot.xml.TDataMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * XMLToDataMeasure
 *
 * @author resinas
 */
public class XMLToDataMeasure implements XMLToMeasureDefinition <TDataMeasure> {
    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        TAppliesToDataConnector connector = connectors.findConnector(xml, TAppliesToDataConnector.class);
        DataContentSelection selection = connectors.createDataContentSelection(connector);
        DataPropertyCondition condition = connectors.createDataCondition(connector);
        return new DataMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                selection,
                condition
        );
    }
}
