var idCounter;

function saveMeasuredBy(elem) {
	var value = elem.data("value");
    var kindSave = [saveTimeInstanceMeasure, saveCountInstanceMeasure, saveStateconditionintanceMeasure, saveDataPropertyCondition, saveDataInstanceMeasure, saveAggregatedInstanceMeasure, saveDerivedInstanceMeasure];
    console.log(load);

	return kindSave[value.value](value.contained);
    // Completarlo para el resto de measures
}

function saveTimeInstanceMeasure(elem) {

	var measure = {
		kind: "TimeInstanceMeasure",
		timeMeasureType: "LINEAR",
		singleInstanceAggFunction: "",
		name: "Time1",
		description: "",
		scale: "",
		unitOfMeasure: "",
		id: "time-instance-"+idCounter;
	    baseMeasure: saveBaseMeasure(elem);
		
	};
	idCounter++;

	return measure;
}

function saveBaseMeasure(elem){
	var result={
			from: saveEvent(elem.startEvent),
			to: saveEvent(elem.endEvent)	
	}
	return result;
}

function saveEvent(event) {
	var kindEvent = [saveActivityEvent, saveProcessEvent, saveDataObjectEvent];

	var result = kindEvent[event.value](event.contained);
	result.kind = "TimeInstantCondition";

	return result;
}

function saveActivityEvent(event) {
	var result = {
		changesToState: {stateString: event.activityState},
		appliesTo: activityNameId[event]
	}

	return result;
}

function saveProcessEvent(event){
	var result = {
			changesToState: {stateString: event.processState},
			appliesTo: activityNameId[event.activityName]
		}

		return result;
}

function saveDataObjectEvent(event){
	var result = {
			changesToState: {stateString: event.DataObjectState},
			appliesTo: activityNameId[event.DataObjectName]
		}

		return result;
}


function saveCountInstanceMeasure(elem){
	var measure = {
			kind: "CountInstanceMeasure",
			name: "Count1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "count-instance-"+idCounter;
			when: saveEvent(elem.startEvent),
			
		};
		idCounter++;

		return measure;
}
function saveStateconditionintanceMeasure(elem){
	var measure = {
			kind: "StateConditionInstanceMeasure",
			name: "SCM1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "StateCondition-instance-"+idCounter;
			when: saveEvent(elem.startEvent),
			
		};
		idCounter++;

		return measure;
}
function saveDataPropertyCondition(elem){
	var measure = {
			kind: "StateConditionInstanceMeasure",
			name: "DataProperty1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "DataPropertyCondition-instance-"+idCounter;
			when: saveEvent(elem.startEvent),
			
		};
		idCounter++;

		return measure;
}
function saveDataInstanceMeasure(elem){
	var measure = {
			kind: "DataInstanceMeasure",
			name: "DataMeasure1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "Data-instance-"+idCounter;
			condition: saveCondition(elem.condition);
	        dataContentSelection: save DataContentSelection(elem.dataContentSelection)
			
		};
		idCounter++;

		return measure;
}

function saveCondition(elem){
	var result={
			kind:"DataPropertyCondition",
			stateConsidered:{stateString:""}
	        dataobject:"Dataobject1",
	        restriction:"",
	        appliesTo:"conditionData-idCounter++"
	}
	idCounter++;
	return result;
}

function saveDataContentSelection(elem){
	var result={
			dataobject:"Dataobject1",
			selection:"",
			dataobjectId:"conditionData-idCounter--",
			dataContentSelection:{,}
	}
	idCounter++
}


	
	


function saveAggregatedInstanceMeasure(elem){
	var measure = {
			kind: "AggregatedInstanceMeasure",
			timeMeasureType: "LINEAR",
			singleInstanceAggFunction: "Minimum",
			name: "TimeAgg1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "AggregatedMeasure-instance-"+idCounter;
	        baseMeasure: saveBaseMeasure(elem)
		};
		idCounter++;

		return measure;
}


function saveDerivedInstanceMeasure(elem){
	
	var measure = {
			kind: "DerivedInstanceMeasure",
			name: "Derived1",
			description: "",
			scale: "",
			unitOfMeasure: "",
			id: "Derived-instance-"+idCounter;
			when: saveEvent(elem.startEvent),
			
		};
		idCounter++;

		return measure;
}


function saveTarget(elem){
	var value = elem.data("value");
    var kindSave = [saveLess, saveGreater, saveLessEqual, saveGreaterEqual];
    console.log(load);

	return kindSave[value.value](value.contained);
    // Completarlo para el resto de measures
}

function saveLess(elem){
	var target={
			refMin:	
			refMax:,
		    type:"es.us.isa.ppinot.model.Target"
   }
	return target;
}
function saveGreater(elem){
var result={
		refMin:	
		refMax:10.0,
		type:"es.us.isa.ppinot.model.Target"
	}
	return result;
}
function saveLessEqual(elem){
var result={
		refMin:	
		refMax:,
		type:"es.us.isa.ppinot.model.Target"	
	}
	return result;
}
function saveGreaterEqual(elem){
var result={
		refMin:	
		refMax: 10.0,
		type:"es.us.isa.ppinot.model.Target"
	}
	return result;
}




