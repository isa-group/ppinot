package es.us.isa.ppinot.handler.transformation.model;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xml.TDerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TUses;

import java.util.List;

/**
 * XMLToDerivedSingleInstanceMeasure
 *
 * @author resinas
 */
public class XMLToDerivedSingleInstanceMeasure implements XMLToMeasureDefinition<TDerivedSingleInstanceMeasure> {
    private XMLToMeasureDefinition conversor;

    public XMLToDerivedSingleInstanceMeasure(XMLToMeasureDefinition conversor) {
        this.conversor = conversor;
    }

    @Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
        List<TUses> usesList = connectors.findConnectors(xml, TUses.class);
        TDerivedSingleInstanceMeasure measure = (TDerivedSingleInstanceMeasure) xml;

        DerivedSingleInstanceMeasure def = new DerivedSingleInstanceMeasure(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure(),
                measure.getFunction());

        for (TUses uses : usesList) {
            MeasureDefinition used = conversor.create(connectors.getTargetMeasure(uses), connectors);
            def.addUsedMeasure(uses.getVariable(), used);
        }

        return def;
    }

}
