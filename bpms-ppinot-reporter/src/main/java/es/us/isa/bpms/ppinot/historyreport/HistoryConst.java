package es.us.isa.bpms.ppinot.historyreport;

public class HistoryConst {
	
	// inicializa constantes utilizadas para identificar el PPI seleccionado por el usuario
	public static final int ACTIVITYSTART = 1;
	public static final int ACTIVITYEND = 2;
	public static final int POOLSTART = 3;
	public static final int POOLEND = 4;
	public static final int TIMEMEASURE = 5;
	public static final int COUNTMEASURE = 6;
	public static final int STATECONDITIONMEASURE = 7;
	public static final int DATAMEASURE = 8;
	public static final int DATAPROPERTYCONDITIONMEASURE = 9;

	public static final int TIMEMEASUREAGGR = 10;
	public static final int COUNTMEASUREAGGR = 11;
	public static final int STATECONDITIONMEASUREAGGR = 12;
	public static final int DATAMEASUREAGGR = 13;
	public static final int DATAPROPERTYCONDITIONMEASUREAGGR = 14;
	
	public static final int DERIVEDSINGLEINSTANCEMEASURE = 15;
	public static final int DERIVEDMULTIINSTANCEMEASURE = 16;
	
	// inicializa constantes utilizadas para formar los nombres de las variables post a utilizar en una aplicacion web
	public static final String PROCESSID_VARNAME = "processId";
	public static final String INSTANCEID_VARNAME = "instanceId";
	public static final String PPIID_VARNAME = "ppiId";
	
	public static final String ID_PREFIX = "id_";
	public static final String NAME_PREFIX = "name_";
	public static final String REFMAX_PREFIX = "refMax_";
	public static final String REFMIN_PREFIX = "refMin_";

	public static final String STARTEND_PREFIX = "activityIdStartEnd_";
	public static final String INI_PREFIX = "activityIdTimeIni_";
	public static final String FIN_PREFIX = "activityIdTimeFin_";
	public static final String ATENDINI_PREFIX = "atEndTimeIni_";
	public static final String ATENDFIN_PREFIX = "atEndTimeFin_";
	public static final String COUNT_PREFIX = "activityIdCount_";
	public static final String ATEND_PREFIX = "atEndCount_";
	public static final String ELEMENT_PREFIX = "taskIdElement_";
	public static final String STATEELEMENT_PREFIX = "stateIdElement_";
	public static final String DATA_PREFIX = "dataIdData_";
	public static final String DATAPROP_PREFIX = "dataPropId_";
	public static final String DATACOND_PREFIX = "dataIdDataCond_";
	public static final String DATASTATE_PREFIX = "dataStateId_";
	
	public static final String AGGR_PREFIX = "aggr_";
	public static final String AGGRINST_PREFIX = "aggr_inst_";
	public static final String AGGRFUNC_PREFIX = "aggr_func_";
	public static final String AGGRYEAR_PREFIX = "aggr_year_";
	public static final String AGGRPERIOD_PREFIX = "aggr_period_";
	public static final String STARTDATE_PREFIX = "aggr_startDate_";
	public static final String ENDDATE_PREFIX = "aggr_endDate_";
	public static final String INSTART_PREFIX = "aggr_inStart_";
	public static final String INEND_PREFIX = "aggr_inEnd_";
	
	public static final String DERFUNC_PREFIX = "funcion_";
	public static final String IDOPER1_PREFIX = "idoper1_"; 
	public static final String IDOPER2_PREFIX = "idoper2_";
	public static final String MEDOPER1_PREFIX = "medoper1_";
	public static final String MEDOPER2_PREFIX = "medoper2_";

	// constantes de funciones
	public static final String MIN = "MinAM";
	public static final String MAX = "MaxAM";
	public static final String SUM = "SumAM";
	public static final String AVE = "AverageAM";
	public static final String LAST = "LastAM";
	public static final String COUNT = "CountAM";
}
