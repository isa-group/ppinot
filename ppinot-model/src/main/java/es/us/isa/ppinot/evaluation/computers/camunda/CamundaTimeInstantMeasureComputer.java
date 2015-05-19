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
import es.us.isa.ppinot.model.base.TimeInstantMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.state.GenericState;

public class CamundaTimeInstantMeasureComputer implements CamundaMeasureComputer{

	private TimeInstantMeasure definition;
    private Integer	value;
  
    public CamundaTimeInstantMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof TimeInstantMeasure)) {
            throw new IllegalArgumentException();
        } 
        this.definition = (TimeInstantMeasure) definition;
        this.value = 0; 
    }
	
	//Counting an Activity has Started or Finished per processInstance
	public List<? extends Measure> compute(HistoryService camundaHistory,
			String processName) {
		// TODO Auto-generated method stub
		List<MeasureInstance> result = new ArrayList<MeasureInstance>();
    	TimeInstantCondition eventTime 	= definition.getWhen();
    	//definition.getWhen().
    	
    	HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
		for (HistoricProcessInstance processInstance:processInstances){
			String processInstanceId = processInstance.getId();
			Long res = (long) 0;
			if (eventTime.getChangesToState().equals(GenericState.START)){
				HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId(eventTime.getAppliesTo());
				res = activityQuery.list().get(0).getStartTime().getTime();			
			}else if (eventTime.getChangesToState().equals(GenericState.END)){
				HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityId(eventTime.getAppliesTo());
				res = activityQuery.finished().list().get(0).getEndTime().getTime();
			}
			//List<HistoricActivityInstance> activities = activityQuery.processInstanceId(processInstanceId).activityId(eventTime.getAppliesTo()).orderByHistoricActivityInstanceStartTime().asc().list();
			MeasureInstance m = new MeasureInstance(definition, res,processName, processInstanceId);
			result.add(m);
		}
		
		return result;
	}

	
}
