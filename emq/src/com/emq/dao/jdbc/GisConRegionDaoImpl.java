package com.emq.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
 
import com.emq.dao.GisConRegionDao;
import com.emq.dao.Constants; 
import com.emq.model.pojo.GisConRegion;

/**
 *  地图-组织机构映射表数据访问实现
 * @author guqiong
 * @create 2009-10-30
 */
public class GisConRegionDaoImpl extends JdbcDaoSupport implements
		GisConRegionDao {

	/**
	 * 将数据集转换为GisConRegion
	 * 
	 * @param rs
	 *            数据集
	 * @return
	 * @throws SQLException
	 */
	private GisConRegion convert(ResultSet rs) throws SQLException {
		GisConRegion bean = new GisConRegion();
		bean.setZmjgbm(rs.getString("zmjgbm"));
		bean.setZmjgmc(rs.getString("zmjgmc"));
		
		BigDecimal value = rs.getBigDecimal("qyjb");
		bean.setQyjb(value == null ? null : value.intValue());
		
		bean.setDtpzh(rs.getString("dtpzh"));
		bean.setDtmc(rs.getString("dtmc"));
		
		value = rs.getBigDecimal("x");
		bean.setX(value == null ? null : value.doubleValue());
		
		value = rs.getBigDecimal("y");
		bean.setY(value == null ? null : value.doubleValue());
		
		value = rs.getBigDecimal("yxbz");
		bean.setYxbz(Constants.bigDecimal2Boolean(value));
		value = rs.getBigDecimal("mrbz");
		bean.setMrbz(Constants.bigDecimal2Boolean(value));
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.dao.GisConRegionDao#getUsedGisConRegion(java.lang.String,
	 * java.lang.String)
	 */
	public GisConRegion getUsedGisConRegion(String zmjgbm, String dqpzh) {
		if (zmjgbm == null || dqpzh == null)
			return null;
		String sql = "select * from Gis_Con_Region where zmjgbm=? and dtpzh=? and yxbz=? and mrbz=?";
		Object[] params = new Object[] { zmjgbm, dqpzh, Constants.TRUE_INT,
				Constants.TRUE_INT };
		GisConRegion result = (GisConRegion) this.getJdbcTemplate().query(sql,
				params, new ResultSetExtractor() {
					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							GisConRegion bean = convert(rs);
							return bean;
						} else
							return null;
					}

				});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.dao.GisConRegionDao#getValidGisConRegions()
	 */
	public List<GisConRegion> getValidGisConRegions() {
		String sql = "select * from Gis_Con_Region where yxbz=?";
		Object[] params = new Object[] { Constants.TRUE_INT };
		final List<GisConRegion> result = new ArrayList<GisConRegion>();
		this.getJdbcTemplate().query(sql, params, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				GisConRegion bean = convert(rs);
				result.add(bean);
			}
		});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.dao.GisConRegionDao#getUsedGisConRegions()
	 */
	public List<GisConRegion> getUsedGisConRegions() {
		String sql = "select * from Gis_Con_Region where yxbz=? and mrbz=?";
		Object[] params = new Object[] { Constants.TRUE_INT, Constants.TRUE_INT };
		final List<GisConRegion> result = new ArrayList<GisConRegion>();
		this.getJdbcTemplate().query(sql, params, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				GisConRegion bean = convert(rs);
				result.add(bean);
			}
		});
		return result;
	}

}
