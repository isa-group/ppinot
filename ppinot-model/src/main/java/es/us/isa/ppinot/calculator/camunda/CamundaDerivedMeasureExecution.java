package es.us.isa.ppinot.calculator.camunda;

import java.util.*;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

import org.mvel2.MVEL;

import java.io.Serializable;

public class CamundaDerivedMeasureExecution implements CamundaMeasureExecution {

	private DerivedMeasure definition;
	private Serializable expression;
	private Map<String, CamundaMeasureExecution> executions;

	
	public CamundaDerivedMeasureExecution(MeasureDefinition definition , ProcessInstanceFilter filter){
		if (!(definition instanceof DerivedMeasure)) {
	            throw new IllegalArgumentException();
        }
		this.definition = (DerivedMeasure) definition;

        CamundaMeasureExecutionFactory executionFactory = new CamundaMeasureExecutionFactory();

        this.definition = (DerivedMeasure) definition;
        this.expression = MVEL.compileExpression(this.definition.getFunction());
        this.executions = new HashMap<String, CamundaMeasureExecution>();
        for (Map.Entry<String, MeasureDefinition> entry : this.definition.getUsedMeasureMap().entrySet()) {
            this.executions.put(entry.getKey(), executionFactory.create(entry.getValue(),filter));
        }
	}
	
	public List<? extends Measure> calculate(HistoryService camundaHistory) {
		// TODO Auto-generated method stub
		return calculate(camundaHistory, "");
	}

	public List<? extends Measure> calculate(HistoryService camundaHistory,
			String processName) {
		List<Measure> results = new ArrayList<Measure>();
        Map<String, List<? extends Measure>> measures = new HashMap<String, List<? extends Measure>>();

        int size = 0;
        for (String varName : executions.keySet()) {
            List<? extends Measure> execution = executions.get(varName).calculate(camundaHistory, processName);
            measures.put(varName, execution);
            size = execution.size();
        }

        for (int i = 0; i < size; i++) {
            MeasureScope scope = null;
            Map<String, Double> variables = new HashMap<String, Double>();
            for (String varName : measures.keySet()) {
                Measure measure = measures.get(varName).get(i);
                variables.put(varName, measure.getValue());
                if (scope == null)
                    scope = new MeasureScope(measure.getProcessId(), measure.getInstances());
            }
            double value = (Double) MVEL.executeExpression(expression, variables);
            results.add(new Measure(definition, scope, value));
        }
        return results;		
	}
}
