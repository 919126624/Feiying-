/**
 * com.wuyizhiye.hr.affair.service.impl.AttendanceOrgServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceOrgDao;
import com.wuyizhiye.hr.attendance.model.AttendanceOrg;
import com.wuyizhiye.hr.attendance.service.AttendanceOrgService;

/**
 * 考勤组织方案
 * @author ouyangyi
 * @since 2013-11-20 上午10:21:33
 */
@Component(value="attendanceOrgService")
@Transactional
public class AttendanceOrgServiceImpl extends BaseServiceImpl<AttendanceOrg> implements AttendanceOrgService {
	
	@Resource
	private AttendanceOrgDao attendanceOrgDao;
	
	@Override
	protected BaseDao getDao() {
		return attendanceOrgDao;
	}	
}
