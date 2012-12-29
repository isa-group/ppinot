package es.us.isa.ppinot.bpmnppinot_xml_owl.notation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class Translator {
	
	static private Map<Enum, String> vocabularyMap;
	
	static {
		
		vocabularyMap = new HashMap<Enum, String>();
		vocabularyMap.put(Vocabulary.ACTIVITYEND, "ActivityEnd");
		vocabularyMap.put(Vocabulary.ACTIVITYSTART, "ActivityStart");
		vocabularyMap.put(Vocabulary.AGGREGATEDMEASURE, "AggregatedMeasure");
		vocabularyMap.put(Vocabulary.AGGREGATES, "aggregates");
		vocabularyMap.put(Vocabulary.APPLIESTO, "appliesTo");
		vocabularyMap.put(Vocabulary.COUNTMEASURE, "CountMeasure");
		vocabularyMap.put(Vocabulary.CYCLICTIMEMEASURE, "CyclicTimeMeasure");
		vocabularyMap.put(Vocabulary.DATAOUPUTASSOCIATION, "DataOutputAssociation");
		vocabularyMap.put(Vocabulary.DATAMEASURE, "DataMeasure");
		vocabularyMap.put(Vocabulary.DATAPROPERTY, "dataProperty");
		vocabularyMap.put(Vocabulary.DATAPROPERTYCONDITIONMEASURE, "DataPropertyConditionMeasure");
		vocabularyMap.put(Vocabulary.DEPENDSDIRECTLYON, "dependsDirectlyOn");
		vocabularyMap.put(Vocabulary.DEPENDSINDIRECTLYON, "dependsIndirectlyOn");
		vocabularyMap.put(Vocabulary.DERIVEDMULTIINSTANCEMEASURE, "DerivedMultiInstanceMeasure");
		vocabularyMap.put(Vocabulary.DERIVEDSINGLEINSTANCEMEASURE, "DerivedSingleInstanceMeasure");
		vocabularyMap.put(Vocabulary.DIRECTLYDEPENDON, "DirectlyDependOn");
		vocabularyMap.put(Vocabulary.END, "end");
		vocabularyMap.put(Vocabulary.FROM, "from");
		vocabularyMap.put(Vocabulary.FUNCTIONALPROPERTY, "FunctionalProperty");
		vocabularyMap.put(Vocabulary.GROUPS, "groups");
		vocabularyMap.put(Vocabulary.IMPLIEDBPFLOWELEMENT, "ImpliedBPFlowElement");
		vocabularyMap.put(Vocabulary.IMPLIEDXORGATEWAYFOR, "ImpliedXorGatewayFor");
		vocabularyMap.put(Vocabulary.INDIRECTLYDEPENDON, "IndirectlyDependOn");
		vocabularyMap.put(Vocabulary.INTERMEDIATE1, "Intermediate1");
		vocabularyMap.put(Vocabulary.ISCALCULATED, "isCalculated");
		vocabularyMap.put(Vocabulary.ISCOUNTIN, "isCountIn");
		vocabularyMap.put(Vocabulary.ISFROMFOR, "isFromFor");
		vocabularyMap.put(Vocabulary.ISGROUPEDBY, "isGroupedBy");
		vocabularyMap.put(Vocabulary.ISINPATHFOR, "isInPathFor");
		vocabularyMap.put(Vocabulary.ISMEASUREDIN, "isMeasuredIn");
		vocabularyMap.put(Vocabulary.ISMETBY, "isMetBy");
		vocabularyMap.put(Vocabulary.ISTOFOR, "isToFor");
		vocabularyMap.put(Vocabulary.ISUSEDINCONDITION, "isUsedInCondition");
		vocabularyMap.put(Vocabulary.LINEARTIMEMEASURE, "LinearTimeMeasure");
		vocabularyMap.put(Vocabulary.MEASURESDATA, "measuresData");
		vocabularyMap.put(Vocabulary.MEETS, "meets");
		vocabularyMap.put(Vocabulary.PRECEDES, "precedes");
		vocabularyMap.put(Vocabulary.PROCESSINSTANCECONDITION, "ProcessInstanceCondition");
		vocabularyMap.put(Vocabulary.START, "start");
		vocabularyMap.put(Vocabulary.STATECONDITIONMEASURE, "StateConditionMeasure");
		vocabularyMap.put(Vocabulary.SUCCEEDS, "succeeds");
		vocabularyMap.put(Vocabulary.TIMEINSTANCE, "TimeInstance");
		vocabularyMap.put(Vocabulary.TIMEINSTANT, "TimeInstant");
		vocabularyMap.put(Vocabulary.TIMEMEASURE, "TimeMeasure");
		vocabularyMap.put(Vocabulary.TO, "to");
		vocabularyMap.put(Vocabulary.WHEN, "when");
		vocabularyMap.put(Vocabulary.WRITERACTIVITYFOR, "WriterActivityFor");
		
	}

	public static Boolean equals(String word, Enum token) {
		
		Boolean b = false;
		
		Iterator<Entry<Enum, String>> itInst = vocabularyMap.entrySet().iterator();
	    while (!b && itInst.hasNext()) {
	        Map.Entry<Enum, String> pairs = (Map.Entry<Enum, String>)itInst.next();
	        Enum key = (Enum) pairs.getKey();
	        String value = (String) pairs.getValue();
	        
	        b = word.toLowerCase().contentEquals(value.toLowerCase()) && token==key;
	    }
		return b;
	}
	
	public static String translate(Enum token) {
		
		return vocabularyMap.get(token);
	}
}
