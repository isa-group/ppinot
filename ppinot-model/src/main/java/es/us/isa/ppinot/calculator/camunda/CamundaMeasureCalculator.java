package es.us.isa.ppinot.calculator.camunda;

import java.util.List;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.ppinot.calculator.MeasureCalculator;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

public class CamundaMeasureCalculator implements MeasureCalculator {
  
	private HistoryService historyService;
	 
	public CamundaMeasureCalculator(HistoryService historyService){
		this.historyService = historyService;
	}
 
	public List<? extends Measure> calculate(MeasureDefinition measure,
			ProcessInstanceFilter filter) {
		CamundaMeasureExecution execution = new CamundaMeasureExecutionFactory().create(measure, filter);
		List<? extends Measure> measures = execution.calculate(historyService);
	     return measures;
	}
	
	//public List<? extends Measure> calculate(MeasureDefinition measure) {
	//	 CamundaMeasureExecution execution = new CamundaMeasureExecutionFactory().create(measure);
	//	 List<? extends Measure> measures = execution.calculate(historyService);
	//     return measures;
//	}
	

}
