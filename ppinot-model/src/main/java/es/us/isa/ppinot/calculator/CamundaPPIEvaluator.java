package es.us.isa.ppinot.calculator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.HistoryService;

import es.us.eii.gps.ppi.model.PPIValue;
import es.us.eii.gps.ppi.model.SimplePPI;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.calculator.camunda.CamundaMeasureExecution;
import es.us.isa.ppinot.calculator.camunda.CamundaMeasureExecutionFactory;
import es.us.isa.ppinot.evaluation.Evaluation;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.PPIEvaluator;
import es.us.isa.ppinot.handler.PPINotModelHandler;
import es.us.isa.ppinot.handler.PPINotModelHandlerImpl;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.PPISet;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.base.TimeMeasure;

public class CamundaPPIEvaluator implements PPIEvaluator {

		// TODO Auto-generated method stub
	 //private static final Logger log = Logger.getLogger(MXMLEvaluator.class.getName());

    private InputStream 									stream;
    private PPINotModelHandler 								ppiNotModelHandler;
    private HistoryService 									camundaHistory;
    private Map<PPI, List<Evaluation>> 						allEvaluations; 
    private Map<MeasureDefinition, List<? extends Measure>> allMeasures; 
    
    
    
    private static final Logger LOGGER = Logger.getLogger("PPIEval");

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
    
    public Collection<Evaluation> eval(PPI ppi) {
    	
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        MeasureDefinition definition = ppi.getMeasuredBy();
  
        CamundaMeasureExecution execution = new CamundaMeasureExecutionFactory().create(definition,ppi.getScope());
        LOGGER.info("Evaluating PPIs");
        LOGGER.info("Measure: "+execution.getClass());
        List<? extends Measure> measures = execution.calculate(camundaHistory);
        
        for (Measure m : measures) {
            evaluations.add(new Evaluation(ppi, m.getValue(), m.getMeasureScope()));
        }
        return evaluations;
    }
    
    /**
     * Equivalent to Evaluate PPI but we use Calculate Measure
     */
    public Collection<? extends Measure> evalMeasure(MeasureDefinition measureDefinition, String processId) {
    	return evalMeasure(measureDefinition, processId, null);
    }
    
    public Collection<? extends Measure> evalMeasure(MeasureDefinition measureDefinition, String processId, ProcessInstanceFilter filter) {
    	
        CamundaMeasureExecution execution = new CamundaMeasureExecutionFactory().create(measureDefinition, filter);
        List<? extends Measure> measures = execution.calculate(camundaHistory, processId);
        return measures;
    }

	public Map<MeasureDefinition, List<? extends Measure>> getMeasures() {
		return allMeasures;
	}


}
