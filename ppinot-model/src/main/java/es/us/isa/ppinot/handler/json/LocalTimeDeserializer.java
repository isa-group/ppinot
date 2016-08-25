package es.us.isa.ppinot.handler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.LocalTime;

import java.io.IOException;

/**
 * LocalTimeDeserializer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText().trim();
        return (str.length() == 0) ? null
                : LocalTime.parse(str);
    }
}
