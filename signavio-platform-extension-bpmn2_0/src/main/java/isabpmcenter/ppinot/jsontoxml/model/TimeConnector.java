package isabpmcenter.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TimeConnector", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTimeConnector")
public class TimeConnector extends AppliesToElementConnector {

	@XmlAttribute
	protected String conditiontype;

	/**
	 * Default constructor
	 */
	public TimeConnector() {
		
	}
	
	/* Getter & Setter */

	public String getConditiontype() {
		return conditiontype;
	}

	public void setConditiontype(String conditiontype) {
		this.conditiontype = conditiontype;
	}

}
