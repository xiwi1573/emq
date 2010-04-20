package com.emq.ui.dwr;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.emq.exception.GISException;
import com.emq.model.ConditionObject;

/**
 * 卷烟专卖点dwr查询接口
 * 
 * @author guqiong
 * @created 2009-9-21
 * @history 2009-10-14 guqiong 增加半径范围查询方法 2009-10-16 guqiong 增加返回xml数据的查询方法
 *          <p>
 *          findXmlByCondition
 *          <p>
 *          findXmlByPoint
 *          <p>
 *          findXmlByRadius
 *          <p>
 *          findXmlByRect
 *          <p>
 *          findXmlByRegion 注意：dwr不支持方法重载 2009-10-20 guqiong add: findXmlById
 *          2009-10-20 guqiong 抽取通用方法至GISMapDataFinder
 */
public interface CigStoreFinder extends GISMapDataFinder {

	/**
	 * 将指定条件的卷烟专卖点进行高亮,改变会话中的地图状态
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param co
	 *            查询条件对象
	 * @param session
	 *            会话
	 * @return null
	 * 
	 */
	public abstract void highlightByCondition(String mapDefRefId,
			ConditionObject co, HttpSession session) throws GISException;

	/**
	 * 获取指定条件的卷烟专卖点信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param co
	 *            查询条件对象
	 * @param session
	 *            会话
	 * @return 卷烟专卖点信息列表，每个零售点信息为一个Map，存放格式key=字段中文名，value=字段值
	 * 
	 * 
	 */
	public abstract List<Map<String, String>> findByCondition(
			String mapDefRefId, ConditionObject co, HttpSession session)
			throws GISException;

	/**
	 * 获取指定条件的卷烟专卖点信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param co
	 *            查询条件对象
	 * @param session
	 *            会话
	 * @return 卷烟专卖点信息列表，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 * 
	 * 
	 */
	public String findXmlByCondition(String mapDefRefId, String returnColumns,
			ConditionObject co, HttpSession session) throws GISException;

}