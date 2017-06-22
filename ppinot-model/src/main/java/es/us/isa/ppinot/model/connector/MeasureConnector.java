package es.us.isa.ppinot.model.connector;

public class MeasureConnector extends CompositeConnector{

	public MeasureConnector(){
		super();
	}
	
	public MeasureConnector(String id, String name, String description){
		super(id, name, description);
	}
	
	public boolean valid(){
		return super.valid();
	}
	
}
