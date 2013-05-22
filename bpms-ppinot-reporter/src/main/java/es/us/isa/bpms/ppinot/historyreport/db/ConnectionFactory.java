package es.us.isa.bpms.ppinot.historyreport.db;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import es.us.isa.bpms.ppinot.historyreport.db.ActivityDAO.ActivityMapper;
import es.us.isa.bpms.ppinot.historyreport.db.DataConditionDAO.DataConditionMapper;
import es.us.isa.bpms.ppinot.historyreport.db.DataobjectDAO.DataobjectMapper;
import es.us.isa.bpms.ppinot.historyreport.db.ProcessDAO.ProcessMapper;


/**
 * Factory de las sesiones para la conexion a la BD
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ConnectionFactory {

	private static SqlSessionFactory sqlSessionFactory = null;
	
	static {
	
		try {
			
			if (sqlSessionFactory == null) {

				XMLConfiguration xmlConfig = new XMLConfiguration("ppinotreporter.cfg.xml");

				PooledDataSource ds = new PooledDataSource();      
				ds.setDriver(xmlConfig.getString("database[@driver]"));      
				ds.setUrl(xmlConfig.getString("database[@url]"));      
				ds.setUsername(xmlConfig.getString("database[@username]"));      
				ds.setPassword(xmlConfig.getString("database[@password]"));  
			    
				Environment env = new Environment("development", new JdbcTransactionFactory(), ds); 
				Configuration config = new Configuration(env); 
				  
				config.addMapper(ProcessMapper.class);
				config.addMapper(ActivityMapper.class);
				config.addMapper(DataobjectMapper.class);
				config.addMapper(DataConditionMapper.class);
				  
				SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder(); 
				
				sqlSessionFactory = builder.build(config); 
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Devuelve una sesion para la conexion a la BD
	 * 
	 * @return Sesion
	 */
	public static SqlSessionFactory getSqlSessionFactory() {
	
		return sqlSessionFactory;
	}

}

