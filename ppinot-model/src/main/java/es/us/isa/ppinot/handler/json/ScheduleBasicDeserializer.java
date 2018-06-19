package es.us.isa.ppinot.handler.json;

import es.us.isa.ppinot.model.schedule.Holidays;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * LocalTimeDeserializer Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScheduleBasicDeserializer extends JsonDeserializer<ScheduleBasic> {

    Holidays holidays;

    public ScheduleBasicDeserializer(Holidays holidays) {
        this.holidays = holidays;
    }

    @Override
    public ScheduleBasic deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText().trim();
        ScheduleBasic result = null;

        if (str.length() != 0 && !str.startsWith("${")) {
            result = ScheduleBasic.parse(str, holidays);
        }
        return result;
    }

}
