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

import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.base.TimeMeasure;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 *
 * @author isa-group
 */
public abstract class MeasureInstanceTimer extends MeasureInstance {

    private boolean processFinished = false;

    public MeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
        super(definition, Double.NaN, processId, instanceId);
    }

    public abstract void starts(DateTime start, LogEntry entry);

    public abstract void ends(DateTime ends);

    protected abstract double computeValue();

    protected boolean isProcessFinished() {
        return processFinished;
    }

    public void processFinished() {
        this.processFinished = true;
    }

    @Override
    public double getValue() {
        double value = computeValue();

        if (!Double.isNaN(value)) {
            long millisValue = (long) value;
            Duration duration = Duration.millis(millisValue);
            Schedule schedule = ((TimeMeasure) definition).getConsiderOnly();
            value = TimeUnit.toTimeUnit(duration, definition.getUnitOfMeasure(), schedule);
        }

        return value;
    }

}
