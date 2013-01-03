package es.us.isa.ppinot.model.condition;

public class Condition {
	
	private String appliesTo;
	
	public Condition() {

		super();
		this.setAppliesTo("");
	}
	
	public Condition(String appliesTo) {

		super();
		this.setAppliesTo(appliesTo);
	}

	public String getAppliesTo() {
		return appliesTo;
	}

	public void setAppliesTo(String appliesTo) {
		this.appliesTo = appliesTo;
	}
	
	

}
