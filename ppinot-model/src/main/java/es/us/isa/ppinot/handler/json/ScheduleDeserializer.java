package es.us.isa.ppinot.handler.json;

import es.us.isa.ppinot.model.Schedule;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
