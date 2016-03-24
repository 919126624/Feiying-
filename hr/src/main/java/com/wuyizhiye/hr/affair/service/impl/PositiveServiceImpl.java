/**
 * com.wuyizhiye.hr.affair.service.impl.PositiveServiceImpl.java
 */
package com.wuyizhiye.hr.affair.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.dao.PositiveDao;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.model.Positive;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.affair.service.PositiveService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="positiveService")
@Transactional
public class PositiveServiceImpl extends DataEntityService<Positive> implements PositiveService {
	@Autowired
	private PositiveDao positiveDao;
	
	@Autowired
	private PositionHistoryBillService positionHistoryBillService;
	@Autowired 
	PersonService personService;
	@Autowired
	private ProcessViewService processViewService;
	
	@Override
	protected BaseDao getDao() {
		return positiveDao;
	}
	
	/**
	 * 审核
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approvalBill(String billId ){
		Map<String,Object> resultMap = new HashMap<String, Object>(); 
		try{
		resultMap.put("STATE", "SUCCESS");
		resultMap.put("MSG", "审批通过！");
		Positive positive = positiveDao.getEntityById(billId);
		if(positive!=null){
			//如果生效日期 小于等于当前日期 则即刻修改人员信息
			if(positive.getValidateTime()!=null && positive.getValidateTime().compareTo(new Date())<=0){
				 
				resultMap = approvalPositiveBill(billId);
				
			}
			
			/*else{
				 
				//人员转正 定时器  写任职历史和修改人员(岗位状态)信息
				String description = "人员转正 定时器  写任职历史和修改人员(岗位状态)信息";
				TimingTaskUtil.createTask("人员转正", description, positive.getValidateTime(),
						"positiveService.approveSchedule(billId)", positive.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
			}*/
			
			positive.setBillStatus(BillStatusEnum.APPROVED);
			positiveDao.updateEntity(positive);
			this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
		}else{
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "单据已经不存在，请刷新后重试！");
		}
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "系统异常！请联系管理员！");
		}
		return resultMap;
	}
	
	/**
	 * 人员转正 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId){
		if(!StringUtils.isEmpty(billId)){
		 
			approvalPositiveBill(billId);
			 
		}
		
	}
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	public Map<String,Object> approvalPositiveBill(String billId){
		Positive positive = positiveDao.getEntityById(billId);
		Map<String,Object> resultMap = new HashMap<String, Object>(); 
		if(positive!=null){
			/*try {*/
				 
				//转正后设置员工状态
			    positive.setIsEffective(1);//标记已生效
				Person per = personService.getEntityById(positive.getApplyPerson().getId());
				per.setPersonStatus(PersonStatusEnum.REGULAR);
				personService.updateEntity(per);
				PositionHistoryBill bill = new PositionHistoryBill();
				bill.setApplyPerson(positive.getApplyPerson());
				bill.setApplyOrg(positive.getApplyOrg());
				bill.setApplyChangeOrg(positive.getApplyOrg());
				bill.setApplyPosition(positive.getApplyPosition());
				bill.setApplyChangePosition(positive.getApplyPosition());
				bill.setApplyJoblevel(positive.getApplyJoblevel());
				bill.setApplyChangeJoblevel(positive.getApplyJoblevel());
				bill.setJobStatus(positive.getApplyPerson().getJobStatus());
				bill.setBillNumber(positive.getNumber());
				bill.setChangeType(PositionChangeTypeEnum.POSITIVE.getValue());
				bill.setPrimary(true);
				bill.setTakeOfficeDate(positive.getApplyPerson().getInnerDate());
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsdisable("1");
				bill.setEffectdate(positive.getValidateTime());
				bill.setCreator(positive.getCreator());
				bill.setUpdator(positive.getUpdator());
				bill.setJobStatus(per.getJobStatus());//设置申请人岗位状态
				positionHistoryBillService.updatePositionHistoryByBill(bill);
				positionHistoryBillService.addPositionHistoryByBill(bill);
				resultMap.put("STATE", "SUCCESS");
				resultMap.put("MSG", "审批通过！");
			/*} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("STATE", "FAIL");
				resultMap.put("MSG", "系统异常！请联系管理员！");
			}*/
		}else{
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "单据已经不存在，请刷新后重试！");
		}
		return resultMap;
	}
}
