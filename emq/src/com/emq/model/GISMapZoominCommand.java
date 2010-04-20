package com.emq.model;

import com.emq.config.SystemConfig;
import com.emq.exception.GISException;
import com.mapinfo.util.DoublePoint;

/**
 * 缩小地图命令
 * 
 * @author guqiong
 * @created 2009-9-22
 */
public class GISMapZoominCommand extends AbstractGISMapControlCommand {
	/**
	 *  地图中心, 屏幕坐标系
	 */
	private DoublePoint zoomCenter;
	/**
	 *  缩放级别
	 */
	private Double zoom;

	/**
	 * 默认缩小命令
	 * 
	 * @param map
	 *            地图容器
	 */
	public GISMapZoominCommand(AbstractGISMap map) throws GISException {
		super(map);
		zoom = new Double(map.getZoom());
		zoom = zoom * SystemConfig.getInstance().getMapDefaultZoomRatio();
	}

	/**
	 * 指定中心点缩小命令
	 * 
	 * @param map
	 *            地图容器
	 * @param x
	 *            中心点x坐标, 屏幕坐标系
	 * @param y
	 *            中心点y坐标, 屏幕坐标系
	 */
	public GISMapZoominCommand(AbstractGISMap map, Double x, Double y)
			throws GISException {
		super(map);
		zoom = new Double(map.getZoom());
		zoom = zoom * SystemConfig.getInstance().getMapDefaultZoomRatio();
		zoomCenter = new DoublePoint(x, y);
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
