package com.wuyizhiye.hr.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.dao.EducationDao;
import com.wuyizhiye.hr.model.Education;

/**
 *教育经历DaoImpl 
 * @author taking.wang
 * @since 2013-01-18
 *
 */
@Component(value="educationDao")
public class EducationDaoImpl extends BaseDaoImpl implements EducationDao {

	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.dao.EducationDao";
	}

	@Override
	public List<Education> getByPersonId(String personId) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("personId", personId);
		return getSqlSession().selectList(getNameSpace() + ".selectList", param);
	}

	@Override
	public void deleteByPersonId(String personId) {
		// TODO Auto-generated method stub
		this.getSqlSession().delete(this.getNameSpace() + ".deleteByPersonId", personId);
	}

}
