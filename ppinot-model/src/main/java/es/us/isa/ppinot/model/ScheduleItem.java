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
 * along with this program; if not, write end the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Bosendn, MA  02111-1307, USA.
 */
package es.us.isa.ppinot.model;

import es.us.isa.ppinot.handler.json.DateTimeDeserializer;
import es.us.isa.ppinot.handler.json.ScheduleDeserializer;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author isa-group
 */
@JsonSerialize(using = ToStringSerializer.class)
public class ScheduleItem implements Comparable<ScheduleItem> {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonProperty("from")
    private DateTime from;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonProperty("to")
    private DateTime to;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ScheduleDeserializer.class)
    @JsonProperty("schedule")
    private Schedule schedule;

    public ScheduleItem() {
    }

    public ScheduleItem(DateTime start, DateTime end, Schedule schedule) {
        DateTimeZone timeZone = schedule != null ? schedule.getTimeZone() : DateTimeZone.getDefault();

        this.from = start.toDateTime(timeZone);
        this.to = end.toDateTime(timeZone);
        this.schedule = schedule;
    }

    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
        return to;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public int compareTo(ScheduleItem o) {
        return this.getFrom().compareTo(o.getFrom());
    }

}
