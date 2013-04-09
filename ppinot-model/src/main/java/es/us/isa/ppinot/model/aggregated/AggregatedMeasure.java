package es.us.isa.ppinot.model.aggregated;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.BaseMeasure;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase con la informacion de un PPI del tipo AggregatedMeasure
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class AggregatedMeasure extends MeasureDefinition {
    
    // Funcion de agregacion que se aplica
    private String aggregationFunction;

    protected String samplingFrequency;
    
    // Informacion para agrupar la medida
    private DataContentSelection groupedBy;

    // La medida que se agrega
    protected MeasureDefinition baseMeasure;
    
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
    	
    	this.setAggregates(false);
    	this.setGroupedBy(null);
    }
    
    /**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param measureUnit Unidad de medida
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
    	
    	this.setAggregates(false);
    	this.setGroupedBy(new DataContentSelection());
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
     * @param value Valor del atributo
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
     * @param baseMeasure La medida que se agrega
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
	public DataContentSelection getGroupedBy() {
		return groupedBy;
	}

    /**
     * Da valor al atributo groupedBy:
     * Informacion para agrupar la medida
     * 
     * @param baseMeasure La medida que se agrega
     */
	public void setGroupedBy(DataContentSelection groupedBy) {
		this.groupedBy = groupedBy;
	}
	
	/**
	 * Indica si el valor de la medida puede ser calculado y mostrado
	 * 
	 * @return 
	 */
	public Boolean getCond() {

		return super.getCond() && 
				this.getBaseMeasure().getCond() &&
				this.getAggregationFunction()!=null && this.getAggregationFunction()!="";
	}

}
