package es.us.isa.ppinot.jsontoxml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

//import org.oryxeditor.server.diagram.Shape;
//import org.oryxeditor.server.diagram.StencilType;

import de.hpi.bpmn2_0.model.BaseElement;
import de.hpi.bpmn2_0.model.FlowElement;

@XmlRootElement(name = "ppi", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPpi")
public class Ppi 
    extends FlowElement
{

	@XmlAttribute
    protected String description;
 
	@XmlAttribute
    protected String target;

	@XmlAttribute(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object measuredBy;
	 
	@XmlAttribute
    protected String goals;
	 
	@XmlAttribute
    protected String scope;
	 
	@XmlAttribute
    protected String responsible;
	 
	@XmlAttribute
    protected String informed;
	 
	@XmlAttribute
    protected String comments;
    
	protected List<CountMeasure> countMeasure;
	protected List<TimeMeasure> timeMeasure;
	protected List<DataPropertyConditionMeasure> dataPropertyConditionMeasure;
	protected List<StateConditionMeasure> stateConditionMeasure;
	protected List<DataMeasure> dataMeasure;
	protected List<AggregatedMeasure> aggregatedMeasure;
	protected List<DerivedSingleInstanceMeasure> derivedSingleInstanceMeasure;
	protected List<DerivedMultiInstanceMeasure> derivedMultiInstanceMeasure;

	/**
	 * Default constructor
	 */
	public Ppi() {
		
	}
	
	public Ppi(Ppi ppi) {
		
		this.description = ppi.description;
		this.target = ppi.target;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	   
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public Object getMeasuredBy() {
		return measuredBy;
	}

	public void setMeasuredBy(Object measuredBy) {
		this.measuredBy = measuredBy;
	}
	 
    public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getInformed() {
		return informed;
	}

	public void setInformed(String informed) {
		this.informed = informed;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
    public void addChild(BaseElement measure) {
		
		if (measure instanceof AggregatedMeasure) {

			this.getAggregatedMeasure().add((AggregatedMeasure) measure);
		} else 
		if (measure instanceof DataPropertyConditionMeasure) {

			this.getDataPropertyConditionMeasure().add((DataPropertyConditionMeasure) measure);
		} else 
		if (measure instanceof StateConditionMeasure) {
			
			this.getStateConditionMeasure().add((StateConditionMeasure) measure);
		} else 
		if (measure instanceof DataMeasure) {

			this.getDataMeasure().add((DataMeasure) measure);
		} else 
		if (measure instanceof DerivedMultiInstanceMeasure) {

			this.getDerivedMultiInstanceMeasure().add((DerivedMultiInstanceMeasure) measure);
		} else 
		if (measure instanceof DerivedSingleInstanceMeasure) {

			this.getDerivedSingleInstanceMeasure().add((DerivedSingleInstanceMeasure) measure);
		} else 
		if (measure instanceof TimeMeasure) {

			this.getTimeMeasure().add((TimeMeasure) measure);
		} else
    	if(measure instanceof CountMeasure) {

			this.getCountMeasure().add((CountMeasure) measure);
    	}
    }
	
	public List<CountMeasure> getCountMeasure() {
        if (countMeasure == null) {
        	countMeasure = new ArrayList<CountMeasure>();
        }
		return countMeasure;
	}

	public void setCountMeasure(List<CountMeasure> countMeasure) {
		this.countMeasure = countMeasure;
	}
    
	public List<AggregatedMeasure> getAggregatedMeasure() {
        if (aggregatedMeasure == null) {
        	aggregatedMeasure = new ArrayList<AggregatedMeasure>();
        }
		return aggregatedMeasure;
	}

	public void setAggregatedMeasure(
			List<AggregatedMeasure> aggregatedMeasure) {
		this.aggregatedMeasure = aggregatedMeasure;
	}
    
	public List<TimeMeasure> getTimeMeasure() {
        if (timeMeasure == null) {
        	timeMeasure = new ArrayList<TimeMeasure>();
        }
		return timeMeasure;
	}

	public void setTimeMeasure(List<TimeMeasure> timeMeasure) {
		this.timeMeasure = timeMeasure;
	}

	public List<DataPropertyConditionMeasure> getDataPropertyConditionMeasure() {
        if (dataPropertyConditionMeasure == null) {
        	dataPropertyConditionMeasure = new ArrayList<DataPropertyConditionMeasure>();
        }
		return dataPropertyConditionMeasure;
	}

	public void setDataPropertyConditionMeasure(
			List<DataPropertyConditionMeasure> dataPropertyConditionMeasure) {
		this.dataPropertyConditionMeasure = dataPropertyConditionMeasure;
	}

	public List<StateConditionMeasure> getStateConditionMeasure() {
        if (stateConditionMeasure == null) {
        	stateConditionMeasure = new ArrayList<StateConditionMeasure>();
        }
		return stateConditionMeasure;
	}

	public void setStateConditionMeasure(
			List<StateConditionMeasure> stateConditionMeasure) {
		this.stateConditionMeasure = stateConditionMeasure;
	}

	public List<DataMeasure> getDataMeasure() {
        if (dataMeasure == null) {
        	dataMeasure = new ArrayList<DataMeasure>();
        }
		return dataMeasure;
	}

	public void setDataMeasure(List<DataMeasure> dataMeasure) {
		this.dataMeasure = dataMeasure;
	}

	public List<DerivedSingleInstanceMeasure> getDerivedSingleInstanceMeasure() {
        if (derivedSingleInstanceMeasure == null) {
        	derivedSingleInstanceMeasure = new ArrayList<DerivedSingleInstanceMeasure>();
        }
		return derivedSingleInstanceMeasure;
	}

	public void setDerivedSingleInstanceMeasure(
			List<DerivedSingleInstanceMeasure> derivedSingleInstanceMeasure) {
		this.derivedSingleInstanceMeasure = derivedSingleInstanceMeasure;
	}

	public List<DerivedMultiInstanceMeasure> getDerivedMultiInstanceMeasure() {
        if (derivedMultiInstanceMeasure == null) {
        	derivedMultiInstanceMeasure = new ArrayList<DerivedMultiInstanceMeasure>();
        }
		return derivedMultiInstanceMeasure;
	}

	public void setDerivedMultiInstanceMeasure(
			List<DerivedMultiInstanceMeasure> derivedMultiInstanceMeasure) {
		this.derivedMultiInstanceMeasure = derivedMultiInstanceMeasure;
	}

}
