package es.us.isa.ppinot.evaluation.computers.camunda;

import java.util.*;

import org.camunda.bpm.engine.HistoryService;

import es.us.isa.ppinot.evaluation.Measure;
import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

import org.mvel2.MVEL;

import java.io.Serializable;

public class CamundaDerivedMeasureComputer implements CamundaMeasureComputer {

	private DerivedMeasure definition;
	private Serializable expression;
	private Map<String, CamundaMeasureComputer> executions;

	
	public CamundaDerivedMeasureComputer(MeasureDefinition definition , ProcessInstanceFilter filter){
		if (!(definition instanceof DerivedMeasure)) {
	            throw new IllegalArgumentException();
        }
		this.definition = (DerivedMeasure) definition;

        CamundaMeasureComputerFactory executionFactory = new CamundaMeasureComputerFactory();

        this.definition = (DerivedMeasure) definition;
        this.expression = MVEL.compileExpression(this.definition.getFunction());
        this.executions = new HashMap<String, CamundaMeasureComputer>();
        for (Map.Entry<String, MeasureDefinition> entry : this.definition.getUsedMeasureMap().entrySet()) {
            this.executions.put(entry.getKey(), executionFactory.create(entry.getValue(),filter));
        }
	}
	

	public List<? extends Measure> compute(HistoryService camundaHistory,
			String processName) {
		List<Measure> results = new ArrayList<Measure>();
        Map<String, List<? extends Measure>> measures = new HashMap<String, List<? extends Measure>>();

        int size = 0;
        for (String varName : executions.keySet()) {
            List<? extends Measure> execution = executions.get(varName).compute(camundaHistory, processName);
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
