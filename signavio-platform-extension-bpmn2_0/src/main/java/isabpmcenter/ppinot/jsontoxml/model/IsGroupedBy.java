package isabpmcenter.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.hpi.bpmn2_0.model.connector.Edge;

@XmlRootElement(name = "isGroupedBy", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tIsGroupedBy")
public class IsGroupedBy extends Edge {

	@XmlAttribute
	protected String dataContentSelection;

	/**
	 * Default constructor
	 */
	public IsGroupedBy() {
		
	}

	/* Getter & Setter */

	public String getDataContentSelection() {
		return dataContentSelection;
	}

	public void setDataContentSelection(String dataContentSelection) {
		this.dataContentSelection = dataContentSelection;
	}

}
