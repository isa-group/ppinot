package es.us.isa.ppinot.handler.test;

import java.util.Map;

import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;

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

//		String nomFichOrigen = "prueba ppi.bpmn20.xml";
//		String nomFichDestino = "ppi-exportado.xml";
		
		String nomFichOrigen = "prueba single-instance-derived-aggregated.bpmn20.xml";
		String nomFichDestino = "single-instance-derived-aggregated-exportado.xml";
		
		PpiNotModelHandler ppiNotModelHandler = new PpiNotModelHandler();

		// importa el xml
		ppiNotModelHandler.load(camino, nomFichOrigen);
		
		String procId = ppiNotModelHandler.getProcId();

		Map<String, TimeInstanceMeasure> timeInstanceMeasure = ppiNotModelHandler.getTimeInstanceModelMap();
		Map<String, CountInstanceMeasure> countInstanceMeasure = ppiNotModelHandler.getCountInstanceModelMap();
		Map<String, StateConditionInstanceMeasure> elementConditionInstanceMeasure = ppiNotModelHandler.getStateConditionInstanceModelMap();
		Map<String, DataInstanceMeasure> dataInstanceMeasure = ppiNotModelHandler.getDataInstanceModelMap();
		Map<String, DataPropertyConditionInstanceMeasure> dataConditionInstanceMeasure = ppiNotModelHandler.getDataPropertyConditionInstanceModelMap();

		Map<String, AggregatedMeasure> timeAggregatedMeasure = ppiNotModelHandler.getTimeAggregatedModelMap();
		Map<String, AggregatedMeasure> countAggregatedMeasure = ppiNotModelHandler.getCountAggregatedModelMap();
		Map<String, AggregatedMeasure> elementConditionAggregatedMeasure = ppiNotModelHandler.getStateConditionAggregatedModelMap();
		Map<String, AggregatedMeasure> dataAggregatedMeasure = ppiNotModelHandler.getDataAggregatedModelMap();
		Map<String, AggregatedMeasure> dataConditionAggregatedMeasure = ppiNotModelHandler.getDataPropertyConditionAggregatedModelMap();
		Map<String, AggregatedMeasure> derivedSingleInstanceAggregatedMeasure = ppiNotModelHandler.getDerivedSingleInstanceAggregatedModelMap();

		Map<String, DerivedMeasure> derivedInstanceMeasure = ppiNotModelHandler.getDerivedSingleInstanceModelMap();
		Map<String, DerivedMeasure> derivedProcessMeasure = ppiNotModelHandler.getDerivedMultiInstanceModelMap();

		Map<String, PPI> ppi = ppiNotModelHandler.getPpiModelMap();
		
		// exporta el xml
		ppiNotModelHandler.setTimeModelMap(timeInstanceMeasure);
		ppiNotModelHandler.setCountModelMap(countInstanceMeasure);
		ppiNotModelHandler.setStateConditionModelMap(elementConditionInstanceMeasure);
		ppiNotModelHandler.setDataModelMap(dataInstanceMeasure);
		ppiNotModelHandler.setDataPropertyConditionModelMap(dataConditionInstanceMeasure);

		ppiNotModelHandler.setTimeAggregatedModelMap(timeAggregatedMeasure);
		ppiNotModelHandler.setCountAggregatedModelMap(countAggregatedMeasure);
		ppiNotModelHandler.setStateConditionAggregatedModelMap(elementConditionAggregatedMeasure);
		ppiNotModelHandler.setDataAggregatedModelMap(dataAggregatedMeasure);
		ppiNotModelHandler.setDataPropertyConditionAggregatedModelMap(dataConditionAggregatedMeasure);
		ppiNotModelHandler.setDerivedSingleInstanceAggregatedModelMap(derivedSingleInstanceAggregatedMeasure);

		ppiNotModelHandler.setDerivedSingleInstanceModelMap(derivedInstanceMeasure);
		ppiNotModelHandler.setDerivedMultiInstanceModelMap(derivedProcessMeasure);

		ppiNotModelHandler.setPpiModelMap(ppi);
		
		ppiNotModelHandler.save(camino, nomFichDestino, procId);
		
		System.out.println("Terminado");
	}
}
