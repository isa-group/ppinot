package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.handler.ModelHandler;
import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TExtensionElements;
import es.us.isa.bpmn.xmlClasses.bpmn20.TTask;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.bpmn.xmlClasses.bpmn20.TProcess;
import es.us.isa.ppinot.model.*;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.BaseMeasure;
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
import es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregatedMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregates;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToDataConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToElementConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TBaseMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TCountMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataPropertyConditionMeasure;
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
 * Clase que permite obtener la información de los PPI en un xml. Devuelve listas con instancias de las clases definidas en el
 * paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a>. Para realizar esto se utilizan las clases en <a href="../../ppinotXML/package-summary.html">el paquete ppinotXML<a> que fueron 
 * generadas con jaxb. Esta clase permite que la aplicación web que muestra el reporte de los PPI pueda adaptarse a cualquier 
 * formato del xml donde se especifican los PPI. Solo es necesario modificar esta clase y el paquete generado con jaxb a 
 * partir del xsd.
 * <p>
 * A partir de esos objetos permite obtener listas de instancias de las clases en el paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a> que 
 * son utilizadas en un objeto subclase de <a href="../../historyreport/HistoryReport.html">HistoryReport.html</a> para obtener el reporte solicitado en el xml.
 * 
 * @author Edelia García González
 * @version 1.0
 *
 */
public class PpiNotModelHandler extends ModelHandler implements PpiNotModelHandlerInterface {

	/**
	 * Objeto con instancias de las clases en el paquete de BPMN, que es utilizado para exportar e importar xml
	 */
	private es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory bpmnFactory;
	
	private Integer contador = 0;
	
	// PPINOT XML classes
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

	// PPINOT Model Classes
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
	
	/**
	 * Constructor de la clase
	 * 
	 * @throws JAXBException
	 */
	public PpiNotModelHandler() throws JAXBException {

		super();
	}

