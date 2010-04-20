package com.emq.config;

/**
 * 地图定义引用
 * @author guqiong
 * @created 2009-9-27
 */
public class MapDefRef {
	/**
	 * 地图定义引用id
	 */
	private String id;
	/**
	 * 引用的地图定义id
	 */
	private String defId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDefId() {
		return defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}
}
