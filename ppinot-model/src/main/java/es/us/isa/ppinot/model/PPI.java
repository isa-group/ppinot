package es.us.isa.ppinot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de los PPI
 * 
 * @author Edelia
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PPI {

	// Propiedades con las que se define el PPI
	private String id;
	private String name;
	private List<String> goals;
	private String responsible;
	private List<String> informed;
	private String comments;
	private Target target;
	private ProcessInstanceFilter scope;

	// Medida asociada al PPI
    private MeasureDefinition measuredBy;

    /**
     * Constructor de la clase
     * 
     */
	public PPI() {
    	super();
        this.goals = new ArrayList<String>();
        this.informed = new ArrayList<String>();
    }
    
    /**
     * Constructor de la clase
     * 
     * @param id Id del PPI
     * @param name Nombre
     * @param goals
     * @param responsible
     * @param informed
     * @param comments Comentarios
     * @param target Objeto Target que indica el rango de valores que en que se espera que este la medida asociada
     * @param scope Objeto ProcessInstanceFilter que indica el periodo de tiempo en el cual se calcula el PPI
     */
	public PPI(
    		String id, String name, List<String> goals, String responsible, List<String> informed, String comments,
    		Target target, ProcessInstanceFilter scope) {
    	
    	super();

    	this.setId(id);
    	this.setName(name);
    	this.setGoals(goals);
    	this.setResponsible(responsible);
    	this.setInformed(informed);
    	this.setComments(comments);
    	
    	this.setTarget(target);
    	
    	this.setScope(scope);
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
     * Devuelve el atributo goals:
     * 
     * 
     * @return Valor del atributo
     */
	public List<String> getGoals() {
		return this.goals;
	}
	
    /**
     * Da valor al atributo goals:
     * 
     * 
     * @param goals Valor del atributo
     */
	public void setGoals(List<String> goals) {
		this.goals = new ArrayList<String>(goals);
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
     * @param responsible Valor del atributo
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
	public List<String> getInformed() {
		return this.informed;
	}
	
    /**
     * Da valor al atributo informed:
     * 
     * 
     * @param informed Valor del atributo
     */
	public void setInformed(List<String> informed) {
		this.informed = new ArrayList<String>(informed);
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
     * @param comments Valor del atributo
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
     * @param target Valor del atributo
     */
	public void setTarget(Target target) {
		this.target = target;
	}

	/**
     * Devuelve el atributo target:
     * Objeto ProcessInstanceFilter que indica el periodo de tiempo en el cual se calcula el PPI
     * 
     * @return Valor del atributo
     */
	public ProcessInstanceFilter getScope() {
		return scope;
	}

    /**
     * Da valor al atributo scope:
     * Objeto ProcessInstanceFilter que indica el periodo de tiempo en el cual se calcula el PPI
     * 
     * @param scope Valor del atributo
     */
	public void setScope(ProcessInstanceFilter scope) {
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
     * @param measure Valor del atributo
     */
    public void setMeasuredBy(MeasureDefinition measure) {
		
		this.measuredBy = measure;
	}
	

	/**
	 * Indica si el valor del PPI puede ser calculado y mostrado
	 * 
	 * @return 
	 */
	public Boolean valid() {
		return this.getMeasuredBy()!=null && this.getMeasuredBy().valid();
	}
	
}
