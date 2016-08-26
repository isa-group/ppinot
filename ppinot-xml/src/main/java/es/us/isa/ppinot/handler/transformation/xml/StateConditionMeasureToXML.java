package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TStateConditionMeasure;

/**
 * StateConditionMeasureToXML
 *
 * @author resinas
 */
public class StateConditionMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<StateConditionMeasure> {
    @Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        TStateConditionMeasure xml = new TStateConditionMeasure();
        loadBaseAttributes(measure, xml);

        StateConditionMeasure stateConditionMeasure = (StateConditionMeasure) measure;

        connectors.addStateCondition(stateConditionMeasure.getCondition(), xml);

        return xml;
    }
}
