package com.wuyizhiye.basedata.person.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.model.PersonPosition;

/**
 * @ClassName PersonPositionDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositioniDao")
public class PersonPositionDaoImpl extends BaseDaoImpl implements
		PersonPositionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonPosition> getByPerson(String person) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}

	@Override
	public void deleteByPerson(String person) {
		getSqlSession().delete(getNameSpace() + ".deleteByPerson" , person);
	}
	
	public void deleteByCondition(Map<String,Object> params) {
		getSqlSession().delete(getNameSpace() + ".deleteByCondition" , params);
	}

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.PersonPositionDao";
	}

	@Override
	public PersonPosition getPerPositionByCondition(Map map) {		
		map.put("primary", 1);//放入主要职位字段,不然导致太多结果的异常
		return (PersonPosition) this.getSqlSession().selectOne(getNameSpace() + ".select", map);
	}

	@Override
	public List<PersonPosition> getPerPositionListByCondition(Map map) {
		
		return this.getSqlSession().selectList(getNameSpace() + ".select", map);
	}

}
