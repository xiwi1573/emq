package com.emq.service;

import java.util.Map;

import com.emq.dao.PlantDao;

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
}
