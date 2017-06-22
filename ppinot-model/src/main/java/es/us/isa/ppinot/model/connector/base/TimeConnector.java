package es.us.isa.ppinot.model.connector.base;

import es.us.isa.ppinot.model.Schedule;
/*import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.base.TimeMeasure;*/
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;


public class TimeConnector extends BaseMeasureConnector implements Cloneable{

	private TimeInstantCondition ccFrom;
	private TimeInstantCondition ccTo;
	//private TimeMeasureType ccTimeMeasureType; //Measure type (cycle o linear)
	//private String ccSingleInstanceAggFunction; //Aggregated function

	private Schedule ccConsiderOnly;
	private DataPropertyCondition ccPrecondition;

	private boolean ccComputeUnfinished; //(??)
		
	public TimeConnector(){
		super();
		this.setCcFrom(null);
		this.setCcTo(null);
		//this.setCcTimeMeasureType(TimeMeasureType.LINEAR);
		//this.setCcSingleInstanceAggFunction("");
	}
	
	//public TimeConnector(String id, String name, String description, TimeInstantCondition ccFrom, TimeInstantCondition ccTo, TimeMeasureType ccTimeMeasureType, String ccSingleInstanceAggFunction){
	public TimeConnector(String id, String name, String description, TimeInstantCondition ccFrom, TimeInstantCondition ccTo){
		super(id, name, description);
		this.setCcFrom(ccFrom);
		this.setCcTo(ccTo);
		//this.setCcTimeMeasureType(ccTimeMeasureType);
		//this.setCcSingleInstanceAggFunction(ccSingleInstanceAggFunction);
	}
	
	/*public TimeMeasureType getCcTimeMeasureType() {
		return this.ccTimeMeasureType;
	}
	
	public void setCcTimeMeasureType(TimeMeasureType ccTimeMeasureType) {
		this.ccTimeMeasureType = ccTimeMeasureType;
	}
	
	public String getCcSingleInstanceAggFunction() {
		return this.ccSingleInstanceAggFunction;
	}
	
	public void setCcSingleInstanceAggFunction(String ccSingleInstanceAggFunction) {
		this.ccSingleInstanceAggFunction = ccSingleInstanceAggFunction;
	}*/
	
	public TimeInstantCondition getCcFrom() {
		return ccFrom;
	}
	
	public void setCcFrom(TimeInstantCondition ccFrom) {
		this.ccFrom = ccFrom;
	}
	
	public TimeInstantCondition getCcTo() {
		return ccTo;
	}
	
	public void setCcTo(TimeInstantCondition ccTo) {
		this.ccTo = ccTo;
	}
	
	public Schedule getCcConsiderOnly() {
        return ccConsiderOnly;
    }
	
	public void setCcConsiderOnly(Schedule ccConsiderOnly) {
        this.ccConsiderOnly = ccConsiderOnly;
    }
	
	public boolean isCcComputeUnfinished() {
		return ccComputeUnfinished;
	}
	
	public TimeConnector setCcComputeUnfinished(boolean ccComputeUnfinished) {
		this.ccComputeUnfinished = ccComputeUnfinished;
		return this;
	}
	
	public DataPropertyCondition getCcPrecondition() {
		return ccPrecondition;
	}
	
	public TimeConnector setCcPrecondition(DataPropertyCondition ccPrecondition) {
		this.ccPrecondition = ccPrecondition;
		return this;
	}
	
	public boolean valid() {
		
		try{
			return super.valid() &&
					this.getCcFrom().getAppliesTo()!=null && !this.getCcFrom().getAppliesTo().isEmpty() &&
					this.getCcTo().getAppliesTo()!=null && !this.getCcTo().getAppliesTo().isEmpty();
		}catch(Exception e){
			return false;
		}
		
		/*return super.valid() &&
				this.getCcFrom().getAppliesTo()!=null && !this.getCcFrom().getAppliesTo().isEmpty() &&
				this.getCcTo().getAppliesTo()!=null && !this.getCcTo().getAppliesTo().isEmpty();*/
	}

public TimeConnector clone(){
		
		final TimeConnector clone;
		
		try{
			clone = (TimeConnector) super.clone();
		}catch(Exception ex){ 
			throw new RuntimeException( "\t!>>>> Excepción en TimeConnector - clone()" );
		}
		return clone;
	}
	
}
