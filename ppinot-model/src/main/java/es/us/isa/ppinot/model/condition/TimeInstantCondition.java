package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Class that represents the conditions that causes a trigger in a certain time instant.
 *
 * @author Edelia
 *
 */
public class TimeInstantCondition extends Condition {

	private RuntimeState changesToState;
    private DataPropertyCondition precondition;


	/**
	 * Class constructor
	 * 
	 */
	public TimeInstantCondition () {
		super();
		this.setChangesToState(null);
        this.setPrecondition(null);
    }
	
	/**
	 * Class constructor
	 * 
	 * @param appliesTo Id of the element to which the conditions is applied
	 * @param changesToState Condition evaluated for the trigger
	 */
	public TimeInstantCondition (String appliesTo, RuntimeState changesToState) {
		super(appliesTo);
		this.setChangesToState(changesToState);
	}

    public TimeInstantCondition(String appliesTo, RuntimeState changesToState, DataPropertyCondition precondition) {
        super(appliesTo);
        this.changesToState = changesToState;
        this.precondition = precondition;
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

    public DataPropertyCondition getPrecondition() {
        return precondition;
    }

    public TimeInstantCondition setPrecondition(DataPropertyCondition precondition) {
        this.precondition = precondition;
        return this;
    }
}
