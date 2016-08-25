package es.us.isa.ppinot.handler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import es.us.isa.ppinot.model.Schedule;

import java.io.IOException;

/**
 * LocalTimeDeserializer
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScheduleDeserializer extends JsonDeserializer<Schedule> {

    @Override
    public Schedule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText().trim();
        Schedule result = null;

        if (str.length() != 0 && !str.startsWith("${")) {
            result = Schedule.parse(str);
        }
        return result;
    }

}
