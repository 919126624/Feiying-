/**
 * com.wuyizhiye.hr.affair.service.impl.ReinstatementServiceImpl.java
 */
package com.wuyizhiye.hr.affair.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.dao.ReinstatementDao;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.model.Reinstatement;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.affair.service.ReinstatementService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="reinstatementService")
@Transactional
public class ReinstatementServiceImpl extends DataEntityService<Reinstatement> implements ReinstatementService {
	@Autowired
	private ReinstatementDao reinstatementDao;

	@Autowired
	private PositionHistoryBillService positionHistoryBillService;
	@Autowired 
	PersonService personService;
	
	@Resource
	private PersonPositionDao personPositionDao;
	
	@Autowired 
	private BasicDataService basicDataService;
	
	@Autowired
	private ProcessViewService processViewService;

	 @Autowired
	 private PersonDao personDao;
	 
	@Override
	protected BaseDao getDao() {
		return reinstatementDao;
	}	
	
	/**
	 * 审核
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approveBill(String billId ){
		Map<String,Object> resultMap = new HashMap<String, Object>(); 
		try{
			resultMap.put("STATE", "SUCCESS");
			resultMap.put("MSG", "审批通过！");
			Reinstatement reinstatement = reinstatementDao.getEntityById(billId);
			if(reinstatement!=null){
				//如果生效日期 小于等于当前日期 则即刻修改人员信息
				if(reinstatement.getValidateTime()!=null && reinstatement.getValidateTime().compareTo(new Date())<=0){
					 
					resultMap = approveReinstatementBill(billId);
					
				}else{
					 
					//人员复职 定时器  写任职历史和修改人员(岗位状态)信息
					String description = "人员复职 定时器  写任职历史和修改人员(岗位状态)信息";
					TimingTaskUtil.createTask("人员复职", description, reinstatement.getValidateTime(),
							"reinstatementService.approveSchedule(billId)", reinstatement.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
				}
				reinstatement.setBillStatus(BillStatusEnum.APPROVED);
				reinstatementDao.updateEntity(reinstatement);
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
	 * 人员复职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId){
		if(!StringUtils.isEmpty(billId)){
		 
			approveReinstatementBill(billId);
			 
		}
		
	}
	
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	public Map<String,Object> approveReinstatementBill(String billId ){
		Map<String,Object> result = new HashMap<String, Object>();
		Reinstatement reinstatement = reinstatementDao.getEntityById(billId);
		if(reinstatement!=null){
			/*try {*/
				Person per = personService.getEntityById(reinstatement.getApplyPerson().getId());
				per.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.ONDUTY.getValue()));
				//per.setLeaveDate(reinstatement.getValidateTime());
				personService.updateEntity(per);
				PositionHistoryBill bill = new PositionHistoryBill();
				bill.setApplyPerson(reinstatement.getApplyPerson());
				bill.setApplyOrg(reinstatement.getApplyOrg());
				bill.setApplyChangeOrg(reinstatement.getApplyOrg());
				bill.setApplyPosition(reinstatement.getApplyPosition());
				bill.setApplyChangePosition(reinstatement.getApplyPosition());
				bill.setApplyJoblevel(reinstatement.getApplyJoblevel());
				bill.setApplyChangeJoblevel(reinstatement.getApplyChangeJoblevel());
				bill.setJobStatus(reinstatement.getApplyPerson().getJobStatus());
				bill.setBillNumber(reinstatement.getNumber());
				bill.setChangeType(PositionChangeTypeEnum.REINSTATEMENT.getValue());
				bill.setPrimary(true);
				bill.setTakeOfficeDate(reinstatement.getApplyPerson().getInnerDate());
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsdisable("1");
				bill.setEffectdate(reinstatement.getValidateTime());
				bill.setCreator(reinstatement.getCreator());
				bill.setUpdator(reinstatement.getUpdator());
				bill.setJobStatus(per.getJobStatus());//设置申请人岗位状态
				positionHistoryBillService.updatePositionHistoryByBill(bill);
				positionHistoryBillService.addPositionHistoryByBill(bill);
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("person", reinstatement.getApplyPerson().getId());
				PersonPosition pp = personPositionDao.getPerPositionByCondition(map);
				//
				if(pp!=null){
					pp.setJobLevel(reinstatement.getApplyChangeJoblevel());
					personPositionDao.updateEntity(pp);
				}
				result.put("STATE", "SUCCESS");
				result.put("MSG", "审批通过！");
			/*} catch (Exception e) {
				e.printStackTrace();
				result.put("STATE", "FAIL");
				result.put("MSG", "系统异常！请联系管理员！");
			}*/
		}else{
			result.put("STATE", "FAIL");
			result.put("MSG", "单据已经不存在，请刷新后重试！");
		}
		return result;
	}

	@Override
	public void addEntity(Reinstatement entity) {
		Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
		entity.setControlUnit(person.getControlUnit());
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(Reinstatement entity) {
		Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
		entity.setControlUnit(person.getControlUnit());
		super.updateEntity(entity);
	}
}
