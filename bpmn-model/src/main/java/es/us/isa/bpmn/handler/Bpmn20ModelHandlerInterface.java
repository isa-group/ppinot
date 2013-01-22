package es.us.isa.bpmn.handler;

import java.util.Map;

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
	
	public Map<String, TTask> getTaskMap();
	public Map<String, TStartEvent> getStartEventMap();
	public Map<String, TEndEvent> getEndEventMap();
	public Map<String, TDataObject> getDataObjectMap();
	public Map<String, TSequenceFlow> getSequenceFlowMap();
	public Map<String, TGateway> getGatewayMap();
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap();
	public Map<String, TSubProcess> getSubProcessMap();
}
