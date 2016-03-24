/**
 * com.wuyizhiye.hr.attendance.service.impl.LeaveRuleServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.hr.attendance.dao.LeaveRuleDao;
import com.wuyizhiye.hr.attendance.model.LeaveRule;
import com.wuyizhiye.hr.attendance.service.LeaveRuleService;

/**
 * 组织service
 * @author FengMy
 * 
 * @since 2012-9-7
 */
@Component(value="leaveRuleService")
@Transactional
public class LeaveRuleServiceImpl extends BaseServiceImpl<LeaveRule> implements LeaveRuleService {
	@Autowired
	private LeaveRuleDao leaveRuleDao;
	@Override
	protected BaseDao getDao() {
		return leaveRuleDao;
	}
	@SuppressWarnings("unchecked")
	@Override
	public LeaveRule getEntityByOrgId(String id) {
		// TODO Auto-generated method stub
		 return leaveRuleDao.getEntityByOrgId(id);
	}
	@Override
	public void addEntity(LeaveRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(LeaveRule entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.updateEntity(entity);
	}	
}
