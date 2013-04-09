package es.us.isa.bpms.ppinot.historyreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelUtils;
import es.us.isa.ppinot.model.PPI;
import es.us.isa.ppinot.model.aggregated.AggregatedMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;

public class HistoryUtils {
	
	/**
	 * Determina el minimo de una lista de valores reales
	 * 
	 * @param list Lista
	 * @return Minimo
	 */
	private static Double min(List<Double> list) {
		
		Double result = 0.0;
		if (list.size()>0) {
			result = list.get(0);
			for(Double elem: list) {
				
				if (elem<result)
					result = elem;
			}
		}
		return result;
	}
	
	/**
	 * Determina el maximo de una lista de valores reales
	 * 
	 * @param list Lista
	 * @return Maximo
	 */
	private static Double max(List<Double> list) {
		
		Double result = 0.0;
		if (list.size()>0) {
			result = list.get(0);
			for(Double elem: list) {
				
				if (elem>result)
					result = elem;
			}
		}
		return result;
	}
	
	/**
	 * Determina la suma de una lista de valores reales
	 * 
	 * @param list Lista
	 * @return Suma
	 */
	private static Double sum(List<Double> list) {
		
		Double result = 0.0;
		for(Double elem: list) {
			
			result += elem;
		}
		return result;
	}
	
	/**
	 * Determina el promedio de una lista de valores reales
	 * 
	 * @param list Lista
	 * @return Promedio
	 */
	private static Double ave(List<Double> list) {
		
		return sum(list) / list.size();
	}

	/**
	 * Aplica la funcion indicada a una lista de valores reales
	 * 
	 * @param func Funcion (MinAM, MaxAM, SumAM o AverageAM)
	 * @param list Lista
	 * @return Resultado
	 */
	static Double applyFunc(String func, List<Double> list) {

		Double result = 0.0;
		if (func.contentEquals(HistoryConst.MIN))
			result = min(list);
		else
		if (func.contentEquals(HistoryConst.MAX))
			result = max(list);
		else
		if (func.contentEquals(HistoryConst.SUM))
			result = sum(list);
		else
		if (func.contentEquals(HistoryConst.AVE))
			result = ave(list);
		
		return result;
	}
	
	/**
	 * Cuenta la cantidad de elementos de una lista que son true
	 * 
	 * @param list Lista
	 * @return Cantidad
	 */
	static  Double count(List<Boolean> list) {

		Double count = 0.0;
		for(Boolean elem: list) {
			
			if (elem)
				count++;
		}
		return count;
	}

