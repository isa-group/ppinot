package es.us.isa.ppinot.handler.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.MonthDay;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.IOException;

/**
 * MonthDaySerializer
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class MonthDaySerializer extends JsonSerializer<MonthDay> {

    private DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendMonthOfYear(1).appendLiteral('/').appendDayOfMonth(1).toFormatter();

    @Override
    public void serialize(MonthDay value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(formatter.print(value));
    }
}
