 $(document).ready(function() {
    	$("div.selectme1").click(function() {
    		if ($(this).children('select').length == 0) {
    			var id = $(this).attr('id').split("-");
    			var currentid = parseInt(id[1]);
    			var str = "";
    			var TimeMeasure = new Array("The duration between time instants when Event and when Event",
    					            "The number of times Event",
    					            "BPET BPEN is state BPES",
    					            "the value of data object property name of data object name");
    			var arr1= new Array("The PPI is defined as");
    			for(i=0; i < TimeMeasure.length; i++) {
    				if (currentid == i) 
    					str += "<option selected id='opt-"+i+"' value='"+i+"'>" + arr1[0]+" "+TimeMeasure[i]+"</option>";
    				else
    					str += "<option value='"+i+"' id='opt-"+i+"'>"+ arr1[0]+" "+TimeMeasure[i]+"</option>";
    			}
    			str = "<select class='selectbox'>"+ str+"</select>";
    			
    			$(this).html(str);
    			$("select.selectbox").focus();
    			$("select.selectbox").blur(function() {
    				var value = $(this).val();
    				var valuetext = $(this).children('option#opt-'+value).text();
    				$("div.selectme1").attr({'id': "selectme-"+value});
    				$(".selectme1").text(valuetext);
    		 
    			if ($("div.selectme11").children('select').length == 0) {
		    			var id = $(this).attr('id').split("-");
		    			var currentid = parseInt(id[1]);
		    			var str = "";
		    			var Event= new Array("activity BPEN becomes BPES", 
		    					             "process BPEN becomes BPES", 
		    					             "data object BPEN becomes BPES",
		    					             "event BPEN is triggred");
		    			var cad1= new Array("The PPI is defined as The duration between time instants when");
		    			var cad2= new Array("and when");
		    			
		    			for(i=0; i < Event.length; i++) {
		    				if (currentid == i) 
		    					str += "<option selected id='opt-"+i+"' value='"+i+"'>"+Event[i]+"</option>";
		    				else
		    					str += "<option value='"+i+"' id='opt-"+i+"'>"+ Event[i]+"</option>";
		    			}
		    			str = "<select class='selectbox1'>"+ str +"</select>";
		    			
		    			$("div.selectme1").html(cad1+""+str+""+cad2+""+str);
		    			$("select.selectbox1").focus();
		    			$("select.selectbox1").blur(function() {
		    				var value1 = $(this).val();
		    				var valuetext1 = $(this).children('option#opt-'+value1).text();
		    				$("div.selectme1").attr({'id': "selectme-"+value1});
		    				$(".selectme1").text(valuetext1);
		    				
		    				 if ($("div.selectme12").children('select').length == 0) {
		 		    			var id = $(this).attr('id').split("-");
		 		    			var currentid = parseInt(id[1]);
		 		    			var str = "";
		 		    			var BPEN= new Array("Assign priority",
		 		    			          "Analyse RFC",
		 		    			          "cancel RFC");
		 		    			var BPES= new Array("ready", "active", "withdrawn", "completing", "completed");
		 		    			var cad1= new Array("The PPI is defined as The duration between time instants when");
		 		    			var cad2= new Array("activity");
		 		    			var cad3= new Array("becomes");
		 		    			var cad4= new Array("and when");
		 		    			for(i=0; i < BPEN.length; i++) {
		 		    				if (currentid == i) 
		 		    					str += "<option selected id='opt-"+i+"' value='"+i+"'>"+BPEN[i]+" "+cad3[0]+" "+BPES[i]+"</option>";
		 		    				else
		 		    					str += "<option  id='opt-"+i+"' value='"+i+"'>"+BPEN[i]+" "+cad3[0]+" "+BPES[i]+"</option>";
		 		    					
		 		    				
		 		    					
		 		    			}
		 		    			str = "<select class='selectbox1'>"+ str +"</select>";
		 		    			
		 		    			$("div.selectme1").html(cad1[0]+" "+cad2[0]+" "+str+" "+cad4[0]+" "+cad2[0]+" "+str);
		 		    			$("select.selectbox1").focus();
		 		    			$("select.selectbox1").blur(function() {
		 		    				var value1 = $(this).val();
		 		    				var valuetext1 = $(this).children('option#opt-'+value1).text();
		 		    				$("div.selectme1").attr({'id': "selectme-"+value1});
		 		    				$(".selectme1").text(valuetext1);
		 		    				
			 		    			 if ($("div.selectme13").children('select').length == 0) {
					 		    			var id = $(this).attr('id').split("-");
					 		    			var currentid = parseInt(id[1]);
					 		    			var str = "";
					 		    	        
					 		    			$("div.selectme1").html("The PPI is defined as The duration between time instants when activity Analyse RFC becomes active and when activity Assign priority  becomes completed");
					 		    			$("select.selectbox1").focus();
					 		    			$("select.selectbox1").blur(function() {
					 		    				var value1 = $(this).val();
					 		    				var valuetext1 = $(this).children('option#opt-'+value1).text();
					 		    				$("div.selectme1").attr({'id': "selectme-"+value1});
					 		    				$(".selectme1").text(valuetext1);
					 		    				
					 		    		   });
					 		    		
					 		   
					 		        }
		 		    		   });
		 		    		
		 		    		
		 		   
		 		        }
		    		   });
		    		
		   
		        }
    		});
   
        }

      });
    });