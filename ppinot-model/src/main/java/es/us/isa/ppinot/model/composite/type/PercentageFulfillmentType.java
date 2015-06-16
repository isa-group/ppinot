package es.us.isa.ppinot.model.composite.type;

import es.us.isa.ppinot.model.composite.CompositeType;

/**
 * @author BEstrada
 *
 */
public class PercentageFulfillmentType extends CompositeType {
	public String aValue;
	public String bValue;
	public int totalInstances;
	
	/**
	 * Constructor de la clase
	 */
	public PercentageFulfillmentType(){
		super();
		this.aValue = "";
		this.bValue = "";
		this.totalInstances = 0;
	}
	
	/**
	 * Constructor de la clase
	 */
	public PercentageFulfillmentType(String aValue, String bValue, int totalInstances){
		super();
		this.aValue = aValue;
		this.bValue = bValue;
		this.totalInstances = totalInstances;
	}

	/**
	 * Devuelve el atributo aValue. Primer valor de la comparación.
	 * 
	 * @return the aValue
	 */
	public String getaValue() {
		return aValue;
	}

	/**
	 * Da valor al atributo aValue. Primer valor de la comparación.
	 * 
	 * @param aValue 
	 */
	public void setaValue(String aValue) {
		this.aValue = aValue;
	}

	/**
	 *  Devuelve el atributo bValue. Segundo valor de la comparación.
	 * 
	 * @return the bValue
	 */
	public String getbValue() {
		return bValue;
	}

	/**
	 * Da valor al atributo bValue. Segundo valor de la comparación.
	 * 
	 * @param bValue the bValue to set
	 */
	public void setbValue(String bValue) {
		this.bValue = bValue;
	}

	/**
	 * Devuelve el atributo totalInstances. Total de instancias a considerar.
	 * 
	 * @return the totalInstances
	 */
	public int getTotalInstances() {
		return totalInstances;
	}

	/**
	 * Da valor al atributo totalInstances. Total de instancias a considerar.
	 * 
	 * @param totalInstances
	 */
	public void setTotalInstances(int totalInstances) {
		this.totalInstances = totalInstances;
	}
	
	
}
