package es.us.isa.ppinot.model;

import java.util.Date;

public class Scope {
	
	// Periodo de tiempo en el cual se desea evaluar la medida
	// se especifica en la propiedad scope en el proceso modelado en BPMN
    	// Año del cual se desea obtener la medida
    private String year;
		// Periodo del año: mes, trimestre o semestre
    private String period;
		// Fecha inicial del periodo del que se desea obtener la medida
    private Date startDate;
    	// Fecha final del periodo del que se desea obtener la medida
    private Date endDate;
    	// En el caso que existan instancias de proceso en que iniciaron antes de la fecha de inicio del periodo y terminaron despues de esta,
    	// indica si se incluyen o no
    private Boolean inStart;
    	// En el caso que existan instancias de proceso en que iniciaron antes de la fecha final del periodo y terminaron despues de esta,
    	// indica si se incluyen o no
    private Boolean inEnd;
    
    /**
     * Constructor de la clase
     */
    public Scope(){
    	super();
    	
    	this.setYear("");
    	this.setPeriod("");
    	this.setStartDate(null);
    	this.setEndDate(null);
    	this.setInStart(false);
    	this.setInEnd(false);
    }
    
    /**
     * Constructor de la clase
     * 
     * @param year Año del cual se desea obtener la medida
     * @param period Periodo del año: mes, trimestre o semestre
     * @param startDate Fecha inicial del periodo del que se desea obtener la medida
     * @param endDate Fecha final del periodo del que se desea obtener la medida
     * @param inStart En el caso que existan instancias de proceso en que iniciaron antes de la fecha de inicio del periodo y terminaron despues de esta, indica si se incluyen o no
     * @param inEnd En el caso que existan instancias de proceso en que iniciaron antes de la fecha final del periodo y terminaron despues de esta, indica si se incluyen o no
     */
    public Scope(String year, String period, Date startDate, Date endDate, Boolean inStart, Boolean inEnd){
    	super();
    	
    	this.setYear(year);
    	this.setPeriod(period);
    	this.setStartDate(startDate);
    	this.setEndDate(endDate);
    	this.setInStart(inStart);
    	this.setInEnd(inEnd);
    }
    
	/**
     * Devuelve el atributo year:
     * Año del cual se desea obtener la medida
     * 
     * @return Valor del atributo
     */
	public String getYear() {
		return year;
	}
	
    /**
     * Da valor al atributo year:
     * Año del cual se desea obtener la medida
     * 
     * @param value Valor del atributo
     */
	public void setYear(String year) {
		this.year = year;
	}
	
	/**
     * Devuelve el atributo period:
     * Periodo del año: mes, trimestre o semestre
     * 
     * @return Valor del atributo
     */
	public String getPeriod() {
		return period;
	}
	
    /**
     * Da valor al atributo period:
     * Periodo del año: mes, trimestre o semestre
     * 
     * @param value Valor del atributo
     */
	public void setPeriod(String period) {
		this.period = period;
	}
	
	/**
     * Devuelve el atributo startDate:
     * Fecha inicial del periodo del que se desea obtener la medida
     * 
     * @return Valor del atributo
     */
	public Date getStartDate() {
		return startDate;
	}
	
    /**
     * Da valor al atributo startDate:
     * Fecha inicial del periodo del que se desea obtener la medida
     * 
     * @param value Valor del atributo
     */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
     * Devuelve el atributo endDate:
     * Fecha final del periodo del que se desea obtener la medida
     * 
     * @return Valor del atributo
     */
	public Date getEndDate() {
		return endDate;
	}
	
    /**
     * Da valor al atributo endDate:
     * Fecha final del periodo del que se desea obtener la medida
     * 
     * @param value Valor del atributo
     */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
     * Devuelve el atributo inStart:
     * En el caso que existan instancias de proceso en que iniciaron antes de la fecha de inicio del periodo y terminaron despues de esta, indica si se incluyen o no
     * 
     * @return Valor del atributo
     */
	public Boolean getInStart() {
		return inStart;
	}
	
    /**
     * Da valor al atributo inStart:
     * En el caso que existan instancias de proceso en que iniciaron antes de la fecha de inicio del periodo y terminaron despues de esta, indica si se incluyen o no
     * 
     * @param value Valor del atributo
     */
	public void setInStart(Boolean inStart) {
		this.inStart = inStart;
	}
	
	/**
     * Devuelve el atributo inEnd:
     * En el caso que existan instancias de proceso en que iniciaron antes de la fecha final del periodo y terminaron despues de esta, indica si se incluyen o no
     * 
     * @return Valor del atributo
     */
	public Boolean getInEnd() {
		return inEnd;
	}
	
    /**
     * Da valor al atributo inEnd:
     * En el caso que existan instancias de proceso en que iniciaron antes de la fecha final del periodo y terminaron despues de esta, indica si se incluyen o no
     * 
     * @param value Valor del atributo
     */
	public void setInEnd(Boolean inEnd) {
		this.inEnd = inEnd;
	}

}
