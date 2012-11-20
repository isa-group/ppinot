package es.us.isa.isabpm.ppinot.model.base;

import es.us.isa.isabpm.ppinot.model.condition.TimeInstantCondition;

/**
 * Clase con la información de un PPI del tipo CountInstanceMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class CountInstanceMeasure extends BaseMeasure {

	private TimeInstantCondition when;
	
	/**
	 * Constructor de la clase
	 */
	public CountInstanceMeasure() {
		super();
		this.setWhen(null);
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripción de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param activity Identificador de la actividad a la que se le aplica la medida
	 * @param atEnd Momento en que se aplica
	 */
	public CountInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure, 
			TimeInstantCondition when) {
		super(id, name, description, scale, unitOfMeasure);
		this.setWhen(when);
	}

	public TimeInstantCondition getWhen() {
		return when;
	}

	public void setWhen(TimeInstantCondition when) {
		this.when = when;
	}

}
