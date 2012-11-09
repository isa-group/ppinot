package isabpmcenter.ppinot.jsontoxml.factory.node;

/**
 * Copyright (c) 2011
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

//import org.oryxeditor.server.diagram.GenericShape;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.bpmn2_0.exceptions.BpmnConverterException;
import isabpmcenter.ppinot.jsontoxml.model.CountMeasure;
import isabpmcenter.ppinot.jsontoxml.model.AggregatedMeasure;
import isabpmcenter.ppinot.jsontoxml.model.DataPropertyConditionMeasure;
import isabpmcenter.ppinot.jsontoxml.model.DataMeasure;
import isabpmcenter.ppinot.jsontoxml.model.DerivedSingleInstanceMeasure;
import isabpmcenter.ppinot.jsontoxml.model.DerivedMultiInstanceMeasure;
import isabpmcenter.ppinot.jsontoxml.model.StateConditionMeasure;
import isabpmcenter.ppinot.jsontoxml.model.TimeMeasure;
//import de.hpi.bpmn2_0.model.diagram.activity.ActivityShape;
import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.factory.BPMNElement;
import de.hpi.bpmn2_0.model.bpmndi.BPMNShape;

import org.oryxeditor.server.diagram.generic.GenericShape;

/**
 * Concrete class to create any kind of task objects from a {@link GenericShape} with
 * the stencil id "http://b3mn.org/stencilset/bpmn2.0#Task"
 * 
 * @author Philipp Giese
 * @author Sven Wagner-Boysen
 * 
 */
@StencilId({"countMeasure",
		"timeMeasure",
		"dataPropertyConditionMeasure",
		"stateConditionMeasure",
		"dataMeasure",
		"aggregatedMeasureGeneric",
		"countAggregatedMeasure",
		"timeAggregatedMeasure",
		"dataPropertyConditionAggregatedMeasure",
		"stateConditionAggregatedMeasure",
		"dataAggregatedMeasure",
		"derivedSingleInstanceMeasure",
		"derivedMultiInstanceMeasure"
		})
public class CountMeasureFactory extends AbstractBpmnFactory {

	@Override
	protected BPMNShape createDiagramElement(GenericShape shape) {
		BPMNShape actShape = new BPMNShape();
		this.setVisualAttributes(actShape, shape);

		return actShape;
	}
	
	@Override
	protected CountMeasure createProcessElement(GenericShape shape)
			throws BpmnConverterException {
		try {
			CountMeasure countMeasure = (CountMeasure) this.invokeCreatorMethod(shape);
//			this.setStandardAttributes(task, shape);
			return countMeasure;
		} catch (Exception e) {
			throw new BpmnConverterException(
					"Error while creating the process element of "
							+ shape.getStencilId(), e);
		}
	}

	@Override
	public BPMNElement createBpmnElement(GenericShape shape, BPMNElement parent)
			throws BpmnConverterException {
		CountMeasure countMeasure = this.createProcessElement(shape);
//		ActivityShape activity = this.createDiagramElement(shape);
//		activity.setActivityRef(countMeasure); 
		BPMNShape activity = this.createDiagramElement(shape);
		activity.setBpmnElement(countMeasure);

		return new BPMNElement(activity, countMeasure, shape.getResourceId());
	}

	@StencilId("countMeasure")
	public CountMeasure createCountMeasure(GenericShape shape) {
		CountMeasure task = new CountMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		return task;
	}

	@StencilId("timeMeasure")
	public TimeMeasure createTimeMeasure(GenericShape shape) {
		TimeMeasure task = new TimeMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		task.setTimeMeasureType(shape.getProperty("timemeasuretype"));
		task.setSingleInstanceAggFunction(shape.getProperty("singleinstanceaggfunction"));
		
		return task;
	}

