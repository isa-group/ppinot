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

import es.us.isa.ppinot.model.DurationWithExclusionCombined;
import es.us.isa.ppinot.model.ScheduleBasic;
import es.us.isa.ppinot.model.ScheduleCombined;
import es.us.isa.ppinot.model.ScheduleItem;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.MonthDay;
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

    DateTime issueStart = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, timeZone);
    DateTime issueEnd = issueStart.toDateTime().plusDays(1).minusMillis(1);

    /**
     * Create a ScheduleCombined with a 24x7 schedule for each month of the current year.
     *
     * @return
     */
    private static ScheduleCombined create24x7MonthlyScheduleFullYear() {
        int year = DateTime.now().getYear();

        ScheduleCombined schedules = new ScheduleCombined();
        for (int month = 1; month <= 12; month++) {
            int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
            int lastDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
            ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
            schedules.addSchedule(sc);
        }

        return schedules;
    }

    /**
     * Create a ScheduleCombined which a schedule period intersect with another.
     *
     * @return
     */
    private static ScheduleCombined create24x7MonthlyScheduleCoincideFullYear() {
        int year = DateTime.now().getYear();

        ScheduleCombined schedules = new ScheduleCombined();
        for (int month = 1; month <= 11; month++) {
            int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
            if (month < 11) {
                ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, firstDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                schedules.addSchedule(sc);
            } else {
                int lastDayOfMonth = new DateTime(year, month + 1, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
                ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                schedules.addSchedule(sc);
            }
        }

        return schedules;
    }

    /**
     * Create a ScheduleCombined which a schedule periods do not fill a whole year.
     *
     * @return
     */
    private static ScheduleCombined create24x7MonthlyScheduleWithGaps() {
        int year = DateTime.now().getYear();

        ScheduleCombined schedules = new ScheduleCombined();
        for (int month = 1; month <= 11; month++) {
            if (month == 1 || month == 11) {
                int firstDayOfMonth = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMinimumValue().getDayOfMonth();
                if (month == 1) {
                    ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, firstDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                    schedules.addSchedule(sc);
                }
                if (month == 11) {
                    int lastDayOfMonth = new DateTime(year, month + 1, 1, 0, 0, DateTimeZone.UTC).dayOfMonth().withMaximumValue().getDayOfMonth();
                    ScheduleItem sc = new ScheduleItem(new MonthDay(month, firstDayOfMonth), new MonthDay(month + 1, lastDayOfMonth), ScheduleBasic.SCHEDULE_24X7);
                    schedules.addSchedule(sc);
                }
            }
        }

        return schedules;
    }

    /**
     * Test case for ScheduleCombined duration.
     */
    @Test
    public void testScheduleBeforeAndAfterIssueDuration() {

        ScheduleCombined schedules = create24x7MonthlyScheduleFullYear();
        long duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();

        Assert.assertEquals(issueEnd.getMillis() - issueStart.getMillis(), duration);

    }

    /**
     * Test case for ScheduleCombined mapping.
     */
    @Test
    public void testIntervalScheduleMapping() {

        ScheduleCombined schedules = create24x7MonthlyScheduleFullYear();
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules);
        Map<Interval, ScheduleBasic> map = duration.getIntervalScheduleMapping();

        Assert.assertEquals(1, map.size());

    }

    /**
     * Test case for ScheduleCombined with empty period.
     */
    @Test
    public void testEmptySchedule() {

        ScheduleCombined schedules = new ScheduleCombined();
        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules);
        long value = Long.MIN_VALUE;

        try {
            value = duration.getMillis();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("No schedule defined. Please, define at least one"));
        }

        Assert.assertEquals(Long.MIN_VALUE, value);

    }

    /**
     * Test case for ScheduleCombined with intersected period.
     */
    @Test
    public void testScheduleCoincide() {

        ScheduleCombined schedules = create24x7MonthlyScheduleCoincideFullYear();
        long value = Long.MIN_VALUE;

        try {
            value = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("Schedule interval must not coincide"));
        }

        Assert.assertEquals(Long.MIN_VALUE, value);

    }

    /**
     * Test case for ScheduleCombined when it does not include the first and last day of the year.
     */
    @Test
    public void testSchedulesDontIncludeFirstAndLastDayOfYear() {

        ScheduleItem sc1 = new ScheduleItem(new MonthDay(monthOfYear - 2, dayOfMonth), new MonthDay(monthOfYear - 1, dayOfMonth), ScheduleBasic.SCHEDULE_24X7);
        ScheduleItem sc2 = new ScheduleItem(new MonthDay(monthOfYear - 1, dayOfMonth + 1), new MonthDay(monthOfYear, dayOfMonth), ScheduleBasic.SCHEDULE_24X7);
        ScheduleItem sc3 = new ScheduleItem(new MonthDay(monthOfYear, dayOfMonth + 1), new MonthDay(monthOfYear + 1, dayOfMonth), ScheduleBasic.SCHEDULE_24X7);

        ScheduleCombined schedules = new ScheduleCombined();
        schedules.addSchedule(sc1);
        schedules.addSchedule(sc2);
        schedules.addSchedule(sc3);

        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart.plusDays(1), issueEnd.plusDays(1), schedules);
        long value = Long.MIN_VALUE;

        try {
            value = duration.getMillis();
        } catch (Exception ex) {
            Assert.assertTrue("First and last day of the year must be included on schedule".equals(ex.getMessage()));
        }

        Assert.assertEquals(Long.MIN_VALUE, value);

    }

    /**
     * Test case to check if ScheduleCombined periods are being cached once when there is multiple calls.
     */
    @Test
    public void testMultipleCallWithScheduleCache() {

        ScheduleCombined schedules = create24x7MonthlyScheduleFullYear();

        DurationWithExclusionCombined duration = new DurationWithExclusionCombined(issueStart.plusDays(1), issueEnd.plusDays(1), schedules);

        Map<Interval, ScheduleBasic> map;
        map = duration.getIntervalScheduleMapping();
        map = duration.getIntervalScheduleMapping();

        Assert.assertEquals(1, map.size());
        Assert.assertEquals(1, DurationWithExclusionCombined.getScheduleIntervalHashExistGapCache().size());
        Assert.assertEquals(false, DurationWithExclusionCombined.getScheduleIntervalHashExistGapCache().values().iterator().next());
        Assert.assertEquals(0, DurationWithExclusionCombined.getScheduleIntervalHashGapIntervalCache().size());

    }

    /**
     * Test case to expect a failed ScheduleCombined Test a failed execution when schedule periods do not fill a whole year.
     */
    @Test
    public void testScheduleWithGaps() {

        ScheduleCombined schedules = create24x7MonthlyScheduleWithGaps();

        long duration = Long.MIN_VALUE;

        try {
            duration = new DurationWithExclusionCombined(issueStart, issueEnd, schedules).getMillis();
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("There is a gap on between the intervals"));
        }

        Assert.assertEquals(Long.MIN_VALUE, duration);

    }

}
