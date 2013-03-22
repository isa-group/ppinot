package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de una actividad. Crea y utiliza la tabla bpms_activity.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see ActivityEntity
 */
public class ActivityDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de una actividad
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface ActivityMapper {
		
		final String CREATETABLEACTIVITY = "CREATE TABLE IF NOT EXISTS bpms_activity (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, activity_name VARCHAR(100) NOT NULL, activity_type VARCHAR(100) NOT NULL )";
		@Update(CREATETABLEACTIVITY)
		public int createTableActivity() throws Exception;
		
		final String CREATETABLEEVENT = "CREATE TABLE IF NOT EXISTS bpms_activity_event (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, start_time TIMESTAMP NOT NULL, end_time TIMESTAMP )";
		@Update(CREATETABLEEVENT)
		public int createTableActivityEvent() throws Exception;
		
		final String SELECTACTIVITYIDSBYINSTANCE = "SELECT DISTINCT(e.activity_id) as activityId, activity_name as activityName FROM bpms_activity_event as e LEFT JOIN bpms_activity as a ON e.process_def_id=a.process_def_id AND e.activity_id=a.activity_id WHERE instance_id = #{instanceId}";
		@Select(SELECTACTIVITYIDSBYINSTANCE)
		public List<ActivityEntity> selectActivityIdsByInstance(@Param("instanceId") final String instanceId) throws Exception;
		
		final String SELECTACTIVITYIDSTASKSBYINSTANCE = "SELECT DISTINCT(e.activity_id) as activityId, activity_name as activityName FROM bpms_activity_event as e LEFT JOIN bpms_activity as a ON e.process_def_id=a.process_def_id AND e.activity_id=a.activity_id WHERE instance_id = #{instanceId} AND (activity_type='userTask' OR activity_type='serviceTask')";
		@Select(SELECTACTIVITYIDSTASKSBYINSTANCE)
		public List<ActivityEntity> selectActivityIdsTasksByInstance(@Param("instanceId") final String instanceId) throws Exception;
		
		final String SELECTACTIVITYIDSBYPROCESS = "SELECT DISTINCT(a.activity_id) as activityId, activity_name as activityName FROM bpms_process as p LEFT JOIN bpms_activity AS a ON p.process_def_id=a.process_def_id WHERE process_id = #{processId}";
		@Select(SELECTACTIVITYIDSBYPROCESS)
		public List<ActivityEntity> selectActivityIdsByProcess(@Param("processId") final String processId) throws Exception;
		
		final String SELECTACTIVITYIDSTASKBYPROCESS = "SELECT DISTINCT(a.activity_id) as activityId, activity_name as activityName FROM bpms_process as p LEFT JOIN bpms_activity AS a ON p.process_def_id=a.process_def_id WHERE process_id = #{processId} AND (activity_type='userTask' OR activity_type='serviceTask')";
		@Select(SELECTACTIVITYIDSTASKBYPROCESS)
		public List<ActivityEntity> selectActivityIdsTasksByProcess(@Param("processId") final String processId) throws Exception;
		
		final String SELECTDATABYACTIVITY = "SELECT e.activity_id as activityId, activity_name as activityName, activity_type as activityType, start_time as startTime, end_time as endTime FROM bpms_activity_event AS e LEFT JOIN bpms_activity AS a ON e.process_def_id=a.process_def_id AND e.activity_id=a.activity_id WHERE instance_id = #{instanceId} AND e.activity_id=#{activityId}";
		@Select(SELECTDATABYACTIVITY)
		public List<ActivityEntity> selectDataByActivity(@Param("instanceId") final String instanceId, @Param("activityId") final String activityId) throws Exception;
	}
	
	static {
		
		// si no existe, crea las tablas en la BD 
		try {
			
			createTable();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Si no existen, crea las tablas bpms_activity y bpms_activity_event
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			mapper.createTableActivity();
			mapper.createTableActivityEvent();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id y el nombre de las actividades ejecutadas en una instancia de proceso
	 * 
	 * @param Id de la instancia de proceso
	 * @return Lista con la informacion de las actividades
	 * @throws Exception
	 */
	public static List<ActivityEntity> selectActivityIdsByInstance(String instanceId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectActivityIdsByInstance(instanceId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id y el nombre de las tareas ejecutadas en una instancia de proceso
	 * 
	 * @param Id de la instancia de proceso
	 * @return Lista con la informacion de las tareas
	 * @throws Exception
	 */
	public static List<ActivityEntity> selectActivityIdsTasksByInstance(String instanceId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectActivityIdsTasksByInstance(instanceId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id y el nombre de las actividades ejecutadas en todas las instancias de un proceso
	 * 
	 * @return Lista con la informacion de las actividades
	 * @throws Exception
	 */
	public static List<ActivityEntity> selectActivityIdsByProcess(String processId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectActivityIdsByProcess(processId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id y el nombre de las tareas ejecutadas las instancias de un proceso
	 * 
	 * @param Id en el diagrama del proceso
	 * @return Lista con la informacion de las tareas
	 * @throws Exception
	 */
	public static List<ActivityEntity> selectActivityIdsTasksByProcess(String processId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectActivityIdsTasksByProcess(processId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene la informacion de la ejecucion de una actividad en una instancia de proceso
	 * 
	 * @return Lista con la informacion de la ejecucion de la actividad en la instancia de proceso
	 * @throws Exception
	 */
	public static List<ActivityEntity> selectDataByActivity(String instanceId, String activityId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectDataByActivity(instanceId, activityId);

			return list;
		} finally {
			
			session.close();
		}
	}
}

