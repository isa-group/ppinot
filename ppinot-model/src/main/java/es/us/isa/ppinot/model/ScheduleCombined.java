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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author isa-group
 */
public class ScheduleCombined implements Comparable<ScheduleCombined> {

    private DateTime from;
    private DateTime to;
    private Schedule schedule;

    public ScheduleCombined(DateTime start, DateTime end, Schedule schedule) {
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
    public int compareTo(ScheduleCombined o) {
        return this.getFrom().compareTo(o.getFrom());
    }
    
}
