package com.wuyizhiye.basedata.permission.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.permission.model.PersonPermission;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PersonPermissionService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPermissionService extends BaseService<PersonPermission> {
	
	/**
	 * 按人员和职位删除权限
	 * @param person
	 * @param position
	 */
	void deleteByPersonAndPosition(String person,String position);
	
	/**
	 * 保存
	 * @param person
	 * @param position
	 * @param permissionIds
	 */
	void savePersonPermissions(Person person,Position position,List<String> permissionIds);
	
	/**
	 * 批量删除人员权限
	 * @param ids
	 */
	void deletePersonPermissions(List<String> ids);
}
