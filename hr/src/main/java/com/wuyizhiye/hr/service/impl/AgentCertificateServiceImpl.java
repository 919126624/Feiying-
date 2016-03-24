package com.wuyizhiye.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.dao.AgentCertificateDao;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.service.AgentCertificateService;

/**
 * 经纪人证serviceImpl
 * @author taking.wang
 * @since 2013-01-18
 */
@Component(value="agentCertificateService")
@Transactional
public class AgentCertificateServiceImpl extends BaseServiceImpl<AgentCertificate> implements AgentCertificateService{

	@Autowired
	private AgentCertificateDao agentCertificateDao;
	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return agentCertificateDao;
	}
	@Override
	public List<AgentCertificate> getByPersonId(String personId) {
		// TODO Auto-generated method stub
		return agentCertificateDao.getByPersonId(personId);
	}

}
