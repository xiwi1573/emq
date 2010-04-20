package com.emq.model;

import java.util.ArrayList;
import java.util.List;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.ui.formbean.MapFormBean;

/**
 * 地图命令解析器，根据参数请求构造命令对象。
 * <p>
 * 
 * @see com.icss.km.gis.ui.servlet.AbstractMapServlet
 * @see com.icss.km.gis.ui.formbean.MapFormBean
 * @author guqiong
 * @created 2009-9-21
 * @history 2009-10-14 guqiong 增加半径范围查询命令支持 2009-10-15 guqiong
 *          将不带commandType参数的请求使用GISMapRefreshCommand，原使用GISMapInitCommand
 *          增加COMMAND_INIT并解析为GISMapInitCommand
 * 
 */
public class GISMapCommandParser {

	private static Logger log = Logger.getLogger(GISMapCommandParser.class);
	/**
	 * 初始化命令
	 */
	public static final String COMMAND_INIT = "init";
	/**
	 * 重置命令
	 */
	public static final String COMMAND_RESET = "reset";
	/**
	 * 放大命令
	 */
	public static final String COMMAND_ZOOMOUT = "zoomout";
	/**
	 * 缩小命令
	 */
	public static final String COMMAND_ZOOMIN = "zoomin";
	/**
	 * 平移命令
	 */
	public static final String COMMAND_MOVE = "move";
	/**
	 * 选择命令
	 */
	public static final String COMMAND_SEARCH = "select";
	/**
	 * 缩略图定位命令
	 */
	public static final String COMMAND_CENTER_PREVIEW = "centerto";
	/**
	 * 回到地图默认中心点命令
	 */
	public static final String COMMAND_RESET_CENTER = "defaultcenter";
	/**
	 * 切换地图命令
	 */
	public static final String COMMAND_SWITCH_AREA = "switcharea";

	/**
	 * 解析请求参数，获取指定类型的GISMapControlCommand实例
	 * 
	 * @param map
	 *            命令需要操作的地图对象
	 * @param bean
	 *            请求操作参数
	 * @return GISMapControlCommand 地图控制命令
	 */
	public static AbstractGISMapControlCommand parse(AbstractGISMap map,
			MapFormBean bean) throws GISException {
		String commandType = bean.getAction();
		if (!bean.isValidate()) {
			log.error("地图 操作请求参数非法");
		}
		if (commandType == null || commandType.equals("")) {
			return new GISMapRefreshCommand(map);
		} else if (commandType.equals(COMMAND_INIT)) {
			return new GISMapInitCommand(map);
		} else if (commandType.equals(COMMAND_RESET)) {
			return new GISMapResetCommand(map);
		} else if (commandType.equals(COMMAND_ZOOMOUT)) {
			return createZoomoutCommand(map, bean);
		} else if (commandType.equals(COMMAND_ZOOMIN)) {
			return createZoominCommand(map, bean);
		} else if (commandType.equals(COMMAND_MOVE)) {
			return createMoveCommand(map, bean);
		} else if (commandType.equals(COMMAND_SEARCH)) {
			return createSearchCommand(map, bean);
		} else if (commandType.equals(COMMAND_CENTER_PREVIEW)) {
			double x = (new Double(bean.getSelectX())).doubleValue();
			double y = (new Double(bean.getSelectY())).doubleValue();
			return new GISMapCenterPreviewCommand(map, x, y);
		} else if (commandType.equals(COMMAND_RESET_CENTER)) {
			return new GISMapResetCenterCommand(map);
		} else if (commandType.equals(COMMAND_SWITCH_AREA)) {
			return new GISMapSwitchCommand(map, bean.getAreaMapId());
		} else {// TODO:解析其它命令
			log.error("不支持的命令类型:");
			log.error(commandType);
			return null;
		}
	}

	/**
	 * 实例化ZoomoutGISMapCommand
	 * 
	 * @param map
	 *            地图容器
	 * @param bean
	 *            请求参数
	 * @return ZoomoutGISMapCommand
	 */
	private static GISMapZoomoutCommand createZoomoutCommand(
			AbstractGISMap map, MapFormBean bean) throws GISException {
		GISMapZoomoutCommand command = null;
		if (bean.getZoomType() != null && !"".equals(bean.getZoomType())) {
			if (bean.getZoomType().equals("point")) { // 中心点放大
				return new GISMapZoomoutCommand(map,
						new Double(bean.getZoomX()),
						new Double(bean.getZoomY()));
			} else if (bean.getZoomType().equals("rectangle")) {// 矩形放大
				Double left = new Double(bean.getZoomoutLeft());
				Double top = new Double(bean.getZoomoutTop());
				Double right = new Double(bean.getZoomoutRight());
				Double down = new Double(bean.getZoomoutDown());
				return new GISMapZoomoutCommand(map, left, top, right, down);
			} else {
				log.error("不支持的放大类型:");
				log.error(bean.getZoomType());
				return null;
			}
		} else {
			// 默认放大
			command = new GISMapZoomoutCommand(map);
		}
		return command;
	}

