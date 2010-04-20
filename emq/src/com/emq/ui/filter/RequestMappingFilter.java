package com.emq.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.emq.logger.Logger;

/**
 * 将request请求的内容,将请求转发至地图服务的servlet,因此，RequestMappingFilter应作为最后一个Filter放在web.xml中。
 * <p>
 * 用作地图服务的第一入口,判断请求的是处理主地图还是缩略图，并获取请求的地图引用id, 将地图引用id转发给相应的servlet。
 * <p>
 * 显示主地图：
 * <p>
 * http://服务器/应用名/gis/main-map/地图引用id
 * <p>
 * 显示缩略地图:
 * <p>
 * http://服务器/应用名/gis/preview-map/地图引用id
 * <p>
 * 地图引用id：
 * <p>
 * 在配置文件中定义，见src/gis-map.xml。
 * 
 * @author guqiong
 * @created 2009-9-27
 */
public class RequestMappingFilter implements Filter {
	private static Logger log = Logger.getLogger(RequestMappingFilter.class);
	/**
	 * 主地图url前缀
	 */
	private static final String MAIN_MAP_URL_PREF = "/gis/main-map";

	/**
	 * 缩略地图url前缀
	 */
	private static final String PREVIEW_MAP_URL_PREF = "/gis/preview-map";

	/**
	 * 主地图servlet
	 */
	private static final String MAIN_MAP_SERVLET = "/servlet/MainMapServlet";

	/**
	 * 缩略图servlet
	 */
	private static final String PREVIEW_MAP_SERVLET = "/servlet/PreviewMapServlet";

	/**
	 * 地图引用id放入request中的key
	 */
	public static final String MAP_REF_ID_KEY = "KMGIS.MAP_REF_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		log.debug("RequestMappingFilter enter:");
		HttpServletRequest request = (HttpServletRequest) req;
		String servlet = request.getRequestURI();
		String MapRefID = null;
		log.debug("servlet:");
		log.debug(servlet);
		if (servlet != null && servlet.trim().length() > 1) {
			if (request.getContextPath() != null) {
				servlet = servlet.substring(request.getContextPath().length());
			}
			log.debug("servlet:");
			log.debug(servlet);
			// 如果url地址是调用主地图，转发至主地图处理的servlet
			if (servlet.indexOf(MAIN_MAP_URL_PREF) != -1) {
				int i = servlet.indexOf(MAIN_MAP_URL_PREF);
				MapRefID = servlet
						.substring(i + MAIN_MAP_URL_PREF.length() + 1);
				String forward = MAIN_MAP_SERVLET;
				request.setAttribute(MAP_REF_ID_KEY, MapRefID);
				
				log.debug("forward:"); 
				log.debug(forward);
				log.debug("MapRefID:");
				log.debug(MapRefID);
				req.getRequestDispatcher(forward).forward(req, res);
				return;
			}
			// 如果url地址是调用缩略地图，转发至缩略地图处理的servlet
			if (servlet.indexOf(PREVIEW_MAP_URL_PREF) != -1) {
				int i = servlet.indexOf(PREVIEW_MAP_URL_PREF);
				MapRefID = servlet.substring(i + PREVIEW_MAP_URL_PREF.length()
						+ 1);
				String forward = PREVIEW_MAP_SERVLET;
				request.setAttribute(MAP_REF_ID_KEY, MapRefID);
				req.getRequestDispatcher(forward).forward(req, res);
				return;
			}
		}
		chain.doFilter(req, res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		log.debug("RequestMappingFilter INIT");
	}

}
