package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.StateCondition;

/**
 * Clase con la informaci�n de un PPI del tipo ElementConditionInstanceMeasure
 * 
 * @author Edelia Garc�a Gonz�lez
 * @version 1.0
 *
 */
public class StateConditionInstanceMeasure extends ConditionMeasure {
	
	/**
	 * Constructor de la clase
	 */
	public StateConditionInstanceMeasure() {
		super();
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripci� de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param condition Condici�n de la medida
	 */
	public StateConditionInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			StateCondition condition) {
		super(id, name, description, scale, unitOfMeasure, condition);
	}


}
