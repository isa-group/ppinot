package es.us.isa.ppinot.model.connector.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DerivedSingleInstanceConnector extends DerivedConnector implements Cloneable{

	public DerivedSingleInstanceConnector(){
		super();
	}
	
	public DerivedSingleInstanceConnector(String id, String name, String description){
		super(id, name, description);
	}
	
	public DerivedSingleInstanceConnector clone(){
		final DerivedSingleInstanceConnector clone;
		try{
			clone = (DerivedSingleInstanceConnector) super.clone();
		}catch(Exception e){
			throw new RuntimeException( "\t!>>>> Excepción en DerivedSingleInstanceConnector - clone()" );
		}
		
		return clone;
	}
	
	public boolean valid() {
		return super.valid();
	}
	
}
