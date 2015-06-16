package es.us.isa.ppinot.model.composite.type;

import es.us.isa.ppinot.model.composite.CompositeType;

/**
 * @author BEstrada
 *
 */
public class PercentagePerformanceType extends CompositeType{
	
	public double expectedValue;
	public double reachedValue;
	
	/**
	 * Constructor de la clase
	 */
	public PercentagePerformanceType(){
		super();
		this.expectedValue = 0;
		this.reachedValue = 0;
	}
	/**
	 * Constructor de la clase
	 */
	public PercentagePerformanceType(String name, double expectedValue, double reachedValue) {
		super(name);
		this.expectedValue = expectedValue;
		this.reachedValue = reachedValue;
	}
	
	/**
	 * Devuelve el atributo expectedValue. Valor de referencia.
	 * 
	 * @return the expectedValue
	 */
	public double getExpectedValue() {
		return expectedValue;
	}
	/**
	 * Da valor al atributo expectedValue. Valor de referencia.
	 * 
	 * @param expectedValue the expectedValue to set
	 */
	public void setExpectedValue(double expectedValue) {
		this.expectedValue = expectedValue;
	}
	/**
	 * Devuelve el atributo reachedValue. Valor alcanzado y a comparar.
	 * 
	 * @return reachedValue
	 */
	public double getReachedValue() {
		return reachedValue;
	}
	/**
	 * Da valor al atributo reachedValue. Valor alcanzado y a comparar.
	 * 
	 * @param reachedValue
	 */
	public void setReachedValue(double reachedValue) {
		this.reachedValue = reachedValue;
	}
	
	
	
	
}
