package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.PPISet;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * AggregatedTest
 *
 * @author resinas
 */
public class AggregatedTest {


    private PPINotModelHandler handler;
    private PPISet ppiSet;

    @Test
    public void shouldLoadAggregatedMeasures() throws Exception {
        load("aggregated.bpmn20.xml");

        numberOfPPISets(1);

        ppiSet = handler.getPPISet("sid-95218261-1cbe-4943-81dd-e5b0357e2ff0");

        numberOfPPIs(0);
        numberOfMeasures(5);

    }

    private void numberOfMeasures(int n) {
        Assert.assertEquals(n, ppiSet.getMeasures().size());
    }

    private void numberOfPPIs(int n) {
        Assert.assertEquals(n, ppiSet.getPpis().size());
    }

    private void load(String processName) throws Exception {
        InputStream sourceStream = getClass().getResourceAsStream(processName);
        handler = new PPINotModelHandlerImpl();
        handler.load(sourceStream);
    }

    private void numberOfPPISets(int n) {
        Assert.assertEquals(n, handler.getPPISets().size());
    }
}
