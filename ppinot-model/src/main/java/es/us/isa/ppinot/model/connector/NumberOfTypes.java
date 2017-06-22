package es.us.isa.ppinot.model.connector;

public class NumberOfTypes {

	public int numAggregated;
	public int numCount;
	public int numData;
	public int numStateCondition;
	public int numTime;
	public int numDerivedSingle;
	public int numDerivedMulti;
	public int numList;
	public int numExternalValue;
	public int numOther;
	
	public NumberOfTypes(){
		this.numAggregated = 0;
		this.numCount = 0;
		this.numData = 0;
		this.numStateCondition = 0;
		this.numTime = 0;
		this.numDerivedSingle = 0;
		this.numDerivedMulti = 0;
		this.numList = 0;
		this.numExternalValue = 0;
		this.numOther = 0;
	}
	
	public NumberOfTypes(int numAggregated, int numCount, int numData, int numStateCondition, int numTime, int numDerivedSingle, int numDerivedMulti, int numList, int numExternalValue, int numOtros){
		this.numAggregated = numAggregated;
		this.numCount = numCount;
		this.numData = numData;
		this.numStateCondition = numStateCondition;
		this.numTime = numTime;
		this.numDerivedSingle = numDerivedSingle;
		this.numDerivedMulti = numDerivedMulti;
		this.numList = numList;
		this.numExternalValue = numExternalValue;
		this.numOther = numOtros;
	}
	
	public int getNumAggregated() {
		return numAggregated;
	}
	public void setNumAggregated(int numAgregated) {
		this.numAggregated = numAgregated;
	}
	public int getNumCount() {
		return numCount;
	}
	public void setNumCount(int numCount) {
		this.numCount = numCount;
	}
	public int getNumData() {
		return numData;
	}
	public void setNumData(int numData) {
		this.numData = numData;
	}
	public int getNumStateCondition() {
		return numStateCondition;
	}
	public void setNumStateCondition(int numStateCondition) {
		this.numStateCondition = numStateCondition;
	}
	public int getNumTime() {
		return numTime;
	}
	public void setNumTime(int numTime) {
		this.numTime = numTime;
	}
	public int getNumDerivedSingle() {
		return numDerivedSingle;
	}
	public void setNumDerivedSingle(int numDerivedSingle) {
		this.numDerivedSingle = numDerivedSingle;
	}
	public int getNumDerivedMulti() {
		return numDerivedMulti;
	}
	public void setNumDerivedMulti(int numDerivedMulti) {
		this.numDerivedMulti = numDerivedMulti;
	}
	public int getNumList() {
		return numList;
	}
	public void setNumList(int numList) {
		this.numList = numList;
	}
	public int getNumExternalValue() {
		return numExternalValue;
	}
	public void setNumExternalValue(int numExternalValue) {
		this.numExternalValue = numExternalValue;
	}
	public int getNumOther() {
		return numOther;
	}
	public void setNumOther(int numOther) {
		this.numOther = numOther;
	}
	
	
	
	
	
	
	
	
}
