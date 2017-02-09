package es.us.isa.ppinot.handler.transformation.model;

import java.util.List;

import es.us.isa.ppinot.handler.transformation.ConnectorsHandler;
//import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.Metric;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xml.TDerivedSingleInstanceMeasure;
//import es.us.isa.ppinot.model.condition.DataPropertyCondition;
//import es.us.isa.ppinot.xml.TAppliesToDataConnector;
import es.us.isa.ppinot.xml.TMetric;
import es.us.isa.ppinot.xml.TMeasure;
import es.us.isa.ppinot.xml.TUses;


public class XMLToMetric implements XMLToMeasureDefinition <TMetric>{
	
	//COPIADO DE XMLToDerivedSingleInstanceMeasure
	private XMLToMeasureDefinition conversor;

	//COPIADO DE XMLToDerivedSingleInstanceMeasure
    public XMLToMetric(XMLToMeasureDefinition conversor) {
        this.conversor = conversor;
    }
		
	@Override
    public MeasureDefinition create(TMeasure xml, ConnectorsHandler connectors) {
		
		List<TUses> usesList = connectors.findConnectors(xml, TUses.class);
		TMetric measure = (TMetric) xml;
		
		Metric def = new Metric(
                xml.getId(),
                xml.getName(),
                xml.getDescription(),
                xml.getScale(),
                xml.getUnitOfMeasure()
                );
		
		for (TUses uses : usesList) {
            MeasureDefinition used = conversor.create(connectors.getTargetMeasure(uses), connectors);
            def.addUsedMeasure(uses.getVariable(), used);
        }
		
		return def;
		
		//*************************************************************************************************
        //TAppliesToDataConnector connector = connectors.findConnector(xml, TAppliesToDataConnector.class);
        //DataContentSelection selection = connectors.createDataContentSelection(connector);
        //DataPropertyCondition condition = connectors.createDataCondition(connector);
        //return new Metric(
        //        xml.getId(),
        //        xml.getName(),
        //        xml.getDescription(),
        //         xml.getScale(),
        //        xml.getUnitOfMeasure()
                //,
                //selection,
                //condition
        //);
    }
}

