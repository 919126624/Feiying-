/**
 * com.wuyizhiye.hr.salary.service.impl.DurationServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.DurationDao;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.service.DurationService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="durationService")
@Transactional
public class DurationServiceImpl extends BaseServiceImpl<Duration> implements DurationService {
	@Autowired
	private DurationDao durationDao;
	@Override
	protected BaseDao getDao() {
		return durationDao;
	}	
}
