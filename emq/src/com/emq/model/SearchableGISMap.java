package com.emq.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.emq.config.LayerTemplateDef;
import com.emq.config.MapDef;
import com.emq.config.SpatialSearchLayerDefine;
import com.emq.config.StaticLayerDefine;
import com.emq.config.SystemConfig;
import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.coordsys.CoordSys;
import com.mapinfo.dp.AttOperator;
import com.mapinfo.dp.AttTuple;
import com.mapinfo.dp.Attribute;
import com.mapinfo.dp.DataProviderHelper;
import com.mapinfo.dp.DataProviderRef;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.PassThroughQueryException;
import com.mapinfo.dp.PrimaryKey;
import com.mapinfo.dp.QueryParams;
import com.mapinfo.dp.RenditionType;
import com.mapinfo.dp.SearchType;
import com.mapinfo.dp.TableDescHelper;
import com.mapinfo.dp.jdbc.QueryBuilder;
import com.mapinfo.dp.jdbc.xy.XYDataProviderHelper;
import com.mapinfo.dp.jdbc.xy.XYTableDescHelper;
import com.mapinfo.dp.util.RewindableFeatureSet;
import com.mapinfo.labeltheme.LabelTheme;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.mapxtreme.client.MapXtremeDataProviderRef;
import com.mapinfo.theme.OverrideTheme;
import com.mapinfo.theme.Theme;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.util.VisibilityConstraints;

/**
 * 支持查询的地图容器，提供对地图查询的多种方法。
 * 
 * @author guqiong
 * @created 2009-10-22
 */
public abstract class SearchableGISMap extends AbstractGISMap {
	private static Logger log = Logger.getLogger(SearchableGISMap.class);

	/**
	 * 用于空间条件查询，显示
	 */
	List spatialSearchLayer = null;
	//protected FeatureLayer spatialSearchLayer = null;
	/**
	 * 用于组合条件查询，隐藏
	 */
	List conditionSearchLayer = null;
	//protected FeatureLayer conditionSearchLayer = null;
	/**
	 * 查询图层是否已加载
	 */
	private boolean layerLoaded = false;
	/**
	 * 空间查询图层名称
	 */
	private final String SPATIAL_SEARCH_LAYER_NAME = "SPATIAL_SEARCH_POI";
	/**
	 * 组合条件查询查询图层名称
	 */
	private final String CONDITION_SEARCH_LAYER_NAME = "CONDITION_SEARCH_POI";
	/**
	 * 查询结果主题名称
	 */
	private final String SEARCH_THEME_NAME = "SEARCH_THEME";	
	/**
	 * 主键选择主题名称
	 */
	private final String PK_SELELCT_THEME_NAME = "PK_SELECT_THEME";

	/**
	 * 构造地图容器
	 * 
	 * @param def
	 *            地图定义
	 */
	public SearchableGISMap(MapDef def) {
		super(def);
	}

	public SearchableGISMap() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMap#loadMap()
	 */
	public synchronized void loadMap() throws GISException {
		super.loadMap();
		if (!this.layerLoaded) {
			try {
				conditionSearchLayer = this.creatSqlLayer(CONDITION_SEARCH_LAYER_NAME);
				spatialSearchLayer = this.createTableLayer(SPATIAL_SEARCH_LAYER_NAME);
				createStaticLayers();
				copyTemlateTheme();	
				layerLoaded = true;
			} catch (Exception e) {
				log.error("加载查询图层失败：");
				throw new GISException(e);
			}
		}else{
			try {
				FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().get(CONDITION_SEARCH_LAYER_NAME);
				if(layer == null){
					conditionSearchLayer = this.creatSqlLayer(CONDITION_SEARCH_LAYER_NAME); 
				}
				layer = (FeatureLayer) bindedMap.getLayers().get(SPATIAL_SEARCH_LAYER_NAME);
				if(layer == null){
					spatialSearchLayer = this.createTableLayer(SPATIAL_SEARCH_LAYER_NAME);
				}
				copyTemlateTheme();	
				createStaticLayers();
			}catch(Exception e){
				log.error("重新加载查询图层失败：");
				throw new GISException(e);
			}
		}
	}

