package es.us.isa.bpms.ppinot.historyreport;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.us.isa.bpms.ppinot.historyreport.db.ActivityEntity;
import es.us.isa.bpms.ppinot.historyreport.db.DataobjectEntity;
import es.us.isa.bpms.ppinot.historyreport.db.HistoryData;
import es.us.isa.bpms.ppinot.historyreport.db.ProcessEntity;
import es.us.isa.ppinot.model.base.CountInstanceMeasure;
import es.us.isa.ppinot.model.base.DataInstanceMeasure;
import es.us.isa.ppinot.model.base.DataPropertyConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.StateConditionInstanceMeasure;
import es.us.isa.ppinot.model.base.TimeInstanceMeasure;
import es.us.isa.ppinot.model.state.GenericState;

class HistoryDoMeasure {

	/**
	 * Determina el momento en que comenzo o termino la actividad indicada en la instancia de proceso indicada. 
	 * Si se solicita el momento de inicio se toma el de la primera vez que se ejecuta y si se solicita el momento en que 
	 * termino se toma el de la ultima vez que se ejecuto.
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param activityId Id de la actividad
	 * @param atEnd Indica si se determina el momento de inicio o fin de la actividad
	 * @return La fecha y hora
	 * @throws NullTimeException 
	 */
	static Date doActivityTimeInstanceCondition(String instanceId, String activityId, Boolean atEnd) throws NullTimeException {
		
		// obtiene la lista con los datos de la actividad activityId en la instancia de proceso instanceId
		List<ActivityEntity> lista = HistoryData.getInstanceActivityData(instanceId, activityId);
		
		// determina la fecha a devolver en dependencia del indicador atEnd
		Date momento;
		if (atEnd)
			momento = lista.get( lista.size()-1 ).getEndTime();
		else
			momento = lista.get(0).getStartTime();
		
		if (momento==null)
			throw new NullTimeException("Tiempo no definido");

		return momento;
	}
	
	/**
	 * Determina el momento en que comenzo o termino la imstancia de proceso indicada
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param atEnd Indica si se determina el momento de inicio o fin de la instancia de proceso
	 * @return La fecha y hora
	 * @throws NullTimeException 
	 */
	static Date doPoolTimeInstanceCondition(String instanceId, Boolean atEnd) throws NullTimeException {
		
		// obtiene los datos de la instancia de proceso instanceId
		ProcessEntity processEntity = HistoryData.getAProcessInstance(instanceId);

		// determina la fecha a devolver en dependencia del indicador atEnd
		Date momento;
		if (atEnd)
			momento = processEntity.getEndTime();
		else
			momento = processEntity.getStartTime();
		
		if (momento==null)
			throw new NullTimeException("Tiempo no definido");
		
		return momento;
	}
	
	/**
	 * Calcula en una instancia de proceso el tiempo transcurrido entre la primera ejecucion de la actividad inicial hasta
	 * la ultima ejecucion de la actividad final. Se toma la medida desde el inicio de la actividad o no, en dependencia de 
	 * lo indicado en los parametros 
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param activityIdIni Id de la actividad inicial
	 * @param atEndIni Si en la actividad inicial se mide desde el final o no
	 * @param activityIdFin Id de la actividad final
	 * @param atEndfin Si en la actividad final se mide desde el final o no
	 * @return Tiempo transcurrido en milisegundos
	 * @throws NullTimeException 
	 * @throws NullActivityException 
	 */
	static long doTimeMeasure(String instanceId, String activityIdIni, Boolean atEndIni, String activityIdFin, Boolean atEndFin) throws NullTimeException, NullActivityException {

		// obtiene la lista con los datos de la actividad activityIdIni en la instancia de proceso instanceId
		List<ActivityEntity> listaIni = HistoryData.getInstanceActivityData(instanceId, activityIdIni);
		
		if (listaIni.size()==0)
			throw new NullActivityException("Actividad inicial no ejecutada en la instancia de proceso");
		
		// determina la fecha solicitada para la actividad inicial
		Date timeIni;
		if (atEndIni)
			timeIni = listaIni.get(0).getEndTime();
		else
			timeIni = listaIni.get(0).getStartTime();
		
		if (timeIni==null)
			throw new NullTimeException("Tiempo no definido");
		
		// obtiene la lista con los datos de la actividad activityIdFin en la instancia de proceso instanceId
		List<ActivityEntity> listaFin = HistoryData.getInstanceActivityData(instanceId, activityIdFin);
		
		if (listaFin.size()==0)
			throw new NullActivityException("Actividad final no ejecutada en la instancia de proceso");
		
		// determina la fecha solicitada para la actividad final
		Date timeFin;
		if (atEndFin)
			timeFin = listaFin.get( listaFin.size()-1 ).getEndTime();
		else
			timeFin = listaFin.get( listaFin.size()-1 ).getStartTime();
		
		if (timeFin==null)
			throw new NullTimeException("Tiempo no definido");
		
		// calcula el tiempo transcurrido entre un momento y otro
		long elapsedTime = timeFin.getTime() - timeIni.getTime();
		
		return elapsedTime;
	}
	
