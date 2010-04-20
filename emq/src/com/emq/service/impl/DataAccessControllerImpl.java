package com.emq.service.impl;

import java.util.List;

import com.emq.exception.ErrorMsgConstants;
import com.emq.exception.GISException;
import com.emq.logger.Logger;
import com.emq.model.AccessCtrolable;
import com.emq.model.AuthenticatedUser;
import com.emq.model.R1AuthenticatedUser;
import com.emq.service.DataAccessController;

/**
 * 数据访问控制器实现
 * 
 * @author guqiong
 * @created 2009-10-29
 */
public class DataAccessControllerImpl implements DataAccessController {

	private static Logger log = Logger
			.getLogger(DataAccessControllerImpl.class);

	public DataAccessControllerImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.service.DataAccessController#isAllowAccess(com.icss.km
	 * .gis.model.pojo.AuthenticatedUser, int)
	 */
	public boolean isAllowAccess(AuthenticatedUser user,
			int targetObjectPermMask) {
		int userPermMask = user.getPermissonMask();
		return (userPermMask & targetObjectPermMask) >= targetObjectPermMask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.icss.km.gis.service.DataAccessController#isAllowAccess(com.icss.km
	 * .gis.model.pojo.AuthenticatedUser, com.icss.km.gis.model.AccessCtrolable)
	 */
	public boolean isAllowAccess(AuthenticatedUser user,
			AccessCtrolable targetObject) {
		int targetObjectPermMask = targetObject.getRequiredPermissonMask();
		return this.isAllowAccess(user, targetObjectPermMask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.service.DataAccessController#getCurrentUser()
	 */
	public  AuthenticatedUser getCurrentUser() throws GISException {
		AuthenticatedUser user = null;
//		try {
//			Context ctx = Context.getInstance();
//			UserInfo r1User = ctx.getCurrentLoginInfo();
//			List orgList = ctx.getCurrentOrganization();
//			if (r1User == null || orgList == null || orgList.size() == 0)
//				throw new GISException(ErrorMsgConstants.KMGIS_SECURITY_01);
//			Organization mainOrg = (Organization) orgList.get(0);
//			String orgId = mainOrg.getOrgcode();
//			log.info("UserInfo.getCurrentOrganization[0]: ");
//			log.info(orgId);
//			GisSsOrg org = gisSsOrgDao.getUsedGisSsOrg(orgId);
//			int jgjb = 0;
//			if(org!=null&&org.getJgjb()!=null){
//				jgjb = org.getJgjb();
//			}
//			user = new R1AuthenticatedUser(orgId,jgjb);
//			return user;
//		} catch (Exception e) {		
//			e.printStackTrace();
//			log.error(ErrorMsgConstants.KMGIS_SECURITY_01, e);
//		}
		//如果没有取到r1用户信息，则取一个测试信息
		if(user == null){
			user = new R1AuthenticatedUser("53010100", ORG_PERM_MASK_COUNTY); 
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.service.DataAccessController#getHigherPermMask(int)
	 */
	public int getHigherPermMask(int permMask) {
		return (permMask << 1) | permMask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.service.DataAccessController#getLowerPermMask(int)
	 */
	public int getLowerPermMask(int permMask) {
		if (permMask != getLowestPermMask())
			return (permMask >> 1);
		else
			return permMask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.icss.km.gis.service.DataAccessController#getLowestPermMask()
	 */
	public int getLowestPermMask() {
		return ORG_PERM_MASK_PLACE;
	}

}
