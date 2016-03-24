package com.wuyizhiye.hr.affair.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.org.dao.JobLevelDao;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.dao.PositionDao;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.dao.PositionHistoryBillDao;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.affair.service.ProcessInfoService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

@Component(value="positionHistoryBillService")
public class PositionHistoryBillServiceImpl extends  BaseServiceImpl<PositionHistoryBill> implements PositionHistoryBillService{

	static final String JOBNUMBER = "ZYGW";//置业顾问
	static final String JOBNUMBERXS = "LDXSDB";//LDXSDB 销售代表 
	static final String KXXSDB = "KXXSDB";//KXXSDB 快销销售代表 
	static final String JOBLEVEL = "PPY";
	
	 @Resource
	 private PositionHistoryBillDao positionHistoryBillDao;
	 @Resource
	 private ProcessInfoService processInfoService;
	 
	 @Resource
	 private PersonPositionHistoryDao personPositionHistoryDao;
	 
	 @Resource
	 private PersonDao personDao;
	 
	 @Resource
	 private PositionDao positionDao;
	 
	 @Resource
	 private JobLevelDao jobLevelDao;
	 
	 @Resource
	 private PersonPositionDao personPositionDao;
	 
	 @Resource
	 private BasicDataService basicDataService;
	 
	 @Resource
	 private OrgDao orgDao;
	 
	 @Resource
	 private QueryExecutor queryExecutor;
	 
	 @Autowired
	 private ProcessViewService processViewService;
	 
	 @Autowired
	 private PersonService personService;
	 
	 @Override
	protected BaseDao getDao() {
		return positionHistoryBillDao;
	}
	
	public List<PositionHistoryBill> selectPositionHistoryBills(Map<String,Object> params){
		return positionHistoryBillDao.selectPositionHistoryBills(params);
	}
	
	public void insertPositionHistoryBill(PositionHistoryBill pstHisBill){
		positionHistoryBillDao.insertPositionHistoryBill(pstHisBill);
	}
	
	public void updatePositionHistoryBillSelective(PositionHistoryBill pstHisBill,PositionHistoryBill pstHisBillParam){
		positionHistoryBillDao.updatePositionHistoryBillSelective(pstHisBill, pstHisBillParam);
	}
	
	public void deletePositionHistoryBillById(String personPositionHistoryId){
		 
		positionHistoryBillDao.deletePositionHistoryBillById(personPositionHistoryId);
	}
	
	public void deletePositionHistoryBillSelective(PositionHistoryBill pstHisBillParam){
		 
		positionHistoryBillDao.deletePositionHistoryBillSelective(pstHisBillParam);
	}
	
	/**
	 * 根据主键修改调职申请对象(只修改不为空的属性)
	 * @param pstHisBill
	 */
	public void updateHistoryBillByPrimaryKeySelective(PositionHistoryBill pstHisBill){
		positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(pstHisBill);
	}
	
