package es.us.isa.ppinot.evaluation;

import java.util.Collection;
import java.util.HashSet;

public class MeasureScope {
    private String processId;
    private Collection<String> instances;

    public MeasureScope(String processId) {
        this.processId = processId;
        this.instances = new HashSet<String>();
    }

    public MeasureScope(String processId, Collection<String> instances) {
        this.processId = processId;
        this.instances = instances;
    }

    public String getProcessId() {
        return processId;
    }

    public Collection<String> getInstances() {
        return instances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasureScope)) return false;

        MeasureScope that = (MeasureScope) o;

        if (!instances.equals(that.instances)) return false;
        if (!processId.equals(that.processId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = processId.hashCode();
        result = 31 * result + instances.hashCode();
        return result;
    }
}