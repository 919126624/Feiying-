package com.wuyizhiye.hr.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.dao.WorkExperienceDao;
import com.wuyizhiye.hr.model.WorkExperience;

/**
 * 工作经历Daoimpl
 * @author taking.wang
 * @since 2013-01-18
 */
@Component(value="workExperienceDao")
public class WorkExperienceDaoImpl extends BaseDaoImpl implements
		WorkExperienceDao {

	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.hr.dao.WorkExperienceDao";
	}

	@Override
	public List<WorkExperience> getByPersonId(String personId) {
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
