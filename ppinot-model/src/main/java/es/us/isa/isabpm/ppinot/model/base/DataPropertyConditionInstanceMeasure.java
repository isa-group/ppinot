package es.us.isa.isabpm.ppinot.model.base;

import es.us.isa.isabpm.ppinot.model.condition.DataPropertyCondition;

/**
 * Clase con la informaci�n de un PPI del tipo DataPropertyConditionInstanceMeasure
 * 
 * @author Edelia Garc�a Gonz�lez
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
     * @param description Descripci� de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param dataobject Identificador del Dataobject al que se le aplica la medida
	 * @param state Estado que se desea consultar
	 */
	public DataPropertyConditionInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			DataPropertyCondition condition) {
		super(id, name, description, scale, unitOfMeasure, condition);
	}

}
