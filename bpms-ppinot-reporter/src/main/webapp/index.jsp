<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
			import="es.us.isa.bpms.ppinot.html.*,
			es.us.isa.bpms.ppinot.historyreport.HistoryReport, es.us.isa.bpms.ppinot.historyreport.HistoryConst,
			es.us.isa.bpms.ppinot.historyreport.db.HistoryData, es.us.isa.bpms.ppinot.historyreport.ActivityStartEnd,
			es.us.isa.bpms.ppinot.historyreport.PoolStartEnd,
			es.us.isa.ppinot.handler.PpiNotModelUtils, es.us.isa.ppinot.model.*, es.us.isa.ppinot.model.base.*, 
			es.us.isa.ppinot.model.aggregated.*, es.us.isa.ppinot.model.derived.*, es.us.isa.ppinot.model.state.*" 
    		import="java.util.*, java.util.Map.Entry, java.io.*"
    		import="org.apache.commons.configuration.XMLConfiguration"
%>
<%
	// obtiene el camino de la carpeta donde se encuentran los xml
	String uploadPath = (String) request.getSession().getAttribute("UPLOADPATH");
	if (uploadPath==null) {
		
		XMLConfiguration xmlConfig = new XMLConfiguration("ppinotreporter.cfg.xml");
		uploadPath = xmlConfig.getString("uploadpath");
		request.getSession().setAttribute("UPLOADPATH", uploadPath);
	}

	// obtiene un map con los xml en la carpeta configurada
	File destino = new File(uploadPath);
	if (!destino.exists())
		destino.mkdirs();
	Map<String, String> xmlMap = new HashMap<String, String>();
    for (String xml : destino.list()) 
    	xmlMap.put(xml, xml);

	// obtiene el objeto para realizar los reportes a partir de la historia de la ejecucion de los procesos
	HistoryReport historyReport = (HistoryReport) request.getSession().getAttribute("historyReport");
	if (historyReport==null) {
		historyReport = new HistoryReport();
	}
	request.getSession().setAttribute("historyReport", historyReport);

	// obtiene el nombre del xml seleccionado por el usuario
	String nomFich = request.getParameter("nomFich");
	if (nomFich==null) nomFich = "";
	
	// obtiene la instancia de proceso seleccionada
	String instanceId = request.getParameter( HistoryConst.INSTANCEID_VARNAME );
	if (instanceId==null) instanceId = "";
	
	// obtiene la medida de instancia seleccionada
	String measureId = request.getParameter( HistoryConst.MEASUREID_VARNAME );
	if (measureId==null) measureId = "";

	// crea el reporte si hay algun xml seleccionado
	String mensaje = "";
	if (nomFich!="")
		try {

			// hace el reporte a partir del archivo xml indicado
			historyReport.doReportFromXML(uploadPath, nomFich, instanceId, measureId);
			
			mensaje = "Achivo XML procesado correctamente";
		} catch (Exception ex) {
	
			mensaje = "Error procesando el archivo XML";
		}

	// obtiene los id del proceso y de la instancia que se muestran el reporte
	String processId = historyReport.getSelectedProcessId();
	if (processId!="")
		instanceId = historyReport.getInstanceIdMap().get(processId);
	
	// crea el objeto para generar y mostrar codigo html
	Html html = new Html();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PPINot Reporter</title>

<link rel="stylesheet" type="text/css"	href="js/ext-4.0.2/resources/css/ext-all.css" />
<script type="text/javascript"	src="js/ext-4.0.2/adapter/ext/ext-base.js"></script>
<script type="text/javascript"	src="js/ext-4.0.2/ext-all.js"></script>

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
			&nbsp;PPINot Reporter
		</div>
	</div>
