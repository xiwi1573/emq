package com.plant.service;

import java.util.Map;

import com.plant.dao.PlantDao;

public class PlantService {

	private PlantDao plantDao;

	public PlantDao getPlantDao() {
		return plantDao;
	}

	public void setPlantDao(PlantDao plantDao) {
		this.plantDao = plantDao;
	}

	public Map checkPurview(String personId, String password) {
		return this.getPlantDao().checkPurview(personId, password);
	}

}
