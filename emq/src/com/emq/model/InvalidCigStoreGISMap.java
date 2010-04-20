package com.emq.model;
 
import com.emq.exception.GISException; 
import com.mapinfo.dp.FeatureSet; 
import com.mapinfo.mapj.FeatureLayer; 

/**
 * 查询无效坐标的卷烟专卖点地图
 * 
 * @author guqiong
 * @created 2009-11-24
 */
public class InvalidCigStoreGISMap extends CigStoreGISMap {
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.icss.km.gis.model.SearchableGISMap#afterSearch(com.mapinfo.mapj.
	 * FeatureLayer, com.mapinfo.dp.FeatureSet)
	 */
	protected FeatureSet afterSearch(final FeatureLayer layer, final FeatureSet fs)
			throws GISException { 
		return new InvalidFeatureSet(fs, this);
	}
}
