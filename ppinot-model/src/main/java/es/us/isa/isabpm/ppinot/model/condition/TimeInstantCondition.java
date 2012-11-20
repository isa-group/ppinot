package es.us.isa.isabpm.ppinot.model.condition;

import es.us.isa.isabpm.ppinot.model.state.RuntimeState;

public class TimeInstantCondition extends Condition {

	private RuntimeState changesToState;

	public TimeInstantCondition () {
		super();
		this.setChangesToState(null);
	}
	
	public TimeInstantCondition (String appliesTo, RuntimeState changesToState) {
		super(appliesTo);
		this.setChangesToState(changesToState);
	}
	
	public RuntimeState getChangesToState() {
		return changesToState;
	}

	public void setChangesToState(RuntimeState changesToState) {
		this.changesToState = changesToState;
	}
}
