/************************************************************************/
/*** MODIFIED VERSION CM ************************************************/
/************************************************************************/

package es.us.isa.ppinot.model.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de las medidas DerivedMultiInstanceMeasure
 *
 * @author Edelia
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "kind")
public class DerivedMultiInstanceMeasure extends DerivedMeasure implements Cloneable{

    public DerivedMultiInstanceMeasure() {
        super();
    }


    /**
     * Constructor de la clase
     *
     * @param id            Id de la medida
     * @param name          Nombre de la medida
     * @param description   Descripcion de la medida
     * @param scale         Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param function      Funcion de la medida
     */
    public DerivedMultiInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
                                       String function) {

        super(id, name, description, scale, unitOfMeasure, function);
    }
    
    public DerivedMultiInstanceMeasure clone(){
    	
    	final DerivedMultiInstanceMeasure clone;
    	
    	try{
    		clone = (DerivedMultiInstanceMeasure) super.clone();
    	}catch(Exception ex){ //CloneNotSupported
    		throw new RuntimeException( "\t!>>>> Excepci�n en DerivedMultiInstanceMeasure - clone()" );
    	}
    	return clone;
    }

	public boolean valid() {
		return super.valid();
	}
}
