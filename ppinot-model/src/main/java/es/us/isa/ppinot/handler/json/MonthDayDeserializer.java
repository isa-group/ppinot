/*
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package es.us.isa.ppinot.handler.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.MonthDay;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.IOException;

/**
 *
 * @author isa-group
 */
public class MonthDayDeserializer extends JsonDeserializer<MonthDay> {

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendMonthOfYear(1).appendLiteral('/').appendDayOfMonth(1).toFormatter();

    @Override
    public MonthDay deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String str = p.getText().trim();

        if (str.isEmpty()) {
            return null;
        }

        return MonthDay.parse(str, formatter);
    }
}
