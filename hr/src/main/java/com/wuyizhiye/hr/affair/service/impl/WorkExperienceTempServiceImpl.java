package com.wuyizhiye.hr.affair.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.affair.dao.WorkExperienceTempDao;
import com.wuyizhiye.hr.affair.model.WorkExperience;
import com.wuyizhiye.hr.affair.service.WorkExperienceTempService;
/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="workExperienceTempService")
@Transactional
public class WorkExperienceTempServiceImpl extends BaseServiceImpl<WorkExperience> implements
		WorkExperienceTempService {

	@Autowired
	private WorkExperienceTempDao workExperienceDao;
	@Override
	protected BaseDao getDao() {
		return workExperienceDao;
	}
	@Override
	public List<WorkExperience> getByPersonId(String personId) {
		return this.workExperienceDao.getByPersonId(personId);
	}

}
