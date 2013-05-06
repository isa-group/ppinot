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
					//funcion de borrado antes de cargar
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
						//hasta aqui estan todos los datos recogidos
						
						//si hay mas de un ppi hay que crear la tabla primero
						//la tabla con id 0 ya esta creada y no hace falta añadirla
						if (index!=0){
							addPPI();
						}
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
				}
			},
			//error en caso de no obtener datos del servidor
			error: function() {
				alert("No se han podido obtener los datos.");
			}
		});	
	}	
};

processesHandler.loadProcessesList();
//End get ajax

//Save ppi
function savePPI(){
	
}
//End save ppi