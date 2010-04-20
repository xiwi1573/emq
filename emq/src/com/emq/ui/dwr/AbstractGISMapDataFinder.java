package com.emq.ui.dwr;

import java.util.ArrayList; 
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.emq.config.SpatialSearchLayerDefine;
import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.SearchableGISMap;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.TableInfo;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;

/**
 * 地理信息dwr查询接口实现，实现与具体表无关的操作，与具体地图相关的操作应由子类完成
 * @author guqiong
 * @created 2009-10-20
 */
public abstract class AbstractGISMapDataFinder implements GISMapDataFinder{
	private static Logger log = Logger.getLogger(AbstractGISMapDataFinder.class);
	
 
	/* (non-Javadoc)
	 * @see com.icss.km.gis.ui.dwr.GISMapDataFinder#findByCondition(java.lang.String, java.lang.Object, javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByCondition(String mapDefRefId,
			Object co, HttpSession session) throws GISException {
		List featureSetList  = new ArrayList();
		try {
			SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
			SpatialSearchLayerDefine sp = (SpatialSearchLayerDefine)theMap.getMapDef().getSpatialSearchLayerDefineList().get(0);
			featureSetList = theMap.searchByCondition(co,sp.getTableName()); 
			return FeatureSetTools.convert(featureSetList, getViewColumnMetaData());
		} catch (Exception e) {
			log.error("地图条件查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	} 
 
	/* (non-Javadoc)
	 * @see com.icss.km.gis.ui.dwr.GISMapDataFinder#highlightByCondition(java.lang.String, java.lang.Object, javax.servlet.http.HttpSession)
	 */
	public void highlightByCondition(String mapDefRefId, Object co,
			HttpSession session) throws GISException {
		FeatureSet fs = null;
		try {
			SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
			SpatialSearchLayerDefine sp = (SpatialSearchLayerDefine)theMap.getMapDef().getSpatialSearchLayerDefineList().get(0);
			theMap.searchAndHighlight(co,sp.getTableName());
		} catch (Exception e) {
			log.error("地图条件查询失败", e);
			throw new GISException(e);
		} finally {
			if (fs != null) {
				try {
					fs.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
	
	/**
	 * 获取地图坐标转换
	 * @param mapDefRefId
	 * @param x
	 * @param y
	 * @param session
	 * @return
	 * @throws GISException
	 */
	public String[] getConverPoint(String mapDefRefId, String x, String y,
			HttpSession session) throws GISException {
		FeatureSet fs = null;
		try {
			double x0 = new Double(x);
			double y0 = new Double(y);
			SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
			DoublePoint doublePoint =  theMap.convert2MainMapPosition(new DoublePoint(x0,y0));
			String[] returnStr = new String[2];
			returnStr[0] = String.valueOf(doublePoint.x);
			returnStr[1] = String.valueOf(doublePoint.y);
			return returnStr;
		} catch (Exception e) {
			log.error("获取地图坐标转换失败", e);
			throw new GISException(e);
		} finally {
			if (fs != null) {
				try {
					fs.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.dwr.CigStoreFinder#findByPoint(java.lang.String,
	 * java.lang.String, java.lang.String, javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByPoint(String mapDefRefId, String x,
			String y, HttpSession session) throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		return findByPoint(theMap, new Double(x), new Double(y));
	}

	/**
	 * 获取会话中的地图容器
	 * 
	 * @param mapDefRefId
	 *            地图引用id
	 * @param session
	 *            会话
	 * @return 地图容器
	 * @throws GISException
	 */
	protected SearchableGISMap getMapFromSession(String mapDefRefId,
			HttpSession session) throws GISException {
		String key = "KMGIS.MAP." + mapDefRefId;
		SearchableGISMap theMap = (SearchableGISMap) session.getAttribute(key);
		if (theMap == null)
			throw new GISException(ErrorMsgConstants.KMGIS_MAP_06);
		return theMap;
	}

	/**
	 * 获取指定地点的图元信息列表 当指定的地方有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param theMap
	 *            地图
	 * @param x
	 *            x坐标, 屏幕坐标系
	 * @param y
	 *            y坐标, 屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 */
	protected List<Map<String, String>> findByPoint(SearchableGISMap theMap,
			Double x, Double y) throws GISException {
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByPoint(new DoublePoint(x, y));
			return FeatureSetTools.convert(featureSetList, getViewColumnMetaData());
		} catch (Exception e) {
			log.error("地图点查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 获取用于显示的图元字段描述信息
	 * @return Map key=字段名，value=中文名
	 */
	protected abstract Map<String, String> getViewColumnMetaData() throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.dwr.CigStoreFinder#findByRegion(java.lang.String,
	 * java.lang.String, java.lang.String, javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByRegion(String mapDefRefId,
			String pcount, String encodedPoints, HttpSession session)
			throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		List<DoublePoint> points = getPointList(encodedPoints);
		return findByRegion(theMap, points);
	}

	/**
	 * 将格式化字符串的点转为DoublePoint对象列表
	 * 
	 * @param encodedPoints
	 * @return
	 */
	private List<DoublePoint> getPointList(String encodedPoints) {
		List<DoublePoint> points = new ArrayList<DoublePoint>();
		String[] sPoints = encodedPoints.split(";");
		for (int i = 0; i < sPoints.length; i++) {
			String[] sPoint = sPoints[i].split(",");
			double x = (new Double(sPoint[0])).doubleValue();
			double y = (new Double(sPoint[1])).doubleValue();
			points.add(new DoublePoint(x, y));
		}
		return points;
	}

	/**
	 * 获取指定多边形范围内的图元信息列表
	 * <p>
	 * 当指定的多边形范围内有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param theMap
	 *            图元地图
	 * @param points
	 *            多边形顶点, 屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 * @throws GISException
	 */
	protected List<Map<String, String>> findByRegion(
			SearchableGISMap theMap, List<DoublePoint> points)
			throws GISException {
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByRegion(points);
			return FeatureSetTools.convert(featureSetList, getViewColumnMetaData());
		} catch (Exception e) {
			log.error("地图多边形查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 获取指定矩形范围内的图元信息列表
	 * <p>
	 * 当指定的矩形范围内有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param theMap
	 *            图元地图
	 * @param rect
	 *            矩形, 屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 * @throws GISException
	 */
	private List<Map<String, String>> findByRect(SearchableGISMap theMap,
			DoubleRect rect) throws GISException {
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByRect(rect);
			return FeatureSetTools.convert(featureSetList, getViewColumnMetaData());
		} catch (Exception e) {
			log.error("地图矩形查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.dwr.CigStoreFinder#findByRect(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByRect(String mapDefRefId, String x0,
			String y0, String x1, String y1, HttpSession session)
			throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		DoubleRect rect = getRect(x0, y0, x1, y1);
		return findByRect(theMap, rect);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.dwr.CigStoreFinder#findByRadius(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpSession)
	 */
	public List<Map<String, String>> findByRadius(String mapDefRefId, String x,
			String y, String radius, HttpSession session) throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		double x0 = new Double(x).doubleValue();
		double y0 = new Double(y).doubleValue();
		double radius0 = new Double(radius).doubleValue();
		return this.findByRadius(theMap, new DoublePoint(x0, y0), radius0);
	}

	/**
	 * 获取指定圆范围内的图元信息列表
	 * <p>
	 * 当指定的圆形范围内有图元时，返回其信息； 否则返回空列表。
	 * 
	 * @param theMap
	 *            图元地图
	 * @param point
	 *            圆中心坐标，屏幕坐标系
	 * @param radius
	 *            半径，屏幕坐标系
	 * @return 图元信息列表，每个图元信息为一个Map，存放格式key=字段中文名，value=字段值
	 * @throws GISException
	 */
	protected List<Map<String, String>> findByRadius(
			SearchableGISMap theMap, DoublePoint point, double radius)
			throws GISException {
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByRadius(point, radius);
			return FeatureSetTools.convert(featureSetList, getViewColumnMetaData());
		} catch (Exception e) {
			log.error("地图半径范围查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
	 
	/* (non-Javadoc)
	 * @see com.icss.km.gis.ui.dwr.GISMapDataFinder#findXmlByCondition(java.lang.String, java.lang.String, java.lang.Object, javax.servlet.http.HttpSession)
	 */
	public String findXmlByCondition(String mapDefRefId, String returnColumns,
			Object co, HttpSession session) throws GISException {
		List featureSetList  = new ArrayList();
		try {
			SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
			SpatialSearchLayerDefine sp = (SpatialSearchLayerDefine)theMap.getMapDef().getSpatialSearchLayerDefineList().get(0);
			featureSetList = theMap.searchByCondition(co,sp.getTableName());
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图条件查询失败", e);
			throw new GISException(ErrorMsgConstants.KMGIS_MAP_07, e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#findXmlByPoint(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpSession)
	 */
	public String findXmlByPoint(String mapDefRefId, String returnColumns,
			String x, String y, HttpSession session) throws GISException {
		List featureSetList  = new ArrayList();
		try {
			double x0 = new Double(x);
			double y0 = new Double(y);
			SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
			featureSetList = theMap.searchByPoint(new DoublePoint(x0, y0));
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图点查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.icss.km.gis.ui.dwr.GISMapDataFinder#findXmlByRadius(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpSession)
	 */
	public String findXmlByRadius(String mapDefRefId, String returnColumns,
			String x, String y, String radius, HttpSession session)
			throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		double x0 = new Double(x).doubleValue();
		double y0 = new Double(y).doubleValue();
		double radius0 = new Double(radius).doubleValue();
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByRadius(new DoublePoint(x0, y0), radius0);
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图半径范围查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}

	}

	/**
	 * 获取用于显示的图元字段描述信息
	 * 
	 * @param tableInfo
	 *            表定义
	 * @return
	 */
	protected List<Integer> getViewColumnIndexes(TableInfo tableInfo,
			String[] colNames) throws Exception {
		List<Integer> colIndexes = new ArrayList<Integer>();
		for (int i = 0; i < colNames.length; i++) {
			colIndexes.add(tableInfo.getColumnIndex(colNames[i]));
		}
		return colIndexes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#findXmlByRect(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, javax.servlet.http.HttpSession)
	 */
	public String findXmlByRect(String mapDefRefId, String returnColumns,
			String x0, String y0, String x1, String y1, HttpSession session)
			throws GISException {
		List featureSetList  = new ArrayList();
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		DoubleRect rect = getRect(x0, y0, x1, y1);
		try {
			featureSetList = theMap.searchByRect(rect);
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图矩形查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 根据图元集合构造xml
	 * 
	 * @param returnColumns
	 *            xml中需要包含的字段数据,格式column0,column1，逗号分隔
	 * @param fs
	 *            图元集合
	 * @return xml
	 * @throws Exception
	 * @throws GISException
	 */
	private String buildXml(String returnColumns, List fsList)
			throws Exception, GISException {
		TableInfo tableInfo = null;
		if(fsList!=null&&fsList.size()>0){
			tableInfo = ((FeatureSet)fsList.get(0)).getTableInfo();
		}
		String[] cols = returnColumns.split(",");
		return "";
//		return FeatureSetTools.convert2Xml(fsList, this.getViewColumnIndexes(
//				tableInfo, cols));
	}

	/**
	 * 将字符表示的矩形转为DoubleRect对象
	 * 
	 * @param x0
	 *            左上角x坐标
	 * @param y0
	 *            左上角y坐标
	 * @param x1
	 *            右下角x坐标
	 * @param y1
	 *            右下角y坐标
	 * @return DoubleRect
	 */
	private DoubleRect getRect(String x0, String y0, String x1, String y1) {
		double left = new Double(x0);
		double top = new Double(y0);
		double right = new Double(x1);
		double down = new Double(y1);
		DoubleRect rect = new DoubleRect(left, top, right, down);
		return rect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.dwr.CigStoreFinder#findXmlByRegion(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpSession)
	 */
	public String findXmlByRegion(String mapDefRefId, String returnColumns,
			String pcount, String encodedPoints, HttpSession session)
			throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		List<DoublePoint> points = getPointList(encodedPoints);
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchByRegion(points);
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图多边形查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.icss.km.gis.ui.dwr.CigStoreFinder#findXmlById(java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpSession)
	 */
	public String findXmlById(String mapDefRefId, String returnColumns,
			String id, HttpSession session) throws GISException {
		SearchableGISMap theMap = getMapFromSession(mapDefRefId, session);
		List featureSetList  = new ArrayList();
		try {
			featureSetList = theMap.searchById(id.split(","));
			return buildXml(returnColumns, featureSetList);
		} catch (Exception e) {
			log.error("地图主键查询失败", e);
			throw new GISException(e);
		} finally {
			if (featureSetList != null) {
				try {
					for(int i=0;i<featureSetList.size();i++){
						FeatureSet fs = (FeatureSet)featureSetList.get(i);
						fs.dispose();
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
}