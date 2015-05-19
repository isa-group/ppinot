package es.us.isa.ppinot.calculator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.evaluation.Evaluation;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.PPIEvaluator;
import es.us.isa.ppinot.evaluation.computers.camunda.CamundaMeasureComputer;
import es.us.isa.ppinot.evaluation.computers.camunda.CamundaMeasureComputerFactory;
import es.us.isa.ppinot.handler.PPINotModelHandler;
import es.us.isa.ppinot.handler.PPINotModelHandlerImpl;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.model.ProcessInstanceFilter;

public class CamundaPPIEvaluator implements PPIEvaluator {


    private InputStream 									stream;
    private PPINotModelHandler 								ppiNotModelHandler;
    private HistoryService 									camundaHistory;
    private Map<PPI, List<Evaluation>> 						allEvaluations; 
    private Map<MeasureDefinition, List<? extends Measure>> allMeasures; 

    public CamundaPPIEvaluator(InputStream inputStream, HistoryService camundaHistory) throws Exception {
        this.stream 			= inputStream;
        this.ppiNotModelHandler = new PPINotModelHandlerImpl();
        this.camundaHistory		= camundaHistory;
    	this.ppiNotModelHandler.load(stream);
    	this.allEvaluations		= new HashMap<PPI, List<Evaluation>>();
    	this.allMeasures		= new HashMap<MeasureDefinition, List<? extends Measure>>();
		
		Map<String, String> relProcessIdName	= new HashMap<String,String>();
    	
		//Little Preprocess to get ProcessName (used in Querys in Camunda History) related to ProcessId (what appears in PPIs)
		for(TProcess process:this.ppiNotModelHandler.getProcesses()){
    		relProcessIdName.put(process.getId(), process.getName());
    	}

		for(PPISet ppiSet:ppiNotModelHandler.getPPISets()){
			for (PPI currentPPI:ppiSet.getPpis()){
				List <? extends Measure>	measures = (List<? extends Measure>)evalMeasure(currentPPI.getMeasuredBy(),relProcessIdName.get(ppiSet.getProcessId()),currentPPI.getScope());
				allMeasures.put(currentPPI.getMeasuredBy(), measures);//result.add(currentPPIValue);				
			}
			
			for(MeasureDefinition currentMeasure:ppiSet.getMeasures()){
				//List<? extends Measure> measures = (List)evalMeasure(currentMeasure,relProcessIdName.get(ppiSet.getProcessId()));
				//allMeasures.put(currentMeasure, measures);//result.add(currentPPIValue);				
			}
		}
	}
    
    /**
     * 	Not used now
     */
    
    public Collection<Evaluation> eval(PPI ppi, String processName) {
    	
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        MeasureDefinition definition = ppi.getMeasuredBy();
  
        CamundaMeasureComputer execution = new CamundaMeasureComputerFactory().create(definition,ppi.getScope());
        List<? extends Measure> measures = execution.compute(camundaHistory, processName);
        
        for (Measure m : measures) {
            evaluations.add(new Evaluation(ppi, m.getValue(), m.getMeasureScope()));
        }
        return evaluations;
    }
    
    /**
     * Equivalent to Evaluate PPI but we use Compute Measure
     */
    public Collection<? extends Measure> evalMeasure(MeasureDefinition measureDefinition, String processId) {
    	return evalMeasure(measureDefinition, processId, null);
    }
    
    public Collection<? extends Measure> evalMeasure(MeasureDefinition measureDefinition, String processName, ProcessInstanceFilter filter) {
    	
        CamundaMeasureComputer execution = new CamundaMeasureComputerFactory().create(measureDefinition, filter);
        List<? extends Measure> measures = execution.compute(camundaHistory, processName);
        return measures;
    }

	public Map<MeasureDefinition, List<? extends Measure>> getMeasures() {
		return allMeasures;
	}


}
