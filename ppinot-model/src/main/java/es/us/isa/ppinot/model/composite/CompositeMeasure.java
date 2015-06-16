package es.us.isa.ppinot.model.composite;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.composite.type.PercentageFulfillmentType;
import es.us.isa.ppinot.model.composite.type.PercentagePerformanceType;

/**
 * @author BEstrada
 *
 */

//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
//include=JsonTypeInfo.As.PROPERTY, property="kind")

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PercentageFulfillmentType.class, name = "PercentageFulfillmentType"),
	@JsonSubTypes.Type(value = PercentagePerformanceType.class, name = "PercentagePerformanceType"),
})

public class CompositeMeasure{
	/**
	 * Nombre de la medida compuesta
	 */
	private String name;
	private CompositeTypeDefinition TypeDefinition;
	/**
     * Medidas a partir de las cuales se calcula la medida compuesta
     */
    protected Map<String, MeasureDefinition> usedMeasureIdMap;
    
    
	//private String type; //
	//private enum CompositeType {PERCENTAGE, RATIO, BOOLEANVAL};
	
	/**
     * Medidas a partir de las cuales se calcula la medida derivada
     */
    //protected Map<String, MeasureDefinition> usedMeasureIdMap;
    protected Map<String, CompositeTypeDefinition> usedCompositeTypeIdMap;
    
    /**
     * Constructor de la clase CompositeMeasure
     * 
     */
    public CompositeMeasure(){
    	this.name = "";
    }
    
    public CompositeMeasure(String name){
    	this.name = name;
    }
    
    public CompositeMeasure(CompositeType type){
    	
    }
    
    /**
     * Devuelve el atributo name:
     * Nombre de la medida compuesta
     * 
     * @return Valor del atributo
     */
    public String getName() {
		return name;
	}

    /**
     * Da valor al atributo name:
     * Nombre de la medida compuesta
     * 
     * @param value Valor del atributo
     */
	public void setName(String value) {
		this.name = value;
	}
		
	/**
     * Devuelve el atributo type:
     * Tipo de dato según la tabla de clasificación de medidas compuestas
     * 
     * @return Valor del atributo
     */
	/*public String getType() {
		return type;
	}*/

	/**
     * Da valor al atributo type:
     * Tipo de dato según la tabla de clasificación de medidas compuestas
     * 
     * @param value Valor del atributo
     */
	/*public void setType(String type) {
		this.type = type;
	}*/
	
	/*public boolean isValidType(CompositeType type){
		switch(type){
		case PERCENTAGE:
			return true;
		case RATIO:
			return true;
		case BOOLEANVAL:
			return true;
		default:
			return false;
		}
	}*/
	
	public Map<String, CompositeTypeDefinition> getUsedCompositeMeasureMap() {

    	if (this.usedCompositeTypeIdMap==null)
    		this.usedCompositeTypeIdMap = new HashMap<String, CompositeTypeDefinition>();
    	return this.usedCompositeTypeIdMap;
    }
	
	//addUsedMeasure
	public void createByType(String name, CompositeTypeDefinition compositeTypeDef) {
    	this.setName(name);
    	this.getUsedCompositeMeasureMap().put((name.contentEquals(""))?compositeTypeDef.getName():name, compositeTypeDef);
    }

	/**
	 * @return the typeDefinition
	 */
	public CompositeTypeDefinition getTypeDefinition() {
		return TypeDefinition;
	}

	/**
	 * @param typeDefinition the typeDefinition to set
	 */
	public void setTypeDefinition(CompositeTypeDefinition typeDefinition) {
		this.TypeDefinition = typeDefinition;
	}
	
	
	//--LOS MÉTODOS ASOCIADOS AL MAPEO ORIGINAL, PARA HACER ALGO SIMILAR A LO QUE SE HACE CON LAS MEDIDAS DERIVADAS
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
		
	
}
