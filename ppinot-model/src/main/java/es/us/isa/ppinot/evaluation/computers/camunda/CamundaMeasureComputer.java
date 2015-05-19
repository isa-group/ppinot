package es.us.isa.ppinot.evaluation.computers.camunda;

import java.util.List;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.model.PPISet;

public interface CamundaMeasureComputer {

	
	public List<? extends Measure> compute(HistoryService camundaHistory, String processName);
	
}
