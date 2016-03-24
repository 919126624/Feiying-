/**
 * com.wuyizhiye.hr.attendance.service.impl.OverTimeServiceImpl.java
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
import com.wuyizhiye.hr.attendance.dao.OverTimeDao;
import com.wuyizhiye.hr.attendance.model.OverTime;
import com.wuyizhiye.hr.attendance.service.OverTimeService;

/**
 * service
 * @author hyl
 * 
 * @since 2013-11-28
 */
@Component(value="overTimeService")
@Transactional
public class OverTimeServiceImpl extends BaseServiceImpl<OverTime> implements OverTimeService {
	@Autowired
	private OverTimeDao overTimeDao;
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	protected BaseDao getDao() {
		return overTimeDao;
	}	
	/**
	 *加班申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveOverTime(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			OverTime overTime = overTimeDao.getEntityById(billId);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				overTime.setBillStatus(BillStatusEnum.APPROVED);
				overTime.setEffectdate(new Date());
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				overTime.setBillStatus(BillStatusEnum.REVOKE);
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				overTime.setBillStatus(BillStatusEnum.REJECT);
			}
			overTime.setLastUpdateTime(new Date());
			overTime.setUpdator(SystemUtil.getCurrentUser());
			overTime.setAuditDate(new Date());
			overTimeDao.updateEntity(overTime);
		}
		return resultMsg;
	}
	
	
	/**
	 * 加班申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveOverTime(String billId ){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		OverTime overTime = overTimeDao.getEntityById(billId);
		//已审核
		overTime.setEffectdate(new Date());
		overTime.setBillStatus(BillStatusEnum.APPROVED);
		overTime.setLastUpdateTime(new Date());
		overTime.setUpdator(SystemUtil.getCurrentUser());
		overTime.setAuditDate(new Date());
		overTimeDao.updateEntity(overTime);
		return resultMsg;
	}
	@Override
	public void addEntity(OverTime entity) {
		if(null!=entity.getApplyPerson()){
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(OverTime entity) {
		if(null!=entity.getApplyPerson()){
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.updateEntity(entity);
	}
}
