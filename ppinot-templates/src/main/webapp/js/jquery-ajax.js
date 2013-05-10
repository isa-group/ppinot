//Get ajax, made like this: http://labs.isa.us.es:8080/cweb/
var processesHandler = {		
	processes: [],			
	editors: [],			
	processLoaded: null,			
	loadProcessesList: function() {
		$.ajax({
			type: "GET",
			url: "api/repository/processes",
			dataType: "json",
			success: function(data) {
				processesHandler.processes = data;
				var menu = $("#menu-processes");
				menu.html("");
				$(data).each(function() {
					processesHandler.processes[this.name] = this.url;
					processesHandler.editors[this.name] = this.editor;
					var listItem = $("<li></li>");
					var enlace = $("<a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a>");
					listItem.append(enlace);
					menu.append(listItem);
					//Otra forma de hacerlo
					//menu.append("<li><a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a></li>");
					//var elem = document.getElementById(this.name);
					enlace.click(function() {
						processesHandler.processLoaded = this.id;						
						processesHandler.loadProcess(this.id);
						$("#drop-processes").text(this.id + " (Select another process)");
					});
				});
			}
		});
	},
	loadProcess: function(id) {
		//api/repository/processes/ppi/ppis
		//api/repository/processes/ppi-copy-/ppis
		var editorurl = this.editors[id];
		var url = this.processes[id]+"/ppis";
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			success: function(data) {
				//Se pregunta antes de cargar el proceso, ya que se borran todas las plantilas
				var res = confirm("Se borraran las plantillas y se perderan los datos no guardados. ¿Está seguro?");
				if (res){
					//funcion de borrado antes de cargar(esta en editor.js)
					borraPlantillas();					
					$(data).each(function (index) {		
						//recogemos los datos
						var ppi_description = this.description;
						var name = this.name;
						var process_id = this.id;
						var goals = this.goals;
						var definition = "";
						var target = "";
						var unit = "";
						var scope = this.scope;
						var source = "";
						var responsible = this.responsible;
						var informed = this.informed;
						var comments = this.comments;
						
						//si hay mas de un ppi hay que crear la tabla primero
						//añade una plantilla(esta en editor.js)
						addPPI();
						
						//rellenamos la tabla utilizando el index
						document.getElementById('PPI_id_'+index+'').innerHTML = ppi_description;
						document.getElementById('Name_'+index+'').innerHTML = name;
						document.getElementById('Process_'+index+'').innerHTML = process_id;
						document.getElementById('Goals_'+index+'').innerHTML = goals;
						document.getElementById('Definition_'+index+'').innerHTML = definition;
						document.getElementById('Target_'+index+'').innerHTML = target;
						document.getElementById('Unit_'+index+'').innerHTML = unit;
						document.getElementById('Scope_'+index+'').innerHTML = scope;
						document.getElementById('Source_'+index+'').innerHTML = source;
						document.getElementById('Responsible_'+index+'').innerHTML = responsible;
						document.getElementById('Informed_'+index+'').innerHTML = informed;
						document.getElementById('Comments_'+index+'').innerHTML = comments;					
					});
					//edit in place de jquery(editor.js). 
					//Si no se hace, machaca los valores con los de la tabla original.
					inLineEditTable();
				}
			},
			//error en caso de no obtener datos del servidor
			error: function() {
				alert("No se han podido recuperar los datos.");
			}
		});	
	}	
};

processesHandler.loadProcessesList();
//End get ajax

//Save ppi
function savePPI(){	
	//recuperamos el proceso seleccionado
	var processName = processesHandler.processLoaded;
	var url = "api/repository/processes/"+processName+"/ppis";	
	
	//número de tablas	
	var n = getNTable();
	var i = 0;
	//variable contenedora
	var data = [];
	//Recorremos todas las tablas para recoger los datos
	while (i<=n){
		//Si se ha borrado alguna plantilla, ese índice no estará, 
		// por tanto hay que incrementar el contador
		if (document.getElementById('PPI_div_'+i+'') == null){
			i++;
		}
		else{
			data[i] = {
				//recogemos los datos de cada tabla
				description: document.getElementById('PPI_id_'+i+'').innerHTML,
				name: document.getElementById('Name_'+i+'').innerHTML,
				id: document.getElementById('Process_'+i+'').innerHTML,
				goals: document.getElementById('Goals_'+i+'').innerHTML,
				//definition: document.getElementById('Definition_'+i+'').innerHTML,
				//target: document.getElementById('Target_'+i+'').innerHTML,
				unit: document.getElementById('Unit_'+i+'').innerHTML,
				scope: document.getElementById('Scope_'+i+'').innerHTML,
				source: document.getElementById('Source_'+i+'').innerHTML,
				responsible: document.getElementById('Responsible_'+i+'').innerHTML,
				informed: document.getElementById('Informed_'+i+'').innerHTML,
				comments: document.getElementById('Comments_'+i+'').innerHTML,
			};
			i++;
		}
	}		
	$.ajax({
		type: "POST",
		url: url,
		data: $.toJSON(data),
		contentType: "application/json",
		success: function(data) {	
			$(data).each(function (index) {
			//volcamos los datos
			this.description = data[index]description;
			this.name = name;
			this.id = process_id;
			this.goals = goals;
			this.definition = definition;
			this.target = target;
			this.unit = unit;
			this.scope = scope;
			this.source = source;
			this.responsible = responsible;
			this.informed = informed;
			this.comments = comments;
			});
		},
		//error en caso de no poder subir los datos al servidor
		error: function() {
			alert("No se han podido grabar los datos.");
		}
	});	
	//POST http://localhost:9090/api/repository/processes/ppi-copy-/ppis 400 
	//(org.codehaus.jackson.map.JsonMappingException: Can not deserialize instance of java.util.List out of START_OBJECT 
		//	token  at [Source: org.mortbay.jetty.HttpParser$Input@88c18a; line: 1, column: 1]) 
}
//End save ppi