package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las medidas base
 * 
 * @author Edelia
 *
 */
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
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 */
	public BaseMeasure(String id, String name, String description, String scale, String unitOfMeasure) {
		super(id, name, description, scale, unitOfMeasure);
	}
	
}
