package es.us.isa.ppinot.owl.converter;

import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.MeasureDefinition;

public class UtilsObjectOWLPpinot {
	
	
	/** Devuelve el nombre de la medida CountAggregatedMeasure**/
	public static String getNameCountAggregatedMeasure(MeasureDefinition medida){
		AggregatedMeasure countAgg = (AggregatedMeasure) medida;
		String nameCountAggregatedMeasure= countAgg.getBaseMeasure().getName();
		nameCountAggregatedMeasure = nameCountAggregatedMeasure.replaceAll(" ", "");
		return nameCountAggregatedMeasure;
	}
	
	/** Devuelve el nombre de la medida TimeAggregatedMeasure**/
	public static String getNameTimeAggregatedMeasure(MeasureDefinition medida){
		AggregatedMeasure TimeAgg = (AggregatedMeasure) medida;
		String nameAggregatedMeasure= TimeAgg.getBaseMeasure().getName();
		nameAggregatedMeasure = nameAggregatedMeasure.replaceAll(" ", "");
		return nameAggregatedMeasure;	
	}
	
	/** Devuelve el nombre de la medida StateConditionAggregatedMeasure**/
	public static String getNameStateConditionAggregatedMeasure(MeasureDefinition medida){
		AggregatedMeasure agg = (AggregatedMeasure) medida;
		String nameAggregatedMeasure= agg.getBaseMeasure().getName();
		nameAggregatedMeasure = nameAggregatedMeasure.replaceAll(" ", "");
		return nameAggregatedMeasure;
	}
	
	
	/** Devuelve el nombre de la medida DataAggregatedMeasure**/
	public static String getNameDataAggregatedMeasure(MeasureDefinition medida){
		AggregatedMeasure agg = (AggregatedMeasure) medida;
		String nameAggregatedMeasure= agg.getBaseMeasure().getName();
		nameAggregatedMeasure = nameAggregatedMeasure.replaceAll(" ", "");
		return nameAggregatedMeasure;
	}
	
	/** Devuelve el nombre de la medida DataPropertyConditionAggregatedMeasure**/
	public static String getNameDataPropertyConditionAggregatedMeasure(MeasureDefinition medida){
		AggregatedMeasure agg = (AggregatedMeasure) medida;
		String nameAggregatedMeasure= agg.getBaseMeasure().getName();
		nameAggregatedMeasure = nameAggregatedMeasure.replaceAll(" ", "");
		return nameAggregatedMeasure;
	}
	
	/** Devuelve el nombre de la medida TimeInstanceMeasure**/
	public static String getNameTimeInstanceMeasure(MeasureDefinition medida){
		
		TimeInstanceMeasure element = (TimeInstanceMeasure) medida;
		String nameTimeMeasure= element.getName();
		nameTimeMeasure = nameTimeMeasure.replaceAll(" ", "");
		return nameTimeMeasure;
	}
	
	/** Devuelve el nombre de la medida CountInstanceMeasure**/
	public static String getNameCountInstanceMeasure(MeasureDefinition medida){
		
		CountInstanceMeasure element = (CountInstanceMeasure) medida;
		String nameMeasure= element.getName();
		nameMeasure = nameMeasure.replaceAll(" ", "");
		return nameMeasure;
	}
	
	/** Devuelve el nombre de la medida StateConditionInstanceMeasure**/
	public static String getNameStateConditionInstanceMeasure(MeasureDefinition medida){
		
		StateConditionInstanceMeasure element = (StateConditionInstanceMeasure) medida;
		String nameMeasure= element.getName();
		nameMeasure = nameMeasure.replaceAll(" ", "");
		return nameMeasure;
	}
	
	/** Devuelve el nombre de la medida DataInstanceMeasure**/
	public static String getNameDataInstanceMeasure(MeasureDefinition medida){
		
		DataInstanceMeasure element = (DataInstanceMeasure) medida;
		String nameMeasure= element.getName();
		nameMeasure = nameMeasure.replaceAll(" ", "");
		return nameMeasure;
	}
	
	/** Devuelve el nombre de la medida DataInstanceMeasure**/
	public static String getDataPropertyConditionInstanceMeasure(MeasureDefinition medida){
		
		DataPropertyConditionInstanceMeasure element = (DataPropertyConditionInstanceMeasure) medida;
		String nameMeasure= element.getName();
		nameMeasure = nameMeasure.replaceAll(" ", "");
		return nameMeasure;
	}
}

