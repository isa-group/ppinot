package es.us.isa.ppinot.model.composite.type;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.composite.CompositeType;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;

public class PercentageDataConditionType extends CompositeType {
	
	public DataMeasure dmValue;
	public CountMeasure iTotalInstances;
	public DerivedSingleInstanceMeasure dsiCalculate;
	public MeasureDefinition mdRoot; 
		
	public PercentageDataConditionType(){
		super();
		this.dmValue = new DataMeasure();
		this.iTotalInstances = new CountMeasure();
		this.dsiCalculate = new DerivedSingleInstanceMeasure();
		this.mdRoot = null;
	}
	
	
	public void buildPercentageDataCondition(){
		
		this.dmValue.setId("DataId");
						
		this.iTotalInstances.setId("CountId");
		this.iTotalInstances.setName("TotalInstances");
		this.iTotalInstances.setScale("INTEGER");
		this.iTotalInstances.setDescription("Counting the total of instances");
		
		this.dmValue.setName("");
		
		this.dsiCalculate.setId("CalculateId");
		this.dsiCalculate.setName("Calculator");
		this.dsiCalculate.setDescription("Measure calculates the percentage");
		this.dsiCalculate.addUsedMeasure("DataValue", dmValue);
		this.dsiCalculate.addUsedMeasure("CountValue", iTotalInstances);
		this.dsiCalculate.setFunction("(DataValue / CountValue) * 100");
	
		this.setDmRoot(dsiCalculate);
		
		Print("Percentage structure has been created.");
	}
	

	/**
	 * @return the dmValue
	 */
	public DataMeasure getDmValue() {
		return dmValue;
	}

	/**
	 * @param dmAValue the dmAValue to set
	 */
	public void setDmValue(DataMeasure dmValue) {
		this.dmValue = dmValue;
	}

	

	/**
	 * @return the iTotalInstances
	 */
	public CountMeasure getiTotalInstances() {
		return iTotalInstances;
	}

	/**
	 * @param iTotalInstances the iTotalInstances to set
	 */
	public void setiTotalInstances(CountMeasure iTotalInstances) {
		this.iTotalInstances = iTotalInstances;
	}

	/**
	 * @return the dsiCompare
	 */
	public DerivedSingleInstanceMeasure getDsiCompare() {
		return dsiCalculate;
	}

	/**
	 * @param dsiCompare the dsiCompare to set
	 */
	public void setDsiCompare(DerivedSingleInstanceMeasure dsiCompare) {
		this.dsiCalculate = dsiCompare;
	}

	public MeasureDefinition getDmRoot(){
		return mdRoot;
	}
	
	public void setDmRoot(MeasureDefinition mdRoot){
		this.mdRoot = mdRoot;
	}
	
	
	public void Print(String sCadenaAImprimir){
		System.out.println(sCadenaAImprimir);
	}
	
	

}
