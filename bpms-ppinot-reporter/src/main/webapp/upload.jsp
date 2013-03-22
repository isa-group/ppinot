<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="es.us.isa.bpms.ppinot.html.*, es.us.isa.ppinot.handler.PpiNotModelUtils,
    es.us.isa.bpms.ppinot.historyreport.HistoryReport,es.us.isa.bpms.ppinot.historyreport.HistoryUtils"
    import="java.util.*"
    import="org.apache.commons.fileupload.*"
    import="org.apache.commons.fileupload.servlet.*"
    import="org.apache.commons.fileupload.disk.*"
    import="java.io.*"
	import="org.apache.commons.configuration.XMLConfiguration"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activiti PPINot</title>
<link rel="stylesheet" type="text/css" href="css/yui-2.8.1-activiti.css" />
<link rel="stylesheet" type="text/css" href="css/activiti-core.css" />
<link rel="stylesheet" type="text/css" href="css/activiti-app.css" />
<link rel="stylesheet" type="text/css" href="css/tasks/task-list.css" />
</head>
<body>
<div id="header">
	<div class="activiti-component">
		<div class="application-info">
		  	<img src="images/logo.png" height="54px" width="67px"/>
		</div>
		<div class="user-info">
			&nbsp;Activiti - PPINot
		</div>
	</div>
</div>
<div id="navigation">
<div id="template_x002e_navigation_x002e_divided-left" class="activiti-component">
<ul class="activiti-menu pages">
  <li>
    <a href="index.jsp" class="normal ">Inicio</a>
  </li>
  <li>
    <a href="upload.jsp" class="current last">Administrar XML</a>
  </li>
</ul>
</div>
</div>
<div id="content">
	<div id="main">

    	<h1>Administrar XML</h1>

<%

	// REALIZA LA ACCION SOLICITADA: SUBIR UN ARCHIVO O BORRAR UNO
	String uploadPath = (String) request.getSession().getAttribute("UPLOADPATH");
	String tmpPath = (String) request.getSession().getAttribute("TMPPATH");
	if (uploadPath==null || tmpPath==null) {
		
		XMLConfiguration xmlConfig = new XMLConfiguration("ppinotreporter.cfg.xml");
		uploadPath = xmlConfig.getString("uploadpath");
		request.getSession().setAttribute("UPLOADPATH", uploadPath);
		tmpPath = xmlConfig.getString("tmppath");
		request.getSession().setAttribute("TMPPATH", tmpPath);
	}
	File tmpFile = new File(tmpPath);
	if (!tmpFile.exists())
		tmpFile.mkdirs();
	File uploadFile = new File(uploadPath);
	if (!uploadFile.exists())
		uploadFile.mkdirs();
	
	//String uploadPath = getServletContext().getInitParameter("UPLOADPATH");
	
	String mensaje = "";
	File destino = new File(uploadPath);
	ServletRequestContext src=new ServletRequestContext(request);

	Boolean eliminar = false;
	Boolean descargar = false;
	Boolean generarEpl = false;
	String nomFich = "";
	
	if(ServletFileUpload.isMultipartContent(src)){

		DiskFileItemFactory factory = new DiskFileItemFactory((1024*1024),destino);
		ServletFileUpload upload=new  ServletFileUpload(factory);

		@SuppressWarnings("unchecked")
		List<FileItem> lista = upload.parseRequest(src);
		File file= null;

		Iterator<FileItem> it = lista.iterator();
		while(it.hasNext()){
			
			FileItem item=(FileItem)it.next();
			
			if(item.isFormField()) {

//out.println(item.getFieldName() + " = " + item.getString() + "<br />");
				
				if (!eliminar)
					eliminar = (item.getFieldName().contentEquals("accion") && item.getString().contentEquals("eliminar"));
				if (!descargar)
					descargar = (item.getFieldName().contentEquals("accion") && item.getString().contentEquals("descargar"));
				if (!generarEpl)
					generarEpl = (item.getFieldName().contentEquals("accion") && item.getString().contentEquals("generarEpl"));
				if (nomFich.contentEquals("") && item.getFieldName().contentEquals("nomFich"))
					nomFich = item.getString();
			}
			else
			{
				
				// si es un .xml, se sube el archivo seleccionado
				if (!item.getName().contentEquals("")) {
					String ext = item.getName().substring( item.getName().lastIndexOf(".") );;
					if (ext.contentEquals(".xml"))
					{
						// si es un archivo .xml se guarda en la ruta indicada
						file=new File(item.getName());
						item.write(new File(destino,file.getName()));
						mensaje = "Archivo subido";
					}
					else
						mensaje = "ERROR: El archivo debe ser .xml";
				}
			}
		}
	}

	// si se indico, se elimina el archivo seleccionado
	if (eliminar) {

		File file = new File(destino, nomFich);
		if (file.delete())   
			mensaje = "El archivo " + nomFich + " ha sido borrado satisfactoriamente";
		else   
			mensaje = "El archivo " + nomFich + " no puede ser borrado";
	}

	// si se indico, se descarga el archivo seleccionado
	if (descargar) {

		File file = new File(destino, nomFich);
		try {
			
			Html.download(response, file);
		} catch (IOException e) {
			
			mensaje = "Error descargando";
		}         
	}
	
	// si se indico, se genera el modulo EPL del archivo seleccionado
	if (generarEpl) {
		
		String eplName = "";
		String eplContent = "";
		try {

			String[] r = HistoryUtils.generateEpl(uploadPath, nomFich);
			
			eplName = r[0];
			eplContent = r[1];
		} catch (Exception ex) {
			
			mensaje = "Error procesando el archivo XML";
		}
		
		File file = new File(new File(tmpPath), eplName);
		try {
		
			Html.save(file, eplContent );
		} catch (Exception ex) {
			
			mensaje = "Error creando el modulo EPL";
		} 
		
		try {
			
			Html.download(response, file);
		} catch (IOException e) {
			
			mensaje = "Error descargando";
		}         
		
	}
	
	// obtiene la lista de los archivos en la carpeta
	String [] xmlLista = destino.list(); 
%>

<script language="javascript">
	function validarEliminar() {
		if (document.appform.nomFich.value=="")
			alert("Debe seleccionar un archivo");
		else
		if (confirm("¿Realmente desea eliminar el archivo seleccionado?")) {
			var accion = document.getElementById("accion");
			accion.value = "eliminar";	
			document.appform.submit();
		}
	}
	function validarDescargar() {
		if (document.appform.nomFich.value=="")
			alert("Debe seleccionar un archivo");
		else {
			var accion = document.getElementById("accion");
			accion.value = "descargar";	
			document.appform.submit();
		}
	}
	function validarEpl() {
		if (document.appform.nomFich.value=="")
			alert("Debe seleccionar un archivo");
		else {
			var accion = document.getElementById("accion");
			accion.value = "generarEpl";	
			document.appform.submit();
		}
	}
</script>

<form name="appform" action="upload.jsp" method="post" enctype="multipart/form-data">

<div class="section">
<label class="etiqueta"><%out.println(mensaje);%></label>
</div>

<div class="section">
<label class="etiqueta">Archivos subidos (<%if (xmlLista==null) out.println("0"); else out.println(xmlLista.length);%>):</label><br />

<select name="nomFich" size="10">
<%
	if (xmlLista!=null)
    for (String xml : xmlLista) {
		
		out.println("<option value=\"" + xml + "\">" + xml + "</option>");
    }
%>
</select>

</div>

<div class="section">
<input id="accion" name="accion" type="hidden" value="" />
<input type="button" value="Eliminar" name="eliminar" onclick="validarEliminar();">
<input type="button" value="Descargar" name="descargar" onclick="validarDescargar();">
<input type="button" value="Generar EPL" name="generarEpl" onclick="validarEpl();">
</div>

<div class="section">
<label class="etiqueta">Archivo a subir:</label><input type="file" name="fichero" />&nbsp;&nbsp;<input type="submit" value="Subir" name="subir">
</div>

</form>

	</div>
</div>

</body>
</html>

