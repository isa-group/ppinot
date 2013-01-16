package es.us.isa.ppinot.handler;

import java.util.Map;

import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;

public interface PpiNotModelHandlerInterface {

	public abstract Map<String, CountInstanceMeasure> getCountInstanceModelMap();
	public abstract Map<String, TimeInstanceMeasure> getTimeInstanceModelMap();
	public abstract Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap();
	public abstract Map<String, DataInstanceMeasure> getDataInstanceModelMap();
	public abstract Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap();
	public abstract Map<String, AggregatedMeasure> getTimeAggregatedModelMap();
	public abstract Map<String, AggregatedMeasure> getCountAggregatedModelMap();
	public abstract Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap();
	public abstract Map<String, AggregatedMeasure> getDataAggregatedModelMap();
	public abstract Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap();
	public abstract Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap();
	public abstract Map<String, DerivedSingleInstanceMeasure> getDerivedSingleInstanceModelMap();
	public abstract Map<String, DerivedMultiInstanceMeasure> getDerivedMultiInstanceModelMap();
	public abstract Map<String, PPI> getPpiModelMap();
	
	public abstract void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap);
	public abstract void setCountModelMap(Map<String, CountInstanceMeasure> modelMap);
	public abstract void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap);
	public abstract void setDataModelMap(Map<String, DataInstanceMeasure> modelMap);
	public abstract void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap);
	public abstract void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	public abstract void setDerivedSingleInstanceModelMap(Map<String, DerivedSingleInstanceMeasure> modelMap);
	public abstract void setDerivedMultiInstanceModelMap(Map<String, DerivedMultiInstanceMeasure> modelMap);
	public abstract void setPpiModelMap(Map<String, PPI> modelMap);
}
