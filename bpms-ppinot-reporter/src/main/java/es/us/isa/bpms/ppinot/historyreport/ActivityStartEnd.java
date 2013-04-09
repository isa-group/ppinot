package es.us.isa.bpms.ppinot.historyreport;

public class ActivityStartEnd extends StartEndMoment {

	private String name;
	private String id;
	private Boolean atEnd;

	public ActivityStartEnd() {
		
		super();
		
		this.setName("");
		this.setId("");
		this.setAtEnd(false);
		this.setCond(false);
	}

	public ActivityStartEnd(String name, String id, Boolean atEnd) {
		
		super();
		
		this.setName(name);
		this.setId(id);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getAtEnd() {
		return atEnd;
	}

	public void setAtEnd(Boolean atEnd) {
		this.atEnd = atEnd;
	}

}
