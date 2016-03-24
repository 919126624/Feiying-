/**
 * com.wuyizhiye.hr.attendance.dao.impl.LeaveRuleDaoImpl.java
 */
package com.wuyizhiye.hr.attendance.dao.impl;


import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.attendance.dao.LeaveRuleDao;
import com.wuyizhiye.hr.attendance.model.LeaveRule;

/**
 * @author FengMy
 * 
 * @since 2013-11-21
 */
@Component(value="leaveRuleDao")
public class LeaveRuleDaoImpl extends BaseDaoImpl implements LeaveRuleDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.attendance.dao.LeaveRuleDao";
	}
	@SuppressWarnings("unchecked")
	@Override
	public LeaveRule getEntityByOrgId(String id) {
		// TODO Auto-generated method stub
		return (LeaveRule) getSqlSession().selectOne(getNameSpace()+".getByOrgId", id);
	}
}
