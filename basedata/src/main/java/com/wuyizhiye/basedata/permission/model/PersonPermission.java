package com.wuyizhiye.basedata.permission.model;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PersonPermission
 * @Description 人员权限
 * @author li.biao
 * @date 2015-4-3
 */
public class PersonPermission extends CoreEntity {
	private static final long serialVersionUID = 463191709436761867L;
	
	private Person person;
	
	private Position position;
	
	private PermissionItem permissionItem;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public PermissionItem getPermissionItem() {
		return permissionItem;
	}

	public void setPermissionItem(PermissionItem permissionItem) {
		this.permissionItem = permissionItem;
	}
}
