package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.handler.json.MapTokenResolver;
import es.us.isa.ppinot.handler.json.TokenReplacingReader;
import es.us.isa.ppinot.handler.json.TokenResolver;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

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

    public void load(Reader collectionReader, Map<String, String> parameters) throws IOException {
        Reader reader = collectionReader;

        if (parameters != null && ! parameters.isEmpty()) {
            reader = new TokenReplacingReader(collectionReader, new MapTokenResolver(parameters));
        }

        load(reader);
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
