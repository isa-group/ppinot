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

import es.us.isa.ppinot.evaluation.computers.timer.DurationWithExclusionCombined;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.ScheduleCombined;
import es.us.isa.ppinot.model.ScheduleItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
public class DurationWithExclusionCombinedTest {

    DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");

    int year = 2017;
    int monthOfYear = 6;
    int dayOfMonth = 1;
    int hourOfDay = 0;
    int minuteOfHour = 0;
    int secondOfMinute = 0;

    DateTime windowStart = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
    DateTime windowEnd = windowStart.toDateTime().plusMonths(1);

    @Test
    public void testWindowIncludedOnSchedule() {

        DateTime scheduleStart = new DateTime(year, monthOfYear - 1, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
        DateTime scheduleEnd = new DateTime(year, monthOfYear + 2, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);

        ScheduleItem sc = new ScheduleItem(scheduleStart, scheduleEnd, Schedule.SCHEDULE_24X7);
        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc);

        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(windowStart, windowEnd, schedules);

        ScheduleCombined filtered = duration.combineScheduleForWindow();
        Assert.assertEquals(filtered.size(), 1);
        Assert.assertEquals(filtered.get(0).getFrom(), windowStart);
        Assert.assertEquals(filtered.get(0).getTo(), windowEnd);

    }

    @Test
    public void testSeveralSchedules() {

        // Schedules 
        DateTime schedule1Start = windowStart.toDateTime().minusMonths(2);
        DateTime schedule1End = windowStart.toDateTime().minusMonths(1).minusMillis(1);
        DateTime schedule2Start = windowStart.toDateTime().minusMonths(1);
        DateTime schedule2End = windowStart.toDateTime().minusMillis(1);
        DateTime schedule3Start = windowStart.toDateTime();
        DateTime schedule3End = windowEnd.toDateTime();

        ScheduleItem sc1 = new ScheduleItem(schedule1Start, schedule1End, Schedule.SCHEDULE_24X7);
        ScheduleItem sc2 = new ScheduleItem(schedule2Start, schedule2End, Schedule.SCHEDULE_24X7);
        ScheduleItem sc3 = new ScheduleItem(schedule3Start, schedule3End, Schedule.SCHEDULE_24X7);

        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc1);
        schedules.addSchedule(sc2);
        schedules.addSchedule(sc3);

        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(windowStart, windowEnd, schedules);

        ScheduleCombined filtered = duration.combineScheduleForWindow();
        Assert.assertEquals(filtered.size(), 1);
        Assert.assertEquals(filtered.get(0).getFrom(), windowStart);
        Assert.assertEquals(filtered.get(0).getTo(), windowEnd);
        Assert.assertEquals(filtered.get(0).getFrom(), schedule3Start);
        Assert.assertEquals(filtered.get(0).getTo(), schedule3End);

    }

    @Test
    public void testIncompleteScheduleWindow() {

        DateTime scheduleStart = windowStart.toDateTime().minusMonths(1).plusDays(15);
        DateTime scheduleEnd = windowStart.toDateTime().plusDays(15);

        ScheduleItem sc = new ScheduleItem(scheduleStart, scheduleEnd, Schedule.SCHEDULE_24X7);
        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc);

        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(windowStart, windowEnd, schedules);

        try {
            ScheduleCombined filtered = duration.combineScheduleForWindow();
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals(ex.getMessage(), "Incomplete schedule slots");
        }

    }

    @Test
    public void testMultipleScheduleDuration() {

        DateTime schedule1Start = windowStart.toDateTime().minusMonths(1).plusDays(15);
        DateTime schedule1End = windowStart.toDateTime().plusDays(15).minusMillis(1);
        DateTime schedule2Start = schedule1End.toDateTime().plusMillis(1);
        DateTime schedule2End = windowEnd.toDateTime().plusDays(15);

        ScheduleItem sc1 = new ScheduleItem(schedule1Start, schedule1End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(18, 0)
            )
        );
        ScheduleItem sc2 = new ScheduleItem(
            schedule2Start, schedule2End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(15, 0)
            )
        );

        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc1);
        schedules.addSchedule(sc2);

        long expected = new DurationWithExclusionCombined(windowStart, windowEnd, schedules).getMillis();
        long expectedHours = TimeUnit.MILLISECONDS.toHours(expected);
        long result = (10 * 15) + (7 * 15);

        Assert.assertEquals(expectedHours, result);

    }

    @Test
    public void testMultipleDisorderedScheduleDuration() {

        DateTime schedule1Start = windowStart.toDateTime().minusMonths(1).plusDays(15);
        DateTime schedule1End = windowStart.toDateTime().plusDays(15).minusMillis(1);
        DateTime schedule2Start = windowStart.toDateTime().plusDays(15);
        DateTime schedule2End = windowEnd.toDateTime().plusDays(15);

        ScheduleItem sc1 = new ScheduleItem(schedule2Start, schedule2End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(18, 0)
            )
        );
        ScheduleItem sc2 = new ScheduleItem(
            schedule1Start, schedule1End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(15, 0)
            )
        );

        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc1);
        schedules.addSchedule(sc2);

        long expected = new DurationWithExclusionCombined(windowStart, windowEnd, schedules).getMillis();
        long expectedHours = TimeUnit.MILLISECONDS.toHours(expected);
        long result = (10 * 15) + (7 * 15);

        Assert.assertEquals(expectedHours, result);

    }

    @Test
    public void testIntersectedSchedule() {

        DateTime schedule1Start = windowStart.toDateTime().minusMonths(1).plusDays(15);
        DateTime schedule1End = windowStart.toDateTime().plusDays(15);
        DateTime schedule2Start = windowStart.toDateTime().plusDays(15);
        DateTime schedule2End = windowEnd.toDateTime().plusDays(15);

        ScheduleItem sc1 = new ScheduleItem(schedule2Start, schedule2End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(18, 0)
            )
        );
        ScheduleItem sc2 = new ScheduleItem(
            schedule1Start, schedule1End,
            new Schedule(
                DateTimeConstants.MONDAY, DateTimeConstants.SUNDAY, new LocalTime(8, 0), new LocalTime(15, 0)
            )
        );

        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc1);
        schedules.addSchedule(sc2);

        try {
            long expected = new DurationWithExclusionCombined(windowStart, windowEnd, schedules).getMillis();
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Intersected schedule for interval"));
        }

    }

}
