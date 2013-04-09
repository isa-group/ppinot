package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.ppinot.handler.PpiNotModelUtils;

public final class HistoryData {

	/**
	 * Devuelve los ids de los procesos de los que existe informacion
	 * 
	 * @return Mapa con los ids de los procesos
	 */
	public static Map<String, String> getProcessNames() {
		
		Map<String, String> map = new LinkedHashMap<String, String>(); 
		try {
			
		    for(ProcessEntity processEntity : ProcessDAO.selectDistinctProcessId()) {
		    		
		    	String processId = processEntity.getProcessId();
			   	map.put( processId, processId );
		    }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Devuelve la informacion de las instancias de un proceso en un periodo de tiempo
	 * 
	 * @param processId Id del proceso en el diagrama 
	 * @param startDate Fecha inicial del periodo
	 * @param endDate Fecha final del periodo
	 * @param inStart Si se incluyen las instancias de proceso que fueron iniciadas antes de la fecha inicial y que terminan despues de la fecha inicial 
	 * @param inEnd Si se incluyen las instancias de proceso que fueron iniciadas antes de la fecha final y que terminan despues de la fecha final
	 * @return Mapa con los ids y la informacion de las instancias de procesos
	 */
	public static Map<String, ProcessEntity> getProcessInstanceData(String processId, Date startDate, Date endDate, Boolean inStart, Boolean inEnd) {
	    
		Map<String, ProcessEntity> map = new LinkedHashMap<String, ProcessEntity>(); 
		try {
	    
			// crea una lista con los datos de las instancias del proceso correspondientes
			String startDateString = PpiNotModelUtils.formatString( startDate );
			String endDateString = PpiNotModelUtils.formatString( endDate );
			
		    for(ProcessEntity entity : ProcessDAO.selectDataByProcess(processId)) {
	
				String startDateStringInst = PpiNotModelUtils.formatString( entity.getStartTime() );
				String endDateStringInst = PpiNotModelUtils.formatString( entity.getEndTime() );
	
		    	if ( ( startDate==null || 
		    		   ( inStart && ( entity.getEndTime()==null || endDateStringInst.compareTo(startDateString)>=0 ) ) ||  
		    		   startDateStringInst.compareTo(startDateString)>=0 ) &&
		    		 ( endDate==null || 
		    		   ( inEnd && startDateStringInst.compareTo(endDateString)<=0 ) || 
		    		   ( entity.getEndTime()!=null && endDateStringInst.compareTo(endDateString)<=0 ) )
		    		) {
	
			    	map.put( entity.getInstanceId(), entity );
		    	}
		    }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * Devuelve los nombres de las actividades ejecutadas en una instancia de proceso
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @return Mapa de ids y nombres de las actividades
	 */
	public static Map<String, String> getInstanceActivityNames(String instanceId) {
	    
		Map<String, String> map = new LinkedHashMap<String, String>(); 
		try {
			
		    for(ActivityEntity entity : ActivityDAO.selectActivityIdsByInstance(instanceId)) {
		    	
		    	map.put(entity.getActivityId(), entity.getActivityName() );
		    }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Devuelve los nombres de las actividades ejecutadas en un proceso
	 * 
	 * @param processId Id en el diagrama del proceso
	 * @return Mapa de ids y nombres de las actividades
	 */
	public static Map<String, String> getProcessActivityNames(String processId) {
	    
		Map<String, String> map = new LinkedHashMap<String, String>(); 
		try {
			
		    for(ActivityEntity entity : ActivityDAO.selectActivityIdsByProcess(processId)) {
		    	
		    	map.put(entity.getActivityId(), entity.getActivityName() );
		    }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Devuelve la informacion de la ejecucion de una actividad en una instancia de proceso.
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @param activityId Id de la actividad
	 * @return Lista con la informacion de la ejecucion de la actividad
	 */
	public static List<ActivityEntity> getInstanceActivityData(String instanceId, String activityId) {

		try {
			
			return ActivityDAO.selectDataByActivity(instanceId, activityId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Devuelve los datos de una instancia de proceso en una plataforma BPM
	 * 
	 * @param instanceId Id de una instancia de proceso
	 * @return Datos de la instancia
	 */
	public static ProcessEntity getAProcessInstance(String instanceId) {
		
		try {
			
			return ProcessDAO.selectDataByInstance(instanceId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Devuelve los nombres de las tareas ejecutadas una instancia de proceso
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @return Mapa de los id y los nombres de las tareas
	 */
	public static Map<String, String> getInstanceTaskNames(String instanceId) {
		
		Map<String, String> map = new LinkedHashMap<String, String>(); 
	    try {
	    	
			for(ActivityEntity entity : ActivityDAO.selectActivityIdsTasksByInstance(instanceId)) {
				
				map.put(entity.getActivityId(), entity.getActivityName() );
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	    return map;
	}
	
	/**
	 * Devuelve los nombres de las tareas ejecutadas de un proceso
	 * 
	 * @param instanceIdMap Id en el diagrama del proceso
	 * @return Mapa de los id y los nombres de las tareas
	 */
	public static Map<String, String> getProcessTaskNames(String processId) {
		
		Map<String, String> map = new LinkedHashMap<String, String>(); 
	    try {
	    	
			for(ActivityEntity entity : ActivityDAO.selectActivityIdsTasksByProcess(processId)) {
				
				map.put(entity.getActivityId(), entity.getActivityName() );
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	    return map;
	}
	
	/**
	 * Devuelve los nombres de los posibles estados de las tareas
	 * 
	 * @return Mapa con los posibles estados de tareas
	 */
	public static Map<String, String> getStateTaskNames() {

		Map<String, String> taskStateMap = new LinkedHashMap<String, String>();
		taskStateMap.put("completed", "Completada");
		taskStateMap.put("executing", "En ejecuci&oacute;n");
		taskStateMap.put("deleted", "Eliminada");

		return taskStateMap;
	}
	
	/**
	 * Indica si la tarea se encuentra en el estado state o no
	 * 
	 * @param taskData Objeto con los datos de la tarea
	 * @param state Estado consultado
	 * @return Si la tarea se encuentra en el estado solicitado o no
	 */
	public static Boolean taskInState(ActivityEntity entity, String state) {
		
		return (entity.getEndTime()==null && state.contentEquals("executing")) || 
		(entity.getEndTime()!=null && state.contentEquals("completed") );
	}
	
	/**
	 * Devuelve los nombres de los dataobject en una instancia de proceso 
	 * 
	 * @param instanceId Id de una instancia de proceso
	 * @return Lista de identificadores de dataobjects
	 */
	public static Map<String, String> getInstanceDataNames(String instanceId)  {
		
		Map<String, String> map = new LinkedHashMap<String, String>(); 
	    try {
	    	
			for(DataobjectEntity dataobject : DataobjectDAO.selectDataobjectIdsByInstance(instanceId)) {
				
				map.put(dataobject.getDataobjectId(), dataobject.getDataobjectId() );
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	    return map;
	}
	
	/**
	 * Devuelve los nombres de los dataobject en una instancia de proceso 
	 * 
	 * @param instanceIdMap Id de una instancia de proceso
	 * @return Lista de identificadores de dataobjects
	 */
	public static Map<String, String> getProcessDataNames(String processId)  {
		
		Map<String, String> map = new LinkedHashMap<String, String>(); 
	    try {
	    	
			for(DataobjectEntity dataobject : DataobjectDAO.selectDataobjectIdsByProcess(processId)) {
				
				if (dataobject!=null)
					map.put(dataobject.getDataobjectId(), dataobject.getDataobjectId() );
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	    return map;
	}
	
	/**
	 * Devuelve informacion de una propiedad de un dataobject en la instancia de proceso instanceId
	 * 
	 * @param instanceId Id de una instancia de proceso
	 * @param dataobjectId Id del dataobject
	 * @param property Id de la propiedad
	 * @return Informacion de la propiedad
	 */
	public static DataobjectEntity getInstanceDataobjectData(String instanceId, String dataobjectId, String property) {
		
		try {
			
			return DataobjectDAO.selectDataByProperty(instanceId, dataobjectId, property);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Devuelve informacion de un estado de un dataobject en la instancia de proceso instanceId
	 * 
	 * @param instanceId Id de una instancia de proceso
	 * @param dataobjectId Id del dataobjectcc
	 * @param state Id del estado
	 * @return Informacion de la propiedad
	 */
	public static DataConditionEntity getInstanceDataConditionData(String instanceId, String dataobjectId, String state) {
		
		try {
			return DataConditionDAO.selectDataByState(instanceId, dataobjectId, state);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Devuelve los nombres de las propiedades de un dataobject en un proceso
	 * 
	 * @param processId Id del proceso
	 * @param dataobjectId Id del dataobject
	 * @return Lista de las claves las propiedades del dataobject
	 */
	public static Map<String, String> getDataobjectPropNames(String processId, String dataobjectId) {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
	    try {
			
	    	for (DataobjectEntity entity : DataobjectDAO.selectDataobjectPropertyIdsByProcess(processId, dataobjectId)) {
			    
				map.put(entity.getProperty(), entity.getProperty());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return map;
	}

	/**
	 * Devuelve los nombres de los estados del dataobject dataobjectId
	 * 
	 * @param processId Id del proceso
	 * @param dataobjectId Id del dataobject
	 * @return Lista de las claves de los estados del dataobject
	 */
	public static Map<String, String> getDataobjectStateNames(String processId, String dataobjectId) {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
	    try {
			
	    	for (DataConditionEntity entity : DataConditionDAO.selectDataobjectStateIdsByProcess(processId, dataobjectId)) {
			    
				map.put(entity.getState(), entity.getState());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * Devuelve la ultima ocurrencia en la historia del dataobject solicitado, en el periodo de tiempo indicado.
	 * 
	 * @param dataobjectId Key del dataobject.
	 * @param startDate Fecha de inicio
	 * @param endDate Fecha final
	 * @param hasta Si se desea la oltima ocurrencia hasta ese periodo o en ese periodo.
	 * @return Dataobject
	 */
	public static DataobjectEntity getLast(String processId, String dataobjectId, Date startDate, Date endDate, String property) {
		
		try {
			
			return DataobjectDAO.selectDataIntervalByProcess(processId, dataobjectId, startDate, endDate, property).get(0);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
