package es.us.isa.bpms.ppinot.historyreport.db;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/** 
 *   
 * @author Edelia
 */
class PruebaDAO {
	
	public interface SimpleMapper {
		
		final String CREATETABLE = "CREATE TABLE IF NOT EXISTS prueba_information ( info_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, info_content VARCHAR(100) )";
		@Update(CREATETABLE)
		public int createTable() throws Exception;
		
		final String SELECTBYID = "SELECT info_id as infoId, info_content as infoContent FROM prueba_information WHERE info_id = #{infoId}";
		@Select(SELECTBYID)
		public PruebaEntity selectById(final int infoId) throws Exception;

		final String SELECTALL = "SELECT info_id as infoId, info_content as infoContent FROM prueba_information";
		@Select(SELECTALL)
		public List<PruebaEntity> selectAll() throws Exception;
		
		final String INSERT = "INSERT INTO prueba_information (info_content) VALUES (#{infoContent})";
		@Insert(INSERT)
		@SelectKey(statement="call identity()", keyProperty="infoId", before=false, resultType=int.class)
		public int insert(final PruebaEntity simpleInfo) throws Exception;
		
		final String UPDATE = "UPDATE prueba_information SET info_content = #{newInfo} WHERE info_id = #{infoId}";
		@Update(UPDATE)
		public int update(@Param("infoId") final int infoId, @Param("newInfo") final String newInfo) throws Exception;
		
		final String DELETEBYID = "DELETE FROM prueba_information WHERE info_id = #{infoId}";
		@Delete(DELETEBYID)
		void deleteById(final int infoId) throws Exception;
		
		final String DELETEALL = "DELETE FROM prueba_information";
		@Delete(DELETEALL)
		void deleteAll() throws Exception;
	}

	public void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			mapper.createTable();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	public PruebaEntity selectById(int info_id) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			PruebaEntity simpleInfo = mapper.selectById(info_id);

			return simpleInfo;
		} finally {
			
			session.close();
		}
	}

	public List<PruebaEntity> selectAll() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			List<PruebaEntity> list = mapper.selectAll();

			return list;
		} finally {
			
			session.close();
		}
	}

	public int insert(PruebaEntity simpleInfo) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			int answer = mapper.insert(simpleInfo);

			session.commit();
			
			return answer;
		} finally {
			
			session.close();
		}
	}
	
	public int update(int info_id, String new_content) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			int answer = mapper.update(info_id, new_content);
			
            session.commit();
			
			return answer;
		
		} finally {
			
			session.close();
		}
	}

	public void deleteById(int info_id) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			mapper.deleteById(info_id);
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	public void deleteAll(int info_id) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			SimpleMapper mapper = session.getMapper(SimpleMapper.class);
			mapper.deleteAll();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	public void prueba() {
		
		try {
			// prueba
System.out.println("********** inicio prueba");

			this.createTable();
System.out.println("tabla creada");
	    	
			PruebaEntity insertado = new PruebaEntity();
			insertado.setInfoContent("prueba 1");
			this.insert(insertado);
			int last = insertado.getInfoId();
System.out.println("registro insertado. Id: "+last);

			insertado.setInfoContent("prueba 2");
			this.insert(insertado);
			last = insertado.getInfoId();
System.out.println("registro insertado. Id: "+last);

			PruebaEntity simple = this.selectById(last);
if (simple==null) System.out.println("registro insertado vacio"); else  System.out.println("ultimo registro insertado: "+simple.getInfoId()+", "+simple.getInfoContent());

			String newValue = "actualizado";
			last--;
			int answer = this.update(last, newValue);
System.out.println("registro actualizado. Id: "+last+", answer: "+answer);
			
			List<PruebaEntity> list = this.selectAll();
if (list==null || list.size()==0) System.out.println("sin registros"); else for (PruebaEntity s : list) System.out.println("registros: "+s.getInfoId()+", "+s.getInfoContent());

			this.deleteById(last);
System.out.println("registro eliminado. Id: "+last);

			list = this.selectAll();
if (list==null || list.size()==0) System.out.println("sin registros"); else for (PruebaEntity s : list) System.out.println("registros: "+s.getInfoId()+", "+s.getInfoContent());

System.out.println("********** fin prueba");
	    	// fin prueba
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}

