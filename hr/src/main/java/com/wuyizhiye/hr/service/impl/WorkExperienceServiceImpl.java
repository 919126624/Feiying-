package com.wuyizhiye.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.dao.WorkExperienceDao;
import com.wuyizhiye.hr.model.WorkExperience;
import com.wuyizhiye.hr.service.WorkExperienceService;

/**
 * 工作经历serviceimpl
 * @author taking.wang
 * @since 2013-01-18
 */
@Component(value="workExperienceService")
@Transactional
public class WorkExperienceServiceImpl extends BaseServiceImpl<WorkExperience> implements
		WorkExperienceService {

	@Autowired
	private WorkExperienceDao workExperienceDao;
	@Override
	protected BaseDao getDao() {
		return workExperienceDao;
	}
	@Override
	public List<WorkExperience> getByPersonId(String personId) {
		return this.workExperienceDao.getByPersonId(personId);
	}
	@Override
	public void deleteByPersonId(String personId) {
		workExperienceDao.deleteByPersonId(personId);
	}

}
