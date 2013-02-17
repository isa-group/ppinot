package isabpmcenter.ppinot.jsontoxml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.FlowElement;
import de.hpi.bpmn2_0.model.activity.Activity;
import de.hpi.bpmn2_0.model.connector.Edge;
import de.hpi.bpmn2_0.model.extension.AbstractExtensionElement;

@XmlRootElement(name = "ppiset", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPpiset")
public class Ppiset extends AbstractExtensionElement {
    
	@XmlElement(name = "countMeasure", type = CountMeasure.class)
	protected List<CountMeasure> countMeasure;
    
	@XmlElement(name = "timeMeasure", type = TimeMeasure.class)
	protected List<TimeMeasure> timeMeasure;
    
	@XmlElement(name = "dataPropertyConditionMeasure", type = DataPropertyConditionMeasure.class)
	protected List<DataPropertyConditionMeasure> dataPropertyConditionMeasure;
    
	@XmlElement(name = "stateConditionMeasure", type = StateConditionMeasure.class)
	protected List<StateConditionMeasure> stateConditionMeasure;
    
	@XmlElement(name = "dataMeasure", type = DataMeasure.class)
	protected List<DataMeasure> dataMeasure;

	@XmlElement(name = "aggregatedMeasure", type = AggregatedMeasure.class)
	protected List<AggregatedMeasure> aggregatedMeasure;
    
	@XmlElement(name = "derivedSingleInstanceMeasure", type = DerivedSingleInstanceMeasure.class)
	protected List<DerivedSingleInstanceMeasure> derivedSingleInstanceMeasure;
    
	@XmlElement(name = "derivedMultiInstanceMeasure", type = DerivedMultiInstanceMeasure.class)
	protected List<DerivedMultiInstanceMeasure> derivedMultiInstanceMeasure;

	@XmlElement(name = "appliesToElementConnector", type = AppliesToElementConnector.class)
    protected List<AppliesToElementConnector> appliesToElementConnector;

	@XmlElement(name = "appliesToDataConnector", type = AppliesToDataConnector.class)
    protected List<AppliesToDataConnector> appliesToDataConnector;

	@XmlElement(name = "TimeConnector", type = TimeConnector.class)
    protected List<TimeConnector> TimeConnector;

	@XmlElement(name = "uses", type = Uses.class)
    protected List<Uses> uses;

	@XmlElement(name = "aggregates", type = Aggregates.class)
    protected List<Aggregates> aggregates;

	@XmlElement(name = "isGroupedBy", type = IsGroupedBy.class)
    protected List<IsGroupedBy> isGroupedBy;
   
	@XmlElement(name = "ppi", type = Ppi.class)
	protected List<Ppi> ppi;
	
	/**
	 * Default constructor
	 */
	public Ppiset() {
		
	}
	
	public Boolean isEmpty() {
		return this.getAggregatedMeasure().size()==0 &&
				this.getCountMeasure().size()==0 &&
				this.getTimeMeasure().size()==0 &&
				this.getDataPropertyConditionMeasure().size()==0 &&
				this.getStateConditionMeasure().size()==0 &&
				this.getDataMeasure().size()==0 &&
				this.getAggregatedMeasure().size()==0 &&
				this.getDerivedSingleInstanceMeasure().size()==0 &&
				this.getDerivedMultiInstanceMeasure().size()==0 &&
				this.getAppliesToElementConnector().size()==0 &&
				this.getAppliesToDataConnector().size()==0 &&
				this.getTimeConnector().size()==0 &&
				this.getUses().size()==0 &&
				this.getAggregates().size()==0 &&
				this.getIsGroupedBy().size()==0;
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

	public List<AppliesToElementConnector> getAppliesToElementConnector() {
        if (appliesToElementConnector == null) {
        	appliesToElementConnector = new ArrayList<AppliesToElementConnector>();
        }
		return appliesToElementConnector;
	}

	public void setAppliesToElementConnector(List<AppliesToElementConnector> appliesToElementConnector) {
		this.appliesToElementConnector = appliesToElementConnector;
	}

	public List<AppliesToDataConnector> getAppliesToDataConnector() {
        if (appliesToDataConnector == null) {
        	appliesToDataConnector = new ArrayList<AppliesToDataConnector>();
        }
		return appliesToDataConnector;
	}

	public void setAppliesToDataConnector(List<AppliesToDataConnector> appliesToDataConnector) {
		this.appliesToDataConnector = appliesToDataConnector;
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

	public List<Ppi> getPpi() {
        if (ppi == null) {
        	ppi = new ArrayList<Ppi>();
        }
		return ppi;
	}

	public void setPpi(List<Ppi> ppi) {
		this.ppi = ppi;
	}

	public List<TimeConnector> getTimeConnector() {
        if (TimeConnector == null) {
        	TimeConnector = new ArrayList<TimeConnector>();
        }
		return TimeConnector;
	}

	public void setTimeConnector(List<TimeConnector> timeConnector) {
		TimeConnector = timeConnector;
	}

	public List<Uses> getUses() {
        if (uses == null) {
        	uses = new ArrayList<Uses>();
        }
		return uses;
	}

	public void setUses(List<Uses> uses) {
		this.uses = uses;
	}

	public List<Aggregates> getAggregates() {
        if (aggregates == null) {
        	aggregates = new ArrayList<Aggregates>();
        }
		return aggregates;
	}

	public void setAggregates(List<Aggregates> aggregates) {
		this.aggregates = aggregates;
	}

	public List<IsGroupedBy> getIsGroupedBy() {
        if (isGroupedBy == null) {
        	isGroupedBy = new ArrayList<IsGroupedBy>();
        }
		return isGroupedBy;
	}

	public void setIsGroupedBy(List<IsGroupedBy> isGroupedBy) {
		this.isGroupedBy = isGroupedBy;
	}

	public void addPpi(FlowElement ppi) {
		
		if (ppi instanceof AggregatedMeasure)
			this.getAggregatedMeasure().add((AggregatedMeasure) ppi);
		else
		if (ppi instanceof DataPropertyConditionMeasure)
			this.getDataPropertyConditionMeasure().add((DataPropertyConditionMeasure) ppi);
		else
		if (ppi instanceof StateConditionMeasure)
			this.getStateConditionMeasure().add((StateConditionMeasure) ppi);
		else
		if (ppi instanceof DataMeasure)
			this.getDataMeasure().add((DataMeasure) ppi);
		else
		if (ppi instanceof DerivedMultiInstanceMeasure)
			this.getDerivedMultiInstanceMeasure().add((DerivedMultiInstanceMeasure) ppi);
		else
		if (ppi instanceof DerivedSingleInstanceMeasure)
			this.getDerivedSingleInstanceMeasure().add((DerivedSingleInstanceMeasure) ppi);
		else
		if (ppi instanceof TimeMeasure)
			this.getTimeMeasure().add((TimeMeasure) ppi);
		else
		if (ppi instanceof CountMeasure)
			this.getCountMeasure().add((CountMeasure) ppi);
		else
		if (ppi instanceof Ppi)
			this.getPpi().add((Ppi) ppi);
	}
	
	public void addPpiCon(Edge ppiCon) {
		
		if (ppiCon instanceof Uses)
			this.getUses().add((Uses) ppiCon);
		else
		if (ppiCon instanceof Aggregates)
			this.getAggregates().add((Aggregates) ppiCon);
		else
		if (ppiCon instanceof IsGroupedBy)
			this.getIsGroupedBy().add((IsGroupedBy) ppiCon);
		else
		if (ppiCon instanceof TimeConnector)
			this.getTimeConnector().add((TimeConnector) ppiCon);
		else
		if (ppiCon instanceof AppliesToElementConnector)
			this.getAppliesToElementConnector().add((AppliesToElementConnector) ppiCon);
		else
		if (ppiCon instanceof AppliesToDataConnector)
			this.getAppliesToDataConnector().add((AppliesToDataConnector) ppiCon);
	}
}
