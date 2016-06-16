package es.us.isa.ppinot.evaluation;

import java.util.Map;

/**
 *
 * @author feserafim
 */
public interface GroupByMeasureScope extends MeasureScope {

    public Map<String, String> getGroupParameters();

    public void setGroupParameters(Map<String, String> groupParameters);

}
