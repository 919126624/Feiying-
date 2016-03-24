package com.wuyizhiye.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.dao.EducationDao;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.service.EducationService;

/**
 *教育经历ServiceImpl 
 * @author taking.wang
 * @since 2013-01-18
 *
 */
@Component(value="educationService")
@Transactional
public class EducationServiceImpl extends BaseServiceImpl<Education> implements
		EducationService {

	@Autowired
	private EducationDao educationDao;
	@Override
	protected BaseDao getDao() {
		return educationDao;
	}
	@Override
	public List<Education> getByPersonId(String personId) {
		return this.educationDao.getByPersonId(personId);
	}
	@Override
	public void deleteByPersonId(String personId) {
		educationDao.deleteByPersonId(personId);
	}


}
