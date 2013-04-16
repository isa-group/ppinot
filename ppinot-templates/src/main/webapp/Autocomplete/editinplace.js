/*************************************************************
Utilizing the jQuery Framework. http://www.jquery.com
Code Author: Ben Thomas, Unleashed Technologies, Jan 13 2010
*************************************************************/

$(document).ready(function() {
	//When div.edit me is clicked, run this function
	$("div.editme").click(function() {
		//This if statement checks to see if there are 
		//and children of div.editme are input boxes. If so,
		//we don't want to do anything and allow the user
		//to continue typing
		if ($(this).children('input').length == 0) {
		
			//Create the HTML to insert into the div. Escape any " characters 
			var inputbox = "<input type='text' class='inputbox' value=\""+$(this).text()+"\">";
			
			//Insert the HTML into the div
			$(this).html(inputbox);
			
			//Immediately give the input box focus. The user
			//will be expecting to immediately type in the input box,
			//and we need to give them that ability
			$("input.inputbox").focus();
			
			//Once the input box loses focus, we need to replace the
			//input box with the current text inside of it.
			$("input.inputbox").blur(function() {
				var value = $(this).val();
				$(".editme").text(value);
			});
		}
	});
	
	$("div.selectme").click(function() {
	//Only place a select box in this div if there is 
	//not one there already
		if ($(this).children('select').length == 0) {
		
			//Since we cannot start an id with a number, we must
			//place alphabetical characters in front of it, separated by a -
			//We find the actual id on the right side of the - character
			var id = $(this).attr('id').split("-");
			var currentid = parseInt(id[1]);
			var str = "";
			
			//This is our array of select values. These could be anything you wish,
			//with any number of values.
			var arr = new Array("Apples","Oranges","Pears","Grapes","Strawberries");

			//Turn the array into a select box. Show the current value as the selected value
			//Note that we put ids on the options. This is paramount in being able to display
			//the correct text back to the user
			for(i=0; i<arr.length; i++) {
				if (currentid == i) 
					str += "<option selected id='opt-"+i+"' value='"+i+"'>"+arr[i]+"</option>";
				else
					str += "<option value='"+i+"' id='opt-"+i+"'>"+arr[i]+"</option>";
			}
			str = "<select class='selectbox'>"+str+"</select>";
			
			//Put the select box into the div
			$(this).html(str);
			
			
			$("select.selectbox").focus();
			$("select.selectbox").blur(function() {
				//Get the user chosen value
				var value = $(this).val();
				
				//Obtain the textual representation of the chosen value
				var valuetext = $(this).children('option#opt-'+value).text();
				
				//Replace the selectme div with the new id. This can be parsed 
				//for a database update, and allows the user to choose another
				//value if he or she clicks on the div again.
				$("div.selectme").attr({'id': "selectme-"+value});
				
				//Put the text into the select div.
				$(".selectme").text(valuetext);
			});
		}
	});

});