	static Map<Date, Date> createDateMap(Date startDate, Date endDate, String year, String period) {
		
		Map<Date, Date> dateList = new LinkedHashMap<Date, Date>();
		if (startDate!=null || endDate!=null) {
			
			dateList.put( startDate, endDate );
		} else 
		if ( year!="") {
			
			if (period.contentEquals("mes")) {
				// por meses
				Integer feb = 28;
				if (PpiNotModelUtils.currentYear()%4==0)
					feb = 29;

				dateList.put( PpiNotModelUtils.parseDate( year + "/01/01" ), PpiNotModelUtils.parseDate( year + "/01/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/02/01" ), PpiNotModelUtils.parseDate( year + "/02/" + feb ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/03/01" ), PpiNotModelUtils.parseDate( year + "/03/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/04/01" ), PpiNotModelUtils.parseDate( year + "/04/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/05/01" ), PpiNotModelUtils.parseDate( year + "/05/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/06/01" ), PpiNotModelUtils.parseDate( year + "/06/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/07/01" ), PpiNotModelUtils.parseDate( year + "/07/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/08/01" ), PpiNotModelUtils.parseDate( year + "/08/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/09/01" ), PpiNotModelUtils.parseDate( year + "/09/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/10/01" ), PpiNotModelUtils.parseDate( year + "/10/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/11/01" ), PpiNotModelUtils.parseDate( year + "/11/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/12/01" ), PpiNotModelUtils.parseDate( year + "/12/31" ) );
			} else
			if (period.contentEquals("trimestre")) {
				// por trimestre
				dateList.put( PpiNotModelUtils.parseDate( year + "/01/01" ), PpiNotModelUtils.parseDate( year + "/03/31" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/04/01" ), PpiNotModelUtils.parseDate( year + "/06/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/07/01" ), PpiNotModelUtils.parseDate( year + "/09/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/10/01" ), PpiNotModelUtils.parseDate( year + "/12/31" ) );
			} else
			if (period.contentEquals("semestre")) {
				// por semestre
				dateList.put( PpiNotModelUtils.parseDate( year + "/01/01" ), PpiNotModelUtils.parseDate( year + "/06/30" ) );
				dateList.put( PpiNotModelUtils.parseDate( year + "/07/01" ), PpiNotModelUtils.parseDate( year + "/12/31" ) );
			} else {
				// todo el año
				dateList.put( PpiNotModelUtils.parseDate( year + "/01/01" ), PpiNotModelUtils.parseDate( year + "/12/31" ) );
			}
			
		} else {
			
			dateList.put( null, null );
		}
		
		return dateList;
	}


	/**
	 * Aplica la funcion indicada a los valores de PPI en un periodo determinado
	 * 
	 * @param func Funcion (Doble o Cuadrado)
	 * @param periodo Periodo en el que se quiere aplica la funcion
	 * @param ppiMap Mapa con los PPI que participan
	 * @return Resultado
	 */
	static Double applyFunc(String func, Integer periodo, Map<String, PPI> ppiMap) {

		Double result = 0.0;
		Pattern patron = Pattern.compile("(\\w*|[0-9]*)(\\-|\\+|\\*|/)(\\w*|[0-9]*)");
		Matcher matcher = patron.matcher( func );
		matcher.find();

//System.out.println("patron "+matcher.group(0)+", "+matcher.group(1)+", "+matcher.group(2)+", "+matcher.group(3));
		if (matcher.group(1)!=null && matcher.group(2)!=null && matcher.group(3)!=null) {
		
			Double operando1;
			if (ppiMap.containsKey(matcher.group(1)))
				operando1 = Double.valueOf(ppiMap.get(matcher.group(1)).getValueString().get(periodo));
			else
				operando1 = Double.valueOf(matcher.group(1));
			
			Double operando2;
			if (ppiMap.containsKey(matcher.group(3)))
				operando2 = Double.valueOf(ppiMap.get(matcher.group(3)).getValueString().get(periodo));
			else
				operando2 = Double.valueOf(matcher.group(3));
			
			String operador = matcher.group(2);

//System.out.println("expresion "+operando1 + operador + operando2);
			if (operador.contentEquals("+"))
				result = operando1 + operando2;
			else
			if (operador.contentEquals("-"))
				result = operando1 - operando2;
			else
			if (operador.contentEquals("*"))
				result = operando1 * operando2;
			else
				if (operador.contentEquals("/"))
					result = operando1 / operando2;
		} 
//System.out.println("valor "+result);
		return result;
	}
	
	private static String generateDataPattern(int count, String dataobjectId, String property) {

		String epl = "";
		epl += "@Name('DataMeasure" + count + "')\r\n";
		epl += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.DataMeasureListener')\r\n";
		epl += "select processDefId, instanceId, executionId, activityId, time, dataobjectId, '" + property + "' as property, propertyMap('" + property + "').value as value, propertyMap('" + property + "').type as type from DataobjectEvent(propertyMap('" + property + "').propertyId='" + property + "') where dataobjectId='" + dataobjectId + "';\r\n";
		return epl;
	}
	
	private static String generateDataConditionPattern(int count, String dataobjectId, String state) {

		String epl = "";
		epl += "@Name('DataConditionMeasure" + count + "_true')\r\n";
		epl += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.DataConditionMeasureListener')\r\n";
		epl += "select processDefId, instanceId, executionId, activityId, time, dataobjectId, '" + state + "' as state, true as truth from DataobjectEvent(stateMap('aprobacion').stateId='" + state + "') where dataobjectId='" + dataobjectId + "';\r\n";

		epl += "@Name('DataConditionMeasure" + count + "_false')\r\n";
		epl += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.DataConditionMeasureListener')\r\n";
		epl += "select processDefId, instanceId, executionId, activityId, time, dataobjectId, '" + state + "' as state, false as truth from DataobjectEvent(stateMap('aprobacion').stateId!='" + state + "') where dataobjectId='" + dataobjectId + "';\r\n";
		return epl;
	}

	public static String[] generateEpl(String camino, String nomFich) throws JAXBException {
		
		// crea el objeto para leer el xml
		PpiNotModelHandler ppiNotXmlExtracter = new PpiNotModelHandler();
		ppiNotXmlExtracter.load(camino, nomFich);
		
		String eplName = "module-" + ppiNotXmlExtracter.getProcId() + ".epl";
		
		String eplContent = "";
		eplContent += "module " + ppiNotXmlExtracter.getProcId() + ";\r\n";
		
		eplContent += "@Name('ProcessStart1')\r\n";
		eplContent += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.ProcessStartListener')\r\n";
		eplContent += "select * from ProcessStartEvent;\r\n";

		eplContent += "@Name('ProcessEnd1')\r\n";
		eplContent += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.ProcessEndListener')\r\n";
		eplContent += "select * from ProcessEndEvent;\r\n";

		eplContent += "@Name('ActivityStart1')\r\n";
		eplContent += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.ActivityStartListener')\r\n";
		eplContent += "select * from ActivityStartEvent;\r\n";

		eplContent += "@Name('ActivityEnd1')\r\n";
		eplContent += "@Tag(name='listeners', value='es.us.isa.bpms.esper.listener.ActivityEndListener')\r\n";
		eplContent += "select * from ActivityEndEvent;\r\n";

		List<String> list = new ArrayList<String>();
		int count = 1;
	    Iterator<Entry<String, DataInstanceMeasure>> itInst = ppiNotXmlExtracter.getDataInstanceModelMap().entrySet().iterator();
	    while (itInst.hasNext()) {
	        Map.Entry<String, DataInstanceMeasure> pairs = (Map.Entry<String, DataInstanceMeasure>)itInst.next();
	        DataInstanceMeasure measure = (DataInstanceMeasure) pairs.getValue();
			
			String dataobjectId = measure.getDataContentSelection().getDataobject();
			String property = measure.getDataContentSelection().getSelection();
			
			if (!list.contains(dataobjectId + "-" + property)) {
				
				eplContent += generateDataPattern(count, dataobjectId, property);
	
				list.add(dataobjectId + "-" + property);
				count++;
			}
		}

	    Iterator<Entry<String, AggregatedMeasure>> itInst1 = ppiNotXmlExtracter.getDataAggregatedModelMap().entrySet().iterator();
	    while (itInst1.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs1 = (Map.Entry<String, AggregatedMeasure>)itInst1.next();
	        AggregatedMeasure measure = (AggregatedMeasure) pairs1.getValue();
			
			String dataobjectId = ((DataInstanceMeasure) measure.getBaseMeasure()).getDataContentSelection().getDataobject();
			String property = ((DataInstanceMeasure) measure.getBaseMeasure()).getDataContentSelection().getSelection();
			
			if (!list.contains(dataobjectId + "-" + property)) {
				
				eplContent += generateDataPattern(count, dataobjectId, property);
	
				list.add(dataobjectId + "-" + property);
				count++;
			}
		}

		list.clear();
		count = 1;
	    Iterator<Entry<String, DataPropertyConditionInstanceMeasure>> itInst2 = ppiNotXmlExtracter.getDataPropertyConditionInstanceModelMap().entrySet().iterator();
	    while (itInst2.hasNext()) {
	        Map.Entry<String, DataPropertyConditionInstanceMeasure> pairs2 = (Map.Entry<String, DataPropertyConditionInstanceMeasure>)itInst2.next();
	        DataPropertyConditionInstanceMeasure measure = (DataPropertyConditionInstanceMeasure) pairs2.getValue();
			
			String dataobjectId = measure.getCondition().getDataobject();
			String state = measure.getCondition().getStateConsidered().getStateString();
			
			if (!list.contains(dataobjectId + "-" + state)) {
				
				eplContent += generateDataConditionPattern(count, dataobjectId, state);
	
				list.add(dataobjectId + "-" + state);
				count++;
			}
		}

	    Iterator<Entry<String, AggregatedMeasure>> itInst3 = ppiNotXmlExtracter.getDataPropertyConditionAggregatedModelMap().entrySet().iterator();
	    while (itInst3.hasNext()) {
	        Map.Entry<String, AggregatedMeasure> pairs3 = (Map.Entry<String, AggregatedMeasure>)itInst3.next();
	        AggregatedMeasure measure = (AggregatedMeasure) pairs3.getValue();
			
			String dataobjectId = ((DataPropertyConditionInstanceMeasure) measure.getBaseMeasure()).getCondition().getDataobject();
			String state = ((DataPropertyConditionInstanceMeasure) measure.getBaseMeasure()).getCondition().getStateConsidered().getStateString();
			
			if (!list.contains(dataobjectId + "-" + state)) {
				
				eplContent += generateDataConditionPattern(count, dataobjectId, state);
	
				list.add(dataobjectId + "-" + state);
				count++;
			}
		}
		
		String[] r = {eplName, eplContent};
		return r;
	}
	
}
