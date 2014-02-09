package es.us.isa.ppinot.evaluation;

import org.junit.Assert;

import java.util.Collection;
import java.util.List;

/**
 * MeasuresAsserter
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public class MeasuresAsserter {
    private Collection<? extends Measure> measures;

    public MeasuresAsserter(List<? extends Measure> measures) {
        this.measures = measures;
    }

    public void assertTheNumberOfMeasuresIs(int n) {
        Assert.assertEquals(n, measures.size());
    }

    public void assertInstanceHasValue(String instance, double value) {
        boolean found = false;

        for (Measure m: measures) {
            if (m.getInstances().contains(instance)) {
                Assert.assertEquals(value, m.getValue(), 0);
                found = true;
                break;
            }
        }

        Assert.assertTrue("Instance " + instance + " has not been evaluated", found);
    }


}
