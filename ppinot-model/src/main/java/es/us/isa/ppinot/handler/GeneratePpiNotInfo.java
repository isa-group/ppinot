package es.us.isa.ppinot.handler;

import java.util.HashMap;
import java.util.Map;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.condition.DataPropertyCondition;
import es.us.isa.ppinot.model.condition.StateCondition;
import es.us.isa.ppinot.model.derived.DerivedMultiInstanceMeasure;
import es.us.isa.ppinot.model.derived.DerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.ObjectFactory;
import es.us.isa.ppinot.xmlClasses.ppinot.TAggregatedMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToDataConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TAppliesToElementConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TCountMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDataPropertyConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedMultiInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TDerivedSingleInstanceMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TIsGroupedBy;
import es.us.isa.ppinot.xmlClasses.ppinot.TStateConditionMeasure;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeConnector;
import es.us.isa.ppinot.xmlClasses.ppinot.TTimeMeasure;

public class GeneratePpiNotInfo {
	
	private Integer contador = 0;
	
	Map<String,Object> obtainInfo(TimeInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TTimeMeasure measure = factory.createTTimeMeasure();
		
		measure.setId(generarId("timeMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TTimeConnector conFrom = factory.createTTimeConnector();
		
		conFrom.setId(generarId("timeConnector", ""));
		conFrom.setSourceRef(measure);
		conFrom.setTargetRef(def.getFrom().getAppliesTo());
		conFrom.setWhen((def.getFrom().getChangesToState().getState()==GenericState.END)?"End":"Start");
		conFrom.setConditiontype("From");

		TTimeConnector conTo = factory.createTTimeConnector();
		
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
	
	Map<String,Object> obtainInfo(	CountInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TCountMeasure measure = factory.createTCountMeasure();
		
		measure.setId(generarId("countMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = factory.createTAppliesToElementConnector();
		
		con.setId(generarId("appliesToElementConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(def.getWhen().getAppliesTo());
		con.setWhen((def.getWhen().getChangesToState().getState()==GenericState.END)?"End":"Start");
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	
	Map<String,Object> obtainInfo(StateConditionInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TStateConditionMeasure measure = factory.createTStateConditionMeasure();
		
		measure.setId(generarId("stateConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToElementConnector con = factory.createTAppliesToElementConnector();
		
		con.setId(generarId("appliesToElementConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(((StateCondition) def.getCondition()).getAppliesTo());
		con.setState((((StateCondition) def.getCondition()).getState().getState()==GenericState.END)?"End":"Start");
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	
	Map<String,Object> obtainInfo(DataInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TDataMeasure measure = factory.createTDataMeasure();
		
		measure.setId(generarId("dataMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = factory.createTAppliesToDataConnector();
		
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
	
	Map<String,Object> obtainInfo(DataPropertyConditionInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TDataPropertyConditionMeasure measure = factory.createTDataPropertyConditionMeasure();
		
		measure.setId(generarId("dataPropertyConditionMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		TAppliesToDataConnector con = factory.createTAppliesToDataConnector();
		
		con.setId(generarId("appliesToDataConnector", ""));
		con.setSourceRef(measure);
		con.setTargetRef(((DataPropertyCondition) def.getCondition()).getAppliesTo());
		con.setRestriction(((DataPropertyCondition) def.getCondition()).getRestriction());
		con.setState(((DataPropertyCondition) def.getCondition()).getStateConsidered().getStateString());
		
		map.put("connector", con);
		map.put("measure", measure);
		
		return map;
	}
	
	Map<String,Object> obtainInfo(AggregatedMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TAggregatedMeasure measure = factory.createTAggregatedMeasure();

		measure.setId(generarId("aggregatedMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());
		measure.setSamplingfrequency(def.getSamplingFrequency());
		
		measure.setAggregationfunction(def.getAggregationFunction());

		map.put("measure", measure);
		
		if (!def.getGroupedBy().getSelection().contentEquals("") || !def.getGroupedBy().getDataobject().contentEquals("")) {
			
			TIsGroupedBy con = factory.createTIsGroupedBy();
			
			con.setId(generarId("isGroupedBy", ""));
			con.setSourceRef(measure);
			con.setTargetRef(def.getGroupedBy().getDataobject());
			
			map.put("connectorIsGroupedBy", con);
		}
		
		return map;
	}
	
	Map<String,Object> obtainInfo( DerivedSingleInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TDerivedSingleInstanceMeasure measure = factory.createTDerivedSingleInstanceMeasure();
		
		measure.setId(generarId("derivedSingleInstanceMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		measure.setFunction(def.getFunction());
	    
		map.put("measure", measure);
		
		return map;
	}
	
	Map<String,Object> obtainInfo(	DerivedMultiInstanceMeasure def, ObjectFactory factory) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		TDerivedMultiInstanceMeasure measure = factory.createTDerivedMultiInstanceMeasure();
		
		measure.setId(generarId("derivedMultiInstanceMeasure", def.getId()));
		measure.setName(def.getName());
		measure.setDescription(def.getDescription());
		measure.setScale(def.getScale());
		measure.setUnitofmeasure(def.getUnitOfMeasure());

		measure.setFunction(def.getFunction());

		map.put("measure", measure);
		
		return map;
	}

	String generarId(String prefix, String id) {
		
		this.contador++;
		return (id=="")?prefix+"_"+this.contador:id;
	}

}
