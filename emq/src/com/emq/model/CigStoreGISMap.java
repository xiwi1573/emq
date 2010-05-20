package com.emq.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.emq.config.MapDef;
import com.emq.config.SpatialSearchLayerDefine;
import com.emq.config.SystemConfig;
import com.emq.dao.CigStoreQueryBuilder;
import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.PointGeometry;
import com.mapinfo.dp.TableInfo;
import com.mapinfo.graphics.Rendition;
import com.mapinfo.graphics.RenditionImpl;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.util.DoublePoint;

/**
 * 卷烟零售店的地图容器
 * 
 * @author guqiong
 * @created 2009-9-21
 */
public class CigStoreGISMap extends SearchableGISMap {

	public CigStoreGISMap(MapDef def) {
		super(def);
	}

	public CigStoreGISMap() {
		super();
	}

	Logger log = Logger.getLogger(CigStoreGISMap.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMap#setAutoLabelOn(boolean)
	 */
	protected void setAutoLabelOn(boolean labelEnabled) {
		for(int i=0;i<this.getSpatialSearchLayer().size();i++){
			FeatureLayer layer = (FeatureLayer)this.getSpatialSearchLayer().get(i);
			if (labelEnabled) {
				List spatialSearchLayerDefineList = this.getMapDef().getSpatialSearchLayerDefineList();
				for(int j=0;j<spatialSearchLayerDefineList.size();j++){
					layer.getLabelProperties().setLabelExpression(
							((SpatialSearchLayerDefine)spatialSearchLayerDefineList.get(j)).getLabel());
				}
//				layer.getLabelProperties().setLabelExpression(
//						this.getMapDef().getSpatialSearchLayerDefine().getLabel());
				layer.setAutoLabel(true);
			} else
				layer.setAutoLabel(false);
		}
		
	}

	/**
	 * 根据查询条件查找图层
	 * 
	 * @param co
	 *            查询条件
	 * @return
	 * @throws GISException
	 */
	public List searchByCondition(ConditionObject co,String tableName) throws GISException { 
		CigStoreQueryBuilder qryBuilder = new CigStoreQueryBuilder(co,tableName);
		List featureSetList = new ArrayList();
		for(int i=0;i<this.getQuerySearchLayer().size();i++){
			FeatureLayer layer = (FeatureLayer)this.getQuerySearchLayer().get(i);
			featureSetList.add(this.searchByQuery(layer, qryBuilder));
		}
		return featureSetList;
//		FeatureLayer layer = this.getQuerySearchLayer();
//		return this.searchByQuery(layer, qryBuilder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.model.AbstractGISMap#searchByCondition(java.lang.Object)
	 */
	public List searchByCondition(Object co,String tableName) throws GISException {
		if (co instanceof ConditionObject)
			return this.searchByCondition((ConditionObject) co,tableName);
		else {
			throw new GISException(ErrorMsgConstants.EMQ_MAP_02);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMap#getHighlightRend()
	 */
	protected Rendition getHighlightRend() {
		Rendition rend = new RenditionImpl();
		rend.setValue(Rendition.SYMBOL_FOREGROUND, new Color(227,74,1));
		rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,1.0);
		rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,1.0);
		return rend;
	}
	 
	/* (non-Javadoc)
	 * @see com.icss.km.gis.model.AbstractGISMap#getSecondHighlightRend()
	 */
	protected Rendition getSecondHighlightRend() {
		Rendition rend = new RenditionImpl(); 
		rend.setValue(Rendition.FONT_FAMILY, "MapInfo Miscellaneous"); 
		rend.setValue(Rendition.FONT_SIZE, 30);  
		rend.setValue(Rendition.SYMBOL_MODE, Rendition.SymbolMode.FONT); 
		rend.setValue(Rendition.SYMBOL_STRING, "u"); 
		rend.setValue(Rendition.SYMBOL_FOREGROUND, Color.red);
		rend.setValue(Rendition.SYMBOL_BACKGROUND, Color.red);
		rend.setValue(Rendition.SYMBOL_BACKGROUND_OPACITY,1.0);
		rend.setValue(Rendition.SYMBOL_FOREGROUND_OPACITY,1.0);
		//调用自定义的图片
//		rend.setValue(Rendition.SYMBOL_URL,"http://localhost:8080/KMGIS/imgs/info.gif");
//	    rend.setValue(Rendition.SYMBOL_MODE, Rendition.SymbolMode.IMAGE);
//	    rend.setValue(Rendition.SYMBOL_SIZE, new com.mapinfo.unit.Size(16));  
//	    rend.setValue(Rendition.SYMBOL_FOREGROUND, Color.white);
//	    rend.setValue(Rendition.SYMBOL_BACKGROUND, Color.green);
	    return rend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.model.AbstractGISMap#isValidZoom(java.lang.Double)
	 */
	protected boolean isValidZoom(Double zoom) {

		return zoom >= 0.25 && zoom <= 250.0;
	}

	/**
	 * 判断点是否在指定的区域内
	 * 
	 * @param gisPoint
	 *            gis坐标点
	 * @param areaCode
	 *            区域代码-只支持昆明市，昆明市四个城区用53010100,其余同GIS_CON_REGION的ZMJGBM,具体配置见kmgis
	 *            .properties
	 * @return
	 * @throws GISException
	 */
	public boolean within(DoublePoint gisPoint, String areaCode)
			throws GISException {
		String areaLayerName = SystemConfig.getInstance().getProperty(
				"map.area.layer");
		String nameColumn = SystemConfig.getInstance().getProperty(
				"map.area.name_column");
		String areaNameKey = "map.area." + areaCode;
		String areaName = SystemConfig.getInstance().getProperty(areaNameKey);
		return super.within(gisPoint, areaLayerName, nameColumn, areaName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.model.AbstractGISMap#isValidFeature(com.mapinfo.dp.Feature
	 * )
	 */
	protected boolean isValidFeature(Feature f, TableInfo tableInfo) throws GISException {
		try {
			PointGeometry pGeometry = (PointGeometry) f.getGeometry();
			if (pGeometry == null) {
				return false;
			}
			DoublePoint gisPoint = pGeometry.getPoint(null);
			if (gisPoint == null) {
				return false;
			}
			 
			int columnIndex = tableInfo.getColumnIndex("gsdm"); 
			String gsdm = f.getAttribute(columnIndex).getString();
			if (gsdm != null && !"".equals(gsdm)) {
				return this.within(gisPoint, gsdm);
			}
			return false;
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	

}
