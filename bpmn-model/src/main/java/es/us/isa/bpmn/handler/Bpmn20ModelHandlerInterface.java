package es.us.isa.bpmn.handler;

import java.util.List;

import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TEndEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExclusiveGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TGateway;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSequenceFlow;
import es.us.isa.bpmn.xmlClasses.bpmn20.TStartEvent;
import es.us.isa.bpmn.xmlClasses.bpmn20.TSubProcess;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;


public interface Bpmn20ModelHandlerInterface extends ModelHandleInterface {
	
	public TProcess getProcess();
	
	public List<TTask> getTaskList();
	public List<TStartEvent> getStartEventList();
	public List<TEndEvent> getEndEventList();
	public List<TDataObject> getDataObjectList();
	public List<TSequenceFlow> getSequenceFlowList();
	public List<TGateway> getGatewayList();
	public List<TExclusiveGateway> getExclusiveGatewayList();
	public List<TSubProcess> getSubProcessList();

	public TTask isTask(String idActivity) throws Exception;
	public TSubProcess isSubProcess(String idActivity) throws Exception;
	public TDataObject isDataObject(String idActivity) throws Exception;
	public TStartEvent isStartEvent(String idActivity) throws Exception;
	public TEndEvent isEndEvent(String idActivity) throws Exception;
}
