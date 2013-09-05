package es.us.isa.ppinot.model.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las medidas DerivedSingleInstanceMeasure
 * 
 * @author Edelia
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DerivedSingleInstanceMeasure extends DerivedMeasure {

    public DerivedSingleInstanceMeasure() {
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
     * @param function Funcion de la medida
     */
    public DerivedSingleInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String function) {

    	super(id, name, description, scale, unitOfMeasure, function);
	}
	
}
