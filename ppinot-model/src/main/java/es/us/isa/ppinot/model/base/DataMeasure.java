package es.us.isa.ppinot.model.base;

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
public class DataMeasure extends BaseMeasure {
	
	// Dataobject y propiedad a la que se le aplica la medida
	private DataContentSelection dataContentSelection;
	// Condicion de la medida
	private Condition precondition;
	
	/**
	 * Constructor de la clase
	 */
	public DataMeasure() {
		super();
		this.setDataContentSelection(null);
		this.setPrecondition(null);
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

}
