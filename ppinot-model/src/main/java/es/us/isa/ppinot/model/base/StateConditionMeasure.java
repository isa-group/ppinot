package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.StateCondition;

/**
 * Clase con la informacion de un PPI del tipo ElementConditionInstanceMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class StateConditionMeasure extends ConditionMeasure {
	
	/**
	 * Constructor de la clase
	 */
	public StateConditionMeasure() {
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
	public StateConditionMeasure(String id, String name, String description, String scale, String unitOfMeasure,
                                 StateCondition condition) {
		super(id, name, description, scale, unitOfMeasure, condition);
	}


	/**
     * Devuelve el atributo condition:
     * Condicion de la medida
     * 
     * @return Valor del atributo
     */
	public StateCondition getCondition() {
		return (StateCondition) super.getCondition();
	}

    /**
     * Da valor al atributo condition:
     * Condicion de la medida
     * 
     * @param condition Valor del atributo
     */
	public void setCondition(StateCondition condition) {
		super.setCondition(condition);
	}
	
	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return valid
	 */
	public boolean valid() {
		
		return super.valid() &&
				this.getCondition().getAppliesTo()!=null && !this.getCondition().getAppliesTo().isEmpty() &&
				this.getCondition().getState()!=null;
	}

}
