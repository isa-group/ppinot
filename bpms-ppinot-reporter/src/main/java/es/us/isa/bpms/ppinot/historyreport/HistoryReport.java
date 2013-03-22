package es.us.isa.bpms.ppinot.historyreport;

import es.us.isa.bpms.ppinot.historyreport.db.HistoryData;
import es.us.isa.bpms.ppinot.historyreport.db.ProcessEntity;
import es.us.isa.ppinot.handler.PpiNotModelUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.Scope;
import es.us.isa.ppinot.model.Target;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.ppinot.model.condition.TimeMeasureType;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;

/**
 * Clase para realizar reportes a partir de la historia en una plataforma BPM. Para ser utilizada, publica metodos abstractos que deben 
 * ser implementados de la forma que se hace para Activiti con la clase <a href="activiti/PlatformHistoryReport.html">PlatformHistoryReport<a>, de manera que para que la aplicacion 
 * funcione con otra plataforma BPM, basta con cambiar el paquete que se utiliza.
 * <p> 
 * Hace uso de las clases en el paquete <a href="measuredefinition/package-summary.html">historyreport.measuredefinition</a> en el que se declaran las clases que manejan la informacion 
 * de los PPI que se desean incluir en el reporte. Esto permite que se puedan realizar reportes obteniendo la informacion desde 
 * diferentes fuentes.
 * <p>
 * Para obtener la informacion desde la interfaz de una aplicacion web, deben utilizarse los nombres de variables post que publica 
 * la clase, las constantes utilizadas para publicar los PPI y el metodo <a href="../historyreport/HistoryReport.html#doReportFromRequest(HttpServletRequest)">doReportFromRequest</a>. Un ejemplo de como realizar esto se 
 * incluye con la documentacion.
 * <p>
 * Para obtener la informacion de un fichero XML, debe utilizarse el metodo <a href="../historyreport/HistoryReport.html#doReportFromXML(HttpServletRequest, java.lang.String, java.lang.String)">doReportFromXML</a>. Para ello previamente, debe disponerse
 * de <a href="../ppinotXML/package-summary.html">las clases generadas con jabxb</a> para el xsd con que se generan esos xml y extender la clase <a href="../xmlExtracter/XmlExtracter.html">XmlExtracter</a> para implementar sus 
 * metodos abstractos, que a partir de los objetos obtenidos al realizar unmarshall del xml, permiten obtener listas de instancias de 
 * las clases en el paquete <a href="measuredefinition/package-summary.html">historyreport.measuredefinition</a>, como las que maneja esta clase HistoryReport para realizar los reportes.
 * En la aplicacion web de ejemplo que se incluye en la documentacion, se ilustra como realizar esto con xml que son subidos mediante
 * la propia aplicacion.
 * <p>
 * Para incluir en los reportes los PPI relacionados con los Dataobject, debieron implementarse esos procesos haciendo uso del paquete 
 * dataobject con la misma plataforma BPM utilizada en la aplicacion donde se utilice esta clase y en el caso de Activiti debieron
 * ser ejecutados con el parametro history level configurado como FULL. Ademas debe incluirse el paquete dataobject en la compilacion 
 * de la aplicacion. El paquete con las clases que implementan los Dataobject para esos procesos (en el caso de los ejemplos en la 
 * documentacion: ejemplo1_dataobject.jar) no tienen que ser incluidos en la compilacion de la aplicacion web, basta con copiar el .jar
 * en la carpeta WEB-INF/lib y reiniciar el servidor.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class HistoryReport {
	
	private class CalculateResult {
		
		Double success = 0.0;
		Integer cant = 0;
	}

	// id del proceso seleccionado
	private String selectedProcessId;

	// ids de los procesos que tienen reportes
	private List<String> processIdList;
	// id de las instancia seleccionada de cada proceso 
	private Map<String, String> instanceIdMap;

	// evaluacion de las instancias de procesos teniendo en cuenta todos los PPIs
	private Map<String, Double> instanceSuccessMap;
	// evaluacion de los procesos teniendo en cuenta todos los PPIs
	private Map<String, Double> processSuccessMap;

	// listas de momentos de inicio y fin
	private Map<String, List<ActivityStartEnd>> activityStartEndList;
	private Map<String, List<PoolStartEnd>> poolStartEndList;

	// mapas con las medidas de cada proceso
	private Map<String, Map<String, PPI>> countInstancePpiMap;
	private Map<String, Map<String, PPI>> timeInstancePpiMap;
	private Map<String, Map<String, PPI>> stateConditionInstancePpiMap;
	private Map<String, Map<String, PPI>> dataInstancePpiMap;
	private Map<String, Map<String, PPI>> dataPropertyConditionInstancePpiMap;

	private Map<String, Map<String, PPI>> countAggregatedPpiMap;
	private Map<String, Map<String, PPI>> timeAggregatedPpiMap;
	private Map<String, Map<String, PPI>> stateConditionAggregatedPpiMap;
	private Map<String, Map<String, PPI>> dataAggregatedPpiMap;
	private Map<String, Map<String, PPI>> dataPropertyConditionAggregatedPpiMap;

	private Map<String, Map<String, PPI>> derivedSingleInstancePpiMap;
	private Map<String, Map<String, PPI>> derivedMultiInstancePpiMap;

	/**
	 * Constructor de la clase 
	 */
	public HistoryReport() {
		
		super();

		this.selectedProcessId = "";
		
		this.processIdList = new ArrayList<String>();
		this.instanceIdMap = new LinkedHashMap<String, String>();

		this.instanceSuccessMap = new LinkedHashMap<String, Double>();
		this.processSuccessMap = new LinkedHashMap<String, Double>();

		this.activityStartEndList = new LinkedHashMap<String, List<ActivityStartEnd>>();
		this.poolStartEndList = new LinkedHashMap<String, List<PoolStartEnd>>();

		this.countInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.timeInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.stateConditionInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.dataInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.dataPropertyConditionInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		
		this.countAggregatedPpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.timeAggregatedPpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.stateConditionAggregatedPpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.dataAggregatedPpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.dataPropertyConditionAggregatedPpiMap = new LinkedHashMap<String, Map<String, PPI>>();

		this.derivedSingleInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
		this.derivedMultiInstancePpiMap = new LinkedHashMap<String, Map<String, PPI>>();
	}
	
	/**
	 * Inicializa el reporte de un proceso
	 * 
	 * @param processId Id del proceso
	 * @param iniInst Indica si se inicializa el reporte de las instancias de ese proceso
	 */
	private void iniProcessReport(String processId, Boolean iniInst) {
		
		if (!this.processIdList.contains(processId)) 
			this.processIdList.add( processId );
		
		this.instanceIdMap.put(processId, "");
		
		this.instanceSuccessMap.put(processId, null);
		this.processSuccessMap.put(processId, null);

		if (iniInst) {
			this.activityStartEndList.put(processId, new ArrayList<ActivityStartEnd>());
			this.poolStartEndList.put(processId, new ArrayList<PoolStartEnd>());

			this.countInstancePpiMap.put(processId, new HashMap<String, PPI>());
			this.timeInstancePpiMap.put(processId, new HashMap<String, PPI>());
			this.stateConditionInstancePpiMap.put(processId, new HashMap<String, PPI>());
			this.dataInstancePpiMap.put(processId, new HashMap<String, PPI>());
			this.dataPropertyConditionInstancePpiMap.put(processId, new HashMap<String, PPI>());

			this.derivedSingleInstancePpiMap.put(processId, new HashMap<String, PPI>());
		}

		this.countAggregatedPpiMap.put(processId, new HashMap<String, PPI>());
		this.timeAggregatedPpiMap.put(processId, new HashMap<String, PPI>());
		this.stateConditionAggregatedPpiMap.put(processId, new HashMap<String, PPI>());
		this.dataAggregatedPpiMap.put(processId, new HashMap<String, PPI>());
		this.dataPropertyConditionAggregatedPpiMap.put(processId, new HashMap<String, PPI>());

		this.derivedMultiInstancePpiMap.put(processId, new HashMap<String, PPI>());
	}
	
	/**
	 * Elimina el reporte de un proceso
	 * 
	 * @param processId Id del proceso
	 */
	private void deleteProcessReport(String processId) {
		
		this.processIdList.remove(processId);
		this.instanceIdMap.remove(processId);

		this.activityStartEndList.remove(processId);
		this.poolStartEndList.remove(processId);

		this.countInstancePpiMap.remove(processId);
		this.timeInstancePpiMap.remove(processId);
		this.stateConditionInstancePpiMap.remove(processId);
		this.dataInstancePpiMap.remove(processId);
		this.dataPropertyConditionInstancePpiMap.remove(processId);

		this.derivedSingleInstancePpiMap.remove(processId);

		this.countAggregatedPpiMap.remove(processId);
		this.timeAggregatedPpiMap.remove(processId);
		this.stateConditionAggregatedPpiMap.remove(processId);
		this.dataAggregatedPpiMap.remove(processId);
		this.dataPropertyConditionAggregatedPpiMap.remove(processId);

		this.derivedMultiInstancePpiMap.remove(processId);
		
		this.instanceSuccessMap.remove(processId);
		this.processSuccessMap.remove(processId);
	}

	/**
	 * Devuelve el atributo activityStartEndList: 
	 * Lista de solicitudes de momento de inicio y fin de actividad
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, List<ActivityStartEnd>> getActivityStartEndList() {
		return this.activityStartEndList;
	}

	/**
	 * Devuelve el atributo poolStartEndList: 
	 * Lista de solicitudes de momento de inicio y fin de proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, List<PoolStartEnd>> getPoolStartEndList() {
		return this.poolStartEndList;
	}

	/**
	 * Devuelve el atributo countInstancePpiMap: 
	 * Mapa de medidas CountInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getCountInstancePpiMap() {
		return this.countInstancePpiMap;
	}

	/**
	 * Devuelve el atributo timeInstancePpiMap: 
	 * Mapa de medidas TimeInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getTimeInstancePpiMap() {
		return this.timeInstancePpiMap;
	}

	/**
	 * Devuelve el atributo stateConditionInstancePpiMap: 
	 * Mapa de medidas StateConditionInstanceMeasure incluidos de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getStateConditionInstancePpiMap() {
		return this.stateConditionInstancePpiMap;
	}

	/**
	 * Devuelve el atributo dataInstancePpiMap: 
	 * Mapa de medidas DataInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDataInstancePpiMap() {
		return this.dataInstancePpiMap;
	}

	/**
	 * Devuelve el atributo dataPropertyConditionInstancePpiMap: 
	 * Mapa de medidas DataPropertyConditionInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDataPropertyConditionInstancePpiMap() {
		return this.dataPropertyConditionInstancePpiMap;
	}

	/**
	 * Devuelve el atributo countAggregatedPpiMap: 
	 * Mapa de medidas de CountInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getCountAggregatedPpiMap() {
		return this.countAggregatedPpiMap;
	}

	/**
	 * Devuelve el atributo timeAggregatedPpiMap: 
	 * Mapa de medidas agregadas de TimeInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getTimeAggregatedPpiMap() {
		return this.timeAggregatedPpiMap;
	}

	/**
	 * Devuelve el atributo stateConditionAggregatedPpiMap: 
	 * Mapa de medidas agregadas de StateConditionInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getStateConditionAggregatedPpiMap() {
		return this.stateConditionAggregatedPpiMap;
	}

	/**
	 * Devuelve el atributo dataAggregatedPpiMap: 
	 * Mapa de medidas agragadas de DataInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDataAggregatedPpiMap() {
		return this.dataAggregatedPpiMap;
	}

	/**
	 * Devuelve el atributo dataPropertyConditionAggregatedPpiMap: 
	 * Mapa de medidas agregadas de DataPropertyConditionInstanceMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDataPropertyConditionAggregatedPpiMap() {
		return this.dataPropertyConditionAggregatedPpiMap;
	}

	/**
	 * Devuelve el atributo derivedSingleInstancePpiMap: 
	 * Mapa de medidas DerivedSingleInstanceMeasure incluidas de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDerivedSingleInstancePpiMap() {
		return this.derivedSingleInstancePpiMap;
	}

	/**
	 * Devuelve el atributo derivedMultiInstancePpiMap: 
	 * Mapa de medidas DerivedMultiInstanceProcessMeasure de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Map<String, PPI>> getDerivedMultiInstancePpiMap() {
		return this.derivedMultiInstancePpiMap;
	}
	
	/**
	 * Devuelve el atributo selectedProcessId:
	 * id del proceso seleccionado
	 * 
	 * @return Valor del atributo
	 */
	public String getSelectedProcessId() {
		return this.selectedProcessId;
	}

	/**
	 * Devuelve el atributo instanceSuccessMap:
	 * evaluacion de las instancia de procesos teniendo en cuenta todos los PPIs
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Double> getInstanceSuccessMap() {
		return instanceSuccessMap;
	}

	/**
	 * Devuelve el atributo processSuccessMap:
	 * evaluacion de los procesos teniendo en cuenta todos los PPIs
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, Double> getProcessSuccessMap() {
		return processSuccessMap;
	}
	
	/**
	 * Devuelve el atributo processSuccessMap:
	 * id de las instancia seleccionada de cada proceso
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, String> getInstanceIdMap() {
		return instanceIdMap;
	}
	
	/**
	 * Busca una medida de instancia en un proceso
	 * 
	 * @param processId Id del proceso
	 * @param id Id de la medida
	 * @return
	 */
	private MeasureDefinition findInstanceMeasure(String processId, String id) {
		
		MeasureDefinition measure = null;
		
		if (this.timeInstancePpiMap.get(processId).containsKey(id)) {
			
			measure = this.timeInstancePpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.countInstancePpiMap.get(processId).containsKey(id)) {
			
			measure = this.countInstancePpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.stateConditionInstancePpiMap.get(processId).containsKey(id)) {
			
			measure = this.stateConditionInstancePpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.dataInstancePpiMap.get(processId).containsKey(id)) {
			
			measure = this.dataInstancePpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.dataPropertyConditionInstancePpiMap.get(processId).containsKey(id)) {
			
			measure = this.dataPropertyConditionInstancePpiMap.get(processId).get(id).getMeasuredBy();
		} 
		
		return measure;
	}
	
	/**
	 * Busca un PPI asociado a una medida de instancia en un proceso
	 * 
	 * @param processId Id del proceso
	 * @param id Id de la medida
	 * @return
	 */
	private PPI findInstancePPI(String processId, String id) {
		
		PPI ppi = null;
		
		if (this.timeInstancePpiMap.get(processId).containsKey(id)) {
			
			ppi = this.timeInstancePpiMap.get(processId).get(id);
		} else
		if (this.countInstancePpiMap.get(processId).containsKey(id)) {
			
			ppi = this.countInstancePpiMap.get(processId).get(id);
		} else
		if (this.stateConditionInstancePpiMap.get(processId).containsKey(id)) {
			
			ppi = this.stateConditionInstancePpiMap.get(processId).get(id);
		} else
		if (this.dataInstancePpiMap.get(processId).containsKey(id)) {
			
			ppi = this.dataInstancePpiMap.get(processId).get(id);
		} else
		if (this.dataPropertyConditionInstancePpiMap.get(processId).containsKey(id)) {
			
			ppi = this.dataPropertyConditionInstancePpiMap.get(processId).get(id);
		} 
		
		return ppi;
	}
	
	/**
	 * Busca una medida de instance en un proceso
	 * 
	 * @param processId Id del proceso
	 * @param id Id de la medida
	 * @return
	 */
	private MeasureDefinition findAggregatedMeasure(String processId, String id) {
		
		MeasureDefinition measure = null;
		
		if (this.timeAggregatedPpiMap.get(processId).containsKey(id)) {
			
			measure = this.timeAggregatedPpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.countAggregatedPpiMap.get(processId).containsKey(id)) {
			
			measure = this.countAggregatedPpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.stateConditionAggregatedPpiMap.get(processId).containsKey(id)) {
			
			measure = this.stateConditionAggregatedPpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.dataAggregatedPpiMap.get(processId).containsKey(id)) {
			
			measure = this.dataAggregatedPpiMap.get(processId).get(id).getMeasuredBy();
		} else
		if (this.dataPropertyConditionAggregatedPpiMap.get(processId).containsKey(id)) {
			
			measure = this.dataPropertyConditionAggregatedPpiMap.get(processId).get(id).getMeasuredBy();
		} 
		
		return measure;
	}
	
	/**
	 * Busca un PPI asociado con una medida de instance en un proceso
	 * 
	 * @param processId Id del proceso
	 * @param id Id de la medida
	 * @return
	 */
	private PPI findAggregatedPPI(String processId, String id) {
		
		PPI ppi = null;
		
		if (this.timeAggregatedPpiMap.get(processId).containsKey(id)) {
			
			ppi = this.timeAggregatedPpiMap.get(processId).get(id);
		} else
		if (this.countAggregatedPpiMap.get(processId).containsKey(id)) {
			
			ppi = this.countAggregatedPpiMap.get(processId).get(id);
		} else
		if (this.stateConditionAggregatedPpiMap.get(processId).containsKey(id)) {
			
			ppi = this.stateConditionAggregatedPpiMap.get(processId).get(id);
		} else
		if (this.dataAggregatedPpiMap.get(processId).containsKey(id)) {
			
			ppi = this.dataAggregatedPpiMap.get(processId).get(id);
		} else
		if (this.dataPropertyConditionAggregatedPpiMap.get(processId).containsKey(id)) {
			
			ppi = this.dataPropertyConditionAggregatedPpiMap.get(processId).get(id);
		} 
		
		return ppi;
	}
	
	/**
	 * Obtiene el reporte de los PPI a partir de las selecciones realiazadas por el usuario en variables post
	 * 
	 * @param request Objeto con las variables post
	 */
	public final void doReportFromRequest(HttpServletRequest request) {
		
		// DETERMINA CUAL ES EL PROCESO QUE ESTA SELECCIONADO
		String processId = request.getParameter( HistoryConst.PROCESSID_VARNAME );
		if (processId==null) processId = "";

		String antProcId = (String) request.getSession().getAttribute("antProcId");
		if (antProcId==null) antProcId = "";
		if (!antProcId.contentEquals(processId))
			request.getSession().setAttribute("antProcId", processId);
		
		this.selectedProcessId = processId;

		// si no hay ningun proceso seleccionado, elimina el que estaba seleccionado anteriormente
		if (processId.contentEquals("") && antProcId!="") {
			
			this.deleteProcessReport(antProcId);
		} else
		// si el proceso seleccionado no se encuentra en el reporte, se inicializa
		if (processId!="" && !this.processIdList.contains(processId)) {
			
			this.iniProcessReport( processId, true );
		} else
		// si hay un proceso seleccionado y es el mismo que estaba seleccionado anteriormente, se hace el reporte a partir de las
		// solicitudes del usuario, crea el reporte con todos los PPI seleccionados
		if (processId!="" && processId.contentEquals(antProcId)) {
			
			// determina cual es la instancia del proceso que esta seleccionada
			String instanceId = request.getParameter( HistoryConst.INSTANCEID_VARNAME );
			if (instanceId==null) instanceId = "";

			String antInstId = (String) request.getSession().getAttribute("antInstId");
			if (antInstId==null) antInstId = "";
			if (!antInstId.contentEquals(instanceId))
				request.getSession().setAttribute("antInstId", instanceId);

			// se inicializa el reporte del proceso
			this.iniProcessReport( processId, instanceId.contentEquals(antInstId) );

			this.instanceIdMap.put(processId, instanceId);

			Map<String, Object> sessionTmp = new LinkedHashMap<String, Object>();
			
			// PARA CADA UNO DE LOS PPI DE MEDIDAS AGREGADAS Y DERIVADAS MULTIINSTANCE QUE HAYAN SIDO SELECCIONADOS
			// obtiene la variable post con los ids de los PPI de medidas agregadas seleccionados por el usuario
			List<String> ppiIdAggr = new ArrayList<String>();
			ppiIdAggr.add( request.getParameter( HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME ) );
			int j = 1;
			while (request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + j )!=null) {
				
				ppiIdAggr.add( request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + j ) );
				j++;
			}
			
			// determina el valor de los PPI de medidas agregadas y derivadas multiinstance
			int ii = 1;
			for( int i=0; i<ppiIdAggr.size(); i++) {
				if (ppiIdAggr.get(i)!=null && !ppiIdAggr.get(i).contentEquals("")) {
					
					// obtiene la seleccion del usuario, comun a todas las medidas agregadas
					String aggrId = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i );
					String aggrName = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i );