	/**
	 * 拷贝模板的样式
	 * @throws IOException
	 * @throws Exception
	 */
	private void copyTemlateTheme() throws IOException, Exception {
		List spatialSearchLayerDefineList = this.getMapDef().getSpatialSearchLayerDefineList();
		for(int i=0;i<spatialSearchLayerDefineList.size();i++){
			SpatialSearchLayerDefine def = (SpatialSearchLayerDefine)spatialSearchLayerDefineList.get(i);
			if(def != null){
				MapJ template = new MapJ();
				LayerTemplateDef layerTemplateDef = SystemConfig.getInstance().getMapDefines().getLayerTemplateDef(def.getTemplate());
				template.loadMapDefinition(layerTemplateDef.getDefineFile());
				FeatureLayer templateLayer = (FeatureLayer) template.getLayers().get(def.getTemplate());
				copyTheme((FeatureLayer)this.spatialSearchLayer.get(i), templateLayer);
				copyTheme((FeatureLayer)this.conditionSearchLayer.get(i), templateLayer);
				copyVisibility((FeatureLayer)this.spatialSearchLayer.get(i), templateLayer);
			}
		}
//		SpatialSearchLayerDefine def = this.getMapDef().getSpatialSearchLayerDefine();
//		if(def != null){
//			MapJ template = new MapJ();
//			LayerTemplateDef layerTemplateDef = SystemConfig.getInstance().getMapDefines().getLayerTemplateDef(def.getTemplate());
//			template.loadMapDefinition(layerTemplateDef.getDefineFile());
//			FeatureLayer templateLayer = (FeatureLayer) template.getLayers().get(def.getTemplate());
//		 	copyTheme(this.spatialSearchLayer, templateLayer);
//		 	copyTheme(this.conditionSearchLayer, templateLayer); 
//		 	copyVisibility(this.spatialSearchLayer, templateLayer);
//		}
	}
	/**
	 * 拷贝模板的可视性
	 * @param dest 目标图层
	 * @param templateLayer 模板图层
	 * @throws Exception
	 */
	private void copyVisibility(FeatureLayer dest, FeatureLayer templateLayer)throws Exception{
		dest.setVisibilityConstraints(templateLayer.getVisibilityConstraints());
	}
	
	
	/**
	 * 拷贝模板的样式
	 * @param dest 目标图层
	 * @param templateLayer 模板图层
	 * @throws Exception
	 */
	private void copyTheme(FeatureLayer dest, FeatureLayer templateLayer) throws Exception {
		// 标注样式
		Iterator labelThemes = templateLayer.getLabelThemeList().iterator();
		dest.getLabelThemeList().removeAll(); 
		while (labelThemes.hasNext()) {
			dest.getLabelThemeList().add(
					(LabelTheme) labelThemes.next()); 
		}
		// 图元样式
		Iterator themes = templateLayer.getThemeList().iterator();
		dest.getThemeList().removeAll(); 
		while (themes.hasNext()) {
			Theme theme = (Theme) themes.next();
			if (theme instanceof OverrideTheme) {
				dest.getThemeList().add(theme); 
			}
		} 
	}

	/**
	 * 获取需要进行查询的图层,用于点、矩形、多边形选择等空间查询
	 * 
	 * @return
	 */
	protected List getSpatialSearchLayer() {
		return this.spatialSearchLayer;
	}

	/**
	 * 获取需要进行查询的图层,用于使用QueryBuilder的查询
	 * 
	 * @return
	 */
	protected List getQuerySearchLayer() {
		return this.conditionSearchLayer;
	}

	/**
	 * 获取需要查询的图层主键名称,进支持单值主键
	 * 
	 * @deprecated 2009-10-19 图层主键名称将从图层读取;
	 * @return
	 */
	protected String getSpetialSearchLayerPKName() {
		return "XKZHM";
	}

	/**
	 * 对图层进行组合条件查询
	 * 
	 * @param ConditionObject
	 * @return 符合条件的图元集合
	 * @throws GISException
	 */
	public abstract List searchByCondition(Object conditionObject,String tableName)
			throws GISException;

