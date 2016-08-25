package es.us.isa.ppinot.model.condition;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Clase de las condiciones
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class ConditionImpl implements Condition {
	
	// Id del elemento al que se aplica una medida
	private String appliesTo;

    public ConditionImpl() {}

	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 */
	public ConditionImpl(String appliesTo) {

		super();
        this.appliesTo = appliesTo;
    }

	/**
     *
     * @return Returns the id of the element to which the condition applies
     */
    @Override
	public String getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(String appliesTo) {
		this.appliesTo = appliesTo;
	}
}
