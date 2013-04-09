package es.us.isa.bpms.ppinot.historyreport;

import java.util.ArrayList;
import java.util.List;

public class StartEndMoment {

    // Indica si el valor de la medida puede ser calculado y mostrado
	private Boolean cond = false;
	
	// Valor de la medida
	private String valueString = "";
	
	public StartEndMoment() {
		
		super();
		this.setCond(false);
		this.iniValue();
	}
	
	public void iniValue() {
		
		this.valueString = "";
	}
	
	/**
	 * Devuelve el valor del atributo cond:
	 * Indica si el valor de la medida puede ser calculado y mostrado
	 * 
	 * @return Valor del atributo
	 */
	public Boolean getCond() {
		return cond;
	}

	/**
	 * Da valor al atributo cond:
	 * Indica si el valor de la medida puede ser calculado y mostrado
	 * 
	 * @param cond Valor del atributo
	 */
	public void setCond(Boolean cond) {
		this.cond = cond;
	}
	
	/**
	 * Devuelve el valor del atributo valueString:
	 * Valor calculado de la medida
	 * 
	 * @return Valor del atributo
	 */
	public List<String> getValueString() {

		List<String> list = new ArrayList<String>();
		list.add(valueString);
		return list;
	}
	
	/**
	 * Da valor al atributo valueString:
	 * Valor calculado de la medida
	 * 
	 * @param valueString Valor del atributo
	 */
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public List<Double> getSuccess() {
		
		List<Double> list = new ArrayList<Double>();
		list.add(1.0);
		return list;
	}

	public List<Boolean> getToMark() {
		
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(true);
		return list;
	}
}
