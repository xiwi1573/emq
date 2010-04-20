package com.emq.exception;

/**
 * 系统异常信息常量表 常量命名规则：项目名_功能分类_序号,字符串中应包含常量名
 * 
 * @author guqiong
 * @created 2009-10-29
 */
public class ErrorMsgConstants {
	/**
	 * 地图功能异常
	 */
	public static final String KMGIS_MAP_01 = "KMGIS_MAP_01: 地图参数设置错误";
	public static final String KMGIS_MAP_02 = "KMGIS_MAP_02: SearchableGISMap的子类不支持的查询条件对象";
	public static final String KMGIS_MAP_03 = "KMGIS_MAP_03: 地图引用的定义不能是抽象定义";
	public static final String KMGIS_MAP_04 = "KMGIS_MAP_04: GISMapSearchCommand 仅支持 SearchableGISMap 及其子类";
	public static final String KMGIS_MAP_05 = "KMGIS_MAP_05: SearchableGISMapsearchByPk的参数个数错误";
	public static final String KMGIS_MAP_06 = "KMGIS_MAP_06: 地图尚未初始化";
	public static final String KMGIS_MAP_07 = "KMGIS_MAP_07: 地图查询失败";
	public static final String KMGIS_MAP_08 = "KMGIS_MAP_08: 未定义的地图id";
	public static final String KMGIS_MAP_09 = "KMGIS_MAP_09: 校验地图数据时出错";
	/**
	 * 统计异常
	 */
	public static final String KMGIS_STATISTICS_01 = "GIS_STATISTICS_01: 未定义的地区分类";
	public static final String KMGIS_STATISTICS_02 = "GIS_STATISTICS_02: 未经授权的统计";	
	/**
	 * 安全异常
	 */
	public static final String KMGIS_SECURITY_01 = "KMGIS_SECURITY_01: 获取r1用户信息时出现错误";
	public static final String KMGIS_SECURITY_02 = "KMGIS_SECURITY_02: 未经授权的地图访问";
	/**
	 * dao异常
	 */
	
	public static final String KMGIS_DAO_01 = "KMGIS_DAO_01: 试图删除不存在的数据";
	public static final String KMGIS_DAO_02 = "KMGIS_DAO_02: 试图录入重复的数据";
	
	/**
	 * 未知错误
	 */
	public static final String KMGIS_UNKNOWN_01 = "KMGIS_UNKNOWN_01: 未知错误";
}
