package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataPropertyConditionMeasure;
import es.us.isa.ppinot.xml.TDataPropertyConditionMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * DataPropertyConditionMeasureToXML
 *
 * @author resinas
 */
public class DataPropertyConditionMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<DataPropertyConditionMeasure> {
    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TDataPropertyConditionMeasure xml = new TDataPropertyConditionMeasure();
        loadBaseAttributes(measure, xml);

        DataPropertyConditionMeasure dataPropertyConditionMeasure = (DataPropertyConditionMeasure) measure;

        connectors.addDataCondition(dataPropertyConditionMeasure.getCondition(), xml);

        return xml;
    }
}
