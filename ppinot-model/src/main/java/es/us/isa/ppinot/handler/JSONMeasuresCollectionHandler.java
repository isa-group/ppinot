package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * JSONMeasuresCollectionHandler
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONMeasuresCollectionHandler {
    private MeasuresCollection collection;
    private ObjectMapper mapper;

    public JSONMeasuresCollectionHandler() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public void load(Reader collectionReader) throws IOException {
        collection = mapper.readValue(collectionReader, MeasuresCollection.class);
    }

    public void save(Writer collectionWriter) throws IOException {
        mapper.writeValue(collectionWriter, collection);
    }

    public MeasuresCollection getCollection() {
        return collection;
    }

    public JSONMeasuresCollectionHandler setCollection(MeasuresCollection collection) {
        this.collection = collection;
        return this;
    }
}
