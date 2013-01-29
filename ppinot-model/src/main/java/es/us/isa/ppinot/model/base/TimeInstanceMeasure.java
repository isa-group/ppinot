package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;

/**
 * Clase con la informaci�n de un PPI del tipo ElapsedTimeInstanceMeasure
 * 
 * @author Edelia Garc�a Gonz�lez
 * @version 1.0
 *
 */
public class TimeInstanceMeasure extends BaseMeasure {
	
	// Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
	private TimeInstantCondition from;
	// Momento en el cual se toma la medida en la actividad final (el inicio o el final)
	private TimeInstantCondition to;
	// Tipo de la medida (c�clica o lineal)
	private TimeMeasureType timeMeasureType;
	// Funci�n de agregaci�n
	private String singleInstanceAggFunction;

	/**
	 * Constructor de la clase
	 */
	public TimeInstanceMeasure() {
		super();
		
    	this.setUnitOfMeasure("mseg");
    	
		this.setFrom(null);
		this.setTo(null);
		this.setTimeMeasureType(TimeMeasureType.LINEAR);
		this.setSingleInstanceAggFunction("");
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripci� de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param from Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
	 * @param to Momento en el cual se toma la medida en la actividad final (el inicio o el final)
	 * @param timeMeasureType Tipo de la medida (c�clica o lineal)
	 * @param singleInstanceAggFunction Funci�n de agregaci�n
	 */
	public TimeInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			TimeInstantCondition from, TimeInstantCondition to, TimeMeasureType timeMeasureType, String singleInstanceAggFunction) {
		super(id, name, description, scale, unitOfMeasure);
		this.setFrom(from);
		this.setTo(to);
		this.setTimeMeasureType(timeMeasureType);
		this.setSingleInstanceAggFunction(singleInstanceAggFunction);
	}

	/**
	 * Devuelve el atributo timeMeasureType
	 * Tipo de la medida (c�clica o lineal)
	 * 
	 * @return Valor del atributo
	 */
	public TimeMeasureType getTimeMeasureType() {
		return this.timeMeasureType;
	}

	/**
	 * Da valor al atributo timeMeasureType
	 * Tipo de la medida (c�clica o lineal)
	 * 
	 * @param Valor del atributo
	 */
	public void setTimeMeasureType(TimeMeasureType timeMeasureType) {
		this.timeMeasureType = timeMeasureType;
	}

	/**
	 * Devuelve el atributo singleInstanceAggFunction
	 * Funci�n de agregaci�n
	 * 
	 * @return Valor del atributo
	 */
	public String getSingleInstanceAggFunction() {
		return this.singleInstanceAggFunction;
	}

	/**
	 * Da valor al atributo singleInstanceAggFunction
	 * Funci�n de agregaci�n
	 * 
	 * @param Valor del atributo
	 */
	public void setSingleInstanceAggFunction(String singleInstanceAggFunction) {
		this.singleInstanceAggFunction = singleInstanceAggFunction;
	}

	/**
     * Devuelve el atributo from:
     * Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
     * 
     * @return Valor del atributo
     */
	public TimeInstantCondition getFrom() {
		return from;
	}

    /**
     * Da valor al atributo from:
     * Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
     * 
     * @param value Valor del atributo
     */
	public void setFrom(TimeInstantCondition from) {
		this.from = from;
	}

	/**
     * Devuelve el atributo to:
     * Momento en el cual se toma la medida en la actividad final (el inicio o el final)
     * 
     * @return Valor del atributo
     */
	public TimeInstantCondition getTo() {
		return to;
	}

    /**
     * Da valor al atributo to:
     * Momento en el cual se toma la medida en la actividad final (el inicio o el final)
     * 
     * @param value Valor del atributo
     */
	public void setTo(TimeInstantCondition to) {
		this.to = to;
	}
	
}
