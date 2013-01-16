package es.us.isa.bpmn.handler;

import java.util.List;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;


public interface Bpmn20ModelHandlerInterface extends ModelHandleInterface {
	
	public abstract List<TTask> getTaskList();
	public abstract List<TStartEvent> getStartEventList();
	public abstract List<TEndEvent> getEndEventList();
	public abstract List<TDataObject> getDataObjectList();
	public abstract List<TSequenceFlow> getSequenceFlowList();
	public abstract List<TGateway> getGatewayList();
	public abstract List<TExclusiveGateway> getExclusiveGatewayList();
	public abstract List<TSubProcess> getSubProcessList();

	public abstract TTask isTask(String idActivity) throws Exception;
	public abstract TSubProcess isSubProcess(String idActivity) throws Exception;
	public abstract TDataObject isDataObject(String idActivity) throws Exception;
	public abstract TStartEvent isStartEvent(String idActivity) throws Exception;
	public abstract TEndEvent isEndEvent(String idActivity) throws Exception;
}
