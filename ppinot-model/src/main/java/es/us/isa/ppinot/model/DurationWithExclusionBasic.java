package es.us.isa.ppinot.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

/**
 * DurationWithExclusion Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class DurationWithExclusionBasic implements DurationWithExclusion {

    private DateTime start;
    private DateTime end;
    private ScheduleBasic schedule;

    public DurationWithExclusionBasic(DateTime start, DateTime end,
        ScheduleBasic schedule) {
        DateTimeZone timeZone = schedule != null
            ? schedule.getTimeZone() : DateTimeZone.getDefault();

        this.start = start.toDateTime(timeZone);
        this.end = end.toDateTime(timeZone);
//            this.start = start;
//            this.end = end;
        this.schedule = schedule;
    }

    public DurationWithExclusionBasic copy() {
        return new DurationWithExclusionBasic(start, end, schedule);
    }

    public long getMillis() {
        long millis = new Duration(start, end).getMillis();

        if (schedule != null) {
            millis = Math.max(0, millis - exclusion());
        }

        return millis;
    }

    private long exclusion() {
        return this.hourExclusion() + this.dayExclusion();
    }

    private long dayExclusion() {
        long exclusion = 0;
        boolean endsMidnight = schedule.getEndTime().equals(LocalTime.MIDNIGHT);

        DateTime today = start.withTimeAtStartOfDay().plusDays(1);
        DateTime endDay = end.withTimeAtStartOfDay();

        while (today.isBefore(endDay)) {
            DateTime nextday = today.plusDays(1);
            if (schedule.dayOfWeekExcluded(today.getDayOfWeek())
                || schedule.dayOfHolidayExcluded(today)) {
                exclusion += new Interval(today, nextday).toDurationMillis();
            } else {
                exclusion += new Interval(today,
                    schedule.getBeginTime().toDateTime(today)).toDurationMillis();
                if (!endsMidnight) {
                    exclusion += new Interval(schedule.getEndTime().toDateTime(today),
                        nextday).toDurationMillis();
                }
            }

            today = nextday;
        }

        return exclusion;
    }

    private long hourExclusion() {
        long exclusion = 0;

        if (sameDay(start, end)) {
            if (schedule.dayOfWeekExcluded(start.getDayOfWeek())
                || schedule.dayOfHolidayExcluded(start)) {
                exclusion = new Duration(start, end).getMillis();
            } else {
                exclusion = oneDayExclusion(start, end);
            }
        } else {
            if (schedule.dayOfWeekExcluded(start.getDayOfWeek())
                || schedule.dayOfHolidayExcluded(start)) {
                exclusion += new Duration(start,
                    start.withTimeAtStartOfDay().plusDays(1)).getMillis();
            } else {
                exclusion += oneDayExclusion(start,
                    start.withTimeAtStartOfDay().plusDays(1));
            }

            if (schedule.dayOfWeekExcluded(end.getDayOfWeek())
                || schedule.dayOfHolidayExcluded(end)) {
                exclusion += new Duration(end.withTimeAtStartOfDay(),
                    end).getMillis();
            } else {
                exclusion += oneDayExclusion(end.withTimeAtStartOfDay(), end);
            }
        }

        return exclusion;
    }

    private boolean sameDay(DateTime oneDay, DateTime anotherDay) {
        return oneDay.toDateTime(schedule.getTimeZone()).withTimeAtStartOfDay().equals(anotherDay.toDateTime(schedule.getTimeZone()).withTimeAtStartOfDay());
    }

    private long oneDayExclusion(DateTime start, DateTime end) {
        DateTime beginInDay = start.withFields(schedule.getBeginTime());
        DateTime endInDay = start.withFields(schedule.getEndTime());
        if (schedule.getEndTime().equals(LocalTime.MIDNIGHT)) {
            endInDay = endInDay.plusDays(1);
        }
//            DateTime beginInDay =
        start.toDateTime(schedule.getTimeZone()).toLocalDate().toDateTime(schedule.getBeginTime(),
            schedule.getTimeZone());
//            DateTime endInDay =
        start.toDateTime(schedule.getTimeZone()).toLocalDate().toDateTime(schedule.getEndTime(),
            schedule.getTimeZone());
        Duration exclusionHours = Duration.millis(0);

        if (end.isBefore(beginInDay) || start.isAfter(endInDay)) {
            exclusionHours = new Duration(start, end);
        } else {
            if (start.isBefore(beginInDay)) {
                exclusionHours = exclusionHours.plus(new Duration(start, beginInDay));
            }
            if (end.isAfter(endInDay)) {
                exclusionHours = exclusionHours.plus(new Duration(endInDay, end));
            }
        }

        return exclusionHours.getMillis();

    }

}
