package com.emq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emq.Global;
import com.emq.dao.PlantDao;

public class PlantService extends BaseService {

	private PlantDao plantDao;

	public PlantDao getPlantDao() {
		return plantDao;
	}

	public void setPlantDao(PlantDao plantDao) {
		this.plantDao = plantDao;
	}

	public Map getPersonById(String personId) {
		return plantDao.getPersonById(personId);
	}

	public List testExtGrid() {
		Map map = new HashMap();
		map.put("姓名", "\\cmpCtrol\\2-2.png");
		map.put("性别", "男");
		map.put("年龄", "7");
		map.put("学历", "4");
		map.put("婚姻状况", "4");
		map.put("住址", "3");
		map.put("公司", "2");
		map.put("职业", "1");
		map.put("爱好", "6");
		map.put("工作性质", "sdf");
		map.put("个人方向", "tt");
		List mapList = new ArrayList();
		mapList.add(map);
		return mapList;
	}

	public String getDataList() {
		List dataList = new ArrayList();
		dataList = plantDao.getDataList("select * from EMQ_PASS_BOOK");
		return this.createPassBookExcel(dataList);
	}

	public List testCombox() {
		List list = new ArrayList();
		list.add(Global.HAS_SELECT);
		// list.add(Global.IS_ALL);
		for (int i = 0; i < 10; i++) {
			Map map = new HashMap();
			map.put("code", i);
			map.put("text", "显示" + i);
			map.put("otherInfo", i + "bb");
			list.add(map);
		}
		return list;
	}

	public List getInstanceExtGrid(int num) {
		List mapList = new ArrayList();
		for (int i = 0; i < num; i++) {
			Map map = new HashMap();
			map.put("姓名", "A1" + i);
			map.put("性别", "男" + i);
			map.put("年龄", "7" + i);
			map.put("学历", "4" + i);
			map.put("婚姻状况", "4" + i);
			map.put("住址", "3" + i);
			map.put("公司", "2" + i);
			map.put("职业", "1" + i);
			map.put("爱好", "6" + i);
			map.put("工作性质", "sdf" + i);
			map.put("个人方向", "tt" + i);
			mapList.add(map);
		}

		return mapList;
	}

	/**
	 * 
	 * @param nodeId
	 * @param treeLevel
	 * @param treeType
	 * @param otherInfo 
	 * treeType指定树的类型
	 * otherInfo记录节点的其他信息,没有则无需指定,可以为空。
	 * nodeId为节点的唯一标志,当nodeId='-1'表示为树的根。
	 */

	public List getCommonTreeNode(String nodeId, String treeLevel,
			String treeType, String otherInfo) {
		List mapList = new ArrayList();
		if(treeType.trim().equals(Global.TREE_TYPE_1)){
			//(供电局+县区市+变电站)
			mapList = this.getTreeType1Data(nodeId, treeLevel, otherInfo);
		}else if(treeType.trim().equals(Global.TREE_TYPE_2)){
            //(供电局+维护班组+人员)
		}else if(treeType.trim().equals(Global.TREE_TYPE_3)){
            //(用电户+变电站)
		}else if(treeType.trim().equals(Global.TREE_TYPE_4)){
            //(线路+变电站)
		}
		return mapList;
	}
	
	/**
	 * 
	 * @param nodeId
	 * @param treeLevel
	 * @param otherInfo
	 * @return(供电局+县区市+变电站)树形构造
	 */
	private List getTreeType1Data(String nodeId, String treeLevel,String otherInfo){
		List mapList = new ArrayList();
		if(treeLevel.equals(Global.TREE_ROOT)){
			//树根,下级应该取供电局
			mapList = this.getPowerSupplyOffice(Global.TREE_LEVEL_1_1,false);
		}else if(treeLevel.equals(Global.TREE_LEVEL_1_1)){
			//供电局,下级应该取县区市
			mapList = this.getProvinceByPowerSupplyOffice(nodeId, Global.TREE_LEVEL_1_2, false);
		}else if(treeLevel.equals(Global.TREE_LEVEL_1_2)){
			//县区市,下级应该取变电站
			mapList = this.getTransformerSubstationByPro(nodeId, treeLevel, true);
		}
		return mapList;
	}
	
