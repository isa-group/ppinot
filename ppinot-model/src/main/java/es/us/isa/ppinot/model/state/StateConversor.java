package es.us.isa.ppinot.model.state;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Clase con el estado de la ejecucion de un elemento BPMN. Maneja la conversion de un estado del cadena a Enum y viceversa
 * 
 * @author Edelia
 *
 */
@SuppressWarnings("rawtypes")
@Deprecated
public class StateConversor {
	
	// estado del tipo Enum
    @JsonIgnore
	private RuntimeState state;
	// la cadena correspondiente al estado
	private String stateString;
	
	/**
	 * Constructor de la clase
	 */
	public StateConversor() {
		super();
		this.stateString = "";
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param state Estado del tipo Enum
	 */
	public StateConversor(RuntimeState state) {
		super();
		this.setState(state);
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param atEnd Indica si esta en el estado START o no
	 */
	public StateConversor(Boolean atEnd) {
		super();
		this.iniStateStr((atEnd)?"end":"start");
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param stateString Estado del tipo String
	 */
	public StateConversor(String stateString) {
		super();
		this.iniStateStr(stateString);
	}

    private void iniStateStr(String stateStr) {
		
//		Enum stateEnum = GenericState.START;
		RuntimeState stateEnum = null;
		
		if (stateStr!=null) {
			
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
	
			if (stateStr.toLowerCase().contentEquals("executing")) stateEnum = BPMNState.EXECUTING; 
			if (stateStr.toLowerCase().contentEquals("deleted")) stateEnum = BPMNState.DELETED;
		} else
			stateStr = "";
		
		this.stateString = stateStr;
		this.setState(stateEnum);
	}
	
	/**
     * Devuelve el atributo state:
     * Estado del tipo Enum
     * 
     * @return Valor del atributo
     */
    @JsonIgnore
	public RuntimeState getState() {
		return state;
	}
	
    /**
     * Da valor al atributo state:
     * Estado del tipo Enum
     * 
     * @param state State
     */
    @JsonIgnore
	public void setState(RuntimeState state) {
		this.state = state;
	}
	
	/**
     * Devuelve el atributo stateString:
     * La cadena correspondiente al estado
     * 
     * @return Valor del atributo
     */
	public String getStateString() {
		return stateString;
	}

    /**
     * Modifica stateString
     * @param stateString stateString
     */
    public void setStateString(String stateString) {
        this.stateString = stateString;
    }
}

/*
public abstract class StateConversor {

	@SuppressWarnings("rawtypes")
	abstract public Enum getState();

	@SuppressWarnings("rawtypes")
	abstract public void setState(Enum state);
}
*/