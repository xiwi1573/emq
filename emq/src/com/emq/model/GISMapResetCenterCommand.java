package com.emq.model;

import com.emq.exception.GISException;

/**
 * 恢复地图默认中心点，恢复地图缩放级别
 * 
 * @author guqiong
 * @created 2009-10-19
 */
public class GISMapResetCenterCommand extends AbstractGISMapControlCommand {

	public GISMapResetCenterCommand(AbstractGISMap map) {
		super(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		this.getGISMap().resetCenter();
		this.getGISMap().resetZoom();
	}
}
