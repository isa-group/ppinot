var idCounter;

function saveMeasuredBy(elem) {
	var value = elem.data("value");

	var kindSave = [saveTimeInstanceMeasure, saveAggregatedMeasure];
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
	var kindEvent = [saveActivityEvent, saveProcessEvent];

	var result = kindEvent[event.value](event.contained);
	result.kind = "TimeInstantCondition";

	return result;
}

function saveActivityEvent(event) {
	var result = {
		changesToState: {stateString: event.activityState},
		appliesTo: activityNameId[event.activityName]
	}

	return result;
}

{"kind":"DataInstanceMeasure",
"condition":{"kind":"DataPropertyCondition","dataobject":"Dataobject1","stateConsidered":{"stateString":""},"restriction":"","appliesTo":"sid-9832DD51-05CF-4B45-BB61-C46740621C4F"},
"dataContentSelection":{"dataobject":"Dataobject1","selection":"","dataobjectId":"sid-9832DD51-05CF-4B45-BB61-C46740621C4F"},
"name":"DataMeasure1",
"id":"sid-98764615-A771-4C7D-8619-16008DAE4679",
"description":"",
"scale":"",
"unitOfMeasure":""}