	@StencilId("dataPropertyConditionMeasure")
	public DataPropertyConditionMeasure createDataPropertyConditionMeasure(GenericShape shape) {
		DataPropertyConditionMeasure task = new DataPropertyConditionMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));
		
		task.setStatesconsidered(shape.getProperty("statesconsidered"));

		return task;
	}

	@StencilId("stateConditionMeasure")
	public StateConditionMeasure createStateConditionMeasure(GenericShape shape) {
		StateConditionMeasure task = new StateConditionMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		return task;
	}

	@StencilId("dataMeasure")
	public DataMeasure createDataMeasure(GenericShape shape) {
		DataMeasure task = new DataMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		return task;
	}

	@StencilId("aggregatedMeasureGeneric")
	public CountMeasure createAggregatedMeasureGeneric(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		
		return task;
	}

	@StencilId("countAggregatedMeasure")
	public CountMeasure createCountAggregatedMeasure(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		task.setSamplingfrequency(shape.getProperty("samplingfrequency"));

		CountMeasure instance = new CountMeasure();
		
		instance.setName(shape.getProperty("name"));
		instance.setDescription(shape.getProperty("description"));
		instance.setScale(shape.getProperty("scale"));
		instance.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		task.getCountMeasure().add(instance);
		
		return task;
	}

	@StencilId("timeAggregatedMeasure")
	public CountMeasure createTimeAggregatedMeasure(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		task.setSamplingfrequency(shape.getProperty("samplingfrequency"));

		TimeMeasure instance = new TimeMeasure();
		
		instance.setName(shape.getProperty("name"));
		instance.setDescription(shape.getProperty("description"));
		instance.setScale(shape.getProperty("scale"));
		instance.setUnitofmeasure(shape.getProperty("unitofmeasure"));
		
		instance.setTimeMeasureType(shape.getProperty("timemeasuretype"));
		instance.setSingleInstanceAggFunction(shape.getProperty("singleinstanceaggfunction"));

		task.getTimeMeasure().add(instance);
		
		return task;
	}

	@StencilId("dataPropertyConditionAggregatedMeasure")
	public CountMeasure createDataPropertyConditionAggregatedMeasure(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		task.setSamplingfrequency(shape.getProperty("samplingfrequency"));

		DataPropertyConditionMeasure instance = new DataPropertyConditionMeasure();
		
		instance.setName(shape.getProperty("name"));
		instance.setDescription(shape.getProperty("description"));
		instance.setScale(shape.getProperty("scale"));
		instance.setUnitofmeasure(shape.getProperty("unitofmeasure"));
		
		instance.setStatesconsidered(shape.getProperty("statesconsidered"));

		task.getDataPropertyConditionMeasure().add(instance);
		
		return task;
	}

	@StencilId("stateConditionAggregatedMeasure")
	public CountMeasure createStateConditionAggregatedMeasure(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		task.setSamplingfrequency(shape.getProperty("samplingfrequency"));

		StateConditionMeasure instance = new StateConditionMeasure();
		
		instance.setName(shape.getProperty("name"));
		instance.setDescription(shape.getProperty("description"));
		instance.setScale(shape.getProperty("scale"));
		instance.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		task.getStateConditionMeasure().add(instance);
		
		return task;
	}

	@StencilId("dataAggregatedMeasure")
	public CountMeasure createDataAggregatedMeasure(GenericShape shape) {
		AggregatedMeasure task = new AggregatedMeasure();

		task.setId(shape.getResourceId());
		task.setAggregationfunction(shape.getProperty("aggregationfunction"));
		task.setSamplingfrequency(shape.getProperty("samplingfrequency"));

		DataMeasure instance = new DataMeasure();
		
		instance.setName(shape.getProperty("name"));
		instance.setDescription(shape.getProperty("description"));
		instance.setScale(shape.getProperty("scale"));
		instance.setUnitofmeasure(shape.getProperty("unitofmeasure"));
		
		task.getDataMeasure().add(instance);
		
		return task;
	}

	@StencilId("derivedSingleInstanceMeasure")
	public CountMeasure createDerivedSingleInstanceMeasure(GenericShape shape) {
		DerivedSingleInstanceMeasure task = new DerivedSingleInstanceMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		task.setFunction(shape.getProperty("function"));
		
		return task;
	}

	@StencilId("derivedMultiInstanceMeasure")
	public CountMeasure createDerivedMultiInstanceMeasure(GenericShape shape) {
		DerivedMultiInstanceMeasure task = new DerivedMultiInstanceMeasure();

		task.setId(shape.getResourceId());
		task.setName(shape.getProperty("name"));
		task.setDescription(shape.getProperty("description"));
		task.setScale(shape.getProperty("scale"));
		task.setUnitofmeasure(shape.getProperty("unitofmeasure"));

		task.setFunction(shape.getProperty("function"));
		
		return task;
	}

}
