package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD del estado de un dataobject en una instancia de proceso. 
 * Crea y utiliza la tabla bpms_datacondition.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see DataConditionEntity
 */
public class DataConditionDAO {

	/**
	 * Mapper para manipular la informacion en la BD de un estado de un dataobject en una instancia de proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface DataConditionMapper {
		
		final String CREATETABLE = "CREATE TABLE IF NOT EXISTS bpms_datacondition (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, dataobject_id VARCHAR(100) NOT NULL, state VARCHAR(100) NOT NULL, truth INT(1) NOT NULL, time TIMESTAMP NOT NULL )";
		@Update(CREATETABLE)
		public int createTable() throws Exception;
		
		final String SELECTDATABYSTATE = "SELECT id, process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, activity_id as activityId, dataobject_id as dataobjectId, state, truth, time FROM bpms_datacondition WHERE instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND  state = #{state}";
		@Select(SELECTDATABYSTATE)
		public DataConditionEntity selectDataByState(@Param("instanceId") final String instanceId, @Param("dataobjectId") final String dataobjectId, @Param("state") final String state) throws Exception;

		final String SELECTDATAOBJECTSTATEIDBYPROCESS = "SELECT DISTINCT(state) FROM bpms_process as p LEFT JOIN bpms_datacondition as d ON p.process_def_id=d.process_def_id WHERE process_id = #{processId} AND  dataobject_id = #{dataobjectId}";
		@Select(SELECTDATAOBJECTSTATEIDBYPROCESS)
		public List<DataConditionEntity> selectDataobjectStateIdsByProcess(@Param("processId") final String processId, @Param("dataobjectId") final String dataobjectId) throws Exception;
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
	 * Si no existe, crea la tabla bpms_datacondition 
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataConditionMapper mapper = session.getMapper(DataConditionMapper.class);
			mapper.createTable();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Obtiene la informacion de una actividad en un estado de un dataobject en una instancia de proceso, a partir de su id
	 * en el diagrama
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @param dataobjectId Id del dataobject
	 * @return Informacion de la actividad
	 * @throws Exception
	 */
	public static DataConditionEntity selectDataByState(String instanceId, String dataobjectId, String state) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataConditionMapper mapper = session.getMapper(DataConditionMapper.class);
			DataConditionEntity entity = mapper.selectDataByState(instanceId, dataobjectId, state);

			return entity;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Obtiene los ids de los estados de un dataobject en un proceso
	 * 
	 * @param Id del proceso
	 * @return Lista con los ids de las propiedades
	 * @throws Exception
	 */
	public static List<DataConditionEntity> selectDataobjectStateIdsByProcess(String processId, String dataobjectId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataConditionMapper mapper = session.getMapper(DataConditionMapper.class);
			List<DataConditionEntity> list = mapper.selectDataobjectStateIdsByProcess(processId, dataobjectId);

			return list;
		} finally {
			
			session.close();
		}
	}
}
