package com.wuyizhiye.basedata.permission.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.permission.dao.PersonPermissionDao;
import com.wuyizhiye.basedata.permission.model.PermissionItem;
import com.wuyizhiye.basedata.permission.model.PersonPermission;
import com.wuyizhiye.basedata.permission.service.PersonPermissionService;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PersonPermissionServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPermissionService")
@Transactional
public class PersonPermissionServiceImpl extends
		BaseServiceImpl<PersonPermission> implements PersonPermissionService {
	@Autowired
	private PersonPermissionDao personPermissionDao;
	
	@Override
	public void deleteByPersonAndPosition(String person, String position) {
		personPermissionDao.deleteByPersonAndPosition(person, position);
	}

	@Override
	protected BaseDao getDao() {
		return personPermissionDao;
	}

	@Override
	public void savePersonPermissions(Person person, Position position,
			List<String> permissionIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("person", person.getId());
		param.put("position", position.getId());
		List<String> existsPermission = personPermissionDao.getExistsPermission(param );
		List<PersonPermission> pps = new ArrayList<PersonPermission>();
		for(String id : permissionIds){
			if(!existsPermission.contains(id)){
				PermissionItem pi = new PermissionItem();
				pi.setId(id);
				PersonPermission pp = new PersonPermission();
				pp.setId(UUID.randomUUID().toString());
				pp.setPermissionItem(pi);
				pp.setPerson(person);
				pp.setPosition(position);
				pps.add(pp);
			}
		}
		personPermissionDao.addBatch(pps);
	}

	@Override
	public void deletePersonPermissions(List<String> ids) {
		personPermissionDao.deleteBatch(ids);
	}
}
