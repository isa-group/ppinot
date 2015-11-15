package es.us.isa.ppinot.model;

import es.us.isa.ppinot.handler.json.LocalTimeDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

/**
 * Schedule
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class Schedule {

    private int beginDay;
    private int endDay;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime beginTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;

    private Schedule() {}

    public Schedule(int beginDay, int endDay, LocalTime beginTime, LocalTime endTime) {
        this.beginDay = beginDay;
        this.endDay = endDay;
        this.beginTime = beginTime;
        this.endTime = endTime;

        if (beginDay < DateTimeConstants.MONDAY || beginDay > DateTimeConstants.SUNDAY || endDay < DateTimeConstants.MONDAY || endDay > DateTimeConstants.SUNDAY) {
            throw new RuntimeException("Invalid day of week");
        }
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

    public boolean dayOfWeekExcluded(int dayOfWeek) {
        return (dayOfWeek < getBeginDay() || dayOfWeek > getEndDay());
    }
}
