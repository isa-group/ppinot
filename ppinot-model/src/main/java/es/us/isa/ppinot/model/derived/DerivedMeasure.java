package es.us.isa.ppinot.model.derived;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import es.us.isa.ppinot.model.MeasureDefinition;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Clase con la información de las medidas derivadas
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include=JsonTypeInfo.As.PROPERTY, property="kind")
public class DerivedMeasure extends MeasureDefinition {
    
    /**
     * Función que se aplica
     */
    private String function;
    
    /**
     * Medidas a partir de las cuales se calcula la medida derivada
     */
    protected Map<String, MeasureDefinition> usedMeasureIdMap;

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
     * @param description Descripción de la medida
     * @param scale Escala de la medida
     * @param measureUnit Unidad de medida
     * @param func Función de la medida
     */
    public DerivedMeasure(String id, String name, String description, String scale, String unitOfMeasure, 
    		String function) {
    	super(id, name, description, scale, unitOfMeasure);
    	this.setFunction(function);
	}

    /**
     * Devuelve el atributo func:
     * Función que se aplica
     * 
     * @return Valor del atributo
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * Da valor al atributo func:
     * Función que se aplica
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
    
    /**
     * Adiciona una medida que se utiliza para calcular la medida derivada
     * 
     * @param variable Nombre de variable asociada a la medida
     * @param measure Medida utilizada para calcular la medida derivada
     */
    public void addUsedMeasure(String variable, MeasureDefinition measure) {
    	
    	this.getUsedMeasureMap().put((variable.contentEquals(""))?measure.getId():variable, measure);
    }
    
    /**
     * Devuelve una medida utilizada para el cáculo a partir de su id
     * 
     * @param id Id de medida
     * @return Objeto de la medida
     */
    public MeasureDefinition getUsedMeasureId(String id) {
    	
    	return this.getUsedMeasureMap().get(id);
    }
	
	/**
	 * Indica si la medida puede ser calculada
	 * 
	 * @return 
	 */
	public Boolean getCond() {
		
		Boolean cond = super.getCond();
		
		Iterator<Entry<String, MeasureDefinition>> itInst = this.getUsedMeasureMap().entrySet().iterator();
	    while (cond && itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
	        MeasureDefinition value = pairs.getValue();

	        cond = cond && value.getCond();
	    }
	    
		return cond;
	}
}
