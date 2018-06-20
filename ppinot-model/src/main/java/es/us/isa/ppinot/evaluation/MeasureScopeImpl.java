package es.us.isa.ppinot.evaluation;

import java.util.*;

public class MeasureScopeImpl implements MeasureScope {
    
    private String processId;
    private Set<String> instances;

    public MeasureScopeImpl(String processId) {
        this.processId = processId;
        this.instances = new HashSet<String>();
    }

    public MeasureScopeImpl(String processId, Collection<String> instances) {
        this.processId = processId;
        this.instances = new HashSet<String>(instances);
    }

    @Override
    public String getProcessId() {
        return processId;
    }

    @Override
    public Set<String> getInstances() {
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
    public boolean equivalentTo(MeasureScope scope) {
        if (! getScopeInfo().equals(scope.getScopeInfo())) return false;
        if (! getInstances().equals(scope.getInstances())) return false;

        return true;
    }

    @Override
    public boolean isContainedIn(MeasureScope scope) {
        for (Map.Entry<String, Object> entry : getScopeInfo().entrySet()) {
            if (! scope.getScopeInfo().containsKey(entry.getKey())) return false;
            Object otherValue = scope.getScopeInfo().get(entry.getKey());
            if (entry.getValue() == null &&  otherValue != null) return false;
            if (entry.getValue() != null && ! entry.getValue().equals(otherValue)) return false;
        }

        if (!scope.getInstances().containsAll(instances)) return false;

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