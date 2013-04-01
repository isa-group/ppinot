//start botton add goal, delete goal
var goal = 1;
function addGoal(){
	var idG = "goals_"+goal++;
	tr = document.createElement("tr");	
	var trg = "tr_"+idG;
	tr.setAttribute("id", trg);
	td1 = document.createElement("td");
	td1.innerHTML = "<input type='text' id='"+idG+"' name='Goals'>";
	td2 = document.createElement("td");
	td2.innerHTML = "<button type='button' onclick='delGoal("+trg+")'><i class='icon-trash'></i></button>";
	tr.appendChild(td1);
	tr.appendChild(td2);	
	trCont = document.getElementById('addgoals');	
	trCont.appendChild(tr);
}

function delGoal(id){
	trp = document.getElementById('addgoals');
	trc = document.getElementById(id);
	trp.removeChild(trc);
}//End botton add goal, delete goal

//start of add new table of PPI
var nTab = 0;
var nDiv = 1;
var newIdDiv = "";

function addDiv(){
	newIdDiv = "PPI_div_"+nDiv++;	
	myDiv = document.createElement("div");	
	myDiv.setAttribute("class","well");	
	myDiv.setAttribute("id",newIdDiv);	
	divC = document.getElementById('PPIs');	
	divC.appendChild(myDiv);
}

function addPPI(){ 
	addDiv();	
	nTable = nTab++;
	var newID = "PPI_table_"+nTable;	
	myTable = document.createElement("table");	
	tbBody = document.createElement("tbody");	
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
	
	// atributos para la tabla	
	myTable.setAttribute("id", newID);
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
	td9.innerHTML = "Definition:";
	td9.setAttribute("height","30");
	td10.innerHTML = "The PPI is defined as ";	
	td11.innerHTML = "<span class='Definition' id='Def' style='background-color: transparent;'></span>";	
	td12.innerHTML = "Target:";	
	td13.setAttribute("height","30");
	td13.innerHTML = "The PPI value must ";	
	td14.innerHTML = "<span class='Target' id='Tar' style='background-color: transparent;'></span>";	
	td15.innerHTML = "Unit:";	
	td15.setAttribute("height","30");
	td16.innerHTML = "<span class='Unit' id='Unit_"+nTable+"' style='background-color: transparent;'>unit</span>";	
	td17.innerHTML = "Scope:";	
	td17.setAttribute("height","30");
	td18.innerHTML = "<span class='Scope' id='Scope_"+nTable+"' style='background-color: transparent;'>The process instances considered for this PPI are</span>";	
	td19.innerHTML = "Source:";	
	td19.setAttribute("height","30");
	td20.innerHTML = "<span class='Source' id='Source_"+nTable+"' style='background-color: transparent;'>source from which the PPI measure can be obtained</span>";	
	td21.innerHTML = "Responsible:";
	td21.setAttribute("height","30");
	td22.innerHTML = "<span class='Responsible' id='Responsible_"+nTable+"' style='background-color: transparent;'>role | department | organization | person</span>";	
	td23.innerHTML = "Informed:";	
	td23.setAttribute("height","30");
	td24.innerHTML = "<span class='Informed' id='Informed_"+nTable+"' style='background-color: transparent;'>role | department | organization | person</span>";	
	td25.innerHTML = "Comments:";	
	td25.setAttribute("height","30");
	td26.innerHTML = "<span class='Comments' id='Comments_"+nTable+"' style='background-color: transparent;'>additional comments about the PPI</span>";
	
	tr1.appendChild(td1);	
	tr1.appendChild(td2);	
	tr2.appendChild(td3);	
	tr2.appendChild(td4);	
	tr3.appendChild(td5);	
	tr3.appendChild(td6);	
	tr4.appendChild(td7);	
	tr4.appendChild(td8);	
	tr5.appendChild(td9);	
	tr5.appendChild(td10);	
	tr5.appendChild(td11);	
	tr6.appendChild(td12);	
	tr6.appendChild(td13);	
	tr6.appendChild(td14);	
	tr7.appendChild(td15);	
	tr7.appendChild(td16);	
	tr8.appendChild(td17);	
	tr8.appendChild(td18);	
	tr9.appendChild(td19);	
	tr9.appendChild(td20);	
	tr10.appendChild(td21);	
	tr10.appendChild(td22);	
	tr11.appendChild(td23);	
	tr11.appendChild(td24);	
	tr12.appendChild(td25);	
	tr12.appendChild(td26);	
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
	
	myTable.appendChild(tbBody);	
	divT = document.getElementById(newIdDiv);	
	divT.appendChild(myTable);	
	inLineEditTable();
}

