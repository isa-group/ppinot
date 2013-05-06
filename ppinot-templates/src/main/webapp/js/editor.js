//start_ add goal and delete goal button
var goal = 1;

function addDiv2(idtr,trg){
	div = document.createElement("div");
	div.setAttribute("id", trg);
	var idtd = idtr;
	tdCont = document.getElementById('addgoals_'+idtd+'');	
	tdCont.appendChild(div);
}
function addGoal(idtr){	
	var g = goal++;
	var idG = "goals_"+g;	
	var trg = "tr_"+idG;
	addDiv2(idtr,trg);	
	td0 = document.createElement("td");
	td0.innerHTML = "<input type='text' id='"+idG+"' name='Goals'>";
	td1 = document.createElement("td");
	td1.innerHTML = "<button type='button' onclick='delGoal("+idtr+","+g+")'><i class='icon-trash'></i></button>";
	div.appendChild(td0);
	div.appendChild(td1);	
}
function delGoal(idtr,id){
	var idt = idtr;
	var parent = document.getElementById('addgoals_'+idt+'');
    var child = document.getElementById('tr_goals_'+id+'');
    parent.removeChild(child);
}
//End_ add goal and delete goal button

//start_ add goal and delete goal button
function delTable(id){
	var resultado = confirm("Va a eliminar la plantilla número "+id+". ¿Está seguro?");
	if (resultado){
		$('.PPITabClass_'+id+'').remove();
	}
}
//End_ add goal and delete goal button

//start of add new table of PPI
var nTab = 1;
var nDiv = 1;
var nClass = 1;
var newIdDiv = "";

function addDiv(){
	newIdDiv = "PPI_div_"+nDiv++;	
	myDiv = document.createElement("div");	
	newNClass = nClass++;
	var clase = "well PPITabClass_"+newNClass;
	myDiv.setAttribute("class",clase);	
	myDiv.setAttribute("id",newIdDiv);	
	divC = document.getElementById('PPIs');	
	divC.appendChild(myDiv);
}

