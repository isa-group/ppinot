package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.MeasureDefinition;

public class BaseMeasure extends MeasureDefinition {
	
	/**
	 * Constructor de la clase
	 */
	public BaseMeasure() {
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
	 */
	public BaseMeasure(String id, String name, String description, String scale, String unitOfMeasure) {
		super(id, name, description, scale, unitOfMeasure);
	}
	
}
