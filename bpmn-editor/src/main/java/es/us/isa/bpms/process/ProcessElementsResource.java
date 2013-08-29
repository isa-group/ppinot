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
import java.util.ArrayList;
import java.util.List;

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
    public ProcessInfo getProcessInfo(@PathParam("id") String id) {
        Bpmn20ModelHandler bpmnModelHandler = getBpmnModelHandler(id);

        return new ProcessInfo(
                listActivities(bpmnModelHandler),
                listGateways(bpmnModelHandler),
                listEvents(bpmnModelHandler),
                listDataObjects(bpmnModelHandler)
        );
    }

    @Path("/activities")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getActivityNames(@PathParam("id") String id) {
        return listActivities(getBpmnModelHandler(id));
    }

    private List<BPElementInfo> listActivities(Bpmn20ModelHandler bpmnModelHandler) {
        List<BPElementInfo> activities = new ArrayList<BPElementInfo>();
        for (TTask task : bpmnModelHandler.getTaskMap().values()) {
            activities.add(new BPElementInfo(task.getId(), task.getName(), "task"));
        }
        for (TSubProcess subProcess : bpmnModelHandler.getSubProcessMap().values()) {
            activities.add(new BPElementInfo(subProcess.getId(), subProcess.getName(), "subprocess"));
        }
        return activities;
    }

    @Path("/events")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getEvents(@PathParam("id") String id) {
        return listEvents(getBpmnModelHandler(id));
    }

    private List<BPElementInfo> listEvents(Bpmn20ModelHandler bpmnModelHandler) {
        List<BPElementInfo> events = new ArrayList<BPElementInfo>();

        for (TStartEvent startEvent : bpmnModelHandler.getStartEventMap().values()) {
            events.add(new BPElementInfo(startEvent.getId(), startEvent.getName(), "startEvent"));
        }

        for (TEndEvent endEvent : bpmnModelHandler.getEndEventMap().values()) {
            events.add(new BPElementInfo(endEvent.getId(), endEvent.getName(), "endEvent"));
        }
        return events;
    }

    @Path("/gateways")
    @Produces("application/json")
    @GET
    public List<BPElementInfo> getGateways(@PathParam("id") String id) {
        return listGateways(getBpmnModelHandler(id));
    }

    private List<BPElementInfo> listGateways(Bpmn20ModelHandler bpmnModelHandler) {
        List<BPElementInfo> gatewaysInfo = new ArrayList<BPElementInfo>();
        for (TGateway gateway : bpmnModelHandler.getGatewayMap().values()) {
            gatewaysInfo.add(new BPElementInfo(gateway.getId(), gateway.getName(), "gateway"));
        }
        return gatewaysInfo;
    }

    @Path("/dataobjects")
    @Produces("application/json")
    @GET
    public List<DataObjectInfo> getDataObjects(@PathParam("id") String id) {
        return listDataObjects(getBpmnModelHandler(id));
    }

    private List<DataObjectInfo> listDataObjects(Bpmn20ModelHandler bpmnModelHandler) {
        List<DataObjectInfo> dataObjectsInfo = new ArrayList<DataObjectInfo>();
        for (TDataObject dataObject : bpmnModelHandler.getDataObjectMap().values()) {
            DataObjectInfo objectInfo = contained(dataObject.getId(), dataObjectsInfo);
            if (objectInfo == null) {
                objectInfo = new DataObjectInfo(dataObject.getId(), dataObject.getName(), "dataObject");
                dataObjectsInfo.add(objectInfo);
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

}