function addPPI(){ 
	addDiv();	
	nTable = nTab++;
	var newID = "PPI_table_"+nTable;	
	var addg = "addgoals_"+nTable;
	myTable = document.createElement("table");	
	tbBody = document.createElement("tbody");	
	tr0 = document.createElement("tr");
	tr1 = document.createElement("tr");	
	tr2 = document.createElement("tr");	
	tr3 = document.createElement("tr");	
	tr4 = document.createElement("tr");	
	tr5 = document.createElement("tr");	
	tr6 = document.createElement("tr");	
	tr7 = document.createElement("tr");	
	tr8 = document.createElement("tr");	
	tr9 = document.createElement("tr");	
	tr10 = document.createElement("tr");	
	tr11 = document.createElement("tr");	
	tr12 = document.createElement("tr");	
	tr13 = document.createElement("tr");	
	td00 = document.createElement("td");	
	td01 = document.createElement("td");
	td1 = document.createElement("td");	
	td2 = document.createElement("td");	
	td3 = document.createElement("td");	
	td4 = document.createElement("td");	
	td5 = document.createElement("td");	
	td6 = document.createElement("td");	
	td7 = document.createElement("td");	
	td8 = document.createElement("td");	
	td9 = document.createElement("td");	
	td10 = document.createElement("td");	
	td11 = document.createElement("td");	
	td12 = document.createElement("td");	
	td13 = document.createElement("td");	
	td14 = document.createElement("td");	
	td15 = document.createElement("td");	
	td16 = document.createElement("td");	
	td17 = document.createElement("td");	
	td18 = document.createElement("td");	
	td19 = document.createElement("td");	
	td20 = document.createElement("td");	
	td21 = document.createElement("td");	
	td22 = document.createElement("td");	
	td23 = document.createElement("td");	
	td24 = document.createElement("td");	
	td25 = document.createElement("td");	
	td26 = document.createElement("td");	
	td27 = document.createElement("td");
	td28 = document.createElement("td");
	td29 = document.createElement("td");
	
	//table attributes
	myTable.setAttribute("id", newID);
	myTable.setAttribute("class", "PPITab");
	td00.innerHTML = "PPI_"+nTable;	
	td00.setAttribute("height","30");
	td01.innerHTML ="<button type='button' id='cabecera_"+nTable+"' onclick='delTable("+nTable+")'> Eliminar plantilla <i class='icon-trash'></i> </button> ";
	td1.innerHTML = "PPI:";	
	td1.setAttribute("height","30");
	td2.innerHTML = "<span class='PPI_id' id='PPI_id_"+nTable+"' style='background-color: transparent;'>PPI descriptive name</span>";
	td3.innerHTML = "Name:";
	td3.setAttribute("height","30");
	td4.innerHTML = "<span class='Name' id='Name_"+nTable+"' style='background-color: transparent;'>name</span>";	
	td5.innerHTML = "Process:";	
	td5.setAttribute("height","30");
	td6.innerHTML = "<span class='Process' id='Process_"+nTable+"' style='background-color: transparent;'>process name(process id)</span>";	
	td7.innerHTML = "Goals:";	
	td7.setAttribute("height","30");	
	td8.innerHTML = "<span class='Goals' id='Goals_"+nTable+"' style='background-color: transparent;'>strategic or operational goals the PPI is related to</span>";	
	td9.innerHTML ="<button type='button' id='Goals_"+nTable+"' onclick='addGoal("+nTable+")'> <i class='icon-plus'></i> </button> ";
	td10.setAttribute("width","70");
	td11.setAttribute("id",addg);
	td12.innerHTML = "Definition:";
	td12.setAttribute("height","30");
	td13.innerHTML = "The PPI is defined as ";	
	td14.innerHTML = "<span class='Definition' id='Definition_"+nTable+"' style='background-color: transparent;'></span>";	
	td15.innerHTML = "Target:";	
	td15.setAttribute("height","30");
	td16.innerHTML = "The PPI value must ";	
	td17.innerHTML = "<span class='Target' id='Target_"+nTable+"' style='background-color: transparent;'></span>";	
	td18.innerHTML = "Unit:";	
	td18.setAttribute("height","30");
	td19.innerHTML = "<span class='Unit' id='Unit_"+nTable+"' style='background-color: transparent;'>unit</span>";	
	td20.innerHTML = "Scope:";	
	td20.setAttribute("height","30");
	td21.innerHTML = "<span class='Scope' id='Scope_"+nTable+"' style='background-color: transparent;'>The process instances considered for this PPI are</span>";	
	td22.innerHTML = "Source:";	
	td22.setAttribute("height","30");
	td23.innerHTML = "<span class='Source' id='Source_"+nTable+"' style='background-color: transparent;'>source from which the PPI measure can be obtained</span>";	
	td24.innerHTML = "Responsible:";
	td24.setAttribute("height","30");
	td25.innerHTML = "<span class='Responsible' id='Responsible_"+nTable+"' style='background-color: transparent;'>role | department | organization | person</span>";	
	td26.innerHTML = "Informed:";	
	td26.setAttribute("height","30");
	td27.innerHTML = "<span class='Informed' id='Informed_"+nTable+"' style='background-color: transparent;'>role | department | organization | person</span>";	
	td28.innerHTML = "Comments:";	
	td28.setAttribute("height","30");
	td29.innerHTML = "<span class='Comments' id='Comments_"+nTable+"' style='background-color: transparent;'>additional comments about the PPI</span>";
	
	tr0.appendChild(td00);
	tr0.appendChild(td01);
	tr1.appendChild(td1);	
	tr1.appendChild(td2);	
	tr2.appendChild(td3);	
	tr2.appendChild(td4);		
	tr3.appendChild(td5);	
	tr3.appendChild(td6);	
	tr4.appendChild(td7);
	tr4.appendChild(td8);
	tr4.appendChild(td9);	
	tr5.appendChild(td10);	
	tr5.appendChild(td11);	
	tr6.appendChild(td12);	
	tr6.appendChild(td13);	
	tr6.appendChild(td14);	
	tr7.appendChild(td15);
	tr7.appendChild(td16);	
	tr7.appendChild(td17);	
	tr8.appendChild(td18);	
	tr8.appendChild(td19);	
	tr9.appendChild(td20);	
	tr9.appendChild(td21);	
	tr10.appendChild(td22);	
	tr10.appendChild(td23);	
	tr11.appendChild(td24);	
	tr11.appendChild(td25);	
	tr12.appendChild(td26);	
	tr12.appendChild(td27);	
	tr13.appendChild(td28);	
	tr13.appendChild(td29);
	
	tbBody.appendChild(tr0);
	tbBody.appendChild(tr1);	
	tbBody.appendChild(tr2);	
	tbBody.appendChild(tr3);	
	tbBody.appendChild(tr4);	
	tbBody.appendChild(tr5);	
	tbBody.appendChild(tr6);	
	tbBody.appendChild(tr7);	
	tbBody.appendChild(tr8);	
	tbBody.appendChild(tr9);	
	tbBody.appendChild(tr10);	
	tbBody.appendChild(tr11);	
	tbBody.appendChild(tr12);	
	tbBody.appendChild(tr13);
	
	myTable.appendChild(tbBody);	
	divT = document.getElementById(newIdDiv);	
	divT.appendChild(myTable);	
	inLineEditTable();
	tabEventTable();
}
function inLineEditTable(){
	$('.PPI_id').inlineEdit();
	$('.Name').inlineEdit();
	$('.Process').inlineEdit();
	$('.Goals').inlineEdit();
	$('.Definition').inlineEdit();
	$('.Target').inlineEdit();
	$('.Unit').inlineEdit();
	$('.Scope').inlineEdit();
	$('.Source').inlineEdit();
	$('.Responsible').inlineEdit();
	$('.Informed').inlineEdit();
	$('.Comments').inlineEdit();
}
function tabEventTable(){
	$('.PPI_id').tabEvent();
	$('.Name').tabEvent();
	$('.Process').tabEvent();
	$('.Goals').tabEvent();
	$('.Definition').tabEvent();
	$('.Target').tabEvent();
	$('.Unit').tabEvent();
	$('.Scope').tabEvent();
	$('.Source').tabEvent();
	$('.Responsible').tabEvent();
	$('.Informed').tabEvent();
	$('.Comments').tabEvent();
}
//end of add new table of PPI

