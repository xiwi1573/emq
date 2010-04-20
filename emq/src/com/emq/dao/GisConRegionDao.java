package com.emq.dao;

import java.util.List;

import com.emq.model.pojo.GisConRegion;

/**
 * GisConRegion 地图-专卖机构关系表数据访问接口
 * 
 * @author guqiong
 * 
 */
public interface GisConRegionDao {
	/**
	 * 获取地图-专卖机构关系(默认的，有效的)
	 * 
	 * @param zmjgbm
	 *            专卖机构编码
	 * @param dqpzh
	 *            地图配置号      
	 * @return
	 */
	public GisConRegion getUsedGisConRegion(String zmjgbm, String dqpzh);
	
	/**
	 * 获取地图-专卖机构关系列表(有效的)
	 * 
	 * @param zmjgbm
	 *            专卖机构编码
	 * @param dqpzh
	 *            地图配置号
	 * @return 
	 */
	public List<GisConRegion> getValidGisConRegions();
	
	/**
	 * 获取地图-专卖机构关系列表(默认的)
	 * 
	 * @param zmjgbm
	 *            专卖机构编码
	 * @param dqpzh
	 *            地图配置号
	 * @return 
	 */
	public List<GisConRegion> getUsedGisConRegions();

}
