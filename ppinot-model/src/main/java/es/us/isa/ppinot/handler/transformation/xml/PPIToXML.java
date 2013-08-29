package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.scope.SimpleTimeFilter;
import es.us.isa.ppinot.xml.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * PPIToXML
 *
 * @author resinas
 */
public class PPIToXML {

    private ObjectFactory factory;

    public PPIToXML(ObjectFactory factory) {
        this.factory = factory;
    }

    public TPpi create(PPI ppi, Map<MeasureDefinition, TMeasure> measureMap) {
        TPpi xml = new TPpi();

        xml.setId(ppi.getId());
        xml.setName(ppi.getName());
        xml.setGoals(createGoals(ppi.getGoals()));
        xml.setResponsible(ppi.getResponsible());
        xml.setInformed(createInformed(ppi.getInformed()));
        xml.setComments(ppi.getComments());
        xml.setTarget(createTarget(ppi.getTarget()));
        xml.setScope(createScope(ppi.getScope()));

        xml.setMeasuredBy(measureMap.get(ppi.getMeasuredBy()));

        return xml;
    }

    private TPpi.Goals createGoals(List<String> goals) {
        TPpi.Goals xml = null;

        if (goals != null) {
            xml = new TPpi.Goals();
            xml.getGoal().addAll(goals);
        }

        return xml;
    }

    private TPpi.Informed createInformed(List<String> informed) {
        TPpi.Informed xml = null;
        if (informed != null) {
            xml = new TPpi.Informed();
            xml.getInform().addAll(informed);
        }
        return xml;
    }

    private TPpi.Target createTarget(Target target) {
        TPpi.Target xml = null;

        if (target != null) {
            xml = new TPpi.Target();

            TSimpleTarget simpleTarget = new TSimpleTarget();
            simpleTarget.setLowerBound(target.getRefMin());
            simpleTarget.setUpperBound(target.getRefMax());

            xml.setBaseTarget(factory.createSimpleTarget(simpleTarget));
        }
        return xml;
    }

    private TPpi.Scope createScope(ProcessInstanceFilter filter) {
        TPpi.Scope xml = null;
        if (filter != null) {
            xml = new TPpi.Scope();
            TScope scope;

            if (filter instanceof LastInstancesFilter) {
                scope = createLastInstancesFilter((LastInstancesFilter) filter);
            }
            else if (filter instanceof SimpleTimeFilter) {
                scope = createSimpleTimeFilter((SimpleTimeFilter) filter);
            }
            else {
                throw new RuntimeException("Scope not supported: " + filter);
            }

            xml.setProcessInstanceFilter(factory.createProcessInstanceFilter(scope));
        }

        return xml;
    }

    private TScope createSimpleTimeFilter(SimpleTimeFilter filter) {
        TSimpleTimeFilter f = new TSimpleTimeFilter();
        f.setFrequency(BigInteger.valueOf(filter.getFrequency()));
        f.setPeriod(TPeriod.fromValue(filter.getPeriod().toString().toLowerCase()));
        f.setRelative(filter.isRelative());

        return f;
    }

    private TScope createLastInstancesFilter(LastInstancesFilter filter) {
        TLastInstancesFilter f = new TLastInstancesFilter();
        f.setNumberOfInstances(BigInteger.valueOf(filter.getNumberOfInstances()));

        return f;
    }

}
