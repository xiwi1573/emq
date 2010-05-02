package com.plant.dao;

import java.util.Map;
import com.plant.dao.jdbc.BaseJdbcDaoImp;

public class PlantDao extends BaseJdbcDaoImp{

	/**
	 * 根据人员编号获取人员信息
	 * @param userId
	 * @return
	 */
	public Map getPersonById(String personId){
		Map person =  this.getDataForMap("select personName,personId,password from plant_person where personId='"+personId+"'");
		return person;
	}
	
	/**
	 * 验证用户密码
	 */
	public Map checkPurview(String personId,String password){
		Map person =  this.getDataForMap("select personName,personId,password from plant_person where personId='"+personId+"' and password='"+password+"'");
		return person;
	}
}