function inLineEditTable(){
	$('.PPI_id').inlineEdit();
	$('.Name').inlineEdit();
	$('.Process').inlineEdit();
	$('.Goals').inlineEdit();
	$('.Unit').inlineEdit();
	$('.Scope').inlineEdit();
	$('.Source').inlineEdit();
	$('.Responsible').inlineEdit();
	$('.Informed').inlineEdit();
	$('.Comments').inlineEdit();
}
//end of add new table of PPI

$(function(){
	var Agg= ["sum", "average", "max", "min"];	
	var b1= ["the"+" "+Agg[0]+" "+"of", "the"+" "+Agg[1]+" "+"of", "the"+" "+Agg[2]+" "+"of", "the"+" "+Agg[3]+" "+"of"];	
	var b2= ["the duration between"];	
	var b3= ["the time instant when"];	
	var b4= [b2[0]+" "+b3[0], b1[0]+" "+b2[0]+" "+b3[0], b1[1]+" "+b2[0]+" "+b3[0], b1[2]+" "+b2[0]+" "+b3[0],  b1[3]+" "+b2[0]+" "+b3[0]];
	
	var BPET= ["activity", "element"];	
	var DataObject= ["data Object"];	
	var DataObjectName=["RFC"];	
	var BPEN= [DataObject[0]+" "+DataObjectName[0], "analysis", DataObjectName[0]];	
	var ac= [BPEN[2]+" "+BPEN[1]];
	var StateConditionMeasure= [BPET[0]+" "+ac[0], BPET[0]+" "+BPEN[0], BPET[1]+" "+ac[0], BPET[1]+" "+BPEN[0]];	
	var BPES= ["active", "completed", "has finished", "canceled", "registered", "approved", "affected"];	
	var EVENT= [StateConditionMeasure[0]+" "+"becomes"+" "+BPES[0], StateConditionMeasure[0]+" "+"becomes"+" "+BPES[1], 
	
	            StateConditionMeasure[0]+" "+"becomes"+" "+BPES[2], StateConditionMeasure[0]+" "+"becomes"+" "+BPES[3],  
	            
	            StateConditionMeasure[0]+" "+"becomes"+" "+BPES[4], StateConditionMeasure[0]+" "+"changes to state"+" "+BPES[0],  
	            
	            StateConditionMeasure[0]+" "+"changes to state"+" "+BPES[1], StateConditionMeasure[0]+" "+"changes to state"+" "+BPES[2],
	            
	            StateConditionMeasure[0]+" "+"changes to state"+" "+BPES[3], StateConditionMeasure[0]+" "+"changes to state"+" "+BPES[4], 
	            
	            StateConditionMeasure[1]+" "+"becomes"+" "+BPES[0], StateConditionMeasure[1]+" "+"becomes"+" "+BPES[1],  
	            
	            StateConditionMeasure[1]+" "+"becomes"+" "+BPES[2], StateConditionMeasure[1]+" "+"becomes"+" "+BPES[3],  
	            
	            StateConditionMeasure[1]+" "+"becomes"+" "+BPES[4], StateConditionMeasure[1]+" "+"changes to state"+" "+BPES[0], 
	            
	            StateConditionMeasure[1]+" "+"changes to state"+" "+BPES[1], StateConditionMeasure[1]+" "+"changes to state"+" "+BPES[2], 
	            
	            StateConditionMeasure[1]+" "+"changes to state"+" "+BPES[3],  StateConditionMeasure[1]+" "+"changes to state"+" "+BPES[4],
	            
	            StateConditionMeasure[2]+" "+"becomes"+" "+BPES[0], StateConditionMeasure[2]+" "+"becomes"+" "+BPES[1], 
	            
	            StateConditionMeasure[2]+" "+"becomes"+" "+BPES[2], StateConditionMeasure[2]+" "+"becomes"+" "+BPES[3],  
	            
	            StateConditionMeasure[2]+" "+"becomes"+" "+BPES[4], StateConditionMeasure[2]+" "+"changes to state"+" "+BPES[2],  
	            
	            StateConditionMeasure[2]+" "+"changes to state"+" "+BPES[1], StateConditionMeasure[2]+" "+"changes to state"+" "+BPES[2], 
	            
	            StateConditionMeasure[2]+" "+"changes to state"+" "+BPES[3], StateConditionMeasure[2]+" "+"changes to state"+" "+BPES[4],
	            
	            StateConditionMeasure[3]+" "+"becomes"+" "+BPES[0], StateConditionMeasure[3]+" "+"becomes"+" "+BPES[1], 
	            
	            StateConditionMeasure[3]+" "+"becomes"+" "+BPES[2], StateConditionMeasure[3]+" "+"becomes"+" "+BPES[3],  
	            
	            StateConditionMeasure[3]+" "+"becomes"+" "+BPES[4], StateConditionMeasure[3]+" "+"changes to state"+" "+BPES[0],  
	            
	            StateConditionMeasure[3]+" "+"changes to state"+" "+BPES[1], StateConditionMeasure[3]+" "+"changes to state"+" "+BPES[2], 
	            
	            StateConditionMeasure[3]+" "+"changes to state"+" "+BPES[3], StateConditionMeasure[3]+" "+"changes to state"+" "+BPES[4] ];
	        var 
	        bp2= [b4[1]+" "+EVENT[0], b4[2]+" "+EVENT[0], b4[3]+" "+EVENT[0], b4[4]+" "+EVENT[0], b4[0]+" "+EVENT[0]];	        
	        var CountMeasure= ["the number of times"];	        
	        var ac3=[CountMeasure[0]+" "+BPET[0]];	        
	        var DataPorpertyConditionMeasure=["that satisfies:", DataObjectName[0]];	        
	        var ConditionMeasure= [DataPorpertyConditionMeasure[0], DataPorpertyConditionMeasure[1], StateConditionMeasure[0], StateConditionMeasure[1], StateConditionMeasure[2], StateConditionMeasure[3]];
	        
	        var DataObjectProperty=["type of change"];	        
	        var ConditionOnDataObjectProperties=[DataObjectProperty[0]+"=perfective"];	        
	        var ac2= ["Analyse in committe that is currently active"];	        
	        var ac4=["and is group by property"];	        
	        var DataObjectPropertyName=[BPES[6]+" "+"departaments"];	        
	        var DataMeasure=["the value of [property]"+DataObjectPropertyName[0]+" "+"of"+BPEN[0]];        
	        var TimeMeasure= [bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[31], bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[36], bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[31],
	        
	                          bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[36], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[31], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[36],
	                          
	                          bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[31], bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[36], bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[32], 
	                          
	                          bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[37], bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[32], bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[37],
	                          
	                          bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[32], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[37], bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[32],
	                          
	                          bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[37], bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[33], bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[38],
	                          
	                          bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[33], bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[38], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[33],
	                          
	                          bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[38], bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[33], bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[38],
	                          
	                          bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[34], bp2[0]+" "+"and"+" "+b3[0]+" "+EVENT[39], bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[34],
	                          
	                          bp2[1]+" "+"and"+" "+b3[0]+" "+EVENT[39], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[34], bp2[2]+" "+"and"+" "+b3[0]+" "+EVENT[39],
	                          
	                          bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[34], bp2[3]+" "+"and"+" "+b3[0]+" "+EVENT[39], bp2[4]+" "+"and when"+" "+EVENT[1], 
	                          
	                          bp2[4]+" "+"and when"+" "+EVENT[2], bp2[4]+" "+"and when"+" "+EVENT[3], bp2[4]+" "+"and when"+" "+EVENT[4], ac3[0]+" "+BPET[0]+" "+EVENT[1],
	                          
	                          ac3[0]+" "+BPET[0]+" "+EVENT[2], ac3[0]+" "+BPET[0]+" "+EVENT[3], ac3[0]+" "+BPET[0]+" "+EVENT[4],
                              
	                          BPET[0]+" "+ac2[0]+" "+BPES[5]+" "+BPEN[2]+" "+DataPorpertyConditionMeasure[0]+" "+ConditionOnDataObjectProperties[0],
                              
	                          b1[0]+" "+CountMeasure[0]+" "+BPEN[0]+" "+"becomes"+" "+BPES[4]+" "+ac4[0]+" "+DataObjectProperty[0]+" "+"of"+" "+DataObject[0]+" "+DataObjectName[0],
                              
	                          b1[1]+" "+CountMeasure[0]+" "+BPEN[0]+" "+"becomes"+" "+BPES[4]+" "+ac4[0]+" "+DataObjectProperty[0]+" "+"of"+" "+DataObject[0]+" "+DataObjectName[0],
                              
	                          b1[2]+" "+CountMeasure[0]+" "+BPEN[0]+" "+"becomes"+" "+BPES[4]+" "+ac4[0]+" "+DataObjectProperty[0]+" "+"of"+" "+DataObject[0]+" "+DataObjectName[0],
                              
	                          b1[3]+" "+CountMeasure[0]+" "+BPEN[0]+" "+"becomes"+" "+BPES[4]+" "+ac4[0]+" "+DataObjectProperty[0]+" "+"of"+" "+DataObject[0]+" "+DataObjectName[0]
                             ];
	        
	        var MeasureForAgg=[DataMeasure[0], TimeMeasure[0], TimeMeasure[1], TimeMeasure[2], TimeMeasure[3], TimeMeasure[4], TimeMeasure[5],                           
	                           TimeMeasure[6], TimeMeasure[7], TimeMeasure[8], TimeMeasure[9], TimeMeasure[10], TimeMeasure[11], TimeMeasure[12],                           
	                           TimeMeasure[13], TimeMeasure[14], TimeMeasure[15], TimeMeasure[16], TimeMeasure[17], TimeMeasure[18],                           
	                           TimeMeasure[19], TimeMeasure[20], TimeMeasure[21], TimeMeasure[22], TimeMeasure[23], TimeMeasure[24], 	                           
	                           TimeMeasure[25], TimeMeasure[26], TimeMeasure[27], TimeMeasure[28], TimeMeasure[29], TimeMeasure[30], 	                           
	                           TimeMeasure[31], TimeMeasure[32], TimeMeasure[33], TimeMeasure[34], TimeMeasure[35], 	                           
	                           TimeMeasure[36], TimeMeasure[37], TimeMeasure[38], TimeMeasure[39], TimeMeasure[40], 	                           
	                           TimeMeasure[41], TimeMeasure[42], TimeMeasure[43], TimeMeasure[44]  ];	         
	       // $("#Def").autocomplete({source: MeasureForAgg});	    
});	  

