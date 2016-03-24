/**
 * com.wuyizhiye.hr.attendance.service.impl.ScheduleRuleServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.hr.attendance.dao.ScheduleRuleDao;
import com.wuyizhiye.hr.attendance.model.ScheduleRule;
import com.wuyizhiye.hr.attendance.service.ScheduleRuleService;

/**
 * 组织service
 * @author FengMy
 * 
 * @since 2012-9-7
 */
@Component(value="scheduleRuleService")
@Transactional
public class ScheduleRuleServiceImpl extends BaseServiceImpl<ScheduleRule> implements ScheduleRuleService {
	@Autowired
	private ScheduleRuleDao scheduleRuleDao;
	@Override
	protected BaseDao getDao() {
		return scheduleRuleDao;
	}
	@Override
	public void addEntity(ScheduleRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(ScheduleRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.updateEntity(entity);
	}	
	
	
}
