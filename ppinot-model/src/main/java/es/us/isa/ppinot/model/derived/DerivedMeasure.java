/************************************************************************/
/*** MODIFIED VERSION *************************************************/
/************************************************************************/

package es.us.isa.ppinot.model.derived;

import es.us.isa.ppinot.model.MeasureDefinition;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Clase con la informacion de las medidas derivadas
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DerivedMeasure extends MeasureDefinition {
    
    /**
     * Funcion que se aplica
     */
    private String function;

    private boolean includeEvidences = false;
    
    /**
     * Medidas a partir de las cuales se calcula la medida derivada
     */
    protected Map<String, MeasureDefinition> usedMeasureIdMap;
    
    protected Map<String, String> usedExternalValueMap;

    /**
     * Constructor de la clase
     */
    public DerivedMeasure() {
    	super();
    	this.setFunction("");
    }
    
    /**
     * Constructor de la clase
     * 
     * @param id Id de la medida
     * @param name Nombre de la medida
     * @param description Descripcion de la medida
     * @param scale Escala de la medida
     * @param unitOfMeasure Unidad de medida
     * @param function Funcion de la medida
     */
    public DerivedMeasure(String id, String name, String description, String scale, String unitOfMeasure, 
    		String function) {
    	super(id, name, description, scale, unitOfMeasure);
    	this.setFunction(function);
	}

    /**
     * Devuelve el atributo func:
     * Funcion que se aplica
     * 
     * @return Valor del atributo
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * Da valor al atributo func:
     * Funcion que se aplica
     * 
     * @param value Valor del atributo
     */
    public void setFunction(String value) {
        this.function = value;
    }
     
    /**
     * Devuelve el atributo usedMeasureMap:
     * Medidas a partir de las cuales se calcula la medida derivada
     * 
     * @return La medida que se agrega
     */
    public Map<String, MeasureDefinition> getUsedMeasureMap() {

    	if (this.usedMeasureIdMap==null)
    		this.usedMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedMeasureIdMap;
    }
    
    public Map<String, String> getUsedExternalValueMap(){
    	if (this.usedExternalValueMap==null)
    		this.usedExternalValueMap = new HashMap<String, String>();
    	return this.usedExternalValueMap;
    }
    
    /**
     * Adiciona una medida que se utiliza para calcular la medida derivada
     * 
     * @param variable Nombre de variable asociada a la medida
     * @param measure Medida utilizada para calcular la medida derivada
     */
    public void addUsedMeasure(String variable, MeasureDefinition measure) {
    	
    	this.getUsedMeasureMap().put((variable.contentEquals(""))?measure.getId():variable, measure);
    }
    
    public void addUsedExternalValue(String variable, String externalValue){
    	//Validar que "variable" no sea vacío ni null.
    	this.getUsedExternalValueMap().put(variable, externalValue);    	
    }
    
    /**
     * Devuelve una medida utilizada para el caculo a partir de su id
     * 
     * @param id Id de medida
     * @return Objeto de la medida
     */
    public MeasureDefinition getUsedMeasureId(String id) {
    	
    	return this.getUsedMeasureMap().get(id);
    }
    
    public String getUsedExternalValue(String id){
    	return this.getUsedExternalValueMap().get(id);
    }

    public boolean isIncludeEvidences() {
        return includeEvidences;
    }

    public void setIncludeEvidences(boolean includeEvidences) {
        this.includeEvidences = includeEvidences;
    }

    /**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return valid
	 */
    //Método original - 20170304
	/*public boolean valid() {
		
		Boolean cond = super.valid();
		
		Iterator<Entry<String, MeasureDefinition>> itInst = this.getUsedMeasureMap().entrySet().iterator();
	    while (cond && itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = itInst.next();
	        MeasureDefinition value = pairs.getValue();

	        cond = cond && value.valid();
	    }
	    
		return cond;
	}*/
    
    public boolean valid() {
		
		Boolean cond = super.valid();
		
		Iterator<Entry<String, MeasureDefinition>> itInst = this.getUsedMeasureMap().entrySet().iterator();
	    		
		while (cond && itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = itInst.next();
	        MeasureDefinition value = pairs.getValue();
	        cond = cond && value.valid();
	    }
		
		int total = 0, items = this.getUsedExternalValueMap().size(); 
		//iExternalElements = this.getUsedExternalValueMap().size();
		
		Object[] variable = new Object[items];
		variable = this.getUsedExternalValueMap().values().toArray();
						
		while(total < items){
			if(variable[total].toString().contentEquals(""))
				cond = false;
			
			total = total + 1;
		}
									    
		return cond;
	}
}
