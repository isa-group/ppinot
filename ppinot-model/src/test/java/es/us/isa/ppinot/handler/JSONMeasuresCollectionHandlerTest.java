package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.state.DataObjectState;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JSONMeasuresCollectionHandlerTest
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONMeasuresCollectionHandlerTest extends AbstractMeasuresCollectionHandlerTest {

    @Test
    public void shouldSaveMeasures() throws Exception {
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();
        MeasuresCollection mc = new MeasuresCollection("test", "description");
        mc.addDefinition(buildAFIP());

        handler.setCollection(mc);
        StringWriter sw = new StringWriter();
        handler.save(sw);
        sw.close();

        System.out.println(sw.toString());

        handler.load(new StringReader(sw.toString()));
        MeasuresCollection readCollection = handler.getCollection();
        assertEquals(1, readCollection.getDefinitions().size());
        MeasureDefinition md = readCollection.getDefinitions().get(0);
        assertTrue(md instanceof DerivedMultiInstanceMeasure);
    }

    @Test
    public void shouldReplaceWithTemplate() throws IOException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("proveedor", "p1");
        JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();

        handler.load(new StringReader(countWithTemplate()), parameters);
        MeasuresCollection mc = handler.getCollection();

        CountMeasure count = (CountMeasure) mc.getDefinitions().get(0);
        DataObjectState state = (DataObjectState) count.getWhen().getChangesToState();
        assertEquals("Grupo == 'p1'", state.getName());
    }

}
