package es.us.isa.ppinot.model.connector.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
	include = JsonTypeInfo.As.PROPERTY, property = "kind")
public class DerivedMultiInstanceConnector extends DerivedConnector implements Cloneable{

	public DerivedMultiInstanceConnector(){
		super();
	}
	public DerivedMultiInstanceConnector(String id, String name, String description){
		super(id, name, description);
	}
	
	public DerivedMultiInstanceConnector clone(){
		final DerivedMultiInstanceConnector clone;
		try{
			clone = (DerivedMultiInstanceConnector) super.clone();
		}catch(Exception e){
			throw new RuntimeException( "\t!>>>> Excepción en DerivedMultiInstanceConnector - clone()" );
		}
		
		return clone;
	}
	
}
