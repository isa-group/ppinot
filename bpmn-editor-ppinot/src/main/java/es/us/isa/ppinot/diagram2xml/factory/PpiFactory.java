package es.us.isa.ppinot.diagram2xml.factory;

import de.hpi.bpmn2_0.annotations.StencilId;
import es.us.isa.ppinot.xml.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.List;

@StencilId({"ppi"})
public class PpiFactory extends AbstractPPINotFactory {

    @StencilId("ppi")
	public JAXBElement<TPpi> createPpi(GenericShape shape) {

        TPpi ppi = new TPpi();

        ppi.setId(shape.getResourceId());
        ppi.setName(shape.getProperty("name"));
        addTarget(ppi, shape);
        addGoals(ppi, shape);
        addScope(ppi, shape);
        ppi.setResponsible(valueOrNull(shape.getProperty("responsible")));
        addInformed(ppi, shape);
        ppi.setComments(valueOrNull(shape.getProperty("comments")));

        return factory.createPpi(ppi);
    }

    private void addTarget(TPpi ppi, GenericShape shape) {
        JSONObject jsSimpleTarget = getFirstObject(shape.getPropertyJsonObject("target"));
        if (jsSimpleTarget != null) {
            TPpi.Target target = new TPpi.Target();
            TSimpleTarget simpleTarget = new TSimpleTarget();
            try {
                if (jsSimpleTarget.has("lowerBound")) {
                    simpleTarget.setLowerBound(jsSimpleTarget.getDouble("lowerBound"));
                }

                if (jsSimpleTarget.has("upperBound")) {
                    simpleTarget.setUpperBound(jsSimpleTarget.getDouble("upperBound"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            target.setBaseTarget(factory.createSimpleTarget(simpleTarget));
            ppi.setTarget(target);
        }

    }

    private void addScope(TPpi ppi, GenericShape shape) {
        JAXBElement<? extends TScope> scopeElement;

        scopeElement = getLastInstancesFilter(shape);

        if (scopeElement == null) {
            scopeElement = getTimeFilter(shape);
        }

        if (scopeElement != null) {
            TPpi.Scope scope = new TPpi.Scope();
            scope.setProcessInstanceFilter(scopeElement);
            ppi.setScope(scope);
        }

    }

    private JAXBElement<? extends TScope> getTimeFilter(GenericShape shape) {
        JAXBElement<TSimpleTimeFilter> element = null;
        if (shape.hasProperty("timeScope")) {

            JSONObject time = getFirstObject(shape.getPropertyJsonObject("timeScope"));
            if (time != null) {
                TSimpleTimeFilter filter = new TSimpleTimeFilter();
                try {
                    filter.setFrequency(BigInteger.valueOf(time.getInt("frequencyScope")));
                    filter.setRelative(time.getBoolean("relativeScope"));
                    filter.setPeriod(TPeriod.fromValue(time.getString("scopePeriod")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                element = factory.createSimpleTimeFilter(filter);
            }
        }

        return element;
    }

    private JAXBElement<? extends TScope> getLastInstancesFilter(GenericShape shape) {
        JAXBElement<TLastInstancesFilter> element = null;

        if (shape.hasProperty("lastInstancesScope")) {
            int lastInstances = shape.getPropertyInteger("lastInstancesScope");

            if (lastInstances != 0) {
                TLastInstancesFilter filter = new TLastInstancesFilter();
                filter.setNumberOfInstances(BigInteger.valueOf(lastInstances));
                element = factory.createLastInstancesFilter(filter);
            }

        }

        return element;
    }

    private void addGoals(TPpi ppi, GenericShape shape) {
        List<String> goalsList = getStringList(shape.getPropertyJsonObject("goals"), "goal");
        if (!goalsList.isEmpty()) {
            TPpi.Goals goals = new TPpi.Goals();
            goals.getGoal().addAll(goalsList);

            ppi.setGoals(goals);
        }
    }

    private void addInformed(TPpi ppi, GenericShape shape) {
        List<String> informedList = getStringList(shape.getPropertyJsonObject("informed"), "inform");
        if (!informedList.isEmpty()) {
            TPpi.Informed informed = new TPpi.Informed();
            informed.getInform().addAll(informedList);

            ppi.setInformed(informed);
        }
    }

}
