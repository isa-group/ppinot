package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.StateCondition;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase con la informaci�n de un PPI del tipo ElementConditionInstanceMeasure
 * 
 * @author Edelia Garc�a Gonz�lez
 * @version 1.0
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
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


	/**
     * Devuelve el atributo condition:
     * Condici�n de la medida
     * 
     * @return Valor del atributo
     */
	public StateCondition getCondition() {
		return (StateCondition) super.getCondition();
	}

    /**
     * Da valor al atributo condition:
     * Condici�n de la medida
     * 
     * @param value Valor del atributo
     */
	public void setCondition(StateCondition condition) {
		super.setCondition(condition);
	}
	
	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return 
	 */
	public Boolean getCond() {
		
		return super.getCond() &&
				this.getCondition().getAppliesTo()!=null && this.getCondition().getAppliesTo()!="" &&	
				this.getCondition().getState().getStateString()!=null && this.getCondition().getState().getStateString()!="";
	}

}
