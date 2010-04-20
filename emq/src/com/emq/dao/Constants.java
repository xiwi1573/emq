package com.emq.dao;

import java.math.BigDecimal;

public class Constants {
	public static final int TRUE_INT = 1;
	public static final int FALSE_INT = 0;
	
	/**
	 * Integer转换为boolean
	 * @param value
	 * @return
	 */
	public static Boolean int2Boolean(Integer value){
		if(value == null)
			return null;
		return value == TRUE_INT? true : false;
	}
	
	/**
	 * BigDecimal转换为Boolean
	 * @param value
	 * BigDecimal
	 * @return
	 */
	public static Boolean bigDecimal2Boolean(BigDecimal value){
		if(value == null)
			return null;
		return value.intValue() == TRUE_INT? true : false;
	}
	
	/**
	 * Boolean转换为BigDecimal
	 * @param value
	 * 	Boolean
	 * @return
	 */
	public static Integer boolean2BigDecimal(Boolean value){
		if(value == null)
			return null;
		return value? TRUE_INT : FALSE_INT;
	}
	
	
}
