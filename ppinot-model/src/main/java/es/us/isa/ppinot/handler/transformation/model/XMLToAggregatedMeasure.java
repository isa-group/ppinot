package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.xml.TAggregatedMeasure;
import es.us.isa.ppinot.xml.TAggregates;
import es.us.isa.ppinot.xml.TIsGroupedBy;
import es.us.isa.ppinot.xml.TMeasure;
import java.util.Arrays;

/**
 * XMLToAggregatedMeasure
 *
 * @author resinas
 */
public class XMLToAggregatedMeasure implements XMLToMeasureDefinition<TAggregatedMeasure> {
    private XMLToMeasureDefinition converter;

    public XMLToAggregatedMeasure(XMLToMeasureDefinition converter) {
        this.converter = converter;
    }

    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        TAggregatedMeasure measure = (TAggregatedMeasure) xml;
        MeasureDefinition aggregates = createAggregates(measure, connectors);

        AggregatedMeasure def = new AggregatedMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                measure.getAggregationFunction(),
                measure.getSamplingFrequency(),
                aggregates
        );

        TIsGroupedBy con = connectors.findConnector(xml, TIsGroupedBy.class);
        if (con != null) {
            def.setGroupedBySelections(Arrays.asList(connectors.createDataContentSelection(con)));
        }

        return def;
    }

    private MeasureDefinition createAggregates(TAggregatedMeasure xml, ConnectorsHandler connectors) {
        MeasureDefinition aggregate = null;
        TMeasure tMeasure = getEmbeddedAggregated(xml);

        if (tMeasure == null)
            tMeasure = connectors.getTargetMeasure(connectors.findConnector(xml, TAggregates.class));

        if (tMeasure != null)
            aggregate = converter.create(tMeasure, connectors);

        return aggregate;
    }

    private TMeasure getEmbeddedAggregated(TAggregatedMeasure xml) {
        TMeasure tMeasure = xml.getTimeMeasure();
        tMeasure = (tMeasure == null) ? xml.getCountMeasure() : tMeasure;
        tMeasure = (tMeasure == null) ? xml.getDataMeasure() : tMeasure;
        tMeasure = (tMeasure == null) ? xml.getDataPropertyConditionMeasure() : tMeasure;
        tMeasure = (tMeasure == null) ? xml.getStateConditionMeasure() : tMeasure;
        tMeasure = (tMeasure == null) ? xml.getDerivedSingleInstanceMeasure() : tMeasure;

        return tMeasure;
    }
}
