package es.us.isa.ppinot.calculator.camunda;

import java.util.List;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.model.PPISet;

public interface CamundaMeasureExecution {


	public List<? extends Measure> calculate(HistoryService camundaHistory);
	public List<? extends Measure> calculate(HistoryService camundaHistory, String processID);
	
}
