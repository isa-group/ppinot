package es.us.isa.ppinot.model.schedule;

import es.us.isa.ppinot.handler.json.LocalTimeDeserializer;
import es.us.isa.ppinot.handler.json.ScheduleBasicDeserializer;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * ScheduleBasic Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
@JsonDeserialize(using = ScheduleBasicDeserializer.class)
public class ScheduleBasic implements Schedule {

    private int beginDay;
    private int endDay;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime beginTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;

    private DateTimeZone timeZone = DateTimeZone.forID("Europe/Madrid");
    private List<DateTime> holidays;

    public static final ScheduleBasic SCHEDULE_24X7 = new ScheduleBasic(1, 7, new LocalTime(0, 0), new LocalTime(0, 0));

    private ScheduleBasic() {
    }

    public ScheduleBasic(int beginDay, int endDay, LocalTime beginTime, LocalTime endTime) {
        this.beginDay = beginDay;
        this.endDay = endDay;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.holidays = new ArrayList<DateTime>();

        if (beginDay < DateTimeConstants.MONDAY || beginDay > DateTimeConstants.SUNDAY || endDay < DateTimeConstants.MONDAY || endDay > DateTimeConstants.SUNDAY) {
            throw new RuntimeException("Invalid day of week");
        }
    }

    public ScheduleBasic(int beginDay, int endDay, LocalTime beginTime, LocalTime endTime, List<DateTime> holidays) {
        this.beginDay = beginDay;
        this.endDay = endDay;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.holidays = holidays;

        if (beginDay < DateTimeConstants.MONDAY || beginDay > DateTimeConstants.SUNDAY || endDay < DateTimeConstants.MONDAY || endDay > DateTimeConstants.SUNDAY) {
            throw new RuntimeException("Invalid day of week");
        }
    }

    @Override
    public DurationWithExclusion computeDuration(DateTime start, DateTime end) {
        return new DurationWithExclusionBasic(start, end, this);
    }

    public int getBeginDay() {
        return beginDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<DateTime> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<DateTime> holidays) {
        this.holidays = holidays;
    }

    public boolean dayOfWeekExcluded(int dayOfWeek) {
        return (dayOfWeek < getBeginDay() || dayOfWeek > getEndDay());
    }

    public boolean dayOfHolidayExcluded(DateTime day) {
        DateTime dayInZone = day.withZone(getTimeZone());
        boolean result = false;
        if (this.getHolidays() != null) {
            for (DateTime h : holidays) {
                if (h.getYear() == dayInZone.getYear() && h.getMonthOfYear() == dayInZone.getMonthOfYear() && h.getDayOfMonth() == dayInZone.getDayOfMonth()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public long millisPerDay() {
        if (beginTime.equals(new LocalTime(0, 0)) && endTime.equals(new LocalTime(23, 59))) {
            return DateTimeConstants.MILLIS_PER_DAY;
        } else {
            return Seconds.secondsBetween(beginTime, endTime).toStandardDuration().getMillis();
        }
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }

    public ScheduleBasic setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    @JsonValue
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        sb.append(fromDayOfWeek(beginDay)).append("-").append(fromDayOfWeek(endDay))
            .append("T").append(dtf.print(beginTime)).append("-").append(dtf.print(endTime));

        if (holidays != null && !holidays.isEmpty()) {
            sb.append("/H");
        }

        return sb.toString();
    }

    public static ScheduleBasic parse(String stringSchedule) {
        String[] array = stringSchedule.split("T");
        String[] days = array[0].split("-");
        String[] hours = array[1].split("-");
        String[] holidaysParam = hours[1].split("/");

        boolean hasHolidays = holidaysParam.length == 2 && holidaysParam[1].equals("H"); //Si el string tiene /H
        if (hasHolidays) {
            hours[1] = holidaysParam[0];
        }

        Integer startDay = parseToDayOfWeek(days[0]);
        Integer endDay = parseToDayOfWeek(days[1]);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");

        LocalTime startTime = dtf.parseDateTime(hours[0]).toLocalDateTime().toLocalTime();
        LocalTime endTime = dtf.parseDateTime(hours[1]).toLocalDateTime().toLocalTime();

        ScheduleBasic schedule = null;
        if (hasHolidays) {
            schedule = new ScheduleBasic(startDay, endDay, startTime, endTime, DefaultHolidays.getDays());
        } else {
            schedule = new ScheduleBasic(startDay, endDay, startTime, endTime);
        }

        return schedule;
    }

    private static Integer parseToDayOfWeek(String day) {
        Integer ret = null;
        if (day.equals("L")) {
            ret = DateTimeConstants.MONDAY;
        } else if (day.equals("M")) {
            ret = DateTimeConstants.TUESDAY;
        } else if (day.equals("X")) {
            ret = DateTimeConstants.WEDNESDAY;
        } else if (day.equals("J")) {
            ret = DateTimeConstants.THURSDAY;
        } else if (day.equals("V")) {
            ret = DateTimeConstants.FRIDAY;
        } else if (day.equals("S")) {
            ret = DateTimeConstants.SATURDAY;
        } else if (day.equals("D")) {
            ret = DateTimeConstants.SUNDAY;
        }
        return ret;
    }

    private static String fromDayOfWeek(int day) {
        String ret = "";
        if (day == DateTimeConstants.MONDAY) {
            ret = "L";
        } else if (day == DateTimeConstants.TUESDAY) {
            ret = "M";
        } else if (day == DateTimeConstants.WEDNESDAY) {
            ret = "X";
        } else if (day == DateTimeConstants.THURSDAY) {
            ret = "J";
        } else if (day == DateTimeConstants.FRIDAY) {
            ret = "V";
        } else if (day == DateTimeConstants.SATURDAY) {
            ret = "S";
        } else if (day == DateTimeConstants.SUNDAY) {
            ret = "D";
        }

        return ret;
    }

}
