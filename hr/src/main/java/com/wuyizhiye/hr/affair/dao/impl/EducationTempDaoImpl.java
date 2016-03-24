package com.wuyizhiye.hr.affair.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.EducationTempDao;
import com.wuyizhiye.hr.affair.model.Education;

/**
 *教育经历DaoImpl 
 * @author Cai.xing
 * @since 2013-04-03
 *
 */
@Component(value="educationTempDao")
public class EducationTempDaoImpl extends BaseDaoImpl implements EducationTempDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.EducationTempDao";
	}

	@Override
	public List<Education> getByPersonId(String personId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("personId", personId);
		return getSqlSession().selectList(getNameSpace() + ".selectList", param);
	}

	@Override
	public void deleteByPersonId(String personId) {
		this.getSqlSession().delete(this.getNameSpace() + ".deleteByPersonId", personId);
	}

}
