package com.emq.ui.formbean;

import java.util.List;

/**
 * 封装UI对地图操作的请求内容
 * 
 * @author guqiong
 * @created 2009-9-21
 * @history 2009-10-14 guqiong 增加半径属性
 * @history 2009-10-15 add toString method
 */
public class MapFormBean implements Cloneable {
	/**
	 * 地图引用id
	 */
	private String mapRefId;
	/**
	 * 需要执行的动作
	 */
	private String action;
	/**
	 * 缩放类型
	 */
	private String zoomType;
	/**
	 * 缩放时的中心点x坐标,屏幕坐标系
	 */
	private String zoomX;
	/**
	 * 缩放时的中心点y坐标,屏幕坐标系
	 */
	private String zoomY;
	/**
	 * 矩形缩放的左上角x坐标,屏幕坐标系
	 */
	private String zoomoutLeft;
	/**
	 * 矩形缩放的左上角y坐标,屏幕坐标系
	 */
	private String zoomoutTop;
	/**
	 * 矩形缩放的右下角x坐标,屏幕坐标系
	 */
	private String zoomoutRight;
	/**
	 * 矩形缩放的右下角y坐标,屏幕坐标系
	 */
	private String zoomoutDown;
	/**
	 * 平移类型
	 */
	private String moveType;
	/**
	 * 平移的起点x坐标,屏幕坐标系
	 */
	private String moveStartX;
	/**
	 * 平移的终点x坐标,屏幕坐标系
	 */
	private String moveEndX;
	/**
	 * 平移的起点y坐标,屏幕坐标系
	 */
	private String moveStartY;
	/**
	 * 平移的终点y坐标,屏幕坐标系
	 */
	private String moveEndY;
	/**
	 * 选择类型
	 */
	private String selectType;
	/**
	 * 选择点的x坐标,屏幕坐标系
	 */
	private String selectX;
	/**
	 * 选择点的y坐标,屏幕坐标系
	 */
	private String selectY;
	/**
	 * 选择圆的半径,屏幕坐标系
	 */
	private String selectRadius;
	/**
	 * 选择矩形的左上角x坐标,屏幕坐标系
	 */
	private String selectLeft;
	/**
	 * 选择矩形的左上角y坐标,屏幕坐标系
	 */
	private String selectTop;
	/**
	 * 选择矩形的右下角x坐标,屏幕坐标系
	 */
	private String selectRight;
	/**
	 * 选择矩形的右下角y坐标,屏幕坐标系
	 */
	private String selectDown;
	/**
	 * 选择多边形的顶点坐标集合,屏幕坐标系
	 */
	private List<String[]> selectRegionPoints;
	/**
	 * 选择多边形的顶点数
	 */
	private String selectRegionPCount;
	/**
	 * 选择的图元主键
	 */
	private String selectId;
	/**
	 * 区域地图id
	 */
	private String areaMapId;
	
	/**
	 * 地图使用机构id
	 */
	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAreaMapId() {
		return areaMapId;
	}

	public void setAreaMapId(String areaMapId) {
		this.areaMapId = areaMapId;
	}

	public String getMapRefId() {
		return mapRefId;
	}

	public void setMapRefId(String mapRefId) {
		this.mapRefId = mapRefId;
	}

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	public String getAction() {
		return action;
	}

	public String getMoveType() {
		return moveType;
	}

	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getZoomType() {
		return zoomType;
	}

	public void setZoomType(String zoomType) {
		this.zoomType = zoomType;
	}

	public String getZoomX() {
		return zoomX;
	}

	public void setZoomX(String zoomX) {
		this.zoomX = zoomX;
	}

	public String getZoomY() {
		return zoomY;
	}

	public void setZoomY(String zoomY) {
		this.zoomY = zoomY;
	}

	public String getZoomoutLeft() {
		return zoomoutLeft;
	}

	public void setZoomoutLeft(String zoomoutLeft) {
		this.zoomoutLeft = zoomoutLeft;
	}

	public String getZoomoutTop() {
		return zoomoutTop;
	}

	public void setZoomoutTop(String zoomoutTop) {
		this.zoomoutTop = zoomoutTop;
	}

	public String getZoomoutRight() {
		return zoomoutRight;
	}

	public void setZoomoutRight(String zoomoutRight) {
		this.zoomoutRight = zoomoutRight;
	}

	public String getZoomoutDown() {
		return zoomoutDown;
	}

	public void setZoomoutDown(String zoomoutDown) {
		this.zoomoutDown = zoomoutDown;
	}

	public String getMoveStartX() {
		return moveStartX;
	}

