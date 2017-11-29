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
package es.us.isa.ppinot.model.schedule;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Map;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionCombined implements DurationWithExclusion {

    private Interval interval;
    private ScheduleCombined scheduleCombined;

    public DurationWithExclusionCombined(DateTime start, DateTime end, ScheduleCombined scheduleCombined) {
        this.interval = new Interval(start, end);
        this.scheduleCombined = scheduleCombined;
    }

    /**
     * Sum duration of all ScheduleItem slots
     *
     * @return
     */
    @Override
    public long getMillis() {
        long duration = 0;

        // Get schedule intervals
        Map<Interval, ScheduleBasic> map = scheduleCombined.getIntervalScheduleMapping(interval.getStart().getYear(), interval.getEnd().getYear());

        for (Interval schInterval : map.keySet()) {
            Interval overlap = interval.overlap(schInterval);
            if (overlap != null) {
                duration += new DurationWithExclusionBasic(overlap.getStart(), overlap.getEnd(), map.get(schInterval)).getMillis();
            }
        }

        return duration;

    }


    @Override
    public DurationWithExclusion copy() {
        return new DurationWithExclusionCombined(interval.getStart(), interval.getEnd(), scheduleCombined.copy());
    }

}
