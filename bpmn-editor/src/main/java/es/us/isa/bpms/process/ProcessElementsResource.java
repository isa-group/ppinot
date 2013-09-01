package es.us.isa.bpms.process;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerImpl;
import es.us.isa.bpmn.xmlClasses.bpmn20.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.*;

/**
 * User: resinas
 * Date: 11/04/13
 * Time: 07:58
 */
public class ProcessElementsResource {
    private InputStream processStream;

    public ProcessElementsResource(InputStream processStream) {
        this.processStream = processStream;
    }

    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Collection<ProcessInfo> getProcessInfo(@PathParam("id") String id) {
        Bpmn20ModelHandler bpmnModelHandler = getBpmnModelHandler(id);
        Map<String, List<BPElementInfo>> activities = listActivities(bpmnModelHandler);
        Map<String, List<BPElementInfo>> gateways = listGateways(bpmnModelHandler);
        Map<String, List<BPElementInfo>> events = listEvents(bpmnModelHandler);
        Map<String, List<DataObjectInfo>> dataObjects = listDataObjects(bpmnModelHandler);
        List<ProcessInfo> processes = new ArrayList<ProcessInfo>();

        for (TProcess p : bpmnModelHandler.getProcesses()) {
            String processId = p.getId();
            processes.add(new ProcessInfo(processId,
                    p.getName(),
                    activities.get(processId),
                    gateways.get(processId),
                    events.get(processId),
                    dataObjects.get(processId)));
        }

        return processes;
    }

    @Path("/activities")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getActivityNames(@PathParam("id") String id) {
        return group(listActivities(getBpmnModelHandler(id)));
    }

    @Path("/events")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getEvents(@PathParam("id") String id) {
        return group(listEvents(getBpmnModelHandler(id)));
    }

    @Path("/gateways")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getGateways(@PathParam("id") String id) {
        return group(listGateways(getBpmnModelHandler(id)));
    }


    @Path("/dataobjects")
    @Produces("application/json")
    @GET
    public List<DataObjectInfo> getDataObjects(@PathParam("id") String id) {
        return group(listDataObjects(getBpmnModelHandler(id)));
    }

    private <T> List<T> group(Map<String, List<T>> stringListMap) {
        List<T> result = new ArrayList<T>();
        for (List<T> list : stringListMap.values()) {
            result.addAll(list);
        }
        return result;
    }

    private Map<String,List<BPElementInfo>> listActivities(Bpmn20ModelHandler bpmnModelHandler) {
        ElementMap<BPElementInfo> activities = new ElementMap<BPElementInfo>(bpmnModelHandler);

        for (TTask task : bpmnModelHandler.getTaskMap().values()) {
            BPElementInfo bpElementInfo = new BPElementInfo(task.getId(), task.getName(), "task");
            activities.update(bpElementInfo);
        }
        for (TSubProcess subProcess : bpmnModelHandler.getSubProcessMap().values()) {
            activities.update(new BPElementInfo(subProcess.getId(), subProcess.getName(), "subprocess"));
        }
        return activities;
    }

    private Map<String,List<BPElementInfo>> listEvents(Bpmn20ModelHandler bpmnModelHandler) {
        ElementMap<BPElementInfo> events = new ElementMap<BPElementInfo>(bpmnModelHandler);

        for (TStartEvent startEvent : bpmnModelHandler.getStartEventMap().values()) {
            events.update(new BPElementInfo(startEvent.getId(), startEvent.getName(), "startEvent"));
        }

        for (TEndEvent endEvent : bpmnModelHandler.getEndEventMap().values()) {
            events.update(new BPElementInfo(endEvent.getId(), endEvent.getName(), "endEvent"));
        }
        return events;
    }

    private Map<String,List<BPElementInfo>> listGateways(Bpmn20ModelHandler bpmnModelHandler) {
        ElementMap<BPElementInfo> gatewaysInfo = new ElementMap<BPElementInfo>(bpmnModelHandler);
        for (TGateway gateway : bpmnModelHandler.getGatewayMap().values()) {
            gatewaysInfo.update(new BPElementInfo(gateway.getId(), gateway.getName(), "gateway"));
        }
        return gatewaysInfo;
    }

    private Map<String,List<DataObjectInfo>> listDataObjects(Bpmn20ModelHandler bpmnModelHandler) {
        ElementMap<DataObjectInfo> dataObjectsInfo = new ElementMap<DataObjectInfo>(bpmnModelHandler);

        for (TDataObject dataObject : bpmnModelHandler.getDataObjectMap().values()) {
            DataObjectInfo objectInfo = contained(dataObject.getId(), group(dataObjectsInfo));
            if (objectInfo == null) {
                objectInfo = new DataObjectInfo(dataObject.getId(), dataObject.getName(), "dataObject");
                dataObjectsInfo.update(objectInfo);
            }

            TDataState state = dataObject.getDataState();
            if (state != null) {
                objectInfo.addDataState(state.getName());
            }
        }
        return dataObjectsInfo;
    }

    private DataObjectInfo contained(String id, List<DataObjectInfo> infoList) {
        DataObjectInfo contained = null;
        for (DataObjectInfo d : infoList) {
            if (id != null && id.equals(d.getId())) {
                contained = d;
                break;
            }
        }
        return contained;
    }

    private Bpmn20ModelHandler getBpmnModelHandler(String id) {
        Bpmn20ModelHandler bpmnModelHandler;
        try {
            bpmnModelHandler = new Bpmn20ModelHandlerImpl();
            bpmnModelHandler.load(processStream);
        } catch (Exception e) {
            throw new RuntimeException("Problem loading process " + id, e);
        }
        return bpmnModelHandler;
    }

    private class ElementMap<T extends BPElementInfo> extends HashMap<String, List<T>> {
        private Bpmn20ModelHandler bpmnModelHandler;

        public ElementMap(Bpmn20ModelHandler bpmnModelHandler) {
            super();
            this.bpmnModelHandler = bpmnModelHandler;
        }

        public void update(T bpElementInfo) {
            String processId = bpmnModelHandler.getProcessOfElement(bpElementInfo.getId()).getId();
            List<T> activities = get(processId);
            if (activities == null) {
                activities = new ArrayList<T>();
                put(processId, activities);
            }

            activities.add(bpElementInfo);
        }

    }

}
