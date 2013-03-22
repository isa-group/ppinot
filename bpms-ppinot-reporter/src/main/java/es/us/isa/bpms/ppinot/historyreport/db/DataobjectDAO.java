package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de una propiedad de un dataobject en una instancia de proceso. 
 * Crea y utiliza la tabla bpms_dataobject.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see DataobjectEntity
 */
public class DataobjectDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de una propiedad de un dataobject en una instancia de proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface DataobjectMapper {
		
		final String CREATETABLE = "CREATE TABLE IF NOT EXISTS bpms_dataobject (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, dataobject_id VARCHAR(100) NOT NULL, property VARCHAR(100) NOT NULL, value VARCHAR(100) NOT NULL, type VARCHAR(100) NOT NULL, time TIMESTAMP NOT NULL )";
		@Update(CREATETABLE)
		public int createTable() throws Exception;

		final String SELECTDATAOBJECTIDSBYINSTANCE = "SELECT DISTINCT(dataobject_id) as dataobjectId FROM bpms_dataobject WHERE instance_id = #{instanceId}";
		@Select(SELECTDATAOBJECTIDSBYINSTANCE)
		public List<DataobjectEntity> selectDataobjectIdsByInstance(@Param("instanceId") final String instanceId) throws Exception;

		final String SELECTDATAOBJECTIDSBYPROCESS = "SELECT DISTINCT(dataobject_id) as dataobjectId FROM bpms_process as p LEFT JOIN bpms_dataobject as d ON p.process_def_id=d.process_def_id WHERE process_id = #{processId}";
		@Select(SELECTDATAOBJECTIDSBYPROCESS)
		public List<DataobjectEntity> selectDataobjectIdsByProcess(@Param("processId") final String processId) throws Exception;

		final String SELECTDATABYPROPERTY = "SELECT id, process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, activity_id as activityId, dataobject_id as dataobjectId, property, value, type, time FROM bpms_dataobject WHERE instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND  property = #{property}";
		@Select(SELECTDATABYPROPERTY)
		public DataobjectEntity selectDataByProperty(@Param("instanceId") final String instanceId, @Param("dataobjectId") final String dataobjectId, @Param("property") final String property) throws Exception;

		final String SELECTDATAOBJECTPROPERTYIDBYPROCESS = "SELECT DISTINCT(property) FROM bpms_process as p LEFT JOIN bpms_dataobject as d ON p.process_def_id=d.process_def_id WHERE process_id = #{processId} AND  dataobject_id = #{dataobjectId}";
		@Select(SELECTDATAOBJECTPROPERTYIDBYPROCESS)
		public List<DataobjectEntity> selectDataobjectPropertyIdsByProcess(@Param("processId") final String processId, @Param("dataobjectId") final String dataobjectId) throws Exception;

		final String SELECTDATAINTERVALBYPROCESS = "SELECT  d.id, d.process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, activity_id as activityId, dataobject_id as dataobjectId, property, value, type, time FROM bpms_process as p LEFT JOIN bpms_dataobject as d ON p.process_def_id=d.process_def_id WHERE process_id = #{processId} AND dataobject_id = #{dataobjectId} AND property = #{property} AND #{startDate}<=time AND time>=#{endDate} ORDER BY time DESC";
		@Select(SELECTDATAINTERVALBYPROCESS)
		public List<DataobjectEntity> selectDataIntervalByProcess(@Param("processId") final String processId, @Param("dataobjectId") final String dataobjectId, @Param("startDate") final Date startDate, @Param("endDate") final Date endDate, @Param("property") final String property) throws Exception;
	}
	
	static {
		
		// si no existe, crea la tabla en la BD 
		try {
			
			createTable();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Si no existe, crea la tabla bpms_dataobject 
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			mapper.createTable();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id de los dataobjects en una instancia de proceso
	 * 
	 * @param Id de la instancia de proceso
	 * @return Mapa con los ids de los dataobjects
	 * @throws Exception
	 */
	public static List<DataobjectEntity> selectDataobjectIdsByInstance(String instanceId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			List<DataobjectEntity> list = mapper.selectDataobjectIdsByInstance(instanceId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene el id de los dataobjects en un proceso
	 * 
	 * @param Id del proceso
	 * @return Mapa con los ids de los dataobjects
	 * @throws Exception
	 */
	public static List<DataobjectEntity> selectDataobjectIdsByProcess(String processId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			List<DataobjectEntity> list = mapper.selectDataobjectIdsByProcess(processId);

			return list;
		} finally {
			
			session.close();
		}
	}

	/**
	 * Obtiene la informacion de una propiedad de un dataobject en una instancia de proceso, a partir de su id
	 * en el diagrama
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @param dataobjectId Id del dataobject
	 * @return Informacion de la actividad
	 * @throws Exception
	 */
	public static DataobjectEntity selectDataByProperty(String instanceId, String dataobjectId, String property) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			DataobjectEntity entity = mapper.selectDataByProperty(instanceId, dataobjectId, property);

			return entity;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene los ids de las propiedades de un dataobject en un proceso
	 * 
	 * @param Id del proceso
	 * @return Lista con los ids de las propiedades
	 * @throws Exception
	 */
	public static List<DataobjectEntity> selectDataobjectPropertyIdsByProcess(String processId, String dataobjectId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			List<DataobjectEntity> list = mapper.selectDataobjectPropertyIdsByProcess(processId, dataobjectId);

			return list;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene la informacion de una propiedad de un dataobject en un proceso en un intervalo de tiempo
	 * 
	 * @param Id del proceso
	 * @param dataobjectId Id del dataobject
	 * @param startDate Fecha inicial
	 * @param endDate Fecha final
	 * @return Lista la informacion de la propiedad del dataobject
	 * @throws Exception
	 */
	public static List<DataobjectEntity> selectDataIntervalByProcess(String processId, String dataobjectId, Date startDate, Date endDate, String property) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			List<DataobjectEntity> list = mapper.selectDataIntervalByProcess(processId, dataobjectId, startDate, endDate, property);

			return list;
		} finally {
			
			session.close();
		}
	}
}
