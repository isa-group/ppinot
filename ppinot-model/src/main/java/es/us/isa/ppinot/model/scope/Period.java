package es.us.isa.ppinot.model.scope;

/**
 * Period
 *
 * @author resinas
 */
public enum Period {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    public static org.joda.time.Period toJodaPeriod(Period p, int frequency) {
        org.joda.time.Period period;

        if (es.us.isa.ppinot.model.scope.Period.DAILY.equals(p)) {
            period = org.joda.time.Period.days(frequency);
        } else if (es.us.isa.ppinot.model.scope.Period.WEEKLY.equals(p)) {
            period = org.joda.time.Period.weeks(frequency);
        } else if (es.us.isa.ppinot.model.scope.Period.MONTHLY.equals(p)) {
            period = org.joda.time.Period.months(frequency);
        } else {
            period = org.joda.time.Period.years(frequency);
        }

        return period;
    }
}
