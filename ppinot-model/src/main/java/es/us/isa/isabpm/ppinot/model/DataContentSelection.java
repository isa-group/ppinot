package es.us.isa.isabpm.ppinot.model;

public class DataContentSelection {
	/**
	 * Propiedad que se agrupa
	 */
	private String selection;
	/**
	 * Dataobject de la propiedad
	 */
	private String dataobject;
	
	public DataContentSelection() {
		
		super();
    	this.setSelection("");
    	this.setDataobject("");
	}
	
	public DataContentSelection(String selection, String dataobject) {
		
		super();
    	this.setSelection(selection);
    	this.setDataobject(dataobject);
	}
	
	public String getDataobject() {
		return dataobject;
	}
	
	public void setDataobject(String dataobject) {
		this.dataobject = dataobject;
	}
	
	public String getSelection() {
		return selection;
	}
	
	public void setSelection(String selection) {
		this.selection = selection;
	}

}
