package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.Period;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.xml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XMLToPPI
 *
 * @author resinas
 */
public class XMLToPPI {


    public PPI create(TPpi tPpi, Map<String, MeasureDefinition> measures) {
        PPI ppi = new PPI(
                tPpi.getId(),
                tPpi.getName(),
                createGoals(tPpi.getGoals()),
                tPpi.getResponsible(),
                createInformed(tPpi.getInformed()),
                tPpi.getComments(),
                createTarget(tPpi.getTarget()),
                createScope(tPpi.getScope())
        );

        ppi.setMeasuredBy(getMeasuredBy(tPpi, measures));

        return ppi;
    }

    private MeasureDefinition getMeasuredBy(TPpi tPpi, Map<String, MeasureDefinition> measures) {
        MeasureDefinition definition = null;
        Object measuredBy = tPpi.getMeasuredBy();
        if (measuredBy != null && measuredBy instanceof TMeasure) {
            definition = measures.get(((TMeasure)measuredBy).getId());
        }

        return definition;
    }

    private ProcessInstanceFilter createScope(TPpi.Scope scope) {
        ProcessInstanceFilter filter = null;
        if (scope != null) {
            Object value = scope.getProcessInstanceFilter().getValue();
            if (value instanceof TLastInstancesFilter) {
                TLastInstancesFilter f = (TLastInstancesFilter) value;
                int numberOfInstances = 0;
                if (f.getNumberOfInstances() != null) {
                    numberOfInstances = f.getNumberOfInstances().intValue();
                }

                filter = new LastInstancesFilter(numberOfInstances);
            }
            else if (value instanceof TSimpleTimeFilter) {
                TSimpleTimeFilter f = (TSimpleTimeFilter) value;
                Period period = null;
                int frequency = 0;

                if (f.getPeriod() != null)
                    period = Period.valueOf(f.getPeriod().value().toUpperCase());
                if (f.getFrequency() != null)
                    frequency = f.getFrequency().intValue();

                filter = new SimpleTimeFilter(
                        period,
                        frequency,
                        f.isRelative());
            }
            else {
                throw new RuntimeException("Scope not supported: " + value);
            }
        }

        return filter;
    }

    private List<String> createGoals(TPpi.Goals goals) {
        List<String> result = new ArrayList<String>();

        if (goals != null)
            result.addAll(goals.getGoal());

        return result;
    }

    private List<String> createInformed(TPpi.Informed informed) {
        List<String> result = new ArrayList<String>();

        if (informed != null) {
            result.addAll(informed.getInform());
        }
        return result;
    }

    private Target createTarget(TPpi.Target target) {
        Target baseTarget = null;
        if (target != null && target.getBaseTarget() != null) {
            if (! (target.getBaseTarget().getValue() instanceof TSimpleTarget)) {
                throw new RuntimeException("Target not supported: " + target.getBaseTarget().getValue());
            }
            TSimpleTarget t = (TSimpleTarget) target.getBaseTarget().getValue();

            if (t != null)
                baseTarget = new Target(t.getLowerBound(), t.getUpperBound());
        }

        return baseTarget;
    }


}
