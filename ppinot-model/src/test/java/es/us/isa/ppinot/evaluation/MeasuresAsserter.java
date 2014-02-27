package es.us.isa.ppinot.evaluation;

import org.junit.Assert;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    public void assertValueOfInterval(int measure,double value) {
    	Assert.assertEquals(value,((Measure)((List)measures).get(measure)).value,0.0);
    }
    
    public void assertNumOfInstances(int value) {
    	boolean check = true;
    	Set<String> instances=new HashSet<String>();
    	for (Measure m: measures) {
            instances.addAll(m.getInstances());
        }
    	
    	Assert.assertEquals(value, instances.size());
    }
    
    public void assertNumOfMeasureCero() {
    	boolean check = true;
    	for (Measure m: measures) {
            if (m.value!=0) {
            	check=false;
            }
        }
    	
    	Assert.assertTrue(check);
    }


}
