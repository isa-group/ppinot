package es.us.isa.isabpm.ppinot.xmlExtracter;

import es.us.isa.isabpm.ppinot.model.*;
import es.us.isa.isabpm.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.isabpm.ppinot.model.base.BaseMeasure;
import es.us.isa.isabpm.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.isabpm.ppinot.model.condition.StateCondition;
import es.us.isa.isabpm.ppinot.model.condition.TimeInstantCondition;
import es.us.isa.isabpm.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.isabpm.ppinot.model.state.GenericState;
import es.us.isa.isabpm.ppinot.model.state.RuntimeState;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDataObject;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TDefinitions;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TExtensionElements;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TProcess;
import es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.TTask;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TAggregatedMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TAggregates;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TAppliesToDataConnector;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TAppliesToElementConnector;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TBaseMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TCountMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TDataMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TDataPropertyConditionMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TDerivedMultiInstanceMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TDerivedSingleInstanceMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TIsGroupedBy;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TMeasureConnector;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TPpi;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TPpiset;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TStateConditionMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TTimeConnector;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TTimeMeasure;
import es.us.isa.isabpm.ppinot.xmlClasses.ppinot.TUses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.*;

import javax.xml.bind.JAXBContext;
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
public class PpiNotXmlExtracter extends XmlExtracter {

	/**
	 * Objeto con instancias de las clases en el paquete de BPMN, que es utilizado para exportar e importar xml
	 */
	private es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.ObjectFactory ofBpmn;

	/**
	 * Objeto con instancias de las clases en el paquete de PPINOT, que es utilizado para exportar e importar xml
	 */
	private es.us.isa.isabpm.ppinot.xmlClasses.ppinot.ObjectFactory ofPpinot;
	
	private Integer contador = 0;
	
	private Map<String, TCountMeasure> countMap;
	private Map<String, TTimeMeasure> timeMap;
	private Map<String, TStateConditionMeasure> stateConditionMap;
	private Map<String, TDataMeasure> dataMap;
	private Map<String, TDataPropertyConditionMeasure> dataPropertyConditionMap;

	private Map<String, TAggregatedMeasure> aggregatedMap;

	private Map<String, TDerivedSingleInstanceMeasure> derivedSingleInstanceMap;
	private Map<String, TDerivedMultiInstanceMeasure> derivedMultiInstanceMap;

	private List<TPpi> ppiList;

	private Map<String, TAppliesToElementConnector> appliesToElementConnectorMap;
	private Map<String, TAppliesToDataConnector> appliesToDataConnectorMap;
	private Map<String, TTimeConnector> timeConnectorMap;
	private Map<String, TUses> usesMap;
	private Map<String, TAggregates> aggregatesMap;
	private Map<String, TIsGroupedBy> isGroupedByMap;

	private Map<String, TTask> taskMap;
	private Map<String, TDataObject> dataobjectMap;

	private Map<String, TimeInstanceMeasure> timeInstanceMeasureMap;
	private Map<String, CountInstanceMeasure> countInstanceMeasureMap;
	private Map<String, StateConditionInstanceMeasure> stateConditionInstanceMeasureMap;
	private Map<String, DataInstanceMeasure> dataInstanceMeasureMap;
	private Map<String, DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceMeasureMap;

	private Map<String, AggregatedMeasure> timeAggregatedMeasureMap;
	private Map<String, AggregatedMeasure> countAggregatedMeasureMap;
	private Map<String, AggregatedMeasure> stateConditionAggregatedMeasureMap;
	private Map<String, AggregatedMeasure> dataAggregatedMeasureMap;
	private Map<String, AggregatedMeasure> dataPropertyConditionAggregatedMeasureMap;

	private Map<String, DerivedSingleInstanceMeasure> derivedSingleInstanceMeasureMap;
	private Map<String, DerivedMultiInstanceMeasure> derivedMultiInstanceMeasureMap;

	private Map<String, PPI> ppiModelMap;


	private List<TimeInstanceMeasure> timeInstanceMeasureList;
	private List<CountInstanceMeasure> countInstanceMeasureList;
	private List<StateConditionInstanceMeasure> stateConditionInstanceMeasureList;
	private List<DataInstanceMeasure> dataInstanceMeasureList;
	private List<DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceMeasureList;

	private List<AggregatedMeasure> timeAggregatedMeasureList;
	private List<AggregatedMeasure> countAggregatedMeasureList;
	private List<AggregatedMeasure> stateConditionAggregatedMeasureList;
	private List<AggregatedMeasure> dataAggregatedMeasureList;
	private List<AggregatedMeasure> dataPropertyConditionAggregatedMeasureList;

	private List<DerivedSingleInstanceMeasure> derivedSingleInstanceMeasureList;
	private List<DerivedMultiInstanceMeasure> derivedMultiInstanceMeasureList;

	private List<PPI> ppiModelList;
	
	/**
	 * Constructor de la clase
	 * 
	 * @throws JAXBException
	 */
	public PpiNotXmlExtracter() throws JAXBException {

		super();
	}

	/**
	 * Inicializa el JAXBContext y los ObjectFactory en dependencia de la ubicación de los paquetes con las clases que permiten
	 * exportar y exportar a xml
	 * @throws JAXBException 
	 * 
	 */
	@Override
	protected void iniExtracter() throws JAXBException {

		this.setTimeInstanceMeasureMap(null);
		this.setCountInstanceMeasureMap(null);
		this.setStateConditionInstanceMeasureMap(null);
		this.setDataInstanceMeasureMap(null);
		this.setDataPropertyConditionInstanceMeasureMap(null);

		this.setTimeAggregatedMeasureMap(null);
		this.setCountAggregatedMeasureMap(null);
		this.setStateConditionAggregatedMeasureMap(null);
		this.setDataAggregatedMeasureMap(null);
		this.setDataPropertyConditionAggregatedMeasureMap(null);

		this.setDerivedSingleInstanceMeasureMap(null);
		this.setDerivedMultiInstanceMeasureMap(null);
		
		this.setPpiModelMap(null);
		
		// crea el JAXBContext para hacer marshall y unmarshall
		this.setJc( JAXBContext.newInstance( 
				es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.ObjectFactory.class, 
				es.us.isa.isabpm.ppinot.xmlClasses.ppinot.ObjectFactory.class, 
				es.us.isa.isabpm.ppinot.xmlClasses.bpmndi.ObjectFactory.class ) );

		// crea un objeto de la clase ObjectFactory para Bpmn
        this.setOfBpmn( new es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.ObjectFactory() );
		// crea un objeto de la clase ObjectFactory para Ppinot
        this.setOfPpinot( new es.us.isa.isabpm.ppinot.xmlClasses.ppinot.ObjectFactory() );
	}
	
	/**
	 * Devuelve el valor del atributo of
	 * Objeto con instancias de las clases en el paquete de BPMN, que es utilizado para exportar e importar xml
	 * 
	 * @return Valor del atributo
	 */
    protected es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.ObjectFactory getOfBpmn() {
		return this.ofBpmn;
	}

	/**
	 * Da valor al atributo element
	 * Objeto con instancias de las clases en el paquete de BPMN, que es utilizado para exportar e importar xml
	 * 
	 * @param of Valor del atributo
	 */
    protected void setOfBpmn(es.us.isa.isabpm.ppinot.xmlClasses.bpmn20.ObjectFactory ofBpmn) {
		this.ofBpmn = ofBpmn;
	}

	/**
	 * Devuelve el valor del atributo ofPpinot
	 * Objeto con instancias de las clases en el paquete de PPINOT, que es utilizado para exportar e importar xml
	 * 
	 * @return Valor del atributo
	 */
    protected es.us.isa.isabpm.ppinot.xmlClasses.ppinot.ObjectFactory getOfPpinot() {
		return this.ofPpinot;
	}

