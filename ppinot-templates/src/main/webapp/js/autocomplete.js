		  
		   	$(document).ready(function() {
    			var options = [{
						text: "be less than",
						appendContent: function(container, goBackFunction, load, notifyChange) {
							$("<span>"+this.text+" </span>").click(goBackFunction).appendTo(container);
							var input = $("<input type='text' placeholder='value'>").appendTo(container).change(
									function() {										
										notifyChange({minRef: $(this).val()});
									}
								);
							try {
								input.val(load.minRef);
								notifyChange({minRef: load.minRef});
							} catch(err) {};
						}
					},
					{
    					text: "be greater than",
    					appendContent: function(container, goBackFunction, load, notifyChange) {
							$("<span>"+this.text+" </span>").click(goBackFunction).appendTo(container);
							var input = $("<input type='text' placeholder='value'>").appendTo(container).change(
									function() {										
										notifyChange({maxRef: $(this).val()});
									}
								);
							try {
								input.val(load.maxRef);
								notifyChange({maxRef: load.maxRef});
							} catch(err) {};
						}
    				},
    				{
    					text: "be less than or equal",
						appendContent: function(container, goBackFunction, load, notifyChange) {
							$("<span>"+this.text+" </span>").click(goBackFunction).appendTo(container);
							var input = $("<input type='text' placeholder='value'>").appendTo(container).change(
									function() {										
										notifyChange({minequalRef: $(this).val()});
									}
								);
							try {
								input.val(load.minequalRef);
								notifyChange({minequalRef: load.minequalRef});
							} catch(err) {};
						}
    				},
    				{
    					text: "be greater than or equal",
						appendContent: function(container, goBackFunction, load, notifyChange) {
							$("<span>"+this.text+" </span>").click(goBackFunction).appendTo(container);
							var input = $("<input type='text' placeholder='value'>").appendTo(container).change(
									function() {										
										notifyChange({maxequalRef: $(this).val()});
									}
								);
							try {
								input.val(load.maxequalRef);
								notifyChange({minRef: load.maxequalRef});
							} catch(err) {};
						}
    				}];

    			var activityStates = ["started", "canceled","completed"];
                //var activityNames = ["Assign priority", "Analyse RFC", "Cancel RFC"];
                var activityNames = [];
                var activityTypes = ["Activity", "process", "events", "data object"];
                var DataObjectNames= ["RFC"];
                var DataObjectState= ["registered"];
                var eventNames= ["receive RFC"];
                var ConditionDataObjectProperties= ["type of Change=perfective"];
                var DataObjectPropertyNames= ["affected department"];
                var aggregates= ["sum", "max", "min", "average"];
                var expressions=["SUM(x1, x2, x3, x4)"];
                var parametres= ["x1", "x2", "x3", "x4"];
		   		
		   		


                
                var eventOptions = [
		   			{
		   				text: "activity {activityName} {activityState}",
		   				activityName: {
		   					options: activityNames
		   				},
		   				activityState: {
		   					prefix: " becomes",
		   					options: activityStates
		   				}
		   			},
		   			{
		   				text: "process {processState}",
		   				processState: {
		   					prefix: " becomes",
		   					options: activityStates
		   				}
		   			},
		   			{
		   				text: "data object {DataObjectName} {DataObjectState}",
		   				DataObjectName: {
		   					options: DataObjectNames
		   				},
		   				DataObjectState: {
		   					prefix: " becomes",
		   					options: DataObjectState
		   				}
		   			},
		   			{
		   				text: "event {eventName}  is triggred",
		   				eventName: {
		   					options: eventNames
		   				}
		   			}
		   			
		   		];

                var stateOptions = [
		   			{
		   				text: "activity {activityName} {activityState}",
		   				activityName: {
		   					options: activityNames
		   				},
		   				activityState: {
		   					prefix: " is",
		   					options: activityStates
		   				}
		   			},
		   			{
		   				text: "process {processState}",
		   				processState: {
		   					prefix: " is",
		   					options: activityStates
		   				}
		   			},
		   			{
		   				text: "data object {DataObjectName} {DataObjectState}",
		   				DataObjectName: {
		   					options: DataObjectNames
		   				},
		   				DataObjectState: {
		   					prefix: " is",
		   					options: DataObjectState
		   				}
		   			},
		   			{
		   				text: "event {eventName} has been triggred",
		   				eventName: {
		   					options: eventNames
		   				}
		   			}
		   			
		   		];

                var timeMeasure = 	{
	   					text: "the duration between the time instants {startEvent} {endEvent}",
	   					startEvent: {
	   						prefix: "when",
	   						options: eventOptions
	   					},
	   					endEvent: {
	   						prefix: " and when",
	   						options: eventOptions
	   					}
	   				};

		   		
		   		var countMeasure= {
    					text: "the number of times {event}",
    					event: {
    						options: eventOptions
    					}
					};

		   		
		   		var StateConditionMeasure= {
						text: "{activityType} {activityName} is {state} {activityState}",
						activityType:{
							           options:activityTypes
						             },
						activityName:{
							            options: activityNames
						             },
						activityState:{
							             prefix: "is state",
							             options: activityStates

					                  },
					    state:{
					    	   options:State
					    	  }               
					    };
					
					                	   
				var State={
						text: "{state}",
						state:{
							    options:stateOptions
                              }
						};	
				
						
					
		   		var DataPropertyCondition= 
					{
  						text: "{DataObjectName} that satifies: {ConditionDataObjectPropertie}",
  						DataObjectName:{
  							options: DataObjectNames
  						},
  						ConditionDataObjectPropertie:{
  							prefix: "that satifies",
  							options:ConditionDataObjectProperties
  						}
  						
  					};
		   		var DataMeasure= {
						text: "the value of {DataObjectPropertyName} of {DataObjectName}",
						DataObjectPropertyName:{
							prefix: "the value of",
							options: DataObjectPropertyNames
						},
						DataObjectName:{
							prefix: "of",
							options: DataObjectNames
						},
					};
		   		var AggregatedMeasure= 	{
						text: "the {Aggregate} of {MeasureForAgg} group by property {DataObjectPropertyName} of {DataObjectName}",
						Aggregate:{
							prefix:"the",
							options: aggregates
						},
						MeasureForAgg:{
							prefix: "of",
							options: measureOptions
						},
						DataObjectPropertyName:{
							prefix: "group by property",
							options: DataObjectPropertyNames
						},
						DataObjectName:{
							prefix: "of",
							options: DataObjectNames
						}
					};
		   		
		   		var DerivedMeasure= {
  						text: "the function {expresion} where {parametros} is {MeasureForDer}",
  						expresion:{
  							prefix: "the function",
  							options: expressions
  						},
  						parametros:{
  							prefix: "where",
  							options: parametres
  						},
  						MeasureForDer:{
  							prefix: "is",
  							options: MeasureForDers
  						}
  						
  					};

         

                
                var MeasureForDers= [
                	timeMeasure,
                	countMeasure,
                	StateConditionMeasure,
                	DataMeasure,
                	AggregatedMeasure
					
	   			];
                var measureForAgges = [
                  	   				timeMeasure,
                  	   			    countMeasure,
                            	    StateConditionMeasure,
                            	    DataPropertyCondition,
                            	    DataMeasure,
                            	    DerivedMeasure
                  	   				
                  	   			];


		   		var measureOptions = [
					timeMeasure,
					countMeasure,
            	    StateConditionMeasure,
            	    DataPropertyCondition,
            	    DataMeasure,
            	    AggregatedMeasure,
            	    DerivedMeasure
				];
});
