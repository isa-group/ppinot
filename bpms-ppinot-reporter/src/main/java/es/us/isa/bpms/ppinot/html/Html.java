package es.us.isa.bpms.ppinot.html;

import es.us.isa.bpms.ppinot.historyreport.db.ProcessEntity;
import es.us.isa.ppinot.handler.PpiNotModelUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * Clase para generar y mostrar codigo html en el pagina
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class Html {
	
	private Map<String, String> containerMap;
	
	private List<String> limitList;
	private List<String> nameList;
	private List<String> windowList;
	private List<String> fieldList;

	public Html() {
		
		super();
		this.setContainerMap(new LinkedHashMap<String, String>());
		this.setLimitList(new ArrayList<String>());
		this.setNameList(new ArrayList<String>());
		this.setWindowList(new ArrayList<String>());
		this.setFieldList(new ArrayList<String>());
	}

	public void setContainerMap(Map<String, String> containerMap) {
		this.containerMap = containerMap;
	}

	public Map<String, String> getContainerMap() {
		return this.containerMap;
	}
	
	public List<String> getLimitList() {
		return this.limitList;
	}

	public void setLimitList(List<String> limitList) {
		this.limitList = limitList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public List<String> getNameList() {
		return this.nameList;
	}

	public List<String> getWindowList() {
		return this.windowList;
	}

	public void setWindowList(List<String> windowList) {
		this.windowList = windowList;
	}

	public List<String> getFieldList() {
		return this.fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * Genera el codigo html del inicio de una seccion en la pagina
	 * 
	 * @return Codigo html
	 */
	public List<String> generateStartSection(Boolean hidden) {
		
		List<String> list = new ArrayList<String>();
		
		list.add("<div class=\"section\" ");
		if (hidden)
			list.add("style=\"display: none\"");
		list.add(">");
		
		return list;
	}

	/**
	 * Genera el codigo html del fin de una seccion en la pagina
	 * 
	 * @return Codigo html
	 */
	public List<String> generateEndSection() {
		
		List<String> list = new ArrayList<String>();

		list.add("</div>");
		
		return list;
	}

	/**
	 * Genera el codigo html de una etiqueta
	 * 
	 * @param Etiqueta a mostrar
	 * @return Codigo html
	 */
	public List<String> generateLabel(String label) {
		
		List<String> list = new ArrayList<String>();
		
		list.add("<label class=\"etiqueta\">" + label + "</label>");
		
		return list;
	}

	/**
	 * Genera el codigo html de una etiqueta que muestra un valor de campo
	 * 
	 * @param Etiqueta a mostrar
	 * @return Codigo html
	 */
	public List<String> generateFieldLabel(String label) {
		
		List<String> list = new ArrayList<String>();
		
		list.add("<label class=\"etiqueta-valor\">" + label + "</label>");
		
		return list;
	}

	/**
	 * Genera el codigo html de un subtitulo
	 * 
	 * @param Subtitulo a mostrar
	 * @return Codigo html
	 */
	public List<String> generateSubtitle(String title) {
		
		List<String> list = new ArrayList<String>();
		
		list.add("<h2>" + title + "</h2>");
		
		return list;
	}
	
	/**
	 * Genera el codigo html de un select a partir de los datos entrados
	 * 
	 * @param name Nombre del select
	 * @param selected Valor seleccionado por el usuario en ese select
	 * @param events Eventos del select
	 * @param map Mapa con los pares value y texto a mostrar en el select
	 * @return Lista con el codigo html del select
	 */
	public List<String> generateStringSelect(String name, String selected, String events, Map<String, String> map) {
		
		List<String> list = new ArrayList<String>();
		
		// inicia el select
		list.add("<select id=\"" + name + "\" name=\"" + name + "\" " + events + ">");
		list.add("	<option value=\"\">Seleccione</option>");

		// muestra las opciones del select, donde el value es la clave del mapa y el texto que se muestra el valor en el mapa
		Iterator<Entry<String, String>> itInst = map.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)itInst.next();
	        String selecId = (String) pairs.getKey();
	        String selecValue = (String) pairs.getValue();

			String linea = "<option value=\"" + selecId + "\" ";
			if (selected!=null && selecId.contentEquals(selected)) {
				linea += "selected ";
			}
			linea += ">" + selecValue + "</option>";
			
			list.add(linea);
	    }
		
		// cierra el select
	    list.add("</select>");
		
		return list;
	}
	
	/**
	 * Genera el codigo html de un select a partir de los datos entrados
	 * 
	 * @param name Nombre del select
	 * @param selected Valor seleccionado por el usuario en ese select
	 * @param events Eventos del select
	 * @param map Mapa con los pares value y texto a mostrar en el select. El texto se forma a partir de un objeto ProcessEntity
	 * @return Lista con el codigo html del select
	 */
	public List<String> generateInstanceDataSelect(String name, String selected, String events, Map<String, ProcessEntity> map) {
		
		List<String> list = new ArrayList<String>();
		
		// inicia el select
		list.add("<select id=\"" + name + "\" name=\"" + name + "\" " + events + ">");
		list.add("	<option value=\"\">Seleccione</option>");

		// muestra las opciones del select, donde el value es la clave del mapa y el texto que se muestra el valor en el mapa
		Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
	        String selecId = (String) pairs.getKey();
	        ProcessEntity selecValue = (ProcessEntity) pairs.getValue();

	        String finString;
	        
	        if (selecValue.getEndTime()!=null)
	        	finString = " a " + selecValue.getEndTime();
	        else
	        	finString = " En ejecuci&oacute;n";
	        
			String linea = "<option value=\"" + selecId + "\" ";
			if (selected!=null && selecId.contentEquals(selected)) {
				linea += "selected ";
			}
//			linea += ">" + selecValue.getInstanceId() + " : " + selecValue.getStartTime() + finString + " por " + selecValue.getStartUserId() + "</option>";
			linea += ">" + selecValue.getInstanceId() + " : " + selecValue.getStartTime() + finString + "</option>";
			
			list.add(linea);
	    }
		
		// cierra el select
	    list.add("</select>");
		
		return list;
	}

	/**
	 * Genera el codigo html de un contenedor para fecha
	 * 
	 * @param id Id del campo
	 * @param value Valor del campo
	 * @return Lista con el codigo html
	 */
	public List<String> generateContainer(String id, String value) {

		// conserva los datos del contenedor para generar posteriormente el codigo en javascript
		this.getContainerMap().put( id, value);
		
		List<String> list = new ArrayList<String>();
		
		String contId = "cont_" + id;
		list.add("<div id=\"" + contId + "\" style=\"float:left\"></div>");
		
		return list;
	}
	
	public List<String> generateFormRow(String label, String value, Boolean obligatory) {
		
		String obligMark = "";
		if (obligatory)
			obligMark = "* ";
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateStartSection(false));
		
		list.add("<div class=\"ppinot-form-label\">");
		list.addAll( this.generateLabel(obligMark + label) );
		list.add("</div>");
		
		list.add("<div class=\"ppinot-form-value\">");
		list.add(value);
		list.add("</div>");

		list.addAll( this.generateEndSection());
		
		return list;
	}
	
	public List<String> generateFormRow(String label, List<String> value, Boolean obligatory) {
		
		String obligMark = "";
		if (obligatory)
			obligMark = "* ";
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateStartSection(false));
		
		list.add("<div class=\"ppinot-form-label\">");
		list.addAll( this.generateLabel(obligMark + label) );
		list.add("</div>");
		
		list.add("<div class=\"ppinot-form-value\">");
		list.addAll(value);
		list.add("</div>");

		list.addAll( this.generateEndSection());
		
		return list;
	}
	
	public List<String> generateOpenWindow(String id) {
		
		this.getWindowList().add(id);
		
		List<String> list = new ArrayList<String>();
		
		list.add("<div id=\"cont_win_" + id + "\">");
		list.add("<div id=\"inside_win_" + id + "\" class=\"x-hidden\">");
		list.add("<div id=\"content_win_" + id + "\" class=\"ppinot-content-win\">");

		list.add("<div class=\"ppinot-obligatory-label\">");
		list.addAll( this.generateLabel("Los campos marcados con * son obligatorios") );
		list.add("</div>");
		
		return list;
	}
	
	public List<String> generateCloseWindow(Boolean modify) {
		
		List<String> list = new ArrayList<String>();
		
		if (modify)
			list.addAll( this.generateFormRow("<input name=\"\" value=\"Calcular\" type=\"button\" onclick=\"accion(true);\" />", "", false) );
		
		list.add("</div>");
		list.add("</div>");
		list.add("</div>");
		
		return list;
	}
	
	public List<String> generateCommonName(	String varName, String name) {

		this.getNameList().add(varName);

		this.getFieldList().add(varName);
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( "Nombre", "<input id=\"" + varName + "\" name=\"" + varName + "\" type=\"text\" value=\"" + name + "\" />", false) );
		
		return list;
	}
	
	public List<String> generateCommonType( 
			String ppiLabel, String varNamePpiId, String ppiId, String onclick, Map<String, String> ppiMap,
			String varName, String name) {

		this.getNameList().add(varName);

		this.getFieldList().add(varNamePpiId);
		this.getFieldList().add(varName);
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( ppiLabel, this.generateStringSelect( varNamePpiId, ppiId, onclick, ppiMap), true ) );
		list.addAll( this.generateFormRow( "Nombre", "<input id=\"" + varName + "\" name=\"" + varName + "\" type=\"text\" value=\"" + name + "\" />", false) );
		
		return list;
	}
	
	public List<String> generateCommonData(
			String ppiLabel, String varNamePpiId, String ppiId, String onclick, Map<String, String> ppiMap,
			String varId, String id, 
			String varName, String name, String varRefMax, String refMax, String varRefMin, String refMin) throws IOException {

		this.getNameList().add(varName);
		this.getLimitList().add(varRefMax);
		this.getLimitList().add(varRefMin);

		this.getFieldList().add(varId);
		this.getFieldList().add(varNamePpiId);
		this.getFieldList().add(varName);
		this.getFieldList().add(varRefMax);
		this.getFieldList().add(varRefMin);
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( ppiLabel, this.generateStringSelect( varNamePpiId, ppiId, onclick, ppiMap), false ) );
		list.addAll( this.generateFormRow( "Id", "<input id=\"" + varId + "\" name=\"" + varId + "\" type=\"text\" value=\"" + id + "\" />", true) );
		list.addAll( this.generateFormRow( "Nombre", "<input id=\"" + varName + "\" name=\"" + varName + "\" type=\"text\" value=\"" + name + "\" />", false) );
		list.addAll( this.generateFormRow( "L&iacute;mite m&iacute;nimo", "<input id=\"" + varRefMin + "\" name=\"" + varRefMin + "\" type=\"text\" value=\"" + refMin + "\" />", false) );
		list.addAll( this.generateFormRow( "L&iacute;mite m&aacute;ximo", "<input id=\"" + varRefMax + "\" name=\"" + varRefMax + "\" type=\"text\" value=\"" + refMax + "\" />", false) );
		
		return list;
	}

	public List<String> generateActivityStartEnd(String varName, String activityIdStarEnd, Map<String, String> map) {

		this.getFieldList().add(varName);
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( "Actividad", this.generateStringSelect( varName , activityIdStarEnd, "", map), true) );

		return list;
	}

	public List<String> generateTimeMeasure( 
			String varNameIni, String activityIdTimeIni, 
			String varNameFin, String activityIdTimeFin, 
			String varNameAtEndIni, String atEndTimeIni, 
			String varNameAtEndFin, String atEndTimeFin, 
			Map<String, String> activityMap, Map<String, String> sinoMap) {

		this.getFieldList().add(varNameIni);
		this.getFieldList().add(varNameFin);
		this.getFieldList().add(varNameAtEndIni);
		this.getFieldList().add(varNameAtEndFin);
		
		List<String> list = new ArrayList<String>();

		list.addAll( this.generateFormRow("Actividad inicial", this.generateStringSelect( varNameIni , activityIdTimeIni, "", activityMap), true) );
		
		list.addAll( this.generateFormRow("Desde final", this.generateStringSelect( varNameAtEndIni , atEndTimeIni, "", sinoMap), true) );
		
		list.addAll( this.generateFormRow("Actividad final", this.generateStringSelect( varNameFin, activityIdTimeFin, "", activityMap), true) );
		
		list.addAll( this.generateFormRow("Desde final", this.generateStringSelect( varNameAtEndFin, atEndTimeFin, "", sinoMap), true) );

		return list;
	}

	public List<String> generateCountMeasure(String varName, String activityIdCount, String varNameAtEnd, String atEndTimeCount, Map<String, String> activityMap, Map<String, String> sinoMap) {

		this.getFieldList().add(varName);
		this.getFieldList().add(varNameAtEnd);
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( "Actividad", this.generateStringSelect( varName , activityIdCount, "", activityMap), true) );
		
		list.addAll( this.generateFormRow( "Desde final", this.generateStringSelect( varNameAtEnd , atEndTimeCount, "", sinoMap), true) );

		return list;
	}

	public List<String> generateElementConditionMeasure(String varNameElement, String taskIdElement, String varStateElement, String stateIdElement, Map<String, String> taskMap, Map<String, String> taskStateMap) {

		this.getFieldList().add(varNameElement);
		this.getFieldList().add(varStateElement);

		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( "Tarea", this.generateStringSelect( varNameElement, taskIdElement, "", taskMap), true) );
		list.addAll( this.generateFormRow( "Estado", this.generateStringSelect( varStateElement, stateIdElement, "", taskStateMap), true) );

		return list;
	}

	public List<String> generateDataMeasure(String varNameData, String dataIdData, String varDataProp, String dataPropId, Map<String, String> dataMap, Map<String, String> dataPropMap) {

		this.getFieldList().add(varNameData);

		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateFormRow( "Dataobject", this.generateStringSelect( varNameData, dataIdData, "onchange=\"accion(false);\"", dataMap), true) );
		
		if (dataIdData!=null && dataIdData!="") {
			
			this.getFieldList().add(varDataProp);

			list.addAll( this.generateFormRow( "Propiedades", this.generateStringSelect( varDataProp, dataPropId, "", dataPropMap), true) );
		}

		return list;
	}

	public List<String> generateDataConditionMeasure(String varNameData, String dataIdData, String varDataState, String dataStateId, Map<String, String> dataMap, Map<String, String> dataStateMap) {
		
		this.getFieldList().add(varNameData);

		List<String> list = new ArrayList<String>();

		list.addAll( this.generateFormRow( "Dataobject", this.generateStringSelect( varNameData, dataIdData, "onchange=\"accion(false);\"", dataMap), true) );
		
		if (dataIdData!=null && dataIdData!="") {

			this.getFieldList().add(varDataState);
			
			list.addAll( this.generateFormRow( "Estados", this.generateStringSelect( varDataState, dataStateId, "", dataStateMap), true) );
		}

		return list;
	}
	
	public List<String> generateAggregatedMeasure(int i, String varAggrFunc , String aggrFunc, String varAggrYear, String aggrYear, String varAggrPeriod, String aggrPeriod, String varStartDate, String startDate, String varInStart, String inStart, String varEndDate, String endDate, String varInEnd, String inEnd, Map<String, String> funcMap, Map<String, String> sinoMap, Map<String, String> yearMap, Map<String, String> periodMap) {

		this.getFieldList().add(varAggrFunc);
		this.getFieldList().add(varAggrYear);
		this.getFieldList().add(varAggrPeriod);
		this.getFieldList().add(varStartDate);
		this.getFieldList().add(varInStart);
		this.getFieldList().add(varEndDate);
		this.getFieldList().add(varInEnd);

		List<String> list = new ArrayList<String>();
		
		String displayPeriodo = "none";
		String displayIntervalo = "none";
		String displayIncluir = "none";
		
		if (aggrYear!=null && !aggrYear.contentEquals("")) {
			
			displayPeriodo = "inline";
			displayIncluir = "inline";
		} else 
		if (startDate!=null && (!startDate.contentEquals("") || !startDate.contentEquals(""))) {
			
			aggrYear = "intervalo";
			displayIntervalo = "inline";
			displayIncluir = "inline";
		}
		
		list.addAll( this.generateFormRow( "Funci&oacute;n", this.generateStringSelect( varAggrFunc , aggrFunc, "", funcMap), true) );
		list.addAll( this.generateFormRow( "Incluir", this.generateStringSelect( varAggrYear , aggrYear, "onchange=\"mostrarPeriodo(this," + i + ");\"", yearMap), false) );
		list.add("<div id=\"cont-periodo-" + i + "\" style=\"display:" + displayPeriodo + "\">");
		list.addAll( this.generateFormRow( "Per&iacute;odo", this.generateStringSelect( varAggrPeriod , aggrPeriod, "", periodMap), false) );
		list.add("</div>");
		list.add("<div id=\"cont-intervalo-" + i + "\" style=\"display:" + displayIntervalo + "\">");
		list.addAll( this.generateFormRow( "Fecha inicial", this.generateContainer(varStartDate, startDate), false) );
		list.addAll( this.generateFormRow( "Fecha final", this.generateContainer(varEndDate, endDate), false) );
		list.add("</div>");
		list.add("<div id=\"cont-incluir-" + i + "\" style=\"display:" + displayIncluir + "\">");
		list.addAll( this.generateFormRow( "Incluir inicial", this.generateStringSelect( varInStart , inStart, "", sinoMap), false) );
		list.addAll( this.generateFormRow( "Incluir final", this.generateStringSelect( varInEnd , inEnd, "", sinoMap), false) );
		list.add("</div>");
		list.addAll( this.generateFormRow( "Medida de instancia", "", false) );

		return list;
	}

	public List<String> generateDerivedMeasure(String varDerFunc, String derFunc, Map<String, String> derFuncMap,
			String varIdOper1, String idOper1, String varMedOper1 , String medOper1,
			String varIdOper2, String idOper2, String varMedOper2 , String medOper2, 
			Map<String, String> measureMap) {

		this.getFieldList().add(varDerFunc);

		this.getFieldList().add(varIdOper1);
		this.getFieldList().add(varIdOper2);
		this.getFieldList().add(varMedOper1);
		this.getFieldList().add(varMedOper2);
		
		List<String> list = new ArrayList<String>();

		list.addAll( this.generateFormRow( "Funci&oacute;n", "<input id=\"" + varDerFunc + "\" name=\"" + varDerFunc + "\" type=\"text\" value=\"" + derFunc + "\" />", true) );

		list.addAll( this.generateFormRow( "Operando 1", "", false) );
		list.addAll( this.generateFormRow( "Variable", "<input id=\"" + varIdOper1 + "\" name=\"" + varIdOper1 + "\" type=\"text\" value=\"" + idOper1 + "\" />", true) );
		list.addAll( this.generateFormRow( "Medida", this.generateStringSelect( varMedOper1 , medOper1, "", measureMap), true) );

		list.addAll( this.generateFormRow( "Operando 2", "", false) );
		list.addAll( this.generateFormRow( "Variable", "<input id=\"" + varIdOper2 + "\" name=\"" + varIdOper2 + "\" type=\"text\" value=\"" + idOper2 + "\" />", false) );
		list.addAll( this.generateFormRow( "Medida", this.generateStringSelect( varMedOper2 , medOper2, "", measureMap), false) );
		
		return list;
	}

	/**
	 * Imprime en la pagina una lista de cadena, una por linea
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param list Lista de cadenas a imprimir
	 * @throws IOException
	 */
	public void print(JspWriter out, List<String> list) throws IOException {
		for(String linea: list) {
			out.print( linea );
		}
	}

	/**
	 * Genera e imprime el codigo html del inicio de una seccion en la pagina
	 * 
	 * @return Codigo html
	 * @throws IOException 
	 */
	public void printStartSection(JspWriter out, Boolean hidden) throws IOException {
		
		this.print( out, this.generateStartSection(hidden) );
	}

	/**
	 * Genera e imprime el codigo html del fin de una seccion en la pagina
	 * 
	 * @return Codigo html
	 */
	public void printEndSection(JspWriter out) throws IOException {
		
		this.print( out, this.generateEndSection() );
	}

	/**
	 * Genera e imprime el codigo html de una etiqueta
	 * 
	 * @param Etiqueta a mostrar
	 * @return Codigo html
	 */
	public void printLabel(JspWriter out, String label) throws IOException {
		
		this.print( out, this.generateLabel(label) );
	}

	/**
	 * Genera e imprime el codigo html de una etiqueta que muestra un valor de campo
	 * 
	 * @param Etiqueta a mostrar
	 * @return Codigo html
	 */
	public void printFieldLabel(JspWriter out, String label) throws IOException {
		
		this.print( out, this.generateFieldLabel(label) );
	}

	/**
	 * Genera e imprime un subtitulo
	 * 
	 * @param Subtitulo a mostrar
	 * @return Codigo html
	 */
	public void printSubtitle(JspWriter out, String title) throws IOException {
		
		this.print( out, this.generateSubtitle(title) );
	}
	
	/**
	 * Genera e imprime el codigo html de un select a partir de los datos entrados
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param selected Valor seleccionado por el usuario en ese select
	 * @param events Eventos del select
	 * @param map Mapa con los pares value y texto a mostrar en el select
	 * @return Lista con el codigo html del select
	 * @throws IOException
	 */
	public void printStringSelect(JspWriter out, String name, String selected, String events, Map<String, String> map) throws IOException {
		
		this.print( out, this.generateStringSelect(name, selected, events, map) );
	}
	
	/**
	 * Genera e imprime el codigo html de un select a partir de los datos entrados
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param name Nombre del select
	 * @param selected Valor seleccionado por el usuario en ese select
	 * @param events Eventos del select
	 * @param map Mapa con los pares value y texto a mostrar en el select. El texto se forma a partir de un objeto ProcessEntity
	 * @throws IOException
	 */
	public void printInstanceDataSelect(JspWriter out, String name, String selected, String events, Map<String, ProcessEntity> map) throws IOException {
		
		this.print( out, this.generateInstanceDataSelect(name, selected, events, map) );
	}
	
	/**
	 * Permite seleccionar uno de los procesos desplegados en la plataforma BPM
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameProcId Nombre de la variable post que toma el id del proceso seleccionado por el usuario
	 * @param procId Id del proceso seleccionado por el usuario
	 * @param map Mapa con la informacion a mostrar en el select de los procesos
	 * @throws IOException
	 */
	public void printProcesses(JspWriter out, String varNameProcId, String procId, Map<String, String> map) throws IOException {

		// permite seleccionar un proceso
		this.printStartSection(out, false);

		this.printLabel(out, "Proceso");
		this.printStringSelect(out, varNameProcId, procId, "onchange=\"accion(false);\"", map );
		
		this.printEndSection(out);
	}
	
	/**
	 * Permite seleccionar una de las instancias en la plataforma BPM del proceso seleccionado por el usuario
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameInstId Nombre de la variable post que toma el id de la instancia de proceso seleccionada por el usuario
	 * @param instanceId Id de la instancia de proceso seleccionada por el usuario
	 * @param map Mapa con la informacion a mostrar en el select de los instancias
	 * @throws IOException
	 */
	public void printInstances(JspWriter out, String varNameInstId, String instanceId, Map<String, ProcessEntity> map) throws IOException {

		// permite seleccionar una instancia del proceso
		this.printStartSection(out, false);
		this.printLabel(out, "Instancia del proceso");

		this.printInstanceDataSelect(out, varNameInstId, instanceId, "onchange=\"accion(false);\"", map );
		
		this.printEndSection(out);
		
	}
	
	public void printHeader(JspWriter out, String classPrefix) throws IOException {
		
		List<String> list = new ArrayList<String>();
		
		list.addAll( this.generateStartSection(false));

		list.add("<div class=\"ppinot-col-mark-header\">");
		list.add("</div>");
		
		list.add("<div class=\"ppinot-col-type-header\">");
		list.add( "Tipo" );
		list.add("</div>");
		
		list.add("<div class=\"ppinot-col-name-header\">");
		list.add( "Nombre" );
		list.add("</div>");
		
		list.add("<div class=\"ppinot-col-value-header" + classPrefix + "\">");
		list.add( "Valor" );
		list.add("</div>");
		
		list.add("<div class=\"ppinot-col-success-header\">");
		list.add( " " );
		list.add("</div>");
		
		list.addAll( this.generateEndSection());
		
		this.print(out, list);
	}
	
	public void printOpenWindow(JspWriter out, String id) throws IOException {
		
		this.print(out, this.generateOpenWindow(id));
	}
	
	public void printCloseWindow(JspWriter out, Boolean modify) throws IOException {
		
		this.print(out, this.generateCloseWindow(modify));
	}
	
	public void printFormRow(JspWriter out, String label, String value, Boolean obligatory) throws IOException {

		this.print(out, this.generateFormRow(label, value, obligatory));
	}
	
	public void printFormRow(JspWriter out, String label, List<String> value, Boolean obligatory) throws IOException {

		this.print(out, this.generateFormRow(label, value, obligatory));
	}
	
	/**
	 * Muestra el valor de un PPI
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param cond Condicion que permite determinar si el valor calculado es correcto o no
	 * @param value Valor a mostrar
	 * @throws IOException
	 */
	public void printPpiValue(JspWriter out, String classPrefix, String id, Boolean cond, String type, String name, List<String> valueList, String measureUnit, List<Double> successList, List<Boolean> markList, Boolean modify) throws IOException {
		
		List<String> list = new ArrayList<String>();
		
		// abre la seccion
		list.addAll( this.generateStartSection(false));

		String btnName = "Mostrar";
		if (modify) {
			btnName = "Modificar";
		}

		list.add("<div class=\"ppinot-col-mark\">");
		list.add("<input id=\"btn_" + id + "\" name=\"btn_" + id + "\" type=\"button\" value=\"" + btnName + "\" onClick=\"openWindow(this, '" + id + "');\" />");
		list.add("</div>");

		list.add("<div class=\"ppinot-col-type\">");
		list.add( type );
		list.add("</div>");

		list.add("<div class=\"ppinot-col-name\">");
		list.add( name );
		list.add("</div>");

		if (cond)
		{
			
			// muestra el valor del ppi para cada uno de los intervalos de tiempo seleccionados
			int i = 0;
			list.add("<div class=\"ppinot-col-values\">");
			for (String value : valueList) {
				
//				list.addAll( this.generateStartSection(false));

				list.add("<div class=\"ppinot-col-value" + classPrefix + "\">");
				String formatedValue = PpiNotModelUtils.numberFormat(value);
				if (formatedValue==null || formatedValue=="")
					formatedValue = value;
				else {
					if (measureUnit!=null && measureUnit!="")
						measureUnit = " " + measureUnit;
					formatedValue += measureUnit;
				}
				list.add( formatedValue );
				list.add("</div>");
				
				// si es posible, se muestra el grado de satisfaccion del indicador
				String success = "";
				if (successList!=null && i<successList.size() && successList.get(i)!=-1)
					success = Double.toString(successList.get(i));
				if (success!="") {
					
					Boolean mark = false;
					if (markList!=null)
						mark = markList.get(i);
					
					list.add("<div class=\"ppinot-col-success\">");
					if (mark) 
						list.add(" <font class=\"ppinot-success-alarm\">");
					list.add("(");
					list.add( PpiNotModelUtils.numberFormat(success) );
					list.add(")");
					if (mark) 
						list.add("</font>");
					list.add("</div>");

				}
				
//				list.addAll( this.generateEndSection());
				i++;
			}
			list.add("</div>");
		} else {
			
			list.add("<div class=\"ppinot-col-value\">");
			list.add( "Complete los datos");
			list.add("</div>");
		}
		
		// cierra la seccion
		list.addAll( this.generateEndSection());
		
		this.print(out, list);
	}
	
	public void printCommonName(JspWriter out, 
			String varName, String name) throws IOException {
		
		this.print(out, this.generateCommonName( varName, name) );
	}
	
	public void printCommonType(JspWriter out, 
			String ppiLabel, String varNamePpiId, String ppiId, String onclick, Map<String, String> ppiMap,
			String varName, String name) throws IOException {

		this.print(out, this.generateCommonType( ppiLabel, varNamePpiId, ppiId, onclick, ppiMap, varName, name) );
	}
	
	public void printCommonData(JspWriter out, 
			String ppiLabel, String varNamePpiId, String ppiId, String onclick, Map<String, String> ppiMap,
			String varId, String id, String varName, String name, String varRefMax, String refMax, String varRefMin, String refMin) throws IOException {

		this.print(out, this.generateCommonData( ppiLabel, varNamePpiId, ppiId, onclick, ppiMap,
				varId, id, varName, name, varRefMax, refMax, varRefMin, refMin) );
	}
	
	/**
	 * Permite introducir los datos para calcular los PPI ActivityStart y ActivityEnd
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varName Nombre de la variable post que toma el id de actividad seleccionada por el usuario
	 * @param activityIdStarEnd Id de actividad seleccionada por el usuario
	 * @param map Mapa con la informacion a mostrar en el select
	 * @throws IOException
	 */
	public void printActivityStartEnd(JspWriter out, String varName, String activityIdStarEnd, Map<String, String> map) throws IOException {

		this.print(out, this.generateActivityStartEnd( varName, activityIdStarEnd, map) );
	}
	
	/**
	 * Permite introducir los datos para calcular el PPI TimeMeasure
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameIni Nombre de la variable post que toma el id de actividad inicial seleccionada por el usuario
	 * @param activityIdTimeIni Id de actividad inicial seleccionada por el usuario
	 * @param varNameFin Nombre de la variable post que toma el id de actividad final seleccionada por el usuario
	 * @param activityIdTimeFin Id de actividad final seleccionada por el usuario
	 * @param varNameAtEndIni Nombre de la variable post que indica si en la actividad inicial la medida se toma desde el inicio o no
	 * @param atEndTimeIni Valor seleccionado por el usuario que indica si en la actividad inicial se mide desde el final o no
	 * @param varNameAtEndFin Nombre de la variable post que indica si en la actividad final la medida se toma desde el final o no
	 * @param atEndTimeFin Valor seleccionado por el usuario que indica si en la actividad final se mide desde el final o no
	 * @param activityMap Mapa con la informacion a mostrar en el select de las actividades
	 * @param sinoMap Mapa con la informacion a mostrar en el select para indicar si se desea medir desde el final o no
	 * @throws IOException
	 */
	public void printTimeMeasure(JspWriter out, 
			String varNameIni, String activityIdTimeIni, 
			String varNameFin, String activityIdTimeFin, 
			String varNameAtEndIni, String atEndTimeIni, 
			String varNameAtEndFin, String atEndTimeFin, 
			Map<String, String> activityMap, Map<String, String> sinoMap) throws IOException {
	
		this.print(out, this.generateTimeMeasure( 
				varNameIni, activityIdTimeIni, 
				varNameFin, activityIdTimeFin, 
				varNameAtEndIni, atEndTimeIni, 
				varNameAtEndFin, atEndTimeFin, 
				activityMap, sinoMap));
	}
	
	/**
	 * Permite introducir los datos para calcular el PPI CountMeasure
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varName Nombre de la variable post que toma el id de actividad seleccionada por el usuario
	 * @param activityIdCount Id de actividad seleccionada por el usuario
	 * @param varNameAtEnd Nombre de la variable post que indica si en la actividad la medida se toma desde el inicio o no
	 * @param atEndTimeCount Valor seleccionado por el usuario que indica si en la actividad se mide desde el final o no
	 * @param activityMap Mapa con la informacion a mostrar en el select de las actividades
	 * @param sinoMap Mapa con la informacion a mostrar en el select para indicar si se desea medir desde el final o no
	 * @throws IOException
	 */
	public void printCountMeasure(JspWriter out, String varName, String activityIdCount, String varNameAtEnd, String atEndTimeCount, Map<String, String> activityMap, Map<String, String> sinoMap) throws IOException {
		
		this.print(out, this.generateCountMeasure(varName, activityIdCount, varNameAtEnd, atEndTimeCount, activityMap, sinoMap));
	}
	
	/**
	 * Permite introducir los datos para calcular el PPI ElementConditionMeasure
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameElement Nombre de la variable post que toma el id de tarea seleccionada por el usuario
	 * @param taskIdElement Id de tarea seleccionada por el usuario
	 * @param varStateElement Nombre de la variable post que indica el estado que desea consultar el usuario
	 * @param stateIdElement Estado seleccionado por el usuario
	 * @param taskMap Mapa con la informacion a mostrar en el select de las tareas
	 * @param taskStateMap Mapa con la informacion de los posibles estados de las tareas
	 * @throws IOException
	 */
	public void printElementConditionMeasure(JspWriter out, String varNameElement, String taskIdElement, String varStateElement, String stateIdElement, Map<String, String> taskMap, Map<String, String> taskStateMap) throws IOException {
		
		this.print(out, this.generateElementConditionMeasure(varNameElement, taskIdElement, varStateElement, stateIdElement, taskMap, taskStateMap) );
	}
	
	/**
	 * Permite introducir los datos para calcular el PPI DataMeasure
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameData Nombre de la variable post que toma el id de dataobject seleccionado por el usuario
	 * @param dataIdData Id del dataobject seleccionado por el usuario
	 * @param varDataProp Nombre de la variable post que indica la propiedad que desea consultar el usuario
	 * @param dataPropId Propiedad seleccionada por el usuario
	 * @param dataMap Mapa con la informacion a mostrar en el select de los dataobject
	 * @param dataPropMap Mapa con la informacion de las propiedades del dataobject seleccionado
	 * @throws IOException
	 */
	public void printDataMeasure(JspWriter out, String varNameData, String dataIdData, String varDataProp, String dataPropId, Map<String, String> dataMap, Map<String, String> dataPropMap) throws IOException {

		this.print(out, this.generateDataMeasure(varNameData, dataIdData, varDataProp, dataPropId, dataMap, dataPropMap) );
	}
	
	/**
	 * Permite introducir los datos para calcular el PPI DataConditionMeasure
	 * 
	 * @param out Objeto para imprimir en la pagina
	 * @param varNameData Nombre de la variable post que toma el id de dataobject seleccionado por el usuario
	 * @param dataIdData Id del dataobject seleccionado por el usuario
	 * @param varDataProp Nombre de la variable post que indica el estado que se desea consultar
	 * @param dataPropId Propiedad seleccionada por el usuario
	 * @param dataMap Mapa con la informacion a mostrar en el select de los dataobject
	 * @param dataPropMap Mapa con la informacion de los estados del dataobject seleccionado
	 * @throws IOException
	 */
	public void printDataConditionMeasure(JspWriter out, String varNameData, String dataIdData, String varDataState, String dataStateId, Map<String, String> dataMap, Map<String, String> dataStateMap) throws IOException {
		
		this.print(out, this.generateDataConditionMeasure(varNameData, dataIdData, varDataState, dataStateId, dataMap, dataStateMap) );
	}
	
	public void printAggregatedMeasure(JspWriter out, int i, String varAggrFunc, String aggrFunc, String varAggrYear, String aggrYear, String varAggrPeriod, String aggrPeriod, String varStartDate, String startDate, String varInStart, String inStart, String varEndDate, String endDate, String varInEnd, String inEnd, Map<String, String> funcMap, Map<String, String> sinoMap, Map<String, String> yearMap, Map<String, String> periodMap) throws IOException {

		this.print(out, this.generateAggregatedMeasure(i, varAggrFunc , aggrFunc, varAggrYear, aggrYear, varAggrPeriod, aggrPeriod, varStartDate, startDate, varInStart, inStart, varEndDate, endDate, varInEnd, inEnd, funcMap, sinoMap, yearMap, periodMap) );
	}
	
	public void printReportSuccess(JspWriter out, String success) throws IOException {
		
		this.printStartSection(out, false);
		
		this.printLabel(out, "Satisfacci&oacute;n promedio");
		this.printFieldLabel(out, PpiNotModelUtils.numberFormat(success) );
		
		this.printEndSection(out);
	}

	public void printContainer(JspWriter out, String id, String value) throws IOException {

		// imprime
		this.print(out, this.generateContainer(id, value) );
	}
	
	public void printJavascript(JspWriter out, String procId, String tabactivo, String openwindow, Boolean modify) throws IOException {

		List<String> list = new ArrayList<String>();
		
		// determina cual es el tab que debe estar activo cuando se carga la pagina
		if (tabactivo!=null && tabactivo.contentEquals("tab2"))
			tabactivo = "1";
		else
			tabactivo = "0";
		
		// el control al que se le asigna el id del tab que esta activo cuando se hace submit de la pagina
		list.add("<input id=\"tabactivo\" name=\"tabactivo\" type=\"hidden\" value=\"\" />");
		list.add("<input id=\"openwindow\" name=\"openwindow\" type=\"hidden\" value=\"\" />");
		list.add("<script>");

		if (!this.getContainerMap().isEmpty()) 
			list.add("var listadate = new Array();");
		
		// para que se mantenga seleccionado el tab que se estaba utilizando cuando se hace submit
		list.add("var objtab = null;");
		
		// para manejar las ventanas
		if (!this.getWindowList().isEmpty()) 
			list.add("var listawindows = new Array();");

		// la function que muestra los controles para seleccionar el periodo de tiempo en los ppi agregados
		list.add("function mostrarPeriodo(year, i) {");
		list.add("  var periodo = document.getElementById('cont-periodo-'+i);");
		list.add("  var intervalo = document.getElementById('cont-intervalo-'+i);");
		list.add("  var incluir = document.getElementById('cont-incluir-'+i);");
		list.add("  periodo.style.display = 'none';");
		list.add("  intervalo.style.display = 'none';");
		list.add("  incluir.style.display = 'none';");
		list.add("  if (year.value=='intervalo') {");
		list.add("	  intervalo.style.display = 'inline';");
		list.add("	  incluir.style.display = 'inline';");
		list.add("  } else ");
		list.add("  if (year.value!='') {");
		list.add("	  periodo.style.display = 'inline';");
		list.add("	  incluir.style.display = 'inline';");
		list.add("  }");
		list.add("}");
				
		// la funcion que se ejecuta cuando se va a realizar submit
		list.add("function accion(cerrar) {");
		
		// cierra la ventana abierta si se solicita
		list.add("if( cerrar ) {");
		list.add("  closeWindow();");
		list.add("}");

		// la validacion de los valores de los campos con limites de referencia
		if (this.getLimitList().size()>0) {
			
			for(String ref: this.getLimitList()) {

				list.add("if( document.getElementById('" + ref + "').value!='' && !(/^[0-9]*\\.{0,1}[0-9]*$/.test(document.getElementById('" + ref + "').value))) {");
				list.add("	alert( 'Los limites de las referencias deben ser numeros reales');");
				list.add("	return false;");
				list.add("}");
			}
			
			for(int i=0; i<this.getLimitList().size(); i=i+2) {

				list.add("if (document.getElementById('" + this.getLimitList().get(i+1) + "').value!='' && document.getElementById('" + this.getLimitList().get(i) + "').value!='' && 1*document.getElementById('" + this.getLimitList().get(i+1) + "').value>=1*document.getElementById('" + this.getLimitList().get(i) + "').value) {");
				list.add("	alert( 'El limite inferior de las referencias deben ser menor que el superior');");
				list.add("	return false;");
				list.add("}");
			}
		}
		
		// para enviar el id del tab seleccionado cuando se hace submit cuando hay un proceso seleccionado
		if (procId!="") {

			list.add("	document.getElementById(\"tabactivo\").value = objtab.getActiveTab().contentEl;");
		}
		
		// poner el html de las ventanas en html de la pagina
		if (this.getFieldList().size()>0) {
			
			for(String var: this.getFieldList()) {

				if (!this.getContainerMap().isEmpty()) {
					list.add("  if (listadate['" + var + "']==null) {");
				}
				list.add("	  document.appform.hd_" + var + ".value = document.getElementById(\"" + var + "\").value;");
				if (!this.getContainerMap().isEmpty()) {
					list.add("  } else {");
					list.add("	  document.appform.hd_" + var + ".value = listadate['" + var + "'].getRawValue();");
					list.add("  }");
				}
			}
		}
		
		// hace submit de la pagina
		list.add("	document.appform.submit();");
		// FIN de la funcion que se ejecuta cuando se va a realizar submit
		list.add("}");

		if (!this.getWindowList().isEmpty()) {

			list.add("function openWindow(btn, id) {");
			list.add("  document.getElementById('openwindow').value = id;");
			list.add("  listawindows['win_' + id ].show(btn);");
			list.add("}");

			list.add("function closeWindow() {");
			list.add("  if (document.getElementById('openwindow').value!='') {");
			list.add("    listawindows['win_' + document.getElementById('openwindow').value ].hide();");
			list.add("    document.getElementById('openwindow').value = '';");
			list.add("  }");
			list.add("}");
		}
		
		// la inicializacion de los objetos de Ext cuando hay un proceso seleccionado
		list.add("function onChange(obj, newValue, oldValue, options ) {");
		list.add("    accion(false);");
		list.add("}");
		list.add("	Ext.onReady(function()");
		list.add("  {");
		list.add("   Ext.BLANK_IMAGE_URL = 'resources/images/default/s.gif';");
		
		// los campos de fecha
		if (!this.getContainerMap().isEmpty()) {
			
			Iterator<Entry<String, String>> itInst = this.getContainerMap().entrySet().iterator();
		    while (itInst.hasNext()) {
		        Map.Entry<String, String> pairs = (Map.Entry<String, String>)itInst.next();
		        String id = (String) pairs.getKey();
		        String value = (String) pairs.getValue();

				list.add("   date_" + id + " = new Ext.form.DateField({");
				list.add("   id : '" + id + "',");
				list.add("   name : '" + id + "',");
				list.add("   renderTo:'cont_" + id + "',");
				list.add("   format:'Y/m/d',");
				list.add("   minValue: '',");
				list.add("   value: '" + value + "',");
				list.add("   allowBlank:false,");
				list.add("   emptyText:'',");
//					list.add("   listeners: {change: onChange},");
				list.add("  });");
		        
				list.add("listadate['" + id + "'] = date_" + id + ";");
		    }
		}
		
		// las ventanas
		if (!this.getWindowList().isEmpty()) {


			String winName = "Mostrar";
			if (modify) {
				winName = "Modificar";
			}
			
			for(String id: this.getWindowList()) {

				list.add("if (listawindows['win_" + id + "']!=null)	{ ");
				list.add("  listawindows['win_" + id + "'].destroy();"); 
				list.add("  listawindows['win_" + id + "'] = null;");
				list.add("}");

				list.add("listawindows['win_" + id + "'] = new Ext.Window({");
				list.add("  el:'cont_win_" + id + "',");
				list.add("  title:'" + winName + "',");
				list.add("  layout:'fit',");
				list.add("  width:445,");
				list.add("  height:575,");
				list.add("  y:20,");
				list.add("  closeAction:'hide',");
				list.add("  plain: true,");

				list.add("  items:[");
				list.add("	{contentEl:'inside_win_" + id + "'}");
				list.add("  ],");

				list.add("  buttons: [{");
				list.add("	text: 'Cerrar',");
				list.add("	handler: function(){");
				list.add("  	document.getElementById('openwindow').value = '';");
				list.add("		listawindows['win_" + id + "'].hide();");
				list.add("	}");
				list.add("  }]");
				list.add("});");
			}
		}

		// los tabs
	    list.add("var tabs_tabs1 = new Ext.TabPanel({");
	    list.add("	 renderTo: 'tabs1',");
	    list.add("	 plain:true,");
	    list.add("   defaults:{autoHeight: true, autoWidth: true},");
	    list.add("   width:1400,");
	    list.add("	 activeTab:" + tabactivo + ",");
	    list.add("	 height: 450,");
	    list.add("	 items:[{");
	    list.add("		contentEl:'tab1', ");
	    list.add("		title: 'Medidas agregadas'");
	    list.add("		},{");
	    list.add("		contentEl:'tab2', ");
	    list.add("		title: 'Medidas de instancia'");
	    list.add("	 }]");
	    list.add("});");
	    
	    // el objeto con el grupo de tabs
	    list.add("objtab = tabs_tabs1;");

		if ( this.getWindowList().contains(openwindow) ) {

			list.add("openWindow(document.getElementById('btn_" + openwindow + "'), '" + openwindow + "');");
		}
	    
		list.add(" }");
		list.add("	);");

		list.add("</script>");
		
		this.print(out, list);
	}
	
	public void abrirTab1(JspWriter out) throws IOException {

		List<String> list = new ArrayList<String>();
		
		list.add("<div id=\"tabs1\" >");
		list.add("<div id=\"tab1\" class=\"x-hide-display\">");
		list.add("<div class=\"ppinot-tab-container\">");
		
		this.print(out, list);
	}
	
	public void abrirTab2(JspWriter out) throws IOException {

		List<String> list = new ArrayList<String>();
		
		list.add("</div></div>");
		list.add("<div id=\"tab2\" class=\"x-hide-display\">");
		list.add("<div class=\"ppinot-tab-container\">");
		
		this.print(out, list);
	}
	
	public void cerrarTabs(JspWriter out) throws IOException {

		List<String> list = new ArrayList<String>();
		
		list.add("</div></div></div>");
		
		this.print(out, list);
	}

	public void printHidden(JspWriter out) throws IOException {
	
		if (this.getFieldList().size()>0) {
			
			List<String> list = new ArrayList<String>();
			
			for(String var: this.getFieldList()) {
	
				list.add("<input name=\"hd_" + var + "\" type=\"hidden\" value=\"\" />");
			}
			
			this.print(out, list);
		}
	}
	
	public static void download(HttpServletResponse response, File file) throws IOException {
	
		FileInputStream stream = null;
		try {
			
		   response.setContentType("APPLICATION/OCTET-STREAM");
		   response.setHeader("Content-Disposition", "Attachment; Filename=\"" + file.getName() + "\"");

		   OutputStream os = response.getOutputStream();                   
		   stream = new FileInputStream(file);
		   int i;
		   while ((i=stream.read())!=-1)
		   {
			   os.write(i);
		   }
		   stream.close();
		   os.close();
		} finally {     
			
			if (stream != null) {
				
				stream.close();             
			}     
		} 			
	}
	
	public static void save(File file, String content) throws IOException {
		
		Writer output = new BufferedWriter(new FileWriter(file));
		try {
		
			output.write( content );
		} finally {
			
			output.close();
		}
	}
}