	/**
	 * Inicializa el JAXBContext y los ObjectFactory en dependencia de la ubicación de los paquetes con las clases que permiten
	 * exportar y exportar a xml
	 * @throws JAXBException 
	 * 
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
	}
	
	protected ObjectFactory getFactory() {
	
		return (ObjectFactory) super.getFactory();
	}

	/**
	 * Obtiene el elemento PpiSetType del xml
	 * Se considera que existirá un solo proceso, con un solo extensionElements, cuyo primer elemento es un ppiset
	 * 
	 * @return El elemento PpiSetType del xml
	 */
	private TPpiset getPpiset() {
		
		// obtiene el proceso creado a partir de la información en el xml
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
	 * Devuelve el id del proceso
	 * 
	 * @return
	 */
	public String getProcId() {
		
		// obtiene el ProcessType creado a partir de la información en el xml

		TProcess process = null;
		
		Object object = ((TDefinitions) this.getImportElement().getValue()).getRootElement().get(0).getValue();
		if (object instanceof TProcess) {
			
			process = (TProcess) object;
		} else {
			
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

	@SuppressWarnings("unused")
	private void removePpiModelMap(String id) {
		
		this.ppiModelMap.remove(id);
	}

	/**
	 * Busca un conector MeasureConnector que salga de la medida measure
	 * 
	 * @param ppi La medida de la que se busca el conector
	 * @return Conector de la medida
	 */
	@SuppressWarnings("rawtypes")
	private TMeasureConnector findMeasureConnector(TMeasure measure, Class cl) {
		
		TMeasureConnector connector = null;
		for( JAXBElement<?> element : this.getPpiset().getMeasureConnector() ) {
			
			if( cl.isInstance(element.getValue()) ) {
				
				TMeasureConnector con = (TMeasureConnector) element.getValue();
				if (con.getSourceRef()!=null && con.getSourceRef().equals(measure)) {
					
					connector = con;
					break;
				}
			}
		}
		
		return connector;
	}
	
	/**
	 * Busca un conector Uses que salga de la medida measure
	 * 
	 * @param ppi La medida de la que se busca el conector
	 * @return Conector de la medida
	 */
	private List<TUses> findUses(TMeasure measure) {
		
		List<TUses> connectorList = new ArrayList<TUses>();
		for( JAXBElement<?> element : this.getPpiset().getMeasureConnector() ) {
			
			if(element.getValue() instanceof TUses) {

				TUses con = (TUses) element.getValue();
				if (con.getSourceRef()!=null && con.getSourceRef().equals(measure)) {
					
					connectorList.add(con);
				}
			}
		}
		
		return connectorList;
	}

	private MeasureDefinition findConnectedMeasure(TMeasureConnector con, Boolean remove) {
	
		String id = ((TMeasure) con.getTargetRef()).getId();
		MeasureDefinition measure = null;
		
		if (this.countInstanceModelMap.containsKey(id)) {
			measure = this.countInstanceModelMap.get(id);
			if (remove)
				this.countInstanceModelMap.remove(id);
		} else
		if (this.timeInstanceModelMap.containsKey(id)) {
			measure = this.timeInstanceModelMap.get(id);
			if (remove)
				this.timeInstanceModelMap.remove(id);
		} else
		if (this.stateConditionInstanceModelMap.containsKey(id)) {
			measure = this.stateConditionInstanceModelMap.get(id);
			if (remove)
				this.stateConditionInstanceModelMap.remove(id);
		} else
		if (this.dataInstanceModelMap.containsKey(id)) {
			measure = this.dataInstanceModelMap.get(id);
			if (remove)
				this.dataInstanceModelMap.remove(id);
		} else
		if (this.dataPropertyConditionInstanceModelMap.containsKey(id)) {
			measure = this.dataPropertyConditionInstanceModelMap.get(id);
			if (remove)
				this.dataPropertyConditionInstanceModelMap.remove(id);
		} else
		if (this.countAggregatedModelMap.containsKey(id)) {
			measure = this.countAggregatedModelMap.get(id);
			if (remove)
				this.countAggregatedModelMap.remove(id);
		} else
		if (this.timeAggregatedModelMap.containsKey(id)) {
			measure = this.timeAggregatedModelMap.get(id);
			if (remove)
				this.timeAggregatedModelMap.remove(id);
		} else
		if (this.stateConditionAggregatedModelMap.containsKey(id)) {
			measure = this.stateConditionAggregatedModelMap.get(id);
			if (remove)
				this.stateConditionAggregatedModelMap.remove(id);
		} else
		if (this.dataAggregatedModelMap.containsKey(id)) {
			measure = this.dataAggregatedModelMap.get(id);
			if (remove)
				this.dataAggregatedModelMap.remove(id);
		} else
		if (this.dataPropertyConditionAggregatedModelMap.containsKey(id)) {
			measure = this.dataPropertyConditionAggregatedModelMap.get(id);
			if (remove)
				this.dataPropertyConditionAggregatedModelMap.remove(id);
		} else 
		if (this.derivedSingleInstanceAggregatedModelMap.containsKey(id)) {
			measure = this.derivedSingleInstanceAggregatedModelMap.get(id);
			if (remove)
				measure = this.derivedSingleInstanceAggregatedModelMap.get(id);
		} else {

			Iterator<Entry<String, DerivedMeasure>> itInst = this.derivedSingleInstanceModelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
		        DerivedMeasure m = (DerivedMeasure) pairs.getValue();

		        measure = m.getUsedMeasureId(id);
				if (measure!=null) {
					break;
				}
			}
		
		    if (measure==null) {
				Iterator<Entry<String, DerivedMeasure>> itInstP = this.derivedMultiInstanceModelMap.entrySet().iterator();
			    while (itInstP.hasNext()) {
			        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInstP.next();
			        DerivedMeasure m = (DerivedMeasure) pairs.getValue();

			        measure = m.getUsedMeasureId(id);
					if (measure!=null) {
						break;
					}
				}
		    }
		}
		
		return measure;
	}
	/**
	 * Busca los conectores TimeConnector que salgan de una medida
	 * 
	 * @param ppi La medida de la que se busca los conectores
	 * @return Mapa con los conectores de la medida. El conector desde el que se aplica la medida se devuelve en la llave From y el conector hasta el cual se aplica la medida se devuelve en la llave To
	 */
	private Map<String, TTimeConnector> findTimeConnectors(TMeasure measure) {
		
		Map<String, TTimeConnector> map = new HashMap<String, TTimeConnector>();
		for( JAXBElement<?> element : this.getPpiset().getMeasureConnector() ) {
			
			if(element.getValue() instanceof TTimeConnector) {

				TTimeConnector con = (TTimeConnector) element.getValue();
				if (con.getSourceRef()!=null && con.getSourceRef().equals(measure)) {
					
					map.put(con.getConditiontype(), con);
					if (map.size()==2)
						break;
				}
			}
		}
		return map;
	}
	
	/**
	 * Obtiene los datos del período que se desea analizar, a partir de la propiedad analisysPeriod de una medida agregada 
	 * 
	 * @param period Cadena con el período de análisis
	 * @return Mapa con los datos del período de análisis
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
	 * Genera el valor de la propiedad analisysPeriod a partir de la información del período de análisis
	 * 
	 * @param year Año
	 * @param period Si se desea los resultados por mes, trimestre o semestre
	 * @param startDate Fecha inicial del período de análisis
	 * @param endDate Fecha final del período de análisis
	 * @param inStart Si se incluye en el análisis los procesos que se inician antes del inicio del período y terminan después de este 
	 * @param inEnd Si se incluye en el análisis los procesos que se inician antes del final del período y terminan después de este
	 * @return
	 */
	private String generateAnalisysPeriod(String year, String period, Date startDate, Date endDate, Boolean inStart, Boolean inEnd) {
		
		String startDateString = Utils.formatString(startDate);
		String endDateString = Utils.formatString(endDate);
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
	 * Obtiene los valores de referencia para evaluar el grado de satisfacción de un ppi, a partir de la propiedad target
	 * 
	 * @param target Valor de la propiedad target de un ppi
	 * @return Mapa con los valores de referencia. El valor mínimo se devuelve con la llave refMin y el valor máximo con la llave refMax
	 */
	private Map<String, Double> parseTarget(String target) {
		
		Map<String, Double> map = new HashMap<String, Double>();

		try {
			Pattern patron = Pattern.compile("(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)\\-(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)|(>|<|=|@)(\\-{0,1}[0-9]+\\.{0,1}[0-9]*)");
			Matcher matcher = patron.matcher( target );
			matcher.find();

			if (matcher.group(3)!=null && matcher.group(3).contentEquals("=")) {
			
				map.put("refMin", Utils.stringToDouble(matcher.group(4)));
				map.put("refMax", Utils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals(">")) {
				
				map.put("refMin", Utils.stringToDouble(matcher.group(4)));
			} else
			if (matcher.group(3)!=null && matcher.group(3).contentEquals("<")) {
				
				map.put("refMax", Utils.stringToDouble(matcher.group(4)));
			} else 
			if (matcher.group(1)!=null && matcher.group(2)!=null) {
				map.put("refMin", Utils.stringToDouble(matcher.group(1)));
				map.put("refMax", Utils.stringToDouble(matcher.group(2)));
			} 
		}  catch (Exception e) {
			
			map.put("refMin", null);
			map.put("refMax", null);
		}
		
		return map;
	}
	
	/**
	 * Genera el valor de la propiedad target de un ppi, a partir de los valores de referencia de este
	 * 
	 * @param refMin Valor mínimo que debería tomar la medida en el ppi
	 * @param refMax Valor máximo que debería tomar la medida en el ppi
	 * @return Valor de la propiedad target del ppi
	 */
	private String generateTarget(Double refMin, Double refMax) {
		
		String target = "";
		
		if (refMax!=null && refMin!=null && refMax==refMin) {
			
			target = "=" + Utils.doubleToString(refMin);
		} else
		if (refMax==null && refMin!=null) {
			
			target = ">" + Utils.doubleToString(refMin);
		} else
		if (refMin==null && refMax!=null) {
			
			target = "<" + Utils.doubleToString(refMax);
		} else 
		if (refMax!=null && refMin!=null) {
			
			target = Utils.doubleToString(refMin) + "-" + Utils.doubleToString(refMax);
		} 
		return target;
	}
	
	/**
	 * Obtienen instancias de alguna de las clases definidas en el paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a>,
	 * a partir de un objeto obtenido mediante el unmarshall de un xml, cuyas clases están definidas en <a href="../../ppinotXML/package-summary.html">el paquete ppinotXML<a>.
	 * Son utilizadas en la implementación de los métodos abstractos que obtienen las listas de las medidas utilizadas en la appweb (getCountInstanceMeasure, getTimeInstanceMeasure, etc).
	 * 
	 * @param measure Objeto producido por el unmarshall
	 * @return Objeto con la medida que es utilizado por la appweb
	 */
	private TimeInstanceMeasure obtainModel(TTimeMeasure measure) {
		
		if (this.timeInstanceModelMap.containsKey(measure.getId()))
			return this.timeInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida TimeMeasure a partir de la información en el xml
		// las actividades a la cuales se aplica la medida se obtiene de los conectores
		Map<String, TTimeConnector> map = findTimeConnectors(measure);
		TTimeConnector conFrom = map.get("From");
		TTimeConnector conTo = map.get("To");
		
		TimeMeasureType timeMeasureType = (measure.getTimeMeasureType().toLowerCase().contentEquals("cyclic"))?TimeMeasureType.CYCLIC:TimeMeasureType.LINEAR;
		
		TimeInstanceMeasure def = null;
		if (map.size()==2)
			def = new TimeInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					new TimeInstantCondition(
							((TBaseElement) conFrom.getTargetRef()).getId(), 
							new RuntimeState(conFrom.getWhen())),
					new TimeInstantCondition(
							((TBaseElement) conTo.getTargetRef()).getId(), 
							new RuntimeState(conTo.getWhen())),
					timeMeasureType,  
					measure.getSingleInstanceAggFunction());
		else
			def = new TimeInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					null,  
					null,
					timeMeasureType,
					measure.getSingleInstanceAggFunction() );
		return def;
	}
	private CountInstanceMeasure obtainModel(TCountMeasure measure) {
		
		if (this.countInstanceModelMap.containsKey(measure.getId()))
			return this.countInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida CountMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class);
		CountInstanceMeasure def = null;
		if (connector!=null)
			def = new CountInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					new TimeInstantCondition(
							((TBaseElement) connector.getTargetRef()).getId(), 
							new RuntimeState(((TAppliesToElementConnector) connector).getWhen())) );  
		else
			def = new CountInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					null );

