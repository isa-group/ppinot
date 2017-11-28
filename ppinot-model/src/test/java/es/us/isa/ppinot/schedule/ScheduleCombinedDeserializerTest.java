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
import es.us.isa.ppinot.model.DurationWithExclusionCombined;
import es.us.isa.ppinot.model.ScheduleCombined;
import es.us.isa.ppinot.model.ScheduleItem;
import java.io.IOException;
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

    DateTime issueStart = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
    DateTime issueEnd = issueStart.toDateTime().plusDays(1);

    @Test
    public void testDeserialize() throws IOException {

        String text = "["
            + "{\"from\": \"--01-01\", \"to\": \"--06-30\", \"schedule\": \"L-DT8:00-19:00\"},"
            + "{\"from\": \"--07-01\", \"to\": \"--12-31\", \"schedule\": \"L-DT8:00-19:00\"}"
            + "]";

        ObjectMapper mapper = new ObjectMapper();

        ScheduleItem[] items = mapper.readValue(text, ScheduleItem[].class);
        ScheduleCombined schedule = new ScheduleCombined(Lists.newArrayList(items));
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule);

        long value = duration.getMillis();

        Assert.assertEquals(8 * 60 * 60 * 1000, value);

    }

}
