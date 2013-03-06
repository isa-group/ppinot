package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.handler.ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExtensionElements;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.model.*;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregatedMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregates;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToDataConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToElementConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TCountMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataPropertyConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedMultiInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TIsGroupedBy;
import es.us.isa.ppinot.xmlClasses.ppinot.TMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TMeasureConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TPpi;
import es.us.isa.ppinot.xmlClasses.ppinot.TPpiset;
import es.us.isa.ppinot.xmlClasses.ppinot.TStateConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TUses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;


/**
 * Clase que permite exportar e importar a XMLs de PPINOT. 
 * 
 * @author Edelia
 *
 */
public class PpiNotModelHandler extends ModelHandler implements PpiNotModelHandlerInterface {

	// objeto factory para generar instancias de clases Jaxb de BPMN 2.0
	private es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory bpmnFactory;
	
	// mapas con las instancias de las clases Jaxb, obtenidas de importar un xml. La key es el id del elemento. 
	private Map<String, TCountMeasure> countMap;
	private Map<String, TTimeMeasure> timeMap;
	private Map<String, TStateConditionMeasure> stateConditionMap;
	private Map<String, TDataMeasure> dataMap;
	private Map<String, TDataPropertyConditionMeasure> dataPropertyConditionMap;

	private Map<String, TAggregatedMeasure> aggregatedMap;

	private Map<String, TDerivedSingleInstanceMeasure> derivedSingleInstanceMap;
	private Map<String, TDerivedMultiInstanceMeasure> derivedMultiInstanceMap;

	private Map<String, TAppliesToElementConnector> appliesToElementConnectorMap;
	private Map<String, TAppliesToDataConnector> appliesToDataConnectorMap;
	private Map<String, TTimeConnector> timeConnectorMap;
	private Map<String, TUses> usesMap;
	private Map<String, TAggregates> aggregatesMap;
	private Map<String, TIsGroupedBy> isGroupedByMap;

	private Map<String, TTask> taskMap;
	private Map<String, TDataObject> dataobjectMap;

	private List<TPpi> ppiList;

	// mapas con las instancias de las clases del modelo, obtenidas a partir de las clases Jaxb. La key es el id del elemento. 
	private Map<String, TimeInstanceMeasure> timeInstanceModelMap;
	private Map<String, CountInstanceMeasure> countInstanceModelMap;
	private Map<String, StateConditionInstanceMeasure> stateConditionInstanceModelMap;
	private Map<String, DataInstanceMeasure> dataInstanceModelMap;
	private Map<String, DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceModelMap;

	private Map<String, AggregatedMeasure> timeAggregatedModelMap;
	private Map<String, AggregatedMeasure> countAggregatedModelMap;
	private Map<String, AggregatedMeasure> stateConditionAggregatedModelMap;
	private Map<String, AggregatedMeasure> dataAggregatedModelMap;
	private Map<String, AggregatedMeasure> dataPropertyConditionAggregatedModelMap;
	private Map<String, AggregatedMeasure> derivedSingleInstanceAggregatedModelMap;

	private Map<String, DerivedMeasure> derivedSingleInstanceModelMap;
	private Map<String, DerivedMeasure> derivedMultiInstanceModelMap;

	private Map<String, PPI> ppiModelMap;
	
	// objeto para obtener instancias de clases del modelo a partir de instancias de clases Jaxb
	private GeneratePpiNotModel generatePpiNotModel;
	// objeto para obtener instancias de clases Jabx a partir de instancias de clases del modelo
	private GeneratePpiNotInfo generatePpiNotInfo;
	
	/**
	 * Constructor de la clase
	 * 
	 * @throws JAXBException
	 */
	public PpiNotModelHandler() throws JAXBException {

		super();
	}

	/**
	 * Realiza las inicializaciones. 
	 * 
	 * @throws JAXBException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void iniLoader() throws JAXBException {
		
		countMap = new HashMap<String, TCountMeasure>();
		timeMap = new HashMap<String, TTimeMeasure>();
		stateConditionMap = new HashMap<String, TStateConditionMeasure>();
		dataMap = new HashMap<String, TDataMeasure>();
		dataPropertyConditionMap = new HashMap<String, TDataPropertyConditionMeasure>();

		aggregatedMap = new HashMap<String, TAggregatedMeasure>();

		derivedSingleInstanceMap = new HashMap<String, TDerivedSingleInstanceMeasure>();
		derivedMultiInstanceMap = new HashMap<String, TDerivedMultiInstanceMeasure>();

		appliesToElementConnectorMap = new HashMap<String, TAppliesToElementConnector>();
		appliesToDataConnectorMap = new HashMap<String, TAppliesToDataConnector>();
		timeConnectorMap = new HashMap<String, TTimeConnector>();
		usesMap = new HashMap<String, TUses>();
		aggregatesMap = new HashMap<String, TAggregates>();
		isGroupedByMap = new HashMap<String, TIsGroupedBy>();

		taskMap = new HashMap<String, TTask>();
		dataobjectMap = new HashMap<String, TDataObject>();

		timeInstanceModelMap = new HashMap<String, TimeInstanceMeasure>();
		countInstanceModelMap = new HashMap<String, CountInstanceMeasure>();
		stateConditionInstanceModelMap = new HashMap<String, StateConditionInstanceMeasure>();
		dataInstanceModelMap = new HashMap<String, DataInstanceMeasure>();
		dataPropertyConditionInstanceModelMap = new HashMap<String, DataPropertyConditionInstanceMeasure>();

		timeAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		countAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		stateConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		dataAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		dataPropertyConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure>();
		derivedSingleInstanceAggregatedModelMap = new HashMap<String, AggregatedMeasure>();

		derivedSingleInstanceModelMap = new HashMap<String, DerivedMeasure>();
		derivedMultiInstanceModelMap = new HashMap<String, DerivedMeasure>();

		ppiModelMap = new HashMap<String, PPI>();

		ppiList = new ArrayList<TPpi>();

		// configura las clases para leer y guardar como xml
		Class[] classList = {es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class, 
				es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory.class, 
				es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class};
		this.xmlConfig( classList, es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory.class );
        
        this.bpmnFactory = new es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory();
        
		this.generatePpiNotModel = new GeneratePpiNotModel();
		
		this.generatePpiNotInfo = new GeneratePpiNotInfo();
	}
	
	/**
	 * Devuelve la factory utilizada 
	 * 
	 * @return Objeto factory
	 */
	protected ObjectFactory getFactory() {
	
		return (ObjectFactory) super.getFactory();
	}
	
