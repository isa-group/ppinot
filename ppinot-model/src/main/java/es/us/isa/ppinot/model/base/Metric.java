package es.us.isa.ppinot.model.base;

//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.lang.Object;
import java.util.HashMap;
import java.util.Map;

//import org.codehaus.jackson.annotate.JsonTypeInfo;


import org.codehaus.jackson.annotate.JsonTypeInfo;

//import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
//import es.us.isa.ppinot.model.composite.ValidateConnection;
//import es.us.isa.ppinot.model.condition.DataPropertyCondition;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
include=JsonTypeInfo.As.PROPERTY, property="kind")
public class Metric extends BaseMeasure implements Cloneable{
	
	protected Map<String, MeasureDefinition> usedMeasureIdMap;
	
	public Metric(){
		super();
	}
		
	public Metric(String id, String name, String description, String scale, String unitOfMeasure){
		super(id, name, description, scale, unitOfMeasure);
	}
	
	//Copiada de la DerivedMeasure.java
    public Map<String, MeasureDefinition> getUsedMeasureMap() {

    	if (this.usedMeasureIdMap==null)
    		this.usedMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedMeasureIdMap;
    }
	
  //Copiada de la DerivedMeasure.java
    public void addUsedMeasure(String variable, MeasureDefinition measure) {
    	this.getUsedMeasureMap().put((variable.contentEquals(""))?measure.getId():variable, measure);
    }
    
  //Copiada de la DerivedMeasure.java
    public MeasureDefinition getUsedMeasureId(String id) { //Quitar el Map
    	return this.getUsedMeasureMap().get(id);
    }
    
    
	/*
	 * Los dos m�todos los quito porque no est�n en la versi�n original del Derived Measure
	 * 
	 *
    public Map<String, MeasureDefinition> getUsedMeasureIdMap() {
		return usedMeasureIdMap;
	}
	
	public void setUsedMeasureIdMap(Map<String, MeasureDefinition> usedMeasureIdMap) {
		this.usedMeasureIdMap = usedMeasureIdMap;
	}
	*/
	public boolean valid(){
		
		//COPIAR EL M�TODO DE CollapsedCompositeMeasure
		return true;
	}
	
	//COPIAR EL M�TODO: public ValidateConnection ValidateConnections(){
	//COPIAR EL M�TODO: public void PrintConnections(){
	//COPIAR LOS M�TODOS: addConnection
	
}