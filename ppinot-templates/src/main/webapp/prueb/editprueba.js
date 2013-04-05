//start_ add goal and delete goal button
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
	}//End_ add goal and delete goal button