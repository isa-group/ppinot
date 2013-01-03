package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;

/**
 * Clase con la información de un PPI del tipo ElapsedTimeInstanceMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class TimeInstanceMeasure extends BaseMeasure {
	
	/**
	 * Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
	 */
	private TimeInstantCondition from;
	/**
	 * Momento en el cual se toma la medida en la actividad final (el inicio o el final)
	 */
	private TimeInstantCondition to;

	private String timeMeasureType;

	private String singleInstanceAggFunction;

	/**
	 * Constructor de la clase
	 */
	public TimeInstanceMeasure() {
		super();
		
    	this.setUnitOfMeasure("mseg");
    	
		this.setFrom(null);
		this.setTo(null);
		this.setTimeMeasureType("");
		this.setSingleInstanceAggFunction("");
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param refMax Límite máximo de la referencia de la medida
     * @param refMin Límite mínimo de la referencia de la medida
	 * @param activityFrom Identificador de la actividad inicial a la que se le aplica la medida
	 * @param atEndFrom Momento en que se aplica en la actividad inicial
	 * @param activityTo Identificador de la actividad final a la que se le aplica la medida
	 * @param atEndTo Momento en que se aplica en la actividad final
	 */
	public TimeInstanceMeasure(String id, String name, String description, String scale, String unitOfMeasure,
			TimeInstantCondition from, TimeInstantCondition to, String timeMeasureType, String singleInstanceAggFunction) {
		super(id, name, description, scale, unitOfMeasure);
		this.setFrom(from);
		this.setTo(to);
		this.setTimeMeasureType(timeMeasureType);
		this.setSingleInstanceAggFunction(singleInstanceAggFunction);
	}

	/**
	 * Devuelve el atributo timeMeasureType
	 * 
	 * @return Valor del atributo
	 */
	public String getTimeMeasureType() {
		return this.timeMeasureType;
	}

	/**
	 * Da valor al atributo timeMeasureType
	 * 
	 * @param Valor del atributo
	 */
	public void setTimeMeasureType(String timeMeasureType) {
		this.timeMeasureType = timeMeasureType;
	}

	/**
	 * Devuelve el atributo atEndTo
	 * 
	 * @return Valor del atributo
	 */
	public String getSingleInstanceAggFunction() {
		return this.singleInstanceAggFunction;
	}

	/**
	 * Da valor al atributo atEndTo
	 * 
	 * @param Valor del atributo
	 */
	public void setSingleInstanceAggFunction(String singleInstanceAggFunction) {
		this.singleInstanceAggFunction = singleInstanceAggFunction;
	}

	public TimeInstantCondition getFrom() {
		return from;
	}

	public void setFrom(TimeInstantCondition from) {
		this.from = from;
	}

	public TimeInstantCondition getTo() {
		return to;
	}

	public void setTo(TimeInstantCondition to) {
		this.to = to;
	}
	
}
