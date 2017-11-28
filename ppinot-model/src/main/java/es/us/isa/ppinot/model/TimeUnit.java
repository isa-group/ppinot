package es.us.isa.ppinot.model;

import es.us.isa.ppinot.model.schedule.Schedule;
import es.us.isa.ppinot.model.schedule.ScheduleBasic;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.PeriodType;

/**
 * TimeUnit
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class TimeUnit {
    public static final String MILLIS = "millis";
    public static final String SECONDS = "seconds";
    public static final String MINUTES = "minutes";
    public static final String HOURS = "hours";
    public static final String DAYS = "days";
    public static final String WORKINGDAYS = "workingdays";

    public static PeriodType toPeriodType(String timeUnit) {
        PeriodType periodType = PeriodType.millis();

        if (SECONDS.equals(timeUnit)) {
            periodType = PeriodType.seconds();
        } else if (MINUTES.equals(timeUnit)) {
            periodType = PeriodType.minutes();
        } else if (HOURS.equals(timeUnit)) {
            periodType = PeriodType.hours();
        } else if (DAYS.equals(timeUnit)) {
            periodType = PeriodType.days();
        }

        return periodType;
    }

    public static double toTimeUnit(Duration duration, String timeUnit) {
        return toTimeUnit(duration, timeUnit, null);
    }

    public static Duration toTimeUnit(double duration, String timeUnit) {
        Duration convertedDuration = null;

        if (SECONDS.equals(timeUnit)) {
            convertedDuration = Duration.standardSeconds((long) duration);
        } else if (MINUTES.equals(timeUnit)) {
            convertedDuration = Duration.standardMinutes((long) duration);
        } else if (HOURS.equals(timeUnit)) {
            convertedDuration = Duration.standardHours((long) duration);
        } else if (DAYS.equals(timeUnit)) {
            convertedDuration = Duration.standardDays((long) duration);
        }

        return convertedDuration;
    }

    public static double toTimeUnit(Duration duration, String timeUnit, Schedule schedule) {
        double convertedDuration = duration.getMillis();

        if (SECONDS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_SECOND;
        } else if (MINUTES.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_MINUTE;
        } else if (HOURS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_HOUR;
        } else if (DAYS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_DAY;
        } else if (WORKINGDAYS.equals(timeUnit)) {
            if (schedule == null) {
                convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_DAY;
            } else {
                convertedDuration = convertedDuration / (double) ((ScheduleBasic)schedule).millisPerDay();
            }
        }

        return convertedDuration;
    }
}
