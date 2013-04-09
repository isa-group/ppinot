package es.us.isa.ppinot.model.condition;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las condiciones que se utilizan en una medida base
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class ProcessInstanceCondition extends Condition {
	
	/*
	 * Constructor de la clase
	 * 
	 */
	public ProcessInstanceCondition() {

		super();
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param appliesTo Id del elemento al que se aplica una medida
	 */
	public ProcessInstanceCondition(String appliesTo) {

		super(appliesTo);
	}

}
