package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de un proceso. Crea y utiliza las tablas bpms_process y bpms_instance.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see ProcessEntity
 */
public class ProcessDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de un proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface ProcessMapper {
		
		final String CREATETABLEPROCESS = "CREATE TABLE IF NOT EXISTS bpms_process (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_id VARCHAR(100) NOT NULL, process_def_id VARCHAR(100) NOT NULL )";
		@Update(CREATETABLEPROCESS)
		public int createTableProcess() throws Exception;
		
		final String CREATETABLEINSTANCE = "CREATE TABLE IF NOT EXISTS bpms_instance (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, start_time TIMESTAMP NOT NULL, end_time TIMESTAMP )";
		@Update(CREATETABLEINSTANCE)
		public int createTableInstance() throws Exception;
		
		final String SELECTDATAINSTANCE = "SELECT p.id as idp, i.id as idi, process_id as processId, i.process_def_id as processDefId, instance_id as instanceId, start_time as startTime, end_time as endTime FROM bpms_instance AS i LEFT JOIN bpms_process as p ON i.process_def_id=p.process_def_id WHERE i.instance_id = #{instanceId}";
		@Select(SELECTDATAINSTANCE)
		public ProcessEntity selectDataByInstance(@Param("instanceId") final String instanceId) throws Exception;
		
		final String SELECTDATAPROCESS = "SELECT p.id as idp, i.id as idi, process_id as processId, i.process_def_id as processDefId, instance_id as instanceId, start_time as startTime, end_time as endTime FROM bpms_process as p LEFT JOIN bpms_instance AS i ON p.process_def_id=i.process_def_id WHERE p.process_id = #{processId}";
		@Select(SELECTDATAPROCESS)
		public List<ProcessEntity> selectDataByProcess(@Param("processId") final String processId) throws Exception;
		
		final String SELECTPROCESSIDS = "SELECT DISTINCT(process_id) as processId FROM bpms_process";
		@Select(SELECTPROCESSIDS)
		public List<ProcessEntity> selectProcessIds() throws Exception;
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
	 * Si no existe, crea las tablas bpms_process y bpms_instance 
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			mapper.createTableProcess();
			mapper.createTableInstance();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Obtiene la informacion de una instancia de proceso, a partir de su id
	 * 
	 * @param instanceId Id de la instancia de proceso
	 * @return Informacion de la instancia de proceso
	 * @throws Exception
	 */
	public static ProcessEntity selectDataByInstance(String instanceId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			ProcessEntity entity = mapper.selectDataByInstance(instanceId);

			return entity;
		} finally {
			
			session.close();
		}
	}

	/**
	 * Obtiene la informacion de todas las instancias de un proceso, a partir de su id en el diagrama
	 * 
	 * @param processId Id del proceso en el diagrama
	 * @return Lista con la informacion de las instancias de proceso
	 * @throws Exception
	 */
	public static List<ProcessEntity> selectDataByProcess(String processId) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			List<ProcessEntity> list = mapper.selectDataByProcess(processId);

			return list;
		} finally {
			
			session.close();
		}
	}

	/**
	 * Obtiene la informacion de todas las instancias de proceso
	 * 
	 * @return Lista con la informacion de las instancias de proceso
	 * @throws Exception
	 */
	public static List<ProcessEntity> selectDistinctProcessId() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			List<ProcessEntity> list = mapper.selectProcessIds();

			return list;
		} finally {
			
			session.close();
		}
	}
}

