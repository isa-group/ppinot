package es.us.isa.ppinot.model.scope;

import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.base.DataMeasure;
import org.joda.time.DateTime;

/**
 * SimpleTimeFilter
 *
 * This class defines a simple time filter that can be interpreted as follows:
 * If relative is true, then it means "the last {frequency} {period}"
 * If relative is false, then it means "each {frequency} {period}"
 * @author resinas
 */
public class SimpleTimeFilter extends ProcessInstanceFilter {

    private Period period;
    private int frequency;
    private int absoluteStart;
    private boolean relative;
    private boolean includeUnfinished = false;
    private DateTime until = DateTime.now();
    private DataMeasure referencePoint = null;

    public SimpleTimeFilter() {
        super();
    }

    public SimpleTimeFilter(Period period, int frequency, boolean relative) {
        super();
        this.period = period;
        this.frequency = frequency;
        this.relative = relative;
        if (period.equals(Period.DAILY)) {
            this.absoluteStart = 0;
        } else {
            this.absoluteStart = 1;
        }
    }

    public SimpleTimeFilter(Period period, int frequency, int absoluteStart) {
        this(period, frequency, false);
        this.absoluteStart = absoluteStart;
    }

    public SimpleTimeFilter(Period period, int frequency, boolean relative, boolean includeUnfinished) {
        this(period, frequency, relative);
        this.includeUnfinished = includeUnfinished;
    }

    public SimpleTimeFilter(Period period, int frequency, int absoluteStart, boolean includeUnfinished) {
        this(period, frequency, false);
        this.absoluteStart = absoluteStart;
        this.includeUnfinished = includeUnfinished;
    }

    public DateTime getUntil() {
        return until;
    }

    public SimpleTimeFilter setUntil(DateTime until) {
        this.until = until;
        return this;
    }

    public Period getPeriod() {
        return period;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getAbsoluteStart() {
        return absoluteStart;
    }

    public boolean isRelative() {
        return relative;
    }

    public boolean isIncludeUnfinished() {
        return includeUnfinished;
    }

    public DataMeasure getReferencePoint() {
        return referencePoint;
    }

    public SimpleTimeFilter setReferencePoint(DataMeasure referencePoint) {
        this.referencePoint = referencePoint;
        return this;
    }

    public SimpleTimeFilter copy() {
        SimpleTimeFilter copy = new SimpleTimeFilter(period, frequency, absoluteStart, includeUnfinished);
        copy.relative = this.relative;
        copy.until = this.until;
        if (this.referencePoint != null) 
            copy.referencePoint = this.referencePoint.copy();
        
        return copy;
    }
}
