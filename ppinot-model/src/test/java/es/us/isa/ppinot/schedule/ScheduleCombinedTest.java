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

import es.us.isa.ppinot.handler.JSONMeasuresCollectionHandler;
import es.us.isa.ppinot.model.schedule.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author isa-group
 */
public class ScheduleCombinedTest {

    public static class ScheduleTest {
        private Schedule schedule;

        public ScheduleTest() {}

        public Schedule getSchedule() {
            return schedule;
        }
    }


    DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");

    ObjectMapper mapper;

    int year = 2017;
    int monthOfYear = 6;
    int dayOfMonth = 1;
    int hourOfDay = 0;
    int minuteOfHour = 0;
    int secondOfMinute = 0;

    DateTime issueStart = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
    DateTime issueEnd = issueStart.toDateTime().plusDays(1);

    /**
     * Create a ScheduleCombined with a 24x7 schedule for each month of the current year.
     *
     * @return
     */
    public static ScheduleCombined create24x7MonthlyScheduleFullYear() {
        int year = DateTime.now().getYear();
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();

        for (int month = 1; month <= 12; month++) {
            int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
            int lastDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
            items.add(new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7));
        }

        return new ScheduleCombined(items);
    }

    @Before
    public void setup() {
        this.mapper = new JSONMeasuresCollectionHandler().getMapper();
    }


    @Test
    public void testDeserialize() throws IOException {

        String text = "["
            + "{\"from\": \"01/01\", \"to\": \"06/30\", \"schedule\": \"L-DT8:00-19:00\"},"
            + "{\"from\": \"07/01\", \"to\": \"12/31\", \"schedule\": \"L-DT8:00-19:00\"}"
            + "]";


        ScheduleCombined schedule = mapper.readValue(text, ScheduleCombined.class);
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule);

        long value = duration.getMillis();

        Assert.assertEquals(11 * 60 * 60 * 1000, value);

    }

    @Test
    public void testDeserialize2() throws IOException {
        String text = "{\"schedule\":[{\"from\":\"1/1\",\"to\":\"6/14\",\"schedule\":\"L-VT09:00-18:00\"},{\"from\":\"6/15\",\"to\":\"9/15\",\"schedule\":\"L-VT08:30-15:00\"},{\"from\":\"9/16\",\"to\":\"12/23\",\"schedule\":\"L-VT09:00-18:00\"},{\"from\":\"12/24\",\"to\":\"12/24\",\"schedule\":\"L-VT09:00-13:00\"},{\"from\":\"12/25\",\"to\":\"12/30\",\"schedule\":\"L-VT09:00-18:00\"},{\"from\":\"12/31\",\"to\":\"12/31\",\"schedule\":\"L-VT09:00-13:00\"}]}";

        ScheduleTest test = mapper.readValue(text, ScheduleTest.class);
        ScheduleCombined schedule = (ScheduleCombined) test.getSchedule();
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedule);

        long value = duration.getMillis();

        Assert.assertEquals(9 * 60 * 60 * 1000, value);

    }

    @Test
    public void testSerialize() throws IOException {
        ScheduleBasic normal = new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 0), new LocalTime(19, 0));
        ScheduleBasic reduced = new ScheduleBasic(DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY, new LocalTime(8, 0), new LocalTime(14, 0));

        ScheduleCombined sch = new ScheduleCombined();
        sch.addSchedule(new ScheduleItem(new MonthDay(1,1), new MonthDay(12,23), normal));
        sch.addSchedule(new ScheduleItem(new MonthDay(12,24), new MonthDay(12,24), reduced));
        sch.addSchedule(new ScheduleItem(new MonthDay(12,25), new MonthDay(12,30), normal));
        sch.addSchedule(new ScheduleItem(new MonthDay(12,31), new MonthDay(12,31), reduced));

        String result = "[{\"from\":\"1/1\",\"to\":\"12/23\",\"schedule\":\"L-VT08:00-19:00\"},{\"from\":\"12/24\",\"to\":\"12/24\",\"schedule\":\"L-VT08:00-14:00\"},{\"from\":\"12/25\",\"to\":\"12/30\",\"schedule\":\"L-VT08:00-19:00\"},{\"from\":\"12/31\",\"to\":\"12/31\",\"schedule\":\"L-VT08:00-14:00\"}]";

        Assert.assertEquals(result, mapper.writeValueAsString(sch));


    }

    /**
     * Test case for ScheduleCombined with empty period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptySchedule() {

        new ScheduleCombined(Collections.EMPTY_LIST);
    }

    /**
     * Test case for ScheduleCombined with intersected period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleCoincide() {
        int year = DateTime.now().getYear();
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();

        for (int month = 1; month <= 11; month++) {
            int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
            if (month < 11) {
                ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, firstDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                items.add(sc);
            } else {
                int lastDayOfMonth = new DateTime(year, month + 1, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
                ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                items.add(sc);
            }
        }

        new ScheduleCombined(items);
    }

    /**
     * Test case for ScheduleCombined when it does not include the first and last day of the year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSchedulesDontIncludeFirstAndLastDayOfYear() {
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();

        items.add(new ScheduleItem(new MonthDay(monthOfYear - 2, dayOfMonth), new MonthDay(monthOfYear - 1, dayOfMonth), ScheduleBasic.SCHEDULE_24X7));
        items.add(new ScheduleItem(new MonthDay(monthOfYear - 1, dayOfMonth + 1), new MonthDay(monthOfYear, dayOfMonth), ScheduleBasic.SCHEDULE_24X7));
        items.add(new ScheduleItem(new MonthDay(monthOfYear, dayOfMonth + 1), new MonthDay(monthOfYear + 1, dayOfMonth), ScheduleBasic.SCHEDULE_24X7));

        new ScheduleCombined(items);
    }

    @Test
    public void testIntervalScheduleMappingNumberOfIntervals() {
        ScheduleCombined schedules = create24x7MonthlyScheduleFullYear();
        Map<Interval, ScheduleBasic> map = schedules.getIntervalScheduleMapping(DateTime.now().getYear(), DateTime.now().getYear());
        Assert.assertEquals(12, map.size());
    }

    /**
     * Test case to expect a failed ScheduleCombined Test a failed execution when schedule periods do not fill a whole year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleWithGaps() {
        int year = DateTime.now().getYear();
        List<ScheduleItem> items = new ArrayList<ScheduleItem>();

        for (int month = 1; month <= 11; month++) {
            if (month == 1 || month == 11) {
                int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
                if (month == 1) {
                    items.add(new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, firstDayOfMonth), ScheduleBasic.SCHEDULE_24X7));
                }
                if (month == 11) {
                    int lastDayOfMonth = new DateTime(year, month + 1, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
                    items.add(new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7));
                }
            }
        }

        new ScheduleCombined(items);

    }

}
