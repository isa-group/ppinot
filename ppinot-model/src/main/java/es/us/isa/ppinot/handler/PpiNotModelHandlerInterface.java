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

	/**
	 * Devuelve el mapa de CountInstanceMeasure
	 */
	public Map<String, CountInstanceMeasure> getCountInstanceModelMap();
	/**
	 * Devuelve el mapa de TimeInstanceMeasure
	 */
	public Map<String, TimeInstanceMeasure> getTimeInstanceModelMap();
	/**
	 * Devuelve el mapa de StateConditionInstanceMeasure
	 */
	public Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap();
	/**
	 * Devuelve el mapa de DataInstanceMeasure
	 */
	public Map<String, DataInstanceMeasure> getDataInstanceModelMap();
	/**
	 * Devuelve el mapa de DataPropertyConditionInstanceMeasure
	 */
	public Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una TimeInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getTimeAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una CountInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getCountAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una StateConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataPropertyConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DerivedSingleInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap();
	/**
	 * Devuelve el mapa de las medidas DerivedSingleInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedSingleInstanceModelMap();
	/**
	 * Devuelve el mapa de las medidas DerivedMultiInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedMultiInstanceModelMap();
	/**
	 * Devuelve el mapa de los PPI
	 */
	public Map<String, PPI> getPpiModelMap();
	
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos CountInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setCountModelMap(Map<String, CountInstanceMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos StateConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DataInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataModelMap(Map<String, DataInstanceMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DataPropertyConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean CountInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean StateConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataPropertyConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DerivedSingleInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedSingleInstanceModelMap(Map<String, DerivedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DerivedMultiInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedMultiInstanceModelMap(Map<String, DerivedMeasure> modelMap);
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos PPI
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setPpiModelMap(Map<String, PPI> modelMap);
}
