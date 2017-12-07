package es.us.isa.ppinot.model;

/**
 * Class that selects a sepecific content from a data object.
 * 
 * @author Edelia
 *
 */
public class DataContentSelection {

	// Selection expression
	private String selection;

	// id of the data object with the selection expression
	private String dataobjectId;
	
	public DataContentSelection() {
		
		super();
    	this.setSelection("");
    	this.setDataobjectId("");
	}
	
	public DataContentSelection(String selection, String dataobjectId) {
		
		super();
    	this.setSelection(selection);
    	this.setDataobjectId(dataobjectId);
	}
	

	public String getDataobjectId() {
		return dataobjectId;
	}
	
	public void setDataobjectId(String dataobjectId) {
		this.dataobjectId = dataobjectId;
	}
	
	public String getSelection() {
		return selection;
	}
	
	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getId() {
		String id = selection;

		if (!dataobjectId.isEmpty()) {
			id = dataobjectId + "-" + selection;
		}

		return id;
	}

}
