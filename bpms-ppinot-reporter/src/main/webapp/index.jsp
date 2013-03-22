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
<%
	// obtiene el camino de la carpeta a donde se suben los xml
	String uploadPath = (String) request.getSession().getAttribute("UPLOADPATH");
	if (uploadPath==null) {
		
		XMLConfiguration xmlConfig = new XMLConfiguration("ppinotreporter.cfg.xml");
		uploadPath = xmlConfig.getString("uploadpath");
		request.getSession().setAttribute("UPLOADPATH", uploadPath);
	}

	// obtiene la lista de los archivos en la carpeta
	File destino = new File(uploadPath);
	if (!destino.exists())
		destino.mkdirs();
	String [] xmlLista = destino.list(); 

	// obtiene el objeto para realizar los reportes a partir de la historia de la ejecucion de los procesos
	HistoryReport historyReport = (HistoryReport) request.getSession().getAttribute("historyReport");
	if (historyReport==null) {
		historyReport = new HistoryReport();
	}

	// obtiene la informacion de la solicitud del usuario
	String nomFich = request.getParameter("nomFich");
	if (nomFich==null) nomFich = "";

	String nomFichSave = request.getParameter("nomFichSave");
	if (nomFichSave==null) nomFichSave = "";

	String guardar = request.getParameter("guardar");

	// realiza la accion solicitada por el usuario
	String mensaje = "";
	if (guardar!=null) {
		
		if (nomFichSave!="") {
	
	// exporta a xml
	historyReport.xmlExport(uploadPath, nomFichSave);
		}
	} else {
		
		if (nomFich=="") {
	
	// hace el reporte a partir de las solicitudes en la interfaz de la aplicacion
	historyReport.doReportFromRequest(request);
		}
		else
		try {

	// hace el reporte a partir del archivo xml indicado
	historyReport.doReportFromXML(request, uploadPath, nomFich);
	request.getSession().setAttribute("antProcId", historyReport.getSelectedProcessId());
	mensaje = "Achivo XML procesado correctamente";
		} catch (Exception ex) {
	
	mensaje = "Error procesando el archivo XML";
		}
	}
	
	Boolean modify = true;
	
	// crea el objeto para generar y mostrar codigo html
	Html html = new Html();
%>
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
<div id="navigation">
<div id="template_x002e_navigation_x002e_divided-left" class="activiti-component">
<ul class="activiti-menu pages">
  <li>
    <a href="index.jsp" class="current ">Inicio</a>
  </li>
  <li>
    <a href="upload.jsp" class="normal last">Administrar XML</a>
  </li>
