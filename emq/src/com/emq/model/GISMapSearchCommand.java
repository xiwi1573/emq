package com.emq.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;

/**
 * 地图查询命令
 * 
 * @author guqiong
 * @created 2009-9-23
 * @history 2009-10-14 guqiong 增加半径范围查询支持
 * 			2009-10-19 guqiong 增加支持多值主键查询
 * 
 */
public class GISMapSearchCommand extends AbstractGISMapControlCommand {

	Logger log = Logger.getLogger(GISMapSearchCommand.class);
	/**
	 * 查询点坐标, 屏幕坐标系
	 */
	private DoublePoint point;	
	/**
	 * 半径，屏幕坐标系
	 */
	private double radius;
	/**
	 * 查询矩形范围, 屏幕坐标系
	 */
	private DoubleRect rectangle;
	/**
	 * 查询的主键
	 */
	private String[] pk;
	/**
	 * 查询的多边形顶点, 屏幕坐标系
	 */
	private List<DoublePoint> regionPoints;
	/**
	 * 查询类型
	 */
	private Integer searchType;
	/**
	 * 查询类型-点查询
	 */
	private static final int SEARCH_POINT = 1;
	/**
	 * 查询类型-矩形范围查询
	 */
	private static final int SEARCH_RECTANGLE = 2;
	/**
	 * 查询类型-根据图元主键查询
	 */
	private static final int SEARCH_BY_PK = 3;
	/**
	 * 查询类型-多边形范围查询
	 */
	private static final int SEARCH_REGION = 4;
	/**
	 * 查询类型-半径范围查询
	 */
	private static final int SEARCH_CIRCLE = 5;

	/**
	 * 构造主键查询名
	 * @deprecated 2009-10-19
	 * 被替代GISMapSearchCommand(AbstractGISMap map, String[] pk) 
	 * @param map
	 * @param pk
	 */
	public GISMapSearchCommand(AbstractGISMap map, String pk) {
		this(map, new String[]{pk});
	}
	
	public GISMapSearchCommand(AbstractGISMap map, String[] pk) {
		super(map);
		this.pk = pk.clone();
		searchType = SEARCH_BY_PK;
	}
	
	private SearchableGISMap getMap(){
		return (SearchableGISMap) super.map;
	}

	/**
	 * 构造点查询命令
	 * 
	 * @param map
	 *            地图容器
	 * @param x
	 *            选择点的x坐标, 屏幕坐标系
	 * @param y
	 *            选择点的y坐标, 屏幕坐标系
	 */
	public GISMapSearchCommand(AbstractGISMap map, Double x, Double y) {
		super(map);
		point = new DoublePoint(x, y);
		searchType = SEARCH_POINT;
	}

	/**
	 * 构造半径范围查询命令
	 * 
	 * @param map
	 *            地图容器
	 * @param radius
	 *            半径, 屏幕坐标系
	 * @param x
	 *            圆心的x坐标, 屏幕坐标系
	 * @param y
	 *            圆心的y坐标, 屏幕坐标系
	 */
	public GISMapSearchCommand(AbstractGISMap map, Double radius, Double x,
			Double y) {
		super(map);
		point = new DoublePoint(x, y);
		this.radius = radius;
		searchType = SEARCH_CIRCLE;
	}

	/**
	 * 构造多边形查询的命令
	 * 
	 * @param map
	 *            地图容器
	 * @param points
	 *            多边形的顶点坐标, 屏幕坐标系
	 */
	public GISMapSearchCommand(AbstractGISMap map, List<Double[]> points) {
		super(map);
		regionPoints = new ArrayList<DoublePoint>();
		Iterator<Double[]> iter = points.iterator();
		while (iter.hasNext()) {
			Double[] p = iter.next();
			double x = p[0].doubleValue();
			double y = p[1].doubleValue();
			regionPoints.add(new DoublePoint(x, y));
		}
		this.searchType = SEARCH_REGION;
	}

	/**
	 * 构造矩形查询命令
	 * 
	 * @param map
	 *            地图容器
	 * @param x0
	 *            矩形左上角x坐标, 屏幕坐标系
	 * @param y0
	 *            矩形左上角y坐标, 屏幕坐标系
	 * @param x1
	 *            矩形右下角x坐标, 屏幕坐标系
	 * @param y1
	 *            矩形右下角y坐标, 屏幕坐标系
	 */
	public GISMapSearchCommand(AbstractGISMap map, Double x0, Double y0,
			Double x1, Double y1) {
		super(map);
		rectangle = new DoubleRect(x0, y0, x1, y1);
		searchType = SEARCH_RECTANGLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		if(!(map instanceof SearchableGISMap))
			throw new GISException(ErrorMsgConstants.KMGIS_MAP_04);		
		try {
			switch (searchType.intValue()) {
			case SEARCH_POINT:
				getMap().searchAndHighlight((DoublePoint) point.clone());
				break;
			case SEARCH_RECTANGLE:
				getMap().searchAndHighlight((DoubleRect) rectangle.clone());
				break;
			case SEARCH_BY_PK:
				getMap().searchAndHighlight(pk);
				break;
			case SEARCH_REGION:
				getMap().searchAndHighlight(regionPoints);
				break;
			case SEARCH_CIRCLE:
				getMap().searchAndHighlight((DoublePoint) point.clone(), radius);
				break;
			// 不应执行至此
			default:
				log.error("不支持的查询类型：searchType=");
				log.error(searchType);
			}
		} catch (GISException e) {
			log.error(e);
			throw new GISException(e);
		}
	}

}
