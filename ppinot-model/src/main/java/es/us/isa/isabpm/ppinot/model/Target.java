package es.us.isa.isabpm.ppinot.model;

public class Target {
	
	/**
	 * Límites de la medida
	 * se especifican en la propiedad target en el proceso modelado en BPMN
	 */
    	// Límite máximo de la referencia de la medida
    private Double refMax;
     	// Límite mínimo de la referencia de la medida
    private Double refMin;
    
    public Target() {
    	super();
    	
    	this.setRefMax(null);
    	this.setRefMin(null);
    }
    
    public Target(Double refMax, Double refMin) {
    	
    	this.setRefMax(refMax);
    	this.setRefMin(refMin);
    }
    
	public Double getRefMin() {
		return refMin;
	}
	
	public void setRefMin(Double refMin) {
		this.refMin = refMin;
	}
	
	public Double getRefMax() {
		return refMax;
	}
	
	public void setRefMax(Double refMax) {
		this.refMax = refMax;
	}

}
