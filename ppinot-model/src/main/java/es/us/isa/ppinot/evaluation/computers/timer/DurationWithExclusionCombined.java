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
package es.us.isa.ppinot.evaluation.computers.timer;

import es.us.isa.ppinot.model.ScheduleCombined;
import es.us.isa.ppinot.model.ScheduleItem;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionCombined {

    private DateTime start;
    private DateTime end;
    private ScheduleCombined scheduleCombined;

    public DurationWithExclusionCombined(DateTime start, DateTime end, ScheduleCombined scheduleCombined) {
        this.start = start;
        this.end = end;
        this.scheduleCombined = scheduleCombined;
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

    /**
     * Sum duration of all ScheduleItem slots
     *
     * @return
     */
    public long getMillis() {
        long duration = 0;

        for (ScheduleItem sc : this.combineScheduleForWindow().getSchedules()) {
            duration += new DurationWithExclusion(sc.getFrom(), sc.getTo(), sc.getSchedule()).getMillis();
        }

        return duration;
    }

    public ScheduleCombined combineScheduleForWindow() {

        ScheduleCombined copy = this.getScheduleCombined().copy();
        return this.createScheduleCombinedSlots(copy);

    }

    /**
     * Create ScheduleItem elements contained on window.
     *
     * @param scheduleCombinedList
     * @return
     */
    private ScheduleCombined createScheduleCombinedSlots(ScheduleCombined scheduleCombinedList) {

        ScheduleCombined ret = new ScheduleCombined();

        for (ScheduleItem sc : scheduleCombinedList.getSchedules()) {

            Interval slotInterval = new Interval(sc.getFrom(), sc.getTo());

            if (ret.getSchedules().size() > 0 && !validateSlotIntersected(ret, slotInterval)) {
                throw new IllegalArgumentException("Intersected schedule for interval " + slotInterval);
            }

            boolean startCondition = slotInterval.contains(this.getStart());
            boolean endCondition = slotInterval.contains(this.getEnd());

            if (startCondition || endCondition) {
                if (startCondition && endCondition) {
                    ret.addSchedule(new ScheduleItem(this.getStart(), this.getEnd(), sc.getSchedule()));
                    break;
                } else {

                    DateTime slotFrom;
                    DateTime slotEnd;

                    // Find out which is the schedule slot START
                    if (!ret.isEmpty()) {
                        slotFrom = sc.getFrom();
                    } else {
                        slotFrom = this.getStart();
                    }

                    // Find out which is the schedule slot END
                    if (slotInterval.contains(this.getEnd())) {
                        slotEnd = this.getEnd();
                    } else {
                        slotEnd = sc.getTo();
                    }

                    ret.addSchedule(new ScheduleItem(slotFrom, slotEnd, sc.getSchedule()));

                }
            }
        }

        if (ret.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot build schedule slots for interval with start (" + this.getStart() + ") and end (" + this.getEnd() + ")");
        } else if (!validateSlots(ret)) {
            throw new IllegalArgumentException("Incomplete schedule slots");
        }

        return ret;
    }

    /**
     * Check if all slots have a millisecond of difference.
     *
     * @param scheduleCombinedList
     * @return
     */
    private boolean validateSlots(ScheduleCombined scheduleCombinedList) {

        boolean ret = true;
        DateTime previousTo = new DateTime();

        for (int i = 0; i < scheduleCombinedList.size(); i++) {

            ScheduleItem sc = scheduleCombinedList.get(i);

            if (i == 0) {
                if (!sc.getFrom().equals(this.getStart())) {
                    ret = false;
                    break;
                } else {
                    previousTo = sc.getTo();
                }
            } else if (!previousTo.plusMillis(1).equals(sc.getFrom())) {
                ret = false;
                break;
            } else if (i == scheduleCombinedList.size() - 1) {
                if (!sc.getTo().equals(this.getEnd())) {
                    ret = false;
                    break;
                }
            } else {
                previousTo = sc.getTo();
            }

        }

        return ret;
    }

    /**
     * Check if there is already a ScheduleItem for an interval.
     *
     * @param scheduleCombinedList
     * @param interval
     * @return
     */
    private boolean validateSlotIntersected(ScheduleCombined scheduleCombinedCandidateList, Interval interval) {

        boolean valid = true;

        for (ScheduleItem sc : scheduleCombinedCandidateList.getSchedules()) {
            boolean startCondition = interval.contains(sc.getFrom());
            boolean endCondition = interval.contains(sc.getTo());
            if (startCondition || endCondition) {
                valid = false;
            }
        }

        return valid;

    }

}
