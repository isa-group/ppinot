package es.us.isa.ppinot.model.aggregated;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.BaseMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con la informacion de un PPI del tipo AggregatedMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class AggregatedMeasure extends MeasureDefinition {
    
    // Funcion de agregacion que se aplica
    private String aggregationFunction;

    protected String samplingFrequency;
    
    // Informacion para agrupar la medida
    private List<DataMeasure> groupedBy;

    // La medida que se agrega
    protected MeasureDefinition baseMeasure;
    
    protected MeasureDefinition filter;
    
    protected boolean includeUnfinished = false;

	protected DataMeasure periodReferencePoint = null;

	protected boolean includeEvidences = false;

    
    // Indica si la medida utiliza el conector aggregates o no
    protected Boolean aggregates;

	/**
     * Constructor de la clase
     */
    public AggregatedMeasure() {
    	
    	super();
    	
    	this.setAggregationFunction("");
    	this.setSamplingFrequency("");
    	this.setBaseMeasure(new BaseMeasure());
    	this.setFilter(null);
    	
    	this.setAggregates(false);
    	this.setGroupedBySelections(null);
    }

    public boolean isIncludeUnfinished() {
        return includeUnfinished;
    }

	public AggregatedMeasure setIncludeUnfinished(boolean includeUnfinished) {
		this.includeUnfinished = includeUnfinished;
		return this;
	}

	/**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param aggregationFunction Funcion de la medida
     * @param samplingFrequency
     * @param baseMeasure La medida que se agrega
     */
    public AggregatedMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String aggregationFunction, String samplingFrequency, MeasureDefinition baseMeasure) {
    	
    	super(id, name, description, scale, unitOfMeasure);
    	
    	this.setAggregationFunction(aggregationFunction);
    	this.setSamplingFrequency(samplingFrequency);
    	this.setBaseMeasure(baseMeasure);
        this.setFilter(null);
    	
    	this.setAggregates(false);
    	this.setGroupedBySelections(null);
	}
    
    
    /**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param aggregationFunction Funcion de la medida
     * @param samplingFrequency
     * @param baseMeasure La medida que se agrega
     * @param filter 
     */
    public AggregatedMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String aggregationFunction, String samplingFrequency, MeasureDefinition baseMeasure, MeasureDefinition filter) {
    	
    	super(id, name, description, scale, unitOfMeasure);
    	
    	this.setAggregationFunction(aggregationFunction);
    	this.setSamplingFrequency(samplingFrequency);
    	this.setBaseMeasure(baseMeasure);
        this.setFilter(filter);
    	
    	this.setAggregates(false);
    	this.setGroupedBySelections(null);
	}

    /**
     * Devuelve el atributo aggregationFunction:
     * Funcion de agregacion que se aplica
     * 
     * @return Valor del atributo
     */
    public String getAggregationFunction() {
        return this.aggregationFunction;
    }

    /**
     * Da valor al atributo aggregationFunction:
     * Funcion de agregacion que se aplica
     * 
     * @param value Valor del atributo
     */
    public void setAggregationFunction(String value) {
        this.aggregationFunction = value;
    }
    
    /**
     * Devuelve el atributo samplingFrequency:
     * 
     * 
     * @return Valor del atributo
     */
    public String getSamplingFrequency() {
		return this.samplingFrequency;
	}

    /**
     * Da valor al atributo samplingFrequency:
     * 
     * 
     * @param samplingFrequency
     */
	public void setSamplingFrequency(String samplingFrequency) {
		this.samplingFrequency = samplingFrequency;
	}
    
    /**
     * Devuelve el atributo baseMeasure:
     * La medida que se agrega
     * 
     * @return La medida que se agrega
     */
    public MeasureDefinition getBaseMeasure() {

    	return this.baseMeasure;
    }
    
    /**
     * Da valor al atributo baseMeasure:
     * La medida que se agrega
     * 
     * @param baseMeasure La medida que se agrega
     */
    public void setBaseMeasure(MeasureDefinition baseMeasure) {
    	
    	this.baseMeasure = baseMeasure;
    }

    public MeasureDefinition getFilter() {
        return filter;
    }

    public void setFilter(MeasureDefinition filter) {
        this.filter = filter;
    }

    /**
     * Devuelve el atributo aggregates:
     * Indica si la medida utiliza el conector aggregates o no
     * 
     * @return La medida que se agrega
     */
    public Boolean getAggregates() {
		return aggregates;
	}

    /**
     * Da valor al atributo aggregates:
     * Indica si la medida utiliza el conector aggregates o no
     * 
     * @param aggregates
     */
	public void setAggregates(Boolean aggregates) {
		this.aggregates = aggregates;
	}

    /**
     * Devuelve el atributo groupedBy:
     * Informacion para agrupar la medida
     * 
     * @return La medida que se agrega
     */
	public List<DataContentSelection> getGroupedBySelections() {
		List<DataContentSelection> selections = new ArrayList<DataContentSelection>();
		for (DataMeasure m : groupedBy) {
			selections.add(m.getDataContentSelection());
		}

		return selections;
	}

    /**
     * Da valor al atributo groupedBy:
     * Informacion para agrupar la medida
     *
     * @param groupedBy
     */
	public void setGroupedBySelections(List<DataContentSelection> groupedBy) {
		this.groupedBy = new ArrayList<DataMeasure>();
		if (groupedBy != null) {
			for (DataContentSelection s : groupedBy) {
				DataMeasure dm = new DataMeasure();
				dm.setDataContentSelection(s);
				this.groupedBy.add(dm);
			}

		}
	}

	public List<DataMeasure> getGroupedBy() {
		return groupedBy;
	}

	public AggregatedMeasure setGroupedBy(List<DataMeasure> groupedBy) {
		this.groupedBy = groupedBy;
		return this;
	}

	public DataMeasure getPeriodReferencePoint() {
		return periodReferencePoint;
	}

	public AggregatedMeasure setPeriodReferencePoint(DataMeasure periodReferencePoint) {
		this.periodReferencePoint = periodReferencePoint;
		return this;
	}

	public boolean isIncludeEvidences() {
		return includeEvidences;
	}

	public void setIncludeEvidences(boolean includeEvidences) {
		this.includeEvidences = includeEvidences;
	}

	/**
	 * Indica si el valor de la medida puede ser calculado y mostrado
	 * 
	 * @return 
	 */
	public boolean valid() {

		return super.valid() &&
				this.getBaseMeasure().valid() &&
				this.getAggregationFunction()!=null && !this.getAggregationFunction().isEmpty();
	}

}
