package es.us.isa.isabpm.ppinot.model.base;

import es.us.isa.isabpm.ppinot.model.DataContentSelection;
import es.us.isa.isabpm.ppinot.model.condition.DataPropertyCondition;

/**
 * Clase con la información de un PPI del tipo DataInstanceMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class DataInstanceMeasure extends BaseMeasure {
	
	private DataContentSelection dataContentSelection;
	
	private DataPropertyCondition condition;
	
	/**
	 * Constructor de la clase
	 */
	public DataInstanceMeasure() {
		super();
		this.setDataContentSelection(null);
		this.setCondition(null);
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param dataobject Identificador del Dataobject al que se le aplica la medida
	 * @param property Propiedad que se desea consultar
	 */
	public DataInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			DataContentSelection dataContentSelection, DataPropertyCondition condition) {
		super(id, name, description, scale, unitOfMeasure);
		this.setDataContentSelection(dataContentSelection);
		this.setCondition(condition);
	}

	/**
	 * Devuelve el atributo property
	 * Propiedad que se desea consultar
	 * 
	 * @return Valor del atributo
	 */
	public DataContentSelection getDataContentSelection() {
		return this.dataContentSelection;
	}

	/**
	 * Da valor al atributo property
	 * Propiedad que se desea consultar
	 * 
	 * @param Valor del atributo
	 */
	public void setDataContentSelection(DataContentSelection dataContentSelection) {
		this.dataContentSelection = dataContentSelection;
	}

	public DataPropertyCondition getCondition() {
		return condition;
	}

	public void setCondition(DataPropertyCondition condition) {
		this.condition = condition;
	}

}
