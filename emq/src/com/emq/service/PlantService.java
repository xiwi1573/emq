package com.emq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emq.dao.PlantDao;
import com.emq.model.CommonCheckbox;

public class PlantService extends BaseService{

	private PlantDao plantDao;
	
	public PlantDao getPlantDao() {
		return plantDao;
	}

	public void setPlantDao(PlantDao plantDao) {
		this.plantDao = plantDao;
	}

	public Map getPersonById(String personId){
		return plantDao.getPersonById(personId);
	}
	
	public List testExtGrid(){
		Map map = new HashMap();
		map.put("姓名", "A1");
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
	
	public String getDataList(){
		List dataList = new ArrayList();
		dataList = plantDao.getDataList("select * from EMQ_PASS_BOOK");
		return this.createPassBookExcel(dataList);
	}
	
	public List testCombox(){
		List list = new ArrayList();
		for(int i=0;i<10;i++){
			Map map = new HashMap();
			map.put("code", i);
			map.put("text","显示"+i);
			map.put("otherInfo",i+"bb");
			list.add(map);
		}
		return list;
	}
}
