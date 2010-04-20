package com.emq.model;

/**
 * 查询条件对象
 * 
 * @author lyt
 * @created 2009-9-22 注意：参数为''或者null或者'all'的时候不做为过滤条件
 * @history 2009-10-21 guqiong add 许可证状态 licenceStat
 */
public class ConditionObject implements Cloneable {

	/**
	 * 许可证编号
	 */
	private String licenceNo;
	/**
	 * 商店名称
	 */
	private String storeName;
	/**
	 * 法人代表
	 */
	private String corporationName;
	/**
	 * 街道名称
	 */
	private String streetName;
	/**
	 * 区域名称
	 */
	private String areaName;
	/**
	 * 经营业态名称
	 */
	private String fareTypeName;
	/**
	 * 经营地址
	 */
	private String fareAddress;

	/**
	 * 线路
	 */
	private String line;
	/**
	 * 等级
	 */
	private String rank;
	/**
	 * 专卖等级
	 */
	private String specialRank;
	/**
	 * 办证开始时间
	 */
	private String paperTime_start;
	/**
	 * 办证结束时间
	 */
	private String paperTime_end;
	/**
	 * 许可证状态 1、正常 5、注销 6、停业7、歇业
	 */
	private String licenceState;

	/**
	 * 品牌编码
	 */
	private String brd;

	/**
	 * 规格编码
	 */
	private String cig;
	
	/**
	 * 县公司代码
	 */
	private String corpCode;
	
	/**
	 * 零售业态
	 */
	private String sellType;
	
	/**
	 * 市场类型
	 */
	private String markType;
	
	/**
	 * 经营规模
	 */
	private String fareSize;
	
	/**
	 * 注销开始时间
	 */
	private String logoutTime_start;
	/**
	 * 注销结束时间
	 */
	private String logoutTime_end;
	
	/**
	 * 不定货月数
	 */
	private int noOrderMonth;
	
	/**
	 * 批次
	 */
	private int pc;
	
	/**
	 * 数据类型:需校正数据(1),已上传校正数据(2),完成校正数据(3)
	 */
	private String dataType;

	public int getNoOrderMonth() {
		return noOrderMonth;
	}

	public void setNoOrderMonth(int noOrderMonth) {
		this.noOrderMonth = noOrderMonth;
	}

	public String getLicenceState() {
		return licenceState;
	}

	public void setLicenceState(String licenceState) {
		this.licenceState = licenceState;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getLicenceNo() {
		return licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getFareTypeName() {
		return fareTypeName;
	}

	public void setFareTypeName(String fareTypeName) {
		this.fareTypeName = fareTypeName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getFareAddress() {
		return fareAddress;
	}

	public void setFareAddress(String fareAddress) {
		this.fareAddress = fareAddress;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPaperTime_end() {
		return paperTime_end;
	}

	public void setPaperTime_end(String paperTime_end) {
		this.paperTime_end = paperTime_end;
	}

	public String getPaperTime_start() {
		return paperTime_start;
	}

	public void setPaperTime_start(String paperTime_start) {
		this.paperTime_start = paperTime_start;
	}

	public String getBrd() {
		return brd;
	}

	public void setBrd(String brd) {
		this.brd = brd;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 是否忽略条件
	 * 
	 * @param param
	 * @return
	 */
	public boolean mayBeUsed(String param) {
		return !(param == null || "".equals(param) || "all".equals(param));
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getFareSize() {
		return fareSize;
	}

	public void setFareSize(String fareSize) {
		this.fareSize = fareSize;
	}

	public String getMarkType() {
		return markType;
	}

	public void setMarkType(String markType) {
		this.markType = markType;
	}

	public String getSellType() {
		return sellType;
	}

	public void setSellType(String sellType) {
		this.sellType = sellType;
	}

	public String getLogoutTime_end() {
		return logoutTime_end;
	}

	public void setLogoutTime_end(String logoutTime_end) {
		this.logoutTime_end = logoutTime_end;
	}

	public String getLogoutTime_start() {
		return logoutTime_start;
	}

	public void setLogoutTime_start(String logoutTime_start) {
		this.logoutTime_start = logoutTime_start;
	}

	public String getSpecialRank() {
		return specialRank;
	}

	public void setSpecialRank(String specialRank) {
		this.specialRank = specialRank;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
