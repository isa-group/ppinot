var ppis = [ {
	"goals" : "This is a goal",
	"scope" : {
		"period" : "",
		"startDate" : null,
		"endDate" : null,
		"inStart" : false,
		"inEnd" : false,
		"year" : ""
	},
	"comments" : "",
	"valueString" : [],
	"responsible" : "",
	"measuredBy" : {
		"kind" : "CountInstanceMeasure",
		"when" : {
			"kind" : "TimeInstantCondition",
			"changesToState" : {
				"stateString" : "Start"
			},
			"appliesTo" : "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"
		},
		"scale" : "",
		"unitOfMeasure" : "",
		"name" : "Count1",
		"id" : "sid-8F97D7AD-B211-429E-911B-F278B5D12D98",
		"description" : ""
	},
	"normalized" : [],
	"informed" : "",
	"success" : [],
	"name" : "PPI 1",
	"id" : "sid-eaf8645a-d548-4e9f-8a39-bf3aa78504fd",
	"target" : {
		"refMin" : null,
		"refMax" : 10.0
	},
	"description" : "Descripcion PPI 1"
}];

$.ajax({
	type: "POST",
	url: "/api/repository/processes/ppi/ppis",
	data: $.toJSON(ppis),
	success: function() {
		console.log("OK");
	},
	contentType : "application/json"
	});

