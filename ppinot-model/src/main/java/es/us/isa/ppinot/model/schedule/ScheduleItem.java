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
package es.us.isa.ppinot.model.schedule;

import es.us.isa.ppinot.handler.json.MonthDayDeserializer;
import es.us.isa.ppinot.handler.json.MonthDaySerializer;
import es.us.isa.ppinot.handler.json.ScheduleBasicDeserializer;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.MonthDay;

/**
 *
 * @author isa-group
 */
public class ScheduleItem implements Comparable<ScheduleItem> {

    @JsonSerialize(using = MonthDaySerializer.class)
    @JsonDeserialize(using = MonthDayDeserializer.class)
    @JsonProperty("from")
    private MonthDay from;

    @JsonSerialize(using = MonthDaySerializer.class)
    @JsonDeserialize(using = MonthDayDeserializer.class)
    @JsonProperty("to")
    private MonthDay to;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ScheduleBasicDeserializer.class)
    @JsonProperty("schedule")
    private ScheduleBasic schedule;

    public ScheduleItem() {
    }

    public ScheduleItem(MonthDay from, MonthDay to, ScheduleBasic schedule) {
        this.from = from;
        this.to = to;
        this.schedule = schedule;
    }

    public MonthDay getFrom() {
        return from;
    }

    public MonthDay getTo() {
        return to;
    }

    public ScheduleBasic getSchedule() {
        return schedule;
    }

    @Override
    public int compareTo(ScheduleItem o) {
        return this.getFrom().compareTo(o.getFrom());
    }

}