	/**
	 * 清除地图初始化后产生的对象，如查询结果theme
	 * 
	 * @throws GISException
	 */
	protected void clean() throws GISException {
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			super.removeTheme((FeatureLayer)this.getSpatialSearchLayer().get(i), SEARCH_THEME_NAME);
			super.removeTheme((FeatureLayer)this.getSpatialSearchLayer().get(i), PK_SELELCT_THEME_NAME);
		}
	}

	/**
	 * 创建xy坐标的图层,基于表
	 * 
	 * @return
	 */
	protected List createTableLayer(String name) throws Exception {
		List layerList = new ArrayList();
		List spatialSearchLayerDefineList = this.getMapDef().getSpatialSearchLayerDefineList();
		for(int i=0;i<spatialSearchLayerDefineList.size();i++){
			DataProviderHelper dataProviderHelper = getDataProviderHelper();

			SpatialSearchLayerDefine spatialSearchLayerDefine = (SpatialSearchLayerDefine)spatialSearchLayerDefineList.get(i);
			String[] idColumn = spatialSearchLayerDefine.getPk();
			String x = spatialSearchLayerDefine.getX();
			String y = spatialSearchLayerDefine.getY();
			String table = spatialSearchLayerDefine.getTableName();

			TableDescHelper tableDescHelper1 = new XYTableDescHelper(table,
					SystemConfig.getInstance().getProperty("jdbc.owner"), false, x, y, null,
					RenditionType.none, null, RenditionType.none, idColumn,
					CoordSys.longLatWGS84);

			DataProviderRef dataProviderRef = new MapXtremeDataProviderRef(
					dataProviderHelper, SystemConfig.getInstance().getMxtURL());

			FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().insertLayer(
					dataProviderRef, tableDescHelper1, 0, name);
			layerList.add(layer);
		}
		return layerList;
	}
//	protected FeatureLayer createTableLayer(String name) throws Exception {
//		DataProviderHelper dataProviderHelper = getDataProviderHelper();
//
//		SpatialSearchLayerDefine spatialSearchLayerDefine = this.getMapDef()
//				.getSpatialSearchLayerDefine();
//		String[] idColumn = spatialSearchLayerDefine.getPk();
//		String x = spatialSearchLayerDefine.getX();
//		String y = spatialSearchLayerDefine.getY();
//		String table = spatialSearchLayerDefine.getTableName();
//
//		TableDescHelper tableDescHelper1 = new XYTableDescHelper(table,
//				SystemConfig.getInstance().getProperty("jdbc.owner"), false, x, y, null,
//				RenditionType.none, null, RenditionType.none, idColumn,
//				CoordSys.longLatWGS84);
//
//		DataProviderRef dataProviderRef = new MapXtremeDataProviderRef(
//				dataProviderHelper, SystemConfig.getInstance().getMxtURL());
//
//		FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().insertLayer(
//				dataProviderRef, tableDescHelper1, 0, name);
//
//		return layer;
//	}
	
	/**
	 * 创建静态显示图层
	 * @return
	 */
	protected void createStaticLayers() throws Exception {
		Map<String,StaticLayerDefine> staticLayerDefines = this.getMapDef().getStaticLayerDefines();
		if(staticLayerDefines == null)
			return;
		for(StaticLayerDefine def: staticLayerDefines.values()){
			createStaticLayer(def);
		}
	}
	
	/**
	 * 创建xy坐标的静态显示图层,基于表
	 * @param id 图层id
	 * @return
	 */
	protected FeatureLayer createStaticLayer(StaticLayerDefine staticLayerDefine) throws Exception {
		DataProviderHelper dataProviderHelper = getDataProviderHelper();
 
		String[] idColumn = staticLayerDefine.getPk();
		String x = staticLayerDefine.getX();
		String y = staticLayerDefine.getY();
		String table = staticLayerDefine.getTableName();
		String label = staticLayerDefine.getLabel();
		TableDescHelper tableDescHelper1 = new XYTableDescHelper(table,
				SystemConfig.getInstance().getProperty("jdbc.owner"), false, x, y, null,
				RenditionType.none, null, RenditionType.none, idColumn,
				CoordSys.longLatWGS84);

		DataProviderRef dataProviderRef = new MapXtremeDataProviderRef(
				dataProviderHelper, SystemConfig.getInstance().getMxtURL());

		FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().insertLayer(
				dataProviderRef, tableDescHelper1, 2, staticLayerDefine.getId());
		
		LayerTemplateDef layerTemplateDef = SystemConfig.getInstance().getMapDefines().getLayerTemplateDef(staticLayerDefine.getTemplate());
		MapJ template = new MapJ();
		template.loadMapDefinition(layerTemplateDef.getDefineFile());
		FeatureLayer templateLayer = (FeatureLayer) template.getLayers().get(staticLayerDefine.getTemplate());
	 	copyTheme(layer, templateLayer); 
	 	copyVisibility(layer, templateLayer);
	 	layer.getLabelProperties().setLabelExpression(label);
		layer.setAutoLabel(true);
		return layer;
	}

	/**
	 * 创建xy坐标的图层，基于sql查询
	 * 
	 * @return
	 */
	protected List creatSqlLayer(String name) throws Exception {
		List layerList = new ArrayList();
		List spatialSearchLayerDefineList = this.getMapDef().getSpatialSearchLayerDefineList();
		for(int i=0;i<spatialSearchLayerDefineList.size();i++){
			DataProviderHelper dataProviderHelper = getDataProviderHelper();

			SpatialSearchLayerDefine spatialSearchLayerDefine = (SpatialSearchLayerDefine)spatialSearchLayerDefineList.get(i);
			String[] idColumn = spatialSearchLayerDefine.getPk();
			String x = spatialSearchLayerDefine.getX();
			String y = spatialSearchLayerDefine.getY();
			String table = spatialSearchLayerDefine.getTableName(); 
			String sql = "select * from " + table + " where 1=0 ";

			TableDescHelper tableDescHelper2 = new XYTableDescHelper(sql, idColumn,
					x, y, null, RenditionType.none, null, RenditionType.none,
					CoordSys.longLatWGS84);

			DataProviderRef dataProviderRef = new MapXtremeDataProviderRef(
					dataProviderHelper, SystemConfig.getInstance().getMxtURL());

			FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().insertLayer(
					dataProviderRef, tableDescHelper2, 0, name);
			// 隐藏的图层
			VisibilityConstraints visibility = new VisibilityConstraints();
			visibility.setEnabled(false);
			layer.setVisibilityConstraints(visibility);
			layerList.add(layer);
		}
		return layerList;
	}
