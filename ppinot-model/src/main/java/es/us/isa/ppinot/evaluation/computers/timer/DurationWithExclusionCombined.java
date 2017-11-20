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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 *
 * @author isa-group
 */
public class DurationWithExclusionCombined {

    private DateTime start;
    private DateTime end;
    private List<ScheduleCombined> scheduleCombined;

    public DurationWithExclusionCombined(DateTime start, DateTime end, List<ScheduleCombined> scheduleCombined) {
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

    public List<ScheduleCombined> getScheduleCombined() {
        return scheduleCombined;
    }

    /**
     * Sum duration of all ScheduleCombined slots
     *
     * @return
     */
    public long getMillis() {
        long duration = 0;

        for (ScheduleCombined sc : this.combineScheduleForWindow()) {
            duration += new DurationWithExclusion(sc.getFrom(), sc.getTo(), sc.getSchedule()).getMillis();
        }

        return duration;
    }

    public List<ScheduleCombined> combineScheduleForWindow() {

        List<ScheduleCombined> copy = new ArrayList(this.getScheduleCombined());
        Collections.sort(copy);

        return this.createScheduleCombinedSlots(copy);

    }

    /**
     * Create ScheduleCombined elements contained on window.
     *
     * @param scheduleCombinedList
     * @return
     */
    private List<ScheduleCombined> createScheduleCombinedSlots(List<ScheduleCombined> scheduleCombinedList) {

        List<ScheduleCombined> ret = new ArrayList();

        for (ScheduleCombined sc : scheduleCombinedList) {

            Interval slotInterval = new Interval(sc.getFrom(), sc.getTo());

            if (ret.size() > 0 && !validateSlotIntersected(ret, slotInterval)) {
                throw new IllegalArgumentException("Intersected schedule for interval " + slotInterval);
            }

            boolean startCondition = slotInterval.contains(this.getStart());
            boolean endCondition = slotInterval.contains(this.getEnd());

            if (startCondition || endCondition) {
                if (startCondition && endCondition) {
                    ret.add(new ScheduleCombined(this.getStart(), this.getEnd(), sc.getSchedule()));
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

                    ret.add(new ScheduleCombined(slotFrom, slotEnd, sc.getSchedule()));

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
    private boolean validateSlots(List<ScheduleCombined> scheduleCombinedList) {

        boolean ret = true;
        DateTime previousTo = new DateTime();

        for (int i = 0; i < scheduleCombinedList.size(); i++) {

            ScheduleCombined sc = scheduleCombinedList.get(i);

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
     * Check if there is already a ScheduleCombined for an interval.
     *
     * @param scheduleCombinedList
     * @param interval
     * @return
     */
    private boolean validateSlotIntersected(List<ScheduleCombined> scheduleCombinedCandidateList, Interval interval) {

        boolean valid = true;

        for (ScheduleCombined sc : scheduleCombinedCandidateList) {
            boolean startCondition = interval.contains(sc.getFrom());
            boolean endCondition = interval.contains(sc.getTo());
            if (startCondition || endCondition) {
                valid = false;
            }
        }

        return valid;

    }

}
