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

import es.us.isa.ppinot.model.schedule.DurationWithExclusionCombined;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import es.us.isa.ppinot.model.schedule.ScheduleCombined;
import es.us.isa.ppinot.model.schedule.ScheduleItem;
import org.joda.time.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionCombinedTest {
    public static final ScheduleBasic REGULARHOURS = new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 0), new LocalTime(20, 0), Arrays.asList(new DateTime(2017, 12, 25, 0, 0),new DateTime(2018, 12, 25, 0, 0)));
    public static final ScheduleBasic SUMMERHOURS =  new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(7, 0), new LocalTime(15, 0));
    public static final ScheduleBasic REDUCEDHOURS =  new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 0), new LocalTime(14, 0));

    /**
     * Create a ScheduleCombined that changes between a 24x7 schedule and a reduced scheudle for each month of the current year.
     *
     * @return
     */
    private ScheduleCombined create24x7AndRegularMonthlyScheduleFullYear() {
        int year = DateTime.now().getYear();
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();


        for (int month = 1; month <= 12; month++) {
            int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
            int lastDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
            items.add(new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month, lastDayOfMonth),
                    month % 2 == 0 ? ScheduleBasic.SCHEDULE_24X7 : REGULARHOURS));
        }

        return new ScheduleCombined(items);
    }


    /**
     * Create a ScheduleCombined that changes between a 24x7 schedule and a reduced scheudle for each month of the current year.
     *
     * @return
     */
    private ScheduleCombined createRealisticScheduleFullYear() {
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();
        items.add(new ScheduleItem(new MonthDay(1, 1), new MonthDay(6, 14), REGULARHOURS));
        items.add(new ScheduleItem(new MonthDay(6, 15), new MonthDay(9, 14), SUMMERHOURS));
        items.add(new ScheduleItem(new MonthDay(9, 15), new MonthDay(12, 23), REGULARHOURS));
        items.add(new ScheduleItem(new MonthDay(12, 24), new MonthDay(12, 24), REDUCEDHOURS));
        items.add(new ScheduleItem(new MonthDay(12, 25), new MonthDay(12, 30), REGULARHOURS));
        items.add(new ScheduleItem(new MonthDay(12, 31), new MonthDay(12, 31), REDUCEDHOURS));

        return new ScheduleCombined(items);
    }

    DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");

    int year = 2017;

    private DateTime day(int day, int month) {
        return day(day, month, 0, 0);
    }

    private DateTime day(int day, int month, int hour, int minute) {
        return new DateTime(year, month, day, hour, minute, 0, timeZone);
    }

    private long millisInHours(int hours) {
        return Hours.hours(hours).toStandardDuration().getMillis();
    }


    /**
     * Test case for ScheduleCombined duration.
     */
    @Test
    public void testScheduleBeforeAndAfterIssueDuration() {
        ScheduleCombined schedules = ScheduleCombinedTest.create24x7MonthlyScheduleFullYear();
        DateTime issueStart = day(1, 6);
        DateTime issueEnd = issueStart.plusDays(1);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();

        Assert.assertEquals(millisInHours(24), duration);

    }

    @Test
    public void testScheduleInSegment() {
        ScheduleCombined schedules = create24x7AndRegularMonthlyScheduleFullYear();
        DateTime issueStart = day(1, 6);
        DateTime issueEnd = issueStart.plusDays(1);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();

        Assert.assertEquals(millisInHours(24), duration);

        issueStart = day(1, 3);
        issueEnd = issueStart.plusDays(1);

        duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();

        Assert.assertEquals(millisInHours(12), duration);

    }

    @Test
    public void testScheduleLimitBeginSegment() {
        ScheduleCombined schedule = create24x7AndRegularMonthlyScheduleFullYear();
        DateTime issueStart = day(30,6);
        DateTime issueEnd = issueStart.plusDays(1);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule).getMillis();

        Assert.assertEquals(millisInHours(24), duration);

    }

    @Test
    public void testScheduleLimitEndSegment() {
        ScheduleCombined schedule = create24x7AndRegularMonthlyScheduleFullYear();
        DateTime issueStart = day(1,11);
        DateTime issueEnd = issueStart.plusDays(1);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule).getMillis();

        Assert.assertEquals(millisInHours(12), duration);

    }


    @Test
    public void testScheduleFullExample() {
        ScheduleCombined schedule = createRealisticScheduleFullYear();
        DateTime issueStart = day(23,12,14,00);
        DateTime issueEnd = issueStart.plusDays(4).withHourOfDay(17);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule).getMillis();

        Assert.assertEquals(millisInHours(12+9), duration);

    }


    @Test
    public void testScheduleSeveralExample() {
        ScheduleCombined schedule = createRealisticScheduleFullYear();
        DateTime issueStart = new DateTime(2018, 12, 21, 14,00);
        DateTime issueEnd = issueStart.plusDays(6).withHourOfDay(17);

        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule).getMillis();

        Assert.assertEquals(millisInHours(6+0+0+6+0+12+9), duration);

    }


}
