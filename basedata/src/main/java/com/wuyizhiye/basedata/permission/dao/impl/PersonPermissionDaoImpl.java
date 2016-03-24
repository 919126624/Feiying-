package com.wuyizhiye.basedata.permission.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.PersonPermissionDao;

/**
 * @ClassName PersonPermissionDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPermissionDao")
public class PersonPermissionDaoImpl extends BaseDaoImpl implements
		PersonPermissionDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.PersonPermissionDao";
	}

	@Override
	public void deleteByPersonAndPosition(String person, String position) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person);
		param.put("position", position);
		getSqlSession().delete(getNameSpace() + ".deleteByPersonAndPosition", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getExistsPermission(Map<String, Object> param) {
		return getSqlSession().selectList(getNameSpace() + ".getExistsPermission", param);
	}
}
