package com.wuyizhiye.basedata.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.dao.JobEtcDao;
import com.wuyizhiye.basedata.org.model.JobEtc;
import com.wuyizhiye.basedata.org.service.JobEtcService;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName JobEtcServiceImpl
 * @Description 组织service
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobEtcService")
@Transactional
public class JobEtcServiceImpl extends DataEntityService<JobEtc> implements JobEtcService {
	@Autowired
	private JobEtcDao jobEtcDao;
	@Override
	protected BaseDao getDao() {
		return jobEtcDao;
	}
	@Override
	public void addEntity(JobEtc entity) {
		entity.setStatus(UserStatusEnum.ENABLE);
		super.addEntity(entity);
	}	
	
}