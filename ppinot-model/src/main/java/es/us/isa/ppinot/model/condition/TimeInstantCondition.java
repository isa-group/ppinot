package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Clase de las condiciones que deben cumplirse en un momento instante de tiempo (inicio o fin)
 * 
 * @author Edelia
 *
 */
public class TimeInstantCondition extends Condition {

	// Estado de la ejecucion
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
	 * @param changesToState Estado de la ejecucion
	 */
	public TimeInstantCondition (String appliesTo, RuntimeState changesToState) {
		super(appliesTo);
		this.setChangesToState(changesToState);
	}
	
	/**
     * Devuelve el atributo changesToState:
     * Estado de la ejecucion
     * 
     * @return Valor del atributo
     */
	public RuntimeState getChangesToState() {
		return changesToState;
	}

    /**
     * Da valor al atributo changesToState:
     * Estado de la ejecucion
     * 
     * @param changesToState Valor del atributo
     */
	public void setChangesToState(RuntimeState changesToState) {
		this.changesToState = changesToState;
	}
	
	public TimeInstantCondition clone(){
		
		final TimeInstantCondition clone;
		
		try{
			
			clone = (TimeInstantCondition) super.clone();
			//(NO-USA)clone.changesToState = this.changesToState.clone(); Un ENUM no se puede clonar
			return clone;
		}catch(Exception e){
			System.out.println("\t!>>>> Excepción en TimeInstantCondition - clone()\n~~~" + e.getMessage());
			return null;
		}
		
	}
}
