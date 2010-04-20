package com.emq.model;
 
/**
 * 通过R1认证的用户
 * @author guqiong
 * @created 2009-10-29
 */
public class R1AuthenticatedUser implements AuthenticatedUser {
 
	private static final long serialVersionUID = 1L;

	/**
	 * 用户组织机构编号
	 */
	private String orgCode;	
	/**
	 * 权限掩码
	 */
	private Integer permissonMask;	
 
	
	public R1AuthenticatedUser(String orgCode, int permissonMask){
		this.orgCode = orgCode;
		this.permissonMask = permissonMask;
	}
	
	public R1AuthenticatedUser(){
		
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	public Integer getPermissonMask() {
		return permissonMask;
	}
	public void setPermissonMask(Integer permissonMask) {
		this.permissonMask = permissonMask;
	}

	 
}
