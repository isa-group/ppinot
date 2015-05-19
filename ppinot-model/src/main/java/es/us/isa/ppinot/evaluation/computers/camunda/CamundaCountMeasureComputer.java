package es.us.isa.ppinot.evaluation.computers.camunda;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.GenericState;

public class CamundaCountMeasureComputer implements CamundaMeasureComputer {

	private CountMeasure definition;
  
    public CamundaCountMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof CountMeasure)) {
            throw new IllegalArgumentException();
        }
        this.definition = (CountMeasure) definition;
    }
	
	public List<? extends Measure> compute(HistoryService camundaHistory,
			String processName) {
		// TODO Auto-generated method stub
		List<MeasureInstance> result = new ArrayList<MeasureInstance>();
    	TimeInstantCondition eventTime 	= definition.getWhen();
    	HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
		for (HistoricProcessInstance processInstance:processInstances){
			String processInstanceId = processInstance.getId();
			Integer res = 0;
			HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId(eventTime.getAppliesTo());
			if (eventTime.getChangesToState()==GenericState.START){
				res = activityQuery.list().size();
			}else{
				res = activityQuery.finished().list().size();
			}
			//List<HistoricActivityInstance> activities = activityQuery.processInstanceId(processInstanceId).activityId(eventTime.getAppliesTo()).orderByHistoricActivityInstanceStartTime().asc().list();
			MeasureInstance m = new MeasureInstance(definition, res,processName, processInstanceId);
			result.add(m);
		}
		
		return result;
	}

}
