package es.us.isa.ppinot.evaluation;

import org.joda.time.DateTime;

/**
 *
 * @author feserafim
 */
public interface TemporalMeasureScope extends MeasureScope {

    DateTime getStart();

    DateTime getEnd();

}
