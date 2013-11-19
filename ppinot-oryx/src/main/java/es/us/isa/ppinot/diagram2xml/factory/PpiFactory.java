package es.us.isa.ppinot.diagram2xml.factory;

import de.hpi.bpmn2_0.annotations.StencilId;
import es.us.isa.ppinot.xml.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@StencilId({"ppi"})
public class PpiFactory extends AbstractPPINotFactory {

    private static final Logger log = Logger.getLogger(PpiFactory.class.toString());

    @StencilId("ppi")
	public JAXBElement<TPpi> createPpi(GenericShape shape) {

        TPpi ppi = new TPpi();
        try {
            ppi.setId(shape.getResourceId());
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in id" + shape.getResourceId(), e);
        }

        try {
            ppi.setName(shape.getProperty("name"));
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in name " + shape.getResourceId(), e);
        }

        try {
            addTarget(ppi, shape);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in Target " + shape.getResourceId(), e);
        }

        try {
            addGoals(ppi, shape);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in goals " + shape.getResourceId(), e);
        }

        try {
            addScope(ppi, shape);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in scope " + shape.getResourceId(), e);
        }

        try {
            ppi.setResponsible(valueOrNull(shape.getProperty("responsible")));
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in responsible " + shape.getResourceId(), e);
        }

        try {
            addInformed(ppi, shape);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in informed " + shape.getResourceId(), e);
        }

        try {
            ppi.setComments(valueOrNull(shape.getProperty("comments")));
        } catch (Exception e) {
            log.log(Level.WARNING, "Error converting PPI from JSON to model in comments " + shape.getResourceId(), e);
        }

        return factory.createPpi(ppi);
    }

    private void addTarget(TPpi ppi, GenericShape shape) {
        JSONObject jsSimpleTarget = getFirstObject(shape.getPropertyJsonObject("target"));
        if (jsSimpleTarget != null) {
            TPpi.Target target = new TPpi.Target();
            TSimpleTarget simpleTarget = new TSimpleTarget();

            double lowerBound = jsSimpleTarget.optDouble("lowerBound");
            if (! Double.isNaN(lowerBound)) {
                simpleTarget.setLowerBound(lowerBound);
            }

            double upperBound = jsSimpleTarget.optDouble("upperBound");
            if (!Double.isNaN(upperBound)) {
                simpleTarget.setUpperBound(upperBound);
            }

            target.setBaseTarget(factory.createSimpleTarget(simpleTarget));
            ppi.setTarget(target);
        }

    }

    private void addScope(TPpi ppi, GenericShape shape) {
        JAXBElement<? extends TScope> scopeElement;

        try {
            scopeElement = getLastInstancesFilter(shape);

            if (scopeElement == null) {
                scopeElement = getTimeFilter(shape);
            }

            if (scopeElement != null) {
                TPpi.Scope scope = new TPpi.Scope();
                scope.setProcessInstanceFilter(scopeElement);
                ppi.setScope(scope);
            }
        } catch (Exception e) {
            log.warning(e.toString());
        }

    }

    private JAXBElement<? extends TScope> getTimeFilter(GenericShape shape) {
        JAXBElement<TSimpleTimeFilter> element = null;
        if (shape.hasProperty("timescope")) {

            JSONObject time = getFirstObject(shape.getPropertyJsonObject("timescope"));
            if (time != null) {
                TSimpleTimeFilter filter = new TSimpleTimeFilter();

                BigInteger frequencyScope = null;
                try {
                    frequencyScope = BigInteger.valueOf(time.getInt("frequencyScope"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filter.setFrequency(frequencyScope);

                filter.setRelative(time.optBoolean("relativeScope"));

                TPeriod tPeriod = null;
                try {
                    tPeriod = TPeriod.fromValue(time.getString("scopePeriod"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filter.setPeriod(tPeriod);

                element = factory.createSimpleTimeFilter(filter);
            }
        }

        return element;
    }

    private JAXBElement<? extends TScope> getLastInstancesFilter(GenericShape shape) {
        JAXBElement<TLastInstancesFilter> element = null;

        if (shape.hasProperty("lastinstancesscope")) {
            Integer lastInstances = shape.getPropertyInteger("lastinstancesscope");

            if (lastInstances != null && lastInstances != 0) {
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
