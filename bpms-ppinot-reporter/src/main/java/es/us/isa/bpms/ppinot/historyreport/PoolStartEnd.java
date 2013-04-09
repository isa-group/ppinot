package es.us.isa.bpms.ppinot.historyreport;

public class PoolStartEnd extends StartEndMoment {

	private String name;
	private Boolean atEnd;

	public PoolStartEnd() {
		
		super();
		
		this.setName("");
		this.setAtEnd(false);
		this.setCond(false);
	}

	public PoolStartEnd(String name, Boolean atEnd) {
		
		super();
		
		this.setName(name);
		this.setAtEnd(atEnd);
		this.setCond(false);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name==null)
			name = "";
		this.name = name;
	}

	public Boolean getAtEnd() {
		return atEnd;
	}

	public void setAtEnd(Boolean atEnd) {
		this.atEnd = atEnd;
	}

}
