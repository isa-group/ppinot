package es.us.isa.ppinot.handler.transformation;

import es.us.isa.bpmn.xmlClasses.bpmn20.TFlowElement;
import es.us.isa.ppinot.handler.json.StateTransformer;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.xml.*;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * ConnectorsHandler
 *
 * @author resinas
 */
public class ConnectorsHandler {

    private List<JAXBElement<? extends TMeasureConnector>> connectors;
    private boolean useName = false;

    public ConnectorsHandler(List<JAXBElement<? extends TMeasureConnector>> measureConnector) {
        connectors = measureConnector;
    }

    public ConnectorsHandler(List<JAXBElement<? extends TMeasureConnector>> measureConnector, boolean useName) {
        this(measureConnector);
        this.useName = useName;
    }

    public <T extends TMeasureConnector> T findConnector(TMeasure measure, Class<T> clazz) {
        T connector = null;
        for( JAXBElement<? extends TMeasureConnector> element : connectors ) {
            TMeasureConnector conn = element.getValue();
            if(clazz.isInstance(conn) &&
                    conn.getSourceRef() != null &&
                    conn.getSourceRef().equals(measure)) {
                connector = (T) conn;
                break;
            }
        }
        return connector;
    }

    public <T extends TMeasureConnector> List<T> findConnectors(TMeasure measure, Class<T> clazz) {
        List<T> connector = new ArrayList<T>();
        for (JAXBElement<? extends TMeasureConnector> element : connectors) {
            TMeasureConnector conn = element.getValue();
            if (clazz.isInstance(conn) &&
                    conn.getSourceRef() != null &&
                    conn.getSourceRef().equals(measure)) {
                connector.add((T) conn);
            }
        }
        return connector;
    }

    public TimeInstantCondition createTimeCondition(TAppliesToElementConnector connector) {
        TimeInstantCondition condition = null;
        if (connector!=null) {
            condition = new TimeInstantCondition(
                    getTargetElement(connector),
                    StateTransformer.transform(connector.getState()));
        }
        return condition;
    }

    public StateCondition createStateCondition(TAppliesToElementConnector connector) {
        StateCondition condition = null;
        if (connector != null) {
            condition = new StateCondition(
                    getTargetElement(connector),
                    StateTransformer.transform(connector.getState())
            );
        }

        return condition;
    }

    public DataPropertyCondition createDataCondition(TAppliesToDataConnector connector) {
        DataPropertyCondition condition = null;
        if (connector != null) {
            condition = new DataPropertyCondition(
                    getTargetElement(connector),
                    connector.getRestriction(),
                    StateTransformer.transformDataState(connector.getStates())
            );
        }

        return condition;
    }

    public DataContentSelection createDataContentSelection(TAppliesToDataConnector connector) {
        DataContentSelection selection = null;
        if (connector != null) {
            selection = new DataContentSelection(
                    connector.getDataContentSelection(),
                    getTargetElement(connector));
        }
        return selection;
    }

    public DataContentSelection createDataContentSelection(TIsGroupedBy connector) {
        DataContentSelection selection = null;
        if (connector != null) {
            selection = new DataContentSelection(
                    connector.getDataContentSelection(),
                    getTargetElement(connector));
        }
        return selection;
    }

    private String getTargetElement(TMeasureConnector connector) {
        String targetElement;
        TFlowElement targetRef = (TFlowElement) connector.getTargetRef();
        if (useName) {
            targetElement = targetRef.getName();
        } else {
            targetElement = targetRef.getId();
        }
        return targetElement;
    }

    public TMeasure getTargetMeasure(TMeasureConnector connector) {
        TMeasure tMeasure = null;
        if (connector != null) {
            Object target = connector.getTargetRef();
            if (target instanceof JAXBElement<?>) {
                JAXBElement<?> elem = (JAXBElement<?>) connector.getTargetRef();
                target = elem.getValue();
            }
            if (target instanceof TMeasure) {
                tMeasure = (TMeasure) target;
            }
        }
        return tMeasure;
    }

}
