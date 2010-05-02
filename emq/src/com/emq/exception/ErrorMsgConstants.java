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
	public static final String EMQ_MAP_01 = "EMQ_MAP_01: 地图参数设置错误";
	public static final String EMQ_MAP_02 = "EMQ_MAP_02: SearchableGISMap的子类不支持的查询条件对象";
	public static final String EMQ_MAP_03 = "EMQ_MAP_03: 地图引用的定义不能是抽象定义";
	public static final String EMQ_MAP_04 = "EMQ_MAP_04: GISMapSearchCommand 仅支持 SearchableGISMap 及其子类";
	public static final String EMQ_MAP_05 = "EMQ_MAP_05: SearchableGISMapsearchByPk的参数个数错误";
	public static final String EMQ_MAP_06 = "EMQ_MAP_06: 地图尚未初始化";
	public static final String EMQ_MAP_07 = "EMQ_MAP_07: 地图查询失败";
	public static final String EMQ_MAP_08 = "EMQ_MAP_08: 未定义的地图id";
	public static final String EMQ_MAP_09 = "EMQ_MAP_09: 校验地图数据时出错";
	/**
	 * 统计异常
	 */
	public static final String EMQ_STATISTICS_01 = "GIS_STATISTICS_01: 未定义的地区分类";
	public static final String EMQ_STATISTICS_02 = "GIS_STATISTICS_02: 未经授权的统计";	
	/**
	 * 安全异常
	 */
	public static final String EMQ_SECURITY_01 = "EMQ_SECURITY_01: 文件不存在！";
	public static final String EMQ_SECURITY_02 = "EMQ_SECURITY_02: 未经授权的地图访问";
	/**
	 * dao异常
	 */
	
	public static final String EMQ_DAO_01 = "EMQ_DAO_01: 试图删除不存在的数据";
	public static final String EMQ_DAO_02 = "EMQ_DAO_02: 试图录入重复的数据";
	public static final String EMQ_DAO_03 = "EMQ_DAO_03: 批量执行SQL出错";
	/**
	 * UI错误
	 */
	public static final String EMQ_UI_01 = "EMQ_UI_01: 文件导入出错";
	public static final String EMQ_UI_02 = "EMQ_UI_02: 该文件中没有内容";
	/**
	 * 未知错误
	 */
	public static final String EMQ_UNKNOWN_01 = "EMQ_UNKNOWN_01: 未知错误";
}
