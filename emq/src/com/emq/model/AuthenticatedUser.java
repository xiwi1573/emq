package com.emq.model;

/**
 * 已验证用户
 * @author guqiong
 * @created 2009-10-29
 */
public interface  AuthenticatedUser  {
	/**
	 * 获取用户具有的权限掩码
	 * @return 
	 */
	public Integer getPermissonMask();
	/**
	 * 获取用户所在的组织机构编码
	 * @return
	 */
	public String getOrgCode();
}
