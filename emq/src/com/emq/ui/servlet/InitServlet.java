package com.emq.ui.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.emq.config.SystemConfig;
import com.emq.logger.Logger;
import com.emq.model.pojo.GisConRegion;
import com.emq.dao.GisConRegionDao;
/**
 * 系统启动初始化
 * @author guqiong
 * @created 2009-10-30
 */
public class InitServlet extends HttpServlet {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(InitServlet.class);
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		log.debug("gis-map init begin");
		super.init();
		WebApplicationContext   ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext()); 
		GisConRegionDao gisConRegionDao=(GisConRegionDao)ctx.getBean("gisConRegionDao");  
		SystemConfig.getInstance().init(); 
		//List<GisConRegion> gisConRegionList = gisConRegionDao.getValidGisConRegions();
		//SystemConfig.getInstance().loadOrgMapMappings(gisConRegionList);
		log.debug("gis-map init end");
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		SystemConfig.getInstance().clean();
		super.destroy();
		
	}
}