</ul>
</div>
</div>
<div id="content">
	<div id="main">

    	<h1>Reporte PPIs</h1>

		<form name="xmlappform" method="post" action="index.jsp">

		<div class="section">
		<label class="etiqueta">Archivo XML:</label>
		<select name="nomFich" onchange="document.xmlappform.submit();">
			<option value="">Seleccione</option>
		<%
			if (xmlLista!=null)
				    for (String xml : xmlLista) {
				
				out.println("<option value=\"" + xml + "\" " );
				if (nomFich!=null && xml.contentEquals(nomFich))
					out.println("selected");
				out.println(">" + xml + "</option>");
				    }
		%>
		</select>
		
		&nbsp;<label class="etiqueta"><%
					out.println(mensaje);
				%></label>
		</div>
		</form>

		<form name="saveappform" method="post" action="index.jsp">

		<div class="section">
		<label class="etiqueta">Guardar como:</label>
		<input name="nomFichSave" type="text" />
		<input name="guardar" type="submit" value="Guardar" />
		</div>
		</form>
		
		<form name="appform" method="post" action="index.jsp">

		<%
			// PERMITE SELECCIONAR UNO DE LOS PROCESOS DESPLEGADOS EN LA PLATAFORMA BPM
				// muestra el select con los ids en el XML de los procesos desplegados
				String processId = historyReport.getSelectedProcessId();
				
				// el html de los contenedores de las ventanas que se muestra despues del formulario
				List<String> htmlList = new ArrayList<String>();
				
				// inicializa un mapa para los select de si o no
				Map<String, String> sinoMap = new LinkedHashMap<String, String>();
				sinoMap.put("1", "S&iacute;");
				sinoMap.put("0", "No");
				
				// inicializa el mapa con los posibles estados de una tarea
				Map<String, String> taskStateMap = HistoryData.getStateTaskNames();

				// inicializa el mapa para los select de funciones de las medidas derivadas
				Map<String, String> derFuncMap = new LinkedHashMap<String, String>();
				derFuncMap.put("Cuadrado", "Cuadrado");
				derFuncMap.put("Doble", "Doble");
				
				html.abrirTab1(out);

				html.printProcesses(out, HistoryConst.PROCESSID_VARNAME, processId, HistoryData.getProcessNames());

				// SI HAY UN PROCESO SELECCIONADO, PERMITE MOSTRAR EL REPORTE DE LOS PPI PARA ESE PROCESO
				if (processId!="") {
			
			// PERMITE SELECCIONAR PPI AGREGADOS
			// inicializa el mapa con los PPI agregados que pueden aparecer en el reporte
			Map<String, String> ppiMapAggr = new LinkedHashMap<String, String>();
			ppiMapAggr.put(String.valueOf(HistoryConst.TIMEMEASUREAGGR), "TimeMeasure");
			ppiMapAggr.put(String.valueOf(HistoryConst.COUNTMEASUREAGGR), "CountMeasure");
			ppiMapAggr.put(String.valueOf(HistoryConst.STATECONDITIONMEASUREAGGR), "StateConditionMeasure");
			ppiMapAggr.put(String.valueOf(HistoryConst.DATAMEASUREAGGR), "DataMeasure");
			ppiMapAggr.put(String.valueOf(HistoryConst.DATAPROPERTYCONDITIONMEASUREAGGR), "DataProppertyConditionMeasure");
			ppiMapAggr.put(String.valueOf(HistoryConst.DERIVEDMULTIINSTANCEMEASURE), "DerivedMultiInstanceMeasure");

			// obtiene la informacion de las actividades en el proceso seleccionado, para generar
			// los select en los cuales se escoge una actividad
			Map<String, String> activityMapAggr = HistoryData.getProcessActivityNames(processId);

			// obtiene la informacion de las actividades en el proceso seleccionado, para generar
			// los select en los cuales se escoge una tarea
			Map<String, String> taskMapAggr = HistoryData.getProcessTaskNames(processId);

			// obtiene la informacion de los dataobjects en el proceso seleccionado, para generar
			// los select en los cuales se escoge un dataobject
			Map<String, String> dataMapCondAggr = HistoryData.getProcessDataNames(processId);

			// inicializa los mapas para los select de funciones de las medidas agregadas
			Map<String, String> funcMap1 = new LinkedHashMap<String, String>();
			funcMap1.put(HistoryConst.MIN, "MinAM");
			funcMap1.put(HistoryConst.MAX, "MaxAM");
			funcMap1.put(HistoryConst.AVE, "AverageAM");
			funcMap1.put(HistoryConst.SUM, "SumAM");

			Map<String, String> funcMap2 = new LinkedHashMap<String, String>();
			funcMap2.put(HistoryConst.COUNT, "CountAM");
			
			Map<String, String> funcMap3 = new LinkedHashMap<String, String>();
			funcMap3.put(HistoryConst.MIN, "MinAM");
			funcMap3.put(HistoryConst.MAX, "MaxAM");
			funcMap3.put(HistoryConst.AVE, "AverageAM");
			funcMap3.put(HistoryConst.SUM, "SumAM");
			funcMap3.put(HistoryConst.LAST, "LastAM");
			
			// inicializa el mapa para el select del año
			Map<String, String> yearMap = new LinkedHashMap<String, String>();
			yearMap.put("intervalo", "Intervalo de fechas");
			for (int year=PpiNotModelUtils.currentYear(); year>=PpiNotModelUtils.STARTYEAR; year--) {
				
				yearMap.put(Integer.toString(year), Integer.toString(year));
			}
			
			// inicializa el mapa para el select del periodo
			Map<String, String> periodMap = new LinkedHashMap<String, String>();
			periodMap.put("mes", "Mes");
			periodMap.put("trimestre", "Trimestre");
			periodMap.put("semestre", "Semestre");
			
			// DA LA POSIBILIDAD DE SELECCIONAR UN NUEVO PPI AGREGADO
			html.printStartSection(out, !modify);
			html.printLabel(out, "Adicionar PPI");
			
			html.printStringSelect(out, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME, "", "onchange=\"accion(false);\"", ppiMapAggr);

			html.printEndSection(out);
			
			if (historyReport.getTimeAggregatedPpiMap().get(processId).size()!=0 ||
					historyReport.getCountAggregatedPpiMap().get(processId).size()!=0 ||
					historyReport.getStateConditionAggregatedPpiMap().get(processId).size()!=0 ||
					historyReport.getDataAggregatedPpiMap().get(processId).size()!=0 ||
					historyReport.getDataPropertyConditionAggregatedPpiMap().get(processId).size()!=0 ||
					historyReport.getDerivedMultiInstancePpiMap().get(processId).size()!=0 )
				html.printHeader(out, "-aggr");

			// MUESTRA LOS PPI AGREGADOS YA SELECCIONADOS
			Map<String, String> measureMap = new HashMap<String, String>();
			
			int i = 1;
			Iterator<Entry<String, PPI>> itInst = historyReport.getTimeAggregatedPpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
				
				String idmap = aggregatedMeasure.getId();
				measureMap.put(idmap, idmap);
				
				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				String ppiId = Integer.toString(HistoryConst.TIMEMEASUREAGGR);
				
				TimeInstanceMeasure instanceMeasure = (TimeInstanceMeasure) aggregatedMeasure.getBaseMeasure();
				
				// muestra el valor del PPI
				html.printPpiValue(out, 
						"-aggr", 
						HistoryConst.AGGR_PREFIX + i, 
						aggregatedMeasure.getCond(), 
						type+": "+ppiMapAggr.get(ppiId), 
						aggregatedMeasure.getName()+": "+instanceMeasure.getName(), 
						ppi.getValueString(), 
						aggregatedMeasure.getUnitOfMeasure(), 
						ppi.getSuccess(), 
						ppi.getToMark(), modify);
				
				// abre el formulario para modificar el ppi
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr, 
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, aggregatedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, aggregatedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
				
				htmlList.addAll( html.generateAggregatedMeasure( i,
						HistoryConst.AGGRFUNC_PREFIX + i, aggregatedMeasure.getAggregationFunction(),
						HistoryConst.AGGRYEAR_PREFIX + i, ppi.getScope().getYear(),
						HistoryConst.AGGRPERIOD_PREFIX + i, ppi.getScope().getPeriod(),
						HistoryConst.STARTDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getStartDate() ),
						HistoryConst.INSTART_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInStart()),
						HistoryConst.ENDDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getEndDate() ),
						HistoryConst.INEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInEnd()),
						funcMap1, sinoMap, yearMap, periodMap) );
				
				htmlList.addAll( html.generateCommonName(HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i, instanceMeasure.getName()) ); 
				htmlList.addAll( html.generateTimeMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.INI_PREFIX + i, instanceMeasure.getFrom().getAppliesTo(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.FIN_PREFIX + i, instanceMeasure.getTo().getAppliesTo(),
						HistoryConst.AGGR_PREFIX + HistoryConst.ATENDINI_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getFrom().getChangesToState().getState()==GenericState.END), 
						HistoryConst.AGGR_PREFIX + HistoryConst.ATENDFIN_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getTo().getChangesToState().getState()==GenericState.END), 
						activityMapAggr, sinoMap) );
				
				// cierra el formulario para modificar el ppi
				htmlList.addAll( html.generateCloseWindow(modify) );

				i++;
			}
			
			itInst = historyReport.getCountAggregatedPpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        
		        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

				String idmap = aggregatedMeasure.getId();
				measureMap.put(idmap, idmap);

				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				String ppiId = Integer.toString(HistoryConst.COUNTMEASUREAGGR);

				CountInstanceMeasure instanceMeasure = (CountInstanceMeasure) aggregatedMeasure.getBaseMeasure();
				
				// muestra el valor del PPI
				html.printPpiValue(out, 
						"-aggr", 
						HistoryConst.AGGR_PREFIX + i, 
						aggregatedMeasure.getCond(), type+": "+ppiMapAggr.get(ppiId), 
						aggregatedMeasure.getName()+": "+instanceMeasure.getName(), 
						ppi.getValueString(), 
						aggregatedMeasure.getUnitOfMeasure(), 
						ppi.getSuccess(), 
						ppi.getToMark(), 
						modify);
				
				// abre el formulario para modificar el ppi
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr, 
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, aggregatedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, aggregatedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
				
				htmlList.addAll( html.generateAggregatedMeasure( i,
						HistoryConst.AGGRFUNC_PREFIX + i, aggregatedMeasure.getAggregationFunction(),
						HistoryConst.AGGRYEAR_PREFIX + i, ppi.getScope().getYear(),
						HistoryConst.AGGRPERIOD_PREFIX + i, ppi.getScope().getPeriod(),
						HistoryConst.STARTDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getStartDate() ),
						HistoryConst.INSTART_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInStart()),
						HistoryConst.ENDDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getEndDate() ),
						HistoryConst.INEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInEnd()),
						funcMap1, sinoMap, yearMap, periodMap) );
				
				htmlList.addAll( html.generateCommonName( HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i, instanceMeasure.getName()) ); 
				htmlList.addAll( html.generateCountMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.COUNT_PREFIX + i, instanceMeasure.getWhen().getAppliesTo(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.ATEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getWhen().getChangesToState().getState()==GenericState.END), 
						activityMapAggr, sinoMap) );
				
				// cierra el formulario para modificar el ppi
				htmlList.addAll( html.generateCloseWindow(modify) );

				i++;
			}
			
		    itInst = historyReport.getStateConditionAggregatedPpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        AggregatedMeasure timeAggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();
		        
		        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

				String idmap = aggregatedMeasure.getId();
				measureMap.put(idmap, idmap);

				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				String ppiId = Integer.toString(HistoryConst.STATECONDITIONMEASUREAGGR);

				StateConditionInstanceMeasure instanceMeasure = (StateConditionInstanceMeasure) aggregatedMeasure.getBaseMeasure();
				
				// muestra el valor del PPI
				html.printPpiValue(out, 
						"-aggr", 
						HistoryConst.AGGR_PREFIX + i, 
						aggregatedMeasure.getCond(), 
						type+": "+ppiMapAggr.get(ppiId), 
						aggregatedMeasure.getName()+": "+instanceMeasure.getName(), 
						ppi.getValueString(), 
						aggregatedMeasure.getUnitOfMeasure(), 
						ppi.getSuccess(), 
						ppi.getToMark(), 
						modify);
				
				// abre el formulario para modificar el ppi
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr,
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, aggregatedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, aggregatedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
				
				htmlList.addAll( html.generateAggregatedMeasure(i,
						HistoryConst.AGGRFUNC_PREFIX + i, aggregatedMeasure.getAggregationFunction(),
						HistoryConst.AGGRYEAR_PREFIX + i, ppi.getScope().getYear(),
						HistoryConst.AGGRPERIOD_PREFIX + i, ppi.getScope().getPeriod(),
						HistoryConst.STARTDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getStartDate() ),
						HistoryConst.INSTART_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInStart()),
						HistoryConst.ENDDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getEndDate() ),
						HistoryConst.INEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInEnd()),
						funcMap2, sinoMap, yearMap, periodMap) );
				
				htmlList.addAll( html.generateCommonName( HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i, instanceMeasure.getName()) ); 
				htmlList.addAll( html.generateElementConditionMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.ELEMENT_PREFIX + i , instanceMeasure.getCondition().getAppliesTo(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.STATEELEMENT_PREFIX + i, instanceMeasure.getCondition().getState().getStateString(), 
						taskMapAggr, taskStateMap) );
				
				// cierra el formulario para modificar el ppi
				htmlList.addAll( html.generateCloseWindow(modify) );

				i++;
			}
			
		    itInst = historyReport.getDataAggregatedPpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        
		        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

				String idmap = aggregatedMeasure.getId();
				measureMap.put(idmap, idmap);

				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				String ppiId = Integer.toString(HistoryConst.DATAMEASUREAGGR);
				
				DataInstanceMeasure instanceMeasure = (DataInstanceMeasure) aggregatedMeasure.getBaseMeasure();
				Map<String, String> dataPropMap = new LinkedHashMap<String, String>();
				String dataIdData = instanceMeasure.getDataContentSelection().getDataobject();
				if (dataIdData!=null && dataIdData!="") {
					dataPropMap = HistoryData.getDataobjectPropNames(processId, dataIdData);
				}
				
				// muestra el valor del PPI
				html.printPpiValue(out, 
						"-aggr", 
						HistoryConst.AGGR_PREFIX + i, 
						aggregatedMeasure.getCond(), 
						type+": "+ppiMapAggr.get(ppiId), 
						aggregatedMeasure.getName()+": "+instanceMeasure.getName(), 
						ppi.getValueString(), 
						aggregatedMeasure.getUnitOfMeasure(), 
						ppi.getSuccess(), 
						ppi.getToMark(), 
						modify);
				
				// abre el formulario para modificar el ppi
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr, 
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, aggregatedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, aggregatedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
				
				htmlList.addAll( html.generateAggregatedMeasure( i,
						HistoryConst.AGGRFUNC_PREFIX + i, aggregatedMeasure.getAggregationFunction(),
						HistoryConst.AGGRYEAR_PREFIX + i, ppi.getScope().getYear(),
						HistoryConst.AGGRPERIOD_PREFIX + i, ppi.getScope().getPeriod(),
						HistoryConst.STARTDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getStartDate() ),
						HistoryConst.INSTART_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInStart()),
						HistoryConst.ENDDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getEndDate() ),
						HistoryConst.INEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInEnd()),
						funcMap3, sinoMap, yearMap, periodMap) );
				
				htmlList.addAll( html.generateCommonName( HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i, instanceMeasure.getName()) ); 
				htmlList.addAll( html.generateDataMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.DATA_PREFIX + i, dataIdData, 
						HistoryConst.AGGR_PREFIX + HistoryConst.DATAPROP_PREFIX + i, instanceMeasure.getDataContentSelection().getSelection(), 
						dataMapCondAggr, dataPropMap) );
				
				// cierra el formulario para modificar el ppi
				htmlList.addAll( html.generateCloseWindow(modify) );

				i++;
			}
			
		    itInst = historyReport.getDataPropertyConditionAggregatedPpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        
		        AggregatedMeasure aggregatedMeasure = (AggregatedMeasure) ppi.getMeasuredBy();

				String idmap = aggregatedMeasure.getId();
				measureMap.put(idmap, idmap);

				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				String ppiId = Integer.toString(HistoryConst.DATAPROPERTYCONDITIONMEASUREAGGR);
				
				DataPropertyConditionInstanceMeasure instanceMeasure = (DataPropertyConditionInstanceMeasure) aggregatedMeasure.getBaseMeasure();
				Map<String, String> dataStateMap = new LinkedHashMap<String, String>();
				String dataIdDataCond = instanceMeasure.getCondition().getDataobject();
				if (dataIdDataCond!=null && dataIdDataCond!="") {
					dataStateMap = HistoryData.getDataobjectStateNames(processId, dataIdDataCond);
				}
				
				// muestra el valor del PPI
				html.printPpiValue(out, 
						"-aggr", 
						HistoryConst.AGGR_PREFIX + i, 
						aggregatedMeasure.getCond(), 
						type+": "+ppiMapAggr.get(ppiId), 
						aggregatedMeasure.getName()+": "+instanceMeasure.getName(), 
						ppi.getValueString(), 
						aggregatedMeasure.getUnitOfMeasure(), 
						ppi.getSuccess(), 
						ppi.getToMark(), 
						modify);
				
				// abre el formulario para modificar el ppi
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr,
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, aggregatedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, aggregatedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
				
				htmlList.addAll( html.generateAggregatedMeasure( i,
						HistoryConst.AGGRFUNC_PREFIX + i, aggregatedMeasure.getAggregationFunction(),
						HistoryConst.AGGRYEAR_PREFIX + i, ppi.getScope().getYear(),
						HistoryConst.AGGRPERIOD_PREFIX + i, ppi.getScope().getPeriod(),
						HistoryConst.STARTDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getStartDate() ),
						HistoryConst.INSTART_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInStart()),
						HistoryConst.ENDDATE_PREFIX + i, PpiNotModelUtils.formatString( ppi.getScope().getEndDate() ),
						HistoryConst.INEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(ppi.getScope().getInEnd()),
						funcMap2, sinoMap, yearMap, periodMap) );

				htmlList.addAll( html.generateCommonName( HistoryConst.AGGRINST_PREFIX + HistoryConst.NAME_PREFIX + i, instanceMeasure.getName()) ); 
				htmlList.addAll( html.generateDataConditionMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.DATACOND_PREFIX + i, dataIdDataCond, 
						HistoryConst.AGGR_PREFIX + HistoryConst.DATASTATE_PREFIX + i, instanceMeasure.getCondition().getStateConsidered().getStateString(), 
						dataMapCondAggr, dataStateMap) );
				
				// cierra el formulario para modificar el ppi
				htmlList.addAll( html.generateCloseWindow(modify) );

				i++;
			}
			
		    itInst = historyReport.getDerivedMultiInstancePpiMap().get(processId).entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
		        PPI ppi = (PPI) pairs.getValue();
		        
		        DerivedMultiInstanceMeasure derivedMeasure = (DerivedMultiInstanceMeasure) ppi.getMeasuredBy();

				// obtiene informacion para mostrar el ppi
				String type = "PPI";
				
				String idOper1 = "";
				String idOper2 = "";
				String medOper1 = "";
				String medOper2 = "";
				Integer cantUsed = 0;
				
				// muestra la medida derivada
				String ppiId = Integer.toString(HistoryConst.DERIVEDMULTIINSTANCEMEASURE);
			    html.printPpiValue(out, 
			    		"-aggr", 
			    		HistoryConst.AGGR_PREFIX + i, 
			    		derivedMeasure.getCond(), type+": "+ppiMapAggr.get(ppiId), 
			    		derivedMeasure.getName(), 
			    		ppi.getValueString(), 
			    		derivedMeasure.getUnitOfMeasure(), 
			    		ppi.getSuccess(), 
			    		ppi.getToMark(), 
			    		modify);

				// abre el formulario para modificar la medida derivada
				htmlList.addAll( html.generateOpenWindow( HistoryConst.AGGR_PREFIX + i ) );
				
				// muestra los controles para introducir los datos
				htmlList.addAll( html.generateCommonData(
						type, HistoryConst.AGGR_PREFIX + HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMapAggr,
						HistoryConst.AGGR_PREFIX + HistoryConst.ID_PREFIX + i, derivedMeasure.getId(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.NAME_PREFIX + i, derivedMeasure.getName(), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), 
						HistoryConst.AGGR_PREFIX + HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 

				htmlList.addAll( html.generateDerivedMeasure(
						HistoryConst.AGGR_PREFIX + HistoryConst.DERFUNC_PREFIX + i, derivedMeasure.getFunction(),
						derFuncMap,
						HistoryConst.AGGR_PREFIX + HistoryConst.IDOPER1_PREFIX + i, idOper1, 
						HistoryConst.AGGR_PREFIX + HistoryConst.MEDOPER1_PREFIX + i, medOper1,
						HistoryConst.AGGR_PREFIX + HistoryConst.IDOPER2_PREFIX + i, idOper2, 
						HistoryConst.AGGR_PREFIX + HistoryConst.MEDOPER2_PREFIX + i, medOper2, 
						measureMap) );
				
				// cierra el formulario para modificar la medida derivada
				htmlList.addAll( html.generateCloseWindow(modify) );
				
				i++;
			}
			
			if (historyReport.getProcessSuccessMap().get(processId)!=null)
				html.printReportSuccess(out, String.valueOf(historyReport.getProcessSuccessMap().get(processId)));
				}
				
				html.abrirTab2(out);
				
				if (processId!="") {
			
			String instanceId = historyReport.getInstanceIdMap().get(processId);

			// permite seleccionar una instancia del proceso seleccionado
			html.printInstances(out, 
					HistoryConst.INSTANCEID_VARNAME, 
					instanceId, 
					HistoryData.getProcessInstanceData( processId, null, null, true, true ) );
				
			// SI HAY UNA INSTANCIA SELECCIONADA, PERMITE SELECCIONAR LOS PPI DE INSTANCIAS QUE SE DESEAN INCLUIR EN EL REPORTE
			if (instanceId!="") {

				// inicializa el mapa con los PPI que pueden aparecer en el reporte
				Map<String, String> ppiMap = new LinkedHashMap<String, String>();
				ppiMap.put(String.valueOf(HistoryConst.ACTIVITYSTART), "ActivityStart");
				ppiMap.put(String.valueOf(HistoryConst.ACTIVITYEND), "ActivityEnd");
				ppiMap.put(String.valueOf(HistoryConst.POOLSTART), "PoolStart");
				ppiMap.put(String.valueOf(HistoryConst.POOLEND), "PoolEnd");
				ppiMap.put(String.valueOf(HistoryConst.TIMEMEASURE), "TimeMeasure");
				ppiMap.put(String.valueOf(HistoryConst.COUNTMEASURE), "CountMeasure");
				ppiMap.put(String.valueOf(HistoryConst.STATECONDITIONMEASURE), "ElementConditionMeasure");
				ppiMap.put(String.valueOf(HistoryConst.DATAMEASURE), "DataMeasure");
				ppiMap.put(String.valueOf(HistoryConst.DATAPROPERTYCONDITIONMEASURE), "DataConditionMeasure");
				ppiMap.put(String.valueOf(HistoryConst.DERIVEDSINGLEINSTANCEMEASURE), "DerivedInstanceMeasure");

				// obtiene la informacion de las actividades en la instancia de proceso seleccionada, para generar
				// los select en los cuales se escoge una actividad
				Map<String, String> activityMap = HistoryData.getInstanceActivityNames(instanceId);
				
				// DA LA POSIBILIDAD DE SELECCIONAR UN NUEVO PPI
				html.printStartSection(out, !modify);
				html.printLabel(out, "Adicionar TIC o PPI");
				
				html.printStringSelect(out, HistoryConst.PPIID_VARNAME, "", "onchange=\"accion(false);\"", ppiMap);
			
				html.printEndSection(out);

				if (historyReport.getActivityStartEndList().get(processId).size()!=0 ||
						historyReport.getPoolStartEndList().get(processId).size()!=0 ||
						historyReport.getTimeInstancePpiMap().get(processId).size()!=0 ||
						historyReport.getCountInstancePpiMap().get(processId).size()!=0 ||
						historyReport.getStateConditionInstancePpiMap().get(processId).size()!=0 ||
						historyReport.getDataInstancePpiMap().get(processId).size()!=0 ||
						historyReport.getDataPropertyConditionInstancePpiMap().get(processId).size()!=0 ||
						historyReport.getDerivedSingleInstancePpiMap().get(processId).size()!=0 )
					html.printHeader(out, "");
					
				// PARA CADA UNO DE LOS PPI QUE HAYAN SIDO SELECCIONADOS, SE MUESTRAN LOS CONTROLES QUE PERMITEN INDICAR LOS
				// VALORES PARA CALCULARLOS
				int i = 1;
				Map<String, String> measureMap = new HashMap<String, String>();
				for(ActivityStartEnd activityStartEnd : historyReport.getActivityStartEndList().get(processId)) {

					// obtiene informacion para mostrar el ppi
					String type = "TIC";
					String ppiId;
					if (activityStartEnd.getAtEnd())
						ppiId = Integer.toString(HistoryConst.ACTIVITYEND);
					else
						ppiId = Integer.toString(HistoryConst.ACTIVITYSTART);
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							activityStartEnd.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							activityStartEnd.getName(), 
							activityStartEnd.getValueString(), 
							"", 
							activityStartEnd.getSuccess(), 
							activityStartEnd.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonType(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.NAME_PREFIX + i, activityStartEnd.getName()) ); 
					
					htmlList.addAll( html.generateActivityStartEnd( 
							HistoryConst.STARTEND_PREFIX + i, activityStartEnd.getId(), 
							activityMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );
					
					i++;
				}

				for(PoolStartEnd poolTimeInstanceDef : historyReport.getPoolStartEndList().get(processId)) {

					// obtiene informacion para mostrar el ppi
					String type = "TIC";
					String ppiId;
					if (poolTimeInstanceDef.getAtEnd())
						ppiId = Integer.toString(HistoryConst.POOLEND);
					else
						ppiId = Integer.toString(HistoryConst.POOLSTART);
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							poolTimeInstanceDef.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							poolTimeInstanceDef.getName(), 
							poolTimeInstanceDef.getValueString(), 
							"", 
							poolTimeInstanceDef.getSuccess(), 
							poolTimeInstanceDef.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonType(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.NAME_PREFIX + i, poolTimeInstanceDef.getName()) ); 
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				Iterator<Entry<String, PPI>> itInst = historyReport.getTimeInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        TimeInstanceMeasure instanceMeasure = (TimeInstanceMeasure) ppi.getMeasuredBy();

					String idmap = instanceMeasure.getId();
					measureMap.put(idmap, idmap);

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					String ppiId = Integer.toString(HistoryConst.TIMEMEASURE);
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							instanceMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							instanceMeasure.getName(), 
							ppi.getValueString(), 
							instanceMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.ID_PREFIX + i, instanceMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, instanceMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateTimeMeasure(
							HistoryConst.INI_PREFIX + i, instanceMeasure.getFrom().getAppliesTo(), 
							HistoryConst.FIN_PREFIX + i, instanceMeasure.getTo().getAppliesTo(),
							HistoryConst.ATENDINI_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getFrom().getChangesToState().getState()==GenericState.END), 
							HistoryConst.ATENDFIN_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getTo().getChangesToState().getState()==GenericState.END), 
							activityMap, sinoMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				itInst = historyReport.getCountInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        CountInstanceMeasure instanceMeasure = (CountInstanceMeasure) ppi.getMeasuredBy();

					String idmap = instanceMeasure.getId();
					measureMap.put(idmap, idmap);

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					String ppiId = Integer.toString(HistoryConst.COUNTMEASURE);
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							instanceMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							instanceMeasure.getName(), 
							ppi.getValueString(), 
							instanceMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.ID_PREFIX + i, instanceMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, instanceMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateCountMeasure(
							HistoryConst.COUNT_PREFIX + i, instanceMeasure.getWhen().getAppliesTo(), 
							HistoryConst.ATEND_PREFIX + i, PpiNotModelUtils.booleanToStringInteger(instanceMeasure.getWhen().getChangesToState().getState()==GenericState.END), 
							activityMap, sinoMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				itInst = historyReport.getStateConditionInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        StateConditionInstanceMeasure instanceMeasure = (StateConditionInstanceMeasure) ppi.getMeasuredBy();

					String idmap = instanceMeasure.getId();
					measureMap.put(idmap, idmap);

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					String ppiId = Integer.toString(HistoryConst.STATECONDITIONMEASURE);

					Map<String, String> taskMap = HistoryData.getInstanceTaskNames(historyReport.getInstanceIdMap().get(processId));
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							instanceMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							instanceMeasure.getName(), 
							ppi.getValueString(), 
							instanceMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.ID_PREFIX + i, instanceMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, instanceMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateElementConditionMeasure(
							HistoryConst.ELEMENT_PREFIX + i , instanceMeasure.getCondition().getAppliesTo(), 
							HistoryConst.STATEELEMENT_PREFIX + i, instanceMeasure.getCondition().getState().getStateString(), 
							taskMap, taskStateMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				itInst = historyReport.getDataInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
			        
			        DataInstanceMeasure instanceMeasure = (DataInstanceMeasure) ppi.getMeasuredBy();

					String idmap = instanceMeasure.getId();
					measureMap.put(idmap, idmap);

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					String ppiId = Integer.toString(HistoryConst.DATAMEASURE);
					
					Map<String, String> dataMap = HistoryData.getInstanceDataNames(instanceId);
					Map<String, String> dataPropMap = new LinkedHashMap<String, String>();
					String dataIdData = instanceMeasure.getDataContentSelection().getDataobject();
					if (dataIdData!=null && dataIdData!="") {
						dataPropMap = HistoryData.getDataobjectPropNames(processId, dataIdData);
					}
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							instanceMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							instanceMeasure.getName(), 
							ppi.getValueString(), 
							instanceMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap, 
							HistoryConst.ID_PREFIX + i, instanceMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, instanceMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateDataMeasure(
							HistoryConst.DATA_PREFIX + i, dataIdData, 
							HistoryConst.DATAPROP_PREFIX + i, instanceMeasure.getDataContentSelection().getSelection(), 
							dataMap, dataPropMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				itInst = historyReport.getDataPropertyConditionInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
				        
			        DataPropertyConditionInstanceMeasure instanceMeasure = (DataPropertyConditionInstanceMeasure) ppi.getMeasuredBy();

					String idmap = instanceMeasure.getId();
					measureMap.put(idmap, idmap);

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					String ppiId = Integer.toString(HistoryConst.DATAPROPERTYCONDITIONMEASURE);
					
					Map<String, String> dataMapCond = HistoryData.getInstanceDataNames(instanceId);
					Map<String, String> dataStateMap = new LinkedHashMap<String, String>();
					String dataIdDataCond = instanceMeasure.getCondition().getDataobject();
					if (dataIdDataCond!=null && dataIdDataCond!="") {
						dataStateMap = HistoryData.getDataobjectStateNames(processId, dataIdDataCond);
					}
					
					// muestra el valor del PPI
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							instanceMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							instanceMeasure.getName(), 
							ppi.getValueString(), 
							instanceMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.ID_PREFIX + i, instanceMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, instanceMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateDataConditionMeasure(
							HistoryConst.DATACOND_PREFIX + i, dataIdDataCond, 
							HistoryConst.DATASTATE_PREFIX + i, instanceMeasure.getCondition().getStateConsidered().getStateString(), 
							dataMapCond, dataStateMap) );
					
					// cierra el formulario para modificar el ppi
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				itInst = historyReport.getDerivedSingleInstancePpiMap().get(processId).entrySet().iterator();
			    while (itInst.hasNext()) {
			        Map.Entry<String, PPI> pairs = (Map.Entry<String, PPI>)itInst.next();
			        PPI ppi = (PPI) pairs.getValue();
				        
			        DerivedSingleInstanceMeasure derivedMeasure = (DerivedSingleInstanceMeasure) ppi.getMeasuredBy();

					// obtiene informacion para mostrar el ppi
					String type = "PPI";
					
					String idOper1 = "";
					String idOper2 = "";
					String medOper1 = "";
					String medOper2 = "";
					Integer cantUsed = 0;
					
					// muestra el valor la medida derivada
					String ppiId = Integer.toString(HistoryConst.DERIVEDSINGLEINSTANCEMEASURE);
					html.printPpiValue(out, 
							"", 
							String.valueOf(i), 
							derivedMeasure.getCond(), 
							type+": "+ppiMap.get(ppiId), 
							derivedMeasure.getName(), 
							ppi.getValueString(), 
							derivedMeasure.getUnitOfMeasure(), 
							ppi.getSuccess(), 
							ppi.getToMark(), 
							modify);
					
					// abre el formulario para modificar el ppi
					htmlList.addAll( html.generateOpenWindow( String.valueOf(i) ) );
					
					// muestra los controles para introducir los datos
					htmlList.addAll( html.generateCommonData(
							type, HistoryConst.PPIID_VARNAME + i, ppiId, "onchange=\"accion(true);\"", ppiMap,
							HistoryConst.ID_PREFIX + i, derivedMeasure.getId(), 
							HistoryConst.NAME_PREFIX + i, derivedMeasure.getName(), 
							HistoryConst.REFMAX_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMax()), HistoryConst.REFMIN_PREFIX + i, PpiNotModelUtils.doubleToString(ppi.getTarget().getRefMin())) ); 
					
					htmlList.addAll( html.generateDerivedMeasure(
							HistoryConst.DERFUNC_PREFIX + i, derivedMeasure.getFunction(),
							derFuncMap,
							HistoryConst.IDOPER1_PREFIX + i, idOper1, 
							HistoryConst.MEDOPER1_PREFIX + i, medOper1,
							HistoryConst.IDOPER2_PREFIX + i, idOper2, 
							HistoryConst.MEDOPER2_PREFIX + i, medOper2,
							measureMap
							) );
					
					// cierra el formulario para modificar la medida derivada
					htmlList.addAll( html.generateCloseWindow(modify) );

					i++;
				}
				
				if (historyReport.getInstanceSuccessMap().get(processId)!=null)
					html.printReportSuccess(out, String.valueOf(historyReport.getInstanceSuccessMap().get(processId)));
			} 
			
				} 
				
				html.cerrarTabs(out);
				
				html.printJavascript(out, processId, request.getParameter("tabactivo"), request.getParameter("openwindow"), modify);
				
				// muestra los hidden que permiten enviar por post los valores de los campos que se encuentran en las ventanas
				html.printHidden(out);
		%>
		</form> 
		
		<%
		// muestra el html de los contenedores de las ventanas que se muestra despues del formulario
		html.print(out, htmlList );
		request.getSession().setAttribute("historyReport", historyReport);
		%>

	</div>
</div>
</body>
</html>