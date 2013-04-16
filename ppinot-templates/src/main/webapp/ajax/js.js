var data;
 
$(document).ready(function() {
	$.ajax({
		url: "data.json",
		data: "nocache=" + Math.random(),
		type: "GET",
		dataType: "json",
		success: function(source){
			data = source;
			showInfo();
		},
		error: function(dato){
			alert("ERROR");
		}
	});							
});
 
function showInfo(){
	$("#data").append(data['data1']['value']);
 
	$.each(data['data2'], function(index, value) {
		$("#data").append('<p>index: ' + index + ' value1: ' + data['data2'][index]['value1']  + ' value2: ' + data['data2'][index]['value2'] + '</p>');
	});
}