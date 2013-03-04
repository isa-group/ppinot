package es.us.isa.ppinot.model;

/**
 * Clase de la que heredan todas las definiciones de medidas
 * 
 * @author Edelia García González
 * @version 1.0
 */
public abstract class MeasureDefinition {
    
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
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripció de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param refMax Límite máximo de la referencia de la medida
     * @param refMin Límite mínimo de la referencia de la medida
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
     * Descripción de la medida
     * 
     * @return Valor del atributo
     */
	public String getDescription() {
		return this.description;
	}

    /**
     * Da valor al atributo description:
     * Descripción de la medida
     * 
     * @param name Valor del atributo
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
     * @param name Valor del atributo
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
	 * @return 
	 */
	public Boolean getCond() {
		
		return this.getId()!=null && !this.getId().contentEquals("");
	}
}
