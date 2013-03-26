
$(document).ready(function(){
	

	$("#editme1").editInPlace({
		callback: function(unused, enteredText) { return enteredText; },
	
		show_buttons: true
	});

	$("#editme2").editInPlace({
		callback: function(unused, enteredText) { return enteredText; },
	
		bg_over: "#cff",
		field_type: "textarea",
		textarea_rows: "15",
		textarea_cols: "35",
		saving_image: "./images/ajax-loader.gif"
	});

	
	$("#editme3").editInPlace({
		callback: function(unused, enteredText) { return enteredText; },
		
		field_type: "select",
		select_options: "Change me to this, No way:no"
	});

	
	$("#editme4").editInPlace({
		callback: function(original_element, html, original){
			$("#updateDiv1").html("The original html was: " + original);
			$("#updateDiv2").html("The updated text is: " + html);
			return(html);
		}
	});
	
	$("#editme5").editInPlace({
		saving_animation_color: "#ECF2F8",
		callback: function(idOfEditor, enteredText, orinalHTMLContent, settingsParams, animationCallbacks) {
			animationCallbacks.didStartSaving();
			setTimeout(animationCallbacks.didEndSaving, 2000);
			return enteredText;
		}
	});
	
	
	
});