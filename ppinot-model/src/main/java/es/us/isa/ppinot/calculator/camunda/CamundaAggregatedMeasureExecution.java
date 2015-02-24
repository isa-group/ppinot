package es.us.isa.ppinot.calculator.camunda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
import org.joda.time.DateTime;

import es.us.isa.ppinot.evaluation.Aggregator;
import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureInstance;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifier;
import es.us.isa.ppinot.evaluation.scopes.ScopeClassifierFactory;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;

public class CamundaAggregatedMeasureExecution implements CamundaMeasureExecution{

	private AggregatedMeasure definition;
	
	private CamundaMeasureExecution baseExecution;
    private Aggregator agg;
    private ScopeClassifier classifier;

	
	public CamundaAggregatedMeasureExecution(MeasureDefinition definition, ProcessInstanceFilter filter){
		if (!(definition instanceof AggregatedMeasure)) {
            throw new IllegalArgumentException();
		}
		this.definition = (AggregatedMeasure) definition;
		this.baseExecution = new CamundaMeasureExecutionFactory().create(this.definition.getBaseMeasure(), filter);
		
		this.classifier = new ScopeClassifierFactory().create(filter);
        //this.classifier = new ScopeClassifierFactory().create(filter);
        this.agg = new Aggregator(this.definition.getAggregationFunction());
	}
	
	public List<? extends Measure> calculate(HistoryService camundaHistory) {
		// TODO Auto-generated method stub
		return calculate(camundaHistory, "");
	}

	public List<? extends Measure> calculate(HistoryService camundaHistory,
			String processName) {
		
		//Lo que hay que hacer
				//1) Para todas las instancias de proceso, calcular la medida NO agregada
				//2) Crear los Scopes, es decir, Sets de Instancias de procesos. Cada Set tiene en comun las fechas y/o GroupedBy
				//3) Agregar las medidas de cada Scope
				
		List<Measure> result = new ArrayList<Measure>();
        //Esto es 1)
		Collection<? extends Measure> measures = baseExecution.calculate(camundaHistory, processName);
        Map<String, MeasureInstance> measureMap = buildMeasureMap(measures);
        //List<String> processIds = new ArrayList<String>();
        HistoricProcessInstanceQuery	processQuery		= camundaHistory.createHistoricProcessInstanceQuery();	
        HistoricVariableInstanceQuery	variableQuery		= null;
        List<HistoricProcessInstance>	processInstances	= processQuery.processDefinitionName(processName).list();
        //List<HistoricVariableInstance>	variableInstances	= null;
        String groupedBy = "";
        if(definition.getGroupedBy()!=null)
        	groupedBy = definition.getGroupedBy().getSelection();
        
        if (groupedBy!=null && !groupedBy.equals("")){
        	
        	variableQuery = camundaHistory.createHistoricVariableInstanceQuery().variableName(groupedBy);	
    		
 			
		}
        Map<String,String> grouped	= new HashMap<String,String>();
        Map<String,Collection<String>> groupedResult	= new HashMap<String,Collection<String>>();
		
        //////////////////////////////////////
		//FALTA 2)
		//
		for(HistoricProcessInstance process:processInstances){
			LogEntry processStart 	= LogEntry.instance(processName, process.getId(), LogEntry.EventType.ready, new DateTime(process.getStartTime()));
			LogEntry processEnd 	= LogEntry.instance(processName, process.getId(), LogEntry.EventType.complete, new DateTime(process.getEndTime()));
			classifier.update(processStart);
			classifier.update(processEnd);
	        if (groupedBy!=null && !groupedBy.equals("")){
	        	HistoricVariableInstance	variableInstance	= variableQuery.processInstanceId(process.getId()).singleResult();
	        	grouped.put(process.getId(), (String)variableInstance.getValue());
	        }
			//processIds.add(process.getId());
			//Calculate Measure
		}
		
			//Build MeasureScope (Set<ProcessInstance> ver implementacion)
		
		
		Collection<MeasureScope> scopes = classifier.listScopes();
		if (groupedBy!=null && !groupedBy.equals("")){
			System.out.println("Grouped");
			for(MeasureScope scope:scopes){
				for(String idProcess:scope.getInstances()){
					String keyDateSelection = scope.getProcessId()+"@"+grouped.get(idProcess);
					if(!groupedResult.containsKey(keyDateSelection)){
						groupedResult.put(keyDateSelection, new ArrayList<String>());
					}
					groupedResult.get(keyDateSelection).add(idProcess);
				}
			}
			for (String key:groupedResult.keySet()){
				System.out.println("Cubeta "+key);
				Collection<Double> toAggregate = chooseMeasuresToAggregateGrouped(groupedResult.get(key),measureMap);
				double val = agg.aggregate(toAggregate);
	            result.add(new Measure(definition, processName,groupedResult.get(key), val));
			}
		}else{    
			System.out.println("NOT Grouped");
			
			//Esto es 3)
			for (MeasureScope scope : scopes) {
	            Collection<Double> toAggregate = chooseMeasuresToAggregate(scope, measureMap);
	            double val = agg.aggregate(toAggregate);
	
	            result.add(new Measure(definition, scope, val));
	        }
		}
		
        for (Measure m:result){
        	System.out.println(m.getInstances()+" "+m.getValue());
        }
        return result;
    }
	
    
    private Map<String, MeasureInstance> buildMeasureMap(Collection<? extends Measure> measures) {
        Map<String, MeasureInstance> measureMap = new HashMap<String, MeasureInstance>();
        for (Measure m : measures) {
            if (m instanceof MeasureInstance) {
                MeasureInstance mi = (MeasureInstance) m;
                System.out.println("Measure "+m.getValue());
                measureMap.put(mi.getInstanceId(), mi);
            }
        }
        return measureMap;
    }
    
    
    

    private Collection<Double> chooseMeasuresToAggregate(MeasureScope scope, Map<String, MeasureInstance> measureMap) {
        Collection<Double> toAggregate = new ArrayList<Double>();
        for (String instance : scope.getInstances()) {
            toAggregate.add(measureMap.get(instance).getValue());
        }
        return toAggregate;
    }
    
    private Collection<Double> chooseMeasuresToAggregateGrouped(Collection<String> processIds, Map<String, MeasureInstance> measureMap) {
        Collection<Double> toAggregate = new ArrayList<Double>();
        for (String instance : processIds) {
            toAggregate.add(measureMap.get(instance).getValue());
        }
        return toAggregate;
    }

}
