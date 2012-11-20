package es.us.isa.isabpm.ppinot.model;

import java.util.ArrayList;
import java.util.List;

public class PPI {

	/**
	 * Propiedades con las que se define el PPI
	 */
	private String id;
	private String name;
	private String description;
	private String goals;
	private String responsible;
	private String informed;
	private String comments;
	private Target target;
	private Scope scope;

	/**
     * Medidas del PPI
     */
    private MeasureDefinition measuredBy;

	/**
     * Los resultados de evaluar el PPI para cada una de las medidas
     */
		// Valores calculados de las medidas
	private List<String> valueString;
		// Porcentaje en que se satisface el PPI
	private List<Double> success;
		// Porcentaje normalizado para promediar con otros PPI
	private List<Double> normalized;
    
    public PPI() {
    	super();

    	this.setId("");
    	this.setName("");
    	this.setGoals("");
    	this.setResponsible("");
    	this.setInformed("");
    	this.setComments("");
    	
    	this.setTarget( null );
    	
    	this.setScope( null );
    	
    	this.iniPPI();
    }
    
    public PPI(
    		String id, String name, String description, String goals, String responsible, String informed, String comments,
    		Target target, Scope scope) {
    	
    	super();

    	this.setId(id);
    	this.setName(name);
    	this.setDescription(description);
    	this.setGoals(goals);
    	this.setResponsible(responsible);
    	this.setInformed(informed);
    	this.setComments(comments);
    	
    	this.setTarget( target );
    	
    	this.setScope( scope );

    	this.iniPPI();
    }

    private void iniPPI() {
    	
       	this.measuredBy = null;
       	this.valueString = new ArrayList<String>();
    	this.success = new ArrayList<Double>();
    	this.normalized = new ArrayList<Double>();
    }
    
	/**
     * Devuelve el atributo id:
     * Id de la medida
     * 
     * @return Valor del atributo
     */
    public String getId() {
        return this.id;
    }

    /**
     * Da valor al atributo id:
     * Nombre de la medida
     * 
     * @param value Valor del atributo
     */
    public void setId(String value) {
    	if (value==null)
    		this.id = "";
    	else
    		this.id = value;
    }

	/**
     * Devuelve el atributo name:
     * Nombre de la medida
     * 
     * @return Valor del atributo
     */
    public String getName() {
		return this.name;
	}

    /**
     * Da valor al atributo name:
     * Nombre de la medida
     * 
     * @param name Valor del atributo
     */
	public void setName(String name) {
    	if (name==null)
    		this.name = "";
    	else
    		this.name = name;
	}

	/**
     * Devuelve el atributo description:
     * Descripción de la medida
     * 
     * @return Valor del atributo
     */
	public String getDescription() {
		return this.description;
	}

    /**
     * Da valor al atributo description:
     * Descripción de la medida
     * 
     * @param name Valor del atributo
     */
	public void setDescription(String description) {
		this.description = description;
	}
    
	public String getGoals() {
		return this.goals;
	}
	
	public void setGoals(String goals) {
		this.goals = goals;
	}
	
	public String getResponsible() {
		return this.responsible;
	}
	
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	
	public String getInformed() {
		return this.informed;
	}
	
	public void setInformed(String informed) {
		this.informed = informed;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
    
    public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}
 
    /**
     * Manejar las medidas y sus valores 
     * 
     */
	
    public MeasureDefinition getMeasuredBy() {
		return measuredBy;
	}

    public void setMeasuredBy(MeasureDefinition measure) {
		
		this.measuredBy = measure;
	}
	
	public List<String> getValueString() {
		return valueString;
	}

	public void setValueString(List<String> valueString) {
		this.valueString = valueString;
	}

	public List<Double> getSuccess() {
		return success;
	}

	public void setSuccess(List<Double> success) {
		this.success = success;
	}

	public List<Double> getNormalized() {
		return normalized;
	}

	public void setNormalized(List<Double> normalized) {
		this.normalized = normalized;
	}

	public void evaluate() {
		
		
		for (String str : this.getValueString()) {
			
			Double success = -1.0;
			Double normalized = -1.0;
			try {
				
				Double value;
				if (str.contentEquals("true"))
					value = 1.0;
				else
				if (str.contentEquals("false"))
					value = 0.0;
				else
					value = Double.valueOf(str);
				
				if (this.getTarget().getRefMin()!=null && this.getTarget().getRefMax()!=null) {
					
					// si el valor del indicador debe estar en un rango
					if (this.getTarget().getRefMin()<=value && value<=this.getTarget().getRefMax()) {
						success = 1.0;
						normalized = success;
					}
					else {
						
						if (value<this.getTarget().getRefMin()) {
						
							success = value / this.getTarget().getRefMin();
							normalized = success;
						}
						else {
							
							success = value / this.getTarget().getRefMax();
							normalized = this.getTarget().getRefMax() / value;
						}
					}
				}
				else
				if (this.getTarget().getRefMin()!=null) {
					
					// si el valor del indicador debe ser mayor o igual a un valor dado
					success = value / this.getTarget().getRefMin();
					normalized = success;
				}
				else
				if (this.getTarget().getRefMax()!=null) {
					
					// si el valor del indicador debe ser menor o igual a un valor dado
					success = value / this.getTarget().getRefMax();
					if (value==0)
						normalized = 0.0;
					else
						normalized = this.getTarget().getRefMax() / value;
				}
					
			} catch (Exception e) {
				
			}
			
			this.getSuccess().add(success);
			this.getNormalized().add(normalized);
		}
	}

	
	public Boolean toMark(int i) {
		
		Boolean mark = false;

		if ( this.getSuccess().get(i)!=null ) {
			
			if (this.getTarget().getRefMin()!=null && this.getTarget().getRefMax()!=null) {
				
				// si el valor del indicador debe estar en un rango
				mark = this.getSuccess().get(i)<1 || this.getSuccess().get(i)>1;
			}
			else
			if (this.getTarget().getRefMin()!=null) {
				
				// si el valor del indicador debe ser mayor o igual a un valor dado
				mark = this.getSuccess().get(i)<1;
			}
			else
			if (this.getTarget().getRefMax()!=null) {
				
				// si el valor del indicador debe ser menor o igual a un valor dado
				mark = this.getSuccess().get(i)>1;
			}
				
		} 
		
		return mark;
	}
	
	public List<Boolean> getToMark() {
		
		List<Boolean> list = new ArrayList<Boolean>();
		for (int i=0; i<this.getSuccess().size(); i++) {
			
			list.add(this.toMark(i));
		}
		return list;
	}

	public Double averageNormalize() {
		
		Double norm = 0.0;
		int cant = 0;
		for (Double value : this.getNormalized())
			if (value>=0) {
				norm += value;
				cant ++;
			}
		if (cant>0)
			return norm / cant;
		else
			return null;
	}
	
}
