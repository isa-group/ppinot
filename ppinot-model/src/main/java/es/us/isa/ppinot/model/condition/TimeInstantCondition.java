package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Clase de las condiciones que deben cumplirse en un momento instante de tiempo (inicio o fin)
 * 
 * @author Edelia
 *
 */
public class TimeInstantCondition extends Condition {

	// Estado de la ejecuci�n
	private RuntimeState changesToState;

	/**
	 * Constructor de la clase
	 * 
	 */
	public TimeInstantCondition () {
		super();
		this.setChangesToState(null);
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 * @param changesToState Estado de la ejecuci�n
	 */
	public TimeInstantCondition (String appliesTo, RuntimeState changesToState) {
		super(appliesTo);
		this.setChangesToState(changesToState);
	}
	
	/**
     * Devuelve el atributo changesToState:
     * Estado de la ejecuci�n
     * 
     * @return Valor del atributo
     */
	public RuntimeState getChangesToState() {
		return changesToState;
	}

    /**
     * Da valor al atributo changesToState:
     * Estado de la ejecuci�n
     * 
     * @param value Valor del atributo
     */
	public void setChangesToState(RuntimeState changesToState) {
		this.changesToState = changesToState;
	}
}
