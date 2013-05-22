package es.us.isa.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "appliesToDataConnector", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAppliesToDataConnector")
public class AppliesToDataConnector extends AppliesToConnector {

	@XmlAttribute
	protected String restriction;

	@XmlAttribute
	protected String dataContentSelection;

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public String getDataContentSelection() {
		return dataContentSelection;
	}

	public void setDataContentSelection(String dataContentSelection) {
		this.dataContentSelection = dataContentSelection;
	}

}
