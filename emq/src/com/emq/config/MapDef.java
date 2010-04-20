package com.emq.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.emq.exception.GISException;

/**
 * 地图定义
 * 
 * @author guqiong
 * @created 2009-9-27
 */
public class MapDef implements Cloneable {

	/**
	 * 地图定义id，标识唯一的一个地图
	 */
	private String id;
	/**
	 * 地图容器的实现类名, 必须是com.icss.km.gis.model.AbstractGISMap的子类
	 */
	private String clazz;
	/**
	 * 主地图配置文件
	 */
	private String mainMapDefFile;
	/**
	 * 主地图默认图片输出宽度
	 */
	private Integer mainMapImgWidth;
	/**
	 * 主地图默认图片输出高度
	 */
	private Integer mainMapImgHight;
	/**
	 * 主地图默认缩放级别
	 */
	private Double mainMapZoom;
	/**
	 * 缩略图配置文件
	 */
	private String previewMapDefFile;
	/**
	 * 缩略图默认图片输出宽度
	 */
	private Integer previewMapImgWidth;
	/**
	 * 缩略图默认图片输出高度
	 */
	private Integer previewMapImgHight;
	/**
	 * 缩略图默认缩放级别
	 */
	private Double previewMapZoom;
	/**
	 * 查询图层定义
	 */
	//private SpatialSearchLayerDefine spatialSearchLayerDefine;
	private List spatialSearchLayerDefineList;
	
	/**
	 * 静态显示图层定义
	 */
	private Map<String,StaticLayerDefine> staticLayerDefines;


	/**
	 * 是否抽象的定义
	 */
	private boolean abstractDef = false;
	/**
	 * 继承的基础定义id
	 */
	private String parentId;
	/**
	 * 地图描述
	 */
	private String describe;	
	/**
	 * 地图中心点x坐标，地图坐标系
	 */
	private Double x;
	/**
	 * 地图中心点y坐标，地图坐标系
	 */
	private Double y;

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isAbstractDef() {
		return abstractDef;
	}

	public void setAbstractDef(boolean abstractDef) {
		this.abstractDef = abstractDef;
	}

//	public SpatialSearchLayerDefine getSpatialSearchLayerDefine() {
//		return spatialSearchLayerDefine;
//	}
//
//	public void setSpatialSearchLayerDefine(
//			SpatialSearchLayerDefine spatialSearchLayerDefine) {
//		this.spatialSearchLayerDefine = spatialSearchLayerDefine;
//	}

	public Map<String, StaticLayerDefine> getStaticLayerDefines() {
		return staticLayerDefines;
	}

