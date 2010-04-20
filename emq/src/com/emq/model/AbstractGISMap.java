package com.emq.model;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.emq.config.MapDef;
import com.emq.config.SystemConfig;
import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.dp.Attribute;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.PointGeometry;
import com.mapinfo.dp.PrimaryKey;
import com.mapinfo.dp.QueryParams;
import com.mapinfo.dp.TableInfo;
import com.mapinfo.graphics.Rendition;
import com.mapinfo.graphics.RenditionImpl;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.mapj.MissingPrimaryKeyException;
import com.mapinfo.mapj.Selection;
import com.mapinfo.mapxtreme.client.MapXtremeImageRenderer;
import com.mapinfo.theme.OverrideTheme;
import com.mapinfo.theme.SelectionTheme;
import com.mapinfo.theme.Theme;
import com.mapinfo.unit.Distance;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;
import com.mapinfo.xmlprot.mxtj.Rendering;

/**
 * 抽象的地图容器模型
 * <p>
 * AbstractGISMap把主地图和缩略地图封装在一起，提供一个绑定主图、缩略图的地图容器。
 * <P>
 * AbstractGISMap提供处理地图的通用方法，与具体地图相关的部分留给子类实现。
 * <p>
 * AbstractGISMap关注与具体图层无关的操作如地图中心点、缩放级别、加载，高亮的模板方法， 而具体需要
 * 查询高亮的图层、属性、高亮的方式等需要由子类来指定。
 * <P>
 * 警告：对地图的中心点操作时必须使用centerto方法，centerto方法决定是否让缩略图中心点与主地图同步。
 * 
 * @author guqiong
 * @created 2009-9-21
 * @history 2009-10-14 guqiong 增加半径范围查询方法 主地图、与缩略图中心点同步 2009-10-15 guqiong fix
 *          bug: KMGIS-20 增加clean方法，并在reset方法中进行调用 2009-10-19 guqiong
 *          增加支持多值主键的查询方法 增加重置地图默认中心点命令 2009-10-22 guqiong
 *          将所有查询方法移至子类SearchableGISMap
 *          2009-12-25 liuyt增加createThemeBySelect方法进行选择查询，延用原来的createTheme，同时修改createTheme用与查询的主题图创建，
 *          该方法可以自动调整缩放比例
 */
public abstract class AbstractGISMap {
	private static Logger log = Logger.getLogger(AbstractGISMap.class);

	/**
	 * 地图容器id = 地图引用id
	 */
	private String id;
	/**
	 * 绑定的主地图
	 */
	protected MapJ bindedMap = null;
	/**
	 * 绑定的缩略地图
	 */
	protected MapJ bindedPreviewMap = null;
	/**
	 * 地图是否已加载
	 */
	private boolean isMapLoaded = false;
	/**
	 * 初始主地图中心,地理坐标系
	 */
	private DoublePoint initMainMapCenter = null;
	/**
	 * 主地图输出图片宽度
	 */
	private Integer mainImgWidth = null;
	/**
	 * 主地图输出图片高度
	 */
	private Integer mainImgHight = null;
	/**
	 * 缩略地图输出图片宽度
	 */
	private Integer previewImgWidth = null;
	/**
	 * 缩略地图输出图片高度
	 */
	private Integer previewImgHight = null;
	/**
	 * 地图定义
	 */
	private MapDef mapDef = null;

	/**
	 * 是否有效的地图缩放等级
	 * 
	 * @param zoom
	 *            地图缩放等级
	 * @return
	 */
	protected abstract boolean isValidZoom(Double zoom);
	
	/**
	 * 根据缩放级别控制是否显示点
	 * 
	 * @param zoom
	 *            地图缩放等级
	 * @throws GISException
	 */
	public abstract void chageLayerTheme(Double zoom) throws GISException;

	/**
	 * 获取高亮的样式
	 * 
	 * @return
	 */
	protected abstract Rendition getHighlightRend();
	/**
	 * 获取二次高亮的样式（在已高亮的结果中再次选择单个结果）
	 * 
	 * @return
	 */
	protected abstract Rendition getSecondHighlightRend();

