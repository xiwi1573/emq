package com.emq.ui.dwr;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.emq.exception.GISException; 

/**
 * 图层信息的dwr查询接口
 * @author guqiong
 * @create 2009-10-20
 */
public interface GISMapDataFinder {

	/**
	 * 将指定条件的图元进行高亮,改变会话中的地图状态
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
			Object co, HttpSession session) throws GISException;

	/**
	 * 获取指定条件的图元信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param co
	 *            查询条件对象
	 * @param session
	 *            会话
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 * 
	 * 
	 */
	public abstract List<Map<String, String>> findByCondition(
			String mapDefRefId, Object co, HttpSession session)
			throws GISException;
	/**
	 * 获取指定地点的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param session
	 *            会话
	 * @param x
	 *            x坐标, 屏幕坐标系
	 * @param y
	 *            y坐标, 屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 */
	public abstract List<Map<String, String>> findByPoint(String mapDefRefId,
			String x, String y, HttpSession session) throws GISException;

	/**
	 * 获取指定中心点半径范围内的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param session
	 *            会话
	 * @param x
	 *            x坐标, 屏幕坐标系
	 * @param y
	 *            y坐标, 屏幕坐标系
	 * @param y
	 *            半径, 屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 */
	public abstract List<Map<String, String>> findByRadius(String mapDefRefId,
			String x, String y, String radius, HttpSession session)
			throws GISException;

	/**
	 * 获取矩形范围内的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param x0
	 *            矩形坐上角x坐标, 屏幕坐标系
	 * @param y0
	 *            矩形坐上角y坐标, 屏幕坐标系
	 * @param x1
	 *            矩形右下角x坐标, 屏幕坐标系
	 * @param y1
	 *            矩形右下角y坐标, 屏幕坐标系
	 * @param session
	 *            会话
	 * @return
	 * @throws GISException
	 */
	public abstract List<Map<String, String>> findByRect(String mapDefRefId,
			String x0, String y0, String x1, String y1, HttpSession session)
			throws GISException;

	/**
	 * 获取指定多边形区域内的图元信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param pcount
	 *            多边形的顶点数
	 * @param session
	 *            会话
	 * @param encodedPoints
	 *            多边形顶点格式串：格式 x0,y0;x1,y1;...xn,yn 每个xy对用逗号分隔，多个xy对用分号分隔
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 * 
	 */
	public abstract List<Map<String, String>> findByRegion(String mapDefRefId,
			String pcount, String encodedPoints, HttpSession session)
			throws GISException;

	/**
	 * 获取指定条件的图元信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param co
	 *            查询条件对象
	 * @param session
	 *            会话
	 * @return 图元信息列表，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 * 
	 * 
	 */
	public abstract String findXmlByCondition(String mapDefRefId,
			String returnColumns, Object co, HttpSession session)
			throws GISException;

	/**
	 * 获取指定地点的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param session
	 *            会话
	 * @param x
	 *            x坐标, 屏幕坐标系
	 * @param y
	 *            y坐标, 屏幕坐标系
	 * @return 图元信息列表，xml格式，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 */
	public abstract String findXmlByPoint(String mapDefRefId,
			String returnColumns, String x, String y, HttpSession session)
			throws GISException;

	/**
	 * 获取指定中心点半径范围内的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param session
	 *            会话
	 * @param x
	 *            x坐标, 屏幕坐标系
	 * @param y
	 *            y坐标, 屏幕坐标系
	 * @param y
	 *            半径, 屏幕坐标系
	 * @return 图元信息列表，xml格式 ，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 */
	public abstract String findXmlByRadius(String mapDefRefId,
			String returnColumns, String x, String y, String radius,
			HttpSession session) throws GISException;

	/**
	 * 获取矩形范围内的图元信息列表
	 * <P>
	 * 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param x0
	 *            矩形坐上角x坐标, 屏幕坐标系
	 * @param y0
	 *            矩形坐上角y坐标, 屏幕坐标系
	 * @param x1
	 *            矩形右下角x坐标, 屏幕坐标系
	 * @param y1
	 *            矩形右下角y坐标, 屏幕坐标系
	 * @param session
	 *            会话
	 * @return 图元信息列表，xml格式，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 * @throws GISException
	 */
	public abstract String findXmlByRect(String mapDefRefId,
			String returnColumns, String x0, String y0, String x1, String y1,
			HttpSession session) throws GISException;

	/**
	 * 获取指定多边形区域内的图元信息列表
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param pcount
	 *            多边形的顶点数
	 * @param session
	 *            会话
	 * @param encodedPoints
	 *            多边形顶点格式串：格式 x0,y0;x1,y1;...xn,yn 每个xy对用逗号分隔，多个xy对用分号分隔
	 * @return 图元信息列表，xml格式，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 * 
	 */
	public abstract String findXmlByRegion(String mapDefRefId,
			String returnColumns, String pcount, String encodedPoints,
			HttpSession session) throws GISException;

	/**
	 * 获取指定id的图元信息
	 * 
	 * @param mapDefRefId
	 *            地图定义引用id
	 * @param returnColumns
	 *            需要返回的数据列，格式：用逗号分隔，如“column1,column2”, 所使用的表由实现此接口的类决定
	 * @param id
	 *            主键值,多值主键由逗号分隔
	 * @param session
	 *            会话
	 * @return 图元信息列表，xml格式，xml格式, 如“<?xml version="1.0"
	 *         encoding="UTF-8"?><rows><row
	 *         id="a"><cell>AA</cell><cell>BB</cell>
	 *         <cell>CC</cell></row></rows>”
	 * @throws GISException
	 */
	public abstract String findXmlById(String mapDefRefId,
			String returnColumns, String id, HttpSession session)
			throws GISException;
}