	public void setStaticLayerDefines(
			Map<String, StaticLayerDefine> staticLayerDefines) {
		this.staticLayerDefines = staticLayerDefines;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMainMapDefFile() {
		return mainMapDefFile == null? null: mainMapDefFile.trim();
	}

	public void setMainMapDefFile(String mainMapDefFile) {
		this.mainMapDefFile = mainMapDefFile;
	}

	public Integer getMainMapImgWidth() {
		return mainMapImgWidth;
	}

	public void setMainMapImgWidth(Integer mainMapImgWidth) {
		this.mainMapImgWidth = mainMapImgWidth;
	}

	public Integer getMainMapImgHight() {
		return mainMapImgHight;
	}

	public void setMainMapImgHight(Integer mainMapImgHight) {
		this.mainMapImgHight = mainMapImgHight;
	}

	public String getPreviewMapDefFile() {
		return previewMapDefFile == null ? null : previewMapDefFile.trim();
	}

	public void setPreviewMapDefFile(String previewMapDefFile) {
		this.previewMapDefFile = previewMapDefFile;
	}

	public Integer getPreviewMapImgWidth() {
		return previewMapImgWidth;
	}

	public void setPreviewMapImgWidth(Integer previewMapImgWidth) {
		this.previewMapImgWidth = previewMapImgWidth;
	}

	public Integer getPreviewMapImgHight() {
		return previewMapImgHight;
	}

	public void setPreviewMapImgHight(Integer previewMapImgHight) {
		this.previewMapImgHight = previewMapImgHight;
	}

	public Double getMainMapZoom() {
		return mainMapZoom;
	}

	public void setMainMapZoom(Double mainMapZoom) {
		this.mainMapZoom = mainMapZoom;
	}

	public Double getPreviewMapZoom() {
		return previewMapZoom;
	}

	public void setPreviewMapZoom(Double previewMapZoom) {
		this.previewMapZoom = previewMapZoom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 继承基础属性
	 * 
	 * @param parent
	 *            父定义
	 * @return 继承父属性后的新MapDef对象
	 */
	protected MapDef extend(MapDef parent) throws GISException {
		try {
			MapDef def = (MapDef) this.clone();
			if (def.mainMapDefFile == null)
				def.setMainMapDefFile(parent.getMainMapDefFile());
			if (def.mainMapImgWidth == null)
				def.mainMapImgWidth = parent.mainMapImgWidth;
			if (def.mainMapImgHight == null)
				def.mainMapImgHight = parent.mainMapImgHight;
			if (def.mainMapZoom == null)
				def.mainMapZoom = parent.mainMapZoom;
			if (def.previewMapDefFile == null)
				def.previewMapDefFile = parent.previewMapDefFile;
			if (def.previewMapImgWidth == null)
				def.previewMapImgWidth = parent.previewMapImgWidth;
			if (def.previewMapImgHight == null)
				def.previewMapImgHight = parent.previewMapImgHight;
			if (def.previewMapZoom == null)
				def.previewMapZoom = parent.previewMapZoom;
			if (def.describe == null)
				def.describe = parent.describe;
			if (def.clazz == null)
				def.clazz = parent.clazz;
			if (def.spatialSearchLayerDefineList == null && parent.spatialSearchLayerDefineList!=null)
				def.spatialSearchLayerDefineList = (List) parent.spatialSearchLayerDefineList;
			if (def.staticLayerDefines == null && parent.staticLayerDefines!=null){
				def.staticLayerDefines = new HashMap<String, StaticLayerDefine>();
				for(StaticLayerDefine sds: parent.staticLayerDefines.values()){
					StaticLayerDefine sd = (StaticLayerDefine)sds.clone();
					def.staticLayerDefines.put(sd.getId(), sd);
				} 
			}
			
			if (def.x == null && parent.x!=null)
				def.x = parent.x.doubleValue();
			if (def.y == null && parent.y!=null)
				def.y = parent.y.doubleValue();
			return def;
		} catch (CloneNotSupportedException e) {
			throw new GISException(e);
		}
	}

	/**
	 * 合并定义,不覆盖已有属性
	 * 
	 * @param other
	 * @return 合并后的新对象
	 */
	public MapDef mergeFrom(MapDef other) throws GISException {
		try {
			MapDef otherCopy = (MapDef) other.clone();
			MapDef thisCopy = (MapDef) this.clone();
			thisCopy = thisCopy.extend(otherCopy);
			String parentId = otherCopy.getParentId();
			while (parentId != null) {
				MapDef parentDef = SystemConfig.getInstance().getMapDefines().getMapDefById(
						parentId);
				thisCopy = thisCopy.extend(parentDef);
				parentId = parentDef.getParentId();
			}
			return thisCopy;
		} catch (Exception e) {
			throw new GISException(e);
		}
	}
	
	/**
	 * 合并父定义,不覆盖已有属性
	 * @return 合并后的新对象
	 */
	public MapDef mergeParent() throws GISException {
		try {
			if (this.getParentId() == null)
				return (MapDef) this.clone();
			MapDef parent = SystemConfig.getInstance().getMapDefines().getMapDefById(
					this.getParentId());
			return this.mergeFrom(parent);
		} catch (Exception e) {
			throw new GISException(e);
		}
	}

	public List getSpatialSearchLayerDefineList() {
		return spatialSearchLayerDefineList;
	}

	public void setSpatialSearchLayerDefineList(List spatialSearchLayerDefineList) {
		this.spatialSearchLayerDefineList = spatialSearchLayerDefineList;
	}
}
