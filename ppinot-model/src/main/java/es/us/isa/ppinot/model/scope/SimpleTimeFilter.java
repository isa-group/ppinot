package es.us.isa.ppinot.model.scope;

import es.us.isa.ppinot.model.ProcessInstanceFilter;

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
}
