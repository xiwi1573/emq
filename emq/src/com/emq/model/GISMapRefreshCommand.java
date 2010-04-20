package com.emq.model;

import com.emq.exception.GISException;

/**
 * 刷新地图，获取地图的最新状态。
 * 
 * @author guqiong
 * @created 2009-10-15
 */
public class GISMapRefreshCommand extends AbstractGISMapControlCommand {

	public GISMapRefreshCommand(AbstractGISMap map) {
		super(map);
	}

	/* (non-Javadoc)
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		
	}
}
