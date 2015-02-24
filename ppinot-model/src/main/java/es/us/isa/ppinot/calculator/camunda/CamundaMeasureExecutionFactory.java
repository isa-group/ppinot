package es.us.isa.ppinot.calculator.camunda;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeInstantMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

public class CamundaMeasureExecutionFactory {

	public CamundaMeasureExecution create(MeasureDefinition definition, ProcessInstanceFilter filter) {
	    CamundaMeasureExecution execution = null;
	    if (definition instanceof TimeMeasure) {
            execution = new CamundaTimeMeasureExecution(definition);
        } else if (definition instanceof TimeInstantMeasure) {
            execution = new CamundaTimeInstantMeasureExecution(definition);
        } else if (definition instanceof CountMeasure) {
            execution = new CamundaCountMeasureExecution(definition);
        } else if (definition instanceof StateConditionMeasure) {
            execution = new CamundaStateConditionMeasureExecution(definition);
        } else if (definition instanceof AggregatedMeasure) {
            execution = new CamundaAggregatedMeasureExecution(definition, filter);
        }else if (definition instanceof DerivedMeasure) { 
            execution = new CamundaDerivedMeasureExecution(definition, filter);
        }else if (definition instanceof DataMeasure){
        	execution = new CamundaDataMeasureExecution(definition);
        }

        return execution;
	}
	
	
}
