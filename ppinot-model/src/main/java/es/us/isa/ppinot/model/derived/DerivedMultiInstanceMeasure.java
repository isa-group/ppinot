package es.us.isa.ppinot.model.derived;

/**
 * Clase de las medidas DerivedMultiInstanceMeasure
 * 
 * @author Edelia
 *
 */
public class DerivedMultiInstanceMeasure extends DerivedMeasure {

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
    public DerivedMultiInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String function) {

    	super(id, name, description, scale, unitOfMeasure, function);
	}
	
}
