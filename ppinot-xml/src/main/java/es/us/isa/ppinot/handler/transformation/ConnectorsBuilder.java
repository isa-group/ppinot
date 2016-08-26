package es.us.isa.ppinot.handler.transformation;

import es.us.isa.ppinot.handler.json.StateTransformer;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.xml.*;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;

/**
 * ConnectorsHandler
 *
 * @author resinas
 */
public class ConnectorsBuilder {

    private List<JAXBElement<? extends TMeasureConnector>> connectors;
    private ObjectFactory factory;
    private Map<String, Object> elementIds;

    public ConnectorsBuilder(List<JAXBElement<? extends TMeasureConnector>> measureConnector,
                             ObjectFactory factory, Map<String, Object> elementIds) {
        connectors = measureConnector;
        this.factory = factory;
        this.elementIds = elementIds;
    }

    public TAppliesToElementConnector addTimeCondition(TimeInstantCondition condition, TMeasure measure) {
        TAppliesToElementConnector connector = null;
        if (condition != null) {
            connector = factory.createTAppliesToElementConnector();
            connector.setSourceRef(measure);
            connector.setTargetRef(elementIds.get(condition.getAppliesTo()));
            connector.setState(condition.getChangesToState().toString());

            connectors.add(factory.createAppliesToElementConnector(connector));
        }

        return connector;
    }

    public TTimeConnector addTimeCondition(TimeInstantCondition condition, TTimeConnectorType type, TMeasure measure) {
        TTimeConnector connector = null;
        if (condition != null) {
            connector = factory.createTTimeConnector();
            connector.setSourceRef(measure);
            connector.setTargetRef(elementIds.get(condition.getAppliesTo()));
            connector.setState(condition.getChangesToState().toString());
            connector.setType(type);

            connectors.add(factory.createTimeConnector(connector));
        }

        return connector;
    }

    public TAppliesToElementConnector addStateCondition(StateCondition condition, TMeasure measure) {
        TAppliesToElementConnector connector = null;
        if (condition != null) {
            connector = factory.createTAppliesToElementConnector();
            connector.setSourceRef(measure);
            connector.setTargetRef(elementIds.get(condition.getAppliesTo()));
            connector.setState(condition.getState().toString());

            connectors.add(factory.createAppliesToElementConnector(connector));
        }

        return connector;

    }

    public TAppliesToDataConnector addDataCondition(DataPropertyCondition condition, TMeasure measure) {
        TAppliesToDataConnector connector = null;
        if (condition != null) {
            connector = factory.createTAppliesToDataConnector();
            connector.setSourceRef(measure);
            connector.setTargetRef(elementIds.get(condition.getAppliesTo()));
            connector.setRestriction(condition.getRestriction());
            connector.setStates(StateTransformer.transformDataState(condition.getStatesConsidered()));

            connectors.add(factory.createAppliesToDataConnector(connector));
        }

        return connector;
    }

    public TAppliesToDataConnector addDataCondition(DataPropertyCondition condition, DataContentSelection selection, TMeasure measure) {
        TAppliesToDataConnector connector = addDataCondition(condition, measure);
        if (connector != null && selection != null) {
            connector.setDataContentSelection(selection.getSelection());
        }
        return connector;
    }

    public TIsGroupedBy addGroupedBy(DataContentSelection selection, TMeasure measure) {
        TIsGroupedBy connector = null;
        if (selection != null) {
            connector = factory.createTIsGroupedBy();
            connector.setSourceRef(measure);
            connector.setTargetRef(elementIds.get(selection.getDataobjectId()));
            connector.setDataContentSelection(selection.getSelection());

            connectors.add(factory.createIsGroupedBy(connector));
        }

        return connector;
    }

    public TUses addUses(String variable, TMeasure used, TMeasure user) {
        TUses connector = null;
        if (used != null) {
            connector = factory.createTUses();
            connector.setSourceRef(user);
            connector.setTargetRef(used);
            connector.setVariable(variable);

            connectors.add(factory.createUses(connector));
        }

        return connector;

    }

}
