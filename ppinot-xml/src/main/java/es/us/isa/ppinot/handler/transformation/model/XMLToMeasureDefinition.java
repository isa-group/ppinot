package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.xml.TMeasure;

/**
 * XMLToMeasureDefinition
 *
 * @author resinas
 */
public interface XMLToMeasureDefinition<X extends TMeasure> {
    MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors);
}
