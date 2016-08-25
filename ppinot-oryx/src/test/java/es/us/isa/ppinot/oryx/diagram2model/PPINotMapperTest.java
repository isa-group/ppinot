package es.us.isa.ppinot.oryx.diagram2model;

import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.test.DiagramTestUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * PPINotMapperTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class PPINotMapperTest {
    @Test
    public void fullTest() throws IOException, JSONException {
        BasicDiagram diagram = DiagramTestUtils.loadDiagram(new InputStreamReader(getClass().getResourceAsStream("/RFCManagement.json")));
        PPINotMapper mapper = new PPINotMapper();
        Collection<PPI> ppis = mapper.map(diagram);

        PPI ppi4 = findById(ppis, "PPI4");
        PPI ppi2 = findById(ppis, "PPI2");

        Assert.assertEquals(2, ppis.size());
        Assert.assertNotEquals(null, ppi2);
        Assert.assertNotEquals(null, ppi4);
        Assert.assertTrue(ppi4.getMeasuredBy() instanceof DerivedMultiInstanceMeasure);
        Assert.assertEquals("(a/b)*100", ((DerivedMultiInstanceMeasure) ppi4.getMeasuredBy()).getFunction());
        Assert.assertEquals(2, ((DerivedMultiInstanceMeasure) ppi4.getMeasuredBy()).getUsedMeasureMap().size());
        Assert.assertTrue(((DerivedMultiInstanceMeasure) ppi4.getMeasuredBy()).getUsedMeasureMap().get("a") instanceof AggregatedMeasure);
    }

    private PPI findById(Collection<PPI> ppis, String id) {
        PPI ppi = null;
        for (PPI p : ppis) {
            if (p.getId().equals(id)) {
                ppi = p;
            }
        }

        return ppi;
    }

}