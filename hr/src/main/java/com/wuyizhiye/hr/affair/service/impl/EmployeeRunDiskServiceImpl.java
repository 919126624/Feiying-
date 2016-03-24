package com.wuyizhiye.hr.affair.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.wuyizhiye.base.util.ObjectCopyUtils;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;
import com.wuyizhiye.hr.affair.service.EmployeeRunDiskService;
import com.wuyizhiye.hr.affair.service.ProcessInfoService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 
 * @author ouyangyi
 * @since 2013-4-23 下午03:19:06
 */
@Component(value="employeeRunDiskService")
@Transactional
public class EmployeeRunDiskServiceImpl extends DataEntityService< EmployeeRunDiskBill> implements
    EmployeeRunDiskService {
	
	private Logger log = Logger.getLogger(EmployeeRunDiskServiceImpl.class);
	
	private final static String CREATE_BY_NUM = "CREATE_BY_NUM";//是否根据员工编码生成登录账号
	
	@Resource
	private EmployeeRunDiskDao employeeRunDiskDao;
	
	@Resource
	private PersonPositionHistoryDao personPositionHistoryDao;
	
	@Resource
	 private ProcessInfoService processInfoService;
	
	@Resource
	private BasicDataService basicDataService;
	
	@Resource
	private PersonPositionDao personPositionDao;
	
	@Resource
	private PersonDao personDao;
	
	@Resource
	private OrgDao orgDao;
	
	@Autowired
	private ProcessViewService processViewService;
	
	@Override
	protected BaseDao getDao() {
		return employeeRunDiskDao;
	}
	
	/**
	 * 跑盘员入职申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveRunDisk(String[] billIds ,String approveType){
		String resultMsg = "SUCC";
		if(billIds==null || billIds.length<1){
			return resultMsg ;
		}
		for(String billId :billIds){
			EmployeeRunDiskBill bill = employeeRunDiskDao.getEntityById(billId);
			//审核完成写任职历史
			BaseUtils.setWho(bill, true);
			if(BillStatusEnum.APPROVED.getValue().equals(approveType)){
				//已审核
				bill.setBillStatus(BillStatusEnum.APPROVED);
				
				
				//如果生效日期 小于等于当前日期 则即刻修改人员信息
				if(bill.getEffectdate()!=null && bill.getEffectdate().compareTo(new Date())<=0){
					if(bill.getApplyPerson() ==null || StringUtils.isEmpty(bill.getApplyPerson().getId())){
						//从未 入职和新增跑盘  新增人员、新增任职信息
						addPersonPositionByBill(bill);
					}else{
						//曾经 入职过或新增跑盘 则修改
						//审核完成 根据跑盘员入职申请单回写人员组织、职级信息信息
						updatePersonPositionByBill(bill);
						
						//根据跑盘员入职申请单维护任职历史失效日期
						updatePositionHistoryByBill(bill);
					}
					
					//根据跑盘员入职申请单写任职历史记录
					addPositionHistoryByBill(bill);
					
					bill.setIsEffective(1);//标记单据已生效
				}
				/*else{
					//主要职位调动 定时器  写任职历史和修改人员职位信息
					String description = "新增跑盘员 定时器  写任职历史和修改人员职位信息";
					TimingTaskUtil.createTask("职位调动", description, bill.getEffectdate(),
							"employeeRunDiskService.approveSchedule(billId)", bill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
				}*/
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
			}else if(BillStatusEnum.REVOKE.getValue().equals(approveType)){
				// 撤销
				bill.setBillStatus(BillStatusEnum.REVOKE);
			}else if(BillStatusEnum.REJECT.getValue().equals(approveType)){
				//驳回
				bill.setBillStatus(BillStatusEnum.REJECT);
			}
			employeeRunDiskDao.updateEntity(bill);
		}
		return resultMsg;
	}
	
	
	/**
	 * 跑盘员入职申请审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveBill(String billId ){
		String resultMsg = "SUCC";
		if(StringUtils.isEmpty(billId)){
			return resultMsg ;
		}
		 
		EmployeeRunDiskBill bill = employeeRunDiskDao.getEntityById(billId);
		//审核完成写任职历史
		BaseUtils.setWho(bill, true);
		//已审核
		bill.setBillStatus(BillStatusEnum.APPROVED);
		
		//如果生效日期 小于等于当前日期 则即刻修改人员信息
		if(bill.getEffectdate()!=null && bill.getEffectdate().compareTo(new Date())<=0){
			if(bill.getApplyPerson() ==null || StringUtils.isEmpty(bill.getApplyPerson().getId())){
				//从未 入职和新增跑盘  新增人员、新增任职信息
				addPersonPositionByBill(bill);
			}else{
				//曾经 入职过或新增跑盘 则修改
				//审核完成 根据跑盘员入职申请单回写人员组织、职级信息信息
				updatePersonPositionByBill(bill);
				
				//根据跑盘员入职申请单维护任职历史失效日期
				updatePositionHistoryByBill(bill);
			}
			
			//根据跑盘员入职申请单写任职历史记录
			addPositionHistoryByBill(bill);
			
			bill.setIsEffective(1);//标记单据已生效
		}
		/*else{
			//主要职位调动 定时器  写任职历史和修改人员职位信息
			String description = "新增跑盘员 定时器  写任职历史和修改人员职位信息";
			TimingTaskUtil.createTask("职位调动", description, bill.getEffectdate(),
					"employeeRunDiskService.approveSchedule(billId)", bill.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
		}*/
		 
		employeeRunDiskDao.updateEntity(bill);
		this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);//修改流程中间表状态
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
				EmployeeRunDiskBill bill = employeeRunDiskDao.getEntityById(billId);
				if(bill.getApplyPerson() ==null || StringUtils.isEmpty(bill.getApplyPerson().getId())){
					//从未 入职和新增跑盘  新增人员、新增任职信息
					addPersonPositionByBill(bill);
				}else{
					//曾经 入职过或新增跑盘 则修改
					//审核完成 根据跑盘员入职申请单回写人员组织、职级信息信息
					updatePersonPositionByBill(bill);
					
					//根据跑盘员入职申请单维护任职历史失效日期
					updatePositionHistoryByBill(bill);
				}
				
				//根据跑盘员入职申请单写任职历史记录
				addPositionHistoryByBill(bill);
				
				bill.setIsEffective(1);//标记单据已生效
				employeeRunDiskDao.updateEntity(bill);
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage()); 
				throw e;
			}
		}
		
	}

	
	/**
	 * 根据跑盘员入职申请单写任职历史记录
	 * @param (EmployeeRunDiskBill) bill
	 * @return
	 */
	public String addPositionHistoryByBill(EmployeeRunDiskBill bill){
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
		
		phis.setPersonId(bill.getApplyPerson().getId());
		phis.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.RUNDISK.getValue()));
		phis.setPrimary(true);
		if(bill.getMainPositionOrg()!=null){
			phis.setChangeOrgId(bill.getMainPositionOrg().getId());
			phis.setChangePositionId(bill.getMainPosition().getId());
			phis.setChangeJobLevel(bill.getMainJobLevel().getId());
		}
		phis.setEffectdate(bill.getEffectdate());
		phis.setChangeType(PositionChangeTypeEnum.RUNDISK.getValue());
		personPositionHistoryDao.addEntity(phis);
		processInfoService.insertProcessInfo(phis);
		return resultMsg ;
	}
	
	/**
	 * 设置员工 登录账号
	 * @param userName
	 * @param employee
	 * @param oldName
	 */
	public void setUserName(String userName,Person employee,String oldName){
		//是否根据员工编码生成登录账号
		String reateByNumFlag = ParamUtils.getParamValue(employee.getOrg().getId(), CREATE_BY_NUM);
		if("Y".equals(reateByNumFlag)){
			//根据员工编码生成登录账号
			employee.setUserName(employee.getNumber());
			return ;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		int a = 0;
		param.put("userName", userName);
		List<Person> p1 = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
		if(p1!=null && p1.size()>0){
			if(!userName.equals(oldName)){
			  a = Integer.valueOf(userName.replace(oldName, ""));
			}
			a++;
			setUserName(oldName+a,employee,oldName);
		}else{
			employee.setUserName(userName);
		}
	}
	
	/**
	 * 审核完成 根据 跑盘员入职申请单 写人员任职信息--新跑盘员入职(从未 入职和新增跑盘)
	 * @param (EmployeeRunDiskBill) bill
	 * @return
	 */
	public String addPersonPositionByBill(EmployeeRunDiskBill bill){
		String resultMsg = "SUCC";
		 //新增人员
		Person p = new Person();
		ObjectCopyUtils.copy(bill, p); 
		p.setId(null);
		Date now = new Date();
		p.setCreateTime(now);
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
		 p.setCreator(loginUser);
		 p.setUpdator(loginUser);
		}
		//设置 员工主要职位的所属组织
		if(bill.getMainPositionOrg()!=null){
		  Org org = orgDao.getEntityById(bill.getMainPositionOrg().getId());
		  p.setOrg(org);
		}
		p.setLastUpdateTime(now);
		p.setStatus(UserStatusEnum.ENABLE);//默认 员工状态 启用
		p.setInnerDate(bill.getEffectdate());//入职日期 为 跑盘生效日期
		p.setPersonStatus(PersonStatusEnum.TRIAL);//默认 试用
		p.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.RUNDISK.toString()));//新增跑盘
		setUserName(p.getName(),p,p.getName());//设置员工 登录账号
		//设置生日 用于生日墙默认值
		if(p.getBirthday()!=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			p.setCalendarType("solar");
			p.setBirthdayEn(df.format(p.getBirthday()));
		}
		personDao.addEntity(p);
		
		
		//新增 人员主要职位 PersonPosition
		PersonPosition pp = new PersonPosition();
		bill.setApplyPerson(p);
		pp.setPerson(bill.getApplyPerson());
		pp.setPosition(bill.getMainPosition());
		pp.setPrimary(true);
		pp.setJobLevel(bill.getMainJobLevel());
		pp.setEffectDate(bill.getEffectdate());
		personPositionDao.addEntity(pp);
		return resultMsg;
	}
	
	/**
	 * 根据跑盘员入职申请单维护任职历史失效日期
	 * @param bill
	 * @return
	 */
	public String updatePositionHistoryByBill(EmployeeRunDiskBill bill){
		String resultMsg = "SUCC";
		Map<String,Object> params  = new HashMap<String, Object>();
		params.put("primary", 1);
		params.put("personId", bill.getApplyPerson().getId());
		params.put("orderByClause", "FEFFECTDATE desc,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc ");
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
	 * 审核完成 根据跑盘员入职申请单回写人员组织、职级信息信息
	 * @param (EmployeeRunDiskBill) bill
	 * @return
	 */
	public String updatePersonPositionByBill(EmployeeRunDiskBill bill){
		String resultMsg = "SUCC";
		Person p = personDao.getEntityById(bill.getApplyPerson().getId());
		//设置 员工主要职位的所属组织
		if(bill.getMainPositionOrg()!=null){
		  Org org = orgDao.getEntityById(bill.getMainPositionOrg().getId());
		  p.setOrg(org);
		}
		/*
		if(org.getControlUnit()!=null){
		 Org controlUnit = orgDao.getEntityById(org.getControlUnit().getId());
		 p.setControlUnit(controlUnit);
		}*/
		p.setJobStatus(basicDataService.getEntityByNumber(JobStatusEnum.RUNDISK.toString()));//跑盘员
		//设置生日 用于生日墙默认值
		if(p.getBirthday()!=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			p.setCalendarType("solar");
			p.setBirthdayEn(df.format(p.getBirthday()));
		}
		//修改人员组织
		personDao.updateEntity(p);
		
		//新增 人员主要职位 PersonPosition
		PersonPosition pp = new PersonPosition();
		bill.setApplyPerson(p);
		pp.setPerson(bill.getApplyPerson());
		pp.setPosition(bill.getMainPosition());
		pp.setPrimary(true);
		pp.setJobLevel(bill.getMainJobLevel());
		pp.setEffectDate(bill.getEffectdate());
		personPositionDao.addEntity(pp);
		
		return resultMsg;
	}

	@Override
	public void addEntity(EmployeeRunDiskBill entity) {
		if(null!=entity.getMainPositionOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getMainPositionOrg().getId()));
		}
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(EmployeeRunDiskBill entity) {
		if(null!=entity.getMainPositionOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getMainPositionOrg().getId()));
		}
		super.updateEntity(entity);
	}
}
