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

	public List getTreeNode(int pN) {
		List mapList = new ArrayList();
		if (pN == -1) {
			Map map = new HashMap();
			map.put("id", 1);
			map.put("text", "第一层一");
			map.put("nid", "1_1");
			mapList.add(map);
			// map.put("oid", "第一层一");
			Map map1 = new HashMap();
			map1.put("id", 2);
			map1.put("text", "第一层二");
			map.put("nid", "1_2");
			mapList.add(map1);
		} else if (pN == 1) {
			Map map = new HashMap();
			map.put("id", 11);
			map.put("text", "第二层一");
			map.put("nid", "2_1");
			map.put("leaf", true);
			mapList.add(map);
		} else if (pN == 2) {
			Map map = new HashMap();
			map.put("id", 12);
			map.put("text", "第二层二");
			map.put("nid", "2_2");
			map.put("leaf", true);
			mapList.add(map);
		}
		return mapList;
	}
}