	/**
	 * 允许显示图元标注
	 */
	protected abstract void setAutoLabelOn(boolean labelEnabled);

	/**
	 * 获取缩略地图输出图片宽度
	 * 
	 * @return
	 */
	public Integer getPreviewImgWidth() {
		return previewImgWidth;
	}

	/**
	 * 设置缩略地图输出图片宽度
	 * 
	 * @param previewImgWidth
	 */
	public void setPreviewImgWidth(Integer previewImgWidth) {
		this.previewImgWidth = previewImgWidth;
	}

	/**
	 * 获取缩略地图输出图片高度
	 * 
	 * @return
	 */
	public Integer getPreviewImgHight() {
		return previewImgHight;
	}

	/**
	 * 设置缩略地图输出图片高度
	 * 
	 * @param previewImgHight
	 */
	public void setPreviewImgHight(Integer previewImgHight) {
		this.previewImgHight = previewImgHight;
	}

	/**
	 * 获取主地图输出图片宽度
	 * 
	 * @return
	 */
	public Integer getMainImgWidth() {
		return mainImgWidth;
	}

	/**
	 * 设置主地图输出图片宽度
	 * 
	 * @param mainImgWidth
	 */
	public void setMainImgWidth(Integer mainImgWidth) {
		this.mainImgWidth = mainImgWidth;
	}

	/**
	 * 获取主地图输出图片高度
	 * 
	 * @return
	 */
	public Integer getMainImgHight() {
		return mainImgHight;
	}

	/**
	 * 设置主地图输出图片高度
	 * 
	 * @param mainImgHight
	 */
	public void setMainImgHight(Integer mainImgHight) {
		this.mainImgHight = mainImgHight;
	}

	/**
	 * 初始主地图中心，地图坐标系
	 * 
	 * @return DoublePoint
	 */
	public DoublePoint getInitMainMapCenter() {
		return (DoublePoint) initMainMapCenter.clone();
	}

	public AbstractGISMap(MapDef def) {
		setMapDef(def);
	}

	public AbstractGISMap() {

	}

	/**
	 * 获取地图的配置
	 * 
	 * @return
	 */
	public MapDef getMapDef() {
		return mapDef;
	}

