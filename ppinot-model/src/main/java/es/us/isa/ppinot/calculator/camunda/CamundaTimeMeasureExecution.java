package es.us.isa.ppinot.calculator.camunda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.state.GenericState;



/**
 * @author cefiro
 *
 */
public class CamundaTimeMeasureExecution implements CamundaMeasureExecution {

	private TimeMeasure definition;
    private Map<String, MeasureInstanceTimer> measures;
    private static final Logger LOGGER = Logger.getLogger("PPIEval");
  
    public CamundaTimeMeasureExecution(MeasureDefinition definition) {
        if (!(definition instanceof TimeMeasure)) {
            throw new IllegalArgumentException();
        }

        this.definition = (TimeMeasure) definition;
        this.measures = new HashMap<String, MeasureInstanceTimer>();
    }

    public List<? extends Measure> calculate(HistoryService camundaHistory) {
        List<MeasureInstance> computation = new ArrayList<MeasureInstance>(calculate(definition, camundaHistory, ""));
        return computation;
    }
    
    public List<? extends Measure> calculate(HistoryService camundaHistory, String processID) {
        List<MeasureInstance> computation = new ArrayList<MeasureInstance>(calculate(definition, camundaHistory, processID));
        return computation;
    }
    
    
    
    public List<? extends MeasureInstance> calculate(TimeMeasure definition, HistoryService camundaHistory, String processName){
    
    	List<MeasureInstance> result = new ArrayList<MeasureInstance>();
    	if (definition.getTimeMeasureType() == TimeMeasureType.LINEAR){
    	   	TimeInstantCondition startTime 	= definition.getFrom();
        	TimeInstantCondition endTime 	= definition.getTo();
     
    		HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
    		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
    		for (HistoricProcessInstance processInstance:processInstances){
    			HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery();
    			String processInstanceId = processInstance.getId(); 
    			List<HistoricActivityInstance> startActivities = activityQuery.processInstanceId(processInstanceId).activityId(startTime.getAppliesTo()).orderByHistoricActivityInstanceStartTime().asc().list();
    			if (startActivities.size()>0){			
    				HistoricActivityInstance startActivity = startActivities.get(0);
    				List<HistoricActivityInstance> endActivities = activityQuery.processInstanceId(processInstanceId).activityId(endTime.getAppliesTo()).orderByHistoricActivityInstanceEndTime().asc().list();
    				if (endActivities.size()>0){
    					HistoricActivityInstance endActivity = endActivities.get(endActivities.size()-1);
    					MeasureInstanceTimer m = new LinearMeasureInstanceTimer(definition, processName, processInstanceId);
    					if (startTime.getChangesToState().equals(GenericState.START)){
    						m.starts(new DateTime(startActivity.getStartTime()));
    					}else{
    						m.starts(new DateTime(startActivity.getEndTime()));
    					}
    					if (endTime.getChangesToState().equals(GenericState.START)){
    						m.ends(new DateTime(endActivity.getStartTime()));
    					}else{
    						m.ends(new DateTime(endActivity.getEndTime()));
    					}
						result.add(m);
    				}
    			}else{
    				//System.out.println("No Starting Activities found");
    			}
    		}
    		    		
    	}else if (definition.getTimeMeasureType() == TimeMeasureType.CYCLIC){
    	   	TimeInstantCondition startTime 	= definition.getFrom();
        	TimeInstantCondition endTime 	= definition.getTo();
     
    		List<DateTime> startTimings = new ArrayList<DateTime>();
    		List<DateTime> endTimings = new ArrayList<DateTime>();
    		
    		HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
    		List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
    		for (HistoricProcessInstance processInstance:processInstances){
    			
    			HistoricActivityInstanceQuery 	activityQuery 		= camundaHistory.createHistoricActivityInstanceQuery();
    			String processInstanceId = processInstance.getId();
    			MeasureInstanceTimer m = new CyclicMeasureInstanceTimer(definition, processName, processInstanceId);

    			List<HistoricActivityInstance> startActivities = activityQuery.processInstanceId(processInstanceId).activityId(startTime.getAppliesTo()).orderByHistoricActivityInstanceStartTime().asc().list();
    			if (startActivities.size()>0){
    				for(HistoricActivityInstance startActivity:startActivities){
    					if (startTime.getChangesToState().equals(GenericState.START)){
    						startTimings.add(new DateTime(startActivity.getStartTime()));
    					}else{
    						startTimings.add(new DateTime(startActivity.getEndTime()));
    					}
    				}
    				List<HistoricActivityInstance> endActivities = activityQuery.processInstanceId(processInstanceId).activityId(endTime.getAppliesTo()).orderByHistoricActivityInstanceEndTime().asc().list();
    				if (endActivities.size()>0){
    					for(HistoricActivityInstance endActivity:endActivities){
        					if (endTime.getChangesToState().equals(GenericState.START)){
        						endTimings.add(new DateTime(endActivity.getStartTime()));
        					}else{
        						endTimings.add(new DateTime(endActivity.getEndTime()));
        					}
        				}
    					for(int i=0;i<startActivities.size();i++){
    						m.starts(startTimings.get(i));
    						m.ends(endTimings.get(i));
    					}
    					result.add(m);
    				}
    			}else{
    				//System.out.println("No Starting Activities found");
    			}
    		}
	
    		
    		
    	}
    	//System.out.println("Medidas: "+result.size());
    	return result;
    }
    
        
    

    private abstract class MeasureInstanceTimer extends MeasureInstance {
        public abstract void starts(DateTime start);
        public abstract void ends(DateTime ends);

        public MeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, Double.NaN, processId, instanceId);
        }
    }

    private class LinearMeasureInstanceTimer extends MeasureInstanceTimer {

        private DateTime start;
        private Duration duration;

        public LinearMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
        }

        public void starts(DateTime start) {
            this.start = start;
            this.duration = null;
        }

        @Override
        public double getValue() {
            double value;

            if (duration != null)
                value = duration.getMillis();
            else {
                value = Double.NaN;
            }

            return value;
        }

        public void ends(DateTime end) {
            duration = new Duration(start, end);
        }

    }

    private class CyclicMeasureInstanceTimer extends MeasureInstanceTimer {
        private DateTime start;
        private Collection<Duration> durations;
        private Aggregator aggregator;

        public CyclicMeasureInstanceTimer(TimeMeasure definition, String processId, String instanceId) {
            super(definition, processId, instanceId);
            durations = new ArrayList<Duration>();
            aggregator = new Aggregator(definition.getSingleInstanceAggFunction());
        }

        public void starts(DateTime start) {
            if (! isRunning())
                this.start = start;
        }

        @Override
        public double getValue() {
            double value;
            Collection<Double> measures = new ArrayList<Double>();
            for (Duration d : durations) {
                measures.add((double) d.getMillis());
            }

            if (isRunning() || measures.isEmpty()) {
                value = Double.NaN;
            } else {
                value = aggregator.aggregate(measures);
            }

            return value;
        }

        public void ends(DateTime end) {
            if (isRunning()) {
                durations.add(new Duration(start, end));
                reset();
            }
        }

        private void reset() {
            start = null;
        }

        private boolean isRunning() {
            return start != null;
        }

    }
	
}
