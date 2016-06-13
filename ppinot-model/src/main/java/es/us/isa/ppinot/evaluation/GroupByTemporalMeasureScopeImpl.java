package es.us.isa.ppinot.evaluation;

import java.util.Map;
import org.joda.time.DateTime;

/**
 * Applied Software Engineering Research Group (ISA Group) University of
 * Sevilla, Spain
 *
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 * @version 1.0
 */
public class GroupByTemporalMeasureScopeImpl extends TemporalMeasureScopeImpl implements GroupByMeasureScope {
    
    private Map<String, String> groupParameters;

    public GroupByTemporalMeasureScopeImpl(String processId, DateTime start, DateTime end) {
        super(processId, start, end);
    }
    
    @Override
    public Map<String,String> getGroupParameters() {
        return this.groupParameters;
    }
    
    public void setGroupParameters(Map<String, String> groupParameters) {
        this.groupParameters = groupParameters;
    }

}
