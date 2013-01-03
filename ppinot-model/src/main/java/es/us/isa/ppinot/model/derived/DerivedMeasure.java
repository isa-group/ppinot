package es.us.isa.ppinot.model.derived;

import java.util.HashMap;
import java.util.Map;

import es.us.isa.ppinot.model.MeasureDefinition;

/**
 * Clase con la información de un PPI del tipo AggregatedMeasure
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class DerivedMeasure extends MeasureDefinition {
    
    /**
     * Función de agregación que se aplica
     */
    private String function;
    
    /**
     * La medida a partir de la cual se calcula la medida derivada
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
     * @param description Descripció de la medida
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
     * Función de agregación que se aplica
     * 
     * @return Valor del atributo
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * Da valor al atributo func:
     * Función de agregación que se aplica
     * 
     * @param value Valor del atributo
     */
    public void setFunction(String value) {
        this.function = value;
    }
     
    /**
     * Devuelve el atributo usedMeasureMap:
     * La medida que se agrega
     * 
     * @return La medida que se agrega
     */
    public Map<String, MeasureDefinition> getUsedMeasureMap() {

    	if (this.usedMeasureIdMap==null)
    		this.usedMeasureIdMap = new HashMap<String, MeasureDefinition>();
    	return this.usedMeasureIdMap;
    }
    
    public void addUsedMeasure(MeasureDefinition measure) {
    	
    	this.getUsedMeasureMap().put(measure.getId(), measure);
    }
    
    public MeasureDefinition getUsedMeasureId(String id) {
    	
    	return this.getUsedMeasureMap().get(id);
    }
}
