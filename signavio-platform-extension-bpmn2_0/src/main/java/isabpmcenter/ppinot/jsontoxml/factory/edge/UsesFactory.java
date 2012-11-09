package isabpmcenter.ppinot.jsontoxml.factory.edge;


import org.oryxeditor.server.diagram.generic.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.model.BaseElement;
import isabpmcenter.ppinot.jsontoxml.model.Uses;

import de.hpi.bpmn2_0.factory.AbstractEdgesFactory;

@StencilId({"uses"})
public class UsesFactory extends AbstractEdgesFactory {
	
	@Override
	protected BaseElement createProcessElement(GenericShape shape)
	throws BpmnConverterException {
		try {
			Uses seqFlow = (Uses) this.invokeCreatorMethod(shape);

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

	@StencilId("uses")
	public Uses createCountMeasure(GenericShape shape) {
		Uses seqFlow = new Uses();

		seqFlow.setVariable(shape.getProperty("variable"));

		return seqFlow;
	}
}

