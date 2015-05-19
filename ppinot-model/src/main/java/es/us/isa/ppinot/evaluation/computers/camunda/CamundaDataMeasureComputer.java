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
import es.us.isa.ppinot.model.base.DataMeasure;

public class CamundaDataMeasureComputer implements CamundaMeasureComputer {

	private DataMeasure definition;
    private Integer	value;
  
    public CamundaDataMeasureComputer(MeasureDefinition definition) {
        if (!(definition instanceof DataMeasure)) {
            throw new IllegalArgumentException();
        }
        this.definition = (DataMeasure) definition;
        this.value = 0; 
        //this.definition.getDataContentSelection().
    }
	
	public List<? extends Measure> compute(HistoryService camundaHistory,
			String processName) {
		// TODO Auto-generated method stub
		List<MeasureInstance> result = new ArrayList<MeasureInstance>();
    	String variableName	= definition.getDataContentSelection().getSelection();
    	HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
		for (HistoricProcessInstance processInstance:processInstances){
			String processInstanceId = processInstance.getId();
			Object res = camundaHistory.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(variableName).singleResult().getValue();
			if (res instanceof Double){
				MeasureInstance m = new MeasureInstance(definition, (Double)res, processName, processInstanceId);
				result.add(m);
			}
			
		}
		
		return result;
	}

}
