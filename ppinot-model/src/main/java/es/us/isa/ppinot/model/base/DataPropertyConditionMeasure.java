package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.DataPropertyCondition;

/**
 * Clase con la informacion de un PPI del tipo DataPropertyConditionMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class DataPropertyConditionMeasure extends ConditionMeasure {
	
	/**
	 * Constructor de la clase
	 */
	public DataPropertyConditionMeasure() {
		super();
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcio de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param condition Condicion de la medida
	 */
	public DataPropertyConditionMeasure(String id, String name, String description, String scale, String unitOfMeasure,
                                        DataPropertyCondition condition) {
		super(id, name, description, scale, unitOfMeasure, condition);
	}

	/**
     * Devuelve el atributo condition:
     * Condicion de la medida
     * 
     * @return Valor del atributo
     */
	public DataPropertyCondition getCondition() {
		return (DataPropertyCondition) super.getCondition();
	}

    /**
     * Da valor al atributo condition:
     * Condicion de la medida
     * 
     * @param condition Valor del atributo
     */
	public void setCondition(DataPropertyCondition condition) {
		super.setCondition(condition);
	}
	
	/**
	 * Indica si el valor la medida puede ser calculada
	 * 
	 * @return valid
	 */
	public boolean valid() {
		
		return super.valid() &&
				this.getCondition().getStateConsidered()!=null && ! this.getCondition().getStateConsidered().isEmpty();
	}

}
