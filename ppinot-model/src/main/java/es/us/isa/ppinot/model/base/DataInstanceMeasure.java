package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase con la informaci�n de una medida DataInstanceMeasure
 * 
 * @author Edelia Garc�a Gonz�lez
 * @version 1.0
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DataInstanceMeasure extends BaseMeasure {
	
	// Dataobject y propiedad a la que se le aplica la medida
	private DataContentSelection dataContentSelection;
	// Condici�n de la medida
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
     * @param description Descripci� de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param dataContentSelection Dataobject y propiedad a la que se le aplica la medida
	 * @param condition Condici�n de la medida
	 */
	public DataInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			DataContentSelection dataContentSelection, DataPropertyCondition condition) {
		super(id, name, description, scale, unitOfMeasure);
		this.setDataContentSelection(dataContentSelection);
		this.setCondition(condition);
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
	 * @param Valor del atributo
	 */
	public void setDataContentSelection(DataContentSelection dataContentSelection) {
		this.dataContentSelection = dataContentSelection;
	}

	/**
	 * Devuelve el atributo condition
	 * Condici�n de la medida
	 * 
	 * @return Valor del atributo
	 */
	public DataPropertyCondition getCondition() {
		return condition;
	}

	/**
	 * Da valor al atributo condition
	 * Condici�n de la medida
	 * 
	 * @param Valor del atributo
	 */
	public void setCondition(DataPropertyCondition condition) {
		this.condition = condition;
	}
	
	/**
	 * Indica si el valor la medida puede ser calculada
	 * 
	 * @return 
	 */
	public Boolean getCond() {
		
		return super.getCond() &&
				this.getDataContentSelection().getDataobject()!=null && this.getDataContentSelection().getDataobject()!="" &&	
				this.getDataContentSelection().getSelection()!=null && this.getDataContentSelection().getSelection()!="";
	}

}
