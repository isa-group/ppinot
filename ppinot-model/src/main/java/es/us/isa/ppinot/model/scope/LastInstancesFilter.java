package es.us.isa.ppinot.model.scope;

import es.us.isa.ppinot.model.ProcessInstanceFilter;

/**
 * LastInstancesFilter
 *
 * @author resinas
 */
public class LastInstancesFilter extends ProcessInstanceFilter {
    private int numberOfInstances;

    public LastInstancesFilter(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }
}
