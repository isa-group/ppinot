package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.DataPropertyCondition;

/**
 * Clase con la información de un PPI del tipo DataPropertyConditionInstanceMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class DataPropertyConditionInstanceMeasure extends ConditionMeasure {
	
	/**
	 * Constructor de la clase
	 */
	public DataPropertyConditionInstanceMeasure() {
		super();
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param condition Condición de la medida
	 */
	public DataPropertyConditionInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			DataPropertyCondition condition) {
		super(id, name, description, scale, unitOfMeasure, condition);
	}

}
