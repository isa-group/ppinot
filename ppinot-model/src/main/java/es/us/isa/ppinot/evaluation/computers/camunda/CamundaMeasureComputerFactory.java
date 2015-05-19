package es.us.isa.ppinot.evaluation.computers.camunda;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.ProcessInstanceFilter;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeInstantMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

public class CamundaMeasureComputerFactory {

	public CamundaMeasureComputer create(MeasureDefinition definition, ProcessInstanceFilter filter) {
	    CamundaMeasureComputer execution = null;
	    if (definition instanceof TimeMeasure) {
            execution = new CamundaTimeMeasureComputer(definition);
        } else if (definition instanceof TimeInstantMeasure) {
            execution = new CamundaTimeInstantMeasureComputer(definition);
        } else if (definition instanceof CountMeasure) {
            execution = new CamundaCountMeasureComputer(definition);
        } else if (definition instanceof StateConditionMeasure) {
            execution = new CamundaStateConditionMeasureComputer(definition);
        } else if (definition instanceof AggregatedMeasure) {
            execution = new CamundaAggregatedMeasureComputer(definition, filter);
        }else if (definition instanceof DerivedMeasure) { 
            execution = new CamundaDerivedMeasureComputer(definition, filter);
        }else if (definition instanceof DataMeasure){
        	execution = new CamundaDataMeasureComputer(definition);
        }

        return execution;
	}
	
	
}