	/**
	 * Devuelve el id del proceso involucrado en el xml
	 * 
	 * @return Id del proceso
	 */
	public String getProcId() {
		
		TProcess process = null;
		
		// obtiene el objeto raiz en TDefinition
		Object object = ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		if (object instanceof TProcess) {
			
			// si el objeto obtenido es un proceso
			process = (TProcess) object;
		} else {
			
			// si el objeto obtenido no es un proceso, se obtiene el primer proceso
			for (JAXBElement<?> element : ((TDefinitions) this.getImportElement().getValue()).getRootElement()) {
				
				Object participant = element.getValue();
				if (participant instanceof TProcess &&
					((TProcess) participant).getExtensionElements()!=null &&
					((TProcess) participant).getExtensionElements().getAny()!=null &&
					((TProcess) participant).getExtensionElements().getAny().get(0)!=null &&
					((JAXBElement<?>) ((TProcess) participant).getExtensionElements().getAny().get(0)).getValue() instanceof TPpiset
				   ) {
					
					process = (TProcess) participant;
					break;
				}
			}
		}
		
		// obtiene el id del proceso
		return (process==null)?"":process.getId();
	}
	
	/**
	 * Devuelve el mapa de CountInstanceMeasure
	 */
	public Map<String, CountInstanceMeasure> getCountInstanceModelMap() {
		
		return countInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de TimeInstanceMeasure
	 */
	public Map<String, TimeInstanceMeasure> getTimeInstanceModelMap() {

		return timeInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de StateConditionInstanceMeasure
	 */
	public Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap() {
		
		return stateConditionInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de DataInstanceMeasure
	 */
	public Map<String, DataInstanceMeasure> getDataInstanceModelMap() {
		
		return dataInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de DataPropertyConditionInstanceMeasure
	 */
	public Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap() {
		
		return dataPropertyConditionInstanceModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una TimeInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getTimeAggregatedModelMap() {

		return timeAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una CountInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getCountAggregatedModelMap() {

		return countAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una StateConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap() {

		return stateConditionAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataAggregatedModelMap() {

		return dataAggregatedModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DataPropertyConditionInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap() {

		return dataPropertyConditionAggregatedModelMap;
	}
	
	/**
	 * Devuelve el mapa de las medidas agregadas que involucran una DerivedSingleInstanceMeasure
	 */
	public Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap() {

		return derivedSingleInstanceAggregatedModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas DerivedSingleInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedSingleInstanceModelMap() {
		
		return derivedSingleInstanceModelMap;
	}

	/**
	 * Devuelve el mapa de las medidas DerivedMultiInstanceMeasure
	 */
	public Map<String, DerivedMeasure> getDerivedMultiInstanceModelMap() {
		
		return derivedMultiInstanceModelMap;
	}
	
	/**
	 * Devuelve el mapa de los PPI
	 */
	public Map<String, PPI> getPpiModelMap() {
		
		return ppiModelMap;
	}
	
	/**
	 * Genera el valor de la propiedad analisysPeriod a partir de la informacion del periodo de analisis
	 * 
	 * @param year Año
	 * @param period Si se desea los resultados por mes, trimestre o semestre
	 * @param startDate Fecha inicial del periodo de analisis
	 * @param endDate Fecha final del periodo de analisis
	 * @param inStart Si se incluye en el analisis los procesos que se inician antes del inicio del periodo y terminan despues de este 
	 * @param inEnd Si se incluye en el analisis los procesos que se inician antes del final del periodo y terminan despues de este
	 * @return
	 */
	private String generateAnalisysPeriod(String year, String period, Date startDate, Date endDate, Boolean inStart, Boolean inEnd) {
		
		String startDateString = PpiNotModelUtils.formatString(startDate);
		String endDateString = PpiNotModelUtils.formatString(endDate);
		String inStartString = (inStart)?"true":"false";
		String inEndString = (inEnd)?"true":"false";

		String analisysPeriod = "";
		
		if (!startDateString.contentEquals("") || !endDateString.contentEquals("")) {
			
			analisysPeriod = "interval(" + startDateString + "," + endDateString + "," + inStartString + "," + inEndString + ")";
		} else 
		if (!year.contentEquals("")) {
			
			analisysPeriod = "period(" + year + "," + period + "," + inStartString + "," + inEndString + ")";
		}
		
		return analisysPeriod;
	}
	
	/**
	 * Genera el valor de la propiedad target de un ppi, a partir de los valores de referencia de este
	 * 
	 * @param refMin Valor minimo que deberia tomar la medida en el ppi
	 * @param refMax Valor maximo que deberia tomar la medida en el ppi
	 * @return Valor de la propiedad target del ppi
	 */
	private String generateTarget(Double refMin, Double refMax) {
		
		String target = "";
		
		if (refMax!=null && refMin!=null && refMax==refMin) {
			
			target = "=" + PpiNotModelUtils.doubleToString(refMin);
		} else
		if (refMax==null && refMin!=null) {
			
			target = ">" + PpiNotModelUtils.doubleToString(refMin);
		} else
		if (refMin==null && refMax!=null) {
			
			target = "<" + PpiNotModelUtils.doubleToString(refMax);
		} else 
		if (refMax!=null && refMin!=null) {
			
			target = PpiNotModelUtils.doubleToString(refMin) + "-" + PpiNotModelUtils.doubleToString(refMax);
		} 
		return target;
	}

	/**
	 * Genera una tarea para un conector del tipo AppliesTo cuando se va a exportar un xml
	 * 
	 * @param con Conector
	 * @return Tarea generada
	 */
	private TTask chainTask(TMeasureConnector con) {
		
    	TTask task = this.bpmnFactory.createTTask();
    	task.setId((String) con.getTargetRef());
    	con.setTargetRef(task);
    	
    	return task;
	}

	/**
	 * Genera una dataobject para un conector del tipo AppliesTo cuando se va a exportar un xml
	 * 
	 * @param con Conector
	 * @return Dataobject generado
	 */
	private TDataObject chainDataobject(TMeasureConnector con) {
		
		TDataObject dataobject = this.bpmnFactory.createTDataObject();
		dataobject.setId(this.generatePpiNotInfo.generarId("dataobject", ""));
		dataobject.setName((String) con.getTargetRef());
    	con.setTargetRef(dataobject);
    	
    	return dataobject;
	}
	
	/**
	 * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida base
	 * 
	 * @param def Objeto del modelo
	 * @return
	 */
	private TMeasure findBaseMeasure(MeasureDefinition def) {
		
		// de acuerdo a la clase de la medida, se invoca el metodo correspondiente de this.generatePpiNotInfo
		// con lo que obtienen los objetos Jaxb asociados con la medida
		// estos objetos con colocados en el map correspondientes a su clase Jaxb
		//
		// cuando los objetos Jaxb son conectores que se aplican a elementos BPMN, se generan los objetos Jaxb de esos elementos
		ObjectFactory factory = this.getFactory();
		TMeasure measure = null;
		if (def instanceof TimeInstanceMeasure) {
			
			// si ya fue generado el objeto Jaxb de la medida, se devuelve este
			// en caso contrario, se generan los objetos Jaxb asociados con la medida
			measure = this.timeMap.get(def.getId());
	    	if (measure==null) {
	    		
	    		// obtiene un map con los objetos Jaxb asociados con la medida
		    	Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((TimeInstanceMeasure) def, factory);
	    		
	    		if (map.containsKey("measure")) {
	    			
	    			// guarda el objeto Jaxb de la medida en el map correspondiente
	    			measure = (TMeasure) map.get("measure");
	    			this.timeMap.put(((TTimeMeasure) map.get("measure")).getId(), (TTimeMeasure) measure);
			    	
	    			// guarda otros objetos Jaxb asociados con la medida, en sus correspondientes maps
		    		if (map.containsKey("connectorFrom")) {
		    	    	
		    			TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
		    			// genera la tarea a la que se aplica el conector
		    	    	TTask task = chainTask(con);
		    	    	this.taskMap.put(task.getId(), task);
		
		    	    	this.timeConnectorMap.put(con.getId(), con);
		    		}
			    	
		    		if (map.containsKey("connectorTo")) {
		    	    	
		    			TTimeConnector con = (TTimeConnector) map.get("connectorTo");
		    	    	TTask task = chainTask(con);
		    	    	this.taskMap.put(task.getId(), task);
		
		    	    	this.timeConnectorMap.put(con.getId(), con);
		    		}
	    		}
	    	}
		} else
		if (def instanceof CountInstanceMeasure) {
			
			measure = this.countMap.get(def.getId());
	    	if (measure==null) {
	    		
		    	Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((CountInstanceMeasure) def, factory);
	    		
	    		if (map.containsKey("measure")) {
	    			
	    			measure = (TMeasure) map.get("measure");
		    		if (map.containsKey("measure"))
		    			this.countMap.put(((TCountMeasure) map.get("measure")).getId(), (TCountMeasure) measure);
		    		
		    		if (map.containsKey("connector")) {
		    	    	
		    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
		    	    	TTask task = chainTask(con);
		    	    	this.taskMap.put(task.getId(), task);
		
		    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
		    		}
	    		}
	    	}
		} else
		if (def instanceof StateConditionInstanceMeasure) {
				
			measure = this.stateConditionMap.get(def.getId());
	    	if (measure==null) {
	    		
		    	Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((StateConditionInstanceMeasure) def, factory);
	    		
	    		if (map.containsKey("measure")) {
	    			
	    			measure = (TMeasure) map.get("measure");
		    		if (map.containsKey("measure"))
		    			this.stateConditionMap.put(((TStateConditionMeasure) map.get("measure")).getId(), (TStateConditionMeasure) measure);
		    		
		    		if (map.containsKey("connector")) {
		    	    	
		    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
		    	    	TTask task = chainTask(con);
		    	    	this.taskMap.put(task.getId(), task);
	
		    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
		    		}
	    		}
	    	}
		} else
		if (def instanceof DataInstanceMeasure) {
				
			measure = this.dataMap.get(def.getId());
	    	if (measure==null) {
	    		
		    	Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((DataInstanceMeasure) def, factory);
	    		
	    		if (map.containsKey("measure")) {
	    			
	    			measure = (TMeasure) map.get("measure");
		    		if (map.containsKey("measure"))
		    			this.dataMap.put(((TDataMeasure) map.get("measure")).getId(), (TDataMeasure) measure);
		    		
		    		if (map.containsKey("connector")) {
		    	    	
		    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
		    	    	// genera el dataobject al que se aplica el conector
		    	    	TDataObject dataobject = chainDataobject(con);
		    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);
	
		    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
		    		}
	    		}
	    	}
		} else
		if (def instanceof DataPropertyConditionInstanceMeasure) {
				
			measure = this.dataPropertyConditionMap.get(def.getId());
	    	if (measure==null) {
	    		
		    	Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((DataPropertyConditionInstanceMeasure) def, factory);
	    		
	    		if (map.containsKey("measure")) {
	    			
	    			measure = (TMeasure) map.get("measure");
		    		if (map.containsKey("measure"))
		    			this.dataPropertyConditionMap.put(((TDataPropertyConditionMeasure) map.get("measure")).getId(), (TDataPropertyConditionMeasure) measure);
		    		
		    		if (map.containsKey("connector")) {
		    	    	
		    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
		    	    	TDataObject dataobject = chainDataobject(con);
		    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);
	
		    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
		    		}
	    		}
	    	}
		}
		
		return measure;
	}
	
	/**
	 * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida agregada
	 * 
	 * @param def Objeto del modelo
	 * @return
	 */
	private TMeasure findAggregatedMeasure(MeasureDefinition def) {
		
		// si ya fue generado el objeto Jaxb de la medida, se devuelve este
		// en caso contrario, se generan los objetos Jaxb asociados con la medida
		TMeasure measure = this.aggregatedMap.get(def.getId());
		if (measure!=null)
			return measure;
	
		ObjectFactory factory = this.getFactory();
		
		// obtiene un map con los objetos Jaxb asociados con la medida
		Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((AggregatedMeasure) def, factory);

		measure = (TMeasure) map.get("measure");
		
		if (measure!=null) {
	    	
			// guarda el objeto Jaxb de la medida en el map correspondiente
			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) measure);
			
			// guarda otros objetos Jaxb asociados con la medida, en sus correspondientes maps
    		if (map.containsKey("connectorIsGroupedBy")) {
    	    	
    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
    	    	// genera el dataobject al que se aplica el conector
    	    	TDataObject dataobject = chainDataobject(con);
    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

    	    	this.isGroupedByMap.put(con.getId(), con);
    		}

    		// obtiene la medida base
    		MeasureDefinition baseMeasureModel = ((AggregatedMeasure) def).getBaseMeasure(); 
    		
    		// si la medida agregada utiliza un conector aggregates
    		if (((AggregatedMeasure) def).getAggregates()) {
    			
    			// crea el objeto Jaxb del conector aggregates
    			TAggregates con = this.getFactory().createTAggregates();
    			
    			// genera los objetos Jaxb asociados con la medida base
    			TMeasure baseMeasure = this.findBaseMeasure(baseMeasureModel);
    			if (baseMeasure==null) 
    				baseMeasure = this.findDerivedMeasure(baseMeasureModel);
    			
    			// da valor a propiedades del objeto Jaxb del conector
    			con.setId(this.generatePpiNotInfo.generarId("aggregates", ""));
    			con.setSourceRef(measure);
    			con.setTargetRef(baseMeasure);
    			
    			// conserva el objeto Jaxb del conector en el map correspondiente
    	    	this.aggregatesMap.put(con.getId(), con);
    		} else {
    			
    			// si la medida agregada no utiliza conector aggregates, 
    			// de acuerdo a la clase de la medida de la medida base, se invoca el metodo correspondiente de this.generatePpiNotInfo
    			// con lo que obtienen los objetos Jaxb asociados con la medida base
    			// estos objetos con colocados en el map correspondientes a su clase Jaxb
    			//
    			// cuando los objetos Jaxb son conectores que se aplican a elementos BPMN, se generan los objetos Jaxb de esos elementos
    			// y al source de esos conectores se pone como valor el objeto Jaxb de la medida agregada 
    			if (baseMeasureModel instanceof TimeInstanceMeasure) {
    				
    				// genera los objetos Jaxb de la medida base
    				Map<String,Object> instanceMap = this.generatePpiNotInfo.obtainInfo((TimeInstanceMeasure) baseMeasureModel, factory);
    				
    	    		if (instanceMap.containsKey("measure")) {
    	    			
    	    			// se le da valor a la propiedad baseMeasure de la medida agregada
    	    			((TAggregatedMeasure) measure).setBaseMeasure(factory.createTimeMeasure((TTimeMeasure) instanceMap.get("measure")));
    	    			// situa otros objetos Jaxb asociados con la medida en sus correspondientes maps
	    	    		if (instanceMap.containsKey("connectorFrom")) {
	    	    	    	
	    	    	    	TTimeConnector con = (TTimeConnector) instanceMap.get("connectorFrom");
	    	    	    	// indica que el source del conector es la medida agregada (al ser generado, toma como valor la medida base)
	    	    	    	con.setSourceRef(measure);
	    	    	    	// genera el objeto Jaxb de la tarea a la que se aplica el conector
	    	    	    	TTask task = chainTask(con);
	    	    	    	this.taskMap.put(task.getId(), task);
	    	
	    	    	    	this.timeConnectorMap.put(con.getId(), con);
	    	    		}
	    	    		if (instanceMap.containsKey("connectorTo")) {
	    	    	    	
	    	    	    	TTimeConnector con = (TTimeConnector) instanceMap.get("connectorTo");
	    	    	    	con.setSourceRef(measure);
	    	    	    	TTask task = chainTask(con);
	    	    	    	this.taskMap.put(task.getId(), task);
	    	
	    	    	    	this.timeConnectorMap.put(con.getId(), con);
	    	    		}
    	    		}
    			} else
    			if (baseMeasureModel instanceof CountInstanceMeasure) {
    	
    				Map<String,Object> instanceMap = this.generatePpiNotInfo.obtainInfo((CountInstanceMeasure) baseMeasureModel, factory);
    				
    	    		if (instanceMap.containsKey("measure")) {
    	    			
    	    			((TAggregatedMeasure) measure).setBaseMeasure(factory.createCountMeasure((TCountMeasure) instanceMap.get("measure")));
 			    		if (instanceMap.containsKey("connector")) {
			    	    	
			    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) instanceMap.get("connector");
	    	    	    	con.setSourceRef(measure);
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
		
			    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
			    		}
    	    		}
    			} else
    			if (baseMeasureModel instanceof StateConditionInstanceMeasure) {
    	
    				Map<String,Object> instanceMap = this.generatePpiNotInfo.obtainInfo((StateConditionInstanceMeasure) baseMeasureModel, factory);
     				
    	    		if (instanceMap.containsKey("measure")) {
    	    			
    	    			((TAggregatedMeasure) measure).setBaseMeasure(factory.createStateConditionMeasure((TStateConditionMeasure) instanceMap.get("measure")));
	    				if (instanceMap.containsKey("connector")) {
			    	    	
			    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) instanceMap.get("connector");
		   	    	    	con.setSourceRef(measure);
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
		
			    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
			    		}
	    			}
    			} else
    			if (baseMeasureModel instanceof DataInstanceMeasure) {
    	
    				Map<String,Object> instanceMap = this.generatePpiNotInfo.obtainInfo((DataInstanceMeasure) baseMeasureModel, factory);
    				
    	    		if (instanceMap.containsKey("measure")) {
    	    			
    	    			((TAggregatedMeasure) measure).setBaseMeasure(factory.createDataMeasure((TDataMeasure) instanceMap.get("measure")));
 			    		if (instanceMap.containsKey("connector")) {
			    	    	
			    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) instanceMap.get("connector");
		   	    	    	con.setSourceRef(measure);
			    	    	TDataObject dataobject = chainDataobject(con);
			    	    	this.dataobjectMap.put(((TAggregatedMeasure) map.get("measure")).getId(), dataobject);
		
			    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
			    		}
    			}
    			} else
    			if (baseMeasureModel instanceof DataPropertyConditionInstanceMeasure) {
    	
    				Map<String,Object> instanceMap = this.generatePpiNotInfo.obtainInfo((DataPropertyConditionInstanceMeasure) baseMeasureModel, factory);
    				
    	    		if (instanceMap.containsKey("measure")) {
    	    			
    	    			((TAggregatedMeasure) measure).setBaseMeasure(factory.createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) instanceMap.get("measure")));
	    				if (instanceMap.containsKey("connector")) {
			    	    	
			    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) instanceMap.get("connector");
		   	    	    	con.setSourceRef(measure);
			    	    	TDataObject dataobject = chainDataobject(con);
			    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);
		
			    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
			    		}
    	    		}
    			}
    		}
		}
		
