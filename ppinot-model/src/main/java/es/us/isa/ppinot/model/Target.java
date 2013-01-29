package es.us.isa.ppinot.model;

public class Target {
	
	// Límites de la medida asociada a un PPI
    	// Límite máximo de la referencia de la medida
    private Double refMax;
     	// Límite mínimo de la referencia de la medida
    private Double refMin;
    
    /**
     * Constructor de la clase 
     */
    public Target() {
    	super();
    	
    	this.setRefMax(null);
    	this.setRefMin(null);
    }
    
    /**
     * Constructor de la clase
     * 
     * @param refMax Límite máximo de la referencia de la medida
     * @param refMin Límite mínimo de la referencia de la medida
     */
    public Target(Double refMax, Double refMin) {
    	
    	this.setRefMax(refMax);
    	this.setRefMin(refMin);
    }
    
	
	/**
     * Devuelve el atributo refMin:
     * Límite máximo de la referencia de la medida
     * 
     * @return Valor del atributo
     */
	public Double getRefMin() {
		return refMin;
	}
	
    /**
     * Da valor al atributo refMin:
     * Límite máximo de la referencia de la medida
     * 
     * @param value Valor del atributo
     */
	public void setRefMin(Double refMin) {
		this.refMin = refMin;
	}
	
	/**
     * Devuelve el atributo refMax:
     * Límite mínimo de la referencia de la medida
     * 
     * @return Valor del atributo
     */
	public Double getRefMax() {
		return refMax;
	}
	
    /**
     * Da valor al atributo refMax:
     * Límite mínimo de la referencia de la medida
     * 
     * @param value Valor del atributo
     */
	public void setRefMax(Double refMax) {
		this.refMax = refMax;
	}

}
