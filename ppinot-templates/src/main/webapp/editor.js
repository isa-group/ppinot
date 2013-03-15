var nTable = 1;
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
	var newID = "PPI_table_"+nTable++;	
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
	td2.innerHTML = "<input type='text' id='ppi' name='PPI_id'  value='"+newID+"'>";	
	td3.innerHTML = "Name:";	
	td4.innerHTML = "<input type='text' id='Name' name='Name'>";	
	td5.innerHTML = "Process:";	
	td6.innerHTML = "<input type='text' id='Process' name='Process'>";	
	td7.innerHTML = "Goals:";	
	td8.innerHTML = "<input type='text' id='Goals' name='Goals'>";	
	td9.innerHTML = "Definition:";
	td10.innerHTML = "The PPI is defined as ";	
	td11.innerHTML = "<input id='Def' type='text' name='Definition'>";	
	td12.innerHTML = "Target:";	
	td13.innerHTML = "The PPI value must ";	
	td14.innerHTML = "<input id='Tar' type='text' name='Target'>";	
	td15.innerHTML = "Unit:";	
	td16.innerHTML = "<input type='text' id='Unit' name='Unit'>";	
	td17.innerHTML = "Scope:";	
	td18.innerHTML = "<input type='text' id='Scope' name='Scope'>";	
	td19.innerHTML = "Source:";	
	td20.innerHTML = "<input type='text' id='Source' name='Source'>";	
	td21.innerHTML = "Responsible:";	
	td22.innerHTML = "<input type='text' id='Responsible' name='Responsible'>";	
	td23.innerHTML = "Informed:";	
	td24.innerHTML = "<input type='text' id='Informed' name='Informed'>";	
	td25.innerHTML = "Comments:";	
	td26.innerHTML = "<input type='text' id='Comments' name='Comments'>";
	
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
}

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
	        $("#Def").autocomplete({source: MeasureForAgg});	    
});	  

$(function(){
	var targetOneBound=["be less than",
	                    "be greater than",		   			    
	                    "be less than or equal",		   			    
	                    "be greater than or equal",		   			    
	                    "between [intervalo]"];			
	$("#Tar").autocomplete({source: targetOneBound});	
});	