package com.emq.model;

import org.apache.commons.beanutils.BeanUtils;

import com.emq.config.MapDef;
import com.emq.config.SystemConfig;
import com.emq.exception.GISException;

/**
 * »ù´¡µØÍ¼ÇÐ»»
 * 
 * @author guqiong
 * @created 2009-10-26
 */
public class GISMapSwitchCommand extends AbstractGISMapControlCommand {
	private String mapDefId;

	public GISMapSwitchCommand(AbstractGISMap map, String mapDefId) {
		super(map);
		this.mapDefId = mapDefId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		try{
			MapDef newDef = SystemConfig.getInstance().getMapDefines().getMapDefById(mapDefId);
			MapDef oldDef  = (MapDef) BeanUtils.cloneBean(this.getGISMap().getMapDef());
			newDef.setSpatialSearchLayerDefineList(oldDef.getSpatialSearchLayerDefineList());
			newDef = newDef.mergeParent();
			newDef = newDef.mergeFrom(oldDef);
			this.getGISMap().reloadMap(newDef);
			this.getGISMap().reset();
		}catch(Exception e){
			throw new GISException(e);
		}
	}
}
