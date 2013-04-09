package es.us.isa.ppinot.jsontoxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

//import org.oryxeditor.server.diagram.Shape;
//import org.oryxeditor.server.diagram.StencilType;

import de.hpi.bpmn2_0.model.FlowElement;


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
@XmlRootElement(name = "countMeasure", namespace="http://www.isa.us.es/ppinot")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCountMeasure")
@XmlSeeAlso({
    TimeMeasure.class,
    DataPropertyConditionMeasure.class,
    StateConditionMeasure.class,
    DataMeasure.class,
    AggregatedMeasure.class,
    DerivedSingleInstanceMeasure.class
})
public class CountMeasure 
    extends FlowElement
{
    
    @XmlAttribute
    protected String name;

	@XmlAttribute
    protected String description;
 
	@XmlAttribute
    protected String scale;
    
    @XmlAttribute
    protected String unitofmeasure;
	
	/**
	 * Default constructor
	 */
	public CountMeasure() {
		
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

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getUnitofmeasure() {
		return unitofmeasure;
	}

	public void setUnitofmeasure(String unitofmeasure) {
		this.unitofmeasure = unitofmeasure;
	}

}
