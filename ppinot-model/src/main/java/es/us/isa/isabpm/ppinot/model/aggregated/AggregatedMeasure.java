package es.us.isa.isabpm.ppinot.model.aggregated;

import es.us.isa.isabpm.ppinot.model.DataContentSelection;
import es.us.isa.isabpm.ppinot.model.MeasureDefinition;
import es.us.isa.isabpm.ppinot.model.base.BaseMeasure;

/**
 * Clase con la información de un PPI del tipo AggregatedMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class AggregatedMeasure extends MeasureDefinition {
    
    /**
     * Función de agregación que se aplica
     */
    private String aggregationFunction;

    protected String samplingFrequency;
    
    private DataContentSelection groupedBy;

   /**
     * La medida que se agrega
     */
    protected MeasureDefinition baseMeasure;
    
    /**
     * Indica si la medida utiliza el conector aggregates o no
     */
    protected Boolean aggregates;

	/**
     * Constructor de la clase
     */
    public AggregatedMeasure() {
    	
    	super();
    	
    	this.setAggregationFunction("");
    	this.setSamplingFrequency("");
    	this.setBaseMeasure(new BaseMeasure());
    	
    	this.setAggregates(false);
    	this.setGroupedBy(null);
    }
    
    /**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param measureUnit Unidad de medida
     * @param aggregationFunction Función de la medida
     * @param samplingFrequency
     */
    public AggregatedMeasure(String id, String name, String description, String scale, String unitOfMeasure,
    		String aggregationFunction, String samplingFrequency, MeasureDefinition baseMeasure) {
    	
    	super(id, name, description, scale, unitOfMeasure);
    	
    	this.setAggregationFunction(aggregationFunction);
    	this.setSamplingFrequency(samplingFrequency);
    	this.setBaseMeasure(baseMeasure);
    	
    	this.setAggregates(false);
    	this.setGroupedBy(new DataContentSelection());
	}

    /**
     * Devuelve el atributo aggregationFunction:
     * Función de agregación que se aplica
     * 
     * @return Valor del atributo
     */
    public String getAggregationFunction() {
        return this.aggregationFunction;
    }

    /**
     * Da valor al atributo aggregationFunction:
     * Función de agregación que se aplica
     * 
     * @param value Valor del atributo
     */
    public void setAggregationFunction(String value) {
        this.aggregationFunction = value;
    }
    
    public String getSamplingFrequency() {
		return this.samplingFrequency;
	}

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

    public Boolean getAggregates() {
		return aggregates;
	}

	public void setAggregates(Boolean aggregates) {
		this.aggregates = aggregates;
	}

	public DataContentSelection getGroupedBy() {
		return groupedBy;
	}

	public void setGroupedBy(DataContentSelection groupedBy) {
		this.groupedBy = groupedBy;
	}

}
