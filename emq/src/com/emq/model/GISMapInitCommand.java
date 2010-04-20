package com.emq.model;

import com.emq.exception.GISException;

/**
 * 初始化地图，设置地图的默认参数
 * 
 * @author guqiong
 * @created 2009-9-21
 * @history 2009-10-15	guqiong  fix bug: KMGIS-20 GISMapInitCommand将重置地图状态 
 */
public class GISMapInitCommand extends AbstractGISMapControlCommand {

	public GISMapInitCommand(AbstractGISMap map) {
		super(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		this.getGISMap().loadMap();
		//重置地图状态，因为loadMap有缓存
		this.getGISMap().reset();
	}
}
