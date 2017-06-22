package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;

/**
 * Clase con la informacion de las medidas CountMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class CountMeasure extends BaseMeasure implements Cloneable{

	// Momento en que se aplica la medida
	private TimeInstantCondition when;
	
	/**
	 * Constructor de la clase
	 */
	public CountMeasure() {
		super();
		this.setWhen(null);
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param when Momento en que se aplica la medida
	 */
	public CountMeasure(String id, String name, String description, String scale, String unitOfMeasure,
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
     * @param when Valor del atributo
     */
	public void setWhen(TimeInstantCondition when) {
		this.when = when;
	}
	
	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return valid
	 */
	public boolean valid() {
		
		return super.valid() &&
				this.getWhen().getAppliesTo()!=null && !this.getWhen().getAppliesTo().isEmpty();
	}
	
	public CountMeasure clone(){
		
		final CountMeasure clone;
		
		try{

			clone = (CountMeasure) super.clone();

		}catch(Exception ex){
			throw new RuntimeException( "\t!>>>> Excepción en CountMeasure - clone()" );
		}
		
		return clone;
	}

}
