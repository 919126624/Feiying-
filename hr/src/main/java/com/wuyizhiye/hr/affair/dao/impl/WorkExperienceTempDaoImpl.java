package com.wuyizhiye.hr.affair.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.WorkExperienceTempDao;
import com.wuyizhiye.hr.affair.model.WorkExperience;

/**
 * 工作经历
 * @author Cai.xing
 * @since 2013-04-03
 */
@Component(value="workExperienceTempDao")
public class WorkExperienceTempDaoImpl extends BaseDaoImpl implements
		WorkExperienceTempDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.WorkExperienceTempDao";
	}

	@Override
	public List<WorkExperience> getByPersonId(String personId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("personId", personId);
		return getSqlSession().selectList(getNameSpace() + ".selectList", param);
	}

	@Override
	public void deleteByPersonId(String personId) {
		this.getSqlSession().delete(this.getNameSpace() + ".deleteByPersonId", personId);
	}

}
