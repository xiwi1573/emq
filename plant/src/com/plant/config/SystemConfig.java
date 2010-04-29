package com.plant.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import com.plant.logger.Logger;
import java.util.List;
import java.util.Map;
/**
 * 系统配置参数管理类<P>单例
 * 全局性的系统参数应放在kmgis.properties文件中
 * 关于地图定义的系统参数放在gis-map.xml文件中
 * @author guqiong
 * @created 2009-9-21
 * @history
 * 			2009-10-26 guqiong add getProperty
 */
public class SystemConfig {
 	/**
 	 * 系统配置文件路径
 	 */
 	private final String default_config = "/kmgis.properties";
	/**
	 * 地图配置路径 
	 */
	private final String gis_map_config = "/gis-map.xml";

	private static Logger log = Logger.getLogger(SystemConfig.class);
	/**
	 * 配置属性集合
	 */
	private  Properties config;
	/**
	 * 地图定义集合
	 */
	private  MapDefines mapDefs = null;
	
	/**
	 * 地图权限, key=组织机构编码, value = 地图定义编号列表
	 */
	private  Map<String, List<String>> allowAccessMaps = new HashMap<String, List<String>>();


	/**
	 * 组织机构的默认地图关系, key=组织机构编码, value = 地图定义
	 */
	private  Map<String, MapDef> orgMapMappings = new HashMap<String, MapDef>();

	private static SystemConfig instance ;
	
	private SystemConfig(){
		
	}
	public Map<String, List<String>> getAllowAccessMaps() {
		return allowAccessMaps;
	}

	public void setAllowAccessMaps(Map<String, List<String>> allowAccessMaps) {
		this.allowAccessMaps = allowAccessMaps;
	}
	
	/**
	 * 获取SystemConfig实例
	 * @return
	 */
	public static SystemConfig getInstance(){
		if(instance == null)
			instance = new SystemConfig();
		return instance;
	}
	/**
	 * 加载地图与组织机构关系
	 */
	public void loadOrgMapMappings(List<GisConRegion> gisConRegionList){
		//加载地图中心点配置
		for(GisConRegion gisConRegion: gisConRegionList){
			MapDef mapDef = this.mapDefs.getMapDefById(gisConRegion.getDtpzh());
			if(mapDef!=null){
				mapDef.setX(gisConRegion.getX());
				mapDef.setY(gisConRegion.getY());
				String orgCode = gisConRegion.getZmjgbm();	
				//默认的地图
				if(gisConRegion.getMrbz() && gisConRegion.getYxbz())
					orgMapMappings.put(orgCode, mapDef);
				//允许访问的地图
				if(allowAccessMaps.get(orgCode) == null)
					allowAccessMaps.put(orgCode, new ArrayList<String>());
				else
					allowAccessMaps.get(orgCode).add(mapDef.getId());
			}
		}
	}
	
	/**
	 * 读取配置文件，初始化
	 */
	public void init(){
		config = new Properties();
		try {
			// 加载默认配置参数
			Class<?> config_class = Class
					.forName("com.icss.km.gis.config.SystemConfig");
			InputStream is = config_class.getResourceAsStream(default_config);
			config.load(is);
			// 打印参数for debug
			Enumeration<Object> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				log.debug(key + "=" + config.getProperty(key));
			}
			
			//加载地图配置
			if (mapDefs == null) {
				try {
					is = SystemConfig.class
							.getResourceAsStream(gis_map_config);
					MapDefsParser parser = new MapDefsParser();
					mapDefs = parser.unmarshall(is);

				} catch (Exception e) {
					log.error("解析地图定义文件时发生错误:", e);
					log.error(gis_map_config);
				} finally{
					if(is!=null)
						try{
							is.close();
						}catch(Exception e){
							log.error(e);
						}
				}

			}
			

		} catch (Exception e) {
			log.error("系统启动时读取配置参数失败： ", e);
			log.error("配置参数文件： ");
			log.error(default_config);
		}
	}
	
	/**
	 * 清除内部数据
	 */
	public void clean(){
		if(config!=null){
			config.clear();
			config = null;
		}
		if(mapDefs!=null){
			mapDefs.clean();
			mapDefs = null;
		}
		if(allowAccessMaps != null){
			allowAccessMaps.clear();
			allowAccessMaps = null;
		}
		if(orgMapMappings != null){
			orgMapMappings.clear();
			orgMapMappings = null;
		}
		instance = null;
	}
	 
	 

	/**
	 * 获取组织机构默认使用的地图
	 * @param orgCode 组织机构代码
	 * @return
	 */
	public  MapDef getDefaultMap(String orgCode){
		 return orgMapMappings.get(orgCode);		 
	}
	 
	/**
	 * 获取地图输出图片类型
	 * 
	 * @return
	 */
	public  String getMapMime() {
		return config.getProperty("map.mime");
	}

	/**
	 * 获取远程渲染服务器的Servlet地址
	 * 
	 * @return
	 */
	public  String getMxtURL() {
		return config.getProperty("map.mxtURL");
	}

	/**
	 * 获取默认的地图缩放比例
	 * @return
	 */
	public  Double getMapDefaultZoomRatio(){
		return new Double(config.getProperty("map.default_zoom_ratio"));
	}
	
	/**
	 * 获取默认的地图移动比例
	 * @return
	 */
	public  Double getMapDefaultMoveRatio(){
		return new Double(config.getProperty("map.default_move_ratio"));
	}
	
	/**
	 * 获取地图定义集
	 * @return MapDefines
	 */
	public  MapDefines getMapDefines() {
		
		return mapDefs;
	}
	
	/**
	 * 获取地图定义
	 * @param refid 地图定义引用id
	 * @return MapDef
	 */
	public  MapDef getMapDef(String refid){
		return getMapDefines().getMapDef(refid);
	}
	
	/**
	 * 获取属性值
	 * @param propName 属性名称
	 * @return
	 */
	public  String getProperty(String propName){
		return config.getProperty(propName);
	}
	
}
