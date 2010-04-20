package com.emq.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import Algorithm.Coords.Converter;
import Algorithm.Coords.Point;
import com.emq.dao.BaseDao;
import com.emq.logger.Logger;
import com.emq.model.CigStoreGISMap;
import com.emq.model.ConditionObject;
import com.emq.model.GISMapFactory;
import com.emq.ui.dwr.CigStoreFinderImpl;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.util.DoublePoint;
/**
 * 基础业务逻辑服务
 * @author lyt
 * @created 2009-9-22
 */
public class BaseService {
	
	private CigStoreGISMap target = null;
	
	private GISMapFactory gisMapFactory;
	
	public GISMapFactory getGisMapFactory() {
		return gisMapFactory;
	}
	public void setGisMapFactory(GISMapFactory gisMapFactory) {
		this.gisMapFactory = gisMapFactory;
	}
	
	private static Logger log = Logger.getLogger(BaseService.class);
	
	private BaseDao baseDao;


	public BaseDao getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	/**
	 * 获取R1登录人员编码
	 * @return String
	 */
	public String getUserId()  {
//		Context ctx = null;
//		if (ctx == null) {
//			try {
//				ctx = Context.getInstance();
//			} catch (Exception e) {
//				//e.printStackTrace(); //开发中不在控制台输出
//			}
//		}
//		if (ctx != null) {
//			try {
//				UserInfo userInfo = ctx.getCurrentLoginInfo();
//				log.debug("当前用户ID:" + userInfo.getUserID());
//				return userInfo.getUserID();
//			} catch (Exception e) {
//				 //e.printStackTrace();
//				 //log.error(e.getMessage());
//			}
//		} else {
//			log.error("您好，登录信息过期，请退出重新登录！");
//		}
		return "dev_icss";
	}
	
	/**
	 * 获取R1登录人员组织编码
	 * @return String
	 */
	public List getUserOrgList()  {
//		Context ctx = null;
//		if (ctx == null) {
//			try {
//				ctx = Context.getInstance();
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				e.printStackTrace(); //开发中不在控制台输出
//			}
//		}
//		if (ctx != null) {
//			try {
//				UserInfo userInfo = ctx.getCurrentLoginInfo();
//				log.debug("当前用户ID:" + userInfo.getUserID());
//				List orgList = ctx.getCurrentOrganization();
//				return orgList;
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.error(e.getMessage());
//			}
//		} else {
//			log.error("您好，登录信息过期，请退出重新登录！");
//		}
		return new ArrayList();
	}

	/**
	 * 获取R1登录人员名称
	 * @return String
	 */
	public String getUserName() {
//		Context ctx = null;
//		if (ctx == null) {
//			log.debug("开始初始化RONE信息");
//			try {
//				ctx = Context.getInstance();
//			} catch (Exception e) {
//				e.printStackTrace(); //开发中不在控制台输出
//			}
//		}
//
//		if (ctx != null) {
//			try {
//				UserInfo userInfo = ctx.getCurrentLoginInfo();
//				//log.debug("当前用户:" + userInfo.getName());
//				return userInfo.getName();
//			} catch (Exception e) {
//				//e.printStackTrace();
//				//log.error(e.getMessage());
//			}
//		} else {
//			log.error("您好，登录信息过期，请退出重新登录！");
//		}
		return "测试用户";
	}
	
	/**
	 * 获取街道信息（编码和名称）
	 * @param area 区域ID
	 * @param corpCode 县区市
	 * @return List
	 */
	public List getStreetList(String corpCode,String area){
		return this.baseDao.getStreetList(corpCode,area);
	}
	/**
	 * 获取区域信息（编码和名称）
	 * @param String corpCode 县公司代码
	 * @return List
	 */
	public List getAreaList(String corpCode){
		return this.baseDao.getAreaList(corpCode);
	}
	
	/**
	 * 获取经营业态信息（编码和名称）
	 * @return List
	 */
	public List getFareTypeList(){
		return this.baseDao.getFareTypeList();
	}
	
	/**
	 * 获取当前位置信息
	 * @param orgId 组织ID
	 * @return List
	 */
	public List getLocationList(String orgId){
		return this.baseDao.getLocationList(orgId);
	}
	
	/**
	 * 获取线路信息
	 * @param corpCode
	 * @return List
	 */
	public List getLineList(String corpCode){
		return this.baseDao.getLineList(corpCode);
	}
	
	/**
	 * 获取等级信息
	 * @return List
	 */
	public List getRankList(){
		return this.baseDao.getRankList();
	}
	
	/**
	 * 获取专卖等级信息
	 * @return List
	 */
	public List getSpecialRankList(){
		return this.baseDao.getSpecialRankList();
	}
	
	/**
	 * 获取品牌信息
	 * @return List
	 */
	public List getBrdList(){
		return this.baseDao.getBrdList();
	}
	
	/**
	 * 获取规格信息
	 * @return List
	 */
	public List getCigList(String brd){
		return this.baseDao.getCigList(brd);
	}
	
	/**
	 * 获取街道下拉框
	 * @param area 区域ID
	 * @param corpCode 县区市
	 * @return 街道下拉框html
	 */
	public String getStreetSelect(String corpCode,String area){
		String streetHtml = "<select name='streetName' id='streetName' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List streetList = this.getStreetList(corpCode,area);
		for(int i=0;i<streetList.size();i++){
			Map map = (Map)streetList.get(i);
			streetHtml += "<option value='"+map.get("JDMC")+"'>"+map.get("JDMC")+"</option>";
		}
		streetHtml += "</select>";
		return streetHtml;
	}
	
	/**
	 * 获取区域下拉框
	 * @param String corpCode 县公司代码
	 * @return 区域下拉框html
	 */
	public String getAreaSelect(String corpCode){
		String areaHtml = "<select name='areaName' id='areaName' onchange='areaChange()' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List areaList = this.getAreaList(corpCode);
		for(int i=0;i<areaList.size();i++){
			Map map = (Map)areaList.get(i);
			areaHtml += "<option value='"+map.get("DQMC")+"'>"+map.get("DQMC")+"</option>";
		}
		areaHtml += "</select>";
		return areaHtml;
	}
	
	/**
	 * 获取经营业态下拉框
	 * @return 经营业态下拉框html
	 */
	public String getFareTypeSelect(){
		String areaHtml = "<select name='fareTypeName' id='fareTypeName' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List fareTypeList = this.getFareTypeList();
		for(int i=0;i<fareTypeList.size();i++){
			Map map = (Map)fareTypeList.get(i);
			areaHtml += "<option value='"+map.get("JYYT")+"'>"+map.get("JYYT")+"</option>";
		}
		areaHtml += "</select>";
		return areaHtml;
	}
	
	/**
	 * 获取当前位置下拉框
	 * @return 当前位置下拉框html
	 */
	public String getLocationSelect(String orgId){
		System.out.println("orgId="+orgId);
		String locationHtml = "<select name='location' id='location' onchange='locationChange(this.value)'>";
		List orgList = this.getUserOrgList();
		System.out.println("orgList:="+orgList.size());
		if(orgId==null||orgId.equals("")){
//			for(int i=0;i<orgList.size();i++){
//				Organization org = (Organization)orgList.get(i);
//				log.debug("当前用户组织编码:" + org.getOrgcode());
//				System.out.println("当前用户组织编码:="+org.getOrgcode());
//				orgId += "'"+org.getOrgcode()+"',";
//			}
			if(orgId.length()>0){
				orgId = orgId.substring(0, orgId.length()-1);
			}
			if(orgId.length()<1){
				orgId = "'53010000'";
			}
		}
		List locationList = this.getLocationList(orgId);
		for(int i=0;i<locationList.size();i++){
			Map map = (Map)locationList.get(i);
			locationHtml += "<option value='"+map.get("ZMJGBM")+"|"+map.get("DTPZH")+"'>"+map.get("ZMJGMC")+"</option>";
		}
		locationHtml += "</select>";
		return locationHtml;
	}
	
	/**
	 * 获取县区市下拉框
	 * @return 县区市下拉框html
	 */
	public String getCorpSelect(){
		
		List orgList = this.getUserOrgList();
		String orgId = "";
//		for(int i=0;i<orgList.size();i++){
//			Organization org = (Organization)orgList.get(i);
//			log.debug("当前用户组织编码:" + org.getOrgcode());
//			orgId += "'"+org.getOrgcode()+"',";
//		}
		if(orgId.length()>0){
			orgId = orgId.substring(0, orgId.length()-1);
		}
		if(orgId.length()<1){
			orgId = "'53010000'";
		}
		List locationList = this.getLocationList(orgId);
		String corpCodeHtml = "<select name='corpCode' id='corpCode' onchange='corpCodeChange(this.value)' style='width:96'><option value=''>-请选择-</option>";
		if(locationList.size()>1){
			corpCodeHtml += "<option value='all'>-全部-</option>";
		}
		for(int i=0;i<locationList.size();i++){
			Map map = (Map)locationList.get(i);
			corpCodeHtml += "<option value='"+map.get("ZMJGBM")+"'>"+map.get("ZMJGMC")+"</option>";
		}
		corpCodeHtml += "</select>";
		return corpCodeHtml;
	}
	
	/**
	 * 获取线路下拉框
	 * @param corpCode
	 * @return 线路下拉框html
	 */
	public String getLineSelect(String corpCode){
		String lineHtml = "<select name='line' id='line' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List lineList = this.getLineList(corpCode);
		for(int i=0;i<lineList.size();i++){
			Map map = (Map)lineList.get(i);
			lineHtml += "<option value='"+map.get("XSXL")+"'>"+map.get("XSXL")+"</option>";
		}
		lineHtml += "</select>";
		return lineHtml;
	}
	
	/**
	 * 获取等级下拉框
	 * @return 等级下拉框html
	 */
	public String getRankSelect(){
		String rankHtml = "<select name='rank' id='rank' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List rankList = this.getRankList();
		for(int i=0;i<rankList.size();i++){
			Map map = (Map)rankList.get(i);
			rankHtml += "<option value='"+map.get("XSKHDJ")+"'>"+map.get("XSKHDJ")+"</option>";
		}
		rankHtml += "</select>";
		return rankHtml;
	}
	
	/**
	 * 获取专卖等级下拉框
	 * @return 等级下拉框html
	 */
	public String getSpecialRankSelect(){
		String specialRankHtml = "<select name='specialRank' id='specialRank' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List specialRankList = this.getRankList();
		for(int i=0;i<specialRankList.size();i++){
			Map map = (Map)specialRankList.get(i);
			specialRankHtml += "<option value='"+map.get("ZMDJ")+"'>"+map.get("ZMDJ")+"</option>";
		}
		specialRankHtml += "</select>";
		return specialRankHtml;
	}
	
	/**
	 * 获取品牌下拉框
	 * @return 品牌下拉框html
	 */
	public String getBrdSelect(){
		String rankHtml = "<select name='brd' id='brd' onchange='brdChange()' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List brdList = this.getBrdList();
		for(int i=0;i<brdList.size();i++){
			Map map = (Map)brdList.get(i);
			rankHtml += "<option value='"+map.get("BRD")+"'>"+map.get("BRDNAME")+"</option>";
		}
		rankHtml += "</select>";
		return rankHtml;
	}
	
