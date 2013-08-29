package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.xml.*;

/**
 * AggregatedMeasureToXML
 *
 * @author resinas
 */
public class AggregatedMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<AggregatedMeasure> {
    private MeasureDefinitionToXML<MeasureDefinition> converter;

    public AggregatedMeasureToXML(MeasureDefinitionToXML<MeasureDefinition> converter) {
        this.converter = converter;
    }

    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TAggregatedMeasure xml = new TAggregatedMeasure();
        loadBaseAttributes(measure, xml);

        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) measure;
        xml.setAggregationFunction(aggregatedMeasure.getAggregationFunction());
        xml.setSamplingFrequency(aggregatedMeasure.getSamplingFrequency());

        TMeasure aggregated = converter.create(aggregatedMeasure.getBaseMeasure(), connectors);

        addAggregated(xml, aggregated);

        if (aggregatedMeasure.getGroupedBy() != null) {
            connectors.addGroupedBy(aggregatedMeasure.getGroupedBy(), aggregated);
        }

        return xml;
    }

    private void addAggregated(TAggregatedMeasure xml, TMeasure aggregated) {
        if (aggregated instanceof TTimeMeasure) {
            xml.setTimeMeasure((TTimeMeasure) aggregated);
        }
        else if (aggregated instanceof TCountMeasure) {
            xml.setCountMeasure((TCountMeasure) aggregated);
        }
        else if (aggregated instanceof TDataMeasure) {
            xml.setDataMeasure((TDataMeasure) aggregated);
        }
        else if (aggregated instanceof TStateConditionMeasure) {
            xml.setStateConditionMeasure((TStateConditionMeasure) aggregated);
        }
        else if (aggregated instanceof TDataPropertyConditionMeasure) {
            xml.setDataPropertyConditionMeasure((TDataPropertyConditionMeasure) aggregated);
        }
        else if (aggregated instanceof TDerivedSingleInstanceMeasure) {
            xml.setDerivedSingleInstanceMeasure((TDerivedSingleInstanceMeasure) aggregated);
        }
    }
}
