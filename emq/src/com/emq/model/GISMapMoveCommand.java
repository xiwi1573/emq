package com.emq.model;

import com.emq.config.SystemConfig;
import com.emq.exception.GISException;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;

/**
 * 平移地图命令
 * 
 * @author guqiong
 * @created 2009-9-22
 */
public class GISMapMoveCommand extends AbstractGISMapControlCommand {
	/**
	 *  地图中心, 屏幕坐标系
	 */
	private DoublePoint newCenter;
	/**
	 * 移动方向-向东
	 */
	public static final int DIRECTION_EAST = 1;
	/**
	 * 移动方向-向南
	 */
	public static final int DIRECTION_SOUTH = 2;
	/**
	 * 移动方向-向西
	 */
	public static final int DIRECTION_WEST = 3;
	/**
	 * 移动方向-向北
	 */
	public static final int DIRECTION_NORTH = 4;

	/**
	 * 构造定向移动命令
	 * 
	 * @param map
	 *            地图容器
	 * @param direction
	 *            指定的方向
	 */
	public GISMapMoveCommand(AbstractGISMap map, int direction)
			throws GISException {
		super(map);
		DoublePoint center = super.getGISMap().getScreenCenter();
		DoubleRect deviceBounds = getGISMap().getDeviceBounds();
		// 计算默认的x轴上移动距离
		double moveXDistance = deviceBounds.width()
				* SystemConfig.getInstance().getMapDefaultMoveRatio();
		// 计算默认的y轴上移动距离
		double moveYDistance = deviceBounds.height()
				* SystemConfig.getInstance().getMapDefaultMoveRatio();

		switch (direction) {
		case DIRECTION_EAST:
			newCenter = new DoublePoint(center.x + moveXDistance, center.y);
			break;
		case DIRECTION_SOUTH:
			newCenter = new DoublePoint(center.x, center.y + moveYDistance);
			break;
		case DIRECTION_WEST:
			newCenter = new DoublePoint(center.x - moveXDistance, center.y);
			break;
		case DIRECTION_NORTH:
			newCenter = new DoublePoint(center.x, center.y - moveYDistance);
			break;

		}
	}

	/**
	 * 构造拖动命令
	 * 
	 * @param map
	 *            地图容器
	 * @param startX
	 *            移动起点X坐标
	 * @param startX
	 *            移动起点Y坐标
	 * @param endX
	 *            移动终点X坐标
	 * @param endY
	 *            移动终点Y坐标
	 */
	public GISMapMoveCommand(AbstractGISMap map, double startX, double startY,
			double endX, double endY) throws GISException {
		super(map);
		DoublePoint center = super.getGISMap().getScreenCenter();
		// 计算默认的x轴上移动距离
		double moveXDistance = endX - startX;
		// 计算默认的y轴上移动距离
		double moveYDistance = endY - startY;
		newCenter = new DoublePoint(center.x - moveXDistance, center.y
				- moveYDistance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		super.getGISMap().setScreenCenter(
				new DoublePoint(newCenter.x, newCenter.y));
	}

}
