function createMeasuresModel(process) {
	var aggregates= ["sum", "max", "min", "average"];

    var eventOptions = [
		{
			text: "activity {activityName} {activityState}",
			activityName: {
				options: process.activityNames
			},
			activityState: {
				prefix: " becomes",
				options: process.activityStates
			}
		},
		{
			text: "process {processName} {processState}",
			processName: {
				options: process.processNames
			},
			processState: {
				prefix: " becomes",
				options: process.processStates
			}
		},
		{
			text: "data object {dataObjectName} {dataObjectState}",
			dataObjectName: {
				options: process.dataObjectNames
			},
			dataObjectState: {
				prefix: " becomes",
				options: process.dataObjectStates
			}
		},
		{
			text: "event {eventName}",
			eventName: {
				options: process.eventNames,
				sufix: "is triggred"
			}
			
		}		
	];
    
    //TODO: No support for cyclic time measures
    //TODO: Only activities are considered in Condition Measures
    var baseMeasures= [
    	{
			text: "the duration between the time instants {startEvent} {endEvent}",
			startEvent: {
				prefix: "when",
				options: eventOptions
			},
			endEvent: {
				prefix: " and when",
				options: eventOptions
			}
		},
		{
			text: "the number of times {event}",
			event: {
				options: eventOptions
			}
		},
		{
			text: "activity {activityName} {activityState}",
			activityName:{
				options: process.activityNames,
			},
			activityState:{
				prefix: " is currently",
				options: process.activityStates
			}
		},
		
		{
			text: "the value {DataObjectPropertyName} {DataObjectName}",
			DataObjectPropertyName:{
				prefix: "of",
				options: process.dataObjectPropertyNames
			},
			DataObjectName:{
				prefix: "of",
				options: process.dataObjectNames
			},
		}
	];

	var measureForAgg = baseMeasures.concat([]);
	// TODO: No support for group by
	var aggregatedMeasure = {
		text: "the {Aggregate} {MeasureForAgg}",
		Aggregate:{
			options: aggregates
		},
		MeasureForAgg:{
			prefix: "of",
			options: measureForAgg
		},
	};

	var measureForDer = baseMeasures.concat([]);
	//TODO: Supports just one param
	var derivedMeasure = {
		text: "the {expresion} {params} {MeasureForDer}",
		expresion:{
			prefix: "function"
		},
		params:{
			prefix: "where"
		},
		MeasureForDer:{
			prefix: "is",
			options: measureForDer
		}			
	};

	measureForAgg.push(derivedMeasure);
	measureForDer.push(aggregatedMeasure);

	var measureOptions = baseMeasures.concat([derivedMeasure, aggregatedMeasure]);

	return measureOptions;
}