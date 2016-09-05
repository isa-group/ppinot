package es.us.isa.ppinot.handler.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * DateTimeDeserializer
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class DateTimeDeserializer extends JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String str = jsonParser.getText().trim();
        return (str.length() == 0) ? null
                : DateTime.parse(str);
    }
}
