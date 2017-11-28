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

import es.us.isa.ppinot.model.schedule.DurationWithExclusion;
import es.us.isa.ppinot.model.schedule.DurationWithExclusionBasic;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionBasicTest {

    DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");

    @Test
    public void testScheduleBasicDuration() {

        ScheduleBasic workingHours = new ScheduleBasic(
            DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY,
            new LocalTime(8, 0, 0, 0),
            new LocalTime(20, 0, 0, 0));

        DateTime start = new DateTime(2017, 6, 1, 8, 0, 0, timeZone);
        DateTime end = new DateTime(2017, 6, 1, 20, 0, 0, timeZone);

        DurationWithExclusion duration = new DurationWithExclusionBasic(start, end, workingHours);
        long value = duration.getMillis();

        Assert.assertEquals(end.getMillis() - start.getMillis(), value);

    }

    @Test
    public void testScheduleBasic24x7DurationMidnight() {

        DateTime start = new DateTime(2017, 6, 1, 0, 0, 0, timeZone);
        DateTime end = new DateTime(2017, 6, 2, 0, 0, 0, timeZone);

        DurationWithExclusion duration = new DurationWithExclusionBasic(start, end, ScheduleBasic.SCHEDULE_24X7);
        long value = duration.getMillis();

        Assert.assertEquals(end.getMillis() - start.getMillis(), value);

    }

}
