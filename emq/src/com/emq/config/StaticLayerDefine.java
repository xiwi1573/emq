package com.emq.config;

/**
 * 静态显示图层定义
 * 
 * @author guqiong
 * @created 2009-10-23
 */
public class StaticLayerDefine implements Cloneable {
	
	/**
	 * map-def内唯一
	 */
	private String id;	

	/**
	 * 模板配置名称@see gis-map.xml
	 */
	private String template;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * x坐标列名
	 */
	private String x;
	/**
	 * y坐标列名
	 */
	private String y;
	/**
	 * 主键列
	 */
	private String[] pk;
	
	/**
	 * 标注列名
	 */
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 获取模板配置名称@see gis-map.xml
	 */
	public String getTemplate() {
		return template == null ? null :template.trim();
	}

	/**
	 * 设置模板配置名称@see gis-map.xml
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * 获取表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取x坐标列名
	 */
	public String getX() {
		return x;
	}

	/**
	 * 设置x坐标列名
	 */
	public void setX(String x) {
		this.x = x;
	}

	/**
	 * 获取y坐标列名
	 */
	public String getY() {
		return y;
	}

	/**
	 * 设置y坐标列名
	 */
	public void setY(String y) {
		this.y = y;
	}

	public String[] getPk() {
		return pk;
	}

	public void setPk(String[] pk) {
		this.pk = pk;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
