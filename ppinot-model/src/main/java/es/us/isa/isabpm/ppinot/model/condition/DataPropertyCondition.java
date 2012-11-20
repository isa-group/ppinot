package es.us.isa.isabpm.ppinot.model.condition;

import es.us.isa.isabpm.ppinot.model.state.RuntimeState;

public class DataPropertyCondition extends ProcessInstanceCondition {

	private String restriction;
	
	private RuntimeState state;
	
	public DataPropertyCondition () {
		super();
		this.setRestriction("");
		this.setStateConsidered(null);
	}
	
	public DataPropertyCondition (String appliesTo, String restriction, RuntimeState state) {
		super(appliesTo);
		this.setRestriction(restriction);
		this.setStateConsidered(state);
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public RuntimeState getStateConsidered() {
		return state;
	}

	public void setStateConsidered(RuntimeState state) {
		this.state = state;
	}

}
