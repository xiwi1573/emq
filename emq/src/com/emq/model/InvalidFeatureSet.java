package com.emq.model;

import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet; 
import com.mapinfo.dp.TableInfo;
import com.mapinfo.dp.util.RewindableFeatureSet; 

/**
 * 无效坐标的FeatureSet,getNextFeature只返回无效的gis数据
 * @author guqiong
 * @created 2009-11-24
 */
public class InvalidFeatureSet extends RewindableFeatureSet {

	private static Logger log = Logger.getLogger(InvalidFeatureSet.class);
	
	private AbstractGISMap map;
	
	private TableInfo tableInfo ;
	public InvalidFeatureSet(FeatureSet arg0) {
		super(arg0); 
	}
	
	public InvalidFeatureSet(FeatureSet arg0, AbstractGISMap map){
		super(arg0); 
		try{
			tableInfo = arg0.getTableInfo();
			this.map = map;
		}catch(Exception e){
			log.error(ErrorMsgConstants.EMQ_UNKNOWN_01, e);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7222140887685098885L;
	
	/* (non-Javadoc)
	 * @see com.mapinfo.dp.util.RewindableFeatureSet#getNextFeature()
	 */
	public Feature getNextFeature() throws Exception{	 
		try {
			do {
				Feature f = super.getNextFeature(); 
				if (f == null)
					return null;
				// 只返回无效的数据
				if(!map.isValidFeature(f, tableInfo))
					return f;
			} while (true);
			
		} catch (Exception e) {
			log.error(ErrorMsgConstants.EMQ_MAP_09, e);
			throw new GISException(e);
		}
	}

}