//					String aggrInstId = request.getParameter( "hd_" + HistoryConst.AGGRINST_PREFIX + HistoryConst.ID_PREFIX + i );
					String aggrInstId = aggrId + "_base";
					String aggrInstName = request.getParameter( "hd_" + HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i );
					String aggrFunc = request.getParameter( "hd_" + HistoryConst.AGGRFUNC_PREFIX + i );
					
					// obtiene la seleccion del usuario, comun a todos los PPI
					Double aggrRefMax = null;
					if (request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i )!=null && request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i )!="")
						aggrRefMax = Double.valueOf(request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i ));
					Double aggrRefMin = null;
					if (request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i )!=null && request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i )!="")
						aggrRefMin = Double.valueOf(request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i ));
					
					String aggrYear = request.getParameter( "hd_" + HistoryConst.AGGRYEAR_PREFIX + i );
					String aggrPeriod = request.getParameter( "hd_" + HistoryConst.AGGRPERIOD_PREFIX + i );
					String startDate = request.getParameter( "hd_" + HistoryConst.STARTDATE_PREFIX + i );
					String endDate = request.getParameter( "hd_" + HistoryConst.ENDDATE_PREFIX + i );
					String inStart = request.getParameter( "hd_" + HistoryConst.INSTART_PREFIX + i );
					String inEnd = request.getParameter( "hd_" + HistoryConst.INEND_PREFIX + i );
					
					// inicializaciones para crear el ppi
					if (aggrYear==null || aggrYear.contentEquals("")) {
						
						aggrYear = "";
						aggrPeriod = "";
						startDate = "";
						endDate = "";
					} else 
					if (aggrYear.contentEquals("intervalo")) {
						
						aggrYear = "";
						aggrPeriod = "";
					} else {
						
						startDate = "";
						endDate = "";
					}
					
					// crea el PPI asociado a la medida 
					PPI ppi = new PPI(aggrId + "_ppi", aggrName + "_ppi", "", "", "", "", "",
				    		new Target(aggrRefMax, aggrRefMin), 
				    		new Scope(aggrYear, 
				    				aggrPeriod, 
				    				PpiNotModelUtils.parseDate(startDate), 
				    				PpiNotModelUtils.parseDate(endDate), 
				    				inStart!=null && inStart.contentEquals("1"), 
				    				inEnd!=null && inEnd.contentEquals("1")));

					// si se trata de una medida DerivedMultiInstanceMeasure
					if ( Integer.valueOf(ppiIdAggr.get(i))==HistoryConst.DERIVEDMULTIINSTANCEMEASURE) {
						 
						// obtiene la seleccion del usuario
						String derFunc = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.DERFUNC_PREFIX + i );
						String idOper1 = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.IDOPER1_PREFIX + i );
						String idOper2 = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.IDOPER2_PREFIX + i );
						String medOper1 = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.MEDOPER1_PREFIX + i );
						String medOper2 = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.MEDOPER2_PREFIX + i );
	
						derFunc = (derFunc==null)?"":derFunc;
						idOper1 = (idOper1==null)?"":idOper1;
						idOper2 = (idOper2==null)?"":idOper2;
						medOper1 = (medOper1==null)?"":medOper1;
						medOper2 = (medOper2==null)?"":medOper2;
	
						// crea la medida derivada
						DerivedMultiInstanceMeasure derivedMultiInstanceMeasure = new DerivedMultiInstanceMeasure(
								aggrId, aggrName, "", "", "", derFunc);
	
						if (!idOper1.contentEquals("") && !medOper1.contentEquals("")) {
							
							MeasureDefinition def1 = this.findAggregatedMeasure(processId, medOper1);
							if (def1!=null)
								derivedMultiInstanceMeasure.addUsedMeasure(idOper1, def1);
							
							if (!idOper2.contentEquals("") && !medOper2.contentEquals("")) {
								
								MeasureDefinition def2 = this.findAggregatedMeasure(processId, medOper2);
								if (def2!=null)
									derivedMultiInstanceMeasure.addUsedMeasure(idOper2, def2);
							}
						}
						
						// asocia la medida derivada con el ppi
						ppi.setMeasuredBy(derivedMultiInstanceMeasure);
						
						// adiciona el PPI al reporte
						this.derivedMultiInstancePpiMap.get(processId).put(aggrId, ppi);
					} else {
						
						// en caso contrario, se trata de una medida agregada
						// crea la medida agregada, a la que se le asocia posteriormente la medida base
						AggregatedMeasure aggregatedMeasure = new AggregatedMeasure(aggrId, aggrName, "", "", "", aggrFunc, "", null);
						ppi.setMeasuredBy(aggregatedMeasure);
						// en dependencia del tipo de medida seleccionado, 
						// se crea la medida asociada a la agregada y se conserva el PPI en el map correspondiente
						switch ( Integer.valueOf(ppiIdAggr.get(i)) ) {
						case HistoryConst.TIMEMEASUREAGGR:
							
							// obtiene la seleccion del usuario
							String activityIdTimeIni = 	request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.INI_PREFIX + i );
							String activityIdTimeFin = 	request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.FIN_PREFIX + i );
							String atEndTimeIni = 	request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.ATENDINI_PREFIX + i );
							String atEndTimeFin = 	request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.ATENDFIN_PREFIX + i );
							
							// asocia la medida base
							aggregatedMeasure.setBaseMeasure( 
									new TimeInstanceMeasure(aggrInstId, aggrInstName, "", "", "",
											new TimeInstantCondition (activityIdTimeIni, new RuntimeState( atEndTimeIni!=null && atEndTimeIni.contentEquals("1") )), 
											new TimeInstantCondition (activityIdTimeFin, new RuntimeState( atEndTimeFin!=null && atEndTimeFin.contentEquals("1") )), 
											TimeMeasureType.LINEAR, ""));
							
							// adiciona el PPI al reporte
							this.timeAggregatedPpiMap.get(processId).put(aggrId, ppi);
							break;
						case HistoryConst.COUNTMEASUREAGGR:
	
							// obtiene la seleccion del usuario
							String activityIdCount = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.COUNT_PREFIX + i );
							String atEndTimeCount = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.ATEND_PREFIX + i );
	
							// asocia la medida base
							aggregatedMeasure.setBaseMeasure( 
									new CountInstanceMeasure(aggrInstId, aggrInstName, "", "", "",
											new TimeInstantCondition (activityIdCount, new RuntimeState( atEndTimeCount!=null && atEndTimeCount.contentEquals("1") ))));
							
							// adiciona el PPI al reporte
							this.countAggregatedPpiMap.get(processId).put(aggrId, ppi);
							break;
						case HistoryConst.STATECONDITIONMEASUREAGGR:
							
							// obtiene la seleccion del usuario
							String taskIdElement = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.ELEMENT_PREFIX + i );
							String stateIdElement = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.STATEELEMENT_PREFIX + i );
	
							// asocia la medida base
							aggregatedMeasure.setBaseMeasure( 
									new StateConditionInstanceMeasure(aggrInstId, aggrInstName, "", "", "",
											new StateCondition ( taskIdElement, new RuntimeState( stateIdElement ) )));
							
							// adiciona el PPI al reporte
							this.stateConditionAggregatedPpiMap.get(processId).put(aggrId, ppi);
							break;
						case HistoryConst.DATAMEASUREAGGR:
	
							// obtiene la seleccion del usuario
							String dataIdData = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.DATA_PREFIX + i );
							if (dataIdData==null) dataIdData = "";
	
							String antDataId = (String) request.getSession().getAttribute( HistoryConst.AGGR_PREFIX +"antDataId" + i);
							if (antDataId==null) antDataId = "";
							sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataId" + ii, dataIdData);
							
							String dataPropId = "";
							if (antDataId.contentEquals(dataIdData)) {
								dataPropId = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.DATAPROP_PREFIX + i );
							}
	
							// asocia la medida base
							aggregatedMeasure.setBaseMeasure( 
									new DataInstanceMeasure(aggrInstId, aggrInstName, "", "", "",
											new DataContentSelection(dataPropId, dataIdData, dataIdData), 
											new DataPropertyCondition("", "", new RuntimeState(), dataIdData) ));
							
							// adiciona el PPI al reporte
							this.dataAggregatedPpiMap.get(processId).put(aggrId, ppi);
							break;
						case HistoryConst.DATAPROPERTYCONDITIONMEASUREAGGR:
	
							// obtiene la seleccion del usuario
							String dataIdDataCond = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX + HistoryConst.DATACOND_PREFIX + i );
							if (dataIdDataCond==null) dataIdDataCond = "";
	
							String antDataIdCond = (String) request.getSession().getAttribute( HistoryConst.AGGR_PREFIX + "antDataIdCond" + i);
							if (antDataIdCond==null) antDataIdCond = "";
							sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataIdCond" + ii, dataIdDataCond);
	
							String dataStateId = "";
							if (antDataIdCond.contentEquals( dataIdDataCond )) {
								dataStateId = request.getParameter( "hd_" + HistoryConst.AGGR_PREFIX +  HistoryConst.DATASTATE_PREFIX + i );
							}
	
							// asocia la medida base
							aggregatedMeasure.setBaseMeasure( 
									new DataPropertyConditionInstanceMeasure(aggrInstId, aggrInstName, "", "", "",
											new DataPropertyCondition("", "", new RuntimeState(dataStateId), dataIdDataCond) ));
							
							// adiciona el PPI al reporte
							this.dataPropertyConditionAggregatedPpiMap.get(processId).put(aggrId, ppi);
							break;
						}
						
						ii++;
					}
				}
			}
		
			// si hay una instancia de proceso seleccionada y no se ha cambiado de instancia de proceso
			if (instanceId!="" && instanceId.contentEquals(antInstId)) {
						
				// obtiene la variable post con los ids seleccionados por el usuario
				List<String> ppiId = new ArrayList<String>();
				ppiId.add( request.getParameter( HistoryConst.PPIID_VARNAME ) );
				j = 1;
				while (request.getParameter( "hd_" + HistoryConst.PPIID_VARNAME + j )!=null) {
					
					ppiId.add( request.getParameter( "hd_" + HistoryConst.PPIID_VARNAME + j ) );
					j++;
				}

				// para cada una de las solicitudes de momentos de inicio o fin, y de medidas de instancia de proceso
				ii = 1;
				for(int i=0; i<ppiId.size(); i++) {
	
					// si efectivamente se ha realizado una seleccion
					if (ppiId.get(i)!=null && ppiId.get(i)!="") {
						
						// obtiene la seleccion del usuario, comun a todos a las solicitudes y a los PPI de medidas de instancia
						String name = request.getParameter( "hd_" + HistoryConst.NAME_PREFIX + i );
						String id = request.getParameter( "hd_" + HistoryConst.ID_PREFIX + i );

						// si se trata de una solicitud de inicio o fin
						if (Integer.valueOf(ppiId.get(i))==HistoryConst.ACTIVITYSTART || Integer.valueOf(ppiId.get(i))==HistoryConst.ACTIVITYEND ||
							Integer.valueOf(ppiId.get(i))==HistoryConst.POOLSTART || Integer.valueOf(ppiId.get(i))==HistoryConst.POOLEND) {
							
							switch ( Integer.valueOf(ppiId.get(i)) ) {
							case HistoryConst.ACTIVITYSTART:
							case HistoryConst.ACTIVITYEND:
								
								// obtiene la seleccion del usuario
								String activityIdStartEnd =	request.getParameter( "hd_" + HistoryConst.STARTEND_PREFIX + i );
								Boolean atEnd = ppiId.get(i).contentEquals( String.valueOf(HistoryConst.ACTIVITYEND) );
							
								// crea la solicitud
								ActivityStartEnd activityStartEnd = new ActivityStartEnd(name, activityIdStartEnd, atEnd);
								activityStartEnd.setCond( activityIdStartEnd!=null && activityIdStartEnd!="" );
								
								// adiciona la solicitud al reporte
								this.activityStartEndList.get(processId).add(activityStartEnd);
								break;
							case HistoryConst.POOLSTART:
							case HistoryConst.POOLEND:
								
								// obtiene la seleccion del usuario
								atEnd = ppiId.get(i).contentEquals( String.valueOf(HistoryConst.POOLEND) );
								
								// crea la solicitud
								PoolStartEnd poolStartEnd = new PoolStartEnd(name, atEnd);
								poolStartEnd.setCond( true );
								
								// adiciona la solicitud al reporte
								this.poolStartEndList.get(processId).add(poolStartEnd);
								break;
							}
						} else {
							
							// obtiene la seleccion del usuario, comun a todos los PPI de medidas de instancia
							Double refMax = null;
							if (request.getParameter( "hd_" + HistoryConst.REFMAX_PREFIX + i )!=null && request.getParameter( "hd_" + HistoryConst.REFMAX_PREFIX + i )!="")
								refMax = Double.valueOf(request.getParameter( "hd_" + HistoryConst.REFMAX_PREFIX + i ));
							Double refMin = null;
							if (request.getParameter( "hd_" + HistoryConst.REFMIN_PREFIX + i )!=null && request.getParameter( "hd_" + HistoryConst.REFMIN_PREFIX + i )!="")
								refMin = Double.valueOf(request.getParameter( "hd_" + HistoryConst.REFMIN_PREFIX + i ));
							
							// crea el PPI asociado a la medida 
							PPI ppi = new PPI(id + "_ppi", name + "_ppi", "", "", "", "", "",
						    		new Target(refMax, refMin), 
						    		new Scope());
							
							switch ( Integer.valueOf(ppiId.get(i)) ) {
							case HistoryConst.TIMEMEASURE:
								
								// obtiene la seleccion del usuario
								String activityIdTimeIni = 	request.getParameter( "hd_" + HistoryConst.INI_PREFIX + i );
								String activityIdTimeFin = 	request.getParameter( "hd_" + HistoryConst.FIN_PREFIX + i );
								String atEndTimeIni = 	request.getParameter( "hd_" + HistoryConst.ATENDINI_PREFIX + i );
								String atEndTimeFin = 	request.getParameter( "hd_" + HistoryConst.ATENDFIN_PREFIX + i );
								
								// crea la medida
								TimeInstanceMeasure timeInstanceMeasure = new TimeInstanceMeasure(id, name, "", "", "",
										new TimeInstantCondition (activityIdTimeIni, new RuntimeState( atEndTimeIni!=null && atEndTimeIni.contentEquals("1") )), 
										new TimeInstantCondition (activityIdTimeFin, new RuntimeState( atEndTimeFin!=null && atEndTimeFin.contentEquals("1") )), 
										TimeMeasureType.LINEAR, "");
								
								// asocia la medida al ppi
								ppi.setMeasuredBy(timeInstanceMeasure);
								
								// adiciona la medida al reporte
								this.timeInstancePpiMap.get(processId).put(id, ppi);
								break;
							case HistoryConst.COUNTMEASURE:
	
								// obtiene la seleccion del usuario
								String activityIdCount = request.getParameter( "hd_" + HistoryConst.COUNT_PREFIX + i );
								String atEndTimeCount = request.getParameter( "hd_" + HistoryConst.ATEND_PREFIX + i );
								
								// crea la medida
								CountInstanceMeasure countInstanceMeasure = new CountInstanceMeasure(id, name, "", "", "",
										new TimeInstantCondition (activityIdCount, new RuntimeState( atEndTimeCount!=null && atEndTimeCount.contentEquals("1") )));
								
								// asocia la medida al ppi
								ppi.setMeasuredBy(countInstanceMeasure);
								
								// adiciona la medida al reporte
								this.countInstancePpiMap.get(processId).put(id, ppi);
								break;
							case HistoryConst.STATECONDITIONMEASURE:
								
								// obtiene la seleccion del usuario
								String taskIdElement = request.getParameter( "hd_" + HistoryConst.ELEMENT_PREFIX + i );
								String stateIdElement = request.getParameter( "hd_" + HistoryConst.STATEELEMENT_PREFIX + i );
	
								// crea la medida
								StateConditionInstanceMeasure stateConditionInstanceMeasure = new StateConditionInstanceMeasure(id, name, "", "", "",
										new StateCondition ( taskIdElement, new RuntimeState( stateIdElement ) ));
								
								// asocia la medida al ppi
								ppi.setMeasuredBy(stateConditionInstanceMeasure);
								
								// adiciona la medida al reporte
								this.stateConditionInstancePpiMap.get(processId).put(id, ppi);
								break;
							case HistoryConst.DATAMEASURE:
	
								// obtiene la seleccion del usuario
								String dataIdData = request.getParameter( "hd_" + HistoryConst.DATA_PREFIX + i );
								if (dataIdData==null) dataIdData = "";
	
								String antDataId = (String) request.getSession().getAttribute("antDataId" + i);
								if (antDataId==null) antDataId = "";
								sessionTmp.put("antDataId" + ii, dataIdData);
	
								String dataPropId = "";
								if (antDataId.contentEquals(dataIdData)) {
									dataPropId = request.getParameter( "hd_" + HistoryConst.DATAPROP_PREFIX + i );
								}

								// crea la medida
								DataInstanceMeasure dataInstanceMeasure = new DataInstanceMeasure(id, name, "", "", "",
										new DataContentSelection(dataPropId, dataIdData, dataIdData), 
										new DataPropertyCondition("", "", new RuntimeState(), dataIdData) );
								
								// asocia la medida al ppi
								ppi.setMeasuredBy(dataInstanceMeasure);
								
								// adiciona la medida al reporte
								this.dataInstancePpiMap.get(processId).put(id, ppi);
								break;
							case HistoryConst.DATAPROPERTYCONDITIONMEASURE:
	
								// obtiene la seleccion del usuario
								String dataIdDataCond = request.getParameter( "hd_" + HistoryConst.DATACOND_PREFIX + i );
								if (dataIdDataCond==null) dataIdDataCond = "";
	
								String antDataIdCond = (String) request.getSession().getAttribute("antDataIdCond" + i);
								if (antDataIdCond==null) antDataIdCond = "";
								sessionTmp.put("antDataIdCond" + ii, dataIdDataCond);
	
								String dataStateId = "";
								if (antDataIdCond.contentEquals( dataIdDataCond )) {
									dataStateId = request.getParameter( "hd_" + HistoryConst.DATASTATE_PREFIX + i );
								}
	
								// crea la medida
								DataPropertyConditionInstanceMeasure dataPropertyConditionInstanceMeasure = new DataPropertyConditionInstanceMeasure(id, name, "", "", "",
										new DataPropertyCondition("", "", new RuntimeState(dataStateId), dataIdDataCond) );
								
								// asocia la medida al ppi
								ppi.setMeasuredBy(dataPropertyConditionInstanceMeasure);
								
								// adiciona la medida al reporte
								this.dataPropertyConditionInstancePpiMap.get(processId).put(id, ppi);
								break;
							case HistoryConst.DERIVEDSINGLEINSTANCEMEASURE:
								String derFunc = request.getParameter( "hd_" + HistoryConst.DERFUNC_PREFIX + i );
								String idOper1 = request.getParameter( "hd_" + HistoryConst.IDOPER1_PREFIX + i );
								String idOper2 = request.getParameter( "hd_" + HistoryConst.IDOPER2_PREFIX + i );
								String medOper1 = request.getParameter( "hd_" + HistoryConst.MEDOPER1_PREFIX + i );
								String medOper2 = request.getParameter( "hd_" + HistoryConst.MEDOPER2_PREFIX + i );

								derFunc = (derFunc==null)?"":derFunc;
								idOper1 = (idOper1==null)?"":idOper1;
								idOper2 = (idOper2==null)?"":idOper2;
								medOper1 = (medOper1==null)?"":medOper1;
								medOper2 = (medOper2==null)?"":medOper2;
								
								// inserta una medida derivada de proceso
								DerivedSingleInstanceMeasure derivedSingleInstanceMeasure = new DerivedSingleInstanceMeasure(id, name, "", "", "",
										derFunc);
								
								if (!idOper1.contentEquals("") && !medOper1.contentEquals("")) {
									
									MeasureDefinition def1 = this.findInstanceMeasure(processId, medOper1);
									if (def1!=null)
										derivedSingleInstanceMeasure.addUsedMeasure(idOper1, def1);
									
									if (!idOper2.contentEquals("") && !medOper2.contentEquals("")) {
										
										MeasureDefinition def2 = this.findInstanceMeasure(processId, medOper2);
										if (def2!=null)
											derivedSingleInstanceMeasure.addUsedMeasure(idOper2, def2);
									}
								}

								
								// asocia la medida al ppi
								ppi.setMeasuredBy(derivedSingleInstanceMeasure);
								
								// adiciona la medida al reporte
								this.derivedSingleInstancePpiMap.get(processId).put(id, ppi);
								break;
							} // switch
						}
						ii++;
					} // if (ppiId[i]!=null && ppiId[i]!="")
				} // for( int i=0; i<ppiId.length; i++)
			} // if (instanceId!="" && instanceId.contentEquals(antInstId))
			
			Iterator<Entry<String, Object>> itInst = sessionTmp.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)itInst.next();
		        String key = (String) pairs.getKey();
		        Object value = pairs.getValue();
		        
				request.getSession().setAttribute(key, value);
		    }

			// determina los valores de los indicadores
			this.calculatePpiValues(processId);
		} // if (processId!="" && processId.contentEquals(antProcId))
	}
	
	private void obtainActivityStartEnd(String processId) {
			
		for(ActivityStartEnd activityStartEnd : this.activityStartEndList.get(processId)) {
			
			activityStartEnd.iniValue();
			if (activityStartEnd.getCond()) {
				
				// obtiene el momento en que se inicia o termina la actividad seleccionada
				try {
					
					activityStartEnd.setValueString( 
							PpiNotModelUtils.formatStringHour(
									HistoryDoMeasure.doActivityTimeInstanceCondition( this.instanceIdMap.get(processId), 
																						activityStartEnd.getId(), 
																						activityStartEnd.getAtEnd() )));
				} catch (NullTimeException ex) {
					
					activityStartEnd.setValueString( "En ejecuci&oacute;n" );
				}
			}
		}
	}
	
	private void obtainPoolStartEnd(String processId) {

		for(PoolStartEnd poolStartEnd : this.poolStartEndList.get(processId)) {

			poolStartEnd.iniValue();
			if (poolStartEnd.getCond()) {
				
				// obtiene el momento en que se inicia o termina la instancia de proceso
				try {
					
					poolStartEnd.setValueString( 
							PpiNotModelUtils.formatStringHour(
									HistoryDoMeasure.doPoolTimeInstanceCondition( this.instanceIdMap.get(processId), 
																					poolStartEnd.getAtEnd() ) ) );
				} catch (NullTimeException ex) {
					
					poolStartEnd.setValueString("En ejecuci&oacute;n" );
				}
			}
		}
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas TimeInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateTimeInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();

		Iterator<Entry<String, PPI>> itInst = this.timeInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI:
				// tiempo transcurrido en milisegundo desde que se ejecuto por primera vez la actividad inicial
				// hasta que se ejecuto por ultima vez la actividad final. En ambas actividades el tiempo se toma
				// al inicio o al final segun se indique
				try {
					
			        TimeInstanceMeasure timeInstanceMeasure = (TimeInstanceMeasure) ppi.getMeasuredBy();
					ppi.getValueString().add(String.valueOf( 
							HistoryDoMeasure.doTimeMeasure( this.instanceIdMap.get(processId), 
																timeInstanceMeasure.getFrom().getAppliesTo(), 
																timeInstanceMeasure.getFrom().getChangesToState().getState()==GenericState.END, 
																timeInstanceMeasure.getTo().getAppliesTo(), 
																timeInstanceMeasure.getTo().getChangesToState().getState()==GenericState.END ) ) );
					
					// evalua la satisfaccion del indicador
					ppi.evaluate();
					
					// para calcular la satisfaccion promedio
					if (ppi.getNormalized().size()>0) {
						Double average = ppi.averageNormalize();
						if (average!=null) {
							result.success += average;
							result.cant++;
						}
					}
				} catch (Exception ex) {
					
					ppi.getValueString().add("En ejecuci&oacute;n" );
				}
			}
		}
	    
	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas CountInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateCountInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
		Iterator<Entry<String, PPI>> itInst = this.countInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI:
				// cuenta la cantidad de veces que la actividad se inicio o finalizo, segun se indique
				CountInstanceMeasure countInstanceMeasure = (CountInstanceMeasure) ppi.getMeasuredBy();
				ppi.getValueString().add(String.valueOf( 
						HistoryDoMeasure.doCountMeasure( this.instanceIdMap.get(processId), 
															countInstanceMeasure.getWhen().getAppliesTo(), 
															countInstanceMeasure.getWhen().getChangesToState().getState()==GenericState.END ) ));
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
				
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas StateConditionInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateStateConditionInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>>  itInst = this.stateConditionInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI:
				// si la tarea se encuentra en el estado indicado
				try {
					
					StateConditionInstanceMeasure stateConditionInstanceMeasure = (StateConditionInstanceMeasure) ppi.getMeasuredBy();
					ppi.getValueString().add(String.valueOf( 
							HistoryDoMeasure.doStateConditionMeasure( this.instanceIdMap.get(processId), 
																		stateConditionInstanceMeasure.getCondition().getAppliesTo(), 
																		stateConditionInstanceMeasure.getCondition().getState().getStateString()) ) );
					
					// evalua la satisfaccion del indicador
					ppi.evaluate();
					
					// para calcular la satisfaccion promedio
					if (ppi.getNormalized().size()>0) {
						Double average = ppi.averageNormalize();
						if (average!=null) {
							result.success += average;
							result.cant++;
						}
					}
				} catch (NullActivityException e) {
					
					ppi.getValueString().add("No ejecutada" );
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas DataInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDataInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>>  itInst = this.dataInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI:
				// devuelve el valor de la propiedad seleccionada del dataobject
				DataInstanceMeasure dataInstanceMeasure = (DataInstanceMeasure) ppi.getMeasuredBy();
				Object value = HistoryDoMeasure.doDataMeasure( this.instanceIdMap.get(processId), 
																dataInstanceMeasure.getDataContentSelection().getDataobject(), 
																dataInstanceMeasure.getDataContentSelection().getSelection());
				if (value==null)
					ppi.getValueString().add( "" );
				else
					ppi.getValueString().add( value.toString() );
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
				
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas DataPropertyConditionInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDataPropertyConditionInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst = this.dataPropertyConditionInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI:
				// indica si el dataobject se encuentra en el estado seleccionado o no
				DataPropertyConditionInstanceMeasure dataPropertyConditionInstanceMeasure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();
				ppi.getValueString().add(String.valueOf( 
						HistoryDoMeasure.doDataPropertyConditionMeasure( this.instanceIdMap.get(processId), 
																			dataPropertyConditionInstanceMeasure.getCondition().getDataobject(), 
																			dataPropertyConditionInstanceMeasure.getCondition().getStateConsidered().getStateString()) ) );
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
				
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas DerivedSingleInstance seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDerivedSingleInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst1 = this.derivedSingleInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				DerivedSingleInstanceMeasure derivedSingleInstanceMeasure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();
				
				Map<String, PPI> tmpPpiMap = new HashMap<String, PPI>();
				
				String noejecutada = null;
				Iterator<Entry<String, MeasureDefinition>> itInst = derivedSingleInstanceMeasure.getUsedMeasureMap().entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
			        String variable = pairs.getKey();
			    	MeasureDefinition used = pairs.getValue();
				
			    	PPI usedPpi = this.findInstancePPI(processId, used.getId());
					
					if (usedPpi.getValueString().get(0).contentEquals("En ejecuci&oacute;n") ||
							usedPpi.getValueString().get(0).contentEquals("No ejecutada")) {
						
						noejecutada = usedPpi.getValueString().get(0);
						break;
					} else
						tmpPpiMap.put(variable, usedPpi);
			    }
				
				if (noejecutada!=null)
					
					ppi.getValueString().add( noejecutada );
				else {
					
					// calcula el valor del ppi asociado a la medida derivada
					try {
						
						ppi.getValueString().add( String.valueOf( 
							HistoryUtils.applyFunc( derivedSingleInstanceMeasure.getFunction(), 0, tmpPpiMap ) ) );
					} catch (Exception e) {
						
						ppi.getValueString().add( String.valueOf( 0.0 ) );
					}
					
					// evalua la satisfaccion del indicador
					ppi.evaluate();
					
					// para calcular la satisfaccion promedio
					if (ppi.getNormalized().size()>0) {
						Double average = ppi.averageNormalize();
						if (average!=null) {
							result.success += average;
							result.cant++;
						}
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas agregadas de TimeInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateTimeAggregatedPpis(String processId) {
		
		CalculateResult result = new CalculateResult();
			
	    Iterator<Entry<String, PPI>> itInst1 = this.timeAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				Map<Date, Date> dateList = HistoryUtils.createDateMap( ppi.getScope().getStartDate(), 
																ppi.getScope().getEndDate(), 
																ppi.getScope().getYear(), 
																ppi.getScope().getPeriod());

				AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				Iterator<Entry<Date, Date>> itInst = dateList.entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>)itInst.next();
			        Date startDate = (Date) pairs.getKey();
			        Date endDate = (Date) pairs.getValue();
				
					// calcula el valor agregado de la medida, de acuerdo a la funcion indicada
			        ppi.getValueString().add(Double.toString( 
								        		HistoryDoMeasure.doTimeAggregatedMeasure( processId, 
												aggregatedMeasure.getAggregationFunction(), 
												startDate, 
												endDate, 
												ppi.getScope().getInStart(), 
												ppi.getScope().getInEnd(), 
												(TimeInstanceMeasure) aggregatedMeasure.getBaseMeasure()) ) );
			    }
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
			    
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas agregadas de CountInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateCountAggregatedPpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst1 = this.countAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				Map<Date, Date> dateList = HistoryUtils.createDateMap( ppi.getScope().getStartDate(), 
																ppi.getScope().getEndDate(), 
																ppi.getScope().getYear(), 
																ppi.getScope().getPeriod());

				AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				Iterator<Entry<Date, Date>> itInst = dateList.entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>)itInst.next();
			        Date startDate = (Date) pairs.getKey();
			        Date endDate = (Date) pairs.getValue();
				
					// calcula el valor agregado de la medida, de acuerdo a la funcion indicada
			        ppi.getValueString().add(Double.toString( 
			        		HistoryDoMeasure.doCountAggregatedMeasure( processId, 
							aggregatedMeasure.getAggregationFunction(), 
							startDate, 
							endDate, 
							ppi.getScope().getInStart(), 
							ppi.getScope().getInEnd(), 
							(CountInstanceMeasure) aggregatedMeasure.getBaseMeasure()) ) );
			    }
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
			    
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas agregadas de StateConditionInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateStateConditionAggregatedPpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst1 = this.stateConditionAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				Map<Date, Date> dateList = HistoryUtils.createDateMap( ppi.getScope().getStartDate(), 
																ppi.getScope().getEndDate(), 
																ppi.getScope().getYear(), 
																ppi.getScope().getPeriod());

				AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				Iterator<Entry<Date, Date>> itInst = dateList.entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>)itInst.next();
			        Date startDate = (Date) pairs.getKey();
			        Date endDate = (Date) pairs.getValue();
				
					// calcula el valor agregado de la medida, de acuerdo a la funcion indicada
			        ppi.getValueString().add(Double.toString( 
								        		HistoryDoMeasure.doStateConditionAggregatedMeasure( processId, 
												aggregatedMeasure.getAggregationFunction(), 
												startDate, 
												endDate, 
												ppi.getScope().getInStart(), 
												ppi.getScope().getInEnd(), 
												(StateConditionInstanceMeasure) aggregatedMeasure.getBaseMeasure()) ) );
			    }
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
			    
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas agregadas de DataInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDataAggregatedPpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst1 = this.dataAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				Map<Date, Date> dateList = HistoryUtils.createDateMap( ppi.getScope().getStartDate(), 
																ppi.getScope().getEndDate(), 
																ppi.getScope().getYear(), 
																ppi.getScope().getPeriod());

				AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				Iterator<Entry<Date, Date>> itInst = dateList.entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>)itInst.next();
			        Date startDate = (Date) pairs.getKey();
			        Date endDate = (Date) pairs.getValue();
				
					// calcula el valor agregado de la medida, de acuerdo a la funcion indicada
			        Object value = HistoryDoMeasure.doDataAggregatedMeasure( processId, 
																				aggregatedMeasure.getAggregationFunction(), 
																				startDate, 
																				endDate, 
																				ppi.getScope().getInStart(), 
																				ppi.getScope().getInEnd(), 
																				(DataInstanceMeasure) aggregatedMeasure.getBaseMeasure());
					if (value==null)
						ppi.getValueString().add( "" );
					else {
						ppi.getValueString().add( value.toString() );
					}
			    }
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
			    
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas agregadas de DataPropertyConditionInstanceMeasure seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDataPropertyConditionAggregatedPpis(String processId) {
		
		CalculateResult result = new CalculateResult();
		
	    Iterator<Entry<String, PPI>> itInst1 = this.dataPropertyConditionAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				Map<Date, Date> dateList = HistoryUtils.createDateMap( ppi.getScope().getStartDate(), 
																ppi.getScope().getEndDate(), 
																ppi.getScope().getYear(), 
																ppi.getScope().getPeriod());

				AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				Iterator<Entry<Date, Date>> itInst = dateList.entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<Date, Date> pairs = (Map.Entry<Date, Date>)itInst.next();
			        Date startDate = (Date) pairs.getKey();
			        Date endDate = (Date) pairs.getValue();
				
					// calcula el valor agregado de la medida, de acuerdo a la funcion indicada
			        ppi.getValueString().add(Double.toString( 
								        		HistoryDoMeasure.doDataPropertyConditionAggregatedMeasure( processId, 
												aggregatedMeasure.getAggregationFunction(), 
												startDate, 
												endDate, 
												ppi.getScope().getInStart(), 
												ppi.getScope().getInEnd(), 
												(DataPropertyConditionInstanceMeasure) aggregatedMeasure.getBaseMeasure()) ) );
			    }
				
				// evalua la satisfaccion del indicador
				ppi.evaluate();
			    
				// para calcular la satisfaccion promedio
				if (ppi.getNormalized().size()>0) {
					Double average = ppi.averageNormalize();
					if (average!=null) {
						result.success += average;
						result.cant++;
					}
				}
			}
		}

	    return result;
	}
	
	/**
	 * Determina el valor de los PPI asociados a medidas DerivedMultiInstance seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 * @return Objeto CalculateResult con la cantidad de indicadores evaluados y la suma del grado de satisfaccion de estos 
	 */
	private CalculateResult calculateDerivedMultiInstancePpis(String processId) {
		
		CalculateResult result = new CalculateResult();
			
	    Iterator<Entry<String, PPI>> itInst1 = this.derivedMultiInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, PPI> pairs1 = (Map.Entry<String, PPI>)itInst1.next();
	        PPI ppi = (PPI) pairs1.getValue();
			
			ppi.iniValues();
			if (ppi.getCond()) {
				
				// calcula el valor del PPI
				String noejecutada = null;
				DerivedMultiInstanceMeasure derivedMultiInstanceMeasure = (DerivedMultiInstanceMeasure) ppi.getMeasuredBy();
				Map<String, PPI> tmpPpiMap = new HashMap<String, PPI>();
				Iterator<Entry<String, MeasureDefinition>> itInst = derivedMultiInstanceMeasure.getUsedMeasureMap().entrySet().iterator();
		        int cantPeriodos = 0;
			    while (itInst.hasNext()) {
			        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
			        String variable = pairs.getKey();
			    	MeasureDefinition used = pairs.getValue();
					
			    	PPI usedPpi = this.findAggregatedPPI(processId, used.getId());
				    

					if (usedPpi.getValueString().get(0).contentEquals("En ejecuci&oacute;n") ||
							usedPpi.getValueString().get(0).contentEquals("No ejecutada")) {
						
						noejecutada = usedPpi.getValueString().get(0);
						break;
					} else {
						
						tmpPpiMap.put(variable, usedPpi);
						if (cantPeriodos==0 || usedPpi.getValueString().size()<cantPeriodos)
							cantPeriodos = usedPpi.getValueString().size();
					}
			    }
				
				if (noejecutada!=null)
					
					ppi.getValueString().add( noejecutada );
				else {
			    
					// calcula el valor de la medida derivada
				    for (Integer i=0; i<cantPeriodos; i++) {
					
						try {
					    	ppi.getValueString().add( String.valueOf( 
								HistoryUtils.applyFunc( derivedMultiInstanceMeasure.getFunction(), i, tmpPpiMap ) ) );
						} catch (Exception e) {
	
							ppi.getValueString().add( String.valueOf( 0.0 ) );
						}
				    }
				    
					// evalua la satisfaccion del indicador
					ppi.evaluate();
					
					// para calcular la satisfaccion promedio
					if (ppi.getNormalized().size()>0) {
						
						Double average = ppi.averageNormalize();
						if (average!=null) {
							result.success += average;
							result.cant++;
						}
					}
				}
			}
		}

	    return result;
	}
		
	/**
	 * Determina el valor de los indicadores seleccionados en un proceso
	 * 
	 * @param processId Id del proceso
	 */
	private void calculatePpiValues(String processId) {

		// si hay una instancia seleccionada, obtiene las solicitudes de momentos de inicio o fin, 
		// y calcula los PPI asociados con medidas de instancias
		if (this.instanceIdMap.get(processId)!="") {
			
			this.obtainActivityStartEnd(processId);
			this.obtainPoolStartEnd(processId);
			
			List<CalculateResult> resultList = new ArrayList<CalculateResult>();
			
			resultList.add( this.calculateTimeInstancePpis(processId) );
			resultList.add( this.calculateCountInstancePpis(processId) );
			resultList.add( this.calculateStateConditionInstancePpis(processId) );
			resultList.add( this.calculateDataInstancePpis(processId) );
			resultList.add( this.calculateDataPropertyConditionInstancePpis(processId) );
			resultList.add( this.calculateDerivedSingleInstancePpis(processId) );
			
			Double instSuccess = 0.0;
			Integer instCant = 0;
			for (CalculateResult result : resultList) {
				
				instSuccess += result.success;
				instCant += result.cant;
			}
			
			if (instCant!=0)
				this.instanceSuccessMap.put(processId, instSuccess / instCant);
		}
		
		// calcula los ppi de medidad agregadas y derivadas multiinstance
		List<CalculateResult> resultList = new ArrayList<CalculateResult>();
		
		resultList.add( this.calculateTimeAggregatedPpis(processId) );
		resultList.add( this.calculateCountAggregatedPpis(processId) );
		resultList.add( this.calculateStateConditionAggregatedPpis(processId) );
		resultList.add( this.calculateDataAggregatedPpis(processId) );
		resultList.add( this.calculateDataPropertyConditionAggregatedPpis(processId) );
		resultList.add( this.calculateDerivedMultiInstancePpis(processId) );
		
		Double procSuccess = 0.0;
		Integer procCant = 0;
		for (CalculateResult result : resultList) {
			
			procSuccess += result.success;
			procCant += result.cant;
		}

		if (procCant!=0)
			this.processSuccessMap.put(processId, procSuccess / procCant);
	}
	
	/**
	 * Hace el reporte a partir de un xml
	 * 
	 * @param request Objeto para manejar las variables de sesion
	 * @param camino Camino del archivo xml
	 * @param nomFich Nombre del archivo
	 * @throws JAXBException
	 */
	public void doReportFromXML(HttpServletRequest request, String camino, String nomFich) throws JAXBException {
		
		// crea el objeto para leer el xml
		PpiNotModelHandler ppiNotXmlExtracter = new PpiNotModelHandler();
		ppiNotXmlExtracter.load(camino, nomFich);
		
		String processId = ppiNotXmlExtracter.getProcId();
		this.iniProcessReport( processId, true );

		this.selectedProcessId = processId;
		
		this.iniProcessReport(processId, true);
		
	    Iterator<Entry<String, PPI>> itInst = ppiNotXmlExtracter.getPpiModelMap().entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
	        
	        MeasureDefinition measure = ppi.getMeasuredBy();
	        if (measure instanceof TimeInstanceMeasure)
	    		this.timeInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof CountInstanceMeasure)
	    		this.countInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof StateConditionInstanceMeasure)
	    		this.stateConditionInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof DataInstanceMeasure)
	    		this.dataInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof DataPropertyConditionInstanceMeasure)
	    		this.dataPropertyConditionInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        
	        if (measure instanceof DerivedSingleInstanceMeasure)
	    		this.derivedSingleInstancePpiMap.get(processId).put(measure.getId(), ppi);
	        else	

	        if (measure instanceof AggregatedMeasure && ((AggregatedMeasure) measure).getBaseMeasure() instanceof TimeInstanceMeasure)
	    		this.timeAggregatedPpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof AggregatedMeasure && ((AggregatedMeasure) measure).getBaseMeasure() instanceof CountInstanceMeasure)
	    		this.countAggregatedPpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof AggregatedMeasure && ((AggregatedMeasure) measure).getBaseMeasure() instanceof StateConditionInstanceMeasure)
	    		this.stateConditionAggregatedPpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof AggregatedMeasure && ((AggregatedMeasure) measure).getBaseMeasure() instanceof DataInstanceMeasure)
	    		this.dataAggregatedPpiMap.get(processId).put(measure.getId(), ppi);
	        else	
	        if (measure instanceof AggregatedMeasure && ((AggregatedMeasure) measure).getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure)
	    		this.dataPropertyConditionAggregatedPpiMap.get(processId).put(measure.getId(), ppi);
	        else	
		        
	        if (measure instanceof DerivedMultiInstanceMeasure)
	    		this.derivedMultiInstancePpiMap.get(processId).put(measure.getId(), ppi);
	    }
		
		// selecciona la ultima instancia del proceso
		Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData( processId, null, null, true, true );
		if (map.size()>0) {
			
			Object[] keys = map.keySet().toArray();
			String lastKey = (String) keys[keys.length-1];
			this.instanceIdMap.put(processId, map.get(lastKey).getInstanceId());
		}

		// da valor a variables de sesion  
		Map<String, Object> sessionTmp = new LinkedHashMap<String, Object>();
		
		Integer ii = 1 + this.activityStartEndList.size() + this.poolStartEndList.size() + this.timeInstancePpiMap.size() +
				this.countInstancePpiMap.size() + this.stateConditionInstancePpiMap.size();

	    itInst = this.dataInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
	        
	        DataInstanceMeasure dataInstanceMeasure = (DataInstanceMeasure) ppi.getMeasuredBy();

			sessionTmp.put("antDataId" + ii, dataInstanceMeasure.getDataContentSelection().getDataobject());
			ii++;
		}

		itInst = this.dataPropertyConditionInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();

	        DataPropertyConditionInstanceMeasure dataPropertyConditionInstanceMeasure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();
	        
			sessionTmp.put("antDataIdCond" + ii, dataPropertyConditionInstanceMeasure.getCondition().getDataobject());
			ii++;
		}

		itInst = this.derivedSingleInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
			
	        DerivedSingleInstanceMeasure derivedSingleInstanceMeasure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();
	        
			Iterator<Entry<String, MeasureDefinition>> itInst1 = derivedSingleInstanceMeasure.getUsedMeasureMap().entrySet().iterator();
		    while (itInst1.hasNext()) {
		        Map.Entry<String, MeasureDefinition> pairs1 = (Map.Entry<String, MeasureDefinition>)itInst1.next();
		    	MeasureDefinition used = pairs1.getValue();

		    	if (used instanceof DataInstanceMeasure){
				
					sessionTmp.put("antDataId" + ii, ((DataInstanceMeasure) used).getDataContentSelection().getDataobject());
				}
		    	else
				if (used instanceof DataPropertyConditionInstanceMeasure){
					
					sessionTmp.put("antDataIdCond" + ii, ((DataPropertyConditionInstanceMeasure) used).getCondition().getDataobject());
				}
				ii++;
		    }
			ii++;
		}
		
		ii = 1 + this.timeAggregatedPpiMap.get(processId).size() + this.countAggregatedPpiMap.get(processId).size() + 
				this.stateConditionAggregatedPpiMap.get(processId).size();

		itInst = this.dataAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
	        
	        AggregatedMeasure dataAggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

			sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataId" + ii, ((DataInstanceMeasure) dataAggregatedMeasure.getBaseMeasure()).getDataContentSelection().getDataobject());
			ii++;
		}

		itInst = this.dataPropertyConditionAggregatedPpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
	        
	        AggregatedMeasure dataPropertyConditionAggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

			sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataIdCond" + ii, ((DataPropertyConditionInstanceMeasure) dataPropertyConditionAggregatedMeasure.getBaseMeasure()).getCondition().getDataobject());
			ii++;
		}

		itInst = this.derivedMultiInstancePpiMap.get(processId).entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
	        PPI ppi = (PPI) pairs.getValue();
	        
	        DerivedMultiInstanceMeasure derivedMultiInstanceMeasure = (DerivedMultiInstanceMeasure) ppi.getMeasuredBy();

			Iterator<Entry<String, MeasureDefinition>> itInst1 = derivedMultiInstanceMeasure.getUsedMeasureMap().entrySet().iterator();
		    while (itInst1.hasNext()) {
		        Map.Entry<String, MeasureDefinition> pairs1 = (Map.Entry<String, MeasureDefinition>)itInst1.next();
		    	MeasureDefinition used = pairs1.getValue();

				if (used instanceof AggregatedMeasure && ((AggregatedMeasure) used).getBaseMeasure() instanceof DataInstanceMeasure){
					
					sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataId" + ii, ((DataInstanceMeasure) ((AggregatedMeasure) used).getBaseMeasure()).getDataContentSelection().getDataobject());
				}
				else
				if (used instanceof AggregatedMeasure && ((AggregatedMeasure) used).getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure){
					
					sessionTmp.put(HistoryConst.AGGR_PREFIX +"antDataIdCond" + ii, ((DataPropertyConditionInstanceMeasure) ((AggregatedMeasure) used).getBaseMeasure()).getCondition().getDataobject());
				}
				ii++;
		    }
			ii++;
		}
		
		Iterator<Entry<String, Object>> itInst1 = sessionTmp.entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, Object> pairs1 = (Map.Entry<String, Object>)itInst1.next();
	        String key = (String) pairs1.getKey();
	        Object value = pairs1.getValue();
	        
			request.getSession().setAttribute(key, value);
	    }
		
		// determina los valores de los indicadores
		this.calculatePpiValues(processId);
	}

	public void xmlExport(String path, String nomFich) throws JAXBException {

		Map<String, TimeInstanceMeasure> timeInstanceModelMap = new HashMap<String, TimeInstanceMeasure>();
		Map<String, CountInstanceMeasure> countInstanceModelMap = new HashMap<String, CountInstanceMeasure>();
		Map<String, StateConditionInstanceMeasure> stateConditionInstanceModelMap = new HashMap<String, StateConditionInstanceMeasure>();
		Map<String, DataInstanceMeasure> dataInstanceModelMap = new HashMap<String, DataInstanceMeasure>();
		Map<String, DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceModelMap = new HashMap<String, DataPropertyConditionInstanceMeasure>();

		Map<String, AggregatedMeasure> timeAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();
		Map<String, AggregatedMeasure> countAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();
		Map<String, AggregatedMeasure> stateConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();
		Map<String, AggregatedMeasure> dataAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();
		Map<String, AggregatedMeasure> dataPropertyConditionAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();
		Map<String, AggregatedMeasure> derivedSingleInstanceAggregatedModelMap = new HashMap<String, AggregatedMeasure> ();

		Map<String, DerivedMeasure> derivedSingleInstanceModelMap = new HashMap<String, DerivedMeasure> ();
		Map<String, DerivedMeasure> derivedMultiInstanceModelMap = new HashMap<String, DerivedMeasure> ();

		Map<String, PPI> ppiModelMap = new HashMap<String, PPI>();
		
		String processId = this.selectedProcessId;
		
		if (this.timeInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.timeInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        TimeInstanceMeasure measure = (TimeInstanceMeasure) ppi.getMeasuredBy();
		        timeInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
		
		if (this.countInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.countInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        CountInstanceMeasure measure = (CountInstanceMeasure) ppi.getMeasuredBy();
		        countInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
		
		if (this.stateConditionInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.stateConditionInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        StateConditionInstanceMeasure measure = (StateConditionInstanceMeasure) ppi.getMeasuredBy();
		        stateConditionInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
		
		if (this.dataInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.dataInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DataInstanceMeasure measure = (DataInstanceMeasure) ppi.getMeasuredBy();
		        dataInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.dataPropertyConditionInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.dataPropertyConditionInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DataPropertyConditionInstanceMeasure measure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();
		        dataPropertyConditionInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.derivedSingleInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.derivedSingleInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DerivedSingleInstanceMeasure measure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();
		        derivedSingleInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.timeAggregatedPpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.timeAggregatedPpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        AggregatedMeasure measure = (AggregatedMeasure) ppi.getMeasuredBy();
		        timeAggregatedModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.countAggregatedPpiMap.containsKey(processId)) {
				
		    Iterator<Entry<String, PPI>> itInst = this.countAggregatedPpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        AggregatedMeasure measure = (AggregatedMeasure) ppi.getMeasuredBy();
		        countAggregatedModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.stateConditionAggregatedPpiMap.containsKey(processId)) {
				
		    Iterator<Entry<String, PPI>> itInst = this.stateConditionAggregatedPpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        AggregatedMeasure measure = (AggregatedMeasure) ppi.getMeasuredBy();
		        stateConditionAggregatedModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.dataAggregatedPpiMap.containsKey(processId)) {
				
		    Iterator<Entry<String, PPI>> itInst = this.dataAggregatedPpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        AggregatedMeasure measure = (AggregatedMeasure) ppi.getMeasuredBy();
		        dataAggregatedModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.dataPropertyConditionAggregatedPpiMap.containsKey(processId)) {
				
		    Iterator<Entry<String, PPI>> itInst = this.dataPropertyConditionAggregatedPpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        AggregatedMeasure measure = (AggregatedMeasure) ppi.getMeasuredBy();
		        dataPropertyConditionAggregatedModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}
			
		if (this.derivedMultiInstancePpiMap.containsKey(processId)) {
				
		    Iterator<Entry<String, PPI>> itInst = this.derivedMultiInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DerivedMultiInstanceMeasure measure = (DerivedMultiInstanceMeasure) ppi.getMeasuredBy();
		        derivedMultiInstanceModelMap.put(measure.getId(), measure);
		        ppiModelMap.put(ppi.getId(), ppi);
		    }
		}

		PpiNotModelHandler ppiNotModelHandler = new PpiNotModelHandler();
	    
		ppiNotModelHandler.setTimeModelMap(timeInstanceModelMap);
		ppiNotModelHandler.setCountModelMap(countInstanceModelMap);
		ppiNotModelHandler.setStateConditionModelMap(stateConditionInstanceModelMap);
		ppiNotModelHandler.setDataModelMap(dataInstanceModelMap);
		ppiNotModelHandler.setDataPropertyConditionModelMap(dataPropertyConditionInstanceModelMap);

		ppiNotModelHandler.setTimeAggregatedModelMap(timeAggregatedModelMap);
		ppiNotModelHandler.setCountAggregatedModelMap(countAggregatedModelMap);
		ppiNotModelHandler.setStateConditionAggregatedModelMap(stateConditionAggregatedModelMap);
		ppiNotModelHandler.setDataAggregatedModelMap(dataAggregatedModelMap);
		ppiNotModelHandler.setDataPropertyConditionAggregatedModelMap(dataPropertyConditionAggregatedModelMap);
		ppiNotModelHandler.setDerivedSingleInstanceAggregatedModelMap(derivedSingleInstanceAggregatedModelMap);

		ppiNotModelHandler.setDerivedSingleInstanceModelMap(derivedSingleInstanceModelMap);
		ppiNotModelHandler.setDerivedMultiInstanceModelMap(derivedMultiInstanceModelMap);

		ppiNotModelHandler.setPpiModelMap(ppiModelMap);

		ppiNotModelHandler.save(path, nomFich, this.selectedProcessId);
	}
	
	
}
