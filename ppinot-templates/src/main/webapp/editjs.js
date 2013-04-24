
var activities= [{
	              "name":"Task2",
	              "id":"sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B",
	              "type":"task"
	               }
                   ,
	               {"name":"Task11",
	            	"id":"sid-680678C2-CDEE-4852-85E7-CAA68E08DF78",
	            	"type":"task"
	            	}];
var events= [{
	          "name":"",
	          "id":"sid-9A9977D6-5D29-4FE5-B999-BBA1DF17B4C1",
	          "type":"startEvent"
	          },
	          {"name":"",
	           "id":"sid-7E04B962-CCB7-400E-A820-8A065602111F",
	           "type":"endEvent"
	           }]
var dataobject= [];
var process;
var getways= [];

$.ajax({
	type: "POST",
	url: "/api/repository/processes/ppi/ppis",
	data: $.toJSON(ppis),
	success: function() {
		function getNameById("sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B", activity);
		function getNameById("sid-680678C2-CDEE-4852-85E7-CAA68E08DF78", activity);
		function getNameById("sid-9A9977D6-5D29-4FE5-B999-BBA1DF17B4C1", events);
		function getNameById("sid-7E04B962-CCB7-400E-A820-8A065602111F", events);
		function getNameById("", getways);
		function getNameById("", dataobject);
		
	},
	contentType : "application/json"
	});

 


function getNameById(id, elements){
	var cad;
	for(i= 0; elements.lenght; i++){
		if(elements[i].id== id){
			cad= elements[i].name;
		}
		
	}
	Console.log(cad);
	
}