package com.emq.ui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.AbstractGISMap;
import com.emq.model.AbstractGISMapControlCommand;
import com.emq.model.GISMapCommandParser;
import com.emq.model.GISMapFactory;
import com.emq.model.GISMapInitCommand;
import com.emq.ui.filter.RequestMappingFilter;
import com.emq.ui.formbean.MapFormBean;

/**
 * 地图控制servlet
 * <p>
 * AbstractMapServlet负责处理请求参数，并调用GISMapCommandParser构造命令对象，最后执行地图操作命令以更新地图状态。
 * <p>
 * AbstractMapServlet将输出图片的选择交给子类，由子类决定输出主地图或是缩略图。
 * 
 * @author guqiong
 * @created 2009-9-21
 */
public abstract class AbstractMapServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AbstractMapServlet.class);

	/* url参数名称 */
	/**
	 * 地图控制命令类型
	 */
	private static final String ACTION = "action";
	/**
	 * 缩放类型
	 */
	private static final String ZOOM_TYPE = "zoomtype";
	/**
	 * 点x坐标
	 */
	private static final String POINT_X = "x";
	/**
	 * 点y坐标
	 */
	private static final String POINT_Y = "y";
	/**
	 * 矩形左上角x坐标
	 */
	private static final String RECTANGLE_LEFT = "left";
	/**
	 * 矩形左上角Y坐标
	 */
	private static final String RECTANGLE_TOP = "top";
	/**
	 * 矩形右下角x坐标
	 */
	private static final String RECTANGLE_RIGHT = "right";
	/**
	 * 矩形右下角y坐标
	 */
	private static final String RECTANGLE_DOWN = "down";
	/**
	 * 平移起点x坐标
	 */
	private static final String START_X = "startx";
	/**
	 * 平移起点y坐标
	 */
	private static final String START_Y = "starty";
	/**
	 * 平移终点x坐标
	 */
	private static final String END_X = "endx";
	/**
	 * 平移终点y坐标
	 */
	private static final String END_Y = "endy";
	/**
	 * 多边形顶点数
	 */
	private static final String REGION_PCOUNT = "pcount";
	/**
	 * 多边形顶点字符串
	 */
	private static final String REGION_POINTS = "points";
	/**
	 * 选择的图元类型
	 */
	private static final String SELELCT_TYPE = "selecttype";
	/**
	 * 平移类型
	 */
	private static final String MOVE_TYPE = "movetype";
	/**
	 * 选中的图元主键
	 */
	private static final String ID = "id";	
	/**
	 * 半径
	 */
	private static final String RADIUS = "radius";		
	/**
	 * 区域地图id
	 */
	private static final String AREA_MAP_ID = "areamapid";
	/**
	 * 地图使用单位id
	 */
	private static final String ORG_ID = "orgId";
	
	/**
	 * 地图容器在会话中的键名前缀，避免重复
	 * <p>
	 * 键名的全称为：前缀+maprefid, 例如KMGIS.MAP.CigStoreMap
	 */
	public static final String MAP_IN_SESSION_KEY_PREF = "KMGIS.MAP.";

	public AbstractMapServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获取地图控制操作参数
		MapFormBean requestForm = getRequestForm(request);
		String mapRefId = requestForm.getMapRefId();
		String orgId = requestForm.getOrgId();
		try {
			AbstractGISMap cigStoreMap = null;
			// 执行地图操作,如果会话超时（session中没有地图容器）,则执行初始化命令
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

			GISMapFactory gisMapFactory = (GISMapFactory) ctx.getBean("gisMapFactory");			
			if (isMapInSession(request.getSession(), mapRefId)) {
				cigStoreMap = getMap(request.getSession(), mapRefId, gisMapFactory);
				AbstractGISMapControlCommand command = GISMapCommandParser
						.parse(cigStoreMap, requestForm);
				log.debug(request.getRequestURL()+ "?"  + request.getQueryString());
				log.debug(requestForm.getMapRefId());
				log.debug("执行命令: ");
				log.debug(command.getClass().getName());
				log.debug("请求参数：");
				log.debug("requestForm：");
				log.debug(requestForm.toString());
				cigStoreMap = command.execute();
			} else {
				cigStoreMap = getMap(request.getSession(), mapRefId, orgId, gisMapFactory);
				AbstractGISMapControlCommand command = new GISMapInitCommand(
						cigStoreMap);
				command.execute();
			}
			// 输出地图
			render(cigStoreMap, request, response);
		} catch (GISException e) {
			String errMsg = "执行地图操作失败: action=";
			errMsg += requestForm.getAction() == null ? " null " : requestForm
					.getAction();
			log.error(errMsg);
			// TODO:跳转到出错信息页面
			throw new ServletException(e);
		}
	}

	/**
	 * 渲染地图，输出
	 * 
	 * @param map
	 *            地图容器
	 * @param request
	 * @param response
	 * @throws GISException
	 */
	protected abstract void render(AbstractGISMap map,
			HttpServletRequest request, HttpServletResponse response)
			throws GISException;

	/**
	 * 从请求参数值构造MapFormBean对象
	 * 
	 * @param request
	 * @return
	 */
	protected MapFormBean getRequestForm(HttpServletRequest request) {
		MapFormBean bean = new MapFormBean();
		bean.setMapRefId((String) request
				.getAttribute(RequestMappingFilter.MAP_REF_ID_KEY));
		bean.setAction(request.getParameter(ACTION));
		bean.setZoomType(request.getParameter(ZOOM_TYPE));
		bean.setZoomX(request.getParameter(POINT_X));
		bean.setZoomY(request.getParameter(POINT_Y));
		bean.setZoomoutLeft(request.getParameter(RECTANGLE_LEFT));
		bean.setZoomoutRight(request.getParameter(RECTANGLE_RIGHT));
		bean.setZoomoutTop(request.getParameter(RECTANGLE_TOP));
		bean.setZoomoutDown(request.getParameter(RECTANGLE_DOWN));
		bean.setMoveType(request.getParameter(MOVE_TYPE));
		bean.setMoveStartX(request.getParameter(START_X));
		bean.setMoveStartY(request.getParameter(START_Y));
		bean.setMoveEndX(request.getParameter(END_X));
		bean.setMoveEndY(request.getParameter(END_Y));
		bean.setSelectType(request.getParameter(SELELCT_TYPE));
		bean.setSelectLeft(request.getParameter(RECTANGLE_LEFT));
		bean.setSelectRight(request.getParameter(RECTANGLE_RIGHT));
		bean.setSelectTop(request.getParameter(RECTANGLE_TOP));
		bean.setSelectDown(request.getParameter(RECTANGLE_DOWN));
		bean.setSelectX(request.getParameter(POINT_X));
		bean.setSelectY(request.getParameter(POINT_Y));
		bean.setSelectId(request.getParameter(ID));
		bean.setSelectRadius(request.getParameter(RADIUS));
		bean.setAreaMapId(request.getParameter(AREA_MAP_ID));
		bean.setOrgId(request.getParameter(ORG_ID));
		// 根据多边形顶点数获取顶点参数集合
		String sPcount = request.getParameter(REGION_PCOUNT);
		String points = request.getParameter(REGION_POINTS);
		List<String[]> selectRegionPoints = new ArrayList<String[]>();
		if (sPcount != null) {
			Integer pcount = new Integer(sPcount);
			bean.setSelectRegionPCount(sPcount);
			String[] regionPoints = points.split(";");
			if (pcount.intValue() != regionPoints.length)
				log.error("多边形顶点数与实际不一致");
			for (int i = 0; i < regionPoints.length; i++) {
				String[] point = regionPoints[i].split(",");
				selectRegionPoints.add(point);
			}
			bean.setSelectRegionPoints(selectRegionPoints);
		}
		return bean;
	}

	/**
	 * 获取session中的地图对象,如果session中没有就新建一个
	 * 
	 * @param session
	 *            HttpSession
	 * @return AbstractGISMap
	 */
	private synchronized AbstractGISMap getMap(HttpSession session,
			String mapRefId, GISMapFactory gisMapFactory) throws GISException {
		return this.getMap(session, mapRefId, null, gisMapFactory);
	}
	
	/**
	 * 获取session中的地图对象,如果session中没有就新建一个
	 * 
	 * @param session
	 *            HttpSession
	 * @param mapRefId
	 * 			地图引用id
	 * @param orgId
	 * 			地图使用的组织机构id,为空表示从配置文件读取
	 * @return AbstractGISMap
	 */
	private synchronized AbstractGISMap getMap(HttpSession session,
			String mapRefId, String orgId, GISMapFactory gisMapFactory) throws GISException {
		String key = MAP_IN_SESSION_KEY_PREF + mapRefId;
		AbstractGISMap map = (AbstractGISMap) session.getAttribute(key);
		if (map == null) {
			if(orgId == null || "".equals(orgId))
				map = gisMapFactory.create(mapRefId);
			else
				map = gisMapFactory.create(mapRefId, orgId);
			session.setAttribute(key, map);
		}
		return map;
	}

	/**
	 * 地图对象是否在会话中
	 * 
	 * @param session
	 *            HttpSession
	 * @return
	 */
	private synchronized boolean isMapInSession(HttpSession session,
			String mapRefId) {
		String key = MAP_IN_SESSION_KEY_PREF + mapRefId;
		return session.getAttribute(key) == null ? false : true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {

	}

	/**
	 * 获取输出图片宽度
	 * 
	 * @param request
	 * @return
	 */
	protected Integer getOutImageWidth(HttpServletRequest request) {
		String imageWidth = request.getParameter("imagewidth");
		if (imageWidth == null || "".equals(imageWidth)) {
			log.error("调用没有指定图片的宽度：");
			log.error(request.getRequestURL());
			return null;
		} else
			return new Integer(imageWidth);
	}

	/**
	 * 获取输出图片高度
	 * 
	 * @param request
	 * @return
	 */
	protected Integer getOutImageHeight(HttpServletRequest request) {
		String imageHeight = request.getParameter("imageheight");
		if (imageHeight == null || "".equals(imageHeight)) {
			log.error("调用没有指定图片的高度：");
			log.error(request.getRequestURL());
			return null;
		} else
			return new Integer(imageHeight);
	}

}
