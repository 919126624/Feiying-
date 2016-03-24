package com.wuyizhiye.hr.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.model.AgentCertificate;

/**
 * 经纪人证dao
 * @author taking.wang
 * @since 2013-01-18
 */
public interface AgentCertificateDao extends BaseDao {

	List<AgentCertificate> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);
}
