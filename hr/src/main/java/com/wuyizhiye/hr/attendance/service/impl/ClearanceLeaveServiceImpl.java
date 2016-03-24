/**
 * com.wuyizhiye.hr.attendance.service.impl.ClearanceLeaveServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.attendance.dao.ClearanceLeaveDao;
import com.wuyizhiye.hr.attendance.dao.LeaveDao;
import com.wuyizhiye.hr.attendance.model.ClearanceLeave;
import com.wuyizhiye.hr.attendance.model.Leave;
import com.wuyizhiye.hr.attendance.service.ClearanceLeaveService;

/**
 * 组织service
 * @author FengMy
 * 
 * @since 2012-9-7
 */
@Component(value="clearanceLeaveService")
@Transactional
public class ClearanceLeaveServiceImpl extends BaseServiceImpl<ClearanceLeave> implements ClearanceLeaveService {
	@Autowired
	private ClearanceLeaveDao clearanceLeaveDao;
	@Autowired
	private ProcessViewService processViewService;
	
	@Autowired
	private LeaveDao leaveDao;
	
	@Override
	protected BaseDao getDao() {
		return clearanceLeaveDao;
	}
	/**
	 *销假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveClearanceLeave(String[] billIds, String approveType) {
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			ClearanceLeave clearnceLeave = clearanceLeaveDao.getEntityById(billId);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				clearnceLeave.setLeaveClearanceStatus(BillStatusEnum.APPROVED);
				clearnceLeave.setEffectdate(new Date());
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				clearnceLeave.setLeaveClearanceStatus(BillStatusEnum.REVOKE);
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				clearnceLeave.setLeaveClearanceStatus(BillStatusEnum.REJECT);
			}
			clearnceLeave.setAuditDate(new Date());
			clearnceLeave.setLastUpdateTime(new Date());
			clearnceLeave.setUpdator(SystemUtil.getCurrentUser());
			clearanceLeaveDao.updateEntity(clearnceLeave);
		}
		return resultMsg;
	}
	/**
	 * 请假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveClearanceLeave(String billId) {
		// TODO Auto-generated method stub
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		ClearanceLeave clearnceLeave = clearanceLeaveDao.getEntityById(billId);
		//已审核
		clearnceLeave.setLeaveClearanceStatus(BillStatusEnum.APPROVED);
		clearnceLeave.setEffectdate(new Date());
		clearnceLeave.setAuditDate(new Date());
		clearnceLeave.setLastUpdateTime(new Date());
		clearnceLeave.setUpdator(SystemUtil.getCurrentUser());
		clearanceLeaveDao.updateEntity(clearnceLeave);
		return resultMsg;
	}
	@Override
	public void addEntity(ClearanceLeave entity) {
		if(null!=entity.getLeave()){
			Leave leave = leaveDao.getEntityById(entity.getLeave().getId());
			entity.setControlUnit(leave.getControlUnit());
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(ClearanceLeave entity) {
		if(null!=entity.getLeave()){
			Leave leave = leaveDao.getEntityById(entity.getLeave().getId());
			entity.setControlUnit(leave.getControlUnit());
		}
		super.updateEntity(entity);
	}	
}
