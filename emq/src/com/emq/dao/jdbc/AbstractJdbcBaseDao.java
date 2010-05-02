package com.emq.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.emq.logger.Logger;

public abstract class AbstractJdbcBaseDao extends JdbcDaoSupport {
	private static Logger log = Logger.getLogger(AbstractJdbcBaseDao.class);
	/**
	 * 执行sql语句，通常用于创建表等操作
	 * 
	 * @param sql
	 */
	public void execute(String sql) {
		this.getJdbcTemplate().execute(sql);
	}
	
	public List execute_proc(String sql) {
		List dataList = new ArrayList();
		CallableStatement proc = null; 
		Connection con = null; 
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(1,2007,1,'12530401','530000','''6901028047128'',''6901028317177'',''6901028315012''',?,?)";
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(?,?,?,?,?,?,?,?)";
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.registerOutParameter(2, java.sql.Types.VARCHAR);
			rs = proc.executeQuery();
			while(rs.next()){
				Map map = new HashMap();
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
				dataList.add(map);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return dataList;
	}
	
	public List execute_procNonReturn(String sql) {
		List dataList = new ArrayList();
		//sql = "exec pro_gis_sale_sum \"201\",\"gsdm = '53010100'\",\"005\",\"1\"";
		CallableStatement proc = null; 
		Connection con = null; 
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			rs = proc.executeQuery();
			while(rs.next()){
				Map map = new HashMap();
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
				}
				dataList.add(map);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return dataList;
	}
	
	public List execute_procByArray(String sql) {
		List returnList = new ArrayList();
		List dataList = new ArrayList();
		List nameList = new ArrayList();
		CallableStatement proc = null; 
		Connection con = null; 
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(1,2007,1,'12530401','530000','''6901028047128'',''6901028317177'',''6901028315012''',?,?)";
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(?,?,?,?,?,?,?,?)";
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.registerOutParameter(2, java.sql.Types.VARCHAR);
			rs = proc.executeQuery();
			while(rs.next()){
				Map map = new HashMap();
				Map nameMap = new HashMap();
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
					nameMap.put(new Integer(i),rs.getMetaData().getColumnName(i));
				}
				nameList.add(nameMap);
				dataList.add(map);
			}
			returnList.add(nameList);
			returnList.add(dataList);
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return returnList;
	}
	
