package es.us.isa.ppinot.model.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.Object;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.condition.Condition;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;


/**
 * Clase con la informacion de una medida DataMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class DataMeasure extends BaseMeasure implements Cloneable{
	
	// Dataobject y propiedad a la que se le aplica la medida
	private DataContentSelection dataContentSelection;
	// Condicion de la medida
	private Condition precondition;

	private boolean first = false;
	
	/**
	 * Constructor de la clase
	 */
	public DataMeasure() {
		super();
		this.setDataContentSelection(null);
		this.setPrecondition(null);
	}

	public boolean isFirst() {
		return first;
	}

	public DataMeasure setFirst(boolean first) {
		this.first = first;
		return this;
	}

	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcio de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param dataContentSelection Dataobject y propiedad a la que se le aplica la medida
	 * @param precondition Condicion de la medida
	 */
	public DataMeasure(String id, String name, String description, String scale, String unitOfMeasure,
                       DataContentSelection dataContentSelection, Condition precondition) {
		super(id, name, description, scale, unitOfMeasure);
		this.setDataContentSelection(dataContentSelection);
		this.setPrecondition(precondition);
	}

	/**
	 * Devuelve el atributo dataContentSelection
	 * Dataobject y propiedad a la que se le aplica la medida
	 * 
	 * @return Valor del atributo
	 */
	public DataContentSelection getDataContentSelection() {
		return this.dataContentSelection;
	}

	/**
	 * Da valor al atributo dataContentSelection
	 * Dataobject y propiedad a la que se le aplica la medida
	 * 
	 * @param dataContentSelection Data selection
	 */
	public void setDataContentSelection(DataContentSelection dataContentSelection) {
		this.dataContentSelection = dataContentSelection;
	}

	/**
	 * Devuelve el atributo precondition
	 * Condicion de la medida
	 * 
	 * @return Valor del atributo
	 */
	public Condition getPrecondition() {
		return precondition;
	}

	/**
	 * Da valor al atributo precondition
	 * Condicion de la medida
	 * 
	 * @param precondition Data property precondition that enables the measure
	 */
	public void setPrecondition(Condition precondition) {
		this.precondition = precondition;
	}
	
	/**
	 * Indica si el valor la medida puede ser calculada
	 * 
	 * @return Whether the measure is valid
	 */
	public boolean valid() {
		
		return super.valid() &&
				this.getDataContentSelection().getDataobjectId()!=null && ! "".equals(this.getDataContentSelection().getDataobjectId()) &&
				this.getDataContentSelection().getSelection()!=null && ! this.getDataContentSelection().getSelection().isEmpty();
	}
		
	public DataMeasure clone() {
        final DataMeasure clone;
        try {
        	//Se recomienda usar DEEP-CLONE y no SHALLOW-CLONE
        	//clone = new DataMeasure(this.getId(), this.getName(), this.getDescription(), this.getScale(), this.getUnitOfMeasure(), this.getDataContentSelection(), this.getPrecondition());
        	
        	//Shallow-Clone
        	clone = (DataMeasure) super.clone();
        	
        }
        catch( Exception ex ) { //CloneNotSupportedException
            throw new RuntimeException( "\t!>>>> Excepción en DataMeasure - clone() \n" + ex.getMessage());
        }
        
        return clone;
    }
	
	public DataMeasure copy() {
		DataMeasure copy = new DataMeasure(getId(), getName(), getDescription(), getScale(), getUnitOfMeasure(), dataContentSelection, precondition);
		copy.first = this.first;
		return copy;
	}

}
