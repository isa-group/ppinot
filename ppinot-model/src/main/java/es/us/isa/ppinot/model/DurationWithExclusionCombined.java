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
package es.us.isa.ppinot.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionCombined implements DurationWithExclusion {

    private DateTime start;
    private DateTime end;
    private ScheduleCombined scheduleCombined;

    private static Map<Integer, Boolean> scheduleIntervalHashExistGapCache = new HashMap();
    private static Map<Integer, List<Interval>> scheduleIntervalHashGapIntervalCache = new HashMap();

    public DurationWithExclusionCombined(DateTime start, DateTime end, ScheduleCombined scheduleCombined) {
        this.start = start;
        this.end = end;
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
        Map<Interval, ScheduleBasic> map = this.getIntervalScheduleMapping();

        for (Interval schInterval : map.keySet()) {
            Interval overlap = this.getInterval().overlap(schInterval);
            if (overlap != null) {
                duration += new DurationWithExclusionBasic(overlap.getStart(), overlap.getEnd(), map.get(schInterval)).getMillis();
            }
        }

        return duration;

    }

    public static Map<Integer, Boolean> getScheduleIntervalHashExistGapCache() {
        return scheduleIntervalHashExistGapCache;
    }

    public static Map<Integer, List<Interval>> getScheduleIntervalHashGapIntervalCache() {
        return scheduleIntervalHashGapIntervalCache;
    }

    /**
     * Create a map with interval associated to its schedule. This method throws an error if ScheduleCombined: does not consider the period of a whole
     * year; has some intersected periods.
     *
     * @return
     */
    public Map<Interval, ScheduleBasic> getIntervalScheduleMapping() {

        Map<Interval, ScheduleBasic> map = new HashMap();
        List<Interval> allScheduleIntervals = new ArrayList();
        int numYears = this.getEnd().getYear() - this.getStart().getYear();

        for (int y = 0; y <= numYears; y++) {

            int currentYear = this.getStart().getYear() + y;

            for (ScheduleItem si : this.getScheduleCombined().getSchedules()) {
                ScheduleBasic schedule = si.getSchedule();
                DateTimeZone timeZone = schedule != null ? schedule.getTimeZone() : DateTimeZone.getDefault();

                // Schedule interval is "from" until "to" (last millisecond)
                DateTime schFrom = new DateTime(currentYear, si.getFrom().getMonthOfYear(), si.getFrom().getDayOfMonth(), 0, 0, 0, 0, timeZone);
                DateTime schTo = new DateTime(currentYear, si.getTo().getMonthOfYear(), si.getTo().getDayOfMonth(), 0, 0, 0, 0, timeZone).plusDays(1).minusMillis(1);

                Interval schInterval = new Interval(schFrom, schTo);
                allScheduleIntervals.add(schInterval);

                // Filter intervals
                boolean isBeforeAndAfter = schFrom.isBefore(this.getStart()) && schTo.isAfter(this.getEnd());
                boolean isEquals = this.getStart().equals(schFrom) || this.getStart().equals(schTo) || this.getEnd().equals(schFrom) || this.getEnd().equals(schTo);
                if (this.getInterval().contains(schFrom) || this.getInterval().contains(schTo) || isBeforeAndAfter || isEquals) {
                    Interval interval = new Interval(schFrom, schTo);
                    map.put(interval, schedule);
                }
            }
        }

        if (allScheduleIntervals.isEmpty()) {
            throw new IllegalArgumentException("No schedule defined. Please, define at least one");
        } else {
            this.checkScheduleGap(allScheduleIntervals);
            if (map.isEmpty()) {
                throw new IllegalArgumentException("No schedule matched for interval " + this.getInterval());
            }
        }

        return map;
    }

    /**
     * Check if there is any gap on schedules. This method also uses cache to avoid running the code multiple times.
     *
     * @param intervals
     */
    private void checkScheduleGap(List<Interval> intervals) {

        int hash = intervals.hashCode();
        boolean existGap;

        if (!scheduleIntervalHashExistGapCache.containsKey(hash)) {

            // Sort intervals by start value
            List<Interval> sortedIntervals = new ArrayList(intervals);
            Collections.sort(sortedIntervals, new Comparator<Interval>() {
                @Override
                public int compare(Interval i1, Interval i2) {
                    return i1.getStart().compareTo(i2.getStart());
                }
            });

            // Check first and last day of the year is included
            int firstDayOfYear = intervals.get(0).getStart().getDayOfYear();
            int lastDayOfYear = intervals.get(intervals.size() - 1).getEnd().getDayOfYear();
            if (firstDayOfYear != 1 || lastDayOfYear < 365) {
                throw new IllegalArgumentException("First and last day of the year must be included on schedule");
            }

            // Check if there is any gap on schedule greater than 1 millisecond
            Interval prev = null;
            List<Interval> gapTuple = new ArrayList();
            for (Interval interval : sortedIntervals) {
                Interval current = interval;
                if (prev != null) {
                    Interval gap = prev.gap(current);
                    if (gap != null) {
                        if (gap.toDurationMillis() > 1) {
                            gapTuple.add(prev);
                            gapTuple.add(current);
                            break;
                        }
                    } else {
                        throw new IllegalArgumentException("Schedule interval must not coincide (" + prev + ") / (" + current + ")");
                    }
                }
                prev = current;
            }

            existGap = !gapTuple.isEmpty();
            scheduleIntervalHashExistGapCache.put(hash, existGap);

            if (existGap) {
                scheduleIntervalHashGapIntervalCache.put(hash, gapTuple);
            }

        } else {
            existGap = scheduleIntervalHashExistGapCache.get(hash);
        }

        if (existGap) {
            throw new IllegalArgumentException(
                "There is a gap on between the intervals " + scheduleIntervalHashGapIntervalCache.get(hash).get(0)
                + " and " + scheduleIntervalHashGapIntervalCache.get(hash).get(1));
        }

    }

    private Interval getInterval() {
        return new Interval(this.getStart(), this.getEnd());
    }

    @Override
    public DurationWithExclusion copy() {
        return new DurationWithExclusionCombined(start, end, scheduleCombined);
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public ScheduleCombined getScheduleCombined() {
        return scheduleCombined;
    }

}
