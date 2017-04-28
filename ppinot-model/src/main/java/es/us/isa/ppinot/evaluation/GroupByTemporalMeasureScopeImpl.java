package es.us.isa.ppinot.evaluation;

import java.util.Collection;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author feserafim
 * @version 1.0
 */
public class GroupByTemporalMeasureScopeImpl extends TemporalMeasureScopeImpl implements GroupByMeasureScope {

    private Map<String, String> groupParameters;

    public GroupByTemporalMeasureScopeImpl(String processId, DateTime start, DateTime end) {
        super(processId, start, end);
    }

    public GroupByTemporalMeasureScopeImpl(String processId, Collection<String> instances, DateTime start, DateTime end) {
        super(processId, instances, start, end);
    }

    @Override
    public Map<String, String> getGroupParameters() {
        return this.groupParameters;
    }

    @Override
    public void setGroupParameters(Map<String, String> groupParameters) {
        this.groupParameters = groupParameters;
    }

    @Override
    public Map<String, Object> getScopeInfo() {
        Map<String,Object> scope = super.getScopeInfo();
        scope.putAll(groupParameters);

        return scope;
    }
}
