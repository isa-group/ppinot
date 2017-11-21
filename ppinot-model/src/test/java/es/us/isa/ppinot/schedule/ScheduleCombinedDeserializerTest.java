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
package es.us.isa.ppinot.schedule;

import com.google.common.collect.Lists;
import es.us.isa.ppinot.evaluation.computers.timer.DurationWithExclusionCombined;
import es.us.isa.ppinot.model.ScheduleCombined;
import es.us.isa.ppinot.model.ScheduleItem;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author isa-group
 */
public class ScheduleCombinedDeserializerTest {
    
    DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");

    int year = 2017;
    int monthOfYear = 6;
    int dayOfMonth = 1;
    int hourOfDay = 0;
    int minuteOfHour = 0;
    int secondOfMinute = 0;

    DateTime windowStart = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
    DateTime windowEnd = windowStart.toDateTime().plusMonths(1).minusMillis(1);

    @Test
    public void testDeserialize() throws IOException {
        
        String text = "["
            + "{\"from\": \"2017-01-01T00:00:00.000Z\", \"to\": \"2017-06-30T23:59:59.999Z\", \"schedule\": \"L-VT8:00-19:00\"},"
            + "{\"from\": \"2017-07-01T00:00:00.000Z\", \"to\": \"2017-12-31T23:59:59.999Z\", \"schedule\": \"L-VT8:00-19:00\"}"
            + "]";
        
        ObjectMapper mapper = new ObjectMapper();
        
        ScheduleItem[] items = mapper.readValue(text, ScheduleItem[].class);
        ScheduleCombined schedule = new ScheduleCombined(Lists.newArrayList(items));
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(windowStart, windowEnd, schedule);

        ScheduleCombined filtered = duration.combineScheduleForWindow();
        Assert.assertEquals(1, filtered.size());
        Assert.assertEquals(filtered.get(0).getFrom(), windowStart);
        Assert.assertEquals(filtered.get(0).getTo(), windowEnd);
        
    }

    @Test
    public void testDeserializeInsertectedSchedules() throws IOException {
        
        String text = "["
            + "{\"from\": \"2017-01-01T00:00:00.000Z\", \"to\": \"2017-08-30T23:59:59.999Z\", \"schedule\": \"L-VT8:00-19:00\"},"
            + "{\"from\": \"2017-07-01T00:00:00.000Z\", \"to\": \"2017-12-31T23:59:59.999Z\", \"schedule\": \"L-VT8:00-19:00\"}"
            + "]";
        
        ObjectMapper mapper = new ObjectMapper();
        
        ScheduleItem[] items = mapper.readValue(text, ScheduleItem[].class);
        ScheduleCombined schedule = new ScheduleCombined(Lists.newArrayList(items));
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(windowStart, windowEnd, schedule);

        try {
            ScheduleCombined filtered = duration.combineScheduleForWindow();
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(!ex.getMessage().equals(""));
        }
        
    }
    
}