	public void setMoveStartX(String moveStartX) {
		this.moveStartX = moveStartX;
	}

	public String getMoveEndX() {
		return moveEndX;
	}

	public void setMoveEndX(String moveEndX) {
		this.moveEndX = moveEndX;
	}

	public String getMoveStartY() {
		return moveStartY;
	}

	public void setMoveStartY(String moveStartY) {
		this.moveStartY = moveStartY;
	}

	public String getMoveEndY() {
		return moveEndY;
	}

	public void setMoveEndY(String moveEndY) {
		this.moveEndY = moveEndY;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getSelectX() {
		return selectX;
	}

	public void setSelectX(String selectX) {
		this.selectX = selectX;
	}

	public String getSelectY() {
		return selectY;
	}

	public void setSelectY(String selectY) {
		this.selectY = selectY;
	}
	
	public String getSelectRadius() {
		return selectRadius;
	}

	public void setSelectRadius(String selectRadius) {
		this.selectRadius = selectRadius;
	}

	public String getSelectLeft() {
		return selectLeft;
	}

	public void setSelectLeft(String selectLeft) {
		this.selectLeft = selectLeft;
	}

	public String getSelectTop() {
		return selectTop;
	}

	public void setSelectTop(String selectTop) {
		this.selectTop = selectTop;
	}

	public String getSelectRight() {
		return selectRight;
	}

	public void setSelectRight(String selectRight) {
		this.selectRight = selectRight;
	}

	public String getSelectDown() {
		return selectDown;
	}

	public void setSelectDown(String selectDown) {
		this.selectDown = selectDown;
	}

	public List<String[]> getSelectRegionPoints() {
		return selectRegionPoints;
	}

	public void setSelectRegionPoints(List<String[]> selectRegionPoints) {
		this.selectRegionPoints = selectRegionPoints;
	}

	public String getSelectRegionPCount() {
		return selectRegionPCount;
	}

	public void setSelectRegionPCount(String selectRegionPCount) {
		this.selectRegionPCount = selectRegionPCount;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 检查自身属性是否合法
	 * 
	 * @return
	 */
	public boolean isValidate() {
		// TODO: 检查自身属性合法性
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("mapRefId: ");
		sb.append(mapRefId);
		sb.append("; ");
		sb.append("action: ");
		sb.append(action);
		sb.append("; ");
		sb.append("zoomType: ");
		sb.append(zoomType);
		sb.append("; ");
		sb.append("zoomX: ");
		sb.append(zoomX);
		sb.append("; ");
		sb.append("zoomY: ");
		sb.append(zoomY);
		sb.append("; ");
		sb.append("zoomoutLeft: ");
		sb.append(zoomoutLeft);
		sb.append("; ");
		sb.append("zoomoutTop: ");
		sb.append(zoomoutTop);
		sb.append("; ");
		sb.append("zoomoutRight: ");
		sb.append(zoomoutRight);
		sb.append("; ");
		sb.append("zoomoutDown: ");
		sb.append(zoomoutDown);
		sb.append("; ");
		sb.append("moveType: ");
		sb.append(moveType);
		sb.append("; ");
		sb.append("moveStartX: ");
		sb.append(moveStartX);
		sb.append("; ");
		sb.append("moveEndX: ");
		sb.append(moveEndX);
		sb.append("; ");
		sb.append("moveStartY: ");
		sb.append(moveStartY);
		sb.append("; ");
		sb.append("moveEndY: ");
		sb.append(moveEndY);
		sb.append("; ");
		sb.append("selectType: ");
		sb.append(selectType);
		sb.append("; ");
		sb.append("selectX: ");
		sb.append(selectX);
		sb.append("; ");
		sb.append("selectY: ");
		sb.append(selectY);
		sb.append("; ");
		sb.append("selectRadius: ");
		sb.append(selectRadius);
		sb.append("; ");
		sb.append("selectLeft: ");
		sb.append(selectLeft);
		sb.append("; ");
		sb.append("selectTop: ");
		sb.append(selectTop);
		sb.append("; ");
		sb.append("selectRight: ");
		sb.append(selectRight);
		sb.append("; ");
		sb.append("selectDown: ");
		sb.append(selectDown);
		sb.append("; ");
		sb.append("selectRegionPoints: ");
		sb.append(selectRegionPoints);
		sb.append("; ");
		sb.append("selectId: ");
		sb.append(selectId);
		sb.append("; ");
		sb.append("selectRegionPCount: ");
		sb.append(selectRegionPCount);
		sb.append("; ");
		return sb.toString();		
	}

}
