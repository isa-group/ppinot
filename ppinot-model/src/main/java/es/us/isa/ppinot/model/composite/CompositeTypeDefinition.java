package es.us.isa.ppinot.model.composite;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.us.isa.ppinot.model.composite.type.PercentageFulfillmentType;
import es.us.isa.ppinot.model.composite.type.PercentagePerformanceType;
import es.us.isa.ppinot.model.composite.type.TimeType;
import es.us.isa.ppinot.model.composite.type.PruebaAggregatedTyped;
import es.us.isa.ppinot.model.composite.type.PruebaDerivedSingleType;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="kind")
@JsonSubTypes({
	@JsonSubTypes.Type(value = PercentageFulfillmentType.class, name = "PercentageFulfillmentType"),
	@JsonSubTypes.Type(value = PercentagePerformanceType.class, name = "PercentagePerformanceType"),
	@JsonSubTypes.Type(value = TimeType.class, name = "TimeType"),
	@JsonSubTypes.Type(value = PruebaDerivedSingleType.class, name = "PruebaDerivedSingleType"),
	@JsonSubTypes.Type(value = PruebaAggregatedTyped.class, name = "PruebaAggregatedTyped"),
})

public abstract class CompositeTypeDefinition {

	private String name;
	
	/*
	 * Constructor de la clase
	 */
	public CompositeTypeDefinition(){
		super();
		this.setName("");
	}

	public CompositeTypeDefinition(String name){
		super();
		this.setName(name);
	}
	
	/*
	 * 
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
