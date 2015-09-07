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
public class Condition implements Cloneable {
	
	// Id del elemento al que se aplica una medida
	private String appliesTo;

    public Condition() {}

	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 */
	public Condition(String appliesTo) {

		super();
        this.appliesTo = appliesTo;
    }

	/**
     *
     * @return Returns the id of the element to which the condition applies
     */
	public String getAppliesTo() {
		return appliesTo;
	}
	
	public Condition clone(){
		final Condition clone;
		
		try{		
			clone = (Condition)super.clone();
			
			if(this.appliesTo != null)
				clone.appliesTo = new String(this.appliesTo);
			else
				clone.appliesTo = null;
			
		}catch(Exception e){
			throw new RuntimeException( "\t!>>>> Excepción en Condition - clone()" );
		}
		return clone;
	}
}
