package com.emq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emq.dao.PlantDao;
import com.emq.model.CommonCheckbox;
import com.emq.util.Global;

public class PlantService {

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
	
	public List testCombox(){
		List list = new ArrayList();
		for(int i=0;i<10;i++){
			CommonCheckbox map = new CommonCheckbox();
			map.setCode("1"+i);
			map.setText("显示"+i);
			map.setOtherInfo(i+"bb");
			list.add(map);
		}
		list.add(0,Global.IS_ALL);//是否有全部属性设置
		list.add(0,Global.HAS_SELECT);// 是否包含请选择属性设置
		return list;
	}
	
	public List testCombox1(String mm,String pp){
		List list = new ArrayList();
		for(int i=0;i<4;i++){
			CommonCheckbox map = new CommonCheckbox();
			map.setCode("1"+i);
			map.setText("显示"+i);
			map.setOtherInfo(i+"bb");
			list.add(map);
		}
		list.add(0,Global.IS_ALL);//是否有全部属性设置
		list.add(0,Global.HAS_SELECT);// 是否包含请选择属性设置
		return list;
	}
}
