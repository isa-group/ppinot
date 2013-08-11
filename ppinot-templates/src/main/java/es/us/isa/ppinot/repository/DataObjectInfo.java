package es.us.isa.ppinot.repository;

import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 10/04/13
 * Time: 23:16
 */
public class DataObjectInfo extends BPElementInfo {
    private Set<String> dataStates;

    public DataObjectInfo() {
    }

    public DataObjectInfo(String id, String name, String type) {
        super(id, name, type);
        this.dataStates = new HashSet<String>();
    }

    public DataObjectInfo(String id, String name, String type, Set<String> dataStates) {
        super(id, name, type);
        this.dataStates = dataStates;
    }

    public Set<String> getDataStates() {
        return dataStates;
    }

    public void addDataState(String dataState) {
        dataStates.add(dataState);
    }

    public void setDataStates(Set<String> dataStates) {
        this.dataStates = dataStates;
    }
}
