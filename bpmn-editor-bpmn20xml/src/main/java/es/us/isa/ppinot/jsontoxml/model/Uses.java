package es.us.isa.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//import org.oryxeditor.server.diagram.Shape;
//import org.oryxeditor.server.diagram.StencilType;

import de.hpi.bpmn2_0.model.connector.Edge;

@XmlRootElement(name = "uses", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tUses")
public class Uses extends Edge {

	@XmlAttribute
	protected String variable;

	/**
	 * Default constructor
	 */
	public Uses() {
		
	}

	/* Getter & Setter */

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

}
