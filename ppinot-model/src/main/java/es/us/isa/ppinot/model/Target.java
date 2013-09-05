package es.us.isa.ppinot.model;

public class Target {
	
    // Upper bound of the target
    private Double refMax;
    // Lower bound of the target
    private Double refMin;
    
    public Target() {
    	super();
    	
    	this.setRefMax(null);
    	this.setRefMin(null);
    }
    
    /**
     *
     * 
     * @param refMax Upper bound of the target
     * @param refMin Lower bound of the target
     */
    public Target(Double refMin, Double refMax) {
    	
    	this.setRefMax(refMax);
    	this.setRefMin(refMin);
    }
    
	
	/**
     * Devuelve el atributo refMin:
     * Limite maximo de la referencia de la medida
     * 
     * @return Valor del atributo
     */
	public Double getRefMin() {
		return refMin;
	}
	
    /**
     * Da valor al atributo refMin:
     * Limite maximo de la referencia de la medida
     * 
     * @param refMin Valor del atributo
     */
	public void setRefMin(Double refMin) {
		this.refMin = refMin;
	}
	
	/**
     * Devuelve el atributo refMax:
     * Limite minimo de la referencia de la medida
     * 
     * @return Valor del atributo
     */
	public Double getRefMax() {
		return refMax;
	}
	
    /**
     * Da valor al atributo refMax:
     * Limite minimo de la referencia de la medida
     * 
     * @param refMax Valor del atributo
     */
	public void setRefMax(Double refMax) {
		this.refMax = refMax;
	}

}
