package es.us.isa.ppinot.model;

import java.util.Date;

public class Scope {
	
	/**
	 * Período de tiempo en el cual se desea evaluar la medida
	 * se especifica en la propiedad scope en el proceso modelado en BPMN
	 */
    	// Año del cual se desea obtener la medida
    private String year;
		// Período del año: mes, trimestre o semestre
    private String period;
		// Fecha inicial del período del que se desea obtener la medida
    private Date startDate;
    	// Fecha final del período del que se desea obtener la medida
    private Date endDate;
    	// En el caso que existan instancias de proceso en que iniciaron antes de la fecha de inicio del período y terminaron después de esta,
    	// indica si se incluyen o no
    private Boolean inStart;
    	// En el caso que existan instancias de proceso en que iniciaron antes de la fecha final del período y terminaron después de esta,
    	// indica si se incluyen o no
    private Boolean inEnd;
    
    public Scope(){
    	super();
    	
    	this.setYear("");
    	this.setPeriod("");
    	this.setStartDate(null);
    	this.setEndDate(null);
    	this.setInStart(false);
    	this.setInEnd(false);
    }
    
    public Scope(String year, String period, Date startDate, Date endDate, Boolean inStart, Boolean inEnd){
    	super();
    	
    	this.setYear(year);
    	this.setPeriod(period);
    	this.setStartDate(startDate);
    	this.setEndDate(endDate);
    	this.setInStart(inStart);
    	this.setInEnd(inEnd);
    }
    
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Boolean getInStart() {
		return inStart;
	}
	
	public void setInStart(Boolean inStart) {
		this.inStart = inStart;
	}
	
	public Boolean getInEnd() {
		return inEnd;
	}
	
	public void setInEnd(Boolean inEnd) {
		this.inEnd = inEnd;
	}

}
