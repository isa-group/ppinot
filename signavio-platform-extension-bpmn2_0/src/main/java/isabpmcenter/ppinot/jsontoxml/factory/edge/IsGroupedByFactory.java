package isabpmcenter.ppinot.jsontoxml.factory.edge;

import org.oryxeditor.server.diagram.generic.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.model.BaseElement;
import isabpmcenter.ppinot.jsontoxml.model.IsGroupedBy;

import de.hpi.bpmn2_0.factory.AbstractEdgesFactory;

@StencilId({"isGroupedBy"})
public class IsGroupedByFactory extends AbstractEdgesFactory {
	
	@Override
	protected BaseElement createProcessElement(GenericShape shape)
	throws BpmnConverterException {
		try {
			IsGroupedBy seqFlow = (IsGroupedBy) this.invokeCreatorMethod(shape);

			seqFlow.setId(shape.getResourceId());
			seqFlow.setName(shape.getProperty("name"));

			this.setCommonAttributes(seqFlow, shape);
			return seqFlow;
		} catch (Exception e) {
			throw new BpmnConverterException(
					"Error while creating the process element of "
							+ shape.getStencilId(), e);
		}
	}

	@StencilId("isGroupedBy")
	public IsGroupedBy createCountMeasure(GenericShape shape) {
		IsGroupedBy seqFlow = new IsGroupedBy();

		seqFlow.setDataContentSelection(shape.getProperty("datacontentselection"));

		return seqFlow;
	}
}