	/**
	 * 实例化GISMapZoominCommand
	 * 
	 * @param map
	 *            地图容器
	 * @param bean
	 *            请求参数
	 * @return GISMapZoominCommand
	 */
	private static GISMapZoominCommand createZoominCommand(AbstractGISMap map,
			MapFormBean bean) throws GISException {
		GISMapZoominCommand command = null;
		if (bean.getZoomType() != null && !"".equals(bean.getZoomType())) {
			if (bean.getZoomType().equals("point")) { // 指定中心点缩小
				return new GISMapZoominCommand(map,
						new Double(bean.getZoomX()),
						new Double(bean.getZoomY()));
			} else {
				log.error("不支持的缩小类型:");
				log.error(bean.getZoomType());
				return null;
			}
		} else {
			// 默认缩小
			command = new GISMapZoominCommand(map);
		}
		return command;
	}

	/**
	 * 实例化GISMapMoveCommand
	 * 
	 * @param map
	 *            地图容器
	 * @param bean
	 *            请求参数
	 * @return GISMapMoveCommand
	 */
	private static GISMapMoveCommand createMoveCommand(AbstractGISMap map,
			MapFormBean bean) throws GISException {
		GISMapMoveCommand command = null;
		if ("drag".equals(bean.getMoveType())) { // 拖动地图命令
			Double startX = new Double(bean.getMoveStartX());
			Double startY = new Double(bean.getMoveStartY());
			Double endX = new Double(bean.getMoveEndX());
			Double endY = new Double(bean.getMoveEndY());
			return new GISMapMoveCommand(map, startX, startY, endX, endY);
		} else if ("left".equals(bean.getMoveType())) { // 左平移
			command = new GISMapMoveCommand(map,
					GISMapMoveCommand.DIRECTION_WEST);
		} else if ("down".equals(bean.getMoveType())) { // 下平移
			command = new GISMapMoveCommand(map,
					GISMapMoveCommand.DIRECTION_SOUTH);
		} else if ("right".equals(bean.getMoveType())) { // 右平移
			command = new GISMapMoveCommand(map,
					GISMapMoveCommand.DIRECTION_EAST);
		} else if ("up".equals(bean.getMoveType())) {// 上平移
			command = new GISMapMoveCommand(map,
					GISMapMoveCommand.DIRECTION_NORTH);
		} else {
			log.error("不支持的平移类型:");
			log.error(bean.getMoveType());
			return null;
		}
		return command;
	}

	/**
	 * 实例化GISMapSearchCommand
	 * 
	 * @param map
	 *            地图容器
	 * @param bean
	 *            请求参数
	 * @return GISMapSearchCommand
	 */
	private static GISMapSearchCommand createSearchCommand(AbstractGISMap map,
			MapFormBean bean) throws GISException {
		GISMapSearchCommand command = null;
		if (bean.getSelectType() != null && !"".equals(bean.getSelectType())) {
			if (bean.getSelectType().equals("point")) {// 点查询
				return new GISMapSearchCommand(map, new Double(bean
						.getSelectX()), new Double(bean.getSelectY()));
			} else if (bean.getSelectType().equals("rectangle")) {// 矩形查询
				Double left = new Double(bean.getSelectLeft());
				Double top = new Double(bean.getSelectTop());
				Double right = new Double(bean.getSelectRight());
				Double down = new Double(bean.getSelectDown());
				return new GISMapSearchCommand(map, left, top, right, down);
			} else if (bean.getSelectType().equals("pk")) {// 主键查询
				return new GISMapSearchCommand(map, bean.getSelectId().split(
						","));
			} else if (bean.getSelectType().equals("region")) {// 多边形查询
				List<Double[]> points = new ArrayList<Double[]>();
				List<String[]> sPoints = bean.getSelectRegionPoints();
				for (int i = 0; i < sPoints.size(); i++) {
					String[] sPoint = sPoints.get(i);
					double x = (new Double(sPoint[0])).doubleValue();
					double y = (new Double(sPoint[1])).doubleValue();
					points.add(new Double[] { x, y });
				}
				return new GISMapSearchCommand(map, points);
			} else if (bean.getSelectType().equals("circle")) {// 圆范围查询
				double x = new Double(bean.getSelectX()).doubleValue();
				double y = new Double(bean.getSelectY()).doubleValue();
				double radius = new Double(bean.getSelectRadius())
						.doubleValue();
				return new GISMapSearchCommand(map, radius, x, y);
			} else {
				log.error("不支持的选择查询类型:");
				log.error(bean.getZoomType());
				return null;
			}
		}
		return command;
	}

}