	/**
	 * 取供电局方法
	 * @param treeLevel
	 * @param isLeaf
	 */
	private List getPowerSupplyOffice(String treeLevel,boolean isLeaf){
		List mapList = new ArrayList();
		//以下虚拟数据,实际从库中取
		Map map = new HashMap();
		map.put("id", "1");
		map.put("text", "大理供电局");
		map.put("nid", "测试");
		map.put("leaf", isLeaf);
		map.put("treeLevel", treeLevel);
		Map map1 = new HashMap();
		map1.put("id", "2");
		map1.put("text", "丽江供电局");
		map1.put("nid", "测试");
		map1.put("leaf", isLeaf);
		map1.put("treeLevel", treeLevel);
		mapList.add(map);
		mapList.add(map1);
		return mapList;
	}
	
	/**
	 * 供电局下的县区市
	 */
	public List getProvinceByPowerSupplyOffice(String nodeId,String treeLevel,boolean isLeaf){
		List mapList = new ArrayList();
		if(nodeId.equals("1")){
         //以下虚拟数据,实际从库中取
			Map map = new HashMap();
			map.put("id", "1_1");
			map.put("text", "祥云");
			map.put("nid", "测试");
			map.put("leaf", isLeaf);
			map.put("treeLevel", treeLevel);
			map.put("id", "1_2");
			map.put("text", "下关");
			map.put("nid", "测试");
			map.put("leaf", isLeaf);
			map.put("treeLevel", treeLevel);
			mapList.add(map);
		}else if(nodeId.equals("2")){
			//以下虚拟数据,实际从库中取
			Map map = new HashMap();
			map.put("id", "2_1");
			map.put("text", "古城");
			map.put("nid", "测试");
			map.put("leaf", isLeaf);
			map.put("treeLevel", treeLevel);
			map.put("id", "2_2");
			map.put("text", "云山");
			map.put("nid", "测试");
			map.put("leaf", isLeaf);
			map.put("treeLevel", treeLevel);
			mapList.add(map);
		}
		return mapList;
	}
	
	public List getTransformerSubstationByPro(String nodeId,String treeLevel,boolean isLeaf){
		List mapList = new ArrayList();
		//数据库中则根据id就能取出数据不用判断
		if(nodeId.equals("1_1")){
	         //以下虚拟数据,实际从库中取
				Map map = new HashMap();
				map.put("id", "1_1_1");
				map.put("text", "变电站祥云1");
				map.put("nid", "测试");
				map.put("leaf", isLeaf);
				map.put("treeLevel", treeLevel);
				Map map1 = new HashMap();
				map1.put("id", "1_1_2");
				map1.put("text", "变电站祥云2");
				map1.put("nid", "测试");
				map1.put("leaf", isLeaf);
				map1.put("treeLevel", treeLevel);
				mapList.add(map);
				mapList.add(map1);
			}else if(nodeId.equals("1_2")){
				Map map = new HashMap();
				map.put("id", "1_2_1");
				map.put("text", "变电站下关1");
				map.put("nid", "测试");
				map.put("leaf", isLeaf);
				map.put("treeLevel", treeLevel);
				Map map1 = new HashMap();
				map1.put("id", "1_2_2");
				map1.put("text", "变电站下关2");
				map1.put("nid", "测试");
				map1.put("leaf", isLeaf);
				map1.put("treeLevel", treeLevel);
				mapList.add(map);
				mapList.add(map1);
			}else if(nodeId.equals("2_1")){
				Map map = new HashMap();
				map.put("id", "2_1_1");
				map.put("text", "变电站古城1");
				map.put("nid", "测试");
				map.put("leaf", isLeaf);
				map.put("treeLevel", treeLevel);
				Map map1 = new HashMap();
				map1.put("id", "2_1_2");
				map1.put("text", "变电站古城2");
				map1.put("nid", "测试");
				map1.put("leaf", isLeaf);
				map1.put("treeLevel", treeLevel);
				mapList.add(map);
				mapList.add(map1);
			}else if(nodeId.equals("2_2")){
				Map map = new HashMap();
				map.put("id", "2_2_1");
				map.put("text", "变电站云山1");
				map.put("nid", "测试");
				map.put("leaf", isLeaf);
				map.put("treeLevel", treeLevel);
				Map map1 = new HashMap();
				map1.put("id", "2_2_2");
				map1.put("text", "变电站云山2");
				map1.put("nid", "测试");
				map1.put("leaf", isLeaf);
				map1.put("treeLevel", treeLevel);
				mapList.add(map);
				mapList.add(map1);
			}
		return mapList;
	}
}
