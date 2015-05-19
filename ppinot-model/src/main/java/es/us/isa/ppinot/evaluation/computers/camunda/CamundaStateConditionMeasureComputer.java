package es.us.isa.ppinot.evaluation.computers.camunda;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.state.GenericState;

public class CamundaStateConditionMeasureComputer implements CamundaMeasureComputer {

	private StateConditionMeasure definition;
	
	public CamundaStateConditionMeasureComputer(MeasureDefinition definition){
		this.definition = (StateConditionMeasure)definition;
	}
	
	//Checking an Activity has Started or Finished anytime per Process Instance
	public List<? extends Measure> compute(HistoryService camundaHistory,
			String processName) {
		List<MeasureInstance> result = new ArrayList<MeasureInstance>();
    	StateCondition eventState 	= definition.getCondition();
    	HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
		for (HistoricProcessInstance processInstance:processInstances){
			String processInstanceId = processInstance.getId();
			HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId(eventState.getAppliesTo());
			Integer res = 0;
			if (eventState.getState()==GenericState.START){
				if (activityQuery.list().size()>0)
					res = 1;
			}else{
				if (activityQuery.finished().list().size()>0)
					res = 1;
			}
			MeasureInstance m = new MeasureInstance(definition, res,processName, processInstanceId);
			result.add(m);
		}
		
		// TODO Auto-generated method stub
		return result;
	}

}
