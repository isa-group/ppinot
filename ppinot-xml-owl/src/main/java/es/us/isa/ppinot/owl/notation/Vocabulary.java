package es.us.isa.ppinot.owl.notation;

/**
 * Clase con el vocabulario utilizado en una ontolog�a PPINOT
 * 
 * @author Edelia
 *
 */
public final class Vocabulary {
	
	public static final String URI = "http://www.isa.us.es/ontologies/ppinot.owl";
	
	private static final String URINT = URI + "#";
	
	// llevan URI
	public static final String ACTIVITYEND_URI = URINT + "ActivityEnd"; 
	public static final String ACTIVITYSTART_URI = URINT + "ActivityStart";
	public static final String AGGREGATEDMEASURE_URI = URINT + "AggregatedMeasure";
	public static final String AGGREGATES_URI = URINT + "aggregates"; 
	public static final String APPLIESTO_URI = URINT + "appliesTo";
    public static final String CHANGESTOSTATE_URI = URINT + "changesToState";
	public static final String COUNTMEASURE_URI = URINT + "CountMeasure"; 
	public static final String CYCLICTIMEMEASURE_URI = URINT + "CyclicTimeMeasure";
	public static final String DATA_URI = URINT + "data";
	public static final String DATACONTENTSELECTION_URI = URINT + "DataContentSelection";
	public static final String DATAOUPUTASSOCIATION_URI = URINT + "DataOutputAssociation";
	public static final String DATAMEASURE_URI = URINT + "DataMeasure"; 
	public static final String DATAPROPERTYCONDITION_URI = URINT + "DataPropertyCondition"; 
	public static final String DATAPROPERTYCONDITIONMEASURE_URI = URINT + "DataPropertyConditionMeasure"; 
	public static final String DATAPROPERTY_URI = URINT + "dataProperty"; 
	public static final String DEFINITION_URI = URINT + "definition"; 
	public static final String DEPENDSDIRECTLYON_URI = URINT + "dependsDirectlyOn"; 
	public static final String DEPENDSINDIRECTLYON_URI = URINT + "dependsIndirectlyOn"; 
	public static final String DERIVEDMULTIINSTANCEMEASURE_URI = URINT + "DerivedMultiInstanceMeasure"; 
	public static final String DERIVEDSINGLEINSTANCEMEASURE_URI = URINT + "DerivedSingleInstanceMeasure"; 
	public static final String DIRECTLYDEPENDON_URI = URINT + "DirectlyDependOn";
	public static final String FROM_URI = URINT + "from"; 
	public static final String FUNCTIONALPROPERTY_URI = URINT + "FunctionalProperty";
	public static final String IMPLIEDBPFLOWELEMENT_URI = URINT + "ImpliedBPFlowElement"; 
	public static final String IMPLIEDXORGATEWAYFOR_URI = URINT + "ImpliedXorGatewayFor"; 
	public static final String INDIRECTLYDEPENDON_URI = URINT + "IndirectlyDependOn";
	public static final String ISCALCULATED_URI = URINT + "isCalculated"; 
	public static final String ISCOUNTIN_URI = URINT + "isCountIn"; 
	public static final String ISFROMFOR_URI = URINT + "isFromFor"; 
	public static final String ISGROUPEDBY_URI = URINT + "isGroupedBy"; 
	public static final String ISINPATHFOR_URI = URINT + "isInPathFor"; 
	public static final String ISMEASUREDIN_URI = URINT + "isMeasuredIn"; 
	public static final String ISMETBY_URI = URINT + "isMetBy"; 
	public static final String ISTOFOR_URI = URINT + "isToFor"; 
	public static final String ISUSEDINCONDITION_URI = URINT + "isUsedInCondition"; 
	public static final String GROUPS_URI = URINT + "groups"; 
	public static final String LINEARTIMEMEASURE_URI = URINT + "LinearTimeMeasure"; 
	public static final String MEASURESDATA_URI = URINT + "measuresData"; 
	public static final String MEETS_URI = URINT + "meets"; 
	public static final String PPI_URI = URINT + "PPI"; 
	public static final String PRECEDES_URI = URINT + "precedes"; 
	public static final String PROCESSINSTANCECONDITION_URI = URINT + "ProcessInstanceCondition";
	public static final String STATECONDITION_URI = URINT + "StateCondition"; 
	public static final String STATECONDITIONMEASURE_URI = URINT + "StateConditionMeasure"; 
	public static final String SUCCEEDS_URI = URINT + "succeeds";
    public static final String STATE_URI = URINT + "state";
    public static final String TIMEMEASURE_URI = URINT + "TimeMeasure";
    public static final String TO_URI = URINT + "to"; 
	public static final String WHEN_URI = URINT + "when";  
	public static final String WRITERACTIVITYFOR_URI = URINT + "WriterActivityFor";

	public static final String SUMAM_URI = URINT + "SumAM";
	public static final String MINAM_URI = URINT + "MinAM";
	public static final String MAXAM_URI = URINT + "MaxAM";
	public static final String AVGAM_URI = URINT + "AvgAM";
	
	// son partes de una frase en el owl
	public static final String ACTIVITY = "Activity"; 
	public static final String DATASTATECHANGE = "DataStateChange"; 
	public static final String DCSELECTION = "DCSelection";
	public static final String END = "end"; 
	public static final String EVENTTRIGGER = "EventTrigger"; 
	public static final String INTERMEDIATE1 = "Intermediate1";
	public static final String RESTRICTION = "Restriction"; 
	public static final String START = "start"; 
	public static final String TIMEINSTANT = "TimeInstant";
	
	// funciones de agregacion en el xml
	public static final String SUM = "Sum"; 
	public static final String MINIMUM = "Minimum"; 
	public static final String MAXIMUM = "Maximum"; 
	public static final String AVERAGE = "Average"; 
}
