package com.emq.service;

import com.emq.exception.GISException;
import com.emq.model.AccessCtrolable;
import com.emq.model.AuthenticatedUser;

/**
 * 数据访问控制器
 * @author guqiong
 * @created 2009-10-29
 */
public interface DataAccessController {
	
	/**
	 * 权限掩码，地级市，11111
	 */
	public static final int ORG_PERM_MASK_CITY = 0x1f;
	/**
	 * 权限掩码，区县，1111
	 */
	public static final int ORG_PERM_MASK_COUNTY = 0xf;
	/**
	 * 权限掩码，县内区域，0111
	 */
	public static final int ORG_PERM_MASK_REGION = 0x7;
	/**
	 * 权限掩码，街道，0011
	 */
	public static final int ORG_PERM_MASK_STREET = 0x3;
	/**
	 * 权限掩码，地点，0001
	 */
	public static final int ORG_PERM_MASK_PLACE = 0x1;

	/**
	 * 是否允许访问目标
	 * 
	 * @param user
	 *            用户
	 * @param targetObjectPermMask
	 *            目标对象的权限掩码
	 * @return true:允许
	 */
	public boolean isAllowAccess(AuthenticatedUser user,
			int targetObjectPermMask);

	/**
	 * 计算高一级的权限码
	 * @param permMask
	 * @return
	 */
	public int getHigherPermMask(int permMask);
	
	/**
	 * 计算低一级的权限码
	 * @param permMask
	 * @return
	 */
	public int getLowerPermMask(int permMask);
	
	/**
	 * 获取最低的权限码
	 * @return
	 */
	public int getLowestPermMask();
	/**
	 * 是否允许访问目标
	 * 
	 * @param user
	 *            用户
	 * @param targetObject
	 *            目标对象
	 * @return
	 */
	public boolean isAllowAccess(AuthenticatedUser user,
			AccessCtrolable targetObject);
	
	/**
	 * 获取当前的用户
	 * @return
	 */
	public AuthenticatedUser getCurrentUser() throws GISException;
}