	/**
	 * Cuenta la cantidad de veces que se ejecuta el inicio o el fin de la actividad activityId en la instancia de proceso instanceId.
	 * Se indica si se desea contar el inicio o el fin mediante atEnd.
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param activityId Id de la actividad
	 * @param atEnd Indica si se cuenta el inicio o el fin de la actividad
	 * @return
	 */
	static int doCountMeasure(String instanceId, String activityId, Boolean atEnd) {
		
		// obtiene la lista con los datos de la actividad activityId en la instancia de proceso instanceId
		List<ActivityEntity> lista = HistoryData.getInstanceActivityData(instanceId, activityId);
		
		// cuenta la cantidad de veces que se inicia o termina la actividad, en dependencia de los solicitado
		int count = 0;
		for (ActivityEntity activityData: lista) {
			if (atEnd) {
				if (activityData.getEndTime()!=null)
					count++;
			}
			else {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Determina el estado de una tarea la ultima vez que se ejecuto
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param taskId Id de la tarea
	 * @param state Estado que se desea consultar
	 * @return Indica si la tarea se encuentra en el estado seleccionado o no
	 * @throws NullActivityException 
	 */
	static Boolean doStateConditionMeasure(String instanceId, String taskId, String state) throws NullActivityException {
		
		// obtiene la lista con los datos de la tarea taskId en la instancia de proceso instanceId
		List<ActivityEntity> lista = HistoryData.getInstanceActivityData(instanceId, taskId);
		
		try {
			return HistoryData.taskInState( lista.get( lista.size()-1 ), state);
		} catch (Exception e) {
			
			throw new NullActivityException("Tarea no ejecutada en la instancia de proceso");
		}
	}
	
	/**
	 * Devuelve el valor de una propiedad del dataobject dataobjectId en la instancia de proceso instanceId
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param dataobjectId Id del dataobject
	 * @param property Id de la propiedad
	 * @return Valor de la propiedad
	 */
	static Object doDataMeasure(String instanceId, String dataobjectId, String property) {
		
		// OJO hay que incluirle que se pueda solicitar la ultima ocurrencia hasta una fecha determinada
		return HistoryData.getInstanceDataobjectData(instanceId, dataobjectId, property).getValue();
	}
	
	/**
	 * Indica si el dataobject dataobjectId en su ultima actualizacion se encuentra en el estado state o no
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param dataobjectId Id del dataobject
	 * @param state Id del estado que se desea comprobar
	 * @return Indica si el dataobject se encuentra en el estado indicado o no
	 */
	static Boolean doDataPropertyConditionMeasure(String instanceId, String dataobjectId, String state) {

		if (HistoryData.getInstanceDataConditionData(instanceId, dataobjectId, state)!=null)
			return HistoryData.getInstanceDataConditionData(instanceId, dataobjectId, state).getTruth();
		else
			return false;
	}
	
	/**
	 * Calcula una medida agregada de TimeInstanceMeasure
	 * 
	 * @param processId Id del proceso
	 * @param func Funcion a aplicar
	 * @param startDate Fecha de inicio del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias cuya fecha de inicio sea anterior a la fecha inicial del periodo y la final posterior
	 * @param inEnd Si se incluyen las instancias cuya fecha final sea posterior a la fecha final del periodo y la inicial anterior
	 * @param measureDef Medida de instancia que se agrega
	 * @return Valor de la medida
	 */
	static Double doTimeAggregatedMeasure(String processId, String func, Date startDate, Date endDate, Boolean inStart, Boolean inEnd, TimeInstanceMeasure measureDef) {

		Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData(processId, startDate, endDate, inStart, inEnd);
		
		Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
		List<Double> list = new ArrayList<Double>(); 
	    while (itInst.hasNext()) {
	        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
	        String instanceId = (String) pairs.getKey();
	        
	        try {
				list.add( (double) doTimeMeasure( instanceId, 
									measureDef.getFrom().getAppliesTo(), measureDef.getFrom().getChangesToState().getState()==GenericState.END, 
									measureDef.getTo().getAppliesTo(), measureDef.getTo().getChangesToState().getState()==GenericState.END ) );
			} catch (NullTimeException e) {
				// las instancias con actividades (de las solicitadas) en ejecucion no son incluidas en el resultado
			} catch (NullActivityException e) {
				// las instancias con actividades (de las solicitadas) que no han sido ejecutadas no son incluidad en el resultado
			}
	    }
		
		return HistoryUtils.applyFunc(func, list);
	}
	
	/**
	 * Calcula una medida agregada del tipo CountAggregatedMeasure
	 * 
	 * @param processId Id del proceso
	 * @param func Funcion a aplicar
	 * @param startDate Fecha de inicio del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias cuya fecha de inicio sea anterior a la fecha inicial del periodo y la final posterior
	 * @param inEnd Si se incluyen las instancias cuya fecha final sea posterior a la fecha final del periodo y la inicial anterior
	 * @param measureDef Medida de instancia que se agrega
	 * @return Valor de la medida
	 */
	static Double doCountAggregatedMeasure(String processId, String func, Date startDate, Date endDate, Boolean inStart, Boolean inEnd, CountInstanceMeasure measureDef) {

		Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData(processId, startDate, endDate, inStart, inEnd);
		
		Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
		List<Double> list = new ArrayList<Double>(); 
	    while (itInst.hasNext()) {
	        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
	        String instanceId = (String) pairs.getKey();
	        
	        list.add( (double) doCountMeasure( instanceId, 
	        					measureDef.getWhen().getAppliesTo(), measureDef.getWhen().getChangesToState().getState()==GenericState.END ) );
	    }

		return HistoryUtils.applyFunc(func, list);
	}
	
	/**
	 * Calcula una medida agregada del tipo ElementConditionAggregatedMeasure
	 * 
	 * @param processId Id del proceso
	 * @param func Funcion a aplicar
	 * @param startDate Fecha de inicio del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias cuya fecha de inicio sea anterior a la fecha inicial del periodo y la final posterior
	 * @param inEnd Si se incluyen las instancias cuya fecha final sea posterior a la fecha final del periodo y la inicial anterior
	 * @param measureDef Medida de instancia que se agrega
	 * @return Valor de la medida
	 */
	static Double doStateConditionAggregatedMeasure(String processId, String func, Date startDate, Date endDate, Boolean inStart, Boolean inEnd, StateConditionInstanceMeasure measureDef) {

		Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData(processId, startDate, endDate, inStart, inEnd);
		
		Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
		List<Boolean> list = new ArrayList<Boolean>(); 
	    while (itInst.hasNext()) {
	        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
	        String instanceId = (String) pairs.getKey();
	        
	        try {
				
				list.add( doStateConditionMeasure( instanceId, 
						measureDef.getCondition().getAppliesTo(), measureDef.getCondition().getState().getStateString() ) );
				
			} catch (NullActivityException e) {
				// el caso de las tareas no ejecutadas en la instancia de proceso analizada
			}
	    }

		return HistoryUtils.count(list);
	}
	
	/**
	 * Calcula una medida agregada del tipo DataAggregatedMeasure
	 * 
	 * @param processId Id del proceso
	 * @param func Funcion a aplicar
	 * @param startDate Fecha de inicio del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias cuya fecha de inicio sea anterior a la fecha inicial del periodo y la final posterior
	 * @param inEnd Si se incluyen las instancias cuya fecha final sea posterior a la fecha final del periodo y la inicial anterior
	 * @param measureDef Medida de instancia que se agrega
	 * @return Valor de la medida
	 */
	static Object doDataAggregatedMeasure(String processId, String func, Date startDate, Date endDate, Boolean inStart, Boolean inEnd, DataInstanceMeasure measureDef) {

		if (func.contentEquals("LastAM")) {
			
			DataobjectEntity entity = HistoryData.getLast(processId, measureDef.getDataContentSelection().getDataobject(), startDate, endDate, measureDef.getDataContentSelection().getSelection());

			return entity.getValue();
		} else {
			
			Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData(processId, startDate, endDate, inStart, inEnd);
			
			Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
			List<Double> list = new ArrayList<Double>(); 
		    while (itInst.hasNext()) {
		        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
		        String instanceId = (String) pairs.getKey();
		        
		        try {
	
			        list.add( Double.valueOf( String.valueOf( doDataMeasure( instanceId, measureDef.getDataContentSelection().getDataobject(), measureDef.getDataContentSelection().getSelection() ) ) ) );
		        } catch (Exception e) {
		        	
		        	list.add(0.0);
		        }
		    }
	
			return HistoryUtils.applyFunc(func, list);
		}
	}
	
	/**
	 * Calcula una medida agregada del tipo DataConditionAggregatedMeasure
	 * 
	 * @param processId Id del proceso
	 * @param func Funcion a aplicar
	 * @param startDate Fecha de inicio del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias cuya fecha de inicio sea anterior a la fecha inicial del periodo y la final posterior
	 * @param inEnd Si se incluyen las instancias cuya fecha final sea posterior a la fecha final del periodo y la inicial anterior
	 * @param measureDef Medida de instancia que se agrega
	 * @return Valor de la medida
	 */
	static Double doDataPropertyConditionAggregatedMeasure(String processId, String func, Date startDate, Date endDate, Boolean inStart, Boolean inEnd, DataPropertyConditionInstanceMeasure measureDef) {

		Map<String, ProcessEntity> map = HistoryData.getProcessInstanceData(processId, startDate, endDate, inStart, inEnd);
		
		Iterator<Entry<String, ProcessEntity>> itInst = map.entrySet().iterator();
		List<Boolean> list = new ArrayList<Boolean>(); 
	    while (itInst.hasNext()) {
	        Map.Entry<String, ProcessEntity> pairs = (Map.Entry<String, ProcessEntity>)itInst.next();
	        String instanceId = (String) pairs.getKey();
	        
	        list.add( doDataPropertyConditionMeasure( instanceId, measureDef.getCondition().getDataobject(), measureDef.getCondition().getStateConsidered().getStateString() ) );
	    }

		return HistoryUtils.count(list);
	}

	
}
