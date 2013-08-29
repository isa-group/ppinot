package es.us.isa.bpms.process;

import java.util.List;

/**
 * ProcessInfo
 *
 * @author resinas
 */
public class ProcessInfo {
    private List<BPElementInfo> activities;
    private List<BPElementInfo> gateways;
    private List<BPElementInfo> events;
    private List<DataObjectInfo> dataObjects;

    public ProcessInfo(List<BPElementInfo> activities, List<BPElementInfo> gateways, List<BPElementInfo> events, List<DataObjectInfo> dataObjects) {
        this.activities = activities;
        this.gateways = gateways;
        this.events = events;
        this.dataObjects = dataObjects;
    }

    public List<BPElementInfo> getActivities() {
        return activities;
    }

    public List<BPElementInfo> getGateways() {
        return gateways;
    }

    public List<BPElementInfo> getEvents() {
        return events;
    }

    public List<DataObjectInfo> getDataObjects() {
        return dataObjects;
    }
}
