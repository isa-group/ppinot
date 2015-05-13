package es.us.isa.ppinot.model;

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
        double convertedDuration = duration.getMillis();

        if (SECONDS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_SECOND;
        } else if (MINUTES.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_MINUTE;
        } else if (HOURS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_HOUR;
        } else if (DAYS.equals(timeUnit)) {
            convertedDuration = convertedDuration / (double) DateTimeConstants.MILLIS_PER_DAY;
        }

        return convertedDuration;
    }
}
