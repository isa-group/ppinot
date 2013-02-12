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
	
	// nombre del dataobject de la propiedad
	private String dataobject;
	
	// id del dataobject de la propiedad
	private String dataobjectId;
	
	/**
	 * Constructor de la clase
	 */
	public DataContentSelection() {
		
		super();
    	this.setSelection("");
    	this.setDataobject("");
    	this.setDataobjectId("");
	}
	
	/**
	 * Constructor de la clase
	 * 
	 * @param selection Id de la propiedad que se agrupa
	 * @param dataobject Id del dataobject
	 */
	public DataContentSelection(String selection, String dataobject, String dataobjectId) {
		
		super();
    	this.setSelection(selection);
    	this.setDataobject(dataobject);
    	this.setDataobjectId(dataobjectId);
	}
	
	/**
	 * Devuelve el atributo dataobject
	 * Nombre del dataobject
	 * 
	 * @return Valor de la propiedad
	 */
	public String getDataobject() {
		return dataobject;
	}
	
	/**
	 * Da valor al atributo dataobject
	 * Nombre del dataobject
	 * 
	 * @param dataobject Valor del atributo
	 */
	public void setDataobject(String dataobject) {
		this.dataobject = dataobject;
	}
	
	/**
	 * Devuelve el atributo dataobject
	 * Id del dataobject
	 * 
	 * @return Valor de la propiedad
	 */
	public String getDataobjectId() {
		return dataobjectId;
	}
	
	/**
	 * Da valor al atributo dataobjectId
	 * Id del dataobject
	 * 
	 * @param dataobject Valor del atributo
	 */
	public void setDataobjectId(String dataobjectId) {
		this.dataobjectId = dataobjectId;
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
