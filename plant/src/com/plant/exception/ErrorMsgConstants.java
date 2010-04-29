package com.plant.exception;

/**
 * 系统异常信息常量表 常量命名规则：项目名_功能分类_序号,字符串中应包含常量名
 * 
 * @author guqiong
 * @created 2009-10-29
 */
public class ErrorMsgConstants {
	/**
	 * dao异常
	 */
	public static final String PLANT_DAO_01 = "KMGIS_DAO_01: 试图删除不存在的数据";
	public static final String PLANT_DAO_02 = "KMGIS_DAO_02: 试图录入重复的数据";
	/**
	 * 未知错误
	 */
	public static final String PLANT_UNKNOWN_01 = "KMGIS_UNKNOWN_01: 未知错误";
}
