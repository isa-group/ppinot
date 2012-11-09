package isabpmcenter.ppinot.jsontoxml.factory.edge;


import org.oryxeditor.server.diagram.generic.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.model.BaseElement;
import isabpmcenter.ppinot.jsontoxml.model.AppliesToConnector;
import isabpmcenter.ppinot.jsontoxml.model.AppliesToElementConnector;
import isabpmcenter.ppinot.jsontoxml.model.AppliesToDataConnector;
import isabpmcenter.ppinot.jsontoxml.model.TimeConnector;

import de.hpi.bpmn2_0.factory.AbstractEdgesFactory;

/**
 * @author Philipp Giese
 * @author Sven Wagner-Boysen
 * 
 */
@StencilId({
	"appliesToElementConnector",
	"appliesToDataConnector",
	"TimeConnector"})
public class AppliesToConnectorFactory extends AbstractEdgesFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.hpi.bpmn2_0.factory.AbstractBpmnFactory#createProcessElement(org.
	 * oryxeditor.server.diagram.GenericShape)
	 */
	@Override
	protected BaseElement createProcessElement(GenericShape shape)
	throws BpmnConverterException {
		try {
			AppliesToConnector seqFlow = (AppliesToConnector) this.invokeCreatorMethod(shape);

			seqFlow.setId(shape.getResourceId());
			seqFlow.setName(shape.getProperty("name"));
			seqFlow.setState(shape.getProperty("state"));

			this.setCommonAttributes(seqFlow, shape);
			return seqFlow;
		} catch (Exception e) {
			throw new BpmnConverterException(
					"Error while creating the process element of "
							+ shape.getStencilId(), e);
		}
	}

	@StencilId("appliesToElementConnector")
	public AppliesToElementConnector createAppliesToElementConnector(GenericShape shape) {
		AppliesToElementConnector seqFlow = new AppliesToElementConnector();

		seqFlow.setWhen(shape.getProperty("when"));

		return seqFlow;
	}

	@StencilId("appliesToDataConnector")
	public AppliesToDataConnector createAppliesToDataConnector(GenericShape shape) {
		AppliesToDataConnector seqFlow = new AppliesToDataConnector();

		seqFlow.setRestriction(shape.getProperty("restriction"));
		seqFlow.setDataContentSelection(shape.getProperty("datacontentselection"));

		return seqFlow;
	}

	@StencilId("TimeConnector")
	public TimeConnector createTimeConnector(GenericShape shape) {
		TimeConnector seqFlow = new TimeConnector();

		seqFlow.setWhen(shape.getProperty("when"));
		seqFlow.setConditiontype(shape.getProperty("conditiontype"));

		return seqFlow;
	}

}

