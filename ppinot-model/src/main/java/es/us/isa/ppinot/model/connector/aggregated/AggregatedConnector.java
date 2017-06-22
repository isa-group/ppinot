package es.us.isa.ppinot.model.connector.aggregated;

import java.util.ArrayList;
import java.util.List;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
//import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.BaseMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.connector.MeasureConnector;

public class AggregatedConnector extends MeasureConnector{

	//Attributes such as the following, shoud be defined in a expandedMeasure,
	//not in a collapsedMeasure.
	// aggregationFunction
	// samplingFrequency
	
	// Information to group the measure
    private List<DataMeasure> ccGroupedBy;

    // Measure to be aggregated
    protected MeasureDefinition ccBaseMeasure;
    
    // Indica si la medida utiliza el conector aggregates o no
    protected Boolean ccAggregates;
	
	public AggregatedConnector(){
		super();
		this.setCcBaseMeasure(null);
		this.setCcAggregates(false);
		this.setCcGroupedBySelections(null);
	}
	
	public AggregatedConnector(String id, String name, String description, MeasureDefinition baseMeasure){
		super(id, name, description);
		this.setCcBaseMeasure(baseMeasure);
        this.setCcAggregates(false);
    	this.setCcGroupedBySelections(null);
	}
	
	public MeasureDefinition getCcBaseMeasure() {
    	return this.ccBaseMeasure;
    }
	
	public void setCcBaseMeasure(MeasureDefinition ccBaseMeasure) {
    	this.ccBaseMeasure = ccBaseMeasure;
    }
	
	public List<DataContentSelection> getCcGroupedBySelections() {
		List<DataContentSelection> ccSelections = new ArrayList<DataContentSelection>();
		for (DataMeasure m : ccGroupedBy) {
			ccSelections.add(m.getDataContentSelection());
		}
		return ccSelections;
	}
	
	public void setCcGroupedBySelections(List<DataContentSelection> ccGroupedBy) {
		this.ccGroupedBy = new ArrayList<DataMeasure>();
		if (ccGroupedBy != null) {
			for (DataContentSelection s : ccGroupedBy) {
				DataMeasure dm = new DataMeasure();
				dm.setDataContentSelection(s);
				this.ccGroupedBy.add(dm);
			}

		}
	}
	
	public List<DataMeasure> getCcGroupedBy() {
		return ccGroupedBy;
	}

	public AggregatedConnector setCcGroupedBy(List<DataMeasure> ccGroupedBy) {
		this.ccGroupedBy = ccGroupedBy;
		return this;
	}
	
	public Boolean getCcAggregates() {
		return ccAggregates;
	}

	public void setCcAggregates(Boolean ccAggregates) {
		this.ccAggregates = ccAggregates;
	}
	
	public boolean valid() {
			
		try{
			return super.valid() && this.getCcBaseMeasure() != null && !this.getCcBaseMeasure().getName().contentEquals("");
				//Me interesa que haya una medida conectada, no que la medida en sí esté correctamnete conectada
				//this.getCcBaseMeasure().valid(); 
				//La función va definida en la expandida, no aquí.
				//&& this.getCcAggregationFunction()!=null && !this.getAggregationFunction().isEmpty();
		}catch(Exception e){
			return false;
		}		
	}
	
	public AggregatedConnector clone(){
		
		final AggregatedConnector clone;
		
		try{
			clone = (AggregatedConnector) super.clone();
		}catch(Exception ex){
			throw new RuntimeException( "\t!>>>> Excepción en AggregatedConnector - clone()" );
		}
		return clone;
	}

}
