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
    private boolean relative;

    public SimpleTimeFilter() {
        super();
    }

    public SimpleTimeFilter(Period period, int frequency, boolean relative) {
        super();
        this.period = period;
        this.frequency = frequency;
        this.relative = relative;
    }

    public Period getPeriod() {
        return period;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean isRelative() {
        return relative;
    }
}
