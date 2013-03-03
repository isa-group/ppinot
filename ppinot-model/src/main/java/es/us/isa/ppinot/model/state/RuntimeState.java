package es.us.isa.ppinot.model.state;

/**
 * Clase con el estado de la ejecución de un elemento BPMN. Maneja la conversión de un estado del cadena a Enum y viceversa
 * 
 * @author Edelia
 *
 */
@SuppressWarnings("rawtypes")
public class RuntimeState {
	
	// estado del tipo Enum
	private Enum state;
	// la cadena correspondiente al estado
	private String stateStr;
	
	/**
	 * Constructor de la clase
	 */
	public RuntimeState() {
		super();
		this.stateStr = "";
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param state Estado del tipo Enum
	 */
	public RuntimeState(Enum state) {
		super();
		this.setState(state);
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param atStart Indica si esta en el estado START o no
	 */
	public RuntimeState(Boolean atStart) {
		super();
		this.setState((atStart)?GenericState.START:GenericState.END);
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param state Estado del tipo String
	 */
	public RuntimeState(String stateStr) {
		super();
//		Enum stateEnum = GenericState.START;
		Enum stateEnum = null;
		
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
		
		this.stateStr = stateStr;
		this.setState(stateEnum);
	}
	
	/**
     * Devuelve el atributo state:
     * Estado del tipo Enum
     * 
     * @return Valor del atributo
     */
	public Enum getState() {
		return state;
	}
	
    /**
     * Da valor al atributo state:
     * Estado del tipo Enum
     * 
     * @param value Valor del atributo
     */
	public void setState(Enum state) {
		this.state = state;
	}
	
	/**
     * Devuelve el atributo stateStr:
     * La cadena correspondiente al estado
     * 
     * @return Valor del atributo
     */
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