package es.us.isa.isabpm.ppinot.model.condition;

import es.us.isa.isabpm.ppinot.model.state.RuntimeState;

public class StateCondition extends ProcessInstanceCondition {

	private RuntimeState state;

	public StateCondition() {
		super();
		this.setState(null);
	}

	public StateCondition(String appliesTo, RuntimeState state) {
		super(appliesTo);
		this.setState(state);
	}
	
	public RuntimeState getState() {
		return state;
	}

	public void setState(RuntimeState state) {
		this.state = state;
	}

}
