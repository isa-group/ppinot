package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Class that represents the conditions that causes a trigger in a certain time instant.
 *
 * @author Edelia
 *
 */
public class TimeInstantCondition extends ConditionImpl {

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

    public DataPropertyCondition getPrecondition() {
        return precondition;
    }

    public TimeInstantCondition setPrecondition(DataPropertyCondition precondition) {
        this.precondition = precondition;
        return this;
    }
}
