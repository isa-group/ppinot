package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.ProcessInstanceCondition;

public class ConditionMeasure extends BaseMeasure {
	
	private ProcessInstanceCondition condition;
	
	/**
	 * Constructor de la clase
	 */
	public ConditionMeasure() {
		super();
		this.setCondition(null);
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param activity Identificador de la actividad a la que se le aplica la medida
	 * @param atEnd Momento en que se aplica
	 */
	public ConditionMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			ProcessInstanceCondition condition) {
		super(id, name, description, scale, unitOfMeasure);
		this.setCondition(condition);
	}

	public ProcessInstanceCondition getCondition() {
		return condition;
	}

	public void setCondition(ProcessInstanceCondition condition) {
		this.condition = condition;
	}

}
