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
		from: saveEvent(elem.startEvent),
		to: saveEvent(elem.endEvent)
	};
	idCounter++;

	return measure;
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
function saveDataInstanceMeasure(elem){}
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

{"kind":"DataInstanceMeasure",
"condition":{"kind":"DataPropertyCondition","dataobject":"Dataobject1","stateConsidered":{"stateString":""},"restriction":"","appliesTo":"sid-9832DD51-05CF-4B45-BB61-C46740621C4F"},
"dataContentSelection":{"dataobject":"Dataobject1","selection":"","dataobjectId":"sid-9832DD51-05CF-4B45-BB61-C46740621C4F"},
"name":"DataMeasure1",
"id":"sid-98764615-A771-4C7D-8619-16008DAE4679",
"description":"",
"scale":"",
"unitOfMeasure":""}

