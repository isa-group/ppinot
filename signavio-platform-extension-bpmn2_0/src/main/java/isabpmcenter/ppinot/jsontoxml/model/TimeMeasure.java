package isabpmcenter.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * <p>Java class for tTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTask">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/bpmn20}tActivity">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "timeMeasure", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTimeMeasure")
public class TimeMeasure
    extends CountMeasure
{

	@XmlAttribute
    protected String timeMeasureType;

	@XmlAttribute
    protected String singleInstanceAggFunction;

	/**
	 * Default constructor
	 */
	public TimeMeasure() {
		
	}
    
    public String getTimeMeasureType() {
		return timeMeasureType;
	}

	public void setTimeMeasureType(String timeMeasureType) {
		this.timeMeasureType = timeMeasureType;
	}

	public String getSingleInstanceAggFunction() {
		return singleInstanceAggFunction;
	}

	public void setSingleInstanceAggFunction(String singleInstanceAggFunction) {
		this.singleInstanceAggFunction = singleInstanceAggFunction;
	}

}
