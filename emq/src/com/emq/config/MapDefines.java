package com.emq.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.emq.logger.Logger;

/**
 * 地图定义集合
 * 
 * @author guqiong
 * @created 2009-9-27
 */
public class MapDefines {

	Logger log = Logger.getLogger(MapDefines.class);
	/**
	 * 存放所有的地图定义
	 */
	private Map<String, MapDef> mapDefs = null;
	/**
	 * 存放所有的地图定义引用
	 */
	private Map<String, MapDefRef> mapDefRefs = null;
	/**
	 * 图层模板定义
	 */
	private Map<String, LayerTemplateDef> layerTemplateDefs = null;

	/**
	 * 清除内部数据
	 */
	public void clean(){
		if(mapDefs!=null){
			mapDefs.clear();
			mapDefs = null;
		}
		if(mapDefRefs!=null){
			mapDefRefs.clear();
			mapDefRefs = null;
		}
		if(layerTemplateDefs!=null){
			layerTemplateDefs.clear();
			layerTemplateDefs = null;
		}
	}
	
	public MapDefines() {
		mapDefs = new HashMap<String, MapDef>();
		mapDefRefs = new HashMap<String, MapDefRef>();
		layerTemplateDefs = new HashMap<String, LayerTemplateDef>();
	}

	public Collection<MapDefRef> getMapDefRefs() {
		return mapDefRefs.values();
	}

	public Collection<MapDef> getMapDefs() {
		return mapDefs.values();
	}

	public Collection<LayerTemplateDef> getLayerTemplateDefs() {
		return layerTemplateDefs.values();
	}

	/**
	 * 加入地图定义
	 * 
	 * @param mapDef
	 *            地图定义
	 */
	public void addMapDef(MapDef mapDef) {
		String id = mapDef.getId();
		if (mapDefs.containsKey(id)) {
			log.error("重复定义的地图id：");
			log.error(id);
		}

		this.mapDefs.put(id, mapDef);
	}

	/**
	 * 移除地图定义
	 * 
	 * @param mapDef
	 *            地图定义
	 */
	public void removeMapDef(MapDef mapDef) {
		this.mapDefs.remove(mapDef);
	}

	/**
	 * 加入地图定义引用
	 * 
	 * @param mapDef
	 *            地图定义引用
	 */
	public void addMapDefRef(MapDefRef mapDefRef) {
		String id = mapDefRef.getId();
		if (mapDefRefs.containsKey(id)) {
			log.error("重复定义的地图引用id：");
			log.error(id);
		}

		this.mapDefRefs.put(id, mapDefRef);
	}

	/**
	 * 移除地图定义引用
	 * 
	 * @param mapDef
	 *            地图定义引用
	 */
	public void removeMapDefRef(MapDefRef mapDefRef) {
		this.mapDefRefs.remove(mapDefRef);
	}
	
	/**
	 * 加入模板定义
	 * 
	 * @param layerTemplateDef
	 *            图层模板定义
	 */
	public void addLayerTemplateDef(LayerTemplateDef layerTemplateDef) {
		String id = layerTemplateDef.getId();
		if (layerTemplateDefs.containsKey(id)) {
			log.error("重复定义的图层模板定义id：");
			log.error(id);
		}

		this.layerTemplateDefs.put(id, layerTemplateDef);
	}

	/**
	 * 移除图层模板定义
	 * 
	 * @param layerTemplateDef
	 *              图层模板定义
	 */
	public void removeLayerTemplateDef(LayerTemplateDef layerTemplateDef) {
		this.layerTemplateDefs.remove(layerTemplateDef);
	}


	/**
	 * 获取地图定义
	 * 
	 * @param refid
	 *            地图引用id
	 * @return MapDef
	 */
	public MapDef getMapDef(String refid) {
		MapDefRef ref = this.mapDefRefs.get(refid);
		return this.mapDefs.get(ref.getDefId());
	}

	/**
	 * 获取地图定义
	 * 
	 * @param id
	 *            地图定义id
	 * @return MapDef
	 */
	public MapDef getMapDefById(String id) {
		return this.mapDefs.get(id);
	}

	/**
	 * 获取图层模板定义
	 * 
	 * @param id
	id *            模板id
	 * @return
	 */
	public LayerTemplateDef getLayerTemplateDef(String id) {
		return this.layerTemplateDefs.get(id);
	}

}
