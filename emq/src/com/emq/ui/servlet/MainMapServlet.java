package com.emq.ui.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.emq.config.SystemConfig;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.AbstractGISMap;

/**
 * Ö÷µØÍ¼Êä³öservlet
 * 
 * @author guqiong
 * @created 2009-9-21
 */
public class MainMapServlet extends AbstractMapServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(MainMapServlet.class);

	public MainMapServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.servlet.AbstractMapSerlet#render(com.icss.km.gis.model
	 * .AbstractGISMap, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void render(AbstractGISMap map, HttpServletRequest request,
			HttpServletResponse response) throws GISException {
		OutputStream outs = null;
		try {
			response.setContentType(SystemConfig.getInstance().getMapMime());
			outs = response.getOutputStream();

			Integer imageWidth = this.getOutImageWidth(request);
			Integer imageHight = this.getOutImageHeight(request);

			map.renderMainMap(outs, imageWidth, imageHight);
			outs.flush();
		} catch (IOException e) {
			throw new GISException(e);
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}

}
