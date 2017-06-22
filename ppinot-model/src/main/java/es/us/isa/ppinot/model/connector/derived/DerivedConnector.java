package es.us.isa.ppinot.model.connector.derived;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.connector.MeasureConnector;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DerivedConnector extends MeasureConnector{
	
	
	//Attribute - String function should be defined on ExpandedMeasure
	
	protected Map<String, MeasureDefinition> usedCcMeasureIdMap;
    
    protected Map<String, String> usedCcExternalValueMap;
    
	public DerivedConnector(){
		super();
	}
	
	public DerivedConnector(String id, String name, String description){
		super(id, name, description);
	}
	
	public Map<String, MeasureDefinition> getUsedCcMeasureMap() {

    	if (this.usedCcMeasureIdMap==null)
    		this.usedCcMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedCcMeasureIdMap;
    }
    
    public Map<String, String> getUsedCcExternalValueMap(){
    	if (this.usedCcExternalValueMap==null)
    		this.usedCcExternalValueMap = new HashMap<String, String>();
    	return this.usedCcExternalValueMap;
    }
    
    public void addUsedCcMeasure(String variable, MeasureDefinition ccMeasure) {
    	
    	this.getUsedCcMeasureMap().put((variable.contentEquals(""))?ccMeasure.getId():variable, ccMeasure);
    }
    
    public void addUsedCcExternalValue(String variable, String ccExternalValue){
    	this.getUsedCcExternalValueMap().put(variable, ccExternalValue);    	
    }
    
    public MeasureDefinition getUsedCcMeasureId(String id) {
    	
    	return this.getUsedCcMeasureMap().get(id);
    }
    
    public String getUsedCcExternalValue(String id){
    	return this.getUsedCcExternalValueMap().get(id);
    }
    
    public boolean valid() {
		
    	Boolean cond = true, bsuper = super.valid();
		
    	Object[] classes = new Object[this.getUsedCcMeasureMap().size()];
		classes = this.getUsedCcMeasureMap().values().toArray();
		  
		if(classes != null && classes.length > 0){
			
			for(int i=0; i<classes.length; i++){
				
				if(cond == false )
					break;
				
				MeasureDefinition md = (MeasureDefinition)classes[i];
				if(md.getId() == null || md.getId().contentEquals("") || md.getName() == null || md.getName().contentEquals(""))
					cond = false;
			}
		}else{
			cond = false;
		}
		
		return bsuper && cond;
	}
}
