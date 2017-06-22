package es.us.isa.ppinot.model.connector;

public class ParameterConnector  extends CompositeConnector{

	private String value;
	
	public ParameterConnector(){
		super();
	}
	
	public ParameterConnector(String id, String name, String description){
		super(id, name, description);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
	public boolean valid(){
		return super.valid() &&  this.getValue()!=null && !this.getValue().contentEquals("");
	}
	
}
