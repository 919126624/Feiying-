/**
\ * com.wuyizhiye.hr.attendance.service.impl.LeaveServiceImpl.java
 */
package com.wuyizhiye.hr.attendance.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.attendance.dao.LeaveDao;
import com.wuyizhiye.hr.attendance.model.Leave;
import com.wuyizhiye.hr.attendance.service.LeaveService;

/**
 * 组织service
 * @author FengMy
 * 
 * @since 2012-9-7
 */
@Component(value="leaveService")
@Transactional
public class LeaveServiceImpl extends BaseServiceImpl<Leave> implements LeaveService {
	@Autowired
	private LeaveDao leaveDao;
	@Autowired
	private ProcessViewService processViewService;
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	protected BaseDao getDao() {
		return leaveDao;
	}	
	
	/**
	 *请假申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveAsk4Leave(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			Leave leave = leaveDao.getEntityById(billId);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
			leave.setAsk4LeaveStatus(BillStatusEnum.APPROVED);
			leave.setEffectdate(new Date());
			this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				leave.setAsk4LeaveStatus(BillStatusEnum.REVOKE);
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				leave.setAsk4LeaveStatus(BillStatusEnum.REJECT);
			}
			leave.setAuditDate(new Date());
			leave.setLastUpdateTime(new Date());
			leave.setUpdator(SystemUtil.getCurrentUser());
			leaveDao.updateEntity(leave);
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
	public String approveAsk4Leave(String billId ){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		Leave leave = leaveDao.getEntityById(billId);
		//已审核
		leave.setAsk4LeaveStatus(BillStatusEnum.APPROVED);
		leave.setEffectdate(new Date());
		leave.setAuditDate(new Date());
		leave.setLastUpdateTime(new Date());
		leave.setUpdator(SystemUtil.getCurrentUser());
		leaveDao.updateEntity(leave);
		return resultMsg;
	}

	@Override
	public void addEntity(Leave entity) {
		if(null!=entity.getApplyPerson()){
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(Leave entity) {
		if(null!=entity.getApplyPerson()){
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.updateEntity(entity);
	}
}
