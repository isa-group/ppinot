var activityNames = [];
var activityNameId = {};
var activityIdName = {};
var activityStates = ["Start", "Cancel","End"];


var DataObjectNames= [];
var DataObjectState= [];
var eventNames = [];

function isActivity(id) {
	var result = false;

	if (typeof activityIdName[id] != "undefined")
		result = true;

	return result;
}

function findPosition(ar, name) {
	var pos = -1;
	var j = 0;
	while (j < ar.length) {
		if (ar[j] == name) {
			pos = j;
			break;
		}
		j++;
	}

	return pos;
}


function loadAll(processName) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/activities",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				if (this.name == "") {
					this.name = this.id;
				}
				activityNames.push(this.name);
				activityIdName[this.id] = this.name;
				activityNameId[this.name] = this.id;				
			});
			loadEvents(processName);
		}
	});
}

function loadEvents(processName) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/events",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				console.log(this);
			});
			loadDataObjects(processName);
		}
	});
}

function loadDataObjects(processName) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/dataobjects",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				console.log(this);
			});
			loadGateways(processName);
		}
	});
}

function loadGateways(processName) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/gateways",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				console.log(this);
			});
			loadTemplates(processName);
		}
	});
}


function loadTemplates(processName) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/ppis",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				console.log(this);
				loadTemplate(this);
			});
		}
	});
}



function loadTemplate(ppi) {
	// carga plantilla
	var elem;
	loadMeasuredBy(elem, ppi.measuredBy);
}

function loadMeasuredBy(elem, measuredBy) {
    var kindIndex = {
		CountInstanceMeasure: 1,
		AggregatedMeasure: 5    	
    };

    var kindContained = {
    	TimeInstanceMeasure: containedTimeInstanceMeasure,
    	CountInstanceMeasure: containedCountInstanceMeasure,
    	AggregatedMeasure: containedAggregatedMeasure
    };

    var load = {
    	value: kindIndex[measuredBy.kind],
    	contained: kindContained[measuredBy.kind](measuredBy)
    };

    // Completarlo para el resto de measures

    console.log(load);

	//elem.linguisticPattern("The PPI is defined as", measureOptions, load);
}

function containedTimeInstanceMeasure(measuredBy) {
	var contained = {
		startEvent: containedEvent(measuredBy.from),
		endEvent: containedEvent(measuredBy.to)
	};

	return contained;
}

function containedCountInstanceMeasure(measuredBy) {
	var contained = {
		event: containedEvent(measuredBy.when)
	};
	return contained;
}

function containedEvent(event) {
	var contained = {};

	if (isActivity(event.appliesTo)) {
		contained.value = 0;
		contained.activityNames = {
			value: findPosition(activityNames, activityIdName[event.appliesTo])
		};		
		contained.activityState = {
			value: findPosition(activityStates, event.changesToState.stateString)
		};
	}

	// Completarlo salvo para el tipo "process"

	console.log(contained);

	return contained;
		
}

function containedAggregatedMeasure(measuredBy) {
	return {};
}