package es.us.isa.ppinot.evaluation;

import es.us.isa.ppinot.evaluation.logs.MXMLLog;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.scope.LastInstancesFilter;
import es.us.isa.ppinot.model.state.GenericState;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * MXMLEvaluatorTest
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MXMLEvaluatorTest {
    @Test
    public void testEval() throws Exception {
        PPIEvaluator evaluator = new MXMLEvaluator(getClass().getResourceAsStream("rfc_log1.mxml"),null);

        CountMeasure count = new CountMeasure();
        count.setWhen(new TimeInstantCondition("Elevate decision to committee", GenericState.START));

        AggregatedMeasure measure = new AggregatedMeasure();
        measure.setAggregationFunction(Aggregator.AVG);
        measure.setBaseMeasure(count);

        PPI ppi = new PPI();
        ppi.setTarget(new Target(0.0, 1.0));
        ppi.setScope(new LastInstancesFilter(7));
        ppi.setMeasuredBy(measure);

        Collection<Evaluation> ev= evaluator.eval(ppi);

        Assert.assertEquals(1, ev.size());

        for (Evaluation e : ev) {
            Assert.assertEquals("Value", 1.0 / 7.0, e.getValue(), 0.0);
            Assert.assertTrue("Success", e.success());
            Assert.assertEquals("Number of instances", 7, e.getScope().getInstances().size());
        }
    }

    @Test
    public void testSeveralEval() throws Exception {
        PPIEvaluator evaluator = new MXMLEvaluator(getClass().getResourceAsStream("rfc_log1.mxml"),null);

        CountMeasure count = new CountMeasure();
        count.setWhen(new TimeInstantCondition("Elevate decision to committee", GenericState.START));

        AggregatedMeasure measure = new AggregatedMeasure();
        measure.setAggregationFunction(Aggregator.AVG);
        measure.setBaseMeasure(count);

        PPI ppi = new PPI();
        ppi.setTarget(new Target(0.0, 1.0));
        ppi.setScope(new LastInstancesFilter(5));
        ppi.setMeasuredBy(measure);

        Collection<Evaluation> ev= evaluator.eval(ppi);

        Assert.assertEquals(3, ev.size());

        Evaluation[] evaluations = ev.toArray(new Evaluation[]{});

        Assert.assertEquals("Value", 0.0, evaluations[0].getValue(), 0.0);
        Assert.assertEquals("Value", 0.0, evaluations[1].getValue(), 0.0);
        Assert.assertEquals("Value", 1.0 / 5.0, evaluations[2].getValue(), 0.0);

        for (Evaluation e : ev) {
            Assert.assertTrue("Success", e.success());
            Assert.assertEquals("Number of instances", 5, e.getScope().getInstances().size());
        }
    }

}
