package es.us.isa.bpms.ppinot.historyreport;

import es.us.isa.bpms.ppinot.historyreport.db.HistoryData;
import es.us.isa.bpms.ppinot.historyreport.db.ProcessEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBException;

import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedMeasure;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;

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
	
	// id de la medida seleccionada para mostrar sus valores para todas las instancias
	private String measureId;

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
		
		this.measureId = "";
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

		this.measureId = "";
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

		// si hay una medida seleccionada, calcula su valor para cada una de las instancias de proceso
		if (this.measureId!="") {
			
			String[] list = measureId.split(":");
			String type = list[0];
			String id = list[1];
			
			Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData( processId, null, null, true, true );

			Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
		        String instanceId = pairs.getKey();

			    if (type==HistoryConst.TIMEMEASURE) {
			    	
			    	PPI ppi = this.getTimeInstancePpiMap().get(processId).get(id);
					

			    } else
			    if (type==HistoryConst.COUNTMEASURE) {
			    	
			    	PPI ppi = this.getCountInstancePpiMap().get(processId).get(id);
					

			    } else
			    if (type==HistoryConst.STATECONDITIONMEASURE) {
			    	
			    	PPI ppi = this.getStateConditionInstancePpiMap().get(processId).get(id);

			    } else
			    if (type==HistoryConst.DATAMEASURE) {
			    	
			    	PPI ppi = this.getDataInstancePpiMap().get(processId).get(id);

			    } else
			    if (type==HistoryConst.DATAPROPERTYCONDITIONMEASURE) {
			    	
			    	PPI ppi = this.getDataPropertyConditionInstancePpiMap().get(processId).get(id);

			    } else
				if (type==HistoryConst.DERIVEDSINGLEINSTANCEMEASURE) {
			    	
			    	PPI ppi = this.getDerivedSingleInstancePpiMap().get(processId).get(id);

				}
		    }
		}
		
		if (this.instanceIdMap.get(processId)!="") {
			
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
		
		// calcula los ppi de medidas agregadas y derivadas multiinstance
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
	 * @param camino Camino del archivo xml
	 * @param nomFich Nombre del archivo
	 * @throws JAXBException
	 */
	public void doReportFromXML(String camino, String nomFich, String instanceId, String measureId) throws JAXBException {
		
		// crea el objeto para leer el xml
		PpiNotModelHandler ppiNotXmlExtracter = new PpiNotModelHandler();
		ppiNotXmlExtracter.load(camino, nomFich);
		
		String processId = ppiNotXmlExtracter.getProcId();
		this.iniProcessReport( processId, true );

		this.selectedProcessId = processId;
		
		this.measureId = measureId;
		
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
		
		// selecciona la ultima instancia del proceso, en el caso que no haya ninguna instancia seleccionada
	    if (instanceId!="")
			this.instanceIdMap.put(processId, instanceId);
	    else {
			Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData( processId, null, null, true, true );
			if (map.size()>0) {
				
				Object[] keys = map.keySet().toArray();
				String lastKey = (String) keys[keys.length-1];
				this.instanceIdMap.put(processId, map.get(lastKey).getInstanceId());
			}
	    }
		
		// determina los valores de los indicadores
		this.calculatePpiValues(processId);
	}
	
	public Map<String, String> getMeasureIdMap() {
		
		String processId = this.selectedProcessId;
		
		Map<String, String> measureIdMap = new HashMap<String, String>();
		
		if (this.timeInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.timeInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        TimeInstanceMeasure measure = (TimeInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.TIMEMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
		
		if (this.countInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.countInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        CountInstanceMeasure measure = (CountInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.COUNTMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
		
		if (this.stateConditionInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.stateConditionInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        StateConditionInstanceMeasure measure = (StateConditionInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.STATECONDITIONMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
		
		if (this.dataInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.dataInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DataInstanceMeasure measure = (DataInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.DATAMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
			
		if (this.dataPropertyConditionInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.dataPropertyConditionInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DataPropertyConditionInstanceMeasure measure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.DATAPROPERTYCONDITIONMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
			
		if (this.derivedSingleInstancePpiMap.containsKey(processId)) {
			
		    Iterator<Entry<String, PPI>> itInst = this.derivedSingleInstancePpiMap.get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
	
		        DerivedSingleInstanceMeasure measure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();
		        
		        String id = HistoryConst.DERIVEDSINGLEINSTANCEMEASURE+ ":" + measure.getId();
		        measureIdMap.put(id, id);
		    }
		}
		
		return measureIdMap;
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
