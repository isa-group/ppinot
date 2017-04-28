package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.MeasuresCollection;
import org.junit.Test;

import java.io.StringWriter;

/**
 * XMLMeasuresCollectionHandlerTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class XMLMeasuresCollectionHandlerTest extends AbstractMeasuresCollectionHandlerTest {

    @Test
    public void testSave() throws Exception {
        XMLMeasuresCollectionHandler handler = new XMLMeasuresCollectionHandler();
        MeasuresCollection mc = new MeasuresCollection("test", "description");
        mc.addDefinition(buildAFIP());

        handler.setCollection(mc);
        StringWriter sw = new StringWriter();
        handler.save(sw);
        sw.close();

        System.out.println(sw.toString());
    }
}