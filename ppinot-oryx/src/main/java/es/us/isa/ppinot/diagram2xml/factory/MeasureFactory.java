package es.us.isa.ppinot.diagram2xml.factory;

import de.hpi.bpmn2_0.annotations.StencilId;
import de.hpi.diagram.SignavioUUID;
import es.us.isa.ppinot.xml.*;

import org.oryxeditor.server.diagram.generic.GenericShape;

import javax.xml.bind.JAXBElement;

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
        "derivedMultiInstanceMeasure",
        "metric"
})
public class MeasureFactory extends AbstractPPINotFactory {

    private void setMeasureCommon(TMeasure measure, GenericShape shape) {
    	
    	System.out.println("\nsetMeasureCommon-recibido: Class[" + shape.getClass().getName().getClass() + "] TMeasure_capturada[" + measure.getClass().getName()+"]");
    	
        measure.setId(shape.getResourceId());
        measure.setName(valueOrNull(shape.getProperty("name")));
        measure.setDescription(valueOrNull(shape.getProperty("description")));
        measure.setScale(valueOrNull(shape.getProperty("scale")));
        measure.setUnitOfMeasure(valueOrNull(shape.getProperty("unitofmeasure")));
        
        System.out.println("setMeasureCommon-seteado: Class[" + measure.getClass().getName() + "] Id["+measure.getId() + "] Name["+ measure.getName()+ "] Descrip["+ measure.getDescription() + "] Scale["+ measure.getScale() + "]");
    }
    
    private void setTimeMeasureProperties(TTimeMeasure measure, GenericShape shape) {
        String timeMeasureType = shape.getProperty("timemeasuretype");
        if (notEmpty(timeMeasureType)) {
            measure.setTimeMeasureType(TTimeMeasureType.valueOf(timeMeasureType.toUpperCase()));
        }
        measure.setSingleInstanceAggFunction(valueOrNull(shape.getProperty("singleinstanceaggfunction")));
    }

    private void setAggregatedCommon(TAggregatedMeasure measure, GenericShape shape) {
        measure.setSamplingFrequency(valueOrNull(shape.getProperty("samplingfrequency")));
        measure.setAggregationFunction(shape.getProperty("aggregationfunction"));
    }

    private void setDerivedCommon(TDerivedMeasure derivedMeasure, GenericShape shape) {
        derivedMeasure.setFunction(shape.getProperty("function"));
    }

    @StencilId("countMeasure")
    public JAXBElement<TCountMeasure> createCountMeasure(GenericShape shape) {
        TCountMeasure measure = new TCountMeasure();
        setMeasureCommon(measure, shape);

        return factory.createCountMeasure(measure);
    }

    @StencilId("timeMeasure")
    public JAXBElement<TTimeMeasure> createTimeMeasure(GenericShape shape) {
        TTimeMeasure measure = new TTimeMeasure();
        setMeasureCommon(measure, shape);
        setTimeMeasureProperties(measure, shape);

        return factory.createTimeMeasure(measure);
    }

    
    @StencilId("dataPropertyConditionMeasure")
    public JAXBElement<TDataPropertyConditionMeasure> createDataPropertyConditionMeasure(GenericShape shape) {
        TDataPropertyConditionMeasure measure = new TDataPropertyConditionMeasure();
        setMeasureCommon(measure, shape);

        return factory.createDataPropertyConditionMeasure(measure);
    }

    @StencilId("stateConditionMeasure")
    public JAXBElement<TStateConditionMeasure> createStateConditionMeasure(GenericShape shape) {
        TStateConditionMeasure measure = new TStateConditionMeasure();
        setMeasureCommon(measure, shape);

        return factory.createStateConditionMeasure(measure);
    }

    @StencilId("dataMeasure")
    public JAXBElement<TDataMeasure> createDataMeasure(GenericShape shape) {
        TDataMeasure measure = new TDataMeasure();
        setMeasureCommon(measure, shape);

        return factory.createDataMeasure(measure);
    }
    
    /*@StencilId("metric")
    public JAXBElement<TMetric> createMetric(GenericShape shape) {
    	System.out.println(">>>>>>>>>>>ENTRO EN EL CREATE_METRIC");
    	TMetric measure = new TMetric();
        setMeasureCommon(measure, shape);
        return factory.createMetric(measure);
    }*/

    
    @StencilId("aggregatedMeasureGeneric")
    public JAXBElement<TAggregatedMeasure> createAggregatedMeasureGeneric(GenericShape shape) {
        TAggregatedMeasure measure = new TAggregatedMeasure();
        setMeasureCommon(measure, shape);
        setAggregatedCommon(measure, shape);

        return factory.createAggregatedMeasure(measure);
    }

    @StencilId("countAggregatedMeasure")
    public JAXBElement<TAggregatedMeasure> createCountAggregatedMeasure(GenericShape shape) {
        JAXBElement<TAggregatedMeasure> element = createAggregatedMeasureGeneric(shape);

        TCountMeasure countMeasure = new TCountMeasure();
        countMeasure.setId(SignavioUUID.generate());
        element.getValue().setCountMeasure(countMeasure);

        return element;
    }

    @StencilId("timeAggregatedMeasure")
    public JAXBElement<TAggregatedMeasure> createTimeAggregatedMeasure(GenericShape shape) {
        JAXBElement<TAggregatedMeasure> element = createAggregatedMeasureGeneric(shape);

        TTimeMeasure timeMeasure = new TTimeMeasure();
        timeMeasure.setId(SignavioUUID.generate());
        setTimeMeasureProperties(timeMeasure, shape);
        element.getValue().setTimeMeasure(timeMeasure);

        return element;
    }

    @StencilId("dataPropertyConditionAggregatedMeasure")
    public JAXBElement<TAggregatedMeasure> createDataPropertyConditionAggregatedMeasure(GenericShape shape) {
        JAXBElement<TAggregatedMeasure> element = createAggregatedMeasureGeneric(shape);

        TDataPropertyConditionMeasure dataPropertyConditionMeasure = new TDataPropertyConditionMeasure();
        dataPropertyConditionMeasure.setId(SignavioUUID.generate());
        element.getValue().setDataPropertyConditionMeasure(dataPropertyConditionMeasure);

        return element;
    }

    @StencilId("stateConditionAggregatedMeasure")
    public JAXBElement<TAggregatedMeasure> createStateConditionAggregatedMeasure(GenericShape shape) {
        JAXBElement<TAggregatedMeasure> element = createAggregatedMeasureGeneric(shape);

        TStateConditionMeasure stateConditionMeasure = new TStateConditionMeasure();
        stateConditionMeasure.setId(SignavioUUID.generate());
        element.getValue().setStateConditionMeasure(stateConditionMeasure);

        return element;
    }

    @StencilId("dataAggregatedMeasure")
    public JAXBElement<TAggregatedMeasure> createDataAggregatedMeasure(GenericShape shape) {
        JAXBElement<TAggregatedMeasure> element = createAggregatedMeasureGeneric(shape);

        TDataMeasure dataMeasure = new TDataMeasure();
        dataMeasure.setId(SignavioUUID.generate());
        element.getValue().setDataMeasure(dataMeasure);

        return element;
    }

    @StencilId("derivedSingleInstanceMeasure")
    public JAXBElement<TDerivedSingleInstanceMeasure> createDerivedSingleInstanceMeasure(GenericShape shape) {
        TDerivedSingleInstanceMeasure derivedSingleInstanceMeasure = new TDerivedSingleInstanceMeasure();

        setMeasureCommon(derivedSingleInstanceMeasure, shape);
        setDerivedCommon(derivedSingleInstanceMeasure, shape);

        return factory.createDerivedSingleInstanceMeasure(derivedSingleInstanceMeasure);
    }

    @StencilId("derivedMultiInstanceMeasure")
    public JAXBElement<TDerivedMultiInstanceMeasure> createDerivedMultiInstanceMeasure(GenericShape shape) {
        TDerivedMultiInstanceMeasure derivedMultiInstanceMeasure = new TDerivedMultiInstanceMeasure();

        setMeasureCommon(derivedMultiInstanceMeasure, shape);
        setDerivedCommon(derivedMultiInstanceMeasure, shape);

        return factory.createDerivedMultiInstanceMeasure(derivedMultiInstanceMeasure);
    }
    
    
      
    
}