	/**
	 * Da valor al atributo element
	 * Objeto con instancias de las clases en el paquete de PPINOT, que es utilizado para exportar e importar xml
	 * 
	 * @param of Valor del atributo
	 */
    protected void setOfPpinot(es.us.isa.isabpm.ppinot.xmlClasses.ppinot.ObjectFactory ofPpinot) {
		this.ofPpinot = ofPpinot;
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
	
	private Map<String, TCountMeasure> getCountMap() {
		
		if (this.countMap==null)
			this.countMap = new HashMap<String, TCountMeasure>();
		return this.countMap;
	}
	
	private Map<String, TTimeMeasure> getTimeMap() {
		
		if (this.timeMap==null)
			this.timeMap = new HashMap<String, TTimeMeasure>();
		return this.timeMap;
	}
	
	private Map<String, TStateConditionMeasure> getStateConditionMap() {
		
		if (this.stateConditionMap==null)
			this.stateConditionMap = new HashMap<String, TStateConditionMeasure>();
		return this.stateConditionMap;
	}
	
	private Map<String, TDataMeasure> getDataMap() {
		
		if (this.dataMap==null)
			this.dataMap = new HashMap<String, TDataMeasure>();
		return this.dataMap;
	}
	
	private Map<String, TDataPropertyConditionMeasure> getDataPropertyConditionMap() {
		
		if (this.dataPropertyConditionMap==null)
			this.dataPropertyConditionMap = new HashMap<String, TDataPropertyConditionMeasure>();
		return this.dataPropertyConditionMap;
	}
	
	private Map<String, TAggregatedMeasure> getAggregatedMap() {
		
		if (this.aggregatedMap==null)
			this.aggregatedMap = new HashMap<String, TAggregatedMeasure>();
		return this.aggregatedMap;
	}
	
	public Map<String, TDerivedSingleInstanceMeasure> getDerivedSingleInstanceMap() {
		
		if (this.derivedSingleInstanceMap==null)
			this.derivedSingleInstanceMap = new HashMap<String, TDerivedSingleInstanceMeasure>();
		return derivedSingleInstanceMap;
	}

	public Map<String, TDerivedMultiInstanceMeasure> getDerivedMultiInstanceMap() {
		
		if (this.derivedMultiInstanceMap==null)
			this.derivedMultiInstanceMap = new HashMap<String, TDerivedMultiInstanceMeasure>();
		return derivedMultiInstanceMap;
	}
	
	private List<TPpi> getPpiList() {
		
		if (this.ppiList==null)
			this.ppiList = new ArrayList<TPpi>();
		return this.ppiList;
	}
	
	private Map<String, TAppliesToElementConnector> getAppliesToElementConnectorMap() {
		
		if (this.appliesToElementConnectorMap==null)
			this.appliesToElementConnectorMap = new HashMap<String, TAppliesToElementConnector>();
		return this.appliesToElementConnectorMap;
	}
	
	private Map<String, TAppliesToDataConnector> getAppliesToDataConnectorMap() {
		
		if (this.appliesToDataConnectorMap==null)
			this.appliesToDataConnectorMap = new HashMap<String, TAppliesToDataConnector>();
		return this.appliesToDataConnectorMap;
	}
	
	private Map<String, TTimeConnector> getTimeConnectorMap() {
		
		if (this.timeConnectorMap==null)
			this.timeConnectorMap = new HashMap<String, TTimeConnector>();
		return this.timeConnectorMap;
	}
	
	private Map<String, TUses> getUsesMap() {
		
		if (this.usesMap==null)
			this.usesMap = new HashMap<String, TUses>();
		return this.usesMap;
	}
	
	private Map<String, TAggregates> getAggregatesMap() {
		
		if (this.aggregatesMap==null)
			this.aggregatesMap = new HashMap<String, TAggregates>();
		return this.aggregatesMap;
	}
	
	private Map<String, TIsGroupedBy> getIsGroupedByMap() {
		
		if (this.isGroupedByMap==null)
			this.isGroupedByMap = new HashMap<String, TIsGroupedBy>();
		return this.isGroupedByMap;
	}
	
	private Map<String, TTask> getTaskMap() {
		
		if (this.taskMap==null)
			this.taskMap = new HashMap<String, TTask>();
		return this.taskMap;
	}
	
	private Map<String, TDataObject> getDataobjectMap() {
		
		if (this.dataobjectMap==null)
			this.dataobjectMap = new HashMap<String, TDataObject>();
		return this.dataobjectMap;
	}
	
	private Map<String, TimeInstanceMeasure> getTimeInstanceMeasureMap() {
		
		if (this.timeInstanceMeasureMap==null)
			this.timeInstanceMeasureMap = new HashMap<String, TimeInstanceMeasure>();
		return timeInstanceMeasureMap;
	}

	private Map<String, CountInstanceMeasure> getCountInstanceMeasureMap() {
		
		if (this.countInstanceMeasureMap==null)
			this.countInstanceMeasureMap = new HashMap<String, CountInstanceMeasure>();
		return countInstanceMeasureMap;
	}
	
	private Map<String, StateConditionInstanceMeasure> getStateConditionInstanceMeasureMap() {
		
		if (this.stateConditionInstanceMeasureMap==null)
			this.stateConditionInstanceMeasureMap = new HashMap<String, StateConditionInstanceMeasure>();
		return stateConditionInstanceMeasureMap;
	}

	private Map<String, DataInstanceMeasure> getDataInstanceMeasureMap() {
		
		if (this.dataInstanceMeasureMap==null)
			this.dataInstanceMeasureMap = new HashMap<String, DataInstanceMeasure>();
		return dataInstanceMeasureMap;
	}

	private Map<String, DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceMeasureMap() {
		
		if (this.dataPropertyConditionInstanceMeasureMap==null)
			this.dataPropertyConditionInstanceMeasureMap = new HashMap<String, DataPropertyConditionInstanceMeasure>();
		return dataPropertyConditionInstanceMeasureMap;
	}

	private Map<String, AggregatedMeasure> getTimeAggregatedMeasureMap() {
		
		if (this.timeAggregatedMeasureMap==null)
			this.timeAggregatedMeasureMap = new HashMap<String, AggregatedMeasure>();
		return timeAggregatedMeasureMap;
	}

	private Map<String, AggregatedMeasure> getCountAggregatedMeasureMap() {
		
		if (this.countAggregatedMeasureMap==null)
			this.countAggregatedMeasureMap = new HashMap<String, AggregatedMeasure>();
		return countAggregatedMeasureMap;
	}

	private Map<String, AggregatedMeasure> getStateConditionAggregatedMeasureMap() {
		
		if (this.stateConditionAggregatedMeasureMap==null)
			this.stateConditionAggregatedMeasureMap = new HashMap<String, AggregatedMeasure>();
		return stateConditionAggregatedMeasureMap;
	}

	private Map<String, AggregatedMeasure> getDataAggregatedMeasureMap() {
		
		if (this.dataAggregatedMeasureMap==null)
			this.dataAggregatedMeasureMap = new HashMap<String, AggregatedMeasure>();
		return dataAggregatedMeasureMap;
	}

	private Map<String, AggregatedMeasure> getDataPropertyConditionAggregatedMeasureMap() {
		
		if (this.dataPropertyConditionAggregatedMeasureMap==null)
			this.dataPropertyConditionAggregatedMeasureMap = new HashMap<String, AggregatedMeasure>();
		return dataPropertyConditionAggregatedMeasureMap;
	}

	private Map<String, DerivedSingleInstanceMeasure> getDerivedSingleInstanceMeasureMap() {
		
		if (this.derivedSingleInstanceMeasureMap==null)
			this.derivedSingleInstanceMeasureMap = new HashMap<String, DerivedSingleInstanceMeasure>();
		return derivedSingleInstanceMeasureMap;
	}

	private Map<String, DerivedMultiInstanceMeasure> getDerivedMultiInstanceMeasureMap() {
		
		if (this.derivedMultiInstanceMeasureMap==null)
			this.derivedMultiInstanceMeasureMap = new HashMap<String, DerivedMultiInstanceMeasure>();
		return derivedMultiInstanceMeasureMap;
	}
	
	private Map<String, PPI> getPpiModelMap() {
		
		if (this.ppiModelMap==null)
			this.ppiModelMap = new HashMap<String, PPI>();
		return ppiModelMap;
	}

	private void removeTimeInstanceMeasureMap(String id) {
		
		if (this.timeInstanceMeasureMap!=null && this.timeInstanceMeasureMap.containsKey(id)) {
			this.timeInstanceMeasureList.remove(this.timeInstanceMeasureMap.get(id));
			this.timeInstanceMeasureMap.remove(id);
		}
	}

	private void removeCountInstanceMeasureMap(String id) {
		
		if (this.countInstanceMeasureMap!=null && this.countInstanceMeasureMap.containsKey(id)) {
			this.countInstanceMeasureList.remove(this.countInstanceMeasureMap.get(id));
			this.countInstanceMeasureMap.remove(id);
		}
	}
	
	private void removeStateConditionInstanceMeasureMap(String id) {
		
		if (this.stateConditionInstanceMeasureMap!=null && this.stateConditionInstanceMeasureMap.containsKey(id)) {
			this.stateConditionInstanceMeasureList.remove(this.stateConditionInstanceMeasureMap.get(id));
			this.stateConditionInstanceMeasureMap.remove(id);
		}
	}

	private void removeDataInstanceMeasureMap(String id) {
		
		if (this.dataInstanceMeasureMap!=null && this.dataInstanceMeasureMap.containsKey(id)) {
			this.dataInstanceMeasureList.remove(this.dataInstanceMeasureMap.get(id));
			this.dataInstanceMeasureMap.remove(id);
		}
	}

	private void removeDataPropertyConditionInstanceMeasureMap(String id) {
		
		if (this.dataPropertyConditionInstanceMeasureMap!=null && this.dataPropertyConditionInstanceMeasureMap.containsKey(id)) {
			this.dataPropertyConditionInstanceMeasureList.remove(this.dataPropertyConditionInstanceMeasureMap.get(id));
			this.dataPropertyConditionInstanceMeasureMap.remove(id);
		}
	}

	private void removeTimeAggregatedMeasureMap(String id) {
		
		if (this.timeAggregatedMeasureMap!=null && this.timeAggregatedMeasureMap.containsKey(id)) {
			this.timeAggregatedMeasureList.remove(this.timeAggregatedMeasureMap.get(id));
			this.timeAggregatedMeasureMap.remove(id);
		}
	}

	private void removeCountAggregatedMeasureMap(String id) {
		
		if (this.countAggregatedMeasureMap!=null && this.countAggregatedMeasureMap.containsKey(id)) {
			this.countAggregatedMeasureList.remove(this.countAggregatedMeasureMap.get(id));
			this.countAggregatedMeasureMap.remove(id);
		}
	}

	private void removeStateConditionAggregatedMeasureMap(String id) {
		
		if (this.stateConditionAggregatedMeasureMap!=null && this.stateConditionAggregatedMeasureMap.containsKey(id)) {
			this.stateConditionAggregatedMeasureList.remove(this.stateConditionAggregatedMeasureMap.get(id));
			this.stateConditionAggregatedMeasureMap.remove(id);
		}
	}

	private void removeDataAggregatedMeasureMap(String id) {
		
		if (this.dataAggregatedMeasureMap!=null && this.dataAggregatedMeasureMap.containsKey(id)) {
			this.dataAggregatedMeasureList.remove(this.dataAggregatedMeasureMap.get(id));
			this.dataAggregatedMeasureMap.remove(id);
		}
	}

	private void removeDataPropertyConditionAggregatedMeasureMap(String id) {
		
		if (this.dataPropertyConditionAggregatedMeasureMap!=null && this.dataPropertyConditionAggregatedMeasureMap.containsKey(id)) {
			this.dataPropertyConditionAggregatedMeasureList.remove(this.dataPropertyConditionAggregatedMeasureMap.get(id));
			this.dataPropertyConditionAggregatedMeasureMap.remove(id);
		}
	}

	private void removePpiModelMap(String id) {
		
		if (this.ppiModelMap!=null && this.ppiModelMap.containsKey(id)) {
			this.ppiModelMap.remove(this.ppiModelMap.get(id));
			this.ppiModelMap.remove(id);
		}
	}
	
	public void setTimeInstanceMeasureMap(Map<String, TimeInstanceMeasure> timeInstanceMeasureMap) {
		this.timeInstanceMeasureMap = timeInstanceMeasureMap;
	}

	public void setCountInstanceMeasureMap(Map<String, CountInstanceMeasure> countInstanceMeasureMap) {
		this.countInstanceMeasureMap = countInstanceMeasureMap;
	}

	public void setStateConditionInstanceMeasureMap(Map<String, StateConditionInstanceMeasure> stateConditionInstanceMeasureMap) {
		this.stateConditionInstanceMeasureMap = stateConditionInstanceMeasureMap;
	}

	public void setDataInstanceMeasureMap(Map<String, DataInstanceMeasure> dataInstanceMeasureMap) {
		this.dataInstanceMeasureMap = dataInstanceMeasureMap;
	}

	public void setDataPropertyConditionInstanceMeasureMap(Map<String, DataPropertyConditionInstanceMeasure> dataPropertyConditionInstanceMeasureMap) {
		this.dataPropertyConditionInstanceMeasureMap = dataPropertyConditionInstanceMeasureMap;
	}

	public void setTimeAggregatedMeasureMap(Map<String, AggregatedMeasure> timeAggregatedMeasureMap) {
		this.timeAggregatedMeasureMap = timeAggregatedMeasureMap;
	}

	public void setCountAggregatedMeasureMap(Map<String, AggregatedMeasure> countAggregatedMeasureMap) {
		this.countAggregatedMeasureMap = countAggregatedMeasureMap;
	}

	public void setStateConditionAggregatedMeasureMap(Map<String, AggregatedMeasure> stateConditionAggregatedMeasureMap) {
		this.stateConditionAggregatedMeasureMap = stateConditionAggregatedMeasureMap;
	}

	public void setDataAggregatedMeasureMap(Map<String, AggregatedMeasure> dataAggregatedMeasureMap) {
		this.dataAggregatedMeasureMap = dataAggregatedMeasureMap;
	}

	public void setDataPropertyConditionAggregatedMeasureMap(Map<String, AggregatedMeasure> dataPropertyConditionAggregatedMeasureMap) {
		this.dataPropertyConditionAggregatedMeasureMap = dataPropertyConditionAggregatedMeasureMap;
	}

	public void setDerivedSingleInstanceMeasureMap(Map<String, DerivedSingleInstanceMeasure> derivedSingleInstanceMeasureMap) {
		this.derivedSingleInstanceMeasureMap = derivedSingleInstanceMeasureMap;
	}

	public void setDerivedMultiInstanceMeasureMap(Map<String, DerivedMultiInstanceMeasure> derivedMultiInstanceMeasureMap) {
		this.derivedMultiInstanceMeasureMap = derivedMultiInstanceMeasureMap;
	}

	public void setPpiModelMap(Map<String, PPI> ppiModelMap) {
		this.ppiModelMap = ppiModelMap;
	}

	/**
	 * Busca un conector MeasureConnector que salga de la medida measure
	 * 
	 * @param ppi La medida de la que se busca el conector
	 * @return Conector de la medida
	 */
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
		
		if (this.getCountInstanceMeasureMap().containsKey(id)) {
			measure = this.getCountInstanceMeasureMap().get(id);
			if (remove)
				this.removeCountInstanceMeasureMap(id);
		} else
		if (this.getTimeInstanceMeasureMap().containsKey(id)) {
			measure = this.getTimeInstanceMeasureMap().get(id);
			if (remove)
				this.removeTimeInstanceMeasureMap(id);
		} else
		if (this.getStateConditionInstanceMeasureMap().containsKey(id)) {
			measure = this.getStateConditionInstanceMeasureMap().get(id);
			if (remove)
				this.removeStateConditionInstanceMeasureMap(id);
		} else
		if (this.getDataInstanceMeasureMap().containsKey(id)) {
			measure = this.getDataInstanceMeasureMap().get(id);
			if (remove)
				this.removeDataInstanceMeasureMap(id);
		} else
		if (this.getDataPropertyConditionInstanceMeasureMap().containsKey(id)) {
			measure = this.getDataPropertyConditionInstanceMeasureMap().get(id);
			if (remove)
				this.removeDataPropertyConditionInstanceMeasureMap(id);
		} else
		if (this.getCountAggregatedMeasureMap().containsKey(id)) {
			measure = this.getCountAggregatedMeasureMap().get(id);
			if (remove)
				this.removeCountAggregatedMeasureMap(id);
		} else
		if (this.getTimeAggregatedMeasureMap().containsKey(id)) {
			measure = this.getTimeAggregatedMeasureMap().get(id);
			if (remove)
				this.removeTimeAggregatedMeasureMap(id);
		} else
		if (this.getStateConditionAggregatedMeasureMap().containsKey(id)) {
			measure = this.getStateConditionAggregatedMeasureMap().get(id);
			if (remove)
				this.removeStateConditionAggregatedMeasureMap(id);
		} else
		if (this.getDataAggregatedMeasureMap().containsKey(id)) {
			measure = this.getDataAggregatedMeasureMap().get(id);
			if (remove)
				this.removeDataAggregatedMeasureMap(id);
		} else
		if (this.getDataPropertyConditionAggregatedMeasureMap().containsKey(id)) {
			measure = this.getDataPropertyConditionAggregatedMeasureMap().get(id);
			if (remove)
				this.removeDataPropertyConditionAggregatedMeasureMap(id);
		} else {

			Iterator<Entry<String, DerivedSingleInstanceMeasure>> itInst = this.getDerivedSingleInstanceMeasureMap().entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, DerivedSingleInstanceMeasure> pairs = (Map.Entry<String, DerivedSingleInstanceMeasure>)itInst.next();
		        DerivedSingleInstanceMeasure m = (DerivedSingleInstanceMeasure) pairs.getValue();

		        measure = m.getUsedMeasureId(id);
				if (measure!=null) {
					break;
				}
			}
		
		    if (measure==null) {
				Iterator<Entry<String, DerivedMultiInstanceMeasure>> itInstP = this.getDerivedMultiInstanceMeasureMap().entrySet().iterator();
			    while (itInstP.hasNext()) {
			        Map.Entry<String, DerivedMultiInstanceMeasure> pairs = (Map.Entry<String, DerivedMultiInstanceMeasure>)itInstP.next();
			        DerivedMultiInstanceMeasure m = (DerivedMultiInstanceMeasure) pairs.getValue();

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
		
		if (this.getTimeInstanceMeasureMap().containsKey(measure.getId()))
			return this.getTimeInstanceMeasureMap().get(measure.getId());
		
		// crea la definición de la medida TimeMeasure a partir de la información en el xml
		// las actividades a la cuales se aplica la medida se obtiene de los conectores
		Map<String, TTimeConnector> map = findTimeConnectors(measure);
		TTimeConnector conFrom = map.get("From");
		TTimeConnector conTo = map.get("To");
		
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
					measure.getTimeMeasureType(),  
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
					"",
					"" );
		return def;
	}
	private CountInstanceMeasure obtainModel(TCountMeasure measure) {
		
		if (this.getCountInstanceMeasureMap().containsKey(measure.getId()))
			return this.getCountInstanceMeasureMap().get(measure.getId());
		
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
		
		if (this.getStateConditionInstanceMeasureMap().containsKey(measure.getId()))
			return this.getStateConditionInstanceMeasureMap().get(measure.getId());
		
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
		
		if (this.getDataInstanceMeasureMap().containsKey(measure.getId()))
			return this.getDataInstanceMeasureMap().get(measure.getId());

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
		
		if (this.getDataPropertyConditionInstanceMeasureMap().containsKey(measure.getId()))
			return this.getDataPropertyConditionInstanceMeasureMap().get(measure.getId());
		
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
		
		if (this.getTimeAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getTimeAggregatedMeasureMap().get(measure.getId());
		
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
		
		if (this.getTimeAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getTimeAggregatedMeasureMap().get(measure.getId());
		
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
		
		if (this.getCountAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getCountAggregatedMeasureMap().get(measure.getId());
		
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
		
		if (this.getStateConditionAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getStateConditionAggregatedMeasureMap().get(measure.getId());
		
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
		
		if (this.getDataAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getDataAggregatedMeasureMap().get(measure.getId());
		
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
		
		if (this.getDataPropertyConditionAggregatedMeasureMap().containsKey(measure.getId()))
			return this.getDataPropertyConditionAggregatedMeasureMap().get(measure.getId());
		
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
	private DerivedSingleInstanceMeasure obtainModel(TDerivedSingleInstanceMeasure measure) {
		
		if (this.getDerivedSingleInstanceMeasureMap().containsKey(measure.getId()))
			return this.getDerivedSingleInstanceMeasureMap().get(measure.getId());
		
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
		
		if (this.getDerivedMultiInstanceMeasureMap().containsKey(measure.getId()))
			return this.getDerivedMultiInstanceMeasureMap().get(measure.getId());
		
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
		
    	TTask task = this.getOfBpmn().createTTask();
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
		
		TDataObject dataobject = this.getOfBpmn().createTDataObject();
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
				
				this.getPpiModelMap().put(ppi.getId(), ppiModel);
				if (this.ppiModelList==null)
					this.ppiModelList = new ArrayList<PPI>();
		        this.ppiModelList.add(ppiModel);
				
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
	@Override
	public List<CountInstanceMeasure> getCountInstanceMeasure() {
		
		if (this.countInstanceMeasureList==null)
			this.countInstanceMeasureList = new ArrayList<CountInstanceMeasure>();
		return countInstanceMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas del tipo TimeMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<TimeInstanceMeasure> getTimeInstanceMeasure() {
		
		if (this.timeInstanceMeasureList==null)
			this.timeInstanceMeasureList = new ArrayList<TimeInstanceMeasure>();
		return timeInstanceMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas del tipo StateConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<StateConditionInstanceMeasure> getStateConditionInstanceMeasure() {
		
		if (this.stateConditionInstanceMeasureList==null)
			this.stateConditionInstanceMeasureList = new ArrayList<StateConditionInstanceMeasure>();
		return stateConditionInstanceMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas del tipo DataMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<DataInstanceMeasure> getDataInstanceMeasure() {
		
		if (this.dataInstanceMeasureList==null)
			this.dataInstanceMeasureList = new ArrayList<DataInstanceMeasure>();
		return dataInstanceMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas del tipo DataPropertyConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<DataPropertyConditionInstanceMeasure> getDataPropertyConditionInstanceMeasure() {
		
		if (this.dataPropertyConditionInstanceMeasureList==null)
			this.dataPropertyConditionInstanceMeasureList = new ArrayList<DataPropertyConditionInstanceMeasure>();
		return dataPropertyConditionInstanceMeasureList;
	}
	
	/**
	 * Devuelve una lista con las medidas agregadas del tipo TimeMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<AggregatedMeasure> getTimeAggregatedMeasure() {

		if (this.timeAggregatedMeasureList==null)
			this.timeAggregatedMeasureList = new ArrayList<AggregatedMeasure>();
		return timeAggregatedMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo CountMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<AggregatedMeasure> getCountAggregatedMeasure() {

		if (this.countAggregatedMeasureList==null)
			this.countAggregatedMeasureList = new ArrayList<AggregatedMeasure>();
		return countAggregatedMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo StateConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<AggregatedMeasure> getStateConditionAggregatedMeasure() {

		if (this.stateConditionAggregatedMeasureList==null)
			this.stateConditionAggregatedMeasureList = new ArrayList<AggregatedMeasure>();
		return stateConditionAggregatedMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas agregadas del tipo DataMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<AggregatedMeasure> getDataAggregatedMeasure() {

		if (this.dataAggregatedMeasureList==null)
			this.dataAggregatedMeasureList = new ArrayList<AggregatedMeasure>();
		return dataAggregatedMeasureList;
	}
	
	/**
	 * Devuelve una lista con las medidas agregadas del tipo DataPropertyConditionMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<AggregatedMeasure> getDataPropertyConditionAggregatedMeasure() {

		if (this.dataPropertyConditionAggregatedMeasureList==null)
			this.dataPropertyConditionAggregatedMeasureList = new ArrayList<AggregatedMeasure>();
		return dataPropertyConditionAggregatedMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas derivadas del tipo DerivedSingleInstanceMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<DerivedSingleInstanceMeasure> getDerivedSingleInstanceMeasure() {
		
		if (this.derivedSingleInstanceMeasureList==null)
			this.derivedSingleInstanceMeasureList = new ArrayList<DerivedSingleInstanceMeasure>();
		return derivedSingleInstanceMeasureList;
	}

	/**
	 * Devuelve una lista con las medidas derivadas del tipo DerivedMultiInstanceMeasure, para ser utilizada en la appweb
	 * 
	 * @return Lista de medidas
	 */
	@Override
	public List<DerivedMultiInstanceMeasure> getDerivedMultiInstanceMeasure() {
		
		if (this.derivedMultiInstanceMeasureList==null)
			this.derivedMultiInstanceMeasureList = new ArrayList<DerivedMultiInstanceMeasure>();
		return derivedMultiInstanceMeasureList;
	}
	
	@Override
	public List<PPI> getPpiModel() {
		
		if (this.ppiModelList==null)
			this.ppiModelList = new ArrayList<PPI>();
		return ppiModelList;
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
		if (this.getTimeMap().containsKey(def.getId())) {
			
			map.put("createdMeasure", map.get(def.getId()));
			return map;
		}
		
		TTimeMeasure measure = this.getOfPpinot().createTTimeMeasure();
		
		measure.setId(generarId("timeMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TTimeConnector conFrom = this.getOfPpinot().createTTimeConnector();
		
		conFrom.setId(generarId("timeConnector", ""));
		conFrom.setSourceRef(measure);
		conFrom.setTargetRef(def.getFrom().getAppliesTo());
		conFrom.setWhen((def.getFrom().getChangesToState().getState()==GenericState.END)?"End":"Start");
		conFrom.setConditiontype("From");

		TTimeConnector conTo = this.getOfPpinot().createTTimeConnector();
		
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
		if (this.getCountMap().containsKey(def.getId())) {
			
			map.put("createdMeasure", this.getCountMap().get(def.getId()));
			return map;
		}
		
		TCountMeasure measure = this.getOfPpinot().createTCountMeasure();
		
		measure.setId(generarId("countMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = this.getOfPpinot().createTAppliesToElementConnector();
		
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
		if (this.getStateConditionMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getStateConditionMap().get(def.getId()));
			return map;
		}
		
		TStateConditionMeasure measure = this.getOfPpinot().createTStateConditionMeasure();
		
		measure.setId(generarId("stateConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = this.getOfPpinot().createTAppliesToElementConnector();
		
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
		if (this.getDataMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getDataMap().get(def.getId()));
			return map;
		}
		
		TDataMeasure measure = this.getOfPpinot().createTDataMeasure();
		
		measure.setId(generarId("dataMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = this.getOfPpinot().createTAppliesToDataConnector();
		
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
		if (this.getDataPropertyConditionMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getDataPropertyConditionMap().get(def.getId()));
			return map;
		}
		
		TDataPropertyConditionMeasure measure = this.getOfPpinot().createTDataPropertyConditionMeasure();
		
		measure.setId(generarId("dataPropertyConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = this.getOfPpinot().createTAppliesToDataConnector();
		
		con.setId(generarId("appliesToDataConnector", ""));
		con.setSourceRef(measure);
		con.setRestriction(((DataPropertyCondition) def.getCondition()).getRestriction());
		con.setState(((DataPropertyCondition) def.getCondition()).getStateConsidered().getStateString());
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	private Map<String,Object> obtainInfo(AggregatedMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.getAggregatedMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getAggregatedMap().get(def.getId()));
			return map;
		}
		
		TAggregatedMeasure measure = this.getOfPpinot().createTAggregatedMeasure();

		measure.setId(generarId("aggregatedMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());
		measure.setSamplingfrequency(def.getSamplingFrequency());
		
		measure.setAggregationfunction(def.getAggregationFunction());
		
		if (!def.getGroupedBy().getSelection().contentEquals("") || !def.getGroupedBy().getDataobject().contentEquals("")) {
			
			TIsGroupedBy con = this.getOfPpinot().createTIsGroupedBy();
			
			con.setId(generarId("isGroupedBy", ""));
			con.setSourceRef(measure);
			con.setTargetRef(def.getGroupedBy().getDataobject());
			
			map.put("connectorIsGroupedBy", con);
		}

		if (def.getAggregates()) {
			
			TAggregates con = this.getOfPpinot().createTAggregates();
			
			con.setId(generarId("aggregates", ""));
			con.setSourceRef(measure);
			
			TBaseMeasure baseMeasure = null;
			if (def.getBaseMeasure() instanceof TimeInstanceMeasure) {
				
				baseMeasure = this.getTimeMap().get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof CountInstanceMeasure) {
	
				baseMeasure = this.getCountMap().get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof StateConditionInstanceMeasure) {
	
				baseMeasure = this.getStateConditionMap().get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof DataInstanceMeasure) {
	
				baseMeasure = this.getDataMap().get(def.getBaseMeasure().getId());
			} else
			if (def.getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure) {
	
				baseMeasure = this.getDataPropertyConditionMap().get(def.getBaseMeasure().getId());
			}
			
			con.setTargetRef(baseMeasure);
			
			map.put("measure", measure);
			map.put("connectorAggregates", con);
		} else {
			if (def.getBaseMeasure() instanceof TimeInstanceMeasure) {
				
				Map<String,Object> instanceMap = this.obtainInfo((TimeInstanceMeasure) def.getBaseMeasure());
				((TTimeConnector) instanceMap.get("connectorFrom")).setSourceRef(measure);
				((TTimeConnector) instanceMap.get("connectorTo")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getOfPpinot().createTimeMeasure((TTimeMeasure) instanceMap.get("measure")));
				
				map.put("connectorFrom", instanceMap.get("connectorFrom"));
				map.put("connectorTo", instanceMap.get("connectorTo"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof CountInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((CountInstanceMeasure) def.getBaseMeasure());
				((TAppliesToElementConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getOfPpinot().createCountMeasure((TCountMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof StateConditionInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((StateConditionInstanceMeasure) def.getBaseMeasure());
				((TAppliesToElementConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getOfPpinot().createStateConditionMeasure((TStateConditionMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof DataInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((DataInstanceMeasure) def.getBaseMeasure());
				((TAppliesToDataConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getOfPpinot().createDataMeasure((TDataMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			} else
			if (def.getBaseMeasure() instanceof DataPropertyConditionInstanceMeasure) {
	
				Map<String,Object> instanceMap = this.obtainInfo((DataPropertyConditionInstanceMeasure) def.getBaseMeasure());
				((TAppliesToDataConnector) instanceMap.get("connector")).setSourceRef(measure);
				
				measure.setBaseMeasure(this.getOfPpinot().createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) instanceMap.get("measure")));
				
				map.put("connector", instanceMap.get("connector"));
				map.put("measure", measure);
			}
		}
		
		return map;
	}
	private Map<String,Object> obtainInfo( DerivedSingleInstanceMeasure def) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if (this.getDerivedSingleInstanceMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getDerivedSingleInstanceMap().get(def.getId()));
			return map;
		}
		
		TDerivedSingleInstanceMeasure measure = this.getOfPpinot().createTDerivedSingleInstanceMeasure();
		
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

			TUses con = this.getOfPpinot().createTUses();
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
		if (this.getDerivedSingleInstanceMap().containsKey(def.getId())){
			
			map.put("createdMeasure", this.getDerivedSingleInstanceMap().get(def.getId()));
			return map;
		}
		
		TDerivedMultiInstanceMeasure measure = this.getOfPpinot().createTDerivedMultiInstanceMeasure();
		
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

			TUses con = this.getOfPpinot().createTUses();
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
	@Override
	public void setTimeMeasure(List<TimeInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (TimeInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getTimeMap().put(((TTimeMeasure) map.get("measure")).getId(), (TTimeMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorFrom")) {
	    	    	
	    			TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getTimeConnectorMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorTo")) {
	    	    	
	    			TTimeConnector con = (TTimeConnector) map.get("connectorTo");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getTimeConnectorMap().put(con.getId(), con);
	    		}
			    i++;
		    }
	    }
	}

	@Override
	public void setCountMeasure(List<CountInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (CountInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getCountMap().put(((TCountMeasure) map.get("measure")).getId(), (TCountMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getAppliesToElementConnectorMap().put(con.getId(), con);
	    		}
			    i++;
		    }
	    }
	}

	@Override
	public void setStateConditionMeasure(List<StateConditionInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (StateConditionInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getStateConditionMap().put(((TStateConditionMeasure) map.get("measure")).getId(), (TStateConditionMeasure) map.get("measure"));
	    		
//	    		if (map.containsKey("ppi"))
//	    			this.getPpiList().add((TPpi) map.get("ppi"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getAppliesToElementConnectorMap().put(con.getId(), con);
	    		}
			    i++;
		    }
	    }
	}

	@Override
	public void setDataMeasure(List<DataInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (DataInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getDataMap().put(((TDataMeasure) map.get("measure")).getId(), (TDataMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getAppliesToDataConnectorMap().put(con.getId(), con);
	    		}
			    i++;
		    }
	    }
	}

	@Override
	public void setDataPropertyConditionMeasure(List<DataPropertyConditionInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (DataPropertyConditionInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getDataPropertyConditionMap().put(((TDataPropertyConditionMeasure) map.get("measure")).getId(), (TDataPropertyConditionMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getAppliesToDataConnectorMap().put(con.getId(), con);
	    		}
			    i++;
		    }
	    }
	}

	@Override
	public void setTimeAggregatedMeasure(List<AggregatedMeasure> modelList) {

    	if (modelList!=null) {
    		
    	    Integer i = 1;
			for (AggregatedMeasure def: modelList) {
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.getAggregatedMap().put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.getAggregatesMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getIsGroupedByMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorFrom")) {
	    	    	
	    	    	TTimeConnector con = (TTimeConnector) map.get("connectorFrom");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getTimeConnectorMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorTo")) {
	    	    	
	    	    	TTimeConnector con = (TTimeConnector) map.get("connectorTo");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getTimeConnectorMap().put(con.getId(), con);
	    		}
	
			    i++;
			}
    	}
	}

	@Override
	public void setCountAggregatedMeasure(List<AggregatedMeasure> modelList) {

    	if (modelList!=null) {
    		
    	    Integer i = 1;
			for (AggregatedMeasure def: modelList) {
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.getAggregatedMap().put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.getAggregatesMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getIsGroupedByMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getAppliesToElementConnectorMap().put(con.getId(), con);
	    		}
	
			    i++;
			}
    	}
	}

	@Override
	public void setStateConditionAggregatedMeasure(List<AggregatedMeasure> modelList) {

    	if (modelList!=null) {
    		
    	    Integer i = 1;
			for (AggregatedMeasure def: modelList) {
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.getAggregatedMap().put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.getAggregatesMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getIsGroupedByMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToElementConnector con = (TAppliesToElementConnector) map.get("connector");
	    	    	TTask task = chainTask(con);
	    	    	this.getTaskMap().put(task.getId(), task);

	    	    	this.getAppliesToElementConnectorMap().put(con.getId(), con);
	    		}
	
			    i++;
			}
    	}
	}

	@Override
	public void setDataAggregatedMeasure(List<AggregatedMeasure> modelList) {

    	if (modelList!=null) {
    		
    	    Integer i = 1;
			for (AggregatedMeasure def: modelList) {
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.getAggregatedMap().put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.getAggregatesMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getIsGroupedByMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(((TAggregatedMeasure) map.get("measure")).getId(), dataobject);

	    	    	this.getAppliesToDataConnectorMap().put(con.getId(), con);
	    		}
	
			    i++;
			}
    	}
	}

	@Override
	public void setDataPropertyConditionAggregatedMeasure(List<AggregatedMeasure> modelList) {

    	if (modelList!=null) {
    		
    	    Integer i = 1;
			for (AggregatedMeasure def: modelList) {
		    	
				Map<String,Object> map = this.obtainInfo(def);

	    		if (map.containsKey("measure"))
	    			this.getAggregatedMap().put(((TAggregatedMeasure) map.get("measure")).getId(), (TAggregatedMeasure) map.get("measure"));
		    	
	    		if (map.containsKey("connectorAggregates")) {
	    	    	
	    	    	TAggregates con = (TAggregates) map.get("connectorAggregates");

	    	    	this.getAggregatesMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connectorIsGroupedBy")) {
	    	    	
	    	    	TIsGroupedBy con = (TIsGroupedBy) map.get("connectorIsGroupedBy");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getIsGroupedByMap().put(con.getId(), con);
	    		}
		    	
	    		if (map.containsKey("connector")) {
	    	    	
	    	    	TAppliesToDataConnector con = (TAppliesToDataConnector) map.get("connector");
	    	    	TDataObject dataobject = chainDataobject(con);
	    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);

	    	    	this.getAppliesToDataConnectorMap().put(con.getId(), con);
	    		}
	
			    i++;
			}
    	}
	}

	@Override
	public void setDerivedSingleInstanceMeasure(List<DerivedSingleInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (DerivedSingleInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getDerivedSingleInstanceMap().put(((TDerivedSingleInstanceMeasure) map.get("measure")).getId(), (TDerivedSingleInstanceMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connectorUses")) {
	    			
	    			for ( TUses con : (List<TUses>) map.get("connectorUses"))
	    				this.getUsesMap().put(con.getId(), con);
	    		}
/*
	    		if (map.containsKey("used")) {
	    			
	    			for ( TMeasure usedMeasure : (List<TMeasure>) map.get("used")) {

		    			if (usedMeasure instanceof TCountMeasure)
		    				this.getCountMap().put(((TCountMeasure) usedMeasure).getId(), (TCountMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TTimeMeasure) 
		    				this.getTimeMap().put(((TTimeMeasure) usedMeasure).getId(), (TTimeMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TStateConditionMeasure) 
		    				this.getStateConditionMap().put(((TStateConditionMeasure) usedMeasure).getId(), (TStateConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataMeasure) 
		    				this.getDataMap().put(((TDataMeasure) usedMeasure).getId(), (TDataMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataPropertyConditionMeasure)
		    				this.getDataPropertyConditionMap().put(((TDataPropertyConditionMeasure) usedMeasure).getId(), (TDataPropertyConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TAggregatedMeasure)
		    				this.getAggregatedMap().put(((TAggregatedMeasure) usedMeasure).getId(), (TAggregatedMeasure) usedMeasure);
		    		
			    		if (map.containsKey("connector")) {
			    	    	
			    			for ( TMeasureConnector con : (List<TMeasureConnector>) map.get("connector")) {
			    				
			    				if (con.getSourceRef()==usedMeasure) {
			    					
					    			if (usedMeasure instanceof TDataMeasure || usedMeasure instanceof TDataPropertyConditionMeasure) {
					    				
						    	    	TDataObject dataobject = chainDataobject(con);
						    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);
					    			} else {
					    				
						    	    	TTask task = chainTask(con);
						    	    	this.getTaskMap().put(task.getId(), task);
					    			}
			
									if (con instanceof TAppliesToElementConnector)
						    	    	this.getAppliesToElementConnectorMap().put(con.getId(), (TAppliesToElementConnector) con);
									else
									if (con instanceof TAppliesToDataConnector)
						    	    	this.getAppliesToDataConnectorMap().put(con.getId(), (TAppliesToDataConnector) con);
									else
									if (con instanceof TAggregates)
						    	    	this.getAggregatesMap().put(con.getId(), (TAggregates) con);
									else
									if (con instanceof TIsGroupedBy)
						    	    	this.getIsGroupedByMap().put(con.getId(), (TIsGroupedBy) con);

					    	    	break;
			    				}
			    			}
			    		}
	    			}
			    	
		    		if (map.containsKey("connectorFrom")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorFrom")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.getTaskMap().put(task.getId(), task);
			    	    	
			    	    	this.getTimeConnectorMap().put(con.getId(), con);
		    			}
		    		}
			    	
		    		if (map.containsKey("connectorTo")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorTo")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.getTaskMap().put(task.getId(), task);
	
			    	    	this.getTimeConnectorMap().put(con.getId(), con);
		    			}
		    		}
	    		}
*/
			    i++;
		    }
	    }
	}

	@Override
	public void setDerivedMultiInstanceMeasure(List<DerivedMultiInstanceMeasure> modelList) {

	    if (modelList!=null) {
		    Integer i = 1;
		    for (DerivedMultiInstanceMeasure def : modelList) {
		    		
		    	Map<String,Object> map = this.obtainInfo(def);
	    		
	    		if (map.containsKey("measure"))
	    			this.getDerivedMultiInstanceMap().put(((TDerivedMultiInstanceMeasure) map.get("measure")).getId(), (TDerivedMultiInstanceMeasure) map.get("measure"));
	    		
	    		if (map.containsKey("connectorUses")) {
	    			
	    			for ( TUses con : (List<TUses>) map.get("connectorUses"))
	    				this.getUsesMap().put(con.getId(), con);
	    		}
/*
	    		if (map.containsKey("used")) {
	    			
	    			for ( TMeasure usedMeasure : (List<TMeasure>) map.get("used")) {

		    			if (usedMeasure instanceof TCountMeasure)
		    				this.getCountMap().put(((TCountMeasure) usedMeasure).getId(), (TCountMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TTimeMeasure) 
		    				this.getTimeMap().put(((TTimeMeasure) usedMeasure).getId(), (TTimeMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TStateConditionMeasure) 
		    				this.getStateConditionMap().put(((TStateConditionMeasure) usedMeasure).getId(), (TStateConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataMeasure) 
		    				this.getDataMap().put(((TDataMeasure) usedMeasure).getId(), (TDataMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TDataPropertyConditionMeasure)
		    				this.getDataPropertyConditionMap().put(((TDataPropertyConditionMeasure) usedMeasure).getId(), (TDataPropertyConditionMeasure) usedMeasure);
		    			else
		    			if (usedMeasure instanceof TAggregatedMeasure)
		    				this.getAggregatedMap().put(((TAggregatedMeasure) usedMeasure).getId(), (TAggregatedMeasure) usedMeasure);
		    		
			    		if (map.containsKey("connector")) {
			    	    	
			    			for ( TMeasureConnector con : (List<TMeasureConnector>) map.get("connector")) {
			    				
			    				if (con.getSourceRef()==usedMeasure) {
			    				
					    			if ((usedMeasure instanceof TAggregatedMeasure && 
					    					(((TAggregatedMeasure) usedMeasure).getBaseMeasure().getValue() instanceof TDataMeasure || 
					    					 ((TAggregatedMeasure) usedMeasure).getBaseMeasure().getValue() instanceof TDataPropertyConditionMeasure)) ||
					    				usedMeasure instanceof TDataMeasure || usedMeasure instanceof TDataPropertyConditionMeasure) {
					    				
						    	    	TDataObject dataobject = chainDataobject(con);
						    	    	this.getDataobjectMap().put(dataobject.getId(), dataobject);
					    			} else {
					    				
						    	    	TTask task = chainTask(con);
						    	    	this.getTaskMap().put(task.getId(), task);
					    			}
			
									if (con instanceof TAppliesToElementConnector)
						    	    	this.getAppliesToElementConnectorMap().put(con.getId(), (TAppliesToElementConnector) con);
									else
									if (con instanceof TAppliesToDataConnector)
						    	    	this.getAppliesToDataConnectorMap().put(con.getId(), (TAppliesToDataConnector) con);
									else
									if (con instanceof TAggregates)
						    	    	this.getAggregatesMap().put(con.getId(), (TAggregates) con);
									else
									if (con instanceof TIsGroupedBy)
						    	    	this.getIsGroupedByMap().put(con.getId(), (TIsGroupedBy) con);

					    	    	break;
			    				}
			    			}
			    		}
	    			}
			    	
		    		if (map.containsKey("connectorFrom")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorFrom")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.getTaskMap().put(task.getId(), task);
			    	    	
			    	    	this.getTimeConnectorMap().put(con.getId(), con);
		    			}
		    		}
			    	
		    		if (map.containsKey("connectorTo")) {
		    	    	
		    			for ( TTimeConnector con : (List<TTimeConnector>) map.get("connectorTo")) {
		    	    	
			    	    	TTask task = chainTask(con);
			    	    	this.getTaskMap().put(task.getId(), task);
	
			    	    	this.getTimeConnectorMap().put(con.getId(), con);
		    			}
		    		}
	    		}
*/
	    		i++;
		    }
	    }
	}
	
	@Override
	public void setPpiModel(List<PPI> modelList) {
		
	    if (modelList!=null) {
		    Integer i = 1;
		    for (PPI def : modelList) {
		
		        MeasureDefinition m = def.getMeasuredBy();
		        
				TPpi ppi = this.getOfPpinot().createTPpi();
				ppi.setId(def.getId());

				ppi.setName(def.getName());
				ppi.setGoals(def.getGoals());
				ppi.setResponsible(def.getResponsible());
				ppi.setInformed(def.getInformed());
				ppi.setComments(def.getComments());
		    	
				ppi.setTarget(this.generateTarget(def.getTarget().getRefMin(), def.getTarget().getRefMax()));
				ppi.setScope(this.generateAnalisysPeriod(def.getScope().getYear(), def.getScope().getPeriod(), def.getScope().getStartDate(), def.getScope().getEndDate(), def.getScope().getInStart(), def.getScope().getInEnd()));

		    	ppi.setMeasuredBy(m.getId());

    			this.getPpiList().add(ppi);
		    	
	    		i++;
		    }
	    }
	}

	@Override
	protected void generateExportElement(String procId) {
		
	    TPpiset ppiset = this.getOfPpinot().createTPpiset();
	    ppiset.setId("ppiset_1");
	    
		Iterator<Entry<String, TCountMeasure>> itInst1 = this.getCountMap().entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, TCountMeasure> pairs = (Map.Entry<String, TCountMeasure>)itInst1.next();
	    	ppiset.getBaseMeasure().add(this.getOfPpinot().createCountMeasure((TCountMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TTimeMeasure>> itInst2 = this.getTimeMap().entrySet().iterator();
	    while (itInst2.hasNext()) {
	        Map.Entry<String, TTimeMeasure> pairs = (Map.Entry<String, TTimeMeasure>)itInst2.next();
	    	ppiset.getBaseMeasure().add(this.getOfPpinot().createTimeMeasure((TTimeMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TStateConditionMeasure>> itInst3 = this.getStateConditionMap().entrySet().iterator();
	    while (itInst3.hasNext()) {
	        Map.Entry<String, TStateConditionMeasure> pairs = (Map.Entry<String, TStateConditionMeasure>)itInst3.next();
	    	ppiset.getBaseMeasure().add(this.getOfPpinot().createStateConditionMeasure((TStateConditionMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDataMeasure>> itInst4 = this.getDataMap().entrySet().iterator();
	    while (itInst4.hasNext()) {
	        Map.Entry<String, TDataMeasure> pairs = (Map.Entry<String, TDataMeasure>)itInst4.next();
	    	ppiset.getBaseMeasure().add(this.getOfPpinot().createDataMeasure((TDataMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDataPropertyConditionMeasure>> itInst5 = this.getDataPropertyConditionMap().entrySet().iterator();
	    while (itInst5.hasNext()) {
	        Map.Entry<String, TDataPropertyConditionMeasure> pairs = (Map.Entry<String, TDataPropertyConditionMeasure>)itInst5.next();
	    	ppiset.getBaseMeasure().add(this.getOfPpinot().createDataPropertyConditionMeasure((TDataPropertyConditionMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAggregatedMeasure>> itInst6 = this.getAggregatedMap().entrySet().iterator();
	    while (itInst6.hasNext()) {
	        Map.Entry<String, TAggregatedMeasure> pairs = (Map.Entry<String, TAggregatedMeasure>)itInst6.next();
	    	ppiset.getAggregatedMeasure().add((TAggregatedMeasure) pairs.getValue());
	    }
	    
		Iterator<Entry<String, TDerivedSingleInstanceMeasure>> itInst7 = this.getDerivedSingleInstanceMap().entrySet().iterator();
	    while (itInst7.hasNext()) {
	        Map.Entry<String, TDerivedSingleInstanceMeasure> pairs = (Map.Entry<String, TDerivedSingleInstanceMeasure>)itInst7.next();
	    	ppiset.getDerivedMeasure().add(this.getOfPpinot().createDerivedSingleInstanceMeasure((TDerivedSingleInstanceMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TDerivedMultiInstanceMeasure>> itInst8 = this.getDerivedMultiInstanceMap().entrySet().iterator();
	    while (itInst8.hasNext()) {
	        Map.Entry<String, TDerivedMultiInstanceMeasure> pairs = (Map.Entry<String, TDerivedMultiInstanceMeasure>)itInst8.next();
	    	ppiset.getDerivedMeasure().add(this.getOfPpinot().createDerivedMultiInstanceMeasure((TDerivedMultiInstanceMeasure) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAppliesToElementConnector>> itInst9 = this.getAppliesToElementConnectorMap().entrySet().iterator();
	    while (itInst9.hasNext()) {
	        Map.Entry<String, TAppliesToElementConnector> pairs = (Map.Entry<String, TAppliesToElementConnector>)itInst9.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createAppliesToElementConnector((TAppliesToElementConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TTimeConnector>> itInst10 = this.getTimeConnectorMap().entrySet().iterator();
	    while (itInst10.hasNext()) {
	        Map.Entry<String, TTimeConnector> pairs = (Map.Entry<String, TTimeConnector>)itInst10.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createTimeConnector((TTimeConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TUses>> itInst11 = this.getUsesMap().entrySet().iterator();
	    while (itInst11.hasNext()) {
	        Map.Entry<String, TUses> pairs = (Map.Entry<String, TUses>)itInst11.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createUses((TUses) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAppliesToDataConnector>> itInst14 = this.getAppliesToDataConnectorMap().entrySet().iterator();
	    while (itInst14.hasNext()) {
	        Map.Entry<String, TAppliesToDataConnector> pairs = (Map.Entry<String, TAppliesToDataConnector>)itInst14.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createAppliesToDataConnector((TAppliesToDataConnector) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TAggregates>> itInst15 = this.getAggregatesMap().entrySet().iterator();
	    while (itInst15.hasNext()) {
	        Map.Entry<String, TAggregates> pairs = (Map.Entry<String, TAggregates>)itInst15.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createAggregates((TAggregates) pairs.getValue()));
	    }
	    
		Iterator<Entry<String, TIsGroupedBy>> itInst16 = this.getIsGroupedByMap().entrySet().iterator();
	    while (itInst16.hasNext()) {
	        Map.Entry<String, TIsGroupedBy> pairs = (Map.Entry<String, TIsGroupedBy>)itInst16.next();
	    	ppiset.getMeasureConnector().add(this.getOfPpinot().createIsGroupedBy((TIsGroupedBy) pairs.getValue()));
	    }
	    
	    for (TPpi ppi : this.getPpiList()) 
	    	ppiset.getPpi().add(ppi);
   	
    	TExtensionElements extensionElements = new TExtensionElements();
	    extensionElements.getAny().add( this.getOfPpinot().createPpiset(ppiset) );
		
	    TProcess process = this.getOfBpmn().createTProcess();
	    process.setId( procId );
	    process.setName( procId );
	    process.setExtensionElements(extensionElements);

		Iterator<Entry<String, TTask>> itInst12 = this.getTaskMap().entrySet().iterator();
	    while (itInst12.hasNext()) {
	        Map.Entry<String, TTask> pairs = (Map.Entry<String, TTask>)itInst12.next();
	        process.getFlowElement().add(this.getOfBpmn().createTask((TTask) pairs.getValue()));
	    }

		Iterator<Entry<String, TDataObject>> itInst13 = this.getDataobjectMap().entrySet().iterator();
	    while (itInst13.hasNext()) {
	        Map.Entry<String, TDataObject> pairs = (Map.Entry<String, TDataObject>)itInst13.next();
	        TDataObject value = pairs.getValue();
			process.getFlowElement().add(this.getOfBpmn().createDataObject(value));
	    }

	    TDefinitions definitions = new TDefinitions();
	    definitions.setId("ppinot-definitions");
	    definitions.setExpressionLanguage("http://www.w3.org/1999/XPath");
	    definitions.setTargetNamespace("http://schema.omg.org/spec/BPMN/2.0");
	    definitions.setTypeLanguage("http://www.w3.org/2001/XMLSchema");
    	definitions.getRootElement().add( this.getOfBpmn().createProcess(process));
    	
        this.setExportElement( this.getOfBpmn().createDefinitions(definitions) );
		
        this.contador = 0;
	}

	@Override
	protected void generateModelLists() {
		
		
		Map<String, CountInstanceMeasure> map1 = new HashMap<String, CountInstanceMeasure>();
		List<CountInstanceMeasure> modelList1 = new ArrayList<CountInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TCountMeasure) {

				CountInstanceMeasure def = obtainModel((TCountMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					map1.put( def.getId(), def );
					modelList1.add(def);
				}
			}
		}

		this.setCountInstanceMeasureMap(map1);
		this.countInstanceMeasureList = modelList1;

		Map<String, TimeInstanceMeasure> map2 = new HashMap<String, TimeInstanceMeasure>();
		List<TimeInstanceMeasure> modelList2 = new ArrayList<TimeInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TTimeMeasure) {

				TimeInstanceMeasure def = obtainModel((TTimeMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					map2.put( def.getId(), def );
					modelList2.add(def);
				}
			}
		}
		
		this.setTimeInstanceMeasureMap(map2);
		this.timeInstanceMeasureList = modelList2;
		
		Map<String, StateConditionInstanceMeasure> map3 = new HashMap<String, StateConditionInstanceMeasure>();
		List<StateConditionInstanceMeasure> modelList3 = new ArrayList<StateConditionInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TStateConditionMeasure) {

				StateConditionInstanceMeasure def = this.obtainModel((TStateConditionMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					map3.put( def.getId(), def );
					modelList3.add(def);
				}
			}
		}

		this.setStateConditionInstanceMeasureMap(map3);
		this.stateConditionInstanceMeasureList = modelList3;
		
		Map<String, DataInstanceMeasure> map4 = new HashMap<String, DataInstanceMeasure>();
		List<DataInstanceMeasure> modelList4 = new ArrayList<DataInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TDataMeasure) {

				DataInstanceMeasure def = this.obtainModel((TDataMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					map4.put( def.getId(), def );
					modelList4.add(def);
				}
			}
		}
		
		this.setDataInstanceMeasureMap(map4);
		this.dataInstanceMeasureList = modelList4;
		
		Map<String, DataPropertyConditionInstanceMeasure> map5 = new HashMap<String, DataPropertyConditionInstanceMeasure>();
		List<DataPropertyConditionInstanceMeasure> modelList5 = new ArrayList<DataPropertyConditionInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getBaseMeasure() ) {
			
			if(element.getValue() instanceof TDataPropertyConditionMeasure) {

				DataPropertyConditionInstanceMeasure def = this.obtainModel((TDataPropertyConditionMeasure) element.getValue());

				this.searchPpi(element.getValue(), def);
				
				if (def!=null) {
					map5.put( def.getId(), def );
					modelList5.add(def);
				}
			}
		}

		this.setDataPropertyConditionInstanceMeasureMap(map5);
		this.dataPropertyConditionInstanceMeasureList = modelList5;

		// las medidas agregadas
		Map<String, AggregatedMeasure> map6 = new HashMap<String, AggregatedMeasure>(); // Time
		List<AggregatedMeasure> modelList6 = new ArrayList<AggregatedMeasure>();

		Map<String, AggregatedMeasure> map7 = new HashMap<String, AggregatedMeasure>();	// Count
		List<AggregatedMeasure> modelList7 = new ArrayList<AggregatedMeasure>();

		Map<String, AggregatedMeasure> map8 = new HashMap<String, AggregatedMeasure>();		// StateCondition
		List<AggregatedMeasure> modelList8 = new ArrayList<AggregatedMeasure>();

		Map<String, AggregatedMeasure> map9 = new HashMap<String, AggregatedMeasure>();	// Data
		List<AggregatedMeasure> modelList9 = new ArrayList<AggregatedMeasure>();

		Map<String, AggregatedMeasure> map10 = new HashMap<String, AggregatedMeasure>();	// DataPropertyCondition
		List<AggregatedMeasure> modelList10 = new ArrayList<AggregatedMeasure>();
		
		for( TAggregatedMeasure measure : this.getPpiset().getAggregatedMeasure()) {
			
			TBaseMeasure baseMeasure;

			if(measure.getBaseMeasure()==null) {
				
				TAggregates connector = (TAggregates) this.findMeasureConnector(measure, TAggregates.class);
				
				if (connector!=null) {
					
					MeasureDefinition baseModel = this.findConnectedMeasure(connector, false);

					AggregatedMeasure def = this.obtainModel(measure, (BaseMeasure) baseModel);
					
					if (def!=null) {
						
						def.setAggregates(true);
						this.searchPpi(measure, def);
						
						if(baseModel instanceof TimeInstanceMeasure ) {
							
							map6.put( def.getId(), def );
							modelList6.add(def);
						} else
						if(baseModel instanceof CountInstanceMeasure ) {
							
							map7.put( def.getId(), def );
							modelList7.add(def);
						} else
						if(baseModel instanceof StateConditionInstanceMeasure ) {
							
							map8.put( def.getId(), def );
							modelList8.add(def);
						} else
						if(baseModel instanceof DataInstanceMeasure ) {
							
							map9.put( def.getId(), def );
							modelList9.add(def);
						} else
						if(baseModel instanceof DataPropertyConditionInstanceMeasure ) {
							
							map10.put( def.getId(), def );
							modelList10.add(def);
						}
					}
				}
			} else {
				
				baseMeasure = measure.getBaseMeasure().getValue();
			
				if(baseMeasure instanceof TTimeMeasure ) {
	
					AggregatedMeasure def = this.obtainModel(measure, (TTimeMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						map6.put( def.getId(), def );
						modelList6.add(def);
					}
				} else
				if(baseMeasure instanceof TCountMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TCountMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						map7.put( def.getId(), def );
						modelList7.add(def);
					}
				} else
				if(baseMeasure instanceof TStateConditionMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TStateConditionMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						map8.put( def.getId(), def );
						modelList8.add(def);
					}
				} else
				if(baseMeasure instanceof TDataMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TDataMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						map9.put( def.getId(), def );
						modelList9.add(def);
					}
				} else
				if(baseMeasure instanceof TDataPropertyConditionMeasure ) {
					
					AggregatedMeasure def = this.obtainModel(measure, (TDataPropertyConditionMeasure) baseMeasure);
	
					this.searchPpi(measure, def);
					
					if (def!=null) {
						map10.put( def.getId(), def );
						modelList10.add(def);
					}
				}
			}
		}

		this.setTimeAggregatedMeasureMap(map6);		// Time
		this.timeAggregatedMeasureList = modelList6;

		this.setCountAggregatedMeasureMap(map7);	// Count
		this.countAggregatedMeasureList = modelList7;

		this.setStateConditionAggregatedMeasureMap(map8);	// StateCondition
		this.stateConditionAggregatedMeasureList = modelList8;

		this.setDataAggregatedMeasureMap(map9);		// Data
		this.dataAggregatedMeasureList = modelList9;

		this.setDataPropertyConditionAggregatedMeasureMap(map10);	// Data Property Condition
		this.dataPropertyConditionAggregatedMeasureList = modelList10;
		
		// Medidas derivadas
		
		List<DerivedSingleInstanceMeasure> modelList11 = new ArrayList<DerivedSingleInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {
				
			if(element.getValue() instanceof TDerivedSingleInstanceMeasure) {
				
				TDerivedSingleInstanceMeasure measure = (TDerivedSingleInstanceMeasure) element.getValue();
				
				DerivedSingleInstanceMeasure def = this.obtainModel(measure);

				this.searchPpi(measure, def);
				
				if (def!=null) {
					this.getDerivedSingleInstanceMeasureMap().put( def.getId(), def );
					modelList11.add(def);
				}
			}
		}

		this.derivedSingleInstanceMeasureList = modelList11;
		
		List<DerivedMultiInstanceMeasure> modelList12 = new ArrayList<DerivedMultiInstanceMeasure>();
		for( JAXBElement<?> element : this.getPpiset().getDerivedMeasure()) {
			
			if(element.getValue() instanceof TDerivedMultiInstanceMeasure) {
				
				TDerivedMultiInstanceMeasure measure = (TDerivedMultiInstanceMeasure) element.getValue();
				
				DerivedMultiInstanceMeasure def = this.obtainModel(measure);

				this.searchPpi(measure, def);
				
				if (def!=null) {
					this.getDerivedMultiInstanceMeasureMap().put( def.getId(), def );
					modelList12.add(def);
				}
			}
		}
		
		this.derivedMultiInstanceMeasureList = modelList12;
		
	}

}
