package es.us.isa.ppinot.model.connector.base;

/*import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.condition.Condition;
import es.us.isa.ppinot.model.condition.ProcessInstanceCondition;*/
import es.us.isa.ppinot.model.condition.StateCondition;


public class StateConditionConnector extends BaseMeasureConnector implements Cloneable{

	private StateCondition ccCondition;
	
	public StateConditionConnector(){
		super();
		this.setCcStateCondition(null);
	}
	
	public StateConditionConnector(String id, String name, String description, StateCondition ccCondition){
		super(id, name, description);
		this.setCcStateCondition(ccCondition);
	}
	
	public void setCcStateCondition(StateCondition ccCondition) {
		this.ccCondition = ccCondition;
	}
	
	public StateCondition getCcStateCondition() {
		return this.ccCondition;
	}
	
	public boolean valid() {
		
		try{
			return super.valid() &&
					this.getCcStateCondition().getAppliesTo()!=null && !this.getCcStateCondition().getAppliesTo().isEmpty() &&
					this.getCcStateCondition().getState()!=null;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public StateConditionConnector clone(){
		
		final StateConditionConnector clone;
		
		try{
			clone = (StateConditionConnector) super.clone();
		}catch(Exception e){
			throw new RuntimeException( "\t!>>>> Excepción en StateConditionConnector - clone()" );
		}
		return clone;
	}
	
}
