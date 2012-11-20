package es.us.isa.isabpm.ppinot.model.state;

@SuppressWarnings("rawtypes")
public class RuntimeState {
	
	private Enum state;
	private String stateStr;
	
	public RuntimeState() {
		super();
		this.stateStr = "";
	}
	
	public RuntimeState(Enum state) {
		super();
		this.setState(state);
	}
	
	public RuntimeState(String stateStr) {
		super();
		Enum stateEnum = GenericState.START;
		
		if (stateStr.toLowerCase().contentEquals("start")) stateEnum = GenericState.START;
		if (stateStr.toLowerCase().contentEquals("end")) stateEnum = GenericState.END;
		
		if (stateStr.toLowerCase().contentEquals("ready")) stateEnum = BPMNState.READY; 
		if (stateStr.toLowerCase().contentEquals("active")) stateEnum = BPMNState.ACTIVE;
		if (stateStr.toLowerCase().contentEquals("withdrawn")) stateEnum = BPMNState.WITHDRAWN; 
		if (stateStr.toLowerCase().contentEquals("completing")) stateEnum = BPMNState.COMPLETING; 
		if (stateStr.toLowerCase().contentEquals("completed")) stateEnum = BPMNState.COMPLETED;
		if (stateStr.toLowerCase().contentEquals("failing")) stateEnum = BPMNState.FAILING;
		if (stateStr.toLowerCase().contentEquals("failed")) stateEnum = BPMNState.FAILED;
		if (stateStr.toLowerCase().contentEquals("terminating")) stateEnum = BPMNState.TERMINATING; 
		if (stateStr.toLowerCase().contentEquals("terminated")) stateEnum = BPMNState.TERMINATED;
		if (stateStr.toLowerCase().contentEquals("compensating")) stateEnum = BPMNState.COMPENSATING; 
		if (stateStr.toLowerCase().contentEquals("compensated")) stateEnum = BPMNState.COMPENSATED;
		
		this.stateStr = stateStr;
		this.setState(stateEnum);
	}
	
	public Enum getState() {
		return state;
	}
	
	public void setState(Enum state) {
		this.state = state;
	}
	
	public String getStateString() {
		return stateStr;
	}
}

/*
public abstract class RuntimeState {

	@SuppressWarnings("rawtypes")
	abstract public Enum getState();

	@SuppressWarnings("rawtypes")
	abstract public void setState(Enum state);
}
*/