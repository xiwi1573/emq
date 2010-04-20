package com.emq.model;

import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.util.DoublePoint;

/**
 * 将主地图中心点定位至缩略图指定的点坐标
 * 
 * @author guqiong
 * @created 2009-9-28
 */
public class GISMapCenterPreviewCommand extends AbstractGISMapControlCommand {
	/**
	 * 主地图中心, 地理坐标系
	 */
	private DoublePoint newMainMapGISCenter;
	
	private static Logger log = Logger.getLogger(GISMapCenterPreviewCommand.class);

	public GISMapCenterPreviewCommand(AbstractGISMap map) {
		super(map);
	}

	/**
	 * @param map
	 * @param x
	 *            点在缩略图的x坐标，屏幕坐标系
	 * @param y
	 *            点在缩略图的y坐标，屏幕坐标系
	 */
	public GISMapCenterPreviewCommand(AbstractGISMap map, double x, double y)
			throws GISException {
		super(map);
		log.debug("缩略图坐标点：x=" + String.valueOf(x) + ", y=" + String.valueOf(y));
		newMainMapGISCenter = super.getGISMap().convert2PreviewMapPosition(
				new DoublePoint(x, y));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		super.getGISMap().centerTo((DoublePoint) newMainMapGISCenter.clone());
	}
}