		return def;
	}
	private StateConditionInstanceMeasure obtainModel(TStateConditionMeasure measure) {
		
		if (this.stateConditionInstanceModelMap.containsKey(measure.getId()))
			return this.stateConditionInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida StateConditionMeasure a partir de la información en el xml
		// la tarea a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class);
		
		StateConditionInstanceMeasure def = null;
		if (connector!=null)
			def = new StateConditionInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					new StateCondition( 
							((TBaseElement) connector.getTargetRef()).getId(),  
							new RuntimeState(((TAppliesToElementConnector) connector).getState())) );
		else
			def = new StateConditionInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					null );
		return def;
	}
	private DataInstanceMeasure obtainModel(TDataMeasure measure) {
		
		if (this.dataInstanceModelMap.containsKey(measure.getId()))
			return this.dataInstanceModelMap.get(measure.getId());

		// crea la definición de la medida DataMeasure a partir de la información en el xml
		// el dataobject al cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class);
		
		DataInstanceMeasure def = null;
		if (connector!=null)
			def = new DataInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					new DataContentSelection( 
							((TAppliesToDataConnector) connector).getDataContentSelection(),
							((TDataObject) connector.getTargetRef()).getName() ),
					new DataPropertyCondition( 
							((TDataObject) connector.getTargetRef()).getName(),  
							((TAppliesToDataConnector) connector).getRestriction(),
							new RuntimeState(((TAppliesToDataConnector) connector).getState()) ) );
		else
			def = new DataInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					null,
					null );
		return def;
	}
	private DataPropertyConditionInstanceMeasure obtainModel(TDataPropertyConditionMeasure measure) {
		
		if (this.dataPropertyConditionInstanceModelMap.containsKey(measure.getId()))
			return this.dataPropertyConditionInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida DataPropertyConditionMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class);
		
		DataPropertyConditionInstanceMeasure def = null;
		if (connector!=null) 
			def = new DataPropertyConditionInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					new DataPropertyCondition( 
							((TDataObject) ((TAppliesToDataConnector) connector).getTargetRef()).getName(), 
							((TAppliesToDataConnector) connector).getRestriction(),
							new RuntimeState(((TAppliesToDataConnector) connector).getState()) ) );
		else
			def = new DataPropertyConditionInstanceMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					null );
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, BaseMeasure baseModel) {
		
		if (this.timeAggregatedModelMap.containsKey(measure.getId()))
			return this.timeAggregatedModelMap.get(measure.getId());
		
		AggregatedMeasure def = new AggregatedMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getAggregationfunction(), 
				measure.getSamplingfrequency(),
				baseModel
				);

		TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
		if (con!=null) {
			def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
					((TDataObject) con.getTargetRef()).getName()) );
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TTimeMeasure baseMeasure) {
		
		if (this.timeAggregatedModelMap.containsKey(measure.getId()))
			return this.timeAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada TimeMeasure a partir de la información en el xml
		// las actividades a laa cuales se aplica la medida se obtienen de los conectores de la medida agregada
		AggregatedMeasure def = null;
		
		Map<String, TTimeConnector> mapCon = findTimeConnectors(measure);
		TTimeConnector conFrom = mapCon.get("From");
		TTimeConnector conTo = mapCon.get("To");
		
		if (mapCon.size()==2) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// las actividades a las cuales se aplica la medida se obtiene de los conectores de la medida agregada
			TimeInstanceMeasure baseModel = this.obtainModel(baseMeasure);
			baseModel.setFrom(new TimeInstantCondition( ((TBaseElement) conFrom.getTargetRef()).getId(), new RuntimeState(conFrom.getWhen())));	// si se mide al inicio o al final de la actividad inicial
			baseModel.setTo(new TimeInstantCondition( ((TBaseElement) conTo.getTargetRef()).getId(), new RuntimeState(conTo.getWhen())));		// si se mide al inicio o al final de la actividad final
			
			// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
			def = new AggregatedMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					measure.getAggregationfunction(), 
					measure.getSamplingfrequency(),
					baseModel
					);

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TCountMeasure baseMeasure) {
		
		if (this.countAggregatedModelMap.containsKey(measure.getId()))
			return this.countAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada CountMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
			CountInstanceMeasure baseModel = this.obtainModel(baseMeasure);
			baseModel.setWhen(new TimeInstantCondition(((TBaseElement) connector.getTargetRef()).getId(), new RuntimeState(((TAppliesToElementConnector) connector).getWhen())));
			
			// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
			def = new AggregatedMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					measure.getAggregationfunction(), 
					measure.getSamplingfrequency(),
					baseModel);

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TStateConditionMeasure baseMeasure) {
		
		if (this.stateConditionAggregatedModelMap.containsKey(measure.getId()))
			return this.stateConditionAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada StateConditionMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
			StateConditionInstanceMeasure baseModel = this.obtainModel(baseMeasure);
			baseModel.setCondition(new StateCondition( ((TBaseElement) connector.getTargetRef()).getId(), new RuntimeState(((TAppliesToElementConnector) connector).getState())));
			
			// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
			def = new AggregatedMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					measure.getAggregationfunction(), 
					measure.getSamplingfrequency(),
					baseModel);

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDataMeasure baseMeasure) {
		
		if (this.dataAggregatedModelMap.containsKey(measure.getId()))
			return this.dataAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada DataMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
			DataInstanceMeasure baseModel = this.obtainModel(baseMeasure);
			baseModel.setDataContentSelection( new DataContentSelection( 
					((TAppliesToDataConnector) connector).getDataContentSelection(),
					((TDataObject) connector.getTargetRef()).getName()) );
			baseModel.setCondition( new DataPropertyCondition( 
					((TDataObject) connector.getTargetRef()).getName(), 
					((TAppliesToDataConnector) connector).getRestriction(),
					new RuntimeState(((TAppliesToDataConnector) connector).getState()) ) );
			
			def = new AggregatedMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					measure.getAggregationfunction(), 
					measure.getSamplingfrequency(), 
					baseModel);

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDataPropertyConditionMeasure baseMeasure) {
		
		if (this.dataPropertyConditionAggregatedModelMap.containsKey(measure.getId()))
			return this.dataPropertyConditionAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada DataPropertyConditionMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
			DataPropertyConditionInstanceMeasure baseModel = this.obtainModel(baseMeasure);
			baseModel.setCondition( new DataPropertyCondition(
					((TDataObject) ((TAppliesToDataConnector) connector).getTargetRef()).getName(), 
					((TAppliesToDataConnector) connector).getRestriction(),
					new RuntimeState(((TAppliesToDataConnector) connector).getState()) ) );
			
			def = new AggregatedMeasure( 
					measure.getId(), 
					measure.getName(), 
					measure.getDescription(),
					measure.getScale(),
					measure.getUnitofmeasure(),
					measure.getAggregationfunction(), 
					measure.getSamplingfrequency(),
					baseModel);

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	private AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDerivedSingleInstanceMeasure baseMeasure) {
		
		if (this.derivedSingleInstanceAggregatedModelMap.containsKey(measure.getId()))
			return this.derivedSingleInstanceAggregatedModelMap.get(measure.getId());
		
		// crea la definición de una medida agregada DataPropertyConditionMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		

		// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
		// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
		DerivedSingleInstanceMeasure baseModel = this.obtainModel(baseMeasure);

		for (TUses connector : findUses(measure)) {
			
			baseModel.addUsedMeasure( this.findConnectedMeasure(connector, false) );
		}
		
		def = new AggregatedMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getAggregationfunction(), 
				measure.getSamplingfrequency(),
				baseModel);

		TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class);
		if (con!=null) {
			def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
					((TDataObject) con.getTargetRef()).getName()) );
		}
		
		return def;
	}
	private DerivedSingleInstanceMeasure obtainModel(TDerivedSingleInstanceMeasure measure) {
		
		if (this.derivedSingleInstanceModelMap.containsKey(measure.getId()))
			return (DerivedSingleInstanceMeasure) this.derivedSingleInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida DerivedSingleInstanceMeasure a partir de la información en el xml
		// la medida a la cual se aplica la medida se obtiene del conector
		DerivedSingleInstanceMeasure def = new DerivedSingleInstanceMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getFunction() );

		for (TUses connector : findUses(measure)) {
			
//			def.addUsedMeasure( this.findConnectedMeasure(connector, true) );
			def.addUsedMeasure( this.findConnectedMeasure(connector, false) );
		}

		return def;
	}
	private DerivedMultiInstanceMeasure obtainModel(TDerivedMultiInstanceMeasure measure) {
		
		if (this.derivedMultiInstanceModelMap.containsKey(measure.getId()))
			return (DerivedMultiInstanceMeasure) this.derivedMultiInstanceModelMap.get(measure.getId());
		
		// crea la definición de la medida DerivedMultiInstanceMeasure a partir de la información en el xml
		// la medida a la cual se aplica la medida se obtiene del conector
		DerivedMultiInstanceMeasure def = new DerivedMultiInstanceMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getFunction() );

		for (TUses connector : findUses(measure)) {
			
//			def.addUsedMeasure( this.findConnectedMeasure(connector, true) );
			def.addUsedMeasure( this.findConnectedMeasure(connector, false) );
		}

		return def;
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
		dataobject.setId(this.generarId("dataobject", ""));
		dataobject.setName((String) con.getTargetRef());
    	con.setTargetRef(dataobject);
    	
    	return dataobject;
	}
	
	/**
	 * Da valor a refMax y refMin de una medida, en el caso que se encuentre en un PPI
	 * 
	 * @param medida en el xml
	 * @param medida para la appweb
	 */
	private void searchPpi(Object object, MeasureDefinition def) {
		
		// determina si la medida está incluida en algún ppi. el refMin y el refMax de la medida es el definido en el ppi
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
			    				Utils.parseDate(analysisperiodMap.get("startDate")), 
			    				Utils.parseDate(analysisperiodMap.get("endDate")), 
			    				analysisperiodMap.get("inStart").contentEquals("yes"), 
			    				analysisperiodMap.get("inEnd").contentEquals("yes")));
				
				ppiModel.setMeasuredBy(def);
				
				this.ppiModelMap.put(ppi.getId(), ppiModel);
				
				break;
			}
		}
	}
	
	/**********************************************************************************************************************
	 * 
	 * Implementaciones de los métodos que devuelven listas de medidas para ser utilizadas en la appweb
	 * 
	 **********************************************************************************************************************/
	/**
	 * Devuelve una lista con las medidas del tipo CountMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, CountInstanceMeasure> getCountInstanceModelMap() {
		
		return countInstanceModelMap;
	}

	/**
	 * Devuelve una lista con las medidas del tipo TimeMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, TimeInstanceMeasure> getTimeInstanceModelMap() {

		return timeInstanceModelMap;
	}

	/**
	 * Devuelve una lista con las medidas del tipo StateConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, StateConditionInstanceMeasure> getStateConditionInstanceModelMap() {
		
		return stateConditionInstanceModelMap;
	}

	/**
	 * Devuelve una lista con las medidas del tipo DataMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, DataInstanceMeasure> getDataInstanceModelMap() {
		
		return dataInstanceModelMap;
	}

	/**
	 * Devuelve una lista con las medidas del tipo DataPropertyConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceModelMap() {
		
		return dataPropertyConditionInstanceModelMap;
	}
	
	/**
	 * Devuelve una lista con las medidas agregadas del tipo TimeMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, AggregatedMeasure> getTimeAggregatedModelMap() {

		return timeAggregatedModelMap;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo CountMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, AggregatedMeasure> getCountAggregatedModelMap() {

		return countAggregatedModelMap;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo StateConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, AggregatedMeasure> getStateConditionAggregatedModelMap() {

		return stateConditionAggregatedModelMap;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo DataMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, AggregatedMeasure> getDataAggregatedModelMap() {

		return dataAggregatedModelMap;
	}
	
	/**
	 * Devuelve una lista con las medidas agregadas del tipo DataPropertyConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedModelMap() {

		return dataPropertyConditionAggregatedModelMap;
	}
	
	public Map<String, AggregatedMeasure> getDerivedSingleInstanceAggregatedModelMap() {

		return derivedSingleInstanceAggregatedModelMap;
	}

	/**
	 * Devuelve una lista con las medidas derivadas del tipo DerivedSingleInstanceMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, DerivedMeasure> getDerivedSingleInstanceModelMap() {
		
		return derivedSingleInstanceModelMap;
	}

	/**
	 * Devuelve una lista con las medidas derivadas del tipo DerivedMultiInstanceMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	public Map<String, DerivedMeasure> getDerivedMultiInstanceModelMap() {
		
		return derivedMultiInstanceModelMap;
	}
	
	public Map<String, PPI> getPpiModelMap() {
		
		return ppiModelMap;
	}

	/**
	 * Genera el id
	 * 
	 * @param prefix Prefijo del id si no se sugiere alguno
	 * @param id Id sugerido
	 * @return Nuevo Id
	 */
	private String generarId(String prefix, String id) {
		
		this.contador++;
		return (id=="")?prefix+"_"+this.contador:id;
	}

	private Map<String,Object> obtainUsedInfo( MeasureDefinition usedModel) {

		Map<String,Object> usedMap = new HashMap<String,Object>();
		if (usedModel instanceof CountInstanceMeasure)
			usedMap = this.obtainInfo((CountInstanceMeasure) usedModel);
		else
		if (usedModel instanceof TimeInstanceMeasure) 
			usedMap = this.obtainInfo((TimeInstanceMeasure) usedModel);
		else
		if (usedModel instanceof StateConditionInstanceMeasure) 
			usedMap = this.obtainInfo((StateConditionInstanceMeasure) usedModel);
		else
		if (usedModel instanceof DataInstanceMeasure) 
			usedMap = this.obtainInfo((DataInstanceMeasure) usedModel);
		else
		if (usedModel instanceof DataPropertyConditionInstanceMeasure)
			usedMap = this.obtainInfo((DataPropertyConditionInstanceMeasure) usedModel);
		else
		if (usedModel instanceof AggregatedMeasure)
			usedMap = this.obtainInfo((AggregatedMeasure) usedModel);
		else
		if (usedModel instanceof AggregatedMeasure) 
			usedMap = this.obtainInfo((AggregatedMeasure) usedModel);
		else
		if (usedModel instanceof AggregatedMeasure) 
			usedMap = this.obtainInfo((AggregatedMeasure) usedModel);
		else
		if (usedModel instanceof AggregatedMeasure) 
			usedMap = this.obtainInfo((AggregatedMeasure) usedModel);
		else
		if (usedModel instanceof AggregatedMeasure)
			usedMap = this.obtainInfo((AggregatedMeasure) usedModel);
		return usedMap;
	}
	
	/**
	 * Obtienen instancias de las clases definidas en <a href="../../ppinotXML/package-summary.html">el paquete ppinotXML<a>, para exportar a XML, 
	 * a partir de instancias de las clases definidas en el paquete <a href="../../historyreport/measuredefinition/package-summary.html">historyreport.measuredefinition</a>,
	 * Son utilizadas en la implementación de los métodos abstractos que obtienen las listas para exportar a XML (getCountMeasureInfo, getTimeMeasureInfo, etc).
	 * 
	 * @param def Objeto con una medida que es utilizado por la appweb
	 * @return Mapa con objetos para ser utilizados en un marshall. Pueden crearse medidas, conectores y ppi.
	 */
	private Map<String,Object> obtainInfo(TimeInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.timeMap.containsKey(def.getId())) {
			
			map.put("createdMeasure", map.get(def.getId()));
			return map;
		}
		
		TTimeMeasure measure = this.getFactory().createTTimeMeasure();
		
		measure.setId(generarId("timeMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TTimeConnector conFrom = this.getFactory().createTTimeConnector();
		
		conFrom.setId(generarId("timeConnector", ""));
		conFrom.setSourceRef(measure);
		conFrom.setTargetRef(def.getFrom().getAppliesTo());
		conFrom.setWhen((def.getFrom().getChangesToState().getState()==GenericState.END)?"End":"Start");
		conFrom.setConditiontype("From");

		TTimeConnector conTo = this.getFactory().createTTimeConnector();
		
		conTo.setId(generarId("timeConnector", ""));
		conTo.setSourceRef(measure);
		conTo.setTargetRef(def.getTo().getAppliesTo());
		conTo.setWhen((def.getTo().getChangesToState().getState()==GenericState.END)?"End":"Start");
		conTo.setConditiontype("To");
		
		map.put("connectorFrom", conFrom);
		map.put("connectorTo", conTo);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(	CountInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.countMap.containsKey(def.getId())) {
			
			map.put("createdMeasure", this.countMap.get(def.getId()));
			return map;
		}
		
		TCountMeasure measure = this.getFactory().createTCountMeasure();
		
		measure.setId(generarId("countMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = this.getFactory().createTAppliesToElementConnector();
		
		con.setId(generarId("appliesToElementConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(def.getWhen().getAppliesTo());
		con.setWhen((def.getWhen().getChangesToState().getState()==GenericState.END)?"End":"Start");
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(StateConditionInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.stateConditionMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.stateConditionMap.get(def.getId()));
			return map;
		}
		
		TStateConditionMeasure measure = this.getFactory().createTStateConditionMeasure();
		
		measure.setId(generarId("stateConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = this.getFactory().createTAppliesToElementConnector();
		
		con.setId(generarId("appliesToElementConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(((StateCondition) def.getCondition()).getAppliesTo());
		con.setState((((StateCondition) def.getCondition()).getState().getState()==GenericState.END)?"End":"Start");
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(DataInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.dataMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.dataMap.get(def.getId()));
			return map;
		}
		
		TDataMeasure measure = this.getFactory().createTDataMeasure();
		
		measure.setId(generarId("dataMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = this.getFactory().createTAppliesToDataConnector();
		
		con.setId(generarId("appliesToDataConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(def.getDataContentSelection().getDataobject());
		con.setDataContentSelection(def.getDataContentSelection().getSelection());
		con.setRestriction(def.getCondition().getRestriction());
		con.setState(def.getCondition().getStateConsidered().getStateString());
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(DataPropertyConditionInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.dataPropertyConditionMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.dataPropertyConditionMap.get(def.getId()));
			return map;
		}
		
		TDataPropertyConditionMeasure measure = this.getFactory().createTDataPropertyConditionMeasure();
		
		measure.setId(generarId("dataPropertyConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = this.getFactory().createTAppliesToDataConnector();
		
		con.setId(generarId("appliesToDataConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(((DataPropertyCondition) def.getCondition()).getAppliesTo());
		con.setRestriction(((DataPropertyCondition) def.getCondition()).getRestriction());
		con.setState(((DataPropertyCondition) def.getCondition()).getStateConsidered().getStateString());
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(AggregatedMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.aggregatedMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.aggregatedMap.get(def.getId()));
			return map;
		}
		
		TAggregatedMeasure measure = this.getFactory().createTAggregatedMeasure();

		measure.setId(generarId("aggregatedMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());
		measure.setSamplingfrequency(def.getSamplingFrequency());
		
		measure.setAggregationfunction(def.getAggregationFunction());
		
		if (!def.getGroupedBy().getSelection().contentEquals("") || !def.getGroupedBy().getDataobject().contentEquals("")) {
			
			TIsGroupedBy con = this.getFactory().createTIsGroupedBy();
			
			con.setId(generarId("isGroupedBy", ""));
			con.setSourceRef(measure);
			con.setTargetRef(def.getGroupedBy().getDataobject());
			
			map.put("connectorIsGroupedBy", con);
		}

		if (def.getAggregates()) {
			
			TAggregates con = this.getFactory().createTAggregates();
			
			con.setId(generarId("aggregates", ""));
			con.setSourceRef(measure);
			
			TBaseMeasure baseMeasure = null;
			if (def.getBaseMeasure() instanceof TimeInstanceMeasure) {
				
				baseMeasure = this.timeMap.get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof CountInstanceMeasure) {
	
				baseMeasure = this.countMap.get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof StateConditionInstanceMeasure) {
	
				baseMeasure = this.stateConditionMap.get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof DataInstanceMeasure) {
	
				baseMeasure = this.dataMap.get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure) {
	
				baseMeasure = this.dataPropertyConditionMap.get(def.getBaseMeasure().getId());
			}
			
			con.setTargetRef(baseMeasure);
			
			map.put("measure", measure);
			map.put("connectorAggregates", con);
		} else {
			if (def.getBaseMeasure() instanceof TimeInstanceMeasure) {
				
				Map<String,Object> instanceMap = this.obtainInfo((TimeInstanceMeasure) def.getBaseMeasure());
				((TTimeConnector) instanceMap.get("connectorFrom")).setSourceRef(measure);
				((TTimeConnector) instanceMap.get("connectorTo")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getFactory().createTimeMeasure((TTimeMeasure) instanceMap.get("measure")));
				
				map.put("connectorFrom", instanceMap.get("connectorFrom"));
				map.put("connectorTo", instanceMap.get("connectorTo"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof CountInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((CountInstanceMeasure) def.getBaseMeasure());
				((TAppliesToElementConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getFactory().createCountMeasure((TCountMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof StateConditionInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((StateConditionInstanceMeasure) def.getBaseMeasure());
				((TAppliesToElementConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getFactory().createStateConditionMeasure((TStateConditionMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof DataInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((DataInstanceMeasure) def.getBaseMeasure());
				((TAppliesToDataConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getFactory().createDataMeasure((TDataMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((DataPropertyConditionInstanceMeasure) def.getBaseMeasure());
				((TAppliesToDataConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getFactory().createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			}
		}
		
		return map;
	}
	private Map<String,Object> obtainInfo( DerivedSingleInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.derivedSingleInstanceMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.derivedSingleInstanceMap.get(def.getId()));
			return map;
		}
		
		TDerivedSingleInstanceMeasure measure = this.getFactory().createTDerivedSingleInstanceMeasure();
		
		measure.setId(generarId("derivedSingleInstanceMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		measure.setFunction(def.getFunction());
		
		List<TMeasure> usedList = new ArrayList<TMeasure>();
		List<TAppliesToElementConnector> connectorElementList = new ArrayList<TAppliesToElementConnector>();
		List<TAppliesToDataConnector> connectorDataList = new ArrayList<TAppliesToDataConnector>();
		List<TTimeConnector> connectorFromList = new ArrayList<TTimeConnector>();
		List<TTimeConnector> connectorToList = new ArrayList<TTimeConnector>();
		List<TUses> connectorUsesList = new ArrayList<TUses>();
		List<TAggregates> connectorAggregatesList = new ArrayList<TAggregates>();
		List<TIsGroupedBy> connectorIsGroupedByList = new ArrayList<TIsGroupedBy>();

		Iterator<Entry<String, MeasureDefinition>> itInst = def.getUsedMeasureMap().entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
	    	String variable = pairs.getKey();
	    	MeasureDefinition usedMeasure = pairs.getValue();
		
			Map<String,Object> usedMap = this.obtainUsedInfo(usedMeasure);

			TUses con = this.getFactory().createTUses();
			con.setId(generarId("uses", ""));
			con.setVariable(variable);
			con.setSourceRef(measure);
			if (usedMap.containsKey("measure")) 
				con.setTargetRef(usedMap.get("measure"));
			else
				con.setTargetRef(usedMap.get("createdMeasure"));
			
			connectorUsesList.add(con);
			
			if (usedMap.containsKey("measure")) {
				
				if (usedMap.containsKey("connector")) {
					
					if (usedMap.get("connector") instanceof TAppliesToElementConnector)
						connectorElementList.add((TAppliesToElementConnector) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TAppliesToDataConnector)
						connectorDataList.add((TAppliesToDataConnector) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TAggregates)
						connectorAggregatesList.add((TAggregates) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TIsGroupedBy)
						connectorIsGroupedByList.add((TIsGroupedBy) usedMap.get("connector"));
				}
				if (usedMap.containsKey("connectorFrom"))
					connectorFromList.add((TTimeConnector) usedMap.get("connectorFrom"));
				if (usedMap.containsKey("connectorTo"))
					connectorToList.add((TTimeConnector) usedMap.get("connectorTo"));
				usedList.add((TMeasure) usedMap.get("measure"));
			}
	    }

		List<TMeasureConnector> connectorList = new ArrayList<TMeasureConnector>();
		connectorList.addAll(connectorElementList);
		connectorList.addAll(connectorDataList);
		connectorList.addAll(connectorAggregatesList);
		connectorList.addAll(connectorIsGroupedByList);
	    
		map.put("measure", measure);

		map.put("used", usedList);
		map.put("connectorUses", connectorUsesList);
		map.put("connector", connectorList);
		map.put("connectorFrom", connectorFromList);
		map.put("connectorTo", connectorToList);
		
		return map;
	}
	private Map<String,Object> obtainInfo(	DerivedMultiInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.derivedSingleInstanceMap.containsKey(def.getId())){
			
			map.put("createdMeasure", this.derivedSingleInstanceMap.get(def.getId()));
			return map;
		}
		
		TDerivedMultiInstanceMeasure measure = this.getFactory().createTDerivedMultiInstanceMeasure();
		
		measure.setId(generarId("derivedMultiInstanceMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		measure.setFunction(def.getFunction());
		
		List<TMeasure> usedList = new ArrayList<TMeasure>();
		List<TAppliesToElementConnector> connectorElementList = new ArrayList<TAppliesToElementConnector>();
		List<TAppliesToDataConnector> connectorDataList = new ArrayList<TAppliesToDataConnector>();
		List<TTimeConnector> connectorFromList = new ArrayList<TTimeConnector>();
		List<TTimeConnector> connectorToList = new ArrayList<TTimeConnector>();
		List<TUses> connectorUsesList = new ArrayList<TUses>();
		List<TAggregates> connectorAggregatesList = new ArrayList<TAggregates>();
		List<TIsGroupedBy> connectorIsGroupedByList = new ArrayList<TIsGroupedBy>();

		Iterator<Entry<String, MeasureDefinition>> itInst = def.getUsedMeasureMap().entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, MeasureDefinition> pairs = (Map.Entry<String, MeasureDefinition>)itInst.next();
	    	String variable = pairs.getKey();
	    	MeasureDefinition usedMeasure = pairs.getValue();
		
			Map<String,Object> usedMap = this.obtainUsedInfo(usedMeasure);

			TUses con = this.getFactory().createTUses();
			con.setId(generarId("uses", ""));
			con.setVariable(variable);
			con.setSourceRef(measure);
			if (usedMap.containsKey("measure")) 
				con.setTargetRef(usedMap.get("measure"));
			else
				con.setTargetRef(usedMap.get("createdMeasure"));
			
			connectorUsesList.add(con);
			
			if (usedMap.containsKey("measure")) {
				
				if (usedMap.containsKey("connector")) {
					
					if (usedMap.get("connector") instanceof TAppliesToElementConnector)
						connectorElementList.add((TAppliesToElementConnector) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TAppliesToDataConnector)
						connectorDataList.add((TAppliesToDataConnector) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TAggregates)
						connectorAggregatesList.add((TAggregates) usedMap.get("connector"));
					else
					if (usedMap.get("connector") instanceof TIsGroupedBy)
						connectorIsGroupedByList.add((TIsGroupedBy) usedMap.get("connector"));
				}
				if (usedMap.containsKey("connectorFrom"))
					connectorFromList.add((TTimeConnector) usedMap.get("connectorFrom"));
				if (usedMap.containsKey("connectorTo"))
					connectorToList.add((TTimeConnector) usedMap.get("connectorTo"));
				usedList.add((TMeasure) usedMap.get("measure"));
			}
	    }

		List<TMeasureConnector> connectorList = new ArrayList<TMeasureConnector>();
		connectorList.addAll(connectorElementList);
		connectorList.addAll(connectorDataList);
		connectorList.addAll(connectorAggregatesList);
		connectorList.addAll(connectorIsGroupedByList);

		map.put("measure", measure);

		map.put("used", usedList);
		map.put("connectorUses", connectorUsesList);
		map.put("connector", connectorList);
		map.put("connectorFrom", connectorFromList);
		map.put("connectorTo", connectorToList);
		
		return map;
	}

	/**********************************************************************************************************************
	 * 
	 * Implementaciones de los métodos que devuelven listas de medidas para ser exportadas en un xml
	 * 
	 **********************************************************************************************************************/
	public void setTimeModelMap(Map<String, TimeInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, TimeInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, TimeInstanceMeasure> pairs = (Map.Entry<String, TimeInstanceMeasure>)itInst.next();
		    	TimeInstanceMeasure def = pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.timeMap.put(((TTimeMeasure) map.get("measure")).getId(), (TTimeMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorFrom")) {
	    	    	
	    			TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
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
	}

	public void setCountModelMap(Map<String, CountInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, CountInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, CountInstanceMeasure> pairs = (Map.Entry<String, CountInstanceMeasure>)itInst.next();
		        CountInstanceMeasure def = pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.countMap.put(((TCountMeasure) map.get("measure")).getId(), (TCountMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.taskMap.put(task.getId(), task);

	    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
	    		}
		    }
	    }
	}

	public void setStateConditionModelMap(Map<String, StateConditionInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, StateConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, StateConditionInstanceMeasure> pairs = (Map.Entry<String, StateConditionInstanceMeasure>)itInst.next();
		        StateConditionInstanceMeasure def = pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.stateConditionMap.put(((TStateConditionMeasure) map.get("measure")).getId(), (TStateConditionMeasure) map.get("measure"));
	    		
//	    		if (map.containsKey("ppi"))
//	    			this.getPpiList().add((TPpi) map.get("ppi"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.taskMap.put(task.getId(), task);

	    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
	    		}
		    }
	    }
	}

	public void setDataModelMap(Map<String, DataInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DataInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
		        DataInstanceMeasure def = pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.dataMap.put(((TDataMeasure) map.get("measure")).getId(), (TDataMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
	    		}
		    }
	    }
	}

	public void setDataPropertyConditionModelMap(Map<String, DataPropertyConditionInstanceMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst.next();
		        DataPropertyConditionInstanceMeasure def = pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.dataPropertyConditionMap.put(((TDataPropertyConditionMeasure) map.get("measure")).getId(), (TDataPropertyConditionMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
	    		}
		    }
	    }
	}

	public void setTimeAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.aggregatesMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.isGroupedByMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorFrom")) {
	    	    	
	    	    	TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
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
	}

	public void setCountAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.aggregatesMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.isGroupedByMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.taskMap.put(task.getId(), task);

	    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
	    		}
	
			}
    	}
	}

	public void setStateConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.aggregatesMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.isGroupedByMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.taskMap.put(task.getId(), task);

	    	    	this.appliesToElementConnectorMap.put(con.getId(), con);
	    		}
	
			}
    	}
	}

	public void setDataAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.aggregatesMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.isGroupedByMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(((TAggregatedMeasure) map.get("measure")).getId(), dataobject);

	    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
	    		}
	
			}
    	}
	}

	public void setDataPropertyConditionAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.aggregatesMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.isGroupedByMap.put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);

	    	    	this.appliesToDataConnectorMap.put(con.getId(), con);
	    		}
	
			}
    	}
	}

	@SuppressWarnings("unchecked")
	public void setDerivedSingleInstanceAggregatedModelMap(Map<String, AggregatedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, AggregatedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, AggregatedMeasure> pairs = (Map.Entry<String, AggregatedMeasure>)itInst.next();
		        AggregatedMeasure def = pairs.getValue();
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.aggregatedMap.put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorUses")) {
	    			
	    			for ( TUses con : (List<TUses>) map.get("connectorUses"))
	    				this.usesMap.put(con.getId(), con);
	    		}
	
			}
    	}
	}

	@SuppressWarnings("unchecked")
	public void setDerivedSingleInstanceModelMap(Map<String, DerivedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
		        DerivedSingleInstanceMeasure def = (DerivedSingleInstanceMeasure) pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.derivedSingleInstanceMap.put(((TDerivedSingleInstanceMeasure) map.get("measure")).getId(), (TDerivedSingleInstanceMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connectorUses")) {
	    			
	    			for ( TUses con : (List<TUses>) map.get("connectorUses"))
	    				this.usesMap.put(con.getId(), con);
	    		}
/*
	    		if (map.containsKey("used")) {
	    			
	    			for ( TMeasure usedMeasure : (List<TMeasure>) map.get("used")) {

		    			if (usedMeasure instanceof TCountMeasure)
		    				this.countMap.put(((TCountMeasure) usedMeasure).getId(), (TCountMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TTimeMeasure) 
		    				this.timeMap.put(((TTimeMeasure) usedMeasure).getId(), (TTimeMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TStateConditionMeasure) 
		    				this.stateConditionMap.put(((TStateConditionMeasure) usedMeasure).getId(), (TStateConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataMeasure) 
		    				this.dataMap.put(((TDataMeasure) usedMeasure).getId(), (TDataMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataPropertyConditionMeasure)
		    				this.dataPropertyConditionMap.put(((TDataPropertyConditionMeasure) usedMeasure).getId(), (TDataPropertyConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TAggregatedMeasure)
		    				this.aggregatedMap.put(((TAggregatedMeasure) usedMeasure).getId(), (TAggregatedMeasure) usedMeasure);
		    		
			    		if (map.containsKey("connector")) {
			    	    	
			    			for ( TMeasureConnector con : (List<TMeasureConnector>) map.get("connector")) {
			    				
			    				if (con.getSourceRef()==usedMeasure) {
			    					
					    			if (usedMeasure instanceof TDataMeasure || usedMeasure instanceof TDataPropertyConditionMeasure) {
					    				
						    	    	TDataObject dataobject = chainDataobject(con);
						    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);
					    			} else {
					    				
						    	    	TTask task = chainTask(con);
						    	    	this.taskMap.put(task.getId(), task);
					    			}
			
									if (con instanceof TAppliesToElementConnector)
						    	    	this.appliesToElementConnectorMap.put(con.getId(), (TAppliesToElementConnector) con);
									else
									if (con instanceof TAppliesToDataConnector)
						    	    	this.appliesToDataConnectorMap.put(con.getId(), (TAppliesToDataConnector) con);
									else
									if (con instanceof TAggregates)
						    	    	this.aggregatesMap.put(con.getId(), (TAggregates) con);
									else
									if (con instanceof TIsGroupedBy)
						    	    	this.isGroupedByMap.put(con.getId(), (TIsGroupedBy) con);

					    	    	break;
			    				}
			    			}
			    		}
	    			}
			    	
		    		if (map.containsKey("connectorFrom")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorFrom")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
			    	    	
			    	    	this.timeConnectorMap.put(con.getId(), con);
		    			}
		    		}
			    	
		    		if (map.containsKey("connectorTo")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorTo")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
	
			    	    	this.timeConnectorMap.put(con.getId(), con);
		    			}
		    		}
	    		}
*/
		    }
	    }
	}

	@SuppressWarnings("unchecked")
	public void setDerivedMultiInstanceModelMap(Map<String, DerivedMeasure> modelMap) {

	    if (modelMap!=null) {
	    	
			Iterator<Entry<String, DerivedMeasure>> itInst = modelMap.entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DerivedMeasure> pairs = (Map.Entry<String, DerivedMeasure>)itInst.next();
		        DerivedMultiInstanceMeasure def = (DerivedMultiInstanceMeasure) pairs.getValue();
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.derivedMultiInstanceMap.put(((TDerivedMultiInstanceMeasure) map.get("measure")).getId(), (TDerivedMultiInstanceMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connectorUses")) {
	    			
	    			for ( TUses con : (List<TUses>) map.get("connectorUses"))
	    				this.usesMap.put(con.getId(), con);
	    		}
/*
	    		if (map.containsKey("used")) {
	    			
	    			for ( TMeasure usedMeasure : (List<TMeasure>) map.get("used")) {

		    			if (usedMeasure instanceof TCountMeasure)
		    				this.countMap.put(((TCountMeasure) usedMeasure).getId(), (TCountMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TTimeMeasure) 
		    				this.timeMap.put(((TTimeMeasure) usedMeasure).getId(), (TTimeMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TStateConditionMeasure) 
		    				this.stateConditionMap.put(((TStateConditionMeasure) usedMeasure).getId(), (TStateConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataMeasure) 
		    				this.dataMap.put(((TDataMeasure) usedMeasure).getId(), (TDataMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataPropertyConditionMeasure)
		    				this.dataPropertyConditionMap.put(((TDataPropertyConditionMeasure) usedMeasure).getId(), (TDataPropertyConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TAggregatedMeasure)
		    				this.aggregatedMap.put(((TAggregatedMeasure) usedMeasure).getId(), (TAggregatedMeasure) usedMeasure);
		    		
			    		if (map.containsKey("connector")) {
			    	    	
			    			for ( TMeasureConnector con : (List<TMeasureConnector>) map.get("connector")) {
			    				
			    				if (con.getSourceRef()==usedMeasure) {
			    				
					    			if ((usedMeasure instanceof TAggregatedMeasure && 
					    					(((TAggregatedMeasure) usedMeasure).getBaseMeasure().getValue() instanceof TDataMeasure || 
					    					 ((TAggregatedMeasure) usedMeasure).getBaseMeasure().getValue() instanceof TDataPropertyConditionMeasure)) ||
					    				usedMeasure instanceof TDataMeasure || usedMeasure instanceof TDataPropertyConditionMeasure) {
					    				
						    	    	TDataObject dataobject = chainDataobject(con);
						    	    	this.dataobjectMap.put(dataobject.getId(), dataobject);
					    			} else {
					    				
						    	    	TTask task = chainTask(con);
						    	    	this.taskMap.put(task.getId(), task);
					    			}
			
									if (con instanceof TAppliesToElementConnector)
						    	    	this.appliesToElementConnectorMap.put(con.getId(), (TAppliesToElementConnector) con);
									else
									if (con instanceof TAppliesToDataConnector)
						    	    	this.appliesToDataConnectorMap.put(con.getId(), (TAppliesToDataConnector) con);
									else
									if (con instanceof TAggregates)
						    	    	this.aggregatesMap.put(con.getId(), (TAggregates) con);
									else
									if (con instanceof TIsGroupedBy)
						    	    	this.isGroupedByMap.put(con.getId(), (TIsGroupedBy) con);

					    	    	break;
			    				}
			    			}
			    		}
	    			}
			    	
		    		if (map.containsKey("connectorFrom")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorFrom")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
			    	    	
			    	    	this.timeConnectorMap.put(con.getId(), con);
		    			}
		    		}
			    	
		    		if (map.containsKey("connectorTo")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorTo")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.taskMap.put(task.getId(), task);
	
			    	    	this.timeConnectorMap.put(con.getId(), con);
		    			}
		    		}
	    		}
*/
		    }
	    }
	}
	
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

				TMeasure md = null;
				if (m instanceof TimeInstanceMeasure)
					md = this.timeMap.get(m.getId());
				else
				if (m instanceof CountInstanceMeasure)
					md = this.countMap.get(m.getId());
				else
				if (m instanceof StateConditionInstanceMeasure)
					md = this.stateConditionMap.get(m.getId());
				else
				if (m instanceof DataInstanceMeasure)
					md = this.dataMap.get(m.getId());
				else
				if (m instanceof DataPropertyConditionInstanceMeasure)
					md = this.dataPropertyConditionMap.get(m.getId());
				else

				if (m instanceof AggregatedMeasure)
					md = this.aggregatedMap.get(m.getId());
				else

				if (m instanceof DerivedSingleInstanceMeasure)
					md = this.derivedSingleInstanceMap.get(m.getId());
				else
				if (m instanceof DerivedMultiInstanceMeasure)
					md = this.derivedMultiInstanceMap.get(m.getId());

		    	ppi.setMeasuredBy(md);
					
    			this.ppiList.add(ppi);
		    	
		    }
	    }
	}

	@Override
	protected void generateExportElement(String procId) {
		
	    TPpiset ppiset = this.getFactory().createTPpiset();
	    ppiset.setId("ppiset_1");
	    
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
	    
	    for (TPpi ppi : this.ppiList) 
	    	ppiset.getPpi().add(ppi);
   	
    	TExtensionElements extensionElements = new TExtensionElements();
	    extensionElements.getAny().add( this.getFactory().createPpiset(ppiset) );
		
	    TProcess process = this.bpmnFactory.createTProcess();
	    process.setId( procId );
	    process.setName( procId );
	    process.setExtensionElements(extensionElements);

		Iterator<Entry<String, TTask>> itInst12 = this.taskMap.entrySet().iterator();
	    while (itInst12.hasNext()) {
	        Map.Entry<String, TTask> pairs = (Map.Entry<String, TTask>)itInst12.next();
	        process.getFlowElement().add(this.bpmnFactory.createTask((TTask) pairs.getValue()));
	    }

		Iterator<Entry<String, TDataObject>> itInst13 = this.dataobjectMap.entrySet().iterator();
	    while (itInst13.hasNext()) {
	        Map.Entry<String, TDataObject> pairs = (Map.Entry<String, TDataObject>)itInst13.next();
	        TDataObject value = pairs.getValue();
			process.getFlowElement().add(this.bpmnFactory.createDataObject(value));
	    }

	    TDefinitions definitions = new TDefinitions();
	    definitions.setId("ppinot-definitions");
	    definitions.setExpressionLanguage("http://www.w3.org/1999/XPath");
	    definitions.setTargetNamespace("http://schema.omg.org/spec/BPMN/2.0");
	    definitions.setTypeLanguage("http://www.w3.org/2001/XMLSchema");
    	definitions.getRootElement().add( this.bpmnFactory.createProcess(process));
    	
        this.setExportElement( this.bpmnFactory.createDefinitions(definitions) );
		
        this.contador = 0;
	}

	@Override
	protected void generateModelLists() {
		
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TCountMeasure) {

				CountInstanceMeasure def = obtainModel((TCountMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					this.countInstanceModelMap.put( def.getId(), def );
				}
			}
		}

		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TTimeMeasure) {

				TimeInstanceMeasure def = obtainModel((TTimeMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					this.timeInstanceModelMap.put( def.getId(), def );
				}
			}
		}
		
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TStateConditionMeasure) {

				StateConditionInstanceMeasure def = this.obtainModel((TStateConditionMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					this.stateConditionInstanceModelMap.put( def.getId(), def );
				}
			}
		}
		
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TDataMeasure) {

				DataInstanceMeasure def = this.obtainModel((TDataMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					this.dataInstanceModelMap.put( def.getId(), def );
				}
			}
		}
		
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TDataPropertyConditionMeasure) {

				DataPropertyConditionInstanceMeasure def = this.obtainModel((TDataPropertyConditionMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					this.dataPropertyConditionInstanceModelMap.put( def.getId(), def );
				}
			}
		}

		// las medidas agregadas
		for( TAggregatedMeasure measure : this.getPpiset().getAggregatedMeasure()) {
			
			TMeasure baseMeasure;

			if(measure.getBaseMeasure()==null) {
				
				TAggregates connector = (TAggregates) this.findMeasureConnector(measure, TAggregates.class);
				
				if (connector!=null) {
					
					MeasureDefinition baseModel = this.findConnectedMeasure(connector, false);

					AggregatedMeasure def = this.obtainModel(measure, (BaseMeasure) baseModel);
					
					if (def!=null) {
						
						def.setAggregates(true);
						this.searchPpi(measure, def);
						
						if(baseModel instanceof TimeInstanceMeasure ) {
							
							this.timeAggregatedModelMap.put( def.getId(), def );
						} else
						if(baseModel instanceof CountInstanceMeasure ) {
							
							this.countAggregatedModelMap.put( def.getId(), def );
						} else
						if(baseModel instanceof StateConditionInstanceMeasure ) {
							
							this.stateConditionAggregatedModelMap.put( def.getId(), def );
						} else
						if(baseModel instanceof DataInstanceMeasure ) {
							
							this.dataAggregatedModelMap.put( def.getId(), def );
						} else
						if(baseModel instanceof DataPropertyConditionInstanceMeasure ) {
							
							this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
						} else
						if(baseModel instanceof DerivedSingleInstanceMeasure ) {
							
							this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
						}
					}
				}
			} else {
				
				baseMeasure = measure.getBaseMeasure().getValue();
			
				if(baseMeasure instanceof TTimeMeasure ) {
	
					AggregatedMeasure def = this.obtainModel(measure, (TTimeMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.timeAggregatedModelMap.put( def.getId(), def );
					}
				} else
				if(baseMeasure instanceof TCountMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TCountMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.countAggregatedModelMap.put( def.getId(), def );
					}
				} else
				if(baseMeasure instanceof TStateConditionMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TStateConditionMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.stateConditionAggregatedModelMap.put( def.getId(), def );
					}
				} else
				if(baseMeasure instanceof TDataMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TDataMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.dataAggregatedModelMap.put( def.getId(), def );
					}
				} else
				if(baseMeasure instanceof TDataPropertyConditionMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TDataPropertyConditionMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.dataPropertyConditionAggregatedModelMap.put( def.getId(), def );
					}
				} else
				if(baseMeasure instanceof TDerivedSingleInstanceMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TDerivedSingleInstanceMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						this.derivedSingleInstanceAggregatedModelMap.put( def.getId(), def );
					}
				}
			}
		}

		
		// Medidas derivadas
		for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {
				
			if(element.getValue() instanceof TDerivedSingleInstanceMeasure) {
				
				TDerivedSingleInstanceMeasure measure = (TDerivedSingleInstanceMeasure) element.getValue();
				
				DerivedSingleInstanceMeasure def = this.obtainModel(measure);

				this.searchPpi(measure, def);
				
				if (def!=null) {
					this.derivedSingleInstanceModelMap.put(def.getId(), def);
				}
			}
		}
		
		for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {
			
			if(element.getValue() instanceof TDerivedMultiInstanceMeasure) {
				
				TDerivedMultiInstanceMeasure measure = (TDerivedMultiInstanceMeasure) element.getValue();
				
				DerivedMultiInstanceMeasure def = this.obtainModel(measure);

				this.searchPpi(measure, def);
				
				if (def!=null) {
					this.derivedMultiInstanceModelMap.put( def.getId(), def );
				}
			}
		}
		
	}

}