	public List execute_procByArray(List sqlList) {
		List list = new ArrayList();
		//CallableStatement proc = null; 
		Connection con = null; 
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			for(int s=0;s<sqlList.size();s++){
				List returnList = new ArrayList();
				List dataList = new ArrayList();
				List nameList = new ArrayList();
				CallableStatement proc = con.prepareCall((String)sqlList.get(s));
				System.out.println("s="+s);
				proc.clearParameters();
				proc.registerOutParameter(1,java.sql.Types.INTEGER);
				proc.registerOutParameter(2, java.sql.Types.VARCHAR);
				System.out.println("registerOutParameter");
				rs = proc.executeQuery();
				System.out.println("executeQuery");
				while(rs.next()){
					Map map = new HashMap();
					Map nameMap = new HashMap();
					for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
						map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
						nameMap.put(new Integer(i),rs.getMetaData().getColumnName(i));
					}
					nameList.add(nameMap);
					dataList.add(map);
				}
				returnList.add(nameList);
				returnList.add(dataList);
				list.add(returnList);
				proc.close(); 
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	public List execute_procByArrayNC(String sql) {
		List returnList = new ArrayList();
		List dataList = new ArrayList();
		List nameList = new ArrayList();
		CallableStatement proc = null; 
		Connection con = null; 
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(1,2007,1,'12530401','530000','''6901028047128'',''6901028317177'',''6901028315012''',?,?)";
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(?,?,?,?,?,?,?,?)";
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.registerOutParameter(2, java.sql.Types.VARCHAR);
			rs = proc.executeQuery();
			while(rs.next()){
				Map map = new HashMap();
				Map nameMap = new HashMap();
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
					nameMap.put(new Integer(i),rs.getMetaData().getColumnName(i));
				}
				nameList.add(nameMap);
				dataList.add(map);
			}
			returnList.add(nameList);
			returnList.add(dataList);
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}
		List rList = new ArrayList();
		rList.add(returnList);
		rList.add(proc);
		rList.add(con);
		return rList;
	}
	
	public List execute_proc(List sqlList) {
		List returnList = new ArrayList();
		CallableStatement proc = null; 
		Connection con = null; 
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(1,2007,1,'12530401','530000','''6901028047128'',''6901028317177'',''6901028315012''',?,?)";
		//sql = "call PROC_STAT_CURSOR_FSPPFZ(?,?,?,?,?,?,?,?)";
		ResultSet  rs = null;
		con = this.getConnection();
		try { 
			for(int i=0;i<sqlList.size();i++){
				String sql = (String)sqlList.get(i);
				List dataList = new ArrayList();
				proc = con.prepareCall(sql);
				proc.registerOutParameter(1,java.sql.Types.INTEGER);
				proc.registerOutParameter(2, java.sql.Types.VARCHAR);
				rs = proc.executeQuery();
				while(rs.next()){
					Map map = new HashMap();
					for(int j=1;j<=rs.getMetaData().getColumnCount();j++){
						map.put(rs.getMetaData().getColumnName(j), rs.getString(j));
					}
					dataList.add(map);
				}
				returnList.add(dataList);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return returnList;
	}
	
	public int executeProc_fa_fc_createtask(String sql) {
		List dataList = new ArrayList();
		CallableStatement proc = null; 
		Connection con = null; 
		ResultSet  rs = null;
		con = this.getConnection();
		int returnValue = 0;
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.execute();
			returnValue = proc.getInt(1);
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return returnValue;
	}
	
	public String[] executeProcByReturn(String sql) {
		String[] result = new String[2];
		CallableStatement proc = null; 
		Connection con = null; 
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.registerOutParameter(2, java.sql.Types.VARCHAR);
			proc.execute();
			result[0] = String.valueOf(proc.getInt(1));
			result[1] = String.valueOf(proc.getString(2));
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public String[] executeProcByReturn(String sql,int inout1,String inout2) {
		String[] result = new String[2];
		CallableStatement proc = null; 
		Connection con = null; 
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.registerOutParameter(2, java.sql.Types.VARCHAR);
			proc.setInt(1,inout1);
			proc.setString(2,inout2);
			proc.execute();
			result[0] = String.valueOf(proc.getInt(1));
			result[1] = String.valueOf(proc.getString(2));
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
				log.debug(ex.getMessage());
			}
		}
		return result;
	}
	
	public void executeProc_normal(String sql) {
		CallableStatement proc = null; 
		Connection con = null; 
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.registerOutParameter(1,java.sql.Types.INTEGER);
			proc.setInt(1,100);
			proc.execute();
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行无参存过
	 * @param sql
	 */
	public void executeProc(String sql) {
		CallableStatement proc = null; 
		Connection con = null; 
		con = this.getConnection();
		try { 
			proc = con.prepareCall(sql);
			proc.execute();
		}catch(Exception ex){
			ex.printStackTrace();
			log.debug(ex.getMessage());
		}finally {
			try { 
			proc.close(); 
			con.close(); 
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 根据sql获取唯一行的Map对象
	 * @param sql
	 * @return Map
	 * @author lyt
	 */
	public Map getDataForMap(String sql){
		Map data = null;
		try {
			data = this.getJdbcTemplate().queryForMap(sql);	
		} catch (DataAccessException e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		if (data == null) {
			return new HashMap();
		}
		return data;
	}
	
	/**
	 * 执行更新操作，如:insert、update或delete等
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql) {
		return this.getJdbcTemplate().update(sql);
	}

	/**
	 * 执行更新操作（带参数），如:insert、update或delete等
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object[] params) {
		return this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * 执行更新操作（带参数），根据PreparedStatementSetter进行参数设置
	 * 
	 * @param sql
	 * @param setter
	 * @return
	 */
	public int update(String sql, PreparedStatementSetter setter) {
		return this.getJdbcTemplate().update(sql, setter);
	}

	/**
	 * 查询方法，根据RowMapper定义，构建返回对象
	 * 
	 * @param sql
	 * @param mapper
	 * @return
	 */
	public List query(String sql, RowMapper mapper) {
		return this.getJdbcTemplate().query(sql, mapper);
	}
	
	/**
	 * 查询方法（带参数），根据RowMapper定义，构建返回对象
	 * 
	 * @param sql
	 * @param params
	 * @param mapper
	 * @return
	 */
	public List query(String sql, Object [] params, RowMapper mapper) {
		return this.getJdbcTemplate().query(sql, params, mapper);
	}

	/**
	 * 查询方法，返回int
	 * 
	 * @param sql
	 * @return
	 */
	public int queryForInt(String sql) {
		return this.getJdbcTemplate().queryForInt(sql);
	}

	/**
	 * 查询方法（带参数），返回int
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int queryForInt(String sql, Object[] params) {
		return this.getJdbcTemplate().queryForInt(sql, params);
	}

	/**
	 * 查询方法，返回clazz指定的类实例
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public Object queryForObject(String sql, Class clazz) {
		return this.getJdbcTemplate().queryForObject(sql, clazz);
	}

	/**
	 * 查询方法（带参数），返回clazz指定的类实例
	 * 
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public Object queryForObject(String sql, Object[] params, Class clazz) {
		return this.getJdbcTemplate().queryForObject(sql, params, clazz);
	}

	/**
	 * 查询方法，返回List
	 * 
	 * @param sql
	 * @return
	 */
	public List queryForList(String sql) {
		return this.getJdbcTemplate().queryForList(sql);
	}

	/**
	 * 查询方法（带参数），返回List
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List queryForList(String sql, Object[] params) {
		return this.getJdbcTemplate().queryForList(sql, params);
	}

	/**
	 * 查询方法，返回String
	 * 
	 * @param sql
	 * @return
	 */
	public String queryForString(String sql) {
		Object result = this.queryForObject(sql, String.class);
		return result == null ? null : (String) result;
	}

	/**
	 * 查询方法（带参数），返回String
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public String queryForString(String sql, Object[] params) {
		Object result = this.queryForObject(sql, params, String.class);
		return result == null ? null : (String) result;
	}

}
