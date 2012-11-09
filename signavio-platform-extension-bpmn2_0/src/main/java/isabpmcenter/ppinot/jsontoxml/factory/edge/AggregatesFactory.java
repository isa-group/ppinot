package isabpmcenter.ppinot.jsontoxml.factory.edge;

import org.oryxeditor.server.diagram.generic.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import de.hpi.bpmn2_0.model.BaseElement;
import isabpmcenter.ppinot.jsontoxml.model.Aggregates;

import de.hpi.bpmn2_0.factory.AbstractEdgesFactory;

@StencilId({"aggregates"})
public class AggregatesFactory extends AbstractEdgesFactory {
	
	@Override
	protected BaseElement createProcessElement(GenericShape shape)
	throws BpmnConverterException {
		try {
			Aggregates seqFlow = (Aggregates) this.invokeCreatorMethod(shape);

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

	@StencilId("aggregates")
	public Aggregates createAggregates(GenericShape shape) {
		Aggregates seqFlow = new Aggregates();

		return seqFlow;
	}
}

