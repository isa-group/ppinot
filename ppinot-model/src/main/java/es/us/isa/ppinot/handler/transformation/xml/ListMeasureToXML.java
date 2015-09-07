package es.us.isa.ppinot.handler.transformation.xml;

import java.util.Map;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.composite.ListMeasure;
//import es.us.isa.ppinot.xml.TListMeasure;
import es.us.isa.ppinot.xml.TMeasure;

public class ListMeasureToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<ListMeasure> {

	private MeasureDefinitionToXML<MeasureDefinition> converter;
	
	public ListMeasureToXML(MeasureDefinitionToXML<MeasureDefinition> converter) {
        this.converter = converter;
    }
	
	@Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        /*TListMeasure xml = new TListMeasure();
        loadBaseAttributes(measure, xml);

        ListMeasure listMeasure = (ListMeasure)measure;
        
        Map<String, MeasureDefinition> uses = listMeasure.getUsedMeasureMap();

        for (String var : uses.keySet()) {
            TMeasure used = converter.create(uses.get(var), connectors);
            connectors.addUses(var, used, xml);
        }*/

        return null; //return xml;
    }
}
