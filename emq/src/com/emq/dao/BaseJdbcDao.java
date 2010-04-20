package com.emq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基础数据查询
 * @author lyt
 * @created 2009-9-22
 */
public interface BaseJdbcDao {
	/**
	 * 根据sql获取数据
	 * @param sql
	 * @return List
	 * @author lyt
	 */
	public List getDataList(String sql);
	/**
	 * 执行updatesql
	 * @param sql
	 * @return List
	 * @author lyt
	 */
	public boolean exeUpdateSql(String sql);
	/**
	 * 执行存储过程
	 * @param sql
	 * @return List
	 * @author lyt
	 */
	public List execute_proc(String sql);
	/**
	 * 批量执行sql
	 * @param sqlList
	 * @author lyt
	 */
	public void exeUpdateSqlByBach(List sqlList) throws SQLException;
	/**
	 * 根据sql获取唯一行的Map对象
	 * @param sql
	 * @return Map
	 * @author lyt
	 */
	public Map getMapData(String sql);
	/**
	 * 执行存过f返回状态值
	 * @param sql
	 * @return String[2] 返回状态值和描述
	 * @author lyt
	 */
	public String[] executeProcByReturn(String sql);
	
}
