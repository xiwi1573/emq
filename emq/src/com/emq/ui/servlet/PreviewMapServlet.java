package com.emq.ui.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.emq.config.SystemConfig;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.AbstractGISMap;

/**
 * Àı¬‘Õºservlet
 * 
 * @author guqiong
 * @created 2009-9-21
 * 
 */
public class PreviewMapServlet extends AbstractMapServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(PreviewMapServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.ui.servlet.AbstractMapServlet#render(com.icss.km.gis.
	 * model.AbstractGISMap, javax.servlet.http.HttpServletRequest,
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

			map.renderPreviewMap(outs, imageWidth, imageHight);
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

	public PreviewMapServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.servlet.AbstractMapServlet#destroy()
	 */
	public void destroy() {
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.ui.servlet.AbstractMapServlet#init()
	 */
	public void init() throws ServletException {

	}

}
