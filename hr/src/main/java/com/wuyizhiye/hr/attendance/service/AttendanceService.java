package com.wuyizhiye.hr.attendance.service;

import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.attendance.model.Attendance;
import com.wuyizhiye.hr.attendance.model.AttendanceDetail;


/**
 * 考勤service
 * @author ouyangyi
 * @since 2013-5-17 下午03:14:37
 */
public interface AttendanceService extends BaseService<Attendance> {
	
	
	/**
	 * 考勤录入 查询考勤人员(登录人组织下的人员)
	 * @param cond
	 * @return
	 */
	public Map<String, Object> query(Map<String, Object> cond);
	
	/**
	 * 保存录入信息
	 * @throws ParseException 
	 */
	public Map<String, Object> saveEntry(Map<String, Object> data) throws Exception;
	
	/**
	 * 月考勤查询（查询指定期间内的统计情况） -- 分页
	 */
	public Map<String, Object> queryMonthByCondPerPage(Pagination<Attendance> pagination,Map<String, Object> cond);
	
	/**
	 * 月考勤查询（查询指定期间内的统计情况）--全部  不分页
	 */
	public Map<String, Object> queryMonthByCond(Map<String, Object> cond);
	
	/**
	 * 月考勤导出Excel
	 * @param cond
	 * @param os
	 * @throws Exception
	 */
	public void exportMonthByCond(Map<String, Object> cond,OutputStream os) throws Exception;
	
	/**
	 * 修改考勤信息 状态（提交、驳回、审核）
	 * @param attendanceIds
	 * @param approveType
	 * @return
	 */
	public String updateAttendanceState(List<Attendance> attendances,String approveType,String approvalRemark);
	
	/**
	 * 查询月考勤明细 及汇总 
	 * @param personId
	 * @param period
	 */
	public List<AttendanceDetail> getAttendanceDetails(Attendance a);
}
