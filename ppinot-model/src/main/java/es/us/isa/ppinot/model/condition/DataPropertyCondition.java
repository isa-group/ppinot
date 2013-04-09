package es.us.isa.ppinot.model.condition;

import es.us.isa.ppinot.model.state.RuntimeState;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las condiciones de un dataobject
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DataPropertyCondition extends ProcessInstanceCondition {

	// Condición que debe cumplir el dataobject
	private String restriction;
	
	// Estado del dataobject
	private RuntimeState state;
	
	// Nombre del dataobject
	private String dataobject;
	
	/**
	 * Constructor de la clase
	 */
	public DataPropertyCondition () {
		super();
		this.setRestriction("");
		this.setStateConsidered(null);
		this.setDataobject("");
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 * @param restriction Condición que debe cumplir el dataobject
	 * @param state Estado del dataobject
	 */
	public DataPropertyCondition (String appliesTo, String restriction, RuntimeState state, String dataobject) {
		super(appliesTo);
		this.setRestriction(restriction);
		this.setStateConsidered(state);
		this.setDataobject(dataobject);
	}

	/**
     * Devuelve el atributo restriction:
     * Condición que debe cumplir el dataobject
     * 
     * @return Valor del atributo
     */
	public String getRestriction() {
		return restriction;
	}

    /**
     * Da valor al atributo restriction:
     * Condición que debe cumplir el dataobject
     * 
     * @param value Valor del atributo
     */
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	/**
     * Devuelve el atributo state:
     * Estado del dataobject
     * 
     * @return Valor del atributo
     */
	public RuntimeState getStateConsidered() {
		return state;
	}

    /**
     * Da valor al atributo state:
     * Estado del dataobject
     * 
     * @param value Valor del atributo
     */
	public void setStateConsidered(RuntimeState state) {
		this.state = state;
	}

	/**
     * Devuelve el atributo dataobject:
     * Nombre del dataobject
     * 
     * @return Valor del atributo
     */
	public String getDataobject() {
		return dataobject;
	}

    /**
     * Da valor al atributo dataobject:
     * Nombre del dataobject
     * 
     * @param value Valor del atributo
     */
	public void setDataobject(String dataobject) {
		this.dataobject = dataobject;
	}

}