//jquery edit in place
(function($) {
	$.fn.inlineEdit = function(options) {
		//define some options with sensible default values
		//- hoverClass: the css classname for the hover style
		options = $.extend({ hoverClass: 'hover' }, options);
		return $.each(this, function() {
			//define self container
			var self = $(this);
			//create a value property to keep track of current value
			self.value = self.text();
			//bind the click event to the current element, in this example it's span.editable
			self.bind('click', function() {
				self
				//populate current element with an input element and add the current value to it
				.html('<input type="text" value="'+ self.value +'">')
				//select this newly created input element
				.find('input')
				//bind the blur event and make it save back the value to the original span area
				//there by replacing our dynamically generated input element
				.bind('blur', function(event) {
					self.value = $(this).val();
					self.text(self.value);
				})
				//give the newly created input element focus
				.focus();
			})
			//on hover add hoverClass, on rollout remove hoverClass
			.hover(
				function(){
					self.addClass(options.hoverClass);
				},
				function(){
					self.removeClass(options.hoverClass);
				}
			);
		});
	}
})(jQuery);
$(function(){
	$('.PPI_id').inlineEdit();
	$('.Name').inlineEdit();
	$('.Process').inlineEdit();
	$('.Goals').inlineEdit();
	$('.Definition').inlineEdit();
	$('.Target').inlineEdit();
	$('.Unit').inlineEdit();
	$('.Scope').inlineEdit();
	$('.Source').inlineEdit();
	$('.Responsible').inlineEdit();
	$('.Informed').inlineEdit();
	$('.Comments').inlineEdit();
}); 
//End jquery

//Tab event	
(function($) {
	$.fn.tabEvent = function() {
		return this.each(function() {
			$(this).bind('keydown', function(event) {
				//Al presionar tabulador o enter
				if (event.keyCode == 9 || event.keyCode == 13) {					
					var nextBox='';
					var list = $(".PPITab span");
				    var i=list.index(this);	
				    
				    //Vuelve al principio de la tabla
				    if (i == (list.length-1)){
				    	nextBox=$(".PPITab span:first");         
				    }else{
				    	nextBox=list.eq(i+1);    
				    }
				    
				    $(this).find("input").blur();
				    $(nextBox).click();  
				    return false;
				    event.preventDefault();
				}
			});
		});
	}
})(jQuery);

$(document).ready(function(){
    $('.PPI_id').tabEvent();
    $('.Name').tabEvent();
    $('.Process').tabEvent();
    $('.Goals').tabEvent();
    $('.Definition').tabEvent();
	$('.Target').tabEvent();
	$('.Unit').tabEvent();
	$('.Scope').tabEvent();
	$('.Source').tabEvent();
	$('.Responsible').tabEvent();
	$('.Informed').tabEvent();
	$('.Comments').tabEvent();
}); 
//End Tab event