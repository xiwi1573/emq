package com.emq.model;
import java.util.List; 

import com.emq.config.MapDef;
import com.emq.config.SystemConfig;
import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.service.DataAccessController; 
/**
 * 地图容器创建工厂
 * @author guqiong
 * @created 2009-9-27
 * @history 2009-10-21
 * 			fix bug:map.setId 应使用地图引用id
 */
public class GISMapFactory {
	private Logger log = Logger.getLogger(GISMapFactory.class);
	
	private DataAccessController dataAccessController;
	
	public DataAccessController getDataAccessController() {
		return dataAccessController;
	}

	public void setDataAccessController(DataAccessController dataAccessController) {
		this.dataAccessController = dataAccessController;
	}

	
	/**
	 * 创建地图容器
	 * @param mapRefId 地图引用id
	 * @return
	 * @throws GISException
	 */
	public AbstractGISMap create(String mapRefId)throws GISException{
		return create(mapRefId, null);
	}
	
	/**
	 * 创建地图容器
	 * @param mapRefId 地图引用id
	 * @param orgId 地图使用机构id
	 * @return
	 * @throws GISException
	 */
	public AbstractGISMap create(String mapRefId, String OrgId)throws GISException{
		MapDef mapDef = SystemConfig.getInstance().getMapDef(mapRefId);
		if(mapDef.isAbstractDef())
			throw new GISException(ErrorMsgConstants.KMGIS_MAP_03);
		else
			return create(mapRefId, mapDef, OrgId);
	}
	
	/**
	 * 创建地图容器
	 * @param mapDef 地图定义
	 * @praram mapRefId  地图引用id
	 * @param orgId 地图使用机构id,为空将使用系统当前用户的组织机构id
	 * @return
	 * @throws GISException
	 */
	protected AbstractGISMap create(String mapRefId, MapDef mapDef, String OrgId) throws GISException{
		AbstractGISMap map = null;
		try{
			String clazz = mapDef.getClazz();
			map = (AbstractGISMap) Class.forName(clazz).newInstance();			
			String orgCode = OrgId;
			if(orgCode==null || "".equals(orgCode)||"null".equals(orgCode)){
				orgCode=dataAccessController.getCurrentUser().getOrgCode();
			}
			MapDef areaDef = SystemConfig.getInstance().getDefaultMap(orgCode);	
			//如果没有默认的地图，再检查允许的列表，都没有则抛异常
			if(areaDef == null){			
				List<String> allowed = SystemConfig.getInstance().getAllowAccessMaps().get(orgCode);
				if(allowed == null || allowed.isEmpty()){
					throw new GISException(ErrorMsgConstants.KMGIS_SECURITY_02);
				}else{
					String defId = allowed.get(0); 
					areaDef = SystemConfig.getInstance().getMapDefines().getMapDefById(defId); 
					if(areaDef == null)
						throw new GISException(ErrorMsgConstants.KMGIS_MAP_08);
				}
	 		}
			map.setMapDef(mapDef.mergeFrom(areaDef));
			map.setId(mapRefId);
			return map;
		}catch(Exception e){
			throw new GISException(e);
		}
	}
}
