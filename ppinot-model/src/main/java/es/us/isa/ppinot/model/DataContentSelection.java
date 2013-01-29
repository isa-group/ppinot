package es.us.isa.ppinot.model;

/**
 * Clase con la información para agrupar los resultados de una medida agregada
 * 
 * @author Edelia
 *
 */
public class DataContentSelection {

	// Propiedad que se agrupa
	private String selection;
	
	// Dataobject de la propiedad
	private String dataobject;
	
	/**
	 * Constructor de la clase
	 */
	public DataContentSelection() {
		
		super();
    	this.setSelection("");
    	this.setDataobject("");
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param selection Id de la propiedad que se agrupa
	 * @param dataobject Id del dataobject
	 */
	public DataContentSelection(String selection, String dataobject) {
		
		super();
    	this.setSelection(selection);
    	this.setDataobject(dataobject);
	}
	
	/**
	 * Devuelve el atributo dataobject
	 * 
	 * @return Valor de la propiedad
	 */
	public String getDataobject() {
		return dataobject;
	}
	
	/**
	 * Da valor al atributo dataobject
	 * Id del dataobject
	 * 
	 * @param dataobject Valor del atributo
	 */
	public void setDataobject(String dataobject) {
		this.dataobject = dataobject;
	}
	
	/**
	 * Devuelve el atributo selection
	 * 
	 * @return Valor de la propiedad
	 */
	public String getSelection() {
		return selection;
	}
	
	/**
	 * Da valor al atributo selection
	 * Id de la propiedad
	 * 
	 * @param dataobject Valor del atributo
	 */
	public void setSelection(String selection) {
		this.selection = selection;
	}

}
