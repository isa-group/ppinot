package es.us.isa.ppinot.model;

public class ExternalValue {

	public String id;
	
	public String name;
	
	public String value;
	
	public String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ExternalValue(){
		this.id = "";
		this.name = "";
		this.description = "";
	}
	
	public ExternalValue(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public boolean valid(){
		
		if(this.name != null && this.name.contentEquals("") && this.value != null && this.value.contentEquals(""))
			return true;
		else
			return false;
				
	}
	
	
}
