package es.us.isa.ppinot.evaluation;

import java.util.*;

public class MeasureScopeImpl implements MeasureScope {
    
    private String processId;
    private Collection<String> instances;

    public MeasureScopeImpl(String processId) {
        this.processId = processId;
        this.instances = new HashSet<String>();
    }

    public MeasureScopeImpl(String processId, Collection<String> instances) {
        this.processId = processId;
        this.instances = instances;
    }

    @Override
    public String getProcessId() {
        return processId;
    }

    @Override
    public Collection<String> getInstances() {
        return instances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasureScopeImpl)) return false;

        MeasureScopeImpl that = (MeasureScopeImpl) o;

        if (!instances.equals(that.instances)) return false;
        if (!processId.equals(that.processId)) return false;

        return true;
    }

    @Override
    public Map<String, Object> getScopeInfo() {
        return new HashMap<String, Object>();
    }

    @Override
    public int hashCode() {
        int result = processId.hashCode();
        result = 31 * result + instances.hashCode();
        return result;
    }
}