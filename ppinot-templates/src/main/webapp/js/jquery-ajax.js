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
					//creamos la lista contenedora de los procesos
					var listItem = $("<li></li>");
					var enlace = $("<a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a>");
					listItem.append(enlace);
					//añadimos la lista al menu select
					menu.append(listItem);
					
					//Otra forma de hacerlo
					//menu.append("<li><a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a></li>");
					//var elem = document.getElementById(this.name);
					
					//al seleccionar un proceso llama a loadProcess para cargar los datos
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
				var res = confirm("Se borrarán las plantillas y se perderán los datos no guardados. ¿Está seguro?");
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

//Save ppis
function savePPI(){	
	//recuperamos el proceso seleccionado
	var processName = processesHandler.processLoaded;
	var url = "api/repository/processes/"+processName+"/ppis";	
	
	//número de tablas	
	var n = getNTable();
	//contador de plantillas
	var i = 0;
	//contador de goals
	var j = 1;//para el array
	var g = 0;//para recorrer los goals
	
	//variables contenedoras
	var data = [];
	var dataGoals = [];	
	
	//Recorremos todas las tablas para recoger los datos
	while (i<=n){
		//Si se ha borrado alguna plantilla, ese índice no estará, 
		// por tanto hay que incrementar el contador
		if (document.getElementById('PPI_div_'+i+'') == null){
			i++;
		}
		else{
			//recogemos los goals
			//el primero a mano, los demas con el bucle
			dataGoals[0] = document.getElementById('Goals_'+i+'').innerHTML;
			var ng = document.getElementById('addgoals_'+i+'').children.length;
			j = 1;
			while (j<=ng){
				//Si se ha borrado algun goal, ese índice no estará, 
				// por tanto hay que incrementar el contador
				if (document.getElementById('goals_'+g+'') == null){
					g++;
				}
				else{
					dataGoals[j] = document.getElementById('goals_'+g+'').value;
					g++;
					j++;
				}
			}
			data[i] = {
				//recogemos los datos de cada plantilla
				description: document.getElementById('PPI_id_'+i+'').innerHTML,
				name: document.getElementById('Name_'+i+'').innerHTML,
				id: document.getElementById('Process_'+i+'').innerHTML,
				goals: dataGoals[0],				
				//measuredBy: document.getElementById('Definition_'+i+'').innerHTML,
				measuredBy: {},
				//target: document.getElementById('Target_'+i+'').innerHTML,
				target: {refMin:null, refMax:null},
				//unit: document.getElementById('Unit_'+i+'').innerHTML,
				//scope: document.getElementById('Scope_'+i+'').innerHTML,
				scope: {period:null, startDate:null, endDate:null, inStart:false, inEnd:false, year:null},
				//source: document.getElementById('Source_'+i+'').innerHTML,
				responsible: document.getElementById('Responsible_'+i+'').innerHTML,
				informed: document.getElementById('Informed_'+i+'').innerHTML,
				comments: document.getElementById('Comments_'+i+'').innerHTML,
				normalized: [],
				success: [],
				valueString: []
			};
			i++;
		}
	}		
	$.ajax({
		type: "POST",
		url: url,
		data: $.toJSON(data),
		contentType: "application/json",
		success: function() {
		},
		//error en caso de no poder subir los datos al servidor
		error: function() {
			alert("No se han podido grabar los datos.");
		}
	});	
}
//End save ppi