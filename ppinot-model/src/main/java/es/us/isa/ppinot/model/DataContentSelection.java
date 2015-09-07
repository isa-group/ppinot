package es.us.isa.ppinot.model;

/**
 * Clase con la informacion para agrupar los resultados de una medida agregada
 * 
 * @author Edelia
 *
 */
public class DataContentSelection implements Cloneable{

	// Propiedad que se agrupa
	private String selection;

	// id del dataobject de la propiedad
	private String dataobjectId;
	
	/**
	 * Constructor de la clase
	 */
	public DataContentSelection() {
		
		super();
    	this.setSelection("");
    	this.setDataobjectId("");
	}
	
	/**
	 * Constructor de la clase
	 *
     * @param selection Id de la propiedad que se agrupa
     * @param dataobjectId Id del dataobject
     */
	public DataContentSelection(String selection, String dataobjectId) {
		
		super();
    	this.setSelection(selection);
    	this.setDataobjectId(dataobjectId);
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
	 * @param dataobjectId Valor del atributo
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
	 * @param selection Valor del atributo
	 */
	public void setSelection(String selection) {
		this.selection = selection;
	}
	
	public DataContentSelection clone(){
		
		final DataContentSelection clone;
		
		try{
			//clone = this.clone();
			//clone = (DataContentSelection)this.clone();
			clone = (DataContentSelection) super.clone();
			/*clone.dataobjectId = new String(this.dataobjectId);
			clone.selection = new String(this.selection);*/
			return clone;
		
		}catch(Exception e){
			System.out.println("\t!>>>> Excepción en DataContentSelection - clone()\n~~~" + e.getMessage());
			return null;
		}
		
		
	}
	

}
