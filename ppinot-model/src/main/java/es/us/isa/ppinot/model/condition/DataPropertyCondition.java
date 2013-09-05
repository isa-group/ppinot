package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase de las condiciones de un dataobject
 * 
 * @author Edelia
 *
 */
public class DataPropertyCondition extends ProcessInstanceCondition {

	// Condicion que debe cumplir el dataobject
	private String restriction;
	
	// Possible states of the data object
	private Set<RuntimeState> statesConsidered;

    public DataPropertyCondition () {
        super();
        statesConsidered = new HashSet<RuntimeState>();
    }

	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 * @param restriction Condicion que debe cumplir el dataobject
	 * @param state Estado del dataobject
	 */
	public DataPropertyCondition (String appliesTo, String restriction, RuntimeState... state) {
		super(appliesTo);
		this.restriction = restriction;
        statesConsidered = new HashSet<RuntimeState>(Arrays.asList(state));
	}

	/**
     * Devuelve el atributo restriction:
     * Condicion que debe cumplir el dataobject
     * 
     * @return Valor del atributo
     */
	public String getRestriction() {
		return restriction;
	}

	/**
     * @return The states of the data object considered in the condition
     */
	public Set<RuntimeState> getStateConsidered() {
		return statesConsidered;
	}

    /**
     * @param state A new state considered in the condition.
     */
	public void addStateConsidered(RuntimeState state) {
		this.statesConsidered.add(state);
	}



}
