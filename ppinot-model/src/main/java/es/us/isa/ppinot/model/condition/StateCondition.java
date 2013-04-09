package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Clase de las condiciones que debe cumplir un elemento BPMN
 * 
 * @author Edelia
 *
 */
public class StateCondition extends ProcessInstanceCondition {

	// Estado de la ejecucion
	private RuntimeState state;

	/**
	 * Constructor de la clase
	 * 
	 */
	public StateCondition() {
		super();
		this.setState(null);
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 * @param state Estado de la ejecucion
	 */
	public StateCondition(String appliesTo, RuntimeState state) {
		super(appliesTo);
		this.setState(state);
	}
	
	/**
     * Devuelve el atributo state:
     * Estado de la ejecucion
     * 
     * @return Valor del atributo
     */
	public RuntimeState getState() {
		return state;
	}

    /**
     * Da valor al atributo state:
     * Estado de la ejecucion
     * 
     * @param value Valor del atributo
     */
	public void setState(RuntimeState state) {
		this.state = state;
	}

}
