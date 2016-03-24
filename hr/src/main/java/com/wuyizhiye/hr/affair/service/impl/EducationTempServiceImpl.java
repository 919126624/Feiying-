package com.wuyizhiye.hr.affair.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.affair.dao.EducationTempDao;
import com.wuyizhiye.hr.affair.model.Education;
import com.wuyizhiye.hr.affair.service.EducationTempService;

/**
 *教育经历
 * @author Cai.xing
 * @since 2013-04-03
 *
 */
@Component(value="educationTempService")
@Transactional
public class EducationTempServiceImpl extends BaseServiceImpl<Education> implements
		EducationTempService {

	@Autowired
	private EducationTempDao educationDao;
	@Override
	protected BaseDao getDao() {
		return educationDao;
	}
	@Override
	public List<Education> getByPersonId(String personId) {
		return this.educationDao.getByPersonId(personId);
	}


}