//	protected FeatureLayer creatSqlLayer(String name) throws Exception {
//		DataProviderHelper dataProviderHelper = getDataProviderHelper();
//
//		SpatialSearchLayerDefine spatialSearchLayerDefine = this.getMapDef()
//				.getSpatialSearchLayerDefine();
//		String[] idColumn = spatialSearchLayerDefine.getPk();
//		String x = spatialSearchLayerDefine.getX();
//		String y = spatialSearchLayerDefine.getY();
//		String table = spatialSearchLayerDefine.getTableName(); 
//		String sql = "select * from " + table + " where 1=0 ";
//
//		TableDescHelper tableDescHelper2 = new XYTableDescHelper(sql, idColumn,
//				x, y, null, RenditionType.none, null, RenditionType.none,
//				CoordSys.longLatWGS84);
//
//		DataProviderRef dataProviderRef = new MapXtremeDataProviderRef(
//				dataProviderHelper, SystemConfig.getInstance().getMxtURL());
//
//		FeatureLayer layer = (FeatureLayer) bindedMap.getLayers().insertLayer(
//				dataProviderRef, tableDescHelper2, 0, name);
//		// 隐藏的图层
//		VisibilityConstraints visibility = new VisibilityConstraints();
//		visibility.setEnabled(false);
//		layer.setVisibilityConstraints(visibility);
//		return layer;
//	}

	/**
	 * 构造DataProviderHelper
	 * @return
	 */
	protected DataProviderHelper getDataProviderHelper() {
	//	String jdbcDriver = SystemConfig.getInstance().getProperty("jdbc.driver");
	//	String jdbcURL = SystemConfig.getInstance().getProperty("jdbc.url");
	//	String userName = SystemConfig.getInstance().getProperty("jdbc.username");
	//	String password = SystemConfig.getInstance().getProperty("jdbc.password");
	//	DataProviderHelper dataProviderHelper = new XYDataProviderHelper(
	//			jdbcDriver, jdbcURL, userName, password);
		DataProviderHelper dataProviderHelper = new XYDataProviderHelper(
				"jdbc:mipool:kmgiss", null, null);
	 
		return dataProviderHelper;
	}

	/**
	 * 矩形范围查询，并高亮显示图元
	 * 
	 * @param rectangle
	 *            矩形范围 屏幕坐标系
	 */
	public void searchAndHighlight(DoubleRect rect) throws GISException {
		FeatureSet featureSet = null;
		try {
			//FeatureLayer layer = this.getSpatialSearchLayer();
			List layer = this.getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = searchByRect((FeatureLayer)layer.get(i), rect);
				removeTheme((FeatureLayer)layer.get(i), PK_SELELCT_THEME_NAME);
				createThemeBySelect(SEARCH_THEME_NAME, featureSet, (FeatureLayer)layer.get(i), getHighlightRend());
			}

		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					featureSet.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 多边形范围查询，并高亮显示图元
	 * 
	 * @param regionPoints
	 *            多边形顶点 屏幕坐标系
	 */
	public void searchAndHighlight(List<DoublePoint> regionPoints)
			throws GISException {
		FeatureSet featureSet = null;
		try {
			List layer = this.getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = this.searchByRegion((FeatureLayer)layer.get(i), regionPoints);
				removeTheme((FeatureLayer)layer.get(i), PK_SELELCT_THEME_NAME);
				createThemeBySelect(SEARCH_THEME_NAME, featureSet, (FeatureLayer)layer.get(i), getHighlightRend());
			}
//			FeatureLayer layer = this.getSpatialSearchLayer();
//			featureSet = this.searchByRegion(layer, regionPoints);
//			removeTheme(layer, PK_SELELCT_THEME_NAME);
//			createThemeBySelect(SEARCH_THEME_NAME, featureSet, layer, getHighlightRend());

		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					featureSet.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 *根据图元主键查询，并高亮显示图元, 地图定位之查询结果点
	 * 
	 * @deprecated 2009-10-19 被searchAndHighlight(String[] pk)替代
	 * @param pk
	 *            图元主键
	 */
	public void searchAndHighlight(String pk) throws GISException {
		String[] arrPk = new String[] { pk };
		searchAndHighlight(arrPk);
	}

	/**
	 *根据图元主键查询，并高亮显示图元, 地图定位之查询结果点
	 * 
	 * @param pk
	 *            图元主键
	 */
	public void searchAndHighlight(String[] pk) throws GISException {
		FeatureSet featureSet = null;
		try {
			List layer = this.getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = this.searchByPk((FeatureLayer)layer.get(i), pk);
				createThemeBySelect(PK_SELELCT_THEME_NAME, featureSet, (FeatureLayer)layer.get(i), this.getSecondHighlightRend());
				featureSet.rewind();
				Feature firstCigStore = featureSet.getNextFeature();
				if (firstCigStore != null) {
					this.locationBy(firstCigStore);
				} else {
					log.error("查询不存在的图层主键: ");
					log.error(pk);
				}
			}
//			FeatureLayer layer = this.getSpatialSearchLayer();
//			featureSet = searchByPk(layer, pk);
//			createThemeBySelect(PK_SELELCT_THEME_NAME, featureSet, layer, this.getSecondHighlightRend());
//			featureSet.rewind();
//			Feature firstCigStore = featureSet.getNextFeature();
//			if (firstCigStore != null) {
//				this.locationBy(firstCigStore);
//			} else {
//				log.error("查询不存在的图层主键: ");
//				log.error(pk);
//			}
		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					featureSet.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 点查询，并高亮显示图元
	 * 
	 * @param point
	 *            点坐标 屏幕坐标系
	 */
	public void searchAndHighlight(DoublePoint p) throws GISException {
		FeatureSet featureSet = null;
		try {
			List layer = this.getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = this.searchByPoint((FeatureLayer)layer.get(i), p);
				removeTheme((FeatureLayer)layer.get(i), PK_SELELCT_THEME_NAME);
				createThemeBySelect(SEARCH_THEME_NAME, featureSet, (FeatureLayer)layer.get(i), getHighlightRend());
			}
//			FeatureLayer layer = getSpatialSearchLayer();
//			featureSet = searchByPoint(layer, p);
//			removeTheme(layer, PK_SELELCT_THEME_NAME);
//			createTheme(SEARCH_THEME_NAME, featureSet, layer, getHighlightRend());
		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					featureSet.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 半径范围查询，并高亮
	 * 
	 * @param p
	 *            圆心点坐标，屏幕坐标系
	 * @param radius
	 *            半径，屏幕坐标系
	 * @throws GISException
	 */
	public void searchAndHighlight(DoublePoint p, double radius)
			throws GISException {
		FeatureSet featureSet = null;
		try {
			List layer = this.getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = this.searchByRadius((FeatureLayer)layer.get(i), p, radius);
				removeTheme((FeatureLayer)layer.get(i), PK_SELELCT_THEME_NAME);
				createThemeBySelect(SEARCH_THEME_NAME, featureSet, (FeatureLayer)layer.get(i), getHighlightRend());
			}
//			FeatureLayer layer = getSpatialSearchLayer();
//			featureSet = this.searchByRadius(layer, p, radius);
//			removeTheme(layer, PK_SELELCT_THEME_NAME);
//			createThemeBySelect(SEARCH_THEME_NAME, featureSet, layer, getHighlightRend());
		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					featureSet.dispose();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 条件查询，并高亮显示图元,同时将地图中心定位至结果的第一个图元
	 * 
	 * @param conditionObject
	 *            条件对象
	 */
	public void searchAndHighlight(Object conditionObject,String tableName) throws GISException {
		List featureSet = null;
		try {
			//更改查询结果高亮所使用的图层
			List layer = getSpatialSearchLayer();
			for(int i=0;i<layer.size();i++){
				featureSet = this.searchByCondition(conditionObject,tableName);
				for(int j=0;j<featureSet.size();j++){
					Feature first = ((FeatureSet)featureSet.get(j)).getNextFeature();
					if (first != null) {
						locationBy(first);
						((FeatureSet)featureSet.get(j)).rewind();
					}
					removeTheme((FeatureLayer)layer.get(i), PK_SELELCT_THEME_NAME);
					double distance = createTheme(SEARCH_THEME_NAME, (FeatureSet)featureSet.get(j), (FeatureLayer)layer.get(i), getHighlightRend());
					getPoiLayer((FeatureLayer)layer.get(i),distance);
				}
				
			}
//			FeatureLayer layer = getSpatialSearchLayer();
//			featureSet = this.searchByCondition(conditionObject);
//			Feature first = featureSet.getNextFeature();
//			if (first != null) {
//				locationBy(first);
//				// 恢复游标到起始位置
//				featureSet.rewind();
//			}
//			removeTheme(layer, PK_SELELCT_THEME_NAME);
//			double distance = createTheme(SEARCH_THEME_NAME, featureSet, layer, getHighlightRend());
//			getPoiLayer(layer,distance);
		} catch (Exception e) {
			log.error("查询图层时出现错误:", e);
			throw new GISException(e);
		} finally {
			if (featureSet != null) {
				try {
					for(int i=0;i<featureSet.size();i++){
						((FeatureSet)featureSet.get(i)).dispose();
					}
					
				} catch (Exception e) {
					log.error(e);
				}
			}

		}
	}
	
	/**
	 * 在图层中查询指定主键值的图元
	 * 
	 * @deprecated 2009-10-19 使用FeatureSet searchByPk(FeatureLayer layer,
	 *             String[] pkColumns, String[] pk)代替
	 * @param layer
	 *            图层
	 * @param pkColumn
	 *            主键列
	 * @param pk
	 *            主键值
	 * @return FeatureSet 图元集合
	 * @throws GISException
	 */
	protected FeatureSet searchByPk(FeatureLayer layer, String pkColumn,
			String pk) throws GISException {
  		return this.searchByPk(layer, new String[]{pk});
 	}

	/**
	 * 在图层中查询指定主键值的图元
	 * 
	 * @param layer
	 *            图层
	 * @param pk
	 *            主键值,位置顺序按照图层定义时的顺序
	 * @return FeatureSet 图元集合
	 * @throws GISException
	 */
	protected FeatureSet searchByPk(FeatureLayer layer, String[] pk)
			throws GISException {
		try {
			List<String> columns = getAllColumns(layer);
			List pkNames = new ArrayList();
			List pkValues = new ArrayList();
			List operators = new ArrayList();
			PrimaryKey layerPk = this.getPk(layer);
			int cols = layerPk.getAttributeCount();
			if (cols != pk.length) {
				throw new GISException(ErrorMsgConstants.EMQ_MAP_05);
			}
			for (int i = 0; i < cols; i++) {
				pkNames.add(layerPk.getAttribute(i).getString());
				pkValues.add(new AttTuple(new Attribute(pk[i])));
				operators.add(AttOperator.eq);
			}
			FeatureSet result = layer.searchByAttributes(columns, pkNames,
					operators, pkValues, QueryParams.ALL_PARAMS);
			return afterSearch(layer, new RewindableFeatureSet(result));
		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}

	}

	/**
	 * 在图层中查询指定点的图元
	 * 
	 * @param layer
	 *            图层
	 * @param point
	 *            指定的点 屏幕坐标系
	 * @return FeatureSet 图元集合
	 * @throws GISException
	 */
	protected FeatureSet searchByPoint(FeatureLayer layer, DoublePoint point)
			throws GISException {
		try {
			List<String> columns = getAllColumns(layer);
			FeatureSet result = layer.searchAtPoint(columns, bindedMap
					.transformScreenToNumeric(point), QueryParams.ALL_PARAMS);
			return afterSearch(layer, new RewindableFeatureSet(result));
		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 查询指定点半径范围内的图元
	 * 
	 * @param point
	 *            指定的点，屏幕坐标系
	 * @param radius
	 *            半径，屏幕坐标系
	 * @return
	 * @throws GISException
	 */
	public List searchByRadius(DoublePoint point, double radius)
			throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(this.searchByRadius((FeatureLayer)this.getSpatialSearchLayer().get(i), point, radius));
		}
		return featureSetList;
	}

	/**
	 * 在图层中查询指定点半径范围内的图元
	 * 
	 * @param layer
	 *            图层
	 * @param point
	 *            指定的点，屏幕坐标系
	 * @param radius
	 *            半径，屏幕坐标系
	 * @return
	 * @throws GISException
	 */
	protected FeatureSet searchByRadius(FeatureLayer layer, DoublePoint point,
			double radius) throws GISException {
		try {
			List<String> columns = getAllColumns(layer);
			// 计算地图坐标下的半径
			DoublePoint tmpPoint = new DoublePoint(point.x + radius, point.y);
			double distance = bindedMap.cartesianDistance(bindedMap
					.transformScreenToNumeric(point), bindedMap
					.transformScreenToNumeric(tmpPoint));

			FeatureSet result = layer.searchWithinRadius(columns, bindedMap
					.transformScreenToNumeric(point), distance, bindedMap
					.getDistanceUnits(), QueryParams.ALL_PARAMS);
			return afterSearch(layer, new RewindableFeatureSet(result));
		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 在图层中查询指定矩形范围内的图元
	 * 
	 * @param layer
	 *            图层
	 * @param rectangle
	 *            矩形范围，屏幕坐标系
	 * @return
	 * @throws GISException
	 */
	protected FeatureSet searchByRect(FeatureLayer layer, DoubleRect rectangle)
			throws GISException {
		try {

			List<String> columns = getAllColumns(layer);

			FeatureSet result = layer.searchWithinRectangle(columns, bindedMap
					.transformScreenToNumeric(rectangle),
					QueryParams.ALL_PARAMS);
			return afterSearch(layer, new RewindableFeatureSet(result));

		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 在图层中查询指定多边形范围内的图元
	 * 
	 * @param layer
	 *            图层
	 * @param regionPoints
	 *            多边形顶点,屏幕坐标系
	 * @return
	 * @throws GISException
	 */
	protected FeatureSet searchByRegion(FeatureLayer layer,
			List<DoublePoint> regionPoints) throws GISException {
		try {

			List<String> columns = getAllColumns(layer);
			// 将参数regionPoints中的点信息置入gisPoint，顺序为x0,y0,x1,y1
			// 注意，不要使用二维数组传给mapx
			double[] gisPoint = new double[regionPoints.size() * 2];
			int index = 0;
			for (int i = 0; i < regionPoints.size(); i++) {
				DoublePoint p = bindedMap.transformScreenToNumeric(regionPoints
						.get(i));
				gisPoint[index] = p.x;
				index++;
				gisPoint[index] = p.y;
				index++;
			}

			if (gisPoint.length < 3) {
				log.error("多边形顶点数小于3：");
				return null;
			}

			QueryParams queryParams = new QueryParams(SearchType.entire, true,
					false, true, true, false);
			Feature searchFeature = this.bindedMap
					.getFeatureFactory()
					.createRegion(gisPoint, null, null, null, this.getPk(layer));
			
			FeatureSet result = layer.searchWithinRegion(columns,
					searchFeature.getGeometry(), queryParams);
			return afterSearch(layer, new RewindableFeatureSet(result));

		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 在图层中查询指定条件的图元
	 * 
	 * @param layer
	 *            图层
	 * @param queryBuilder
	 *            查询条件
	 * @return
	 * @throws GISException
	 */
	protected FeatureSet searchByQuery(FeatureLayer layer,
			QueryBuilder queryBuilder) throws GISException {
		try {
			List<String> columns = getAllColumns(layer);
			layer.setQueryBuilder(queryBuilder);
			FeatureSet result = layer
					.searchAll(columns, QueryParams.ALL_PARAMS);
			return afterSearch(layer, new RewindableFeatureSet(result));
		} catch (PassThroughQueryException e) {
			throw new GISException(e);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	/**
	 * 查询指定主键值的图元信息
	 * 
	 * @deprecated 2009-10-19使用FeatureSet searchById(String[] id)替代
	 * @param id
	 *            图元id
	 * @return
	 * @throws GISException
	 */
	public List searchById(String id) throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(this.searchById(new String[] { id }));
		}
		return featureSetList;
	}

	/**
	 * 查询指定主键值的图元信息
	 * 
	 * @param id
	 *            图元主键值
	 * @return
	 * @throws GISException
	 */
	public List searchById(String[] id) throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(searchByPk((FeatureLayer)this.getSpatialSearchLayer().get(i), id));
		}
		return featureSetList;
	}

	/**
	 * 查询指定坐标点的图元信息
	 * 
	 * @param point
	 *            坐标点,屏幕坐标系
	 * @return 坐标点所在位置的图元集合，可以多个
	 * @throws GISException
	 */
	public List searchByPoint(DoublePoint point) throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(searchByPoint((FeatureLayer)this.getSpatialSearchLayer().get(i), point));
		}
		return featureSetList;
	}

	/**
	 * 查询指定矩形范围内的图元信息
	 * 
	 * @param rectangle
	 *            矩形,屏幕坐标系
	 * @return 矩形范围内的图元集合
	 * @throws GISException
	 */
	public List searchByRect(DoubleRect rectangle) throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(searchByRect((FeatureLayer)this.getSpatialSearchLayer().get(i), rectangle));
		}
		return featureSetList;
	}

	/**
	 * 查询指定多边形范围内的图元信息
	 * 
	 * @param regionPoints
	 *            多边形顶点,屏幕坐标系
	 * @return 多边形范围内的图元集合
	 * @throws GISException
	 */
	public List searchByRegion(List<DoublePoint> regionPoints)
			throws GISException {
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			featureSetList.add(searchByRegion((FeatureLayer)this.getSpatialSearchLayer().get(i), regionPoints));
		}
		return featureSetList;
	} 
	
	/**
	 * 查询前工作，供子类覆盖
	 * @param layer 查询图层
	 */
	protected void beforeSearch(final FeatureLayer layer) throws GISException {
		
	}
	
	/**
	 * 查询后工作，供子类覆盖
	 * @param layer 查询图层
	 * @param fs 查询结果集
	 */
	protected FeatureSet afterSearch(final FeatureLayer layer, final FeatureSet fs) throws GISException {
		return fs;
	}
	
	/**
	 * 根据缩放级别控制是否显示点
	 * 
	 * @param zoom
	 *            地图缩放等级
	 * @throws GISException
	 */
	public void chageLayerTheme(Double zoom) throws GISException{
		for(int i=0;i<getSpatialSearchLayer().size();i++){
			this.getPoiLayer((FeatureLayer)getSpatialSearchLayer().get(i), zoom);
		}
	}
}
