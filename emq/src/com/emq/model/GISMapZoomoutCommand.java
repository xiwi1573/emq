package com.emq.model;

import com.emq.config.SystemConfig;
import com.emq.exception.GISException;
import com.mapinfo.util.DoublePoint;

/**
 * 放大地图命令
 * 
 * @author guqiong
 * @created 2009-9-22
 */
public class GISMapZoomoutCommand extends AbstractGISMapControlCommand {
	/**
	 * 地图中心, 屏幕坐标系
	 */
	private DoublePoint zoomCenter;
	/**
	 * 缩放级别
	 */
	private Double zoom;

	/**
	 * 默认放大命令
	 * 
	 * @param map
	 */
	public GISMapZoomoutCommand(AbstractGISMap map) throws GISException {
		super(map);
		zoom = new Double(map.getZoom());
		zoom = zoom / SystemConfig.getInstance().getMapDefaultZoomRatio();
	}

	/**
	 * 指定中心点放大命令
	 * 
	 * @param map
	 *            地图容器
	 * @param x
	 *            中心点x坐标, 屏幕坐标系
	 * @param y
	 *            中心点y坐标, 屏幕坐标系
	 */
	public GISMapZoomoutCommand(AbstractGISMap map, Double x, Double y)
			throws GISException {
		super(map);
		zoom = new Double(map.getZoom());
		zoom = zoom / SystemConfig.getInstance().getMapDefaultZoomRatio();
		zoomCenter = new DoublePoint(x, y);
		
	}

	/**
	 * 选择矩形放大命令
	 * 
	 * @param map
	 *            map 地图容器
	 * @param left
	 *            矩形左上角x, 屏幕坐标系
	 * @param top
	 *            矩形左上角y, 屏幕坐标系
	 * @param right
	 *            矩形右下角x, 屏幕坐标系
	 * @param down
	 *            矩形右下角y, 屏幕坐标系
	 * @throws GISException
	 */
	public GISMapZoomoutCommand(AbstractGISMap map, Double left, Double top,
			Double right, Double down) throws GISException {
		super(map);
		zoom = new Double(map.getZoom());
		// TODO:计算缩放比例
		zoom = zoom / SystemConfig.getInstance().getMapDefaultZoomRatio();
		zoomCenter = new DoublePoint(right - left, down - top);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		super.getGISMap().setZoom(zoom.doubleValue());
		if (zoomCenter != null)
			super.getGISMap().setScreenCenter(
					new DoublePoint(zoomCenter.x, zoomCenter.y));
	}

}