	/**
	 * 调职单(调职、降职、晋升)审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approve(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			PositionHistoryBill bill = new PositionHistoryBill();
			bill.setId(billId);
			bill.setPrimary(true);//主要职务
			BaseUtils.setWho(bill, true);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsEffective(1);//标记已生效
				
				//审核完成写任职历史
				PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
				
				//如果生效日期 小于等于当前日期 则即刻修改人员信息
				if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
					//根据调职申请单 维护任职历史失效日期
					updatePositionHistoryByBill(phBill);
					
					//根据调职申请单 写任职历史记录
					addPositionHistoryByBill(phBill);
					
					//审核完成 根据调职申请单 回写人员组织、职级信息信息
					updatePersonPositionByBill(phBill);
					taskSyncCustomerOrg();
				}
				/*else{
					//主要职位调动 定时器  写任职历史和修改人员职位信息
					String description = "职位调动 定时器  写任职历史和修改人员职位信息";
					TimingTaskUtil.createTask("职位调动", description, phBill.getEffectdate(),
							"positionHistoryBillService.approveSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
					taskSyncCustomerOrg();
				}*/
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				bill.setBillStatus(BillStatusEnum.REVOKE.getValue());
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				bill.setBillStatus(BillStatusEnum.REJECT.getValue());
			}
			positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(bill);
		}
		return resultMsg ;
	}
	
	/**
	 * 同步人员调岗后意向客的组织
	 */
	public void taskSyncCustomerOrg(){
		Object intentionCustomerService = null;
		try {
			intentionCustomerService = ApplicationContextAware.getApplicationContext().getBean("intentionCustomerService");
			Method method = intentionCustomerService.getClass().getMethod("taskSyncCustomerOrg");
			method.invoke(intentionCustomerService);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 调职单(调职、降职、晋升)  单个审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveBill(String billId){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
	 
		PositionHistoryBill bill = new PositionHistoryBill();
		bill.setId(billId);
		bill.setPrimary(true);//主要职务
		BaseUtils.setWho(bill, true);
		 
		//已审核
		bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
		bill.setIsEffective(1);//标记已生效
		//审核完成写任职历史
		PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
		//如果生效日期 小于等于当前日期 则即刻修改人员信息
		if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
			//根据调职申请单 维护任职历史失效日期
			updatePositionHistoryByBill(phBill);
			
			//根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//审核完成 根据调职申请单 回写人员组织、职级信息信息
			updatePersonPositionByBill(phBill);
			taskSyncCustomerOrg();
		}
		
		/*else{
			//主要职位调动 定时器  写任职历史和修改人员职位信息
			String description = "职位调动 定时器  写任职历史和修改人员职位信息";
			TimingTaskUtil.createTask("职位调动", description, phBill.getEffectdate(),
					"positionHistoryBillService.approveSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
			taskSyncCustomerOrg();
		}*/
		 
		positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(bill);
		return resultMsg ;
	}
	
	/**
	 * 调职单(调职、降职、晋升)  反审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String backApprove(String billId ,String approveType){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		PositionHistoryBill bill = positionHistoryBillDao.getEntityById(billId);
		Map<String,Object> params  = new HashMap<String, Object>();
		//params.put("status", "WAIT");//未执行
		params.put("params", bill.getId());
		//判断申请单是否写了 任职历史
		List<Task> list = this.queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskDao.select", params, Task.class);
		if(list !=null && list.size()>0){
			Task task = list.get(0);
			if(task.getStatus() !=null && task.getStatus().getName().equals("WAIT")){
				//如果未执行 则直接删除定时任务
				this.queryExecutor.executeDelete("com.wuyizhiye.base.timing.dao.TaskDao.delete", task.getId());
				PositionHistoryBill updateBill = new PositionHistoryBill();
				updateBill.setId(billId);
				updateBill.setBillStatus(BillStatusEnum.SUBMIT.getValue());
				positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(updateBill);
				return resultMsg;
			}
		} 
		
		params.clear();
		params.put("primary", 1);
		params.put("personId", bill.getApplyPerson().getId());
		params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		if(phisList==null || phisList.size()<1){
			resultMsg = "任职历史记录异常,不能反审核!";
			return resultMsg ;
		}
		//判断最后一条任职记录是不是本次申请单生成的
		//变更前后的组织 、职位、职级 、生效日期 都相同
		PersonPositionHistory lastHis = phisList.get(0);//最后第一条任职历史
		PersonPositionHistory lastButTwoHis = phisList.get(1);//倒数第二条任职历史
		if(lastButTwoHis==null || lastButTwoHis.getEffectdate()==null || lastButTwoHis.getExpirydate()==null){
			resultMsg = "任职历史记录异常,不能反审核!";
			return resultMsg ;
		}
		if(lastHis.getChangeType().equals(bill.getChangeType()) && StringUtils.isNotEmpty(lastHis.getOldOrgId()) 
				&& StringUtils.isNotEmpty(lastHis.getOldPositionId()) 
				&& StringUtils.isNotEmpty(lastHis.getOldJobLevel())
				&& lastHis.getOldOrgId().equals(bill.getApplyOrg().getId()) 
				&& lastHis.getOldPositionId().equals(bill.getApplyPosition().getId()) 
				&& lastHis.getOldJobLevel().equals(bill.getApplyJoblevel().getId())
				&& StringUtils.isNotEmpty(lastHis.getChangeOrgId()) && StringUtils.isNotEmpty(lastHis.getChangePositionId()) 
				&& StringUtils.isNotEmpty(lastHis.getChangeJobLevel())
				&& lastHis.getChangeOrgId().equals(bill.getApplyChangeOrg().getId()) 
				&& lastHis.getChangePositionId().equals(bill.getApplyChangePosition().getId()) 
				&& lastHis.getChangeJobLevel().equals(bill.getApplyChangeJoblevel().getId())
				&& lastHis.getEffectdate()!=null && lastHis.getEffectdate().compareTo(bill.getEffectdate())==0){
			//如果是本次申请单生成(变更前后的组织 、职位、职级 、生效日期 都相同)
			
			//修改人员信息
			Person p = personDao.getEntityById(bill.getApplyPerson().getId());
			//如果本次申请单 是降跑盘 则反审时 要修改人员的岗位状态
			if(isRunDiskByDemotion(bill)){
				
				//反审 还原员工岗位状态
				p.setJobStatus(bill.getJobStatus());
			}
			if(bill.getApplyOrg()!=null){
			 Org org = orgDao.getEntityById(bill.getApplyOrg().getId());
			 p.setOrg(org);//反审 还原调动前组织
		    }
			 personDao.updateEntity(p);
			//反审核  还原员工主要职位、职级
			params.clear();
			params.put("person", bill.getApplyPerson().getId());
			params.put("isPrimary", 1);
			PersonPosition pp = personPositionDao.getPerPositionByCondition(params);
			if(pp !=null){
				pp.setPosition(bill.getApplyPosition());
				pp.setJobLevel(bill.getApplyJoblevel());
				pp.setEffectDate(lastButTwoHis.getEffectdate());
			}
			personPositionDao.updateEntity(pp);
			//反审核 把最后一条任职历史记录删除  把倒数第二条记录的失效日期置空
			lastButTwoHis.setExpirydate(null);
			personPositionHistoryDao.updateEntity(lastButTwoHis);//倒数第二条记录的失效日期置空
			personPositionHistoryDao.deleteById(lastHis.getId());//最后一条任职历史记录删除
			
			PositionHistoryBill updateBill = new PositionHistoryBill();
			updateBill.setId(billId);
			updateBill.setBillStatus(BillStatusEnum.SUBMIT.getValue());
			positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(updateBill);
			
		}else{
			resultMsg = "该申请单生效后已经被实际使用,不能反审核!";
		}
		return resultMsg;
	}
	
	/**
	 * 主要职位调动 定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId)throws Exception{
		if(!StringUtils.isEmpty(billId)){
			try{
		    //审核完成写任职历史
		    PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
			//根据调职申请单 维护任职历史失效日期
			updatePositionHistoryByBill(phBill);
			
			//根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//审核完成 根据调职申请单 回写人员组织、职级信息信息
			updatePersonPositionByBill(phBill);
			
			phBill.setIsEffective(1);//标记已生效
			this.positionHistoryBillDao.updateEntity(phBill);
			}catch(Exception e){
				e.printStackTrace();
				 
			}
		}
		
	}
	
	/**
	 * 调职单(兼职)审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approvePartTimeJob(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			PositionHistoryBill bill = new PositionHistoryBill();
			bill.setId(billId);
			bill.setPrimary(false);//主要兼职
			BaseUtils.setWho(bill, true);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsEffective(1);//标记已生效
				 
				//审核完成写任职历史
				PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
				if(PositionChangeTypeEnum.INCREASE_PARTTIMEJOB.getValue().equals(phBill.getChangeType())){
					//兼职
					
					//如果生效日期 小于等于当前日期 则即刻新增人员兼职信息
					if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
						//根据调职申请单 写任职历史记录
						addPositionHistoryByBill(phBill);
						
						//审核完成 根据调职申请单 新增兼职信息
						addPersonPositionByBill(phBill);
					}
					
					/*else{
						//兼职   定时器  写任职历史和修改人员职位信息
						String description = "兼职   定时器  写任职历史和修改人员职位信息";
						TimingTaskUtil.createTask("兼职", description, phBill.getEffectdate(),
								"positionHistoryBillService.increasePartTimeJobSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
					}*/
				}else{
					//撤职
					
					//如果生效日期 小于等于当前日期 则即刻修改人员信息
					if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
						//根据调职申请单 维护任职历史失效日期
						updatePositionHistory4PartTimeJob(phBill);
						
						//根据调职申请单 写任职历史记录
						addPositionHistoryByBill(phBill);
						
						//审核完成   删除人员兼职
						deletePersonPositionByBill(phBill);
					}
					
					/*else{
						//撤职   定时器  写任职历史和修改人员职位信息
						String description = "撤职  定时器  写任职历史和修改人员职位信息";
						TimingTaskUtil.createTask("撤职", description, phBill.getEffectdate(),
								"positionHistoryBillService.dismissPartTimeJobSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
					}*/
				}
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				bill.setBillStatus(BillStatusEnum.REVOKE.getValue());
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				bill.setBillStatus(BillStatusEnum.REJECT.getValue());
			}
			positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(bill);
		}
		return resultMsg ;
	}
	
	/**
	 * 兼职 撤职  反审核
	 * @param billId
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String backApprovePartTimeJob(String billId ,String approveType){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		PositionHistoryBill bill = positionHistoryBillDao.getEntityById(billId);
		Map<String,Object> params  = new HashMap<String, Object>();
		//params.put("status", "WAIT");//未执行
		params.put("params", bill.getId());
		//判断申请单是否写了 任职历史
		List<Task> list = this.queryExecutor.execQuery("com.wuyizhiye.base.timing.dao.TaskDao.select", params, Task.class);
		if(list !=null && list.size()>0){
			Task task = list.get(0);
			if(task.getStatus() !=null && task.getStatus().getName().equals("WAIT")){
				//如果未执行 则直接删除定时任务
				this.queryExecutor.executeDelete("com.wuyizhiye.base.timing.dao.TaskDao.delete", task.getId());
				PositionHistoryBill updateBill = new PositionHistoryBill();
				updateBill.setId(billId);
				updateBill.setBillStatus(BillStatusEnum.SUBMIT.getValue());
				positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(updateBill);
				return resultMsg;
			}
		}
		
		if(PositionChangeTypeEnum.INCREASE_PARTTIMEJOB.getValue().equals(bill.getChangeType())){
			//兼职 反审核
			//删除人员兼职 PersonPosition
			params.clear();
			params.put("personId", bill.getApplyPerson().getId());
			params.put("isPrimary", 0);
			params.put("positionId", bill.getApplyChangePosition().getId());
			params.put("jobLevelId", bill.getApplyChangeJoblevel().getId());
			personPositionDao.deleteByCondition(params);
			//删除 兼职任职历史
			params.clear();
			params.put("primary", 0);
			params.put("personId", bill.getApplyPerson().getId());
			params.put("changeOrgId", bill.getApplyChangeOrg().getId());
			params.put("changePositionId", bill.getApplyChangePosition().getId());
			params.put("changeJobLevel", bill.getApplyChangeJoblevel().getId());
			params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc");
			List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
			if(phisList==null || phisList.size()<1){
				resultMsg = "兼职任职历史记录异常,不能反审核!";
				return resultMsg ;
			}
			personPositionHistoryDao.deleteById(phisList.get(0).getId());
		}else{
			//撤职
			// 反审核  还原兼职记录的失效日期
			params.clear();
			params.put("primary", 0);
			params.put("personId", bill.getApplyPerson().getId());
			params.put("changeOrgId", bill.getApplyOrg().getId());
			params.put("changePositionId", bill.getApplyPosition().getId());
			params.put("changeJobLevel", bill.getApplyJoblevel().getId());
			params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc");
			List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
			if(phisList==null || phisList.size()<1){
				resultMsg = "兼职任职历史记录异常,不能反审核!";
				return resultMsg ;
			}
			PersonPositionHistory increaseHis = phisList.get(0);
			increaseHis.setExpirydate(null);
			personPositionHistoryDao.updateEntity(increaseHis);//兼职失效日期置空
			
			//还原兼职 任职信息 PersonPosition
			PersonPosition pp = new PersonPosition();
			pp.setPerson(bill.getApplyPerson());
			pp.setPosition(bill.getApplyPosition());
			pp.setPrimary(false);
			pp.setJobLevel(bill.getApplyJoblevel());
			pp.setEffectDate(increaseHis.getEffectdate());//兼职生效日期
			personPositionDao.addEntity(pp);
			
			// 反审核 删除撤职任职历史记录
			params.clear();
			params.put("primary", 0);
			params.put("personId", bill.getApplyPerson().getId());
			params.put("oldOrgId", bill.getApplyOrg().getId());
			params.put("oldPositionId", bill.getApplyPosition().getId());
			params.put("oldJobLevel", bill.getApplyJoblevel().getId());
			params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc");
			phisList =  personPositionHistoryDao.getPositionHistory(params);
			if(phisList==null || phisList.size()<1){
				resultMsg = "撤职 任职历史记录异常,不能反审核!";
				return resultMsg ;
			}
			personPositionHistoryDao.deleteEntity(phisList.get(0));
		}
		
		PositionHistoryBill updateBill = new PositionHistoryBill();
		updateBill.setId(billId);
		updateBill.setBillStatus(BillStatusEnum.SUBMIT.getValue());
		positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(updateBill);
		
		return resultMsg;
	}
	
	/**
	 * 兼职   定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void increasePartTimeJobSchedule(String billId)throws Exception{
		if(!StringUtils.isEmpty(billId)){
		    //审核完成写任职历史
		    PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
		  //根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//审核完成 根据调职申请单 新增兼职信息
			addPersonPositionByBill(phBill);
			
			phBill.setIsEffective(1);//标记已生效
			this.positionHistoryBillDao.updateEntity(phBill);
		}
		
	}
	
	
	/**
	 * 撤职   定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void dismissPartTimeJobSchedule(String billId)throws Exception{
		if(!StringUtils.isEmpty(billId)){
		    //审核完成写任职历史
		    PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
		    //根据调职申请单 维护任职历史失效日期
			updatePositionHistory4PartTimeJob(phBill);
			
			//根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//审核完成   删除人员兼职
			deletePersonPositionByBill(phBill);
			
			phBill.setIsEffective(1);//标记已生效
			this.positionHistoryBillDao.updateEntity(phBill);
		}
		
	}
	
	/**
	 * 跑盘员删除  审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveDelRunDisk(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			PositionHistoryBill bill = new PositionHistoryBill();
			bill.setId(billId);
			bill.setPrimary(true);//主要职务
			BaseUtils.setWho(bill, true);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsEffective(1);//标记已生效
				//审核完成写任职历史
				PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
				
				//如果生效日期 小于等于当前日期 则即刻修改人员信息
				if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
					//根据调职申请单 维护任职历史失效日期
					updatePositionHistoryByBill(phBill);
					
					//根据调职申请单 写任职历史记录
					addPositionHistoryByBill(phBill);
					
					//跑盘员删除审核完成  岗位状态改为 离职
					updatePersonByBill4DelRunDisk(phBill);
				}
				/*else{
					//主要职位调动 定时器  写任职历史和修改人员职位信息
					String description = "跑盘员删除 定时器  写任职历史和修改人员职位信息";
					TimingTaskUtil.createTask("职位调动", description, phBill.getEffectdate(),
							"positionHistoryBillService.approveDelRunDiskSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
				}*/
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				bill.setBillStatus(BillStatusEnum.REVOKE.getValue());
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				bill.setBillStatus(BillStatusEnum.REJECT.getValue());
			}
			positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(bill);
		}
		return resultMsg ;
	}
	
	/**
	 * 跑盘员删除  流程审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveDelRunDiskBill(String billId){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		 
		PositionHistoryBill bill = new PositionHistoryBill();
		bill.setId(billId);
		bill.setPrimary(true);//主要职务
		BaseUtils.setWho(bill, true);
		 
		//已审核
		bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
		bill.setIsEffective(1);//标记已生效
		//审核完成写任职历史
		PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
		//如果生效日期 小于等于当前日期 则即刻修改人员信息
		if(phBill.getEffectdate()!=null && phBill.getEffectdate().compareTo(new Date())<=0){
			//根据调职申请单 维护任职历史失效日期
			updatePositionHistoryByBill(phBill);
			
			//根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//跑盘员删除审核完成  岗位状态改为 离职
			updatePersonByBill4DelRunDisk(phBill);
		}
		/*else{
			//主要职位调动 定时器  写任职历史和修改人员职位信息
			String description = "跑盘员删除 定时器  写任职历史和修改人员职位信息";
			TimingTaskUtil.createTask("职位调动", description, phBill.getEffectdate(),
					"positionHistoryBillService.approveDelRunDiskSchedule(billId)", phBill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
		}*/
		 
		positionHistoryBillDao.updateHistoryBillByPrimaryKeySelective(bill);
		this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
		return resultMsg ;
	}
	
	/**
	 * 跑盘员删除 定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void approveDelRunDiskSchedule(String billId)throws Exception{
		if(!StringUtils.isEmpty(billId)){
			try{
		    //审核完成写任职历史
		    PositionHistoryBill phBill = positionHistoryBillDao.getEntityById(billId);
		
			//根据调职申请单 维护任职历史失效日期
			updatePositionHistoryByBill(phBill);
			
			//根据调职申请单 写任职历史记录
			addPositionHistoryByBill(phBill);
			
			//跑盘员删除审核完成  岗位状态改为 离职
			updatePersonByBill4DelRunDisk(phBill);
			
			phBill.setIsEffective(1);//标记已生效
			this.positionHistoryBillDao.updateEntity(phBill);
			}catch(Exception e){
				e.printStackTrace();
				 
			}
		}
		
	}
	
	/**
	 * 是否为降跑盘
	 * @param bill
	 * @return
	 */
	public boolean isRunDiskByDemotion(PositionHistoryBill bill){
		if(PositionChangeTypeEnum.DEMOTION.getValue().equals(bill.getChangeType())){
			//置业顾问 编码
			String jobNumber = ParamUtils.getParamValue(bill.getApplyChangeOrg().getId(), JOBNUMBER);
			if(StringUtils.isEmpty(jobNumber)){
				jobNumber ="8016";//默认8016
			}
			//销售代表 编码
			String jobNumberXs = ParamUtils.getParamValue(bill.getApplyChangeOrg().getId(), JOBNUMBERXS);
			if(StringUtils.isEmpty(jobNumberXs)){
				jobNumberXs ="8020";//默认8020
			}
			String kxxsdb = ParamUtils.getParamValue(bill.getApplyChangeOrg().getId(), KXXSDB);
			 
			//跑盘员职级 level
			String jobLevel = ParamUtils.getParamValue(bill.getApplyChangeOrg().getId(), JOBLEVEL);
			if(StringUtils.isEmpty(jobLevel)){
				jobLevel ="1";//默认1
			}
			JobLevel jl = jobLevelDao.getEntityById(bill.getApplyChangeJoblevel().getId());
			if( jl!=null && jl.getJob()!=null && (jobNumber.equals(jl.getJob().getNumber()) || jobNumberXs.equals(jl.getJob().getNumber())
					|| (kxxsdb!=null && kxxsdb.equals(jl.getJob().getNumber())))
					&& Integer.valueOf(jobLevel).equals(jl.getLevel())){

				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 审核完成 根据调职申请单 回写人员组织、职级信息信息
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String updatePersonPositionByBill(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		Person p = personDao.getEntityById(bill.getApplyPerson().getId());
		if(bill.getApplyChangeOrg()!=null){
		  Org org = orgDao.getEntityById(bill.getApplyChangeOrg().getId());
		  p.setOrg(org);
		}
		/*if(org.getControlUnit()!=null){
		 Org controlUnit = orgDao.getEntityById(org.getControlUnit().getId());
		 p.setControlUnit(controlUnit);
		}*/
		//如果是降跑盘 要修改人员的岗位状态为 见习
		
		if(isRunDiskByDemotion(bill)){
			p.setJobStatus(this.basicDataService.getEntityByNumber(JobStatusEnum.PROBATION.toString()));//岗位状态改为  见习
		}
		
		//修改人员组织
		personDao.updateEntity(p);
		//修改 人员职位 职级 PersonPosition
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("person", bill.getApplyPerson().getId());
		params.put("isPrimary", 1);
		PersonPosition pp = personPositionDao.getPerPositionByCondition(params);
		if(pp !=null){
			pp.setPosition(bill.getApplyChangePosition());
			pp.setJobLevel(bill.getApplyChangeJoblevel());
			pp.setEffectDate(bill.getEffectdate());
		}
		
		personPositionDao.updateEntity(pp);
		List<PersonPosition> pps=new ArrayList<PersonPosition>();
		pps.add(pp);
		personService.synWeiXinPerson(p, pps, "update");//同步人员信息到企业微信号
		
		return resultMsg;
	}
	
	/**
	 * 跑盘员删除审核完成  岗位状态改为 离职
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String updatePersonByBill4DelRunDisk(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		Person p = personDao.getEntityById(bill.getApplyPerson().getId());
		//跑盘员删除审核完成  岗位状态改为 离职
		 p.setJobStatus(this.basicDataService.getEntityByNumber(JobStatusEnum.DIMISSION.toString()));//岗位状态改为   离职
		 p.setLeaveDate(bill.getEffectdate());//离职日期
		personDao.updateEntity(p);
		
		//删除员工 任职信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("personId", bill.getApplyPerson().getId());
		personPositionDao.deleteByCondition(params);
		return resultMsg;
	}
	
	/**
	 * 审核完成 根据调职申请单 写人员兼职信息--兼职
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String addPersonPositionByBill(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		 
		//新增 人员兼职 PersonPosition
		PersonPosition pp = new PersonPosition();
		pp.setPerson(bill.getApplyPerson());
		pp.setPosition(bill.getApplyChangePosition());
		pp.setPrimary(bill.getPrimary());
		pp.setJobLevel(bill.getApplyChangeJoblevel());
		pp.setEffectDate(bill.getEffectdate());
		personPositionDao.addEntity(pp);
		return resultMsg;
	}
	
	/**
	 * 审核完成 根据调职申请单 写人员兼职信息--撤职
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String deletePersonPositionByBill(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		 
		//删除人员兼职 PersonPosition
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("personId", bill.getApplyPerson().getId());
		params.put("isPrimary", 0);
		params.put("positionId", bill.getApplyPosition().getId());
		params.put("jobLevelId", bill.getApplyJoblevel().getId());
		personPositionDao.deleteByCondition(params);
		return resultMsg;
	}
	
	/**
	 * 根据调职申请单 写任职历史记录
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String addPositionHistoryByBill(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		PersonPositionHistory phis = new PersonPositionHistory();
		Date now = new Date();
		phis.setCreateTime(now);
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
		 phis.setCreatorId(loginUser.getId());
		 phis.setUpdator(loginUser.getId());
		}
		Org loginOrg = bill.getOrg();
		if(loginOrg!=null){
		  phis.setOrgId(loginOrg.getId());
		}
		phis.setLastupdateTime(now);
		phis.setIsdisable("N");
		phis.setGivePersonId(bill.getHandOverPerson()!=null?bill.getHandOverPerson().getId():null);//离职交接人 by hlz
		phis.setPersonId(bill.getApplyPerson().getId());
		phis.setJobStatus(bill.getJobStatus());
		phis.setPrimary(bill.getPrimary());
		phis.setOldOrgId(bill.getApplyOrg().getId());
		phis.setOldPositionId(bill.getApplyPosition().getId());
		phis.setOldJobLevel(bill.getApplyJoblevel().getId());
		if(bill.getApplyChangeOrg()!=null){
			phis.setChangeOrgId(bill.getApplyChangeOrg().getId());
			phis.setChangePositionId(bill.getApplyChangePosition().getId());
			phis.setChangeJobLevel(bill.getApplyChangeJoblevel().getId());
		}
		phis.setEffectdate(bill.getEffectdate());
		phis.setChangeType(bill.getChangeType());
		
		processInfoService.insertProcessInfo(phis);
		personPositionHistoryDao.addEntity(phis);
		return resultMsg ;
	}
	
	/**
	 * 根据调职申请单 维护任职历史失效日期
	 * @param bill
	 * @return
	 */
	public String updatePositionHistoryByBill(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		Map<String,Object> params  = new HashMap<String, Object>();
		params.put("primary", bill.getPrimary());
		params.put("personId", bill.getApplyPerson().getId());
		params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc,FEXPIRYDATE desc ");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		if(phisList==null || phisList.size()<1){
			return resultMsg ;
		}
		PersonPositionHistory param = new PersonPositionHistory();
		param.setId(phisList.get(0).getId());
		param.setExpirydate(bill.getEffectdate());//把调职申请单的生效日期 更新到上一次调职的失效日期
		param.setLastupdateTime(new Date());
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
			param.setUpdator(loginUser.getId());
		}
		Org loginOrg = bill.getOrg();
		if(loginOrg!=null){
			param.setOrgId(loginOrg.getId());
		}
		param.setPrimary(true);//主要职位
		personPositionHistoryDao.updateByPrimaryKeySelective(param);
		return resultMsg ;
	}
	
	/**
	 * 根据调职申请单 维护任职历史失效日期--撤职
	 * @param bill
	 * @return
	 */
	public String updatePositionHistory4PartTimeJob(PositionHistoryBill bill){
		String resultMsg = "SUCC";
		Map<String,Object> params  = new HashMap<String, Object>();
		params.put("primary", bill.getPrimary());
		params.put("personId", bill.getApplyPerson().getId());
		params.put("changeOrgId", bill.getApplyOrg().getId());
		params.put("changePositionId", bill.getApplyPosition().getId());
		params.put("changeJobLevel", bill.getApplyJoblevel().getId());
		params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc,FEXPIRYDATE desc ");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		if(phisList==null || phisList.size()<1){
			return resultMsg ;
		}
		PersonPositionHistory param = new PersonPositionHistory();
		param.setId(phisList.get(0).getId());
		param.setExpirydate(bill.getEffectdate());//把调职申请单的生效日期 更新到上一次兼职的失效日期
		param.setLastupdateTime(new Date());
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
			param.setUpdator(loginUser.getId());
		}
		Org loginOrg = bill.getOrg();
		if(loginOrg!=null){
			param.setOrgId(loginOrg.getId());
		}
		personPositionHistoryDao.updateByPrimaryKeySelective(param);
		return resultMsg ;
	}

	@Override
	public void addEntity(PositionHistoryBill entity) {
		if(null!=entity.getApplyChangeOrg()){//如果变动组织不为空,则取变动组织所在的cu
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getApplyChangeOrg().getId()));
		}else{//取变动人所在的cu			
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(PositionHistoryBill entity) {
		if(null!=entity.getApplyChangeOrg()){//如果变动组织不为空,则取变动组织所在的cu
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getApplyChangeOrg().getId()));
		}else{//取变动人所在的cu			
			Person person=this.personDao.getEntityById(entity.getApplyPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.updateEntity(entity);
	}
}
