package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.handler.json.MapTokenResolver;
import es.us.isa.ppinot.handler.json.ScheduleBasicDeserializer;
import es.us.isa.ppinot.handler.json.TokenReplacingReader;
import es.us.isa.ppinot.model.MeasuresCollection;
import es.us.isa.ppinot.model.schedule.*;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
        this(DefaultHolidays.getDays());
    }

    public JSONMeasuresCollectionHandler(Holidays holidays) {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("Schedule", Version.unknownVersion());
        module.addDeserializer(ScheduleBasic.class, new ScheduleBasicDeserializer(holidays));
        module.addDeserializer(Schedule.class, new ScheduleDeserializer(mapper));
        mapper.registerModule(module);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public ObjectMapper getMapper() {
        return mapper;
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
