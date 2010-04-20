package com.emq.model;

import com.emq.exception.GISException;

/**
 * »Ö¸´µØÍ¼³õÊ¼×´Ì¬
 * 
 * @author guqiong
 * @created 2009-9-23
 */
public class GISMapResetCommand extends AbstractGISMapControlCommand {

	public GISMapResetCommand(AbstractGISMap map) {
		super(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMapControlCommand#doExecute()
	 */
	public void doExecute() throws GISException {
		this.getGISMap().reset();
	}
}
