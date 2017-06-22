package es.us.isa.ppinot.model;

//import es.us.isa.ppinot.deleted.composite.*;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.*;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.derived.ListMeasure;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase de la que heredan todas las definiciones de medidas
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CountMeasure.class, name = "CountMeasure"),
        @JsonSubTypes.Type(value = TimeMeasure.class, name = "TimeMeasure"),
        @JsonSubTypes.Type(value = ConditionMeasure.class, name="ConditionMeasure"),
        @JsonSubTypes.Type(value = DataMeasure.class, name="DataMeasure"),
        @JsonSubTypes.Type(value = DataPropertyConditionMeasure.class, name="DataPropertyConditionMeasure"),
        @JsonSubTypes.Type(value = StateConditionMeasure.class, name="StateConditionMeasure"),
        @JsonSubTypes.Type(value = DerivedMeasure.class, name="DerivedMeasure"),
        @JsonSubTypes.Type(value = DerivedMultiInstanceMeasure.class, name="DerivedMultiInstanceMeasure"),
        @JsonSubTypes.Type(value = DerivedSingleInstanceMeasure.class, name="DerivedSingleInstanceMeasure"),
        @JsonSubTypes.Type(value = ListMeasure.class, name="ListMeasure"), //Agregated 2017-02-22
        @JsonSubTypes.Type(value = AggregatedMeasure.class, name = "AggregatedMeasure"),
        
        //@JsonSubTypes.Type(value = Metric.class, name = "Metric")
 })
		
public abstract class MeasureDefinition implements Cloneable{
    
	// Propiedades comunes para definir todas las medidas
    	// Id de la medida
    private String id;
    	// Nombre de la medida
    private String name;
    	// Descripcion de la medida
    private String description;
    	// Escala de la medida
    private String scale;
		// Unidad de medida del indicador
	private String unitOfMeasure;
	
	/**
	 * Constructor de la clase
	 */
	public MeasureDefinition() {
		
		super();
    	this.setId("");
    	this.setName("");
    	this.setDescription("");
    	this.setScale("");
    	this.setUnitOfMeasure("");
	}
	
	/**
	 * Constructor de la clase
	 * 
     * @param id Measure ID
     * @param name Measure name
     * @param description Measure description
     * @param scale Measure scale
     * @param unitOfMeasure Unit of measure
	 */
	public MeasureDefinition(String id, String name, String description, String scale, String unitOfMeasure) {
		
		super();
    	this.setId(id);
    	this.setName(name);
    	this.setDescription(description);
    	this.setScale(scale);
    	this.setUnitOfMeasure(unitOfMeasure);
	}

	/**
     * Devuelve el atributo id:
     * Id de la medida
     * 
     * @return Valor del atributo
     */
    public String getId() {
        return this.id;
    }

    /**
     * Da valor al atributo id:
     * Nombre de la medida
     * 
     * @param value Valor del atributo
     */
    public void setId(String value) {
    	if (value==null)
    		this.id = "";
    	else
    		this.id = value;
    }

	/**
     * Devuelve el atributo name:
     * Nombre de la medida
     * 
     * @return Valor del atributo
     */
    public String getName() {
		return this.name;
	}

    /**
     * Da valor al atributo name:
     * Nombre de la medida
     * 
     * @param name Valor del atributo
     */
	public void setName(String name) {
    	if (name==null)
    		this.name = "";
    	else
    		this.name = name;
	}

	/**
     * Devuelve el atributo description:
     * Descripcion de la medida
     * 
     * @return Valor del atributo
     */
	public String getDescription() {
		return this.description;
	}

    /**
     * Da valor al atributo description:
     * Descripcion de la medida
     * 
     * @param description Valor del atributo
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * Devuelve el atributo scale:
     * Escala de la medida
     * 
     * @return Valor del atributo
     */
	public String getScale() {
		return this.scale;
	}

    /**
     * Da valor al atributo scale:
     * Escala de la medida
     * 
     * @param scale Valor del atributo
     */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * Devuelve el valor del atributo unitOfMeasure:
	 * Porcentaje en que se satisface el indicador
	 * 
	 * @return Valor del atributo
	 */
	public String getUnitOfMeasure() {
		return this.unitOfMeasure;
	}

	/**
	 * Da valor al atributo unitOfMeasure:
	 * Porcentaje en que se satisface el indicador
	 * 
	 * @param unitOfMeasure Valor del atributo
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
	/**
	 * Indica si el valor de la medida puede ser calculado y mostrado
	 * 
	 * @return Whether the measure is valid
	 */
	public boolean valid() {
		
		return this.getId()!=null && !this.getId().contentEquals("");
	}
	
	public MeasureDefinition clone(){
		final MeasureDefinition clone;
		
		try{
			
			clone = (MeasureDefinition) super.clone();
			
			return clone;
			
		}catch(Exception e){
			System.out.println("\t!>>>> Excepción en MeasureDefinition - clone()\n~~~" + e.getMessage());
			return null;
		}
	}
}
