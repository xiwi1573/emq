package com.emq.config;

/**
 * 图层模板定义
 * 
 * @author guqiong
 * @created 2009-10-23
 */
public class LayerTemplateDef {
	/**
	 * 模板id，对应defineFine中模板图层名称
	 */
	private String id;
	/**
	 * mapinfo配置文件
	 */
	private String defineFile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefineFile() {
		return defineFile == null ? null : defineFile.trim();
	}

	public void setDefineFile(String defineFile) {
		this.defineFile = defineFile;
	}

}
