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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import java.util.*;


/**
 * @author isa-group
 */
@JsonDeserialize(as = ScheduleCombined.class)
public class ScheduleCombined implements Schedule {

    private static Map<Integer, Boolean> scheduleIntervalHashAbutsCache = new HashMap();
    private static Map<Integer, List<Interval>> scheduleIntervalHashGapIntervalCache = new HashMap();

    private List<ScheduleItem> schedules;

    public ScheduleCombined() {
        this.schedules = new ArrayList<ScheduleItem>();
    }

    @JsonCreator
    public ScheduleCombined(List<ScheduleItem> schedules) {
        this.schedules = schedules;
        this.checkScheduleGap(getIntervalScheduleMapping(DateTime.now().getYear(), DateTime.now().getYear()).keySet());
    }

    public static Map<Integer, Boolean> getScheduleIntervalHashAbutsCache() {
        return scheduleIntervalHashAbutsCache;
    }

    public static Map<Integer, List<Interval>> getScheduleIntervalHashGapIntervalCache() {
        return scheduleIntervalHashGapIntervalCache;
    }

    @JsonValue
    public List<ScheduleItem> getSchedules() {
        return schedules;
    }

    public void addSchedule(ScheduleItem item) {
        this.getSchedules().add(item);
    }

    public boolean isEmpty() {
        return this.getSchedules().isEmpty();
    }

    public int size() {
        return this.getSchedules().size();
    }

    public ScheduleItem get(int i) {
        return this.getSchedules().get(i);
    }

    public ScheduleCombined copy() {
        List<ScheduleItem> copy = new ArrayList<ScheduleItem>(this.getSchedules());
        Collections.sort(copy);

        return new ScheduleCombined(copy);
    }

    @Override
    public DurationWithExclusion computeDuration(DateTime start, DateTime end) {
        return new DurationWithExclusionCombined(start, end, this);
    }

    /**
     * Create a map with interval associated to its schedule. This method throws an error if ScheduleCombined: does not consider the period of a whole
     * year; has some intersected periods.
     *
     * @return
     */
    public Map<Interval, ScheduleBasic> getIntervalScheduleMapping(int startYear, int endYear) {

        Map<Interval, ScheduleBasic> map = new HashMap<Interval, ScheduleBasic>();
        int numYears = endYear - startYear;

        for (int y = 0; y <= numYears; y++) {

            int currentYear = startYear + y;

            for (ScheduleItem si : getSchedules()) {
                ScheduleBasic schedule = si.getSchedule();
                DateTimeZone timeZone = schedule != null ? schedule.getTimeZone() : DateTimeZone.getDefault();

                // Schedule interval is "from" until "to" (last millisecond)
                DateTime schFrom = new DateTime(currentYear, si.getFrom().getMonthOfYear(), si.getFrom().getDayOfMonth(), 0, 0, 0, 0, timeZone);
                DateTime schTo = new DateTime(currentYear, si.getTo().getMonthOfYear(), si.getTo().getDayOfMonth(), 0, 0, 0, 0, timeZone).plusDays(1);

                Interval interval = new Interval(schFrom, schTo);
                map.put(interval, schedule);

                // Filter intervals
//                boolean isBeforeAndAfter = schFrom.isBefore(this.getStart()) && schTo.isAfter(this.getEnd());
//                boolean isEquals = this.getStart().equals(schFrom) || this.getStart().equals(schTo) || this.getEnd().equals(schFrom) || this.getEnd().equals(schTo);
//                if (this.getInterval().contains(schFrom) || this.getInterval().contains(schTo) || isBeforeAndAfter || isEquals) {
//                    Interval interval = new Interval(schFrom, schTo);
//                    map.put(interval, schedule);
//                }
            }
        }

        if (map.keySet().isEmpty()) {
            throw new IllegalArgumentException("No schedule defined. Please, define at least one");
        } else {
            if (map.isEmpty()) {
                throw new IllegalArgumentException("No schedule matched for years " + startYear + ", " + endYear);
            }
        }

        return map;
    }

    /**
     * Check if there is any gap on schedules. This method also uses cache to avoid running the code multiple times.
     *
     * @param intervals
     */
    private void checkScheduleGap(Collection<Interval> intervals) {
        List<Interval> sortedIntervals = new ArrayList<Interval>(intervals);
        Collections.sort(sortedIntervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.getStart().compareTo(i2.getStart());
            }
        });

        int hash = sortedIntervals.hashCode();
        boolean abuts = true;

        if (!ScheduleCombined.scheduleIntervalHashAbutsCache.containsKey(hash)) {

            // Sort intervals by start value

            // Check first and last day of the year is included
            int firstDayOfYear = sortedIntervals.get(0).getStart().getDayOfYear();
            int lastDayOfYear = sortedIntervals.get(intervals.size() - 1).getEnd().minusMillis(1).getDayOfYear();
            if (firstDayOfYear != 1 || lastDayOfYear < 365) {
                throw new IllegalArgumentException("First and last day of the year must be included on schedule");
            }

            // Check if there is any gap on schedule greater than 1 millisecond
            Interval prev = null;
            for (Interval current : sortedIntervals) {
                if (prev != null) {
                    if (!prev.abuts(current)) {
                        abuts = false;
                    }
                }
                prev = current;
            }

            ScheduleCombined.scheduleIntervalHashAbutsCache.put(hash, abuts);

        } else {
            abuts = ScheduleCombined.scheduleIntervalHashAbutsCache.get(hash);
        }

        if (!abuts) {
            throw new IllegalArgumentException("Consecutive interval must abut");
        }

    }

}
