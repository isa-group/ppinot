package es.us.isa.ppinot.model.condition;

/**
 * Clase de las condiciones que se utilizan en una medida base
 * 
 * @author Edelia
 *
 */
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
	
	public ProcessInstanceCondition clone(){

		final ProcessInstanceCondition clone;
		
		try{
			clone = (ProcessInstanceCondition) super.clone();
		}catch(Exception e){
			throw new RuntimeException( "\t!>>>> Excepción en ProcessInstanceCondition - clone()" );
		}
		return clone;
	}

}
