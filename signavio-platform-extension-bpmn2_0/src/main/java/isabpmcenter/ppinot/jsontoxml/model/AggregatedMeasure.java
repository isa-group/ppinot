package isabpmcenter.ppinot.jsontoxml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "aggregatedMeasure", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAggregatedMeasure")
public class AggregatedMeasure 
    extends CountMeasure
{
    
    @XmlAttribute
    protected String aggregationfunction;

	@XmlAttribute
    protected String samplingfrequency;
    
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
	
	/**
	 * Default constructor
	 */
	public AggregatedMeasure() {
		
	}
	
	public String getAggregationfunction() {
		return aggregationfunction;
	}

	public void setAggregationfunction(String aggregationfunction) {
		this.aggregationfunction = aggregationfunction;
	}

	public String getSamplingfrequency() {
		return samplingfrequency;
	}

	public void setSamplingfrequency(String samplingfrequency) {
		this.samplingfrequency = samplingfrequency;
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

}
