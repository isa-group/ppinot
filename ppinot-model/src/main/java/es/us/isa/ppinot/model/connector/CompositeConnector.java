package es.us.isa.ppinot.model.connector;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.ConditionMeasure;
import es.us.isa.ppinot.model.base.CountMeasure;
import es.us.isa.ppinot.model.base.DataMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionMeasure;
import es.us.isa.ppinot.model.base.StateConditionMeasure;
import es.us.isa.ppinot.model.base.TimeMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.connector.MeasureConnector;
import es.us.isa.ppinot.model.connector.ParameterConnector;
import es.us.isa.ppinot.model.connector.aggregated.AggregatedConnector;
import es.us.isa.ppinot.model.connector.base.BaseMeasureConnector;
import es.us.isa.ppinot.model.connector.base.CountConnector;
import es.us.isa.ppinot.model.connector.base.DataConnector;
import es.us.isa.ppinot.model.connector.base.StateConditionConnector;
import es.us.isa.ppinot.model.connector.base.TimeConnector;
import es.us.isa.ppinot.model.connector.derived.DerivedConnector;
import es.us.isa.ppinot.model.derived.ListMeasure;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
	include=JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
	@JsonSubTypes.Type(value = ParameterConnector.class, name = "ParameterConnector"),
	@JsonSubTypes.Type(value = MeasureConnector.class, name = "MeasureConnector"),
	@JsonSubTypes.Type(value = BaseMeasureConnector.class, name = "BaseConnector"),
	@JsonSubTypes.Type(value = CountConnector.class, name = "CountConnector"),
	@JsonSubTypes.Type(value = DataConnector.class, name = "DataConnector"),
	@JsonSubTypes.Type(value = StateConditionConnector.class, name = "StateConditionConnector"),
	@JsonSubTypes.Type(value = TimeConnector.class, name = "TimeConnector"),
	@JsonSubTypes.Type(value = AggregatedConnector.class, name = "AggregatedConnector"),
	@JsonSubTypes.Type(value = DerivedConnector.class, name = "DerivedConnector"),
	
})
public class CompositeConnector implements Cloneable{
	
	//Id of the connector
	private String id;
	
	//Name of the connector
	private String name;
	
	//Description of the connector
	private String description;
	
	//Vale la pena relacionarlo directamente con una medida????
	
	public CompositeConnector(){
		super();
		this.setId("");
		this.setName("");
		this.setDescription("");
	}
	
	public CompositeConnector(String id, String name, String description){
		super();
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public CompositeConnector clone(){
		final CompositeConnector clone;
		try{
			clone = (CompositeConnector) super.clone();
			return clone;
		}catch(Exception e){
			System.out.println("\t!>>>> Exception of CompositeConnector - clone()\n~~~" + e.getMessage());
			return null;
		}
	}
	
	public boolean valid(){
		return this.getId()!=null && !this.getId().contentEquals("") && this.getName()!=null && !this.getName().contentEquals("");
	}
	
}
