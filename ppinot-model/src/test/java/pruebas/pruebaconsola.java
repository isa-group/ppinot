package pruebas;

import java.util.List;

import es.us.isa.isabpm.ppinot.model.PPI;
import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.isabpm.ppinot.xmlExtracter.PpiNotXmlExtracter;

public class pruebaconsola {

	public static void main(String[] args) throws Exception {
		
		String camino = "D:\\eclipse-appweb-indigo\\repository_isa\\";
		
//		String nomFichOrigen = "prueba base.bpmn20.xml";
//		String nomFichDestino = "base-exportado.xml";

//		String nomFichOrigen = "prueba aggregated.bpmn20.xml";
//		String nomFichDestino = "aggregated-exportado.xml";

//		String nomFichOrigen = "prueba derived.bpmn20.xml";
//		String nomFichDestino = "derived-exportado.xml";

//		String nomFichOrigen = "prueba lanes.bpmn20.xml";
//		String nomFichDestino = "lanes-exportado.xml";

//		String nomFichOrigen = "prueba isgroupedby connector.bpmn20.xml";
//		String nomFichDestino = "isgroupedby connector-exportado.xml";

//		String nomFichOrigen = "prueba aggregated connector.bpmn20.xml";
//		String nomFichDestino = "aggregated connector-exportado.xml";

		String nomFichOrigen = "prueba ppi.bpmn20.xml";
		String nomFichDestino = "ppi-exportado.xml";
		
		PpiNotXmlExtracter ppiNotXmlExtracter = new PpiNotXmlExtracter();

		// importa el xml
		ppiNotXmlExtracter.unmarshall(camino, nomFichOrigen);
		
		String procId = ppiNotXmlExtracter.getProcId();

		List<TimeInstanceMeasure> timeInstanceMeasure = ppiNotXmlExtracter.getTimeInstanceMeasure();
		List<CountInstanceMeasure> countInstanceMeasure = ppiNotXmlExtracter.getCountInstanceMeasure();
		List<StateConditionInstanceMeasure> elementConditionInstanceMeasure = ppiNotXmlExtracter.getStateConditionInstanceMeasure();
		List<DataInstanceMeasure> dataInstanceMeasure = ppiNotXmlExtracter.getDataInstanceMeasure();
		List<DataPropertyConditionInstanceMeasure> dataConditionInstanceMeasure = ppiNotXmlExtracter.getDataPropertyConditionInstanceMeasure();

		List<AggregatedMeasure> timeAggregatedMeasure = ppiNotXmlExtracter.getTimeAggregatedMeasure();
		List<AggregatedMeasure> countAggregatedMeasure = ppiNotXmlExtracter.getCountAggregatedMeasure();
		List<AggregatedMeasure> elementConditionAggregatedMeasure = ppiNotXmlExtracter.getStateConditionAggregatedMeasure();
		List<AggregatedMeasure> dataAggregatedMeasure = ppiNotXmlExtracter.getDataAggregatedMeasure();
		List<AggregatedMeasure> dataConditionAggregatedMeasure = ppiNotXmlExtracter.getDataPropertyConditionAggregatedMeasure();

		List<DerivedSingleInstanceMeasure> derivedInstanceMeasure = ppiNotXmlExtracter.getDerivedSingleInstanceMeasure();
		List<DerivedMultiInstanceMeasure> derivedProcessMeasure = ppiNotXmlExtracter.getDerivedMultiInstanceMeasure();

		List<PPI> ppi = ppiNotXmlExtracter.getPpiModel();
		
		// exporta el xml
		ppiNotXmlExtracter.setTimeMeasure(timeInstanceMeasure);
		ppiNotXmlExtracter.setCountMeasure(countInstanceMeasure);
		ppiNotXmlExtracter.setStateConditionMeasure(elementConditionInstanceMeasure);
		ppiNotXmlExtracter.setDataMeasure(dataInstanceMeasure);
		ppiNotXmlExtracter.setDataPropertyConditionMeasure(dataConditionInstanceMeasure);

		ppiNotXmlExtracter.setTimeAggregatedMeasure(timeAggregatedMeasure);
		ppiNotXmlExtracter.setCountAggregatedMeasure(countAggregatedMeasure);
		ppiNotXmlExtracter.setStateConditionAggregatedMeasure(elementConditionAggregatedMeasure);
		ppiNotXmlExtracter.setDataAggregatedMeasure(dataAggregatedMeasure);
		ppiNotXmlExtracter.setDataPropertyConditionAggregatedMeasure(dataConditionAggregatedMeasure);

		ppiNotXmlExtracter.setDerivedSingleInstanceMeasure(derivedInstanceMeasure);
		ppiNotXmlExtracter.setDerivedMultiInstanceMeasure(derivedProcessMeasure);

		ppiNotXmlExtracter.setPpiModel(ppi);
		
		ppiNotXmlExtracter.marshall(camino, nomFichDestino, procId);
		
		System.out.println("Terminado");
	}
}
