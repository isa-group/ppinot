package es.us.isa.ppinot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de los PPI
 * 
 * @author Edelia
 *
 */
public class PPI {

	// Propiedades con las que se define el PPI
	private String id;
	private String name;
	private String description;
	private String goals;
	private String responsible;
	private String informed;
	private String comments;
	private Target target;
	private Scope scope;

	// Medida asociada al PPI
    private MeasureDefinition measuredBy;

	// Los resultados de evaluar el PPI para cada una de las medidas
		// Valores calculados de las medidas
	private List<String> valueString;
		// Porcentaje en que se satisface el PPI
	private List<Double> success;
		// Porcentaje normalizado para promediar con otros PPI
	private List<Double> normalized;
    
    /**
     * Constructor de la clase
     * 
     */
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
    	
    	this.iniValues();
    }
    
    /**
     * Constructor de la clase
     * 
     * @param id Id del PPI
     * @param name Nombre
     * @param description Descripcion
     * @param goals 
     * @param responsible
     * @param informed
     * @param comments Comentarios
     * @param target Objeto Target que indica el rango de valores que en que se espera que este la medida asociada
     * @param scope Objeto Scope que indica el periodo de tiempo en el cual se calcula el PPI
     */
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
    	
    	this.iniValues();
    }
	
	public void iniValues() {
		
		this.valueString = new ArrayList<String>();
	}

    /**
     * Inicializaciones
     */
	private void iniPPI() {
    	
       	this.measuredBy = null;
       	this.valueString = new ArrayList<String>();
    	this.success = new ArrayList<Double>();
    	this.normalized = new ArrayList<Double>();
    }
    
	/**
     * Devuelve el atributo id:
     * Id del PPI
     * 
     * @return Valor del atributo
     */
    public String getId() {
        return this.id;
    }

    /**
     * Da valor al atributo id:
     * Id del PPI
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
     * Nombre del PPI
     * 
     * @return Valor del atributo
     */
    public String getName() {
		return this.name;
	}

    /**
     * Da valor al atributo name:
     * Nombre del PPI
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
     * Descripcion del PPI
     * 
     * @return Valor del atributo
     */
	public String getDescription() {
		return this.description;
	}

    /**
     * Da valor al atributo description:
     * Descripcion del PPI
     * 
     * @param name Valor del atributo
     */
	public void setDescription(String description) {
		this.description = description;
	}
    
	/**
     * Devuelve el atributo goals:
     * 
     * 
     * @return Valor del atributo
     */
	public String getGoals() {
		return this.goals;
	}
	
    /**
     * Da valor al atributo goals:
     * 
     * 
     * @param name Valor del atributo
     */
	public void setGoals(String goals) {
		this.goals = goals;
	}
	
	/**
     * Devuelve el atributo responsible:
     * 
     * 
     * @return Valor del atributo
     */
	public String getResponsible() {
		return this.responsible;
	}
	
    /**
     * Da valor al atributo responsible:
     * 
     * 
     * @param name Valor del atributo
     */
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	
	/**
     * Devuelve el atributo informed:
     * 
     * 
     * @return Valor del atributo
     */
	public String getInformed() {
		return this.informed;
	}
	
    /**
     * Da valor al atributo informed:
     * 
     * 
     * @param name Valor del atributo
     */
	public void setInformed(String informed) {
		this.informed = informed;
	}
	
	/**
     * Devuelve el atributo comments:
     * Comentarios
     * 
     * @return Valor del atributo
     */
	public String getComments() {
		return this.comments;
	}
	
    /**
     * Da valor al atributo comments:
     * Comentarios
     * 
     * @param name Valor del atributo
     */
	public void setComments(String comments) {
		this.comments = comments;
	}
    
	/**
     * Devuelve el atributo target:
     * Objeto Target que indica el rango de valores que en que se espera que este la medida asociada
     * 
     * @return Valor del atributo
     */
    public Target getTarget() {
		return target;
	}

    /**
     * Da valor al atributo target:
     * Objeto Target que indica el rango de valores que en que se espera que este la medida asociada
     * 
     * @param name Valor del atributo
     */
	public void setTarget(Target target) {
		this.target = target;
	}

	/**
     * Devuelve el atributo target:
     * Objeto Scope que indica el periodo de tiempo en el cual se calcula el PPI
     * 
     * @return Valor del atributo
     */
	public Scope getScope() {
		return scope;
	}

    /**
     * Da valor al atributo scope:
     * Objeto Scope que indica el periodo de tiempo en el cual se calcula el PPI
     * 
     * @param name Valor del atributo
     */
	public void setScope(Scope scope) {
		this.scope = scope;
	}
 
	/**
     * Devuelve el atributo measureBy:
     * Medida asociada con el PPI
     * 
     * @return Valor del atributo
     */
    public MeasureDefinition getMeasuredBy() {
		return measuredBy;
	}

    /**
     * Da valor al atributo measuredBy:
     * Medida asociada con el PPI
     * 
     * @param name Valor del atributo
     */
    public void setMeasuredBy(MeasureDefinition measure) {
		
		this.measuredBy = measure;
	}
	
	/**
     * Devuelve el atributo valueString:
     * Lista de String con los valores del PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @return Valor del atributo
     */
	public List<String> getValueString() {
		return valueString;
	}

    /**
     * Da valor al atributo valueString:
     * Lista de String con los valores del PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @param name Valor del atributo
     */
	public void setValueString(List<String> valueString) {
		this.valueString = valueString;
	}

	/**
     * Devuelve el atributo success:
     * Lista de Double que indican si se satisface PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @return Valor del atributo
     */
	public List<Double> getSuccess() {
		return success;
	}

    /**
     * Da valor al atributo success:
     * Lista de Double que indican si se satisface PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @param name Valor del atributo
     */
	public void setSuccess(List<Double> success) {
		this.success = success;
	}

	/**
     * Devuelve el atributo normalized:
     * Lista de Double con los valores del PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @return Valor del atributo
     */
	public List<Double> getNormalized() {
		return normalized;
	}

    /**
     * Da valor al atributo normalized:
     * Lista de Double con los valores del PPI para cada uno de los periodos de tiempo evaluados
     * 
     * @param name Valor del atributo
     */
	public void setNormalized(List<Double> normalized) {
		this.normalized = normalized;
	}

	/**
	 * Evalua el PPI
	 */
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

	
	/**
	 * Indica si se debe marcar 
	 * 
	 * @param i
	 * @return
	 */
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
	
	/**
	 * 
	 * @return
	 */
	public List<Boolean> getToMark() {
		
		List<Boolean> list = new ArrayList<Boolean>();
		for (int i=0; i<this.getSuccess().size(); i++) {
			
			list.add(this.toMark(i));
		}
		return list;
	}

	/**
	 * 
	 * @return
	 */
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
	
	/**
	 * Indica si el valor del PPI puede ser calculado y mostrado
	 * 
	 * @return 
	 */
	public Boolean getCond() {
		
		return this.getMeasuredBy()!=null && this.getMeasuredBy().getCond();
	}
	
}
