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
					console.log("name:  "+this.name);
					console.log("url:  "+this.url);
					console.log("editor:  "+this.editor);
					console.log("id:  "+this.id);
					var listItem = $("<li></li>");
					var enlace = $(<a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a>");
					listItem.append(enlace);
					menu.append(listItem);
					//menu.append("<li><a tabindex=\"-1\" id=\""+this.name+"\" href=\"#\">"+this.name+"</a></li>");
					//var elem = document.getElementById(this.name);
					enlace.click(function() {
						processesHandler.processLoaded = this.id;						
						processesHandler.loadProcess(this.id);
						//processesHandler.loadRaciTable(this.id);
						$("#drop-processes").text(this.id + " (Select another process)");
						//$("#racimodelpane").show();
					});
				});
			}
		});
	},
	loadProcess: function(id) {
		//api/repository/processes/ppi/ppis
		//api/repository/processes/ppi-copy-/ppis
		var editorurl = this.editors[id];
		$("#signavio-frame").attr("src", editorurl);
			var url = this.processes[id];
		$.ajax({
			type: "GET",
			url: url,
			dataType: "text",
			success: function(data) {
				$("#bpmnmodel").val(data);
			}
		});				
	}
	/*,
	loadRaciTable: function(id) {
		var url = this.processes[id]+"/raci";
		var processesHandler = this;
		
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			success: function(data) {
				$("#racimodel").val($.toJSON(data));
				raciLoader.load(data);
				raciLoader.generateTable();
			},
			statusCode: {
			    404: function() {
			      processesHandler.initRaci(id);
			    }
			}
		});
	}*/	
};
/*var raciLoader = {
		racitable: $("#racitable"),
		racidetail: $("#racidetail"),
		
		configure: function(tableId, detailId) {
			this.racitable = $("#"+tableId);
			this.racidetail = $("#"+detailId);
		},
		
		racimatrix: [],
		
		roles: [],
		
		availableRaciRoles: ["responsible", "accountable", "support", "consulted", "informed"],
		
		addActivity: function(activityName) {
			if (this.racimatrix[activityName] == null)
				this.racimatrix[activityName] = [];
		},
		
		addRole: function(activityName, role, raciRole, binding) {
			this.roles[role] = role;
			if (this.racimatrix[activityName][role] == null)
				this.racimatrix[activityName][role] = [];
			
			if (binding != null) 
				this.racimatrix[activityName][role][raciRole] = binding;
			else
				this.racimatrix[activityName][role][raciRole] = "";
			
		},
		
		initRaci: function(data) {
			var racimatrix = [];
			$(data).each(function() {
				racimatrix[this] = [];
			});

			this.racimatrix = racimatrix;
		},
		
		newRole: function(roleName) {
			this.roles[roleName] = roleName;
		},
		
		generateTable: function() {
			var racitable = this.racitable;
			var racimatrix = this.racimatrix;
			var racidetail = this.racidetail;
			var raciLoader = this;
			var availableRaciRoles = this.availableRaciRoles;
			
			racitable.html("");
			this.generateTableHeader(racitable);
			this.generateTableRows(racitable);
			
			$("td").popover({trigger: "hover", placement: "bottom"});
			$("td").click(function() {
				var content = "";
				var activity = this.parentNode.cells[0].innerText;
				var role = this.parentNode.parentNode.parentNode.rows[0].cells[this.cellIndex].innerText;
				content += "<form class=\"well\" action=\"#\">";
				for (id in availableRaciRoles) {
					var roleRaci = availableRaciRoles[id];
					content += "<label class=\"checkbox\">";
					content += "<input id=\"racidetail-"+roleRaci+"\" type=\"checkbox\">"+roleRaci;
					content += "</label>";
					
					//content += "<label>Raci role: </label>"
					//content += "<input type=\"text\" class=\"input-small\" value=\""+roleRaci+"\"/>";
					//content += "<select><option>responsible</option><option>accountable</option>  <option>support</option>  <option>informed</option>  <option>consulted</option></select>";
					//content += "<label>Binding expression: </label>"
					content += "<input id=\"binding-"+roleRaci+"\" type=\"text\" class=\"input-xlarge\" placeholder=\"Binding expression\"/>";
					content += "<hr/>";							
				}
				content += "<a href=\"#\" class=\"btn btn-primary\" id=\"racidetail-ok\">Ok</a>";
				content += "<a href=\"#\" class=\"btn\" id=\"racidetail-cancel\">Cancel</a>";
				content += "</form>";
				racidetail.html(content);
				
				for (id in availableRaciRoles) {
					var roleRaci = availableRaciRoles[id];
					var checkbox = $("#racidetail-"+roleRaci);
					var binding = $("#binding-"+roleRaci);
					
					if (racimatrix[activity][role] != null && racimatrix[activity][role][roleRaci] != null) {
						checkbox.attr("checked", true);
						binding.val(racimatrix[activity][role][roleRaci]);
					}
					else {
						checkbox.attr("checked", false);
						binding.val("");
					}
				}
				
				$("#racidetail-ok").click(function() {
					racimatrix[activity][role] = [];
					for (id in availableRaciRoles) {
						var roleRaci = availableRaciRoles[id];
						var checkbox = $("#racidetail-"+roleRaci);
						var binding = $("#binding-"+roleRaci);
						
						if (checkbox.is(":checked")) {
							racimatrix[activity][role][roleRaci] = binding.val();
						}
					}
					
					raciLoader.generateTable();
				});
				
				$("#racidetail-cancel").click(function() {
					racidetail.html("");
				});
				
			});
		},
		
		generateTableRows: function(racitable) {
			for (activity in this.racimatrix) {
				var row = "<tr>";
				row += "<td>"+activity+"</td>";
				row += this.generateRoles(activity);
				row +="</tr>";
				racitable.append(row);						
			}
		},
		
		generateRoles: function(activity) {
			var row = "";
			
			for (role in this.roles) {
				var content = "";
				var tooltip = "";
				var raciRoles = this.racimatrix[activity][role];
				if (raciRoles != null) {
					for (raciRole in raciRoles) {
						if (content != "") {
							content += " / ";
							tooltip += "<br/>";
						}
						var cellText = this.generateCellText(raciRole, raciRoles[raciRole]); 
						content += cellText.cell;
						tooltip += cellText.tooltip;
					}
				}
				var title = "Role "+role+" in activity "+activity;
				row += "<td rel=\"popover\" data-title=\""+title+"\" data-content=\""+tooltip+"\">"+content+"</td>";
			}
			
			return row;
			
		},
		
		getRaci: function() {
			var raci = {};
			raci.bp = "Autogenerated";
			raci.activities = [];
			var tempActivities = [];
			
			for (activity in this.racimatrix) {
				tempActivities[activity] = this.getRaciActivity(activity);
			}
			
			for (activity in tempActivities) {
				raci.activities.push({
					activityName: activity,
					responsible: tempActivities[activity]["responsible"],
					accountable: tempActivities[activity]["accountable"],
					support: tempActivities[activity]["support"],
					informed: tempActivities[activity]["informed"],
					consulted: tempActivities[activity]["consulted"]
				});
			}
			
			return raci;
		},
		
		getRaciActivity: function(activity) {
			var result = [];
			
			for (role in this.racimatrix[activity]) {
				for (raciRole in this.racimatrix[activity][role]) {
					var elem = {role: role, bindingExpression: this.racimatrix[activity][role][raciRole]};
					if (raciRole == "responsible" || raciRole == "accountable") {
						result[raciRole] = elem;	
					}
					else {
						if (result[raciRole] == null)
							result[raciRole] = [elem];
						else
							result[raciRole].push(elem);
					}							 
				}
			}
			
			if (result["responsible"] == null)
				result["responsible"] = {};
			if (result["accountable"] == null)
				result["accountable"] = {};
			if (result["support"] == null)
				result["support"] = [];
			if (result["informed"] == null)
				result["informed"] = [];
			if (result["consulted"] == null)
				result["consulted"] = [];

			return result;
		},
		
		generateCellText: function(raciRole, binding) {
			var cellText = "";
			var tooltip = "";
			
			if (raciRole == "responsible")
				cellText = "R";
			else if (raciRole == "accountable")
				cellText = "A";
			else if (raciRole == "support")
				cellText = "S";
			else if (raciRole == "consulted")
				cellText = "C";
			else if (raciRole == "informed")
				cellText = "I";
			
			tooltip = "<b>"+raciRole+"</b>";
			
			if (binding != null && binding != "") {
				cellText += "*";
				tooltip += ": "+binding;
			}
			return {"cell": cellText, "tooltip": tooltip};
		},
		
		
		generateTableHeader: function(racitable) {
			var row = "<thead><tr><th>Activities</th>";
			
			for (x in this.roles) {
				row += "<th>"+x+"</th>";
			}
			row += "</tr></thead>";
			
			racitable.append(row);
		},
				
		load: function(raci) {
			var raciLoader = this;
			this.racimatrix = [];
			this.roles = [];
			$(raci.activities).each(function() {
				var activityName = this.activityName;
				raciLoader.addActivity(this.activityName);
				
				if (this.responsible != null) {
					raciLoader.addRole(this.activityName, this.responsible.role, "responsible", this.responsible.bindingExpression);
				}
				
				if (this.accountable != null) {
					raciLoader.addRole(this.activityName, this.accountable.role, "accountable", this.accountable.bindingExpression);						
				}
				
				if (this.support != null) {
					$(this.support).each(function() {
						raciLoader.addRole(activityName, this.role, "support", this.bindingExpression);	
					});													
				}
				
				if (this.consulted != null) {
					$(this.consulted).each(function() {
						raciLoader.addRole(activityName, this.role, "consulted", this.bindingExpression);	
					});													
				}
				
				if (this.informed != null) {
					$(this.informed).each(function() {
						raciLoader.addRole(activityName, this.role, "informed", this.bindingExpression);	
					});													
				}
				
			});
			
		}				
};

raciLoader.configure("racitable", "racidetail");*/
processesHandler.loadProcessesList();
//End get ajax