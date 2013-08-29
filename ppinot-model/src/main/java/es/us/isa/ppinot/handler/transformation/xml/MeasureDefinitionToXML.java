package es.us.isa.ppinot.handler.transformation.xml;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * MeasureDefinitionToXML
 *
 * @author resinas
 */
public interface MeasureDefinitionToXML<M extends MeasureDefinition> {
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors);
}
