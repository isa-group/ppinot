package es.us.isa.ppinot.model.condition;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las condiciones
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class Condition {
	
	// Id del elemento al que se aplica una medida
	private String appliesTo;
	
	/**
	 * Constructor de la clase
	 * 
	 */
	public Condition() {

		super();
		this.setAppliesTo("");
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 */
	public Condition(String appliesTo) {

		super();
		this.setAppliesTo(appliesTo);
	}

	/**
     * Devuelve el atributo appliesTo:
     * Id del elemento al que se aplica una medida
     * 
     * @return Valor del atributo
     */
	public String getAppliesTo() {
		return appliesTo;
	}

    /**
     * Da valor al atributo appliesTo:
     * Id del elemento al que se aplica una medida
     * 
     * @param value Valor del atributo
     */
	public void setAppliesTo(String appliesTo) {
		this.appliesTo = appliesTo;
	}
	
	

}
