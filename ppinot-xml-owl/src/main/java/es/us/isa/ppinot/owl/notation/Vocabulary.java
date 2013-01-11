package es.us.isa.ppinot.owl.notation;

public final class Vocabulary {
	
	public static final String URI = "http://www.isa.us.es/ontologies/ppinot.owl" + "#";
	
	// llevan URI
	public static final String ACTIVITYEND_URI = URI + "ActivityEnd"; 
	public static final String ACTIVITYSTART_URI = URI + "ActivityStart";
	public static final String AGGREGATEDMEASURE_URI = URI + "AggregatedMeasure";
	public static final String AGGREGATES_URI = URI + "aggregates"; 
	public static final String APPLIESTO_URI = URI + "appliesTo"; 
	public static final String COUNTMEASURE_URI = URI + "CountMeasure"; 
	public static final String CYCLICTIMEMEASURE_URI = URI + "CyclicTimeMeasure";
	public static final String DATAOUPUTASSOCIATION_URI = URI + "DataOutputAssociation";
	public static final String DATAMEASURE_URI = URI + "DataMeasure"; 
	public static final String DATAPROPERTYCONDITIONMEASURE_URI = URI + "DataPropertyConditionMeasure"; 
	public static final String DATAPROPERTY_URI = URI + "dataProperty"; 
	public static final String DEPENDSDIRECTLYON_URI = URI + "dependsDirectlyOn"; 
	public static final String DEPENDSINDIRECTLYON_URI = URI + "dependsIndirectlyOn"; 
	public static final String DERIVEDMULTIINSTANCEMEASURE_URI = URI + "DerivedMultiInstanceMeasure"; 
	public static final String DERIVEDSINGLEINSTANCEMEASURE_URI = URI + "DerivedSingleInstanceMeasure"; 
	public static final String DIRECTLYDEPENDON_URI = URI + "DirectlyDependOn";
	public static final String FROM_URI = URI + "from"; 
	public static final String FUNCTIONALPROPERTY_URI = URI + "FunctionalProperty";
	public static final String IMPLIEDBPFLOWELEMENT_URI = URI + "ImpliedBPFlowElement"; 
	public static final String IMPLIEDXORGATEWAYFOR_URI = URI + "ImpliedXorGatewayFor"; 
	public static final String INDIRECTLYDEPENDON_URI = URI + "IndirectlyDependOn";
	public static final String ISCALCULATED_URI = URI + "isCalculated"; 
	public static final String ISCOUNTIN_URI = URI + "isCountIn"; 
	public static final String ISFROMFOR_URI = URI + "isFromFor"; 
	public static final String ISGROUPEDBY_URI = URI + "isGroupedBy"; 
	public static final String ISINPATHFOR_URI = URI + "isInPathFor"; 
	public static final String ISMEASUREDIN_URI = URI + "isMeasuredIn"; 
	public static final String ISMETBY_URI = URI + "isMetBy"; 
	public static final String ISTOFOR_URI = URI + "isToFor"; 
	public static final String ISUSEDINCONDITION_URI = URI + "isUsedInCondition"; 
	public static final String GROUPS_URI = URI + "groups"; 
	public static final String LINEARTIMEMEASURE_URI = URI + "LinearTimeMeasure"; 
	public static final String MEASURESDATA_URI = URI + "measuresData"; 
	public static final String MEETS_URI = URI + "meets"; 
	public static final String PRECEDES_URI = URI + "precedes"; 
	public static final String PROCESSINSTANCECONDITION_URI = URI + "ProcessInstanceCondition";
	public static final String STATECONDITIONMEASURE_URI = URI + "StateConditionMeasure"; 
	public static final String SUCCEEDS_URI = URI + "succeeds"; 
	public static final String TIMEMEASURE_URI = URI + "TimeMeasure"; 
	public static final String TO_URI = URI + "to"; 
	public static final String WHEN_URI = URI + "when";  
	public static final String WRITERACTIVITYFOR_URI = URI + "WriterActivityFor";

	public static final String SUMAM_URI = URI + "SumAM";
	public static final String MINAM_URI = URI + "MinAM";
	public static final String MAXAM_URI = URI + "MaxAM";
	public static final String AVGAM_URI = URI + "AvgAM";
	
	// son partes de una frase en el owl
	public static final String ACTIVITY = "Activity"; 
	public static final String DATASTATECHANGE = "DataStateChange"; 
	public static final String END = "end"; 
	public static final String EVENTTRIGGER = "EventTrigger"; 
	public static final String INTERMEDIATE1 = "Intermediate1";
	public static final String RESTRICTION = "Restriction"; 
	public static final String START = "start"; 
	public static final String TIMEINSTANCE = "TimeInstance"; 
	public static final String TIMEINSTANT = "TimeInstant";
	
	// funciones de agregacion en el xml
	public static final String SUM = "Sum"; 
	public static final String MINIMUM = "Minimum"; 
	public static final String MAXIMUM = "Maximum"; 
	public static final String AVERAGE = "Average"; 
}