		return measure;
	}

	/**
	 * Crea los objetos Jaxb asociados con una medida, si el objeto del modelo proporcionado corresponde a una medida derivada
	 * 
	 * @param def Objeto del modelo
	 * @return
	 */
	private TMeasure findDerivedMeasure(MeasureDefinition def) {
		
		Boolean wasCreated = false;
		TMeasure measure = null;
		
		// de acuerdo a la clase de la medida de la medida, se invoca el metodo correspondiente de this.generatePpiNotInfo
		// con lo que obtienen los objetos Jaxb asociados con la medida
		// estos objetos con colocados en el map correspondientes a su clase Jaxb
		if (def instanceof DerivedSingleInstanceMeasure) {
		
			// si ya fue generado el objeto Jaxb de la medida, se devuelve este
			// en caso contrario, se generan los objetos Jaxb asociados con la medida
			measure = this.derivedSingleInstanceMap.get(def.getId());
	    	if (measure==null) {
	    		
				// genera los objetos Jaxb de la medida 
				Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((DerivedSingleInstanceMeasure) def, this.getFactory());
    		
				measure = (TMeasure) map.get("measure");
	    		if (measure!=null) {
	    			
	    			// indica que la medida fue creada
	    			wasCreated = true;
	    			// guarda el objeto Jaxb de la medida en el map correspondiente
	    			this.derivedSingleInstanceMap.put(((TDerivedSingleInstanceMeasure) measure).getId(), (TDerivedSingleInstanceMeasure) measure);
	    		}
		    }
		} else
		if (def instanceof DerivedMultiInstanceMeasure) {
			
			measure = this.derivedMultiInstanceMap.get(def.getId());
	    	if (measure==null) {
	    		
				Map<String,Object> map = this.generatePpiNotInfo.obtainInfo((DerivedMultiInstanceMeasure) def, this.getFactory());
    		
				measure = (TMeasure) map.get("measure");
	    		if (measure!=null) {
	    			
	    			wasCreated = true;
	    			this.derivedMultiInstanceMap.put(((TDerivedMultiInstanceMeasure) measure).getId(), (TDerivedMultiInstanceMeasure) measure);
	    		}
		    }
		}

		// si la medida fue creada
		if (wasCreated) {
			
			// para cada una de las medidas asociadas con la medida derivada
			Iterator<Entry<String, MeasureDefinition>> itInst = ((DerivedMeasure) def).getUsedMeasureMap().entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
		    	String variable = pairs.getKey();
		    	MeasureDefinition usedMeasureModel = pairs.getValue();
			
		    	// genera los objetos Jaxb asociados con la medida asociada
    			TMeasure usedMeasure = this.findBaseMeasure(usedMeasureModel);
    			if (usedMeasure==null) 
    				usedMeasure = this.findAggregatedMeasure(usedMeasureModel);
    			if (usedMeasure==null) 
    				usedMeasure = this.findDerivedMeasure(usedMeasureModel);

    			// crea el objeto Jaxb del conector uses correspondiente a la medida asociada
				TUses con = this.getFactory().createTUses();
				con.setId(this.generatePpiNotInfo.generarId("uses", ""));
				con.setVariable(variable);
				con.setSourceRef(measure);
				con.setTargetRef(usedMeasure);
				
				// situa el objeto Jaxb del conector en el map correspondiente
   				this.usesMap.put(con.getId(), con);
		    }
		}
		return measure;
	}
		
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
		    	TimeInstanceMeasure def = pairs.getValue();

		    	this.findBaseMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos CountInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setCountModelMap(Map<String, CountInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
		        CountInstanceMeasure def = pairs.getValue();
		    		
		    	this.findBaseMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos StateConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
		        StateConditionInstanceMeasure def = pairs.getValue();
		    		
		    	this.findBaseMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DataInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataModelMap(Map<String, DataInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
		        DataInstanceMeasure def = pairs.getValue();
		    		
		    	this.findBaseMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DataPropertyConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
		        DataPropertyConditionInstanceMeasure def = pairs.getValue();
		    		
		    	this.findBaseMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	private void setAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
		        this.findAggregatedMeasure(def);
		    }
    	}
	}
	
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean CountInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean StateConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean DataPropertyConditionInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos AggregatedMeasure cuyas medidas base sean TimeInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    this.setAggregatedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DerivedMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedModelMap(Map<String, DerivedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
		        DerivedSingleInstanceMeasure def = (DerivedSingleInstanceMeasure) pairs.getValue();
		    		
		        this.findDerivedMeasure(def);
		    }
	    }
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DerivedSingleInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedSingleInstanceModelMap(Map<String, DerivedMeasure> modelMap) {

	    this.setDerivedModelMap(modelMap);
	}

	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos DerivedMultiInstanceMeasure
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setDerivedMultiInstanceModelMap(Map<String, DerivedMeasure> modelMap) {

	    this.setDerivedModelMap(modelMap);
	}
	
	/**
	 * Genera los objetos Jaxb correspondientes a un map de objetos PPI
	 * 
	 * @param modelMap Map con objetos del modelo
	 */
	public void setPpiModelMap(Map<String, PPI> modelMap) {
		
	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, PPI>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI def = pairs.getValue();
		        
				TPpi ppi = this.getFactory().createTPpi();
				ppi.setId(def.getId());

				ppi.setName(def.getName());
				ppi.setGoals(def.getGoals());
				ppi.setResponsible(def.getResponsible());
				ppi.setInformed(def.getInformed());
				ppi.setComments(def.getComments());
		    	
				ppi.setTarget(this.generateTarget(def.getTarget().getRefMin(), def.getTarget().getRefMax()));
				ppi.setScope(this.generateAnalisysPeriod(def.getScope().getYear(), def.getScope().getPeriod(), def.getScope().getStartDate(), def.getScope().getEndDate(), def.getScope().getInStart(), def.getScope().getInEnd()));
				
		        MeasureDefinition m = def.getMeasuredBy();

				TMeasure md = this.findBaseMeasure(m);
				if (md==null)
					md = this.findAggregatedMeasure(m);
				if (md==null)
					md = this.findDerivedMeasure(m);

		    	ppi.setMeasuredBy(md);
					
    			this.ppiList.add(ppi);
		    	
		    }
	    }
	}

	/**
	 * Genera las instancias de clases Jaxb a partir de instancias de clases del modelo que debieron ser seteadas previamente con el metodo
	 * set correspondiente a cada tipo de medida. 
	 * Genera el JAXBElement para exportar, por lo que debe finalizar invocando a this.setExportElement
	 * 
	 * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
	 */
	@Override
	protected void generateExportElement(String procId) {
		
		// inicializa el objeto para generar objetos Jaxb a partir de los objetos del modelos
		this.generatePpiNotInfo = new GeneratePpiNotInfo();
		
		// crea el objeto Jaxb Tppiset, en el que se incluyen todos los objetos Jaxb de las medidas
	    TPpiset ppiset = this.getFactory().createTPpiset();
	    ppiset.setId("ppiset_1");
	    
	    // adiciona al ppiset cada uno de los objetos Jaxb de las medidas
		Iterator<Entry<String, TCountMeasure>> itInst1 = this.countMap.entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, TCountMeasure> pairs = (Map.Entry<String, TCountMeasure>)itInst1.next();
	    	ppiset.getBaseMeasure().add(this.getFactory().createCountMeasure((TCountMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TTimeMeasure>> itInst2 = this.timeMap.entrySet().iterator();
	    while (itInst2.hasNext()) {
	        Map.Entry<String, TTimeMeasure> pairs = (Map.Entry<String, TTimeMeasure>)itInst2.next();
	    	ppiset.getBaseMeasure().add(this.getFactory().createTimeMeasure((TTimeMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TStateConditionMeasure>> itInst3 = this.stateConditionMap.entrySet().iterator();
	    while (itInst3.hasNext()) {
	        Map.Entry<String, TStateConditionMeasure> pairs = (Map.Entry<String, TStateConditionMeasure>)itInst3.next();
	    	ppiset.getBaseMeasure().add(this.getFactory().createStateConditionMeasure((TStateConditionMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDataMeasure>> itInst4 = this.dataMap.entrySet().iterator();
	    while (itInst4.hasNext()) {
	        Map.Entry<String, TDataMeasure> pairs = (Map.Entry<String, TDataMeasure>)itInst4.next();
	    	ppiset.getBaseMeasure().add(this.getFactory().createDataMeasure((TDataMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDataPropertyConditionMeasure>> itInst5 = this.dataPropertyConditionMap.entrySet().iterator();
	    while (itInst5.hasNext()) {
	        Map.Entry<String, TDataPropertyConditionMeasure> pairs = (Map.Entry<String, TDataPropertyConditionMeasure>)itInst5.next();
	    	ppiset.getBaseMeasure().add(this.getFactory().createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAggregatedMeasure>> itInst6 = this.aggregatedMap.entrySet().iterator();
	    while (itInst6.hasNext()) {
	        Map.Entry<String, TAggregatedMeasure> pairs = (Map.Entry<String, TAggregatedMeasure>)itInst6.next();
	    	ppiset.getAggregatedMeasure().add((TAggregatedMeasure) pairs.getValue());
	    }
	    
		Iterator<Entry<String, TDerivedSingleInstanceMeasure>> itInst7 = this.derivedSingleInstanceMap.entrySet().iterator();
	    while (itInst7.hasNext()) {
	        Map.Entry<String, TDerivedSingleInstanceMeasure> pairs = (Map.Entry<String, TDerivedSingleInstanceMeasure>)itInst7.next();
	    	ppiset.getDerivedMeasure().add(this.getFactory().createDerivedSingleInstanceMeasure((TDerivedSingleInstanceMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDerivedMultiInstanceMeasure>> itInst8 = this.derivedMultiInstanceMap.entrySet().iterator();
	    while (itInst8.hasNext()) {
	        Map.Entry<String, TDerivedMultiInstanceMeasure> pairs = (Map.Entry<String, TDerivedMultiInstanceMeasure>)itInst8.next();
	    	ppiset.getDerivedMeasure().add(this.getFactory().createDerivedMultiInstanceMeasure((TDerivedMultiInstanceMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAppliesToElementConnector>> itInst9 = this.appliesToElementConnectorMap.entrySet().iterator();
	    while (itInst9.hasNext()) {
	        Map.Entry<String, TAppliesToElementConnector> pairs = (Map.Entry<String, TAppliesToElementConnector>)itInst9.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createAppliesToElementConnector((TAppliesToElementConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TTimeConnector>> itInst10 = this.timeConnectorMap.entrySet().iterator();
	    while (itInst10.hasNext()) {
	        Map.Entry<String, TTimeConnector> pairs = (Map.Entry<String, TTimeConnector>)itInst10.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createTimeConnector((TTimeConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TUses>> itInst11 = this.usesMap.entrySet().iterator();
	    while (itInst11.hasNext()) {
	        Map.Entry<String, TUses> pairs = (Map.Entry<String, TUses>)itInst11.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createUses((TUses) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAppliesToDataConnector>> itInst14 = this.appliesToDataConnectorMap.entrySet().iterator();
	    while (itInst14.hasNext()) {
	        Map.Entry<String, TAppliesToDataConnector> pairs = (Map.Entry<String, TAppliesToDataConnector>)itInst14.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createAppliesToDataConnector((TAppliesToDataConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAggregates>> itInst15 = this.aggregatesMap.entrySet().iterator();
	    while (itInst15.hasNext()) {
	        Map.Entry<String, TAggregates> pairs = (Map.Entry<String, TAggregates>)itInst15.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createAggregates((TAggregates) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TIsGroupedBy>> itInst16 = this.isGroupedByMap.entrySet().iterator();
	    while (itInst16.hasNext()) {
	        Map.Entry<String, TIsGroupedBy> pairs = (Map.Entry<String, TIsGroupedBy>)itInst16.next();
	    	ppiset.getMeasureConnector().add(this.getFactory().createIsGroupedBy((TIsGroupedBy) pairs.getValue()));
	    }
	    
	    // adiciona al ppiset cada uno de los objetos Jaxb de los ppi
	    for (TPpi ppi : this.ppiList) 
	    	ppiset.getPpi().add(ppi);
   	
	    // crea un objeto Jaxb del tipo extensionElements
    	TExtensionElements extensionElements = new TExtensionElements();
	    extensionElements.getAny().add( this.getFactory().createPpiset(ppiset) );
		
	    // crea un objeto Jaxb del tipo process
	    TProcess process = this.bpmnFactory.createTProcess();
	    process.setId( procId );
	    process.setName( procId );
	    process.setExtensionElements(extensionElements);

	    // adiciona al proceso generado, las tareas generadas (TTask) al crear los objetos Jaxb de las medidas 
		Iterator<Entry<String, TTask>> itInst12 = this.taskMap.entrySet().iterator();
	    while (itInst12.hasNext()) {
	        Map.Entry<String, TTask> pairs = (Map.Entry<String, TTask>)itInst12.next();
	        process.getFlowElement().add(this.bpmnFactory.createTask((TTask) pairs.getValue()));
	    }

	    // adiciona al proceso generado, los dataobjets generados (TDataObject) al crear los objetos Jaxb de las medidas 
		Iterator<Entry<String, TDataObject>> itInst13 = this.dataobjectMap.entrySet().iterator();
	    while (itInst13.hasNext()) {
	        Map.Entry<String, TDataObject> pairs = (Map.Entry<String, TDataObject>)itInst13.next();
	        TDataObject value = pairs.getValue();
			process.getFlowElement().add(this.bpmnFactory.createDataObject(value));
	    }

	    // crea un objeto Jaxb del tipo definitions
	    TDefinitions definitions = new TDefinitions();
	    definitions.setId("ppinot-definitions");
	    definitions.setExpressionLanguage("http://www.w3.org/1999/XPath");
	    definitions.setTargetNamespace("http://schema.omg.org/spec/BPMN/2.0");
	    definitions.setTypeLanguage("http://www.w3.org/2001/XMLSchema");
    	definitions.getRootElement().add( this.bpmnFactory.createProcess(process));
    	
    	// da valor al objeto JAXBElement que se utiliza para hacer el marshall de un xml
        this.setExportElement( this.bpmnFactory.createDefinitions(definitions) );
		
	}
	
	/**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida base 
	 * 
	 * @param jaxbValue Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition findBaseMeasureModel(Object jaxbValue) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = ((TMeasure) jaxbValue).getId();
		Boolean wasCreated = false;
		MeasureDefinition def = null;
		
		// de acuerdo a la clase del objeto Jaxb
		if(jaxbValue instanceof TCountMeasure) {
			
			// se devuelve el objeto del modelo correspondiente, si ya estaba creado  
			def = this.countInstanceModelMap.get(id);
			if (def==null) {
				// genera el objeto del modelo
				def = this.generatePpiNotModel.obtainModel((TCountMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					// adiciona el objeto del modelo al map correspondiente
					this.countInstanceModelMap.put( def.getId(), (CountInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TTimeMeasure) {

			def = this.timeInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TTimeMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.timeInstanceModelMap.put( def.getId(), (TimeInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TStateConditionMeasure) {

			def = this.stateConditionInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TStateConditionMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.stateConditionInstanceModelMap.put( def.getId(), (StateConditionInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TDataMeasure) {

			def = this.dataInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDataMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.dataInstanceModelMap.put( def.getId(), (DataInstanceMeasure) def );
				}
			}
		} else
		if(jaxbValue instanceof TDataPropertyConditionMeasure) {

			def = this.dataPropertyConditionInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDataPropertyConditionMeasure) jaxbValue, ppiset);
				wasCreated = def!=null;
				if (wasCreated) {
					this.dataPropertyConditionInstanceModelMap.put( def.getId(), (DataPropertyConditionInstanceMeasure) def );
				}
			}
		}
		
		if (wasCreated) {
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.searchPpi(jaxbValue, def);
		}

		return def;
	}
	
	/**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida agregada 
	 * 
	 * @param jaxbValue Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition findAggregatedMeasureModel(TAggregatedMeasure measure) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = measure.getId();
		Boolean wasCreated = false;
		AggregatedMeasure def = null;
		
		// si la medida agregada no tiene seteada la propiedad baseMeasure, es que esta asociada con un conector aggregates
		if(measure.getBaseMeasure()==null) {
			
			// se obtiene el conector asociado a la medida
			TAggregates connector = (TAggregates) this.generatePpiNotModel.findMeasureConnector(measure, TAggregates.class, ppiset);
			
			// si existe el conector
			if (connector!=null) {
				
				// se obtiene el objeto del modelo de la medida conectada con la medida agregada
				MeasureDefinition baseModel = this.findConnectedMeasure(connector);

				// de acuerdo a la clase de la medida conectada
				if(baseModel instanceof TimeInstanceMeasure ) {
					
					// se verifica si la medida agregada ya habia sido generada
					def = this.timeAggregatedModelMap.get(id);
					if (def==null) {
						// si no habia sido generada, se obtiene el objeto del modelo de la medida agregada
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							// se adiciona la medida agregada al map correspondiente
							this.timeAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof CountInstanceMeasure ) {
					
					def = this.countAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.countAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof StateConditionInstanceMeasure ) {
					
					def = this.stateConditionAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.stateConditionAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DataInstanceMeasure ) {
					
					def = this.dataAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.dataAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DataPropertyConditionInstanceMeasure ) {
					
					def = this.dataPropertyConditionAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {
							this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
						}
					}
				} else
				if(baseModel instanceof DerivedSingleInstanceMeasure ) {
					
					def = this.derivedSingleInstanceAggregatedModelMap.get(id);
					if (def==null) {
						def = this.generatePpiNotModel.obtainModel(measure, baseModel, ppiset);
						wasCreated = def!=null;
						if (wasCreated) {

							for (TUses con : this.generatePpiNotModel.findUses(this.derivedSingleInstanceMap.get(baseModel.getId()), ppiset)) {
								
								((DerivedMeasure) baseModel).addUsedMeasure( con.getVariable(), this.findConnectedMeasure(con) );
							}
							this.derivedSingleInstanceAggregatedModelMap.put( def.getId(), def );
						}
					}
				}
				
				if (wasCreated) {
					
					// si la medida agregada fue creada se indica que esta medida esta asociada a un conector aggregates
					def.setAggregates(true);
				}
			}
		} else {
			
			// si la medida agregada no esta asociada a un conector aggregates, se obtiene su medida base
			TMeasure baseMeasure = measure.getBaseMeasure().getValue();
		
			// de acuerdo a la clase de la medida base
			if(baseMeasure instanceof TTimeMeasure ) {

				// se verifica si la medida agregada ya habia sido generada
				def = this.timeAggregatedModelMap.get(id);
				if (def==null) {
					// si no habia sido generada, se obtiene el objeto del modelo de la medida agregada
					def = this.generatePpiNotModel.obtainModel(measure, (TTimeMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						// se adiciona la medida agregada al map correspondiente
						this.timeAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TCountMeasure ) {
				
				def = this.countAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TCountMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.countAggregatedModelMap.put( def.getId(), def );
					}
				
				}
			} else
			if(baseMeasure instanceof TStateConditionMeasure ) {
				
				def = this.stateConditionAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TStateConditionMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.stateConditionAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDataMeasure ) {
				
				def = this.dataAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDataMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.dataAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDataPropertyConditionMeasure ) {
				
				def = this.dataPropertyConditionAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDataPropertyConditionMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {
						this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
					}
				}
			} else
			if(baseMeasure instanceof TDerivedSingleInstanceMeasure ) {
				
				def = this.derivedSingleInstanceAggregatedModelMap.get(id);
				if (def==null) {
					def = this.generatePpiNotModel.obtainModel(measure, (TDerivedSingleInstanceMeasure) baseMeasure, ppiset);
					wasCreated = def!=null;
					if (wasCreated) {

						MeasureDefinition baseModel = def.getBaseMeasure();
						for (TUses connector : this.generatePpiNotModel.findUses(baseMeasure, ppiset)) {
							
							((DerivedMeasure) baseModel).addUsedMeasure( connector.getVariable(), this.findConnectedMeasure(connector) );
						}
						this.derivedSingleInstanceAggregatedModelMap.put( def.getId(), def );
					}
				}
			}
		}
		
		if (wasCreated) {
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.searchPpi(measure, def);
		}
		
		return def;
	}
	
	/**
	 * Genera un objeto del modelo a partir de un objeto Jaxb si este es una medida derivada 
	 * 
	 * @param jaxbValue Objeto Jaxb
	 * @return Objeto del modelo
	 */
	private MeasureDefinition findDerivedMeasureModel(Object jaxbValue) {
		
		// obtiene el ppiset en el xml, que es utilizado al generar el objeto del modelo, para obtener los conectores y otros elementos
		// relacionados con la medida 
		TPpiset ppiset = this.getPpiset();
		// inicializaciones
		String id = ((TMeasure) jaxbValue).getId();
		Boolean wasCreated = false;
		MeasureDefinition def = null;
		
		// de acuerdo a la clase de la medida 
		if(jaxbValue instanceof TDerivedSingleInstanceMeasure) {
			
			// se verifica si la medida ya habia sido generada
			def = this.derivedSingleInstanceModelMap.get(id);
			if (def==null) {
				// si no habia sido generada, se obtiene el objeto del modelo de la medida 
				def = this.generatePpiNotModel.obtainModel((TDerivedSingleInstanceMeasure) jaxbValue);
				wasCreated = def!=null;
				if (wasCreated) {
					// se adiciona la medida al map correspondiente
					this.derivedSingleInstanceModelMap.put(def.getId(), (DerivedMeasure) def);
				}
			}
		} else
		if(jaxbValue instanceof TDerivedMultiInstanceMeasure) {
			
			def = this.derivedMultiInstanceModelMap.get(id);
			if (def==null) {
				def = this.generatePpiNotModel.obtainModel((TDerivedMultiInstanceMeasure) jaxbValue);
				wasCreated = def!=null;
				if (wasCreated) {
					this.derivedMultiInstanceModelMap.put( def.getId(), (DerivedMeasure) def );
				}
			}
		}
		
		if (wasCreated) {
			
			// si el objeto del modelo fue creado, se buscan las medidas conectadas a la medida derivada con un conector uses
			// y esas medidas se adicionan a la lista de variables utilizadas por la medida derivada
			for (TUses connector : this.generatePpiNotModel.findUses((TDerivedMeasure) jaxbValue, ppiset)) {
				
				((DerivedMeasure) def).addUsedMeasure( connector.getVariable(), this.findConnectedMeasure(connector) );
			}
			
			// crea el objeto del modelo del PPI asociado a la medida
			this.searchPpi(jaxbValue, def);
		}
		
		return def;
	}

	/**
	 * Obtiene el objeto Jaxb del tipo ppiset en el proceso
	 * 
	 * @return
	 */
	private TPpiset getPpiset() {
		
		TProcess process = null;
		TPpiset ppiset = null;
		
		Object object = ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		if (object instanceof TProcess) {
			
			process = (TProcess) object;
			ppiset = (TPpiset) ((JAXBElement<?>) process.getExtensionElements().getAny().get(0)).getValue();
		} else {
			
			for (JAXBElement<?> element : ((TDefinitions) this.getImportElement().getValue()).getRootElement()) {
				
				Object participant = element.getValue();
				if (participant instanceof TProcess &&
					((TProcess) participant).getExtensionElements()!=null &&
					((TProcess) participant).getExtensionElements().getAny()!=null &&
					((TProcess) participant).getExtensionElements().getAny().get(0)!=null &&
					((JAXBElement<?>) ((TProcess) participant).getExtensionElements().getAny().get(0)).getValue() instanceof TPpiset
				   ) {
					
					ppiset = (TPpiset) ((JAXBElement<?>) ((TProcess) participant).getExtensionElements().getAny().get(0)).getValue();
					break;
				}
			}
		}

		return ppiset;
	}
	
	/**
	 * Obtiene los datos del periodo que se desea analizar, a partir de la propiedad analisysPeriod de una medida agregada 
	 * 
	 * @param period Cadena con el periodo de analisis
	 * @return Mapa con los datos del periodo de analisis
	 */
	private Map<String, String> parseAnalysisPeriod(String period) {
		
		Map<String, String> map = new HashMap<String, String>();

		if (period.contentEquals("")) {
			
			map.put("year", "");
			map.put("period", "");
			map.put("startDate", "");
			map.put("endDate", "");
			map.put("inStart", "");
			map.put("inEnd", "");
		}
		else
			try {
				Pattern patron = Pattern.compile("(interval)\\((\\d{4}\\/\\d{2}\\/\\d{2}),(\\d{4}\\/\\d{2}\\/\\d{2})\\,(true|false)\\,(true|false)\\)|(period)\\((\\d{4}),(trimestre|semestre|mes)\\,(true|false)\\,(true|false)\\)");
				Matcher matcher = patron.matcher( period );
				matcher.find();
	
				if (matcher.group(6)!=null && matcher.group(6).contentEquals("period")) {
					
					map.put("year", matcher.group(7));
					map.put("period", matcher.group(8));
					map.put("startDate", "");
					map.put("endDate", "");
					map.put("inStart", (matcher.group(9).compareTo("true")==0)?"yes":"");
					map.put("inEnd", (matcher.group(10).compareTo("true")==0)?"yes":"");
				}
				else 
				if (matcher.group(1).contentEquals("interval")) {
					
					map.put("year", "");
					map.put("period", "");
					map.put("startDate", matcher.group(2));
					map.put("endDate", matcher.group(3));
					map.put("inStart", (matcher.group(4).compareTo("true")==0)?"yes":"");
					map.put("inEnd", (matcher.group(5).compareTo("true")==0)?"yes":"");
				} 

			}  catch (Exception e) {
				
				map.put("year", "");
				map.put("period", "");
				map.put("startDate", "");
				map.put("endDate", "");
				map.put("inStart", "");
				map.put("inEnd", "");
			}
		
		return map;
	}

	/**
	 * Obtiene los valores de referencia para evaluar el grado de satisfaccion de un ppi, a partir de la propiedad target
	 * 
	 * @param target Valor de la propiedad target de un ppi
	 * @return Mapa con los valores de referencia. El valor minimo se devuelve con la llave refMin y el valor maximo con la llave refMax
	 */
	private Map<String, Double> parseTarget(String target) {
		
		Map<String, Double> map = new HashMap<String, Double>();

		try {
			Pattern patron = Pattern.compile("(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)\\-(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)|(>|<|=|@)(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)");
			Matcher matcher = patron.matcher( target );
			matcher.find();

			if (matcher.group(3)!=null && matcher.group(3).contentEquals("=")) {
			
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals(">")) {
				
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals("<")) {
				
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(4)));
			} else 
			if (matcher.group(1)!=null && matcher.group(2)!=null) {
				map.put("refMin", PpiNotModelUtils.stringToDouble(matcher.group(1)));
				map.put("refMax", PpiNotModelUtils.stringToDouble(matcher.group(2)));
			} 
		}  catch (Exception e) {
			
			map.put("refMin", null);
			map.put("refMax", null);
		}
		
		return map;
	}
	
	/**
	 * Crea el objeto del modelo del PPI asociado a una medida 
	 * 
	 * @param object Objeto Jaxb de la medida
	 * @param def Objeto del modelo de la medida
	 */
	private void searchPpi(Object object, MeasureDefinition def) {
		
		for (TPpi ppi : this.getPpiset().getPpi()) {
			
			if(ppi.getMeasuredBy().equals(object)) {
				
				Map<String, Double> targetMap = this.parseTarget(ppi.getTarget());
				
				Map<String, String> analysisperiodMap = parseAnalysisPeriod(ppi.getScope());
				
				PPI ppiModel = new PPI(
						ppi.getId(), 
						ppi.getName(), 
						ppi.getDescription(), 
						ppi.getGoals(), 
						ppi.getResponsible(), 
						ppi.getInformed(), 
						ppi.getInformed(),
						new Target( targetMap.get("refMax"), targetMap.get("refMin")), 
			    		new Scope( analysisperiodMap.get("year"), 
			    				analysisperiodMap.get("period"), 
			    				PpiNotModelUtils.parseDate(analysisperiodMap.get("startDate")), 
			    				PpiNotModelUtils.parseDate(analysisperiodMap.get("endDate")), 
			    				analysisperiodMap.get("inStart").contentEquals("yes"), 
			    				analysisperiodMap.get("inEnd").contentEquals("yes")));
				
				ppiModel.setMeasuredBy(def);
				
				this.ppiModelMap.put(ppi.getId(), ppiModel);
				
				break;
			}
		}
	}

	/**
	 * Obtiene el objeto del modelo de una medida, a la cual se aplica un conector
	 * 
	 * @param con Objeto Jaxb de un conector
	 * @return Objeto del modelo de una medida
	 */
	private MeasureDefinition findConnectedMeasure(TMeasureConnector con) {
	
		Object target = con.getTargetRef();
		MeasureDefinition def  = this.findBaseMeasureModel( target );

		if (def==null && target instanceof TAggregatedMeasure)
			def = this.findAggregatedMeasureModel( (TAggregatedMeasure) target );

		if (def==null)
			def = this.findDerivedMeasureModel( target );

		return def;
	}

	/**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 * Despues de invocar este metodo se pueden obtener los objetos del modelo mediante los metodos get correspondientes a cada tipo de medida.
	 * 
	 */
	@Override
	protected void generateModelLists() {
		
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			this.findBaseMeasureModel( element.getValue() );
		}

		for( TAggregatedMeasure measure : this.getPpiset().getAggregatedMeasure()) {
			
			this.findAggregatedMeasureModel( measure );
		}

		for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {
				
			this.findDerivedMeasureModel( element.getValue() );
		}
	}

}
