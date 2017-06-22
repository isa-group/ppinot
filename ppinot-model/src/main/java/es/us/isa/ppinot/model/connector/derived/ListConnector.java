package es.us.isa.ppinot.model.connector.derived;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.derived.ListMeasure;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY, property="kind")
public class ListConnector extends DerivedConnector implements Cloneable{

	private int numberOfElements;

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	
	public ListConnector(){
		super();
	}
	
	public ListConnector(String id, String name, String description){
		super(id, name, description);
	}
	
	public ListConnector clone(){
    	
    	final ListConnector clone;
    	
    	try{
    		clone = (ListConnector) super.clone();
    	}catch(Exception ex){
    		throw new RuntimeException( "\t!>>>> Exception in ListConnector - clone()" );
    	}
    	return clone;
    }
	
	public boolean valid(){
		
		boolean response = true;
		
		int iMeasureElements = this.getUsedCcMeasureMap().size();
		int iExternalElements = this.getUsedCcExternalValueMap().size();
		
		if(iMeasureElements <= 0 && iExternalElements <= 0)
			response = false;
			
		if(iMeasureElements >0 && iExternalElements >0)
			response = false;
					
		//At least one measure connected
		if(iMeasureElements > 0){
						
			//All measures must be of the same type
			
			Object[] variable = new Object[iMeasureElements];
			variable = this.getUsedCcMeasureMap().values().toArray();

			boolean sameType = true;
			
			for(int i=0; i < iMeasureElements-1; i++){
				 if(!variable[i].toString().substring(variable[i].toString().lastIndexOf(".")+1, variable[i].toString().indexOf("@")).equals(variable[i+1].toString().substring(variable[i+1].toString().lastIndexOf(".")+1, variable[i+1].toString().indexOf("@")))){
					 sameType = false;
				 }
			}
			
			if(sameType == false){
				response = false;
			}
		}
		
		if(iExternalElements > 0){
				
			Object[] variable, names = new Object[iExternalElements];
			variable = this.getUsedCcExternalValueMap().values().toArray();
			names = this.getUsedCcExternalValueMap().keySet().toArray();
			String emptyString = "";
			
			for(int i=0; i < iExternalElements; i++){
				if(variable[i].toString().contentEquals("")){
					emptyString =  names[i] + " - " + emptyString;
				}
			}
			
			if(!emptyString.contentEquals(""))
				response = false;
								
		}
		return response;		
	}
}
