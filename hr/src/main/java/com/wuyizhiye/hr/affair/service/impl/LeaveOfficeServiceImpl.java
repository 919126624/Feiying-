/**
 * com.wuyizhiye.hr.affair.service.impl.LeaveOfficeServiceImpl.java
 */
package com.wuyizhiye.hr.affair.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.dao.LeaveOfficeDao;
import com.wuyizhiye.hr.affair.model.LeaveOffice;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.LeaveOfficeService;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * @author Cai.xing
 * @since 2013-04-08
 */
@Component(value="leaveOfficeService")
@Transactional
public class LeaveOfficeServiceImpl extends DataEntityService<LeaveOffice> implements LeaveOfficeService {
	
	private Logger log = Logger.getLogger(LeaveOfficeServiceImpl.class);
	
	@Autowired
	private LeaveOfficeDao leaveOfficeDao;
	@Autowired 
	private BasicDataService basicDataService;
	@Autowired
	private PositionHistoryBillService positionHistoryBillService;
	@Autowired 
	PersonService personService;
	@Autowired
	private PersonPositionService personPositionService;
	@Autowired
	private ProcessViewService processViewService;
	
	 @Autowired
	 private PersonDao personDao;
	 
	@Override
	protected BaseDao getDao() {
		return leaveOfficeDao;
	}
	
	/**
	 * 审核
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approvalBill(String billId ){
		Map<String,Object> resultMap = new HashMap<String, Object>(); 
		resultMap.put("STATE", "SUCCESS");
		resultMap.put("MSG", "审批通过！");
		LeaveOffice leaveOffice = leaveOfficeDao.getEntityById(billId);
		if(leaveOffice!=null){
			//如果生效日期 小于等于当前日期 则即刻修改人员信息
			if(leaveOffice.getValidateTime()!=null && leaveOffice.getValidateTime().compareTo(new Date())<=0){
				 
				resultMap = approvalLeaveBill(billId);
				
				
			}
			
			/*else{
				 
				//人员离职 定时器  写任职历史和修改人员(岗位状态)信息
				String description = "人员离职 定时器  写任职历史和修改人员(岗位状态)信息";
				TimingTaskUtil.createTask("人员离职", description, leaveOffice.getValidateTime(),
						"leaveOfficeService.approveSchedule(billId)", leaveOffice.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
			}*/
			
			leaveOffice.setBillStatus(BillStatusEnum.APPROVED);
			leaveOfficeDao.updateEntity(leaveOffice);
			this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
		}else{
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "单据已经不存在，请刷新后重试！");
		}
		
	
		return resultMap;
	}
	
	/**
	 * 人员离职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId){
		if(!StringUtils.isEmpty(billId)){
		 
			approvalLeaveBill(billId);
			 
		}
		
	}
	
	/**
	 * 审核
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approvalLeaveBill(String billId ){
		Map<String,Object> resultMap = new HashMap<String, Object>(); 
		LeaveOffice leaveOffice = leaveOfficeDao.getEntityById(billId);
		if(leaveOffice!=null){
		/*	try {*/
				//离职
				Person per = personService.getEntityById(leaveOffice.getApplyPerson().getId());
				per.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.DIMISSION.getValue()));
				per.setLeaveDate(leaveOffice.getValidateTime());
				
				PositionHistoryBill bill = new PositionHistoryBill();
				
				bill.setApplyPerson(leaveOffice.getApplyPerson());
				bill.setApplyOrg(leaveOffice.getApplyOrg());
				bill.setApplyChangeOrg(null);
				bill.setApplyPosition(leaveOffice.getApplyPosition());
				bill.setApplyJoblevel(leaveOffice.getApplyJoblevel());
				bill.setBillNumber(leaveOffice.getNumber());
				bill.setChangeType(PositionChangeTypeEnum.LEAVE.getValue());
				bill.setPrimary(true);
				bill.setTakeOfficeDate(leaveOffice.getApplyPerson().getInnerDate());
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsdisable("1");
				
				bill.setEffectdate(leaveOffice.getValidateTime());
				
				bill.setHandOverPerson(leaveOffice.getGivePerson());//离职交接人 by hlz
				bill.setCreator(leaveOffice.getCreator());
				bill.setUpdator(leaveOffice.getUpdator());
				bill.setJobStatus(per.getJobStatus());//设置申请人岗位状态
				
				this.leaveOffice(per,bill);
				 
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
	
	public void leaveOffice(Person per,PositionHistoryBill bill){
		//维护主要职位 任职历史
		positionHistoryBillService.updatePositionHistoryByBill(bill);
		positionHistoryBillService.addPositionHistoryByBill(bill);
		//维护兼职 任职历史   兼职失效日期改为离职日期
		Map<String,Object> param = new HashMap<String, Object>(); 
		param.put("personId", bill.getApplyPerson().getId());
		param.put("expirydate", bill.getEffectdate());
		queryExecutor.executeUpdate("com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao.updateExpirydate4Leave", param);
		personService.updateEntity(per);
		//删除任职信息
		//根据人员删除任职信息
		personPositionService.deleteByPersonId(per.getId());
		
		/**
		 * 将用户同步到微信企业号 begin
		 */
	    try{
	    	
	    	String strJson = "{";
	        strJson += "\"userid\":\""+per.getNumber()+"\"";
	        strJson += "}";
	        
		    HttpClientUtil.callHttpUrl(SystemUtil.getBase()+"/weixinapi/weixinperson?t=del", URLEncoder.encode(strJson,"utf-8"));
		     
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    /**
		 * 将用户同步到微信企业号 end
		 */	
	}

	@Override
	public void addEntity(LeaveOffice entity) {
		Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
		entity.setControlUnit(person.getControlUnit());
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(LeaveOffice entity) {
		Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
		entity.setControlUnit(person.getControlUnit());
		super.updateEntity(entity);
	}
}
