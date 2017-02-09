package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.xml.TDataMeasure;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * DataMeasureToXML
 *
 * @author resinas
 */
public class DataMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<DataMeasure> {
    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TDataMeasure xml = new TDataMeasure();
        loadBaseAttributes(measure, xml);

        DataMeasure dataMeasure = (DataMeasure) measure;

        connectors.addDataCondition((DataPropertyCondition) dataMeasure.getPrecondition(), dataMeasure.getDataContentSelection(), xml);

        return xml;
    }
}
