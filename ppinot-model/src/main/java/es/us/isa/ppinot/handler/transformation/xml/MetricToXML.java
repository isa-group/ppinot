package es.us.isa.ppinot.handler.transformation.xml;

import java.util.Map;

import es.us.isa.ppinot.handler.transformation.ConnectorsBuilder;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.base.Metric;
import es.us.isa.ppinot.xml.TMetric;
import es.us.isa.ppinot.xml.TMeasure;


public class MetricToXML extends AbstractMeasureToXML implements MeasureDefinitionToXML<Metric> {

	//ADAPTADO DE DerivedSingleInstanceMeasure
	private MeasureDefinitionToXML<MeasureDefinition> converter;
	
	//ADAPTADO DE DerivedSingleInstanceMeasure
	public MetricToXML(MeasureDefinitionToXML<MeasureDefinition> converter) {
        this.converter = converter;
    }
	
	
	@Override
    public TMeasure create(MeasureDefinition measure, ConnectorsBuilder connectors) {
        
		System.out.println(">>>>>>>>>>>>Entro en clase MetricToXML\n");
		
		TMetric xml = new TMetric();
        loadBaseAttributes(measure, xml);

        Metric metric = (Metric) measure;

        //ESTA NECESITA ALGO MÁS!!! -- LOS CONECTORES
        //connectors.addDataCondition(dataMeasure.getPrecondition(), dataMeasure.getDataContentSelection(), xml);      
        
        //ADAPTADO DE DerivedSingleInstanceMeasure
        Map<String, MeasureDefinition> uses = metric.getUsedMeasureMap();

        for (String var : uses.keySet()) {
            TMeasure used = converter.create(uses.get(var), connectors);
            connectors.addUses(var, used, xml);
            System.out.println("--->VAR["+var+"] - USED["+used+"] XML["+xml+"]");
        }
        
        return xml;
    }
	
}