	/**
	 * 获取规格下拉框
	 * @return 规格下拉框html
	 */
	public String getCigSelect(String brd){
		String rankHtml = "<select name='cig' id='cig' style='width:96'><option value=''>-请选择-</option><option value='all'>-全部-</option>";
		List cigList = this.getCigList(brd);
		for(int i=0;i<cigList.size();i++){
			Map map = (Map)cigList.get(i);
			rankHtml += "<option value='"+map.get("CIG")+"'>"+map.get("CIGNAME")+"</option>";
		}
		rankHtml += "</select>";
		return rankHtml;
	}
	
	/**
	 * 根据查询条件进行查询
	 * @param mapId
	 * @param startx
	 * @param starty
	 * @param newx
	 * @param newy
	 * @param session
	 * @return
	 */
	public void findByCondition(String mapId,ConditionObject co,HttpSession session){
		CigStoreFinderImpl cf = new CigStoreFinderImpl();
		try{
			cf.highlightByCondition(mapId,co, session);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 自动调度偏移计算
	 */
	public void runConverter(){
		List sqlList = new ArrayList();
		List dataList = baseDao.getRunConverterList();
		System.out.println("一共有"+dataList.size()+"需要计算偏移坐标!");
		try {
			target = (CigStoreGISMap) gisMapFactory.create("CigStoreMap");
			target.loadMap();
			Converter converter = new Converter();
			for(int i=0;i<dataList.size();i++){
				Map map = (Map)dataList.get(i);
				Double x = (Double)map.get("X1");
				Double y = (Double)map.get("Y1");
				Point p = null;
				DoublePoint dp = null;
				if(x<y){
					p = converter.getEncryPoint(y,x); 
					dp = new DoublePoint(y,x);
					sqlList.add("update GIS_MS_LICENSE set X="+p.getY()+",Y="+p.getX()+" where XKZHM='"+map.get("XKZHM")+"'");
				}else{
					p = converter.getEncryPoint(x,y); 
					dp = new DoublePoint(x,y);
					sqlList.add("update GIS_MS_LICENSE set X="+p.getX()+",Y="+p.getY()+" where XKZHM='"+map.get("XKZHM")+"'");
				}
//				超出范围列如采点数据
				boolean within = target.within(dp,(String)map.get("GSDM"));
				if(!within){
					String sql = "update gis_ms_license_check set sfxs=1,lrcdsj=getdate() where xkzhm='"+map.get("XKZHM")+"'";
					sqlList.add(sql);
				}
			}
			baseDao.exeUpdateSqlByBach(sqlList);
			System.out.println("计算偏移坐标结束!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改xy坐标
	 * @param po 
	 */
	public boolean updateCheckPoint(Double x,Double y,String xkzhm){
		boolean mark = true;
		try{
			Map pointMap = this.getBaseDao().getDataForMap("select x,y,x1,y1 from GIS_MS_LICENSE_CHECK where XKZHM='"+xkzhm+"'");
			Double x_temp = (Double)pointMap.get("X")-(Double)pointMap.get("X1");
			Double y_temp = (Double)pointMap.get("Y")-(Double)pointMap.get("Y1");
			Double x1 = x-x_temp;
		    Double y1 = y-y_temp;;
			this.getBaseDao().exeUpdateSql("update GIS_MS_LICENSE_CHECK set x="+x+",y="+y+",x1="+x1+",y1="+y1+" where XKZHM='"+xkzhm+"'");
		} catch (Exception e) {
			mark = false;
			e.printStackTrace();
		}
		return mark;
	}
	
	/**
	 * 获取批次下拉框
	 * @return 批次下拉框html
	 */
	public String getPcSelect(String corpCode){
		String rankHtml = "<select name='pc' id='pc' style='width:96'><option value=''>-请选择-</option>";
		List pcList = this.baseDao.getPcList(corpCode);
		for(int i=0;i<pcList.size();i++){
			Map map = (Map)pcList.get(i);
			rankHtml += "<option value='"+map.get("PC")+"'>"+map.get("PC")+"</option>";
		}
		rankHtml += "</select>";
		return rankHtml;
	}
	
}
