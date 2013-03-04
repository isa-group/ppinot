package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;

/**
 * Clase con la información de las medidas CountInstanceMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class CountInstanceMeasure extends BaseMeasure {

	// Momento en que se aplica la medida
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
	 * @param when Momento en que se aplica la medida
	 */
	public CountInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure, 
			TimeInstantCondition when) {
		super(id, name, description, scale, unitOfMeasure);
		this.setWhen(when);
	}

	/**
     * Devuelve el atributo when:
     * Momento en que se aplica la medida
     * 
     * @return Valor del atributo
     */
	public TimeInstantCondition getWhen() {
		return when;
	}

    /**
     * Da valor al atributo when:
     * Momento en que se aplica la medida
     * 
     * @param value Valor del atributo
     */
	public void setWhen(TimeInstantCondition when) {
		this.when = when;
	}
	
	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return 
	 */
	public Boolean getCond() {
		
		return super.getCond() && 
				this.getWhen().getAppliesTo()!=null && this.getWhen().getAppliesTo()!="";
	}

}
