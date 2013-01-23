package es.us.isa.ppinot.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import es.us.isa.bpmn.xmlClasses.bpmn20.TBaseElement;
import es.us.isa.bpmn.xmlClasses.bpmn20.TDataObject;
import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.MeasureDefinition;
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
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.model.state.RuntimeState;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregatedMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToDataConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToElementConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TCountMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataPropertyConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedMultiInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TIsGroupedBy;
import es.us.isa.ppinot.xmlClasses.ppinot.TMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TMeasureConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TPpiset;
import es.us.isa.ppinot.xmlClasses.ppinot.TStateConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TUses;

public class GeneratePpiNotModel {
	
	TimeInstanceMeasure obtainModel(TTimeMeasure measure, TPpiset ppiset) {
		
		// crea la definición de la medida TimeMeasure a partir de la información en el xml
		// las actividades a la cuales se aplica la medida se obtiene de los conectores
		Map<String, TTimeConnector> map = findTimeConnectors(measure, ppiset);
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
	
	CountInstanceMeasure obtainModel(TCountMeasure measure, TPpiset ppiset) {
		
		// crea la definición de la medida CountMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class, ppiset);
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
	StateConditionInstanceMeasure obtainModel(TStateConditionMeasure measure, TPpiset ppiset) {
		
		// crea la definición de la medida StateConditionMeasure a partir de la información en el xml
		// la tarea a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class, ppiset);
		
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
	
	DataInstanceMeasure obtainModel(TDataMeasure measure, TPpiset ppiset) {

		// crea la definición de la medida DataMeasure a partir de la información en el xml
		// el dataobject al cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class, ppiset);
		
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
	
	DataPropertyConditionInstanceMeasure obtainModel(TDataPropertyConditionMeasure measure, TPpiset ppiset) {
		
		// crea la definición de la medida DataPropertyConditionMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class, ppiset);
		
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
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, MeasureDefinition baseModel, TPpiset ppiset) {
		
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

		TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
		if (con!=null) {
			def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
					((TDataObject) con.getTargetRef()).getName()) );
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TTimeMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada TimeMeasure a partir de la información en el xml
		// las actividades a laa cuales se aplica la medida se obtienen de los conectores de la medida agregada
		AggregatedMeasure def = null;
		
		Map<String, TTimeConnector> mapCon = findTimeConnectors(measure, ppiset);
		TTimeConnector conFrom = mapCon.get("From");
		TTimeConnector conTo = mapCon.get("To");
		
		if (mapCon.size()==2) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// las actividades a las cuales se aplica la medida se obtiene de los conectores de la medida agregada
			TimeInstanceMeasure baseModel = this.obtainModel(baseMeasure, ppiset);
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

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TCountMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada CountMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class, ppiset);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
			CountInstanceMeasure baseModel = this.obtainModel(baseMeasure, ppiset);
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

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TStateConditionMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada StateConditionMeasure a partir de la información en el xml
		// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToElementConnector.class, ppiset);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// la actividad a la cual se aplica la medida se obtiene del conector de la medida agregada
			StateConditionInstanceMeasure baseModel = this.obtainModel(baseMeasure, ppiset);
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

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDataMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada DataMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class, ppiset);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
			DataInstanceMeasure baseModel = this.obtainModel(baseMeasure, ppiset);
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

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDataPropertyConditionMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada DataPropertyConditionMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		
		// crea la medida agregada. La información del período de análisis se obtiene de la propiedad analysisPeriod
		TMeasureConnector connector = findMeasureConnector(measure, TAppliesToDataConnector.class, ppiset);
		
		if (connector!=null) {

			// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
			// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
			DataPropertyConditionInstanceMeasure baseModel = this.obtainModel(baseMeasure, ppiset);
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

			TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
			if (con!=null) {
				def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
						((TDataObject) con.getTargetRef()).getName()) );
			}
		}
		
		return def;
	}
	
	AggregatedMeasure obtainModel( TAggregatedMeasure measure, TDerivedSingleInstanceMeasure baseMeasure, TPpiset ppiset) {
		
		// crea la definición de una medida agregada DataPropertyConditionMeasure a partir de la información en el xml
		// el dataobject a la cual se aplica la medida se obtiene del conector de la medida agregada
		AggregatedMeasure def = null;
		

		// crea la definición de la medida de instancia que tiene la información para calcular la medida agregada
		// el dataobject al cual se aplica la medida se obtiene del conector de la medida agregada
		DerivedSingleInstanceMeasure baseModel = this.obtainModel(baseMeasure);
		
		def = new AggregatedMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getAggregationfunction(), 
				measure.getSamplingfrequency(),
				baseModel);

		TMeasureConnector con = findMeasureConnector(measure, TIsGroupedBy.class, ppiset);
		if (con!=null) {
			def.setGroupedBy( new DataContentSelection(((TIsGroupedBy) con).getDataContentSelection(), 
					((TDataObject) con.getTargetRef()).getName()) );
		}
		
		return def;
	}
	
	DerivedSingleInstanceMeasure obtainModel(TDerivedSingleInstanceMeasure measure) {
		
		// crea la definición de la medida DerivedSingleInstanceMeasure a partir de la información en el xml
		// la medida a la cual se aplica la medida se obtiene del conector
		DerivedSingleInstanceMeasure def = new DerivedSingleInstanceMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getFunction() );

		return def;
	}
	
	DerivedMultiInstanceMeasure obtainModel(TDerivedMultiInstanceMeasure measure) {
		
		// crea la definición de la medida DerivedMultiInstanceMeasure a partir de la información en el xml
		// la medida a la cual se aplica la medida se obtiene del conector
		DerivedMultiInstanceMeasure def = new DerivedMultiInstanceMeasure( 
				measure.getId(), 
				measure.getName(), 
				measure.getDescription(),
				measure.getScale(),
				measure.getUnitofmeasure(),
				measure.getFunction() );

		return def;
	}
	
	List<TUses> findUses(TMeasure measure, TPpiset ppiset) {
		
		List<TUses> connectorList = new ArrayList<TUses>();
		for( JAXBElement<?> element : ppiset.getMeasureConnector() ) {
			
			if(element.getValue() instanceof TUses) {

				TUses con = (TUses) element.getValue();
				if (con.getSourceRef()!=null && con.getSourceRef().equals(measure)) {
					
					connectorList.add(con);
				}
			}
		}
		
		return connectorList;
	}

	Map<String, TTimeConnector> findTimeConnectors(TMeasure measure, TPpiset ppiset) {
		
		Map<String, TTimeConnector> map = new HashMap<String, TTimeConnector>();
		for( JAXBElement<?> element : ppiset.getMeasureConnector() ) {
			
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

	@SuppressWarnings("rawtypes")
	TMeasureConnector findMeasureConnector(TMeasure measure, Class cl, TPpiset ppiset) {
		
		TMeasureConnector connector = null;
		for( JAXBElement<?> element : ppiset.getMeasureConnector() ) {
			
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

}
