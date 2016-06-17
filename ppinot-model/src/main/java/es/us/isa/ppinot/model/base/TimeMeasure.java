package es.us.isa.ppinot.model.base;

import es.us.isa.ppinot.model.Schedule;
import es.us.isa.ppinot.model.TimeUnit;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;

/**
 * Clase con la informacion de un PPI del tipo ElapsedTimeInstanceMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class TimeMeasure extends BaseMeasure {

	// Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
	private TimeInstantCondition from;
	// Momento en el cual se toma la medida en la actividad final (el inicio o el final)
	private TimeInstantCondition to;
	// Tipo de la medida (ciclica o lineal)
	private TimeMeasureType timeMeasureType;
	// Funcion de agregacion
	private String singleInstanceAggFunction;

    private Schedule considerOnly;

	private boolean computeUnfinished;

	/**
	 * Constructor de la clase
	 */
	public TimeMeasure() {
		super();
		
    	this.setUnitOfMeasure(TimeUnit.MILLIS);
    	
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
     * @param description Descripcio de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
	 * @param from Momento en el cual se toma la medida en la actividad inicial (el inicio o el final)
	 * @param to Momento en el cual se toma la medida en la actividad final (el inicio o el final)
	 * @param timeMeasureType Tipo de la medida (ciclica o lineal)
	 * @param singleInstanceAggFunction Funcion de agregacion
	 */
	public TimeMeasure(String id, String name, String description, String scale, String unitOfMeasure,
                       TimeInstantCondition from, TimeInstantCondition to, TimeMeasureType timeMeasureType, String singleInstanceAggFunction) {
		super(id, name, description, scale, unitOfMeasure);
		this.setFrom(from);
		this.setTo(to);
		this.setTimeMeasureType(timeMeasureType);
		this.setSingleInstanceAggFunction(singleInstanceAggFunction);
	}

	/**
	 * Devuelve el atributo timeMeasureType
	 * Tipo de la medida (ciclica o lineal)
	 * 
	 * @return Valor del atributo
	 */
	public TimeMeasureType getTimeMeasureType() {
		return this.timeMeasureType;
	}

	/**
	 * Da valor al atributo timeMeasureType
	 * Tipo de la medida (ciclica o lineal)
	 * 
	 * @param timeMeasureType Time measure type
	 */
	public void setTimeMeasureType(TimeMeasureType timeMeasureType) {
		this.timeMeasureType = timeMeasureType;
	}

	/**
	 * Devuelve el atributo singleInstanceAggFunction
	 * Funcion de agregacion
	 * 
	 * @return Valor del atributo
	 */
	public String getSingleInstanceAggFunction() {
		return this.singleInstanceAggFunction;
	}

	/**
	 * Da valor al atributo singleInstanceAggFunction
	 * Funcion de agregacion
	 * 
	 * @param singleInstanceAggFunction del atributo
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
     * @param from Valor del atributo
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
     * @param to Valor del atributo
     */
	public void setTo(TimeInstantCondition to) {
		this.to = to;
	}

    public Schedule getConsiderOnly() {
        return considerOnly;
    }

    public void setConsiderOnly(Schedule considerOnly) {
        this.considerOnly = considerOnly;
    }

	public boolean isComputeUnfinished() {
		return computeUnfinished;
	}

	public TimeMeasure setComputeUnfinished(boolean computeUnfinished) {
		this.computeUnfinished = computeUnfinished;
		return this;
	}

	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return Whether the measure is valid
	 */
	public boolean valid() {
		
		return super.valid() &&
				this.getFrom().getAppliesTo()!=null && !this.getFrom().getAppliesTo().isEmpty() &&
				this.getTo().getAppliesTo()!=null && !this.getTo().getAppliesTo().isEmpty();
	}
	
}
