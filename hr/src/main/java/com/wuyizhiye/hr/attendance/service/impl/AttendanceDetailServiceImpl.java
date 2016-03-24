/**
 * com.wuyizhiye.hr.affair.service.impl.AttendanceDetailServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.attendance.dao.AttendanceDetailDao;
import com.wuyizhiye.hr.attendance.model.AttendanceDetail;
import com.wuyizhiye.hr.attendance.service.AttendanceDetailService;

/**
 * 考勤明细
 * @author ouyangyi
 * @since 2013-5-17 下午03:22:01
 */
@Component(value="attendanceDetailService")
@Transactional
public class AttendanceDetailServiceImpl extends BaseServiceImpl<AttendanceDetail> implements AttendanceDetailService {
	
	@Resource
	private AttendanceDetailDao attendanceDetailDao;
	
	@Override
	protected BaseDao getDao() {
		return attendanceDetailDao;
	}	
}
