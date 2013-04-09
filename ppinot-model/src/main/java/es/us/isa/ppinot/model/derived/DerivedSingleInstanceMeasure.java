package es.us.isa.ppinot.model.derived;

/**
 * Clase de las medidas DerivedSingleInstanceMeasure
 * 
 * @author Edelia
 *
 */
public class DerivedSingleInstanceMeasure extends DerivedMeasure {

    /**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param measureUnit Unidad de medida
     * @param func Funcion de la medida
     */
    public DerivedSingleInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String function) {

    	super(id, name, description, scale, unitOfMeasure, function);
	}
	
}