$(function(){
	var targetOneBound=["be less than",
	                    "be greater than",		   			    
	                    "be less than or equal",		   			    
	                    "be greater than or equal",		   			    
	                    "between [intervalo]"];			
	//$("#Tar").autocomplete({source: targetOneBound});	
});	

(function($) {
	$.fn.inlineEdit = function(options) {
		// define some options with sensible default values
		// - hoverClass: the css classname for the hover style
		options = $.extend({
		hoverClass: 'hover'
		}, options);
		return $.each(this, function() {
			// define self container
			var self = $(this);
			// create a value property to keep track of current value
			self.value = self.text();
			// bind the click event to the current element, in this example it's span.editable
			self.bind('click', function() {
				self
				// populate current element with an input element and add the current value to it
				.html('<input type="text" value="'+ self.value +'">')
				// select this newly created input element
				.find('input')
				// bind the blur event and make it save back the value to the original span area
				// there by replacing our dynamically generated input element
				.bind('blur', function(event) {
					self.value = $(this).val();
					self.text(self.value);
				})
				// give the newly created input element focus
				.focus();
			})
			// on hover add hoverClass, on rollout remove hoverClass
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
	$('.Unit').inlineEdit();
	$('.Scope').inlineEdit();
	$('.Source').inlineEdit();
	$('.Responsible').inlineEdit();
	$('.Informed').inlineEdit();
	$('.Comments').inlineEdit();
}); 