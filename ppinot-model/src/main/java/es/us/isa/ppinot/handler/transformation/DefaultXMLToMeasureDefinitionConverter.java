package es.us.isa.ppinot.handler.transformation;

import es.us.isa.ppinot.handler.transformation.model.*;
import es.us.isa.ppinot.handler.transformation.xml.*;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.xml.TMeasure;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DefaultXMLToMeasureDefinitionConverter
 *
 * @author resinas
 */
public class DefaultXMLToMeasureDefinitionConverter implements XMLToMeasureDefinition<TMeasure>, MeasureDefinitionToXML<MeasureDefinition> {
    private Map<Class<? extends TMeasure>, XMLToMeasureDefinition<? extends TMeasure>> xmlConverters;
    private Map<Class<? extends MeasureDefinition>, MeasureDefinitionToXML<? extends MeasureDefinition>> measureConverters;

    public DefaultXMLToMeasureDefinitionConverter() {
        xmlConverters = new HashMap<Class<? extends TMeasure>, XMLToMeasureDefinition<? extends TMeasure>>();
        measureConverters = new HashMap<Class<? extends MeasureDefinition>, MeasureDefinitionToXML<? extends MeasureDefinition>>();
        addConverter(new XMLToTimeMeasure());
        addConverter(new XMLToStateConditionMeasure());
        addConverter(new XMLToDataPropertyConditionMeasure());
        addConverter(new XMLToCountMeasure());
        addConverter(new XMLToTimeInstantMeasure());
        addConverter(new XMLToDataMeasure());
        addConverter(new XMLToAggregatedMeasure(this));
        addConverter(new XMLToDerivedSingleInstanceMeasure(this));
        addConverter(new XMLToDerivedMultiInstanceMeasure(this));
        addConverter(new TimeMeasureToXML());
        addConverter(new StateConditionMeasureToXML());
        addConverter(new DataPropertyConditionMeasureToXML());
        addConverter(new CountMeasureToXML());
        addConverter(new DataMeasureToXML());
        addConverter(new AggregatedMeasureToXML(this));
        addConverter(new DerivedSingleInstanceMeasureToXML(this));
        addConverter(new DerivedMultiInstanceMeasureToXML(this));
        
    }

    private void addConverter(Object converter) {
        for (Type c : converter.getClass().getGenericInterfaces()) {
            if (c instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) c;
                if (pt.getRawType().equals(XMLToMeasureDefinition.class)) {
                    xmlConverters.put((Class<? extends TMeasure>) pt.getActualTypeArguments()[0],
                            (XMLToMeasureDefinition<? extends TMeasure>) converter);
                }
                else if (pt.getRawType().equals(MeasureDefinitionToXML.class)) {
                    measureConverters.put((Class<? extends MeasureDefinition>) pt.getActualTypeArguments()[0],
                            (MeasureDefinitionToXML<? extends MeasureDefinition>) converter);
                }
            }
        }
    }

    public DefaultXMLToMeasureDefinitionConverter(List<Object> additionalConverters) {
        this();
        for (Object converter : additionalConverters) {
            addConverter(converter);
        }
    }

    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        XMLToMeasureDefinition<? extends TMeasure> converter = xmlConverters.get(xml.getClass());
        if (converter == null)
            throw new RuntimeException("Measure not supported: " + xml.getClass());
        return converter.create(xml, connectors);
    }

    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        MeasureDefinitionToXML<? extends MeasureDefinition> converter = measureConverters.get(measure.getClass());
        if (converter == null)
            throw new RuntimeException("Measure not supported: " + measure.getClass());
        return converter.create(measure, connectors);
    }
}
