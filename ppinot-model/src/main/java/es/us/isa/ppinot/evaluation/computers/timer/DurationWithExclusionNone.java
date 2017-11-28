package es.us.isa.ppinot.evaluation.computers.timer;

import es.us.isa.ppinot.model.schedule.DurationWithExclusion;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * DurationWithExclusionNone
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class DurationWithExclusionNone implements DurationWithExclusion {

    private Duration duration;

    public DurationWithExclusionNone(DateTime start, DateTime end) {
        duration = new Duration(start, end);
    }

    private DurationWithExclusionNone(Duration duration) {
        this.duration = duration;
    }

    @Override
    public long getMillis() {
        return duration.getMillis();
    }

    @Override
    public DurationWithExclusion copy() {
        return new DurationWithExclusionNone(duration);
    }
}
