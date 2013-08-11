function loadAll(processName) {
	var process = new Process("ppi");
	process.load().then(function() {
		var measures = createMeasuresModel(process);
		console.log(measures);
		loadTemplates(processName, process, measures);
	});
}


function loadTemplates(processName, process, measuresModel) {
	$.ajax({
		type: "GET",
		url: "/api/repository/processes/" + processName + "/ppis",
		dataType: "json",
		success: function(data) {
			$(data).each(function (index) {
				var temp = new Template(this, process, measuresModel);
				var ppi = $("<div></div>");
				ppi.appendTo($("#container"));
				temp.load(ppi);
			});
		}
	});
}

function Template(ppi, process, measuresModel) {
	this.ppi = ppi;
	this.process = process;
	this.measuresModel = measuresModel;
}

jQuery.extend(Template.prototype, {
	load: function (elem) {
		var measuredByElem = $("<div></div>");
		var targetElem = $("<div></div>");

		measuredByElem.appendTo(elem);
		targetElem.appendTo(elem);

		this.loadMeasuredBy(measuredByElem, this.ppi.measuredBy);
		// loadTarget(targetElem, ppi.target);
	},

	findPosition: function(ar, name) {
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
	},

	loadMeasuredBy: function(elem, measuredBy) {
		var load = this.loadMeasure(measuredBy);	
		elem.linguisticPattern("The PPI is defined as", this.measuresModel, load);
	},

	loadMeasure: function(measuredBy){
	    var kindIndex = {
	    	TimeInstanceMeasure: 0,
			CountInstanceMeasure: 1,
			StateConditionMeasure: 2,
			DataPropertyCondition: 3,
			DataMeasure: 4,
			AggregatedMeasure: 5,
			DerivedMeasure: 6
	    };

	    // var kindContained = {
	    // 	TimeInstanceMeasure: this.containedTimeInstanceMeasure,
	    // 	CountInstanceMeasure: this.containedCountInstanceMeasure,
	    // 	StateConditionMeasure: this.containedStateConditionInstanceMeasure,
	    // 	DataPropertyCondition: this.containedDataPropertyCondition,
	    // 	DataMeasure: this.containedDataInstanceMeasure,
	    // 	AggregatedMeasure: this.containedAggregatedMeasure,
	    // 	DerivedMeasure: this.containedDerivedInstanceMeasure
	    // };

	    var load = {
	    	value: kindIndex[measuredBy.kind],
	    	contained: this[measuredBy.kind](measuredBy)
	    };

	    console.log(load);

	    return load;
	},

	TimeInstanceMeasure: function(measuredBy) {
		var contained = {
			startEvent: this.containedEvent(measuredBy.from),
			endEvent: this.containedEvent(measuredBy.to)
		};

		return contained;
	},

	CountInstanceMeasure: function(measuredBy) {
		var contained = {
			event: this.containedEvent(measuredBy.when)
		};
		return contained;
	},

	containedEvent: function(event){
		var contained = {};

		if (this.process.isActivity(event.appliesTo)) {
			contained.value = 0;
			contained.contained = {};
			contained.contained.activityNames = {
				value: this.containedActivityNames(event)
			};		
			contained.contained.activityState = {
				value: this.findPosition(this.process.activityStates, event.changesToState.stateString)
			};
		}

		// Completarlo salvo para el tipo "process"

		console.log(contained);

		return contained;
			
	},

	containedActivityType: function(event){
		var contained = {
				value: this.findPosition(this.process.activityNames, this.process.activityIdName[event.id])
		};
		console.log(contained);

		return contained;
	},

	containedActivityNames: function(event){
		var contained = {
				value: this.findPosition(this.process.activityNames, this.process.activityIdName[event.appliesTo])
		};
		console.log(contained);

		return contained;
	},

	containedActivityState: function(event){
		var contained = {
				value: this.findPosition(this.process.activityStates, event.changesToState.stateString)
		};
		console.log(contained);

		return contained;
	},

	StateConditionMeasure: function(measuredBy){
	  var contained={
			  activityType: this.containedActivityType(measuredBy.Id),
			  activityName: this.containedActivityNames(measuredBy.appliesTo),
			  activityState: this.containedActivityState(measuredBy.changesToState.stateString),
			  state:  this.containedState(measuredBy)
	  }
	 
	  console.log(contained);
	     return contained;
	},

	containedState: function(event){
		  var contained={
				  activityType: this.containedActivityType(event),
				  activityName: this.containedActivityNames(event),
				  activityState: this.containedActivityState(event)
			}
		 console.log(contained);
		     return contained;
	},

	DataPropertyCondition: function(measuredBy){
		var contained={
				DataObjectName: this.containedDataObjectName(measuredBy.groupBy.dataObject),
				ConditionDataObjectProperties: this.containedConditionDataObjectProperty(measuredBy.groupBy.dataObjectId)
				
		}
		console.log(contained);
	    return contained;
	},

	containedDataObjectName: function(measuredBy){
		var contained={
				value: this.findPosition(measuredBy, this.dataObjectNames[measuredBy.groupBy.dataObject])
		}
		console.log(contained);
	    return contained;
	},

	ConditionDataObjectPropertie: function(measuredBy){
		var contained={
				value: this.findPosition(measuredBy, this.dataObjectNames[measuredBy.groupBy.dataObjectId])
		}
		console.log(contained);
	    return contained;
	},

	DataMeasure: function(measuredBy){
	  var contained={
			  DataObjectPropertyName: this.containedDataObjectPropertyName(measuredBy.groupBy.dataObject),
			  DataObjectName: this.containedDataObjectName(measuredBy.groupBy.dataObject)  
	  }
	  
	  console.log(contained);
	  return contained;
		
	},



	AggregatedMeasure: function(measuredBy) {
		var contained={
				Aggregate: measuredBy.aggregationFunction,
				MeasureForAgg: this.loadMeasure(measuredBy.baseMeasure),
				// DataObjectPropertyName: this.containedDataObjectPropertyName(measuredBy.groupBy.dataObject),
				// DataObjectName: this.containedDataObjectName(measuredBy.groupBy.dataObject)
		}
		console.log(contained);
	    return contained;
	},

	containedDataObjectPropertyName: function(measuredBy){
		var contained={
				value: this.findPosition(measuredBy, this.process.dataObjectNames[measuredBy.groupBy.dataObject])
		}
		console.log(contained);
	    return contained;
	},

	DerivedInstanceMeasure: function(measuredBy){
		var contained={
				expresion: "",
				parametros:	"",
				MeasureForDer: this.loadMeasure(measuredBy)
				
		}
		console.log(contained);
	    return contained;
	},	


});




	

function loadTarget(elem, target){
	var kindIndex = {
			minRef: 0,
			maxRef: 1,
			minequalRef: 2,
			maxequalRef: 3,
			
	    };

	    var kindContained = {
	    		minRef: containedMinRef,
	    		maxRef: containedMaxRef,
	    		minequalRef: containedMinEqualRef,
	    		maxequalRef: containedMaxEqualRef
	    };

	    var loadTarget = {
	    	value: kindIndex[target.kind],
	    	contained: kindContained[target.kind](target)
	    };
	    console.log(loadTarget);
		elem.linguisticPattern("The PPI value must", options, loadTarget);
}

function containedMinRef(){
	var contained={
		value:0	
	}
	console.log(contained);
    return contained;
}

function containedMaxRef(){
var contained={
		value:1	
	}
	console.log(contained);
    return contained;
}

function containedMinEqualRef(){
var contained={
		value:2
	}
	console.log(contained);
    return contained;
}

function containedMaxEqualRef(){
     var contained={
    		 value:3	
	}
	console.log(contained);
    return contained;
}



	
