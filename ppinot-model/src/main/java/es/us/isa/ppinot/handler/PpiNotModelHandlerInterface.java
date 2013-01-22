package es.us.isa.ppinot.handler;

import java.util.Map;

import es.us.isa.bpmn.handler.ModelHandleInterface;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

public interface PpiNotModelHandlerInterface extends ModelHandleInterface {

	public Map<String, CountInstanceMeasure> getCountInstanceModelMap();
	public Map<String, TimeInstanceMeasure> getTimeInstanceModelMap();
	public Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap();
	public Map<String, DataInstanceMeasure> getDataInstanceModelMap();
	public Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap();
	public Map<String, AggregatedMeasure> getTimeAggregatedModelMap();
	public Map<String, AggregatedMeasure> getCountAggregatedModelMap();
	public Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap();
	public Map<String, AggregatedMeasure> getDataAggregatedModelMap();
	public Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap();
	public Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap();
	public Map<String, DerivedMeasure> getDerivedSingleInstanceModelMap();
	public Map<String, DerivedMeasure> getDerivedMultiInstanceModelMap();
	public Map<String, PPI> getPpiModelMap();
	
	public void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap);
	public void setCountModelMap(Map<String, CountInstanceMeasure> modelMap);
	public void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap);
	public void setDataModelMap(Map<String, DataInstanceMeasure> modelMap);
	public void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap);
	public void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public void setDerivedSingleInstanceModelMap(Map<String, DerivedMeasure> modelMap);
	public void setDerivedMultiInstanceModelMap(Map<String, DerivedMeasure> modelMap);
	public void setPpiModelMap(Map<String, PPI> modelMap);
}
