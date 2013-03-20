function HTMLhead(){
	var meta= document.createElement("meta");
	var title= document.createElement("title");
	var link= document.createElement("link");
	var ehead1= document.head.appendChild("meta");
	var ehead2= document.head.appendChild("title");
	var ehead3= document.head.appendChild("link");
}

function DivInputLabel(var boss, var div, var input, var label, var text){
	var divy= document.getElementById("div");
    var ebody= boss.appendChild("divy");
    
         var unputy= document.getElementById("input");
         var labelado= document.getElementById("label");
         var ebrody= ebody.appendchild("unputy");
         var ebrody2= ebody.appendchild("labelado").innerHTML= text;
}
function DivInputSelect(var boss, var div, var select, var label, var outgrup, var text){
	var divp= document.getElementById("div");
    var ebody6pro= boss.appendChild("divp");
    var labelpro= document.getElementById("label").innerHTML= "text:";
    var pro= document.getElementById("select");
    var out= document.getElementById("outgrup");
    ebody6pro.appendChild("labelpro");
    var ed=ebody6pro.appendChild("pro");
    ed.appendChild("out");
}


function HTMLBODY(){
	var divform= document.getElementById("div_form");
	var ebody1= document.body.appendChild("divform");
	
	var form= document.createElement("form");
	var ebody2= ebody1.appendChild("form");
	
	   var divdatospersonales= document.getElementById("div_datos_personales");
	   var ebody21= ebody2.appendChild("divdatospersonales");
	
	        var fieldset1= document.createElement("fieldset");
	        var legend= document.createElement("legend");
	        var ebody4= ebody21.appendChild("fieldset1");
	
	        var ebody5= ebody4.appendChild("legend");
	
	        var divsexo= document.getElementById("div_sexo");
	        var ebody6= ebody4.appendChild("divsexo");
	 
	           var divtitulosexo= document.getElementById("div_titulo_sexo");
	           var ebody61= ebody6.appendChild("divtitulosexo");
	           
	          DivInputLabel("ebody6", "div_sexo_hombre", "sexo_hombre", "label_hombre", "HOMBRE:");
	          DivInputLabel("ebody6", "div_sexo_mujer", "sexo_mujer", "label_mujer", "MUJER:");
	
	           DivInputLabel("ebody4", "div_dni", "label_dni", "dni", "D.N.I.:");
	           DivInputLabel("ebody4", "div_nombre", "label_nombre", "nombre", "NOMBRE:");
	           DivInputLabel("ebody4", "div_apellidos", "label_apellidos", "apellidos", "APELLIDO:");
	  
	   var divdireccion= document.getElementById("div_direccion");
	   var ebody22= ebody2.appendChild("divdireccion");
	   
	         var fieldset2= document.createElement("fieldset");
             var legend2= document.createElement("legend");
             var ebody41= ebody22.appendChild("fieldset2");
	
                 var ebody51= ebody41.appendChild("legend2");
             
                 DivInputLabel("ebody41", "div_calle", "label_calle", "calle", "Calle/Avda.:");
                 DivInputLabel("ebody41", "div_poblacion", "label_poblacion", "poblacion", "Poblacion:");
                 
                 DivInputSelect("ebody41", "div_provincia", "provincia", "label_provincia", "Andalucia", "Provincia:")
                 DivInputSelect("ebody41", "div_provincia", "provincia", "label_provincia", "Extremadura", "Provincia:")
              
           var divpractica= document.getElementById("div_practica");
	       var ebody23= ebody2.appendChild("divpractica");
	         
	           var fieldset3= document.createElement("fieldset");
               var legend3= document.createElement("legend");
               var ebody42= ebody23.appendChild("fieldset3");
	         
                 var ebody52= ebody42.appendChild("legend3");
                 
                 DivInputLabel("ebody42", "div_archivo", "label_archivo", "archivo", "Archivos:");
	             
	             
	             var divinfoarchivo=  document.getElementById("div_infoarchivo");
	             var ebody6info= ebody42.appendChild("divinfoarchivo");
	             
	             DivInputLabel("ebody6info", "div_comprimido", "label_comprimido", "comprimido", "ArchivosComprimido:");
	             DivInputLabel("ebody6info", "div_encriptado", "label_encriptado", "encriptado", "ArchivosEncriptado:");
	   	         DivInputLabel("ebody42", "div_descripcion", "label_descripcion", "descripcion", "Descripcion:");
	   	     
	   	    var divsubmit= document.getElementById("div_submit");
	   	    var ebody6submit= ebody23.appendChild("divsubmit");
	   	    var submit= document.getElementById("submit");
}

function creaDocumento(){
	HTMLHead();
	HTMLBODY();
}