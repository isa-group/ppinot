package es.us.isa.ppinot.model.connector.base;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;

public class CountConnector extends BaseMeasureConnector implements Cloneable{

	private TimeInstantCondition ccwhen;
	
	public CountConnector(){
		super();
		this.setCcWhen(null);
	}
	
	public CountConnector(String id, String name, String description, TimeInstantCondition ccwhen){
		super(id, name, description);
		this.setCcWhen(ccwhen);
	}
	
	public TimeInstantCondition getCcWhen() {
		return ccwhen;
	}
	
	public void setCcWhen(TimeInstantCondition ccwhen) {
		this.ccwhen = ccwhen;
	}
	
	public boolean valid() {
		
		try{
			return super.valid() &&
					this.getCcWhen().getAppliesTo()!=null && !this.getCcWhen().getAppliesTo().isEmpty();
		}catch(Exception e){
			return false;
		}
		
	}
	
	public CountConnector clone(){
		
		final CountConnector clone;
		
		try{
			clone = (CountConnector) super.clone();
		}catch(Exception ex){
			throw new RuntimeException( "\t!>>>> Excepción en CountConnector - clone()" );
		}
		
		return clone;
	}
	
}