</div>
<div id="content">
	<div id="main">

    	<h1>Reporte PPIs</h1>

		<form name="appform" method="post" action="index.jsp">
		<% 
			// muestra la lista para seleccionar un xml
			html.printStartSection(out, false);
			html.printLabel(out, "Archivo XML:");
			html.printStringSelect(out, "nomFich", nomFich, "onchange=\"document.appform.submit();\"", xmlMap);
			html.printLabel(out, "&nbsp;"+mensaje);
			html.printEndSection(out);

			// muestra el id del proceso seleccionado
			html.printStartSection(out, false);
			html.printLabel(out, "Proceso: "+processId);
			html.printEndSection(out);

			html.abrirTab1(out);

			// SI HAY UN PROCESO SELECCIONADO, MUESTRA EL REPORTE DE LOS PPI PARA ESE PROCESO
			if (processId!="") {
			
				if (historyReport.getTimeAggregatedPpiMap().get(processId).size()!=0 ||
						historyReport.getCountAggregatedPpiMap().get(processId).size()!=0 ||
						historyReport.getStateConditionAggregatedPpiMap().get(processId).size()!=0 ||
						historyReport.getDataAggregatedPpiMap().get(processId).size()!=0 ||
						historyReport.getDataPropertyConditionAggregatedPpiMap().get(processId).size()!=0 ||
						historyReport.getDerivedMultiInstancePpiMap().get(processId).size()!=0 )
					html.printHeader(out, "-aggr");
	
				// MUESTRA LOS PPI DE PROCESO EN EL XML
				// inicializa el mapa con los PPI de proceso que pueden aparecer en el reporte
				
				Iterator<Entry<String, PPI>> itInst = historyReport.getTimeAggregatedPpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
					
					String ppiId = HistoryConst.TIMEMEASUREAGGR;
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							aggregatedMeasure.getCond(), 
							ppiId, 
							aggregatedMeasure.getId(), 
							ppi.getValueString(), 
							aggregatedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark());
				}
				
				itInst = historyReport.getCountAggregatedPpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
	
					String ppiId = HistoryConst.COUNTMEASUREAGGR;
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							aggregatedMeasure.getCond(), 
							ppiId, 
							aggregatedMeasure.getId(), 
							ppi.getValueString(), 
							aggregatedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark());
				}
				
			    itInst = historyReport.getStateConditionAggregatedPpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
	
					String ppiId = HistoryConst.STATECONDITIONMEASUREAGGR;
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							aggregatedMeasure.getCond(), 
							ppiId, 
							aggregatedMeasure.getId(), 
							ppi.getValueString(), 
							aggregatedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark());
				}
				
			    itInst = historyReport.getDataAggregatedPpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
	
					String ppiId = HistoryConst.DATAMEASUREAGGR;
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							aggregatedMeasure.getCond(), 
							ppiId, 
							aggregatedMeasure.getId(), 
							ppi.getValueString(), 
							aggregatedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark());
				}
				
			    itInst = historyReport.getDataPropertyConditionAggregatedPpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
	
					String ppiId = HistoryConst.DATAPROPERTYCONDITIONMEASUREAGGR;
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							aggregatedMeasure.getCond(), 
							ppiId, 
							aggregatedMeasure.getId(), 
							ppi.getValueString(), 
							aggregatedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark());
				}
				
			    itInst = historyReport.getDerivedMultiInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        DerivedMultiInstanceMeasure derivedMeasure = (DerivedMultiInstanceMeasure) ppi.getMeasuredBy();
	
					String ppiId = HistoryConst.DERIVEDMULTIINSTANCEMEASURE;
			        
					// muestra la medida derivada
				    html.printPpiValue(out, 
				    		derivedMeasure.getCond(), 
				    		ppiId, 
				    		derivedMeasure.getId(), 
				    		ppi.getValueString(), 
				    		derivedMeasure.getUnitOfMeasure(), 
				    		ppi.getSuccess(), 
				    		ppi.getToMark());
				}
				
				if (historyReport.getProcessSuccessMap().get(processId)!=null)
					html.printReportSuccess(out, String.valueOf(historyReport.getProcessSuccessMap().get(processId)));
			}
				
			html.abrirTab2(out);
			
			if (processId!="") {
				
				// permite seleccionar una instancia del proceso seleccionado
				html.printInstances(out, 
						HistoryConst.INSTANCEID_VARNAME, 
						instanceId, 
						HistoryData.getProcessInstanceData( processId, null, null, true, true ) );
					
				// SI HAY UNA INSTANCIA SELECCIONADA, MUESTRA LOS PPI DE INSTANCIAS EN EL XML
				if (instanceId!="") {
	
					if (historyReport.getTimeInstancePpiMap().get(processId).size()!=0 ||
							historyReport.getCountInstancePpiMap().get(processId).size()!=0 ||
							historyReport.getStateConditionInstancePpiMap().get(processId).size()!=0 ||
							historyReport.getDataInstancePpiMap().get(processId).size()!=0 ||
							historyReport.getDataPropertyConditionInstancePpiMap().get(processId).size()!=0 ||
							historyReport.getDerivedSingleInstancePpiMap().get(processId).size()!=0 )
						html.printHeader(out, "");
						
					// PARA CADA UNO DE LOS PPI SE MUESTRAN SUS VALORES 
					Iterator<Entry<String, PPI>> itInst = historyReport.getTimeInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
				        
				        TimeInstanceMeasure instanceMeasure = (TimeInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.TIMEMEASURE;
						
						// muestra el valor del PPI
						html.printPpiValue(out, 
								instanceMeasure.getCond(), 
								ppiId, 
								instanceMeasure.getId(), 
								ppi.getValueString(), 
								instanceMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					itInst = historyReport.getCountInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
				        
				        CountInstanceMeasure instanceMeasure = (CountInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.COUNTMEASURE;
						
						// muestra el valor del PPI
						html.printPpiValue(out, 
								instanceMeasure.getCond(), 
								ppiId, 
								instanceMeasure.getId(), 
								ppi.getValueString(), 
								instanceMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					itInst = historyReport.getStateConditionInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
				        
				        StateConditionInstanceMeasure instanceMeasure = (StateConditionInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.STATECONDITIONMEASURE;
						
						// muestra el valor del PPI
						html.printPpiValue(out, 
								instanceMeasure.getCond(), 
								ppiId, 
								instanceMeasure.getId(), 
								ppi.getValueString(), 
								instanceMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					itInst = historyReport.getDataInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
				        
				        DataInstanceMeasure instanceMeasure = (DataInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.DATAMEASURE;
						
						// muestra el valor del PPI
						html.printPpiValue(out, 
								instanceMeasure.getCond(), 
								ppiId, 
								instanceMeasure.getId(), 
								ppi.getValueString(), 
								instanceMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					itInst = historyReport.getDataPropertyConditionInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
					        
				        DataPropertyConditionInstanceMeasure instanceMeasure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.DATAPROPERTYCONDITIONMEASURE;
						
						// muestra el valor del PPI
						html.printPpiValue(out, 
								instanceMeasure.getCond(), 
								ppiId, 
								instanceMeasure.getId(), 
								ppi.getValueString(), 
								instanceMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					itInst = historyReport.getDerivedSingleInstancePpiMap().get(processId).entrySet().iterator();
				    while (itInst.hasNext()) {
				        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
				        PPI ppi = (PPI) pairs.getValue();
					        
				        DerivedSingleInstanceMeasure derivedMeasure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();
	
						String ppiId = HistoryConst.DERIVEDSINGLEINSTANCEMEASURE;
				        
						// muestra el valor la medida derivada
						html.printPpiValue(out, 
								derivedMeasure.getCond(), 
								ppiId, 
								derivedMeasure.getId(), 
								ppi.getValueString(), 
								derivedMeasure.getUnitOfMeasure(), 
								ppi.getSuccess(), 
								ppi.getToMark());
					}
					
					if (historyReport.getInstanceSuccessMap().get(processId)!=null)
						html.printReportSuccess(out, String.valueOf(historyReport.getInstanceSuccessMap().get(processId)));
				} 
			
			} 
			
			html.abrirTab3(out);
			
			// permite seleccionar un ppi de instancia
			html.printStringSelect(out, 
					HistoryConst.MEASUREID_VARNAME, 
					measureId, 
					"onchange=\"document.appform.submit();\"",
					historyReport.getMeasureIdMap() );
				
			// SI HAY UNA MEDIDA SELECCIONADA, MUESTRA SUS VALORES PARA TODAS LAS INSTANCIAS DEL PROCESO
			if (measureId!="") {

			}
			
			html.cerrarTabs(out);
			
			html.printJavascript(out, processId, request.getParameter("tabactivo"), request.getParameter("openwindow"), true);
		%>
		</form> 
	</div>
</div>
</body>
</html>