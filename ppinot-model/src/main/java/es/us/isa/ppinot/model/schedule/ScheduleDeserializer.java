package es.us.isa.ppinot.model.schedule;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * ScheduleDeserializer
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class ScheduleDeserializer extends StdDeserializer<Schedule> {
    private static final String BASIC = "basic";
    private static final String COMBINED = "combined";

    public ScheduleDeserializer() {
        super(Schedule.class);
    }

    @Override
    public Schedule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken current = jp.getCurrentToken();
        Schedule s = null;

        if (current.equals(JsonToken.START_ARRAY)) {
            s = jp.readValueAs(ScheduleCombined.class);
        } else if (current.equals(JsonToken.VALUE_STRING)) {
            if (jp.getText().startsWith("[")) {
                ObjectMapper mapper = new ObjectMapper();
                s = mapper.readValue(jp.getText(), ScheduleCombined.class);
            } else {
                s = jp.readValueAs(ScheduleBasic.class);
            }
        } else if (current.equals(JsonToken.START_OBJECT)) {
            current = jp.nextToken();
            if (JsonToken.FIELD_NAME.equals(current)) {
                if (BASIC.equals(jp.getText())) {
                    jp.nextToken();
                    s = jp.readValueAs(ScheduleBasic.class);
                } else if (COMBINED.equals(jp.getText())) {
                    jp.nextToken();
                    s = jp.readValueAs(ScheduleCombined.class);
                }
            }
        } else {
            s = null;
        }

        return s;
    }
}
