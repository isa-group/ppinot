package es.us.isa.ppinot.jsontoxml.factory.node;

//import org.oryxeditor.server.diagram.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
//import de.hpi.bpmn2_0.model.diagram.activity.ActivityShape;
import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.factory.BPMNElement;
import de.hpi.bpmn2_0.model.bpmndi.BPMNShape;
import es.us.isa.ppinot.jsontoxml.model.Ppi;

import org.oryxeditor.server.diagram.generic.GenericShape;

@StencilId({"ppi"})
public class PpiFactory extends AbstractBpmnFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.hpi.bpmn2_0.factory.AbstractBpmnFactory#createDiagramElement(org.
	 * oryxeditor.server.diagram.GenericShape)
	 */
	@Override
	protected BPMNShape createDiagramElement(GenericShape shape) {
		BPMNShape actShape = new BPMNShape();
		this.setVisualAttributes(actShape, shape);

		return actShape;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seede.hpi.bpmn2_0.factory.AbstractBpmnFactory#createProcessElement(org.
	 * oryxeditor.server.diagram.GenericShape)
	 */
	@Override
	protected Ppi createProcessElement(GenericShape shape)
			throws BpmnConverterException {
		
		try {
			
			Ppi ppi = (Ppi) this.invokeCreatorMethod(shape);
			return ppi;
		} catch (Exception e) {
			throw new BpmnConverterException(
					"Error while creating the process element of "
							+ shape.getStencilId(), e);
		}
	}
	
	@Override
	public BPMNElement createBpmnElement(GenericShape shape, BPMNElement parent)
			throws BpmnConverterException {
		
		Ppi ppi = this.createProcessElement(shape);
		
		BPMNShape activity = this.createDiagramElement(shape);
		activity.setBpmnElement(ppi);
	
	
		return new BPMNElement(activity, ppi, shape.getResourceId());
	}
	
	@StencilId("ppi")
	public Ppi createPpi(GenericShape shape) {
		
		Ppi task = new Ppi();
	
		task.setId(shape.getResourceId());
		task.setDescription(shape.getProperty("description"));
		task.setTarget(shape.getProperty("target"));

		task.setGoals(shape.getProperty("goals"));
		task.setScope(shape.getProperty("scope"));
		task.setResponsible(shape.getProperty("responsible"));
		task.setInformed(shape.getProperty("informed"));
		task.setComments(shape.getProperty("comments"));
		
		return task;
	}

}