	/**
	 * 设置地图的配置
	 * 
	 * @param mapDef
	 *            地图定义
	 */
	public void setMapDef(MapDef mapDef) {
		this.mapDef = mapDef;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取地图屏幕范围
	 * 
	 * @return
	 */
	public DoubleRect getDeviceBounds() {
		return (DoubleRect) bindedMap.getDeviceBounds().clone();
	}

	/**
	 * 获取地图的屏幕中心坐标
	 * 
	 * @return
	 * @throws GISException
	 */
	public DoublePoint getScreenCenter() throws GISException {
		try {
			return bindedMap.transformNumericToScreen((DoublePoint) bindedMap
					.getCenter().clone());
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 设置地图的屏幕中心坐标
	 * 
	 * @param screenPoint
	 *            屏幕中心坐标
	 * @throws GISException
	 */
	public void setScreenCenter(DoublePoint screenPoint) throws GISException {
		try {
			this.centerTo(bindedMap.transformScreenToNumeric(screenPoint));
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 移动地图中心点，主地图与缩略图同步
	 * 
	 * @param gisPoint
	 *            点坐标，地图坐标系
	 * @throws GISException
	 */
	public void centerTo(DoublePoint gisPoint) throws GISException {
		try {
			this.bindedMap.setCenter((DoublePoint) gisPoint.clone());
			this.bindedPreviewMap.setCenter((DoublePoint) gisPoint.clone());
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 设置地图缩放等级
	 * 
	 * @return
	 * @throws GISException
	 */
	public double getZoom() throws GISException {
		try {
			return bindedMap.getZoom();
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 设置地图缩放等级
	 * 
	 * @param zoom
	 *            地图缩放等级
	 * @throws GISException
	 */
	public void setZoom(Double zoom) throws GISException {
		try {
			if (isValidZoom(zoom)) {
				bindedMap.setZoom(zoom.doubleValue());
				this.bindedPreviewMap.setZoom(zoom.doubleValue() * 2);
			}
			chageLayerTheme(zoom);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}
	
	

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 渲染地图，输出到流
	 * 
	 * @param map
	 *            需要渲染的地图
	 * @param outStream
	 *            输出的流
	 * @param imageWidth
	 *            输出的图片宽度
	 * @param imageHight
	 *            输出的图片高度
	 * @throws GISException
	 */
	private void render(MapJ map, OutputStream outStream, Integer imageWidth,
			Integer imageHight) throws GISException {
		map.setDeviceBounds(new DoubleRect(0, 0, imageWidth.doubleValue(),
				imageHight.doubleValue()));
		MapXtremeImageRenderer rr = new MapXtremeImageRenderer(SystemConfig
				.getInstance().getMxtURL());
		try {
			ImageRequestComposer imgRequest = ImageRequestComposer.create(map,
					256, Color.white, SystemConfig.getInstance().getMapMime());
			imgRequest.setRendering(Rendering.SPEED);
			rr.render(imgRequest);
			rr.toStream(outStream);
		} catch (Exception e) {
			log.error("渲染地图失败", e);
			log.error("渲染地图服务器: ");
			log.error(SystemConfig.getInstance().getMxtURL());
			throw new GISException(e);
		}
		rr.dispose();
	}

	/**
	 * 清除地图初始化后产生的对象
	 * 
	 * @throws GISException
	 */
	protected void clean() throws GISException {

	}

	/**
	 * 回到初始状态
	 * 
	 * @throws GISException
	 */
	public void reset() throws GISException {
		try {
			this.clean();
			initMapStates(mapDef);
			setAutoLabelOn(true);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 根据配置参数初始化地图状态
	 * 
	 * @param mapDef
	 *            地图配置
	 * @throws GISException
	 */
	protected void initMapStates(MapDef mapDef) throws GISException {
		try {
			bindedMap.setDistanceUnits(LinearUnit.kilometer);
			bindedPreviewMap.setDistanceUnits(LinearUnit.kilometer);
			bindedMap.setZoom(mapDef.getMainMapZoom().doubleValue());
			bindedPreviewMap.setZoom(mapDef.getPreviewMapZoom().doubleValue());
			this.centerTo((DoublePoint) initMainMapCenter.clone());
			this.setMainImgHight(mapDef.getMainMapImgHight());
			this.setMainImgWidth(mapDef.getMainMapImgWidth());
			this.setPreviewImgHight(mapDef.getPreviewMapImgHight());
			this.setPreviewImgWidth(mapDef.getPreviewMapImgWidth());
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 重载地图
	 * 
	 * @param mapDef
	 * @throws GISException
	 */
	public synchronized void reloadMap(MapDef mapDef) throws GISException {
		this.setMapDef(mapDef);
		isMapLoaded = false;
		loadMap();
	}

	/**
	 * 加载地图,初始化地图参数
	 * 
	 * @param mapDef
	 *            地图定义文件名(全路径)
	 * @throws GISException
	 */
	public synchronized void loadMap() throws GISException {
		if (!isMapLoaded) {
			try {
				this.bindedMap = new MapJ();
				this.bindedPreviewMap = new MapJ();

				this.bindedMap.loadMapDefinition(mapDef.getMainMapDefFile());
				this.bindedPreviewMap.loadMapDefinition(mapDef
						.getPreviewMapDefFile());

				loadCenter();

				this.initMapStates(mapDef);
				isMapLoaded = true;

			} catch (IOException e) {
				log.error("读取地图定义文件失败：");
				log.error(mapDef.getMainMapDefFile());
				log.error(mapDef.getPreviewMapDefFile());
				throw new GISException(e);
			} catch (Exception e) {
				log.error("地图初始化失败：");
				log.error(mapDef.getMainMapDefFile());
				log.error(mapDef.getPreviewMapDefFile());
				throw new GISException(e);
			}
		}
	}

	/**
	 * 加载地图中心点
	 * 
	 * @throws Exception
	 */
	private void loadCenter() throws Exception {
		// 如果配置指定了中心点就使用，否则使用地图默认值
		if (mapDef.getX() != null && mapDef.getY() != null) {
			initMainMapCenter = new DoublePoint(mapDef.getX(), mapDef.getY());

		} else {
			initMainMapCenter = (DoublePoint) bindedMap.getCenter().clone();
		}
		this.bindedMap.setCenter((DoublePoint) initMainMapCenter.clone());
		this.bindedPreviewMap
				.setCenter((DoublePoint) initMainMapCenter.clone());
	}

	/**
	 * 渲染主地图，输出到流
	 * 
	 * @param outStream
	 *            图片流
	 * @param imageWidth
	 *            输出图片宽度
	 * @param imageHight
	 *            输出图片高度
	 * @throws GISException
	 */
	public void renderMainMap(OutputStream outStream, Integer imageWidth,
			Integer imageHight) throws GISException {
		if (imageWidth == null)
			imageWidth = this.getMainImgWidth();
		this.setMainImgWidth(imageWidth);

		if (imageHight == null)
			imageHight = this.getMainImgHight();
		this.setMainImgHight(imageHight);

		render(this.bindedMap, outStream, imageWidth, imageHight);
	}

	/**
	 * 渲染预览地图，输出到流
	 * 
	 * @param outStream
	 *            图片流
	 * @param imageWidth
	 *            输出图片宽度
	 * @param imageHight
	 *            输出图片高度
	 * @throws GISException
	 */
	public void renderPreviewMap(OutputStream outStream, Integer imageWidth,
			Integer imageHight) throws GISException {
		if (imageWidth == null)
			imageWidth = this.getPreviewImgWidth();
		this.setPreviewImgWidth(imageWidth);

		if (imageHight == null)
			imageHight = this.getPreviewImgHight();
		this.setPreviewImgHight(imageHight);
		render(this.bindedPreviewMap, outStream, imageWidth, imageHight);
	}

	/**
	 * 刷新地图
	 * 
	 * @throws Exception
	 */
	public void refresh() throws GISException {
		try {

		} catch (Exception e) {
			log.error("map zoom: ");
			log.error(this.getZoom());
			log.error("map screenCenter: ");
			log.error(this.getScreenCenter());
			throw new GISException(ErrorMsgConstants.KMGIS_MAP_01, e);
		}
	}

	/**
	 * 将屏幕坐标转换成地图的坐标
	 * 
	 * @param map
	 * @param rectangle
	 *            屏幕矩形范围坐标
	 * @return DoubleRect 矩形范围坐标，地图坐标系
	 * @throws GISException
	 */
	protected DoubleRect convert2MapPosition(MapJ map, DoubleRect rectangle)
			throws GISException {
		try {
			return map.transformScreenToNumeric(rectangle);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 将地图的屏幕坐标转换成地图坐标
	 * 
	 * @param map
	 *            MapJ
	 * @param point
	 *            屏幕点坐标
	 * @return DoublePoint 点坐标，地图坐标系
	 * @throws GISException
	 */
	protected DoublePoint convert2MapPosition(MapJ map, DoublePoint point)
			throws GISException {
		try {
			return map.transformScreenToNumeric(point);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 将主地图的屏幕坐标转换成地图坐标
	 * 
	 * @param point
	 *            屏幕坐标点
	 * @return 点坐标，地图坐标系
	 * @throws Exception
	 */
	public DoublePoint convert2MainMapPosition(DoublePoint point)
			throws GISException {
		return this.convert2MapPosition(this.bindedMap, point);
	}

	/**
	 * 将缩略地图的屏幕坐标转换成地图坐标
	 * 
	 * @param point屏幕坐标点
	 * @return 点坐标，地图坐标系
	 * @throws Exception
	 */
	public DoublePoint convert2PreviewMapPosition(DoublePoint point)
			throws GISException {
		return this.convert2MapPosition(this.bindedPreviewMap, point);
	}

	/**
	 * 主地图的地理坐标转换成屏幕坐标
	 * 
	 * @param gisPoint
	 *            地理坐标点
	 * @return
	 * @throws Exception
	 */
	public DoublePoint convert2MainMapScreen(DoublePoint gisPoint)
			throws GISException {
		try {
			return this.bindedMap.transformNumericToScreen(gisPoint);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 缩略地图的地理坐标转换成屏幕坐标
	 * 
	 * @param gisPoint
	 *            地理坐标点
	 * @return
	 * @throws Exception
	 */
	public DoublePoint convert2PreviewMapScreen(DoublePoint gisPoint)
			throws GISException {
		try {
			return this.bindedPreviewMap.transformNumericToScreen(gisPoint);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 定位地图中心点至图元所在的位置
	 * 
	 * @param feature
	 *            图元
	 */
	protected void locationBy(Feature feature) throws GISException {
		try {
			DoublePoint point = feature.getGeometry().getBounds().center();
			this.centerTo(point);
		} catch (Exception e) {
			throw new GISException(e);
		}

	}

	/**
	 * 创建主题图(选择查询用)
	 * 
	 * @param themeName
	 *            主题名称
	 * @param featureSet
	 *            图元集合
	 * @param layer
	 *            图层
	 * @throws GISException
	 */
	protected void createThemeBySelect(String themeName, FeatureSet featureSet,
			FeatureLayer layer,Rendition rendition) throws GISException {
		try {

			Double zoom = this.bindedMap.getDistanceUnits().convert(
					layer.getMaxZoom().getScalarValue(),
					layer.getMaxZoom().getLinearUnit());
			// 如果超出可见范围
			if (this.bindedMap.getZoom() > zoom)
				this.setZoom(new Double(zoom.intValue()));

			featureSet.rewind();
			layer.setSelectable(true);
			layer.setEnabled(true);
			// 创建主题图
			//System.out.println(featureSet.getTableInfo().getColumnType(featureSet.getTableInfo().getColumnIndex("ID")));
			SelectionTheme selectTheme = new SelectionTheme(themeName);
			Selection selection = new Selection(); 
			selection.add(featureSet);

			selectTheme.setSelection(selection);
			selectTheme.setRendition(rendition);

			int themeCounts = layer.getThemeList().size();
			// 先删除后加入主题图到层
			if (themeCounts > 0) {
				removeTheme(layer, themeName);
				layer.getThemeList().insert(selectTheme, 0);
			} else
				layer.getThemeList().add(selectTheme);
		} catch (MissingPrimaryKeyException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}
	
	/**
	 * 创建主题图
	 * 
	 * @param themeName
	 *            主题名称
	 * @param featureSet
	 *            图元集合
	 * @param layer
	 *            图层
	 * @throws GISException
	 */
	protected double createTheme(String themeName, FeatureSet featureSet,
			FeatureLayer layer,Rendition rendition) throws GISException {
		try {

//			计算一个可见范围
			featureSet.rewind();
			Feature f = null;
			double max_x = 0;
			double max_y = 0;
			double min_x = 1000;
			double min_y = 1000;
			do{
				f = featureSet.getNextFeature(); 
				if(f==null)
					break;
				PointGeometry pGeometry = (PointGeometry) f.getGeometry();
				DoublePoint gisPoint = pGeometry.getPoint(null);
				double x = gisPoint.x;
				double y = gisPoint.y;
				if((x>102.1 && x<103.7 && y>24.3 && y<26.6)||(y>102.1 && y<103.7 && x>24.3 && x<26.6)){
//					计算
					if(x>max_x){
						max_x = x;
					}
					if(y>max_y){
						max_y = y;
					}
					if(x<min_x){
						min_x = x;
					}
					if(y<min_y){
						min_y = y;
					}
				}
			}while(true);
			featureSet.rewind(); 
			
			//计算最大两点的距离，也就是可视范围 
			double distance;	
			DoublePoint p1 = new DoublePoint(min_x, min_y);
			DoublePoint p2 = new DoublePoint(max_x, max_y);
			distance = this.distance(p1, p2); 

			if (distance < 2.5) {
			  distance = 2.5;
			}
			// 改变地图可视范围
			double newZoom = distance;
			this.setZoom(newZoom);
			// 同步中心点
			double center_x = (max_x - min_x) / 2 + min_x;
			double center_y = (max_y - min_y) / 2 + min_y;
			DoublePoint center = new DoublePoint(center_x, center_y);
			this.bindedMap.setCenter(center);
			// 同步图层可视范围
			if (distance < 5) {
				distance = 5;
			}
			layer.setMaxZoom(new Distance(distance * 1000 + 0.1,layer.getMaxZoom().getLinearUnit()));
			featureSet.rewind();
			layer.setSelectable(true);
			layer.setEnabled(true);
			// 创建主题图
			SelectionTheme selectTheme = new SelectionTheme(themeName);
			Selection selection = new Selection(); 
			selection.add(featureSet);

			selectTheme.setSelection(selection);
			selectTheme.setRendition(rendition);

			int themeCounts = layer.getThemeList().size();
			// 先删除后加入主题图到层
			if (themeCounts > 0) {
				removeTheme(layer, themeName);
				layer.getThemeList().insert(selectTheme, 0);
			} else
				layer.getThemeList().add(selectTheme);
			return distance;
		} catch (MissingPrimaryKeyException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 根据缩放倍数获取POI渲染
	 * @param layer
	 * @param distance
	 */
	public void getPoiLayer(FeatureLayer layer,double distance){
		Iterator themes = layer.getThemeList().iterator();
		while (themes.hasNext()) {
			Theme theme = (Theme) themes.next();
			if (theme instanceof OverrideTheme){
				OverrideTheme myOTheme = new OverrideTheme(getPoiRend(distance), theme.getName());
				themes.remove();
				layer.getThemeList().add(myOTheme);
				break;
			}
				
		}
	}
	
	/**
	 * 根据缩放倍数获取未选中零售户样式
	 */
	protected Rendition getPoiRend(double distance) {
		Rendition rend = new RenditionImpl(); 
		rend.setValue(Rendition.FONT_FAMILY, "MapInfo Symbols"); 
		rend.setValue(Rendition.FONT_SIZE, 12);  
		rend.setValue(Rendition.SYMBOL_MODE, Rendition.SymbolMode.FONT); 
		rend.setValue(Rendition.SYMBOL_STRING, "8"); 
		rend.setValue(Rendition.SYMBOL_FOREGROUND, new Color(99,101,255));
		if(distance>5.1){
			rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,0.0);
			rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,0.0);
		}else{
			rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,1.0);
			rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,1.0);
		}
	    return rend;
	}
	
	/**
	 * 根据缩放倍数获取未选中无证户样式
	 */
	protected Rendition getNoLincesePoiRend(double distance) {
		Rendition rend = new RenditionImpl(); 
		rend.setValue(Rendition.FONT_FAMILY, "MapInfo Symbols"); 
		rend.setValue(Rendition.FONT_SIZE, 12);  
		rend.setValue(Rendition.SYMBOL_MODE, Rendition.SymbolMode.FONT); 
		rend.setValue(Rendition.SYMBOL_STRING, "!"); 
		rend.setValue(Rendition.SYMBOL_FOREGROUND, new Color(99,101,255));
		if(distance>5.1){
			rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,0.0);
			rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,0.0);
		}else{
			rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,1.0);
			rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,1.0);
		}
	    return rend;
	}
	
	/**
	 * 删除主题图
	 * 
	 * @param layer
	 *            图层
	 * @param themeName
	 *            主题名
	 */
	public void removeTheme(FeatureLayer layer, String themeName) {
		Iterator themes = layer.getThemeList().iterator();
		while (themes.hasNext()) {
			Theme theme = (Theme) themes.next();
			if (theme instanceof SelectionTheme) {
				if (theme.getName().equals(themeName))
					themes.remove();
			}
		}
	}

	/**
	 * 获取图层主键
	 * 
	 * @param layer
	 * @return PrimaryKey
	 * @throws GISException
	 */
	protected PrimaryKey getPk(FeatureLayer layer) throws GISException {
		try {
			TableInfo tableInfo = layer.getTableInfo();
			int[] pkColumnIndexes = tableInfo.getPrimaryKeyInfo();
			Attribute[] pk = new Attribute[pkColumnIndexes.length];
			for (int i = 0; i < pkColumnIndexes.length; i++) {
				String columnName = tableInfo.getColumnName(i);
				Attribute attrPk = new Attribute(columnName);
				pk[i] = attrPk;
			}
			return new PrimaryKey(pk);
		} catch (Exception e) {
			log.error("读取图层主键失败：");
			throw new GISException(e);
		}
	}

	/**
	 * 获取图层所有的属性列
	 * 
	 * @param layer
	 *            图层
	 * @return List 列名list
	 * @throws Exception
	 */
	protected List<String> getAllColumns(FeatureLayer layer) throws Exception {
		TableInfo tableInfo = layer.getTableInfo();
		List<String> columns = new ArrayList<String>();
		for (int i = 0; i < tableInfo.getColumnCount(); i++)
			columns.add(tableInfo.getColumnName(i));
		return columns;
	}

	/**
	 * 重置地图中心点到默认位置
	 * 
	 * @throws GISException
	 */
	public void resetCenter() throws GISException {
		this.centerTo(this.initMainMapCenter);
	}

	/**
	 * 重置地图缩放级别
	 * 
	 * @throws GISException
	 */
	public void resetZoom() throws GISException {
		this.setZoom(this.mapDef.getMainMapZoom());
	}

	/**
	 * 计算两点间距离
	 * 
	 * @param gisPoint0
	 *            gis坐标点
	 * @param gisPoint1
	 *            gis坐标点
	 * @return
	 */
	public double distance(DoublePoint gisPoint0, DoublePoint gisPoint1) {
		return this.bindedMap.sphericalDistance(gisPoint0, gisPoint1);
	}

	/**
	 * 判断点是否在指定的区域内
	 * 
	 * @param gisPoint
	 *            gis坐标点
	 * @param areaLayerName
	 *            区域图层名称
	 * @param areaColumn
	 *            区域名称列
	 * @param areaValue
	 *            指定的区域名称
	 * @return
	 * @throws GISException
	 */
	public boolean within(DoublePoint gisPoint, String areaLayerName,
			String areaColumn, String areaValue) throws GISException {
		try {
			FeatureLayer layer = (FeatureLayer) this.bindedMap.getLayers().get(
					areaLayerName);
			List<String> columns = getAllColumns(layer);
			TableInfo tableInfo = layer.getTableInfo();
			int columnIndex = tableInfo.getColumnIndex(areaColumn);
			int columnType = tableInfo.getColumnType(columnIndex);
			FeatureSet withinFeatures = layer.searchAtPoint(columns, gisPoint,
					QueryParams.ALL_PARAMS);
			Feature f = null;
			do {
				f = withinFeatures.getNextFeature();
				if (f == null)
					break;

				String findAreaId;
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMaximumFractionDigits(0);
				switch (columnType) {
				case TableInfo.COLUMN_TYPE_FLOAT:
					findAreaId = nf.format(f.getAttribute(columnIndex)
							.getFloat());
				case TableInfo.COLUMN_TYPE_DOUBLE:
				case TableInfo.COLUMN_TYPE_DECIMAL:
					findAreaId = nf.format(f.getAttribute(columnIndex)
							.getDouble());
					break;
				default:
					findAreaId = f.getAttribute(columnIndex).getString();

				}
				if (areaValue.equals(findAreaId))
					return true;
			} while (true);

		} catch (Exception e) {
			throw new GISException(e);
		}
		return false;
	}

	/**
	 * 是否正确的坐标图元
	 * 
	 * @param f
	 *            点图元
	 * @throws GISException
	 */
	protected abstract boolean isValidFeature(Feature f, TableInfo tableInfo)
			throws GISException;
}
