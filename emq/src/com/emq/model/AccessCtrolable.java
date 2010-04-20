package com.emq.model;

/**
 * 可以被DataAccessController管理的对象接口
 * @author guqiong
 * @created 2009-10-29
 */
public interface AccessCtrolable {
	/**
	 * 获取访问此对象所需的权限掩码
	 * @return
	 */
	public int getRequiredPermissonMask();
}
