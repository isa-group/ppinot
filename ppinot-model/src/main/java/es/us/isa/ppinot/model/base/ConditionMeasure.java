package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.ProcessInstanceCondition;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * CLase de las medidas que involucran una condición como DataPropertyConditionMeasure y StateConditionMeasure
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class ConditionMeasure extends BaseMeasure {
	
	// condición de la medida
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
     * @param description Descripción de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param condition Condición de la medida
	 */
	public ConditionMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			ProcessInstanceCondition condition) {
		super(id, name, description, scale, unitOfMeasure);
		this.setCondition(condition);
	}

	/**
     * Devuelve el atributo condition:
     * Condición de la medida
     * 
     * @return Valor del atributo
     */
	public ProcessInstanceCondition getCondition() {
		return condition;
	}

    /**
     * Da valor al atributo condition:
     * Condición de la medida
     * 
     * @param value Valor del atributo
     */
	public void setCondition(ProcessInstanceCondition condition) {
		this.condition = condition;
	}

}
