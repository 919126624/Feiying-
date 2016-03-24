/**
 * com.wuyizhiye.hr.attendance.dao.LeaveRuleDao.java
 */
package com.wuyizhiye.hr.attendance.dao;


import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.attendance.model.LeaveRule;

/**
 * @author System
 *
 * @since 2013-11-21
 */
public interface LeaveRuleDao extends BaseDao {
	 LeaveRule getEntityByOrgId(String id);
}
