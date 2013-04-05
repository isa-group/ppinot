 $(document).ready(function(){
        	
        	
        	// Using a callback function to update 2 divs
        	$("#editme4").click({
        		callback: function(original_element, html, original){
        			$("#updateDiv1").html("The original html was: " + original);
        			$("#updateDiv2").html("The updated text is: " + html);
        			return(html);
        		}
        	});
        	
        });
        