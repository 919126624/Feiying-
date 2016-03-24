package com.wuyizhiye.hr.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.model.AgentCertificate;

/**
 * 经纪人证service
 * @author taking.wang
 * @since 2013-01-18
 */
public interface AgentCertificateService extends BaseService<AgentCertificate>{
	public List<AgentCertificate> getByPersonId(String personId);
}
