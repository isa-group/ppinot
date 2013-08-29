package es.us.isa.ppinot.diagram2xml.factory;

import de.hpi.bpmn2_0.annotations.StencilId;
import es.us.isa.ppinot.xml.*;
import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;

/**
 * ConnectorFactory
 *
 * @author resinas
 */
public class ConnectorFactory extends AbstractPPINotFactory {

    private void setAppliesToElementProperties(TAppliesToElementConnector connector, GenericShape shape) {
        String state = shape.getProperty("when");
        if ("Other".equals(state)) {
            state = shape.getProperty("state");
        }
        connector.setState(state);
    }

    @StencilId("TimeConnector")
    public JAXBElement<TTimeConnector> createTimeConnector(GenericShape shape) {
        TTimeConnector timeConnector = new TTimeConnector();

        timeConnector.setId(shape.getResourceId());
        setAppliesToElementProperties(timeConnector, shape);
        String conditionType = shape.getProperty("ConditionType");
        if (!notEmpty(conditionType)) {
            conditionType = shape.getProperty("conditiontype");
        }

        if (notEmpty(conditionType)) {
            timeConnector.setType(TTimeConnectorType.valueOf(conditionType.toUpperCase()));
        }

        return factory.createTimeConnector(timeConnector);
    }

    @StencilId("appliesToElementConnector")
    public JAXBElement<TAppliesToElementConnector> createAppliesToElementConnector(GenericShape shape) {
        TAppliesToElementConnector appliesToElementConnector = new TAppliesToElementConnector();

        appliesToElementConnector.setId(shape.getResourceId());
        setAppliesToElementProperties(appliesToElementConnector, shape);

        return factory.createAppliesToElementConnector(appliesToElementConnector);
    }

    @StencilId("appliesToDataConnector")
    public JAXBElement<TAppliesToDataConnector> createAppliesToDataConnector(GenericShape shape) {
        TAppliesToDataConnector appliesToDataConnector = new TAppliesToDataConnector();

        appliesToDataConnector.setId(shape.getResourceId());
        appliesToDataConnector.setRestriction(valueOrNull(shape.getProperty("restriction")));
        appliesToDataConnector.setStates(valueOrNull(shape.getProperty("state")));
        appliesToDataConnector.setDataContentSelection(valueOrNull(shape.getProperty("dataContentSelection")));

        return factory.createAppliesToDataConnector(appliesToDataConnector);
    }

    @StencilId("aggregates")
    public JAXBElement<TAggregates> createAggregates(GenericShape shape) {
        TAggregates aggregates = new TAggregates();
        aggregates.setId(shape.getResourceId());
        return factory.createAggregates(aggregates);
    }

    @StencilId("isGroupedBy")
    public JAXBElement<TIsGroupedBy> createIsGroupedBy(GenericShape shape) {
        TIsGroupedBy isGroupedBy = new TIsGroupedBy();

        isGroupedBy.setId(shape.getResourceId());
        isGroupedBy.setDataContentSelection(shape.getProperty("dataContentSelection"));

        return factory.createIsGroupedBy(isGroupedBy);
    }

    @StencilId("uses")
    public JAXBElement<TUses> createUses(GenericShape shape) {
        TUses uses = new TUses();
        uses.setId(shape.getResourceId());
        uses.setVariable(shape.getProperty("variable"));
        return factory.createUses(uses);
    }

}
