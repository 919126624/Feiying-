/**
 * com.wuyizhiye.hr.service.impl.EmployeeOrientationServiceImpl.java
 */
package com.wuyizhiye.hr.affair.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.TaskTimeEnum;
import com.wuyizhiye.base.timing.TaskTypeEnum;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.affair.dao.EducationTempDao;
import com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao;
import com.wuyizhiye.hr.affair.dao.WorkExperienceTempDao;
import com.wuyizhiye.hr.affair.model.Education;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.model.WorkExperience;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.RewardPunishment;

/**
 * 职员service
 * @author Cai.xing
 *
 * @since 2013-04-03
 */
@Component(value="employeeOrientationService")
@Transactional
public class EmployeeOrientationServiceImpl extends DataEntityService< EmployeeOrientation> implements
    EmployeeOrientationService {
	private final static String BILLNUMBER = "YGBH";
	private final static String CREATE_BY_NUM = "CREATE_BY_NUM";//是否根据员工编码生成登录账号
	private final static String CREATE_BY_NUM_NEW = "CREATE_BY_NUM_NEW";//是否生成新的员工编码
	
	@Autowired
	private EmployeeOrientationDao employeeOrientationDao;
	@Autowired
	private WorkExperienceTempDao workExperienceDao;
	@Autowired
	private EducationTempDao educationDao;
	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private PersonService personService;
	
	@Autowired
	private PersonPositionService personPositionService;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PositionHistoryBillService positionHistoryBillService;
	
	@Autowired
	private ProcessViewService processViewService;
	
	@Override
	protected BaseDao getDao() {
		return employeeOrientationDao;
	}
	
	
	@Override
	public void addEmployeeOrientation(EmployeeOrientation employeeOrientation,
			List<PersonPosition> personPosition,
			List<RewardPunishment> rewardPunishmentList,
			List<AgentCertificate> agentCertificateList,
			List<WorkExperience> workExperienceList,
			List<Education> educationList) {
		super.addEntity(employeeOrientation);
		this.addEducation(employeeOrientation, educationList);
		this.addWorkExperience(employeeOrientation, workExperienceList);
		if(workFlowService.existsProcessDefinition("enrollProcess")){
			if(employeeOrientation.getBillStatu()==BillStatusEnum.SUBMIT){
				startWorkFlow(employeeOrientation);		
			}else if(employeeOrientation.getBillStatu()==BillStatusEnum.SAVE){			
				this.processViewService.generateWFProcessView(employeeOrientation
						.getTitle(), employeeOrientation.getId(),
						employeeOrientation.getProcessInstance(), ProcessTypeEnum.ENROLL,
						employeeOrientation.getBillStatu().getValue(), "HR",
						null == employeeOrientation.getApplyPerson() ? ""
								: employeeOrientation.getApplyPerson().getId(),
						employeeOrientation.getName(), employeeOrientation
								.getPhoto());
			}
		}else if(employeeOrientation.getBillStatu()==BillStatusEnum.SUBMIT
				||employeeOrientation.getBillStatu()==BillStatusEnum.SAVE){
			this.processViewService.generateNWFProcessView(employeeOrientation
					.getTitle(), employeeOrientation.getId(), ProcessTypeEnum.ENROLL,
					employeeOrientation.getBillStatu().getValue(), "HR",
					null == employeeOrientation.getApplyPerson() ? ""
							: employeeOrientation.getApplyPerson().getId(),
					employeeOrientation.getName(), employeeOrientation
							.getPhoto());
		}
	}

	private void startWorkFlow(EmployeeOrientation employeeOrientation){
		if(workFlowService.existsProcessDefinition("enrollProcess")){
			Map<String,Object> param = new HashMap<String, Object>();
			employeeOrientation.setMainPosition(positionService.getEntityById(employeeOrientation.getMainPosition().getId()));
			param.put("positionType", employeeOrientation.getMainPosition().getJob().getNumber());//入职岗位类型
			param.put("positionId", employeeOrientation.getMainPosition().getId());//职位ID
			Person ps=new Person();
			ps.setName(employeeOrientation.getName());
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), employeeOrientation.getId(), employeeOrientation.getMainPositionOrg(), employeeOrientation.getTitle(), param,ps) ;
			variables.setViewPath("hr/employeeOrientation/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("enrollProcess", employeeOrientation.getId(), variables);
			employeeOrientation.setProcessInstance(process.getProcessInstanceId());
			super.updateEntity(employeeOrientation);
			
			this.processViewService.generateWFProcessView(employeeOrientation
					.getTitle(), employeeOrientation.getId(),
					employeeOrientation.getProcessInstance(), ProcessTypeEnum.ENROLL,
					employeeOrientation.getBillStatu().getValue(), "HR",
					null == employeeOrientation.getApplyPerson() ? ""
							: employeeOrientation.getApplyPerson().getId(),
					employeeOrientation.getName(), employeeOrientation
							.getPhoto());
		}				
	}


	@Override
	public void updateEmployeeOrientation(EmployeeOrientation employeeOrientation,
			List<PersonPosition> personPosition,
			List<RewardPunishment> rewardPunishmentList,
			List<AgentCertificate> agentCertificateList,
			List<WorkExperience> workExperienceList,
			List<Education> educationList) {
		super.updateEntity(employeeOrientation);
		this.workExperienceDao.deleteByPersonId(employeeOrientation.getId());
		this.educationDao.deleteByPersonId(employeeOrientation.getId());
		this.addWorkExperience(employeeOrientation, workExperienceList);
		this.addEducation(employeeOrientation, educationList);
		if(workFlowService.existsProcessDefinition("enrollProcess")){
			if(employeeOrientation.getBillStatu()==BillStatusEnum.SUBMIT && StringUtils.isEmpty(employeeOrientation.getProcessInstance())){
				startWorkFlow(employeeOrientation);
			}else if(employeeOrientation.getBillStatu()==BillStatusEnum.SAVE){
				this.processViewService.generateWFProcessView(employeeOrientation
						.getTitle(), employeeOrientation.getId(),
						employeeOrientation.getProcessInstance(), ProcessTypeEnum.ENROLL,
						employeeOrientation.getBillStatu().getValue(), "HR",
						null == employeeOrientation.getApplyPerson() ? ""
								: employeeOrientation.getApplyPerson().getId(),
						employeeOrientation.getName(), employeeOrientation
								.getPhoto());
			}
		}else{
			this.processViewService.generateNWFProcessView(employeeOrientation
					.getTitle(), employeeOrientation.getId(), ProcessTypeEnum.ENROLL,
					employeeOrientation.getBillStatu().getValue(), "HR",
					null == employeeOrientation.getApplyPerson() ? ""
							: employeeOrientation.getApplyPerson().getId(),
					employeeOrientation.getName(), employeeOrientation
							.getPhoto());
		}
	}

	
	@Override
	public void deleteEmployeeOrientation(EmployeeOrientation employeeOrientation) {
		// TODO Auto-generated method stub
		
		//先删除职位，工作经历，教育经历
		this.workExperienceDao.deleteByPersonId(employeeOrientation.getId());
		this.educationDao.deleteByPersonId(employeeOrientation.getId());
		//删人员
		super.deleteEntity(employeeOrientation);
	}
	
	/**
	 * 增加工作经历
	 * added by taking.wang
	 * @param rewardPunishmentList
	 */
	private void addWorkExperience(EmployeeOrientation employeeOrientation, List<WorkExperience> workExperienceList){
		if(workExperienceList!=null && workExperienceList.size() > 0){
			for(WorkExperience workExperience : workExperienceList){
				workExperience.setEmployee(employeeOrientation);
			}
			workExperienceDao.addBatch(workExperienceList);
		}
	}
	
	/**
	 * 增加教育经历
	 * add by taking.wang
	 * @param educationList
	 */
	private void addEducation(EmployeeOrientation employeeOrientation, List<Education> educationList){
		if(educationList!=null && educationList.size() > 0){
			for(Education education : educationList){
				education.setEmployee(employeeOrientation);
			}
			educationDao.addBatch(educationList);
		}
	}


	@Override
	public String personHasBillAppvol(String billId,String personId, String appStatu) {
		Map<String,Object> params =  new HashMap<String,Object>();
		params.put("notBillId", billId);
		params.put("personId", personId);
		params.put("billSta", appStatu);
		params.put("billStatus", appStatu);
		params.put("flag", "flag");
		
		if(this.queryExecutor.execCount("com.wuyizhiye.hr.affair.dao.LeaveOfficeDao.select",params)>0){
			return "离职";
		}
		if(this.queryExecutor.execCount("com.wuyizhiye.hr.affair.dao.PositiveDao.select",params)>0){
			return "转正";
		}
		if(this.queryExecutor.execCount("com.wuyizhiye.hr.affair.dao.ReinstatementDao.select",params)>0){
			return "复职";
		}
		List<PositionHistoryBill> list = this.queryExecutor.execQuery("com.wuyizhiye.hr.dao.PositionHistoryBillDao.selectByExample", params, PositionHistoryBill.class);
		if(list!=null && list.size()>0){
			return PositionChangeTypeEnum.valueOf(list.get(0).getChangeType()).getLabel();
		}
		return null;
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
			EmployeeOrientation employeeOrientation = employeeOrientationDao.getEntityById(billId);
			if(employeeOrientation!=null){
				//如果生效日期 小于等于当前日期 则即刻修改人员信息
				if(employeeOrientation.getInnerDate()!=null && employeeOrientation.getInnerDate().compareTo(new Date())<=0){
					 
					resultMap = approvalOrientationBill(billId);
					
				}
				
			/*	else{
					Person per = personService.getEntityById(employeeOrientation.getApplyPerson().getId());
					per.setJobStatus(employeeOrientation.getJobStatus());
					
					String reateByNumNewFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), CREATE_BY_NUM_NEW);
					if("Y".equals(reateByNumNewFlag)){
						per.setNumber(employeeOrientation.getNumber());
						per.setUserName(employeeOrientation.getNumber());
					}
					personService.updateEntity(per);
					//新增任职信息
					PersonPosition personPosition = new PersonPosition();
					personPosition.setUUID();
					personPosition.setPosition(employeeOrientation.getMainPosition());
					personPosition.setPrimary(true);
					personPosition.setPerson(per);
					personPosition.setEffectDate(employeeOrientation.getInnerDate());
					personPosition.setJobLevel(employeeOrientation.getMainJobLevel());
					personPositionService.addEntity(personPosition);
					
					
					//人员入职 定时器  写任职历史和修改人员(岗位状态)信息
					String description = "人员入职 定时器  写任职历史和修改人员(岗位状态)信息";
					TimingTaskUtil.createTask("人员入职", description, employeeOrientation.getInnerDate(),
							"employeeOrientationService.approveSchedule(billId)", employeeOrientation.getId(), TaskTypeEnum.ONCE, TaskTimeEnum.TWO);
				}*/
				employeeOrientation.setBillStatu(BillStatusEnum.APPROVED);
				
				
				
				
				employeeOrientationDao.updateEntity(employeeOrientation);
				this.processViewService.updateStauts(billId, com.wuyizhiye.workflow.enums.BillStatusEnum.APPROVED);
				
			}else{
				resultMap.put("STATE", "FAIL");
				resultMap.put("MSG", "单据已经不存在，请刷新后重试！");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "系统异常！请联系管理员！");
		}
		//更新单据状态
		return resultMap;
	}
	
	/**
	 * 人员入职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId){
		if(!StringUtils.isEmpty(billId)){
		 
			approvalOrientationBill(billId);
		}
		
	}
	
	/**
	 * 审核通过
	 * @param billId
	 * @return
	 */
	@Transactional
	public Map<String,Object> approvalOrientationBill(String billId){
		Map<String,Object> msg = new HashMap<String, Object>();
		//审批通过后反写教育经历\工作经历及人员信息
		EmployeeOrientation employeeOrientation = employeeOrientationDao.getEntityById(billId);
		if(employeeOrientation!=null){
			/*try {*/
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("FID",billId);
				employeeOrientation.setIsEffective(1);//标记成已生效
				
				//根据名字判断username是否有
				employeeOrientation.setUserName(employeeOrientation.getName());
				if(employeeOrientation.getApplyPerson()!=null && StringUtils.isNotNull(employeeOrientation.getApplyPerson().getId())){
					Person personOld = personDao.getEntityById(employeeOrientation.getApplyPerson().getId());
					
					//employeeOrientation.setUserName(personOld.getName());
					employeeOrientation.setUserName(personOld.getUserName());//使用之前的登录账号     sht  2014.07.10
					
					employeeOrientation.setPassword(personOld.getPassword());
				   //删除人员信息
				  personDao.deleteById(employeeOrientation.getApplyPerson().getId());
				}else{
					//如果是入职前 已经在系统中存在过(做过新增跑盘、入职)的人员 登录账号不变
					setUserName(employeeOrientation.getName(),employeeOrientation,employeeOrientation.getName());
				}
				if(employeeOrientation.getApplyPerson()==null || StringUtils.isEmpty(employeeOrientation.getApplyPerson().getId())){
					//设置员工ID
					Person applyPerson = new Person();
					applyPerson.setUUID();
					employeeOrientation.setApplyPerson(applyPerson);
				}
				employeeOrientationDao.updateEntity(employeeOrientation);
				//根据人员删除任职信息
				personPositionService.deleteByPersonId(employeeOrientation.getApplyPerson().getId());
				//删除人员教育经历
				educationDao.deleteByPersonId(employeeOrientation.getApplyPerson().getId());
				//删除人员工作经历
				workExperienceDao.deleteByPersonId(employeeOrientation.getApplyPerson().getId());
				//拷贝人员
				queryExecutor.executeInsert("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.copyToPerson", param);
				//拷贝工作经历
				queryExecutor.executeInsert("com.wuyizhiye.hr.affair.dao.EducationTempDao.copyToEducation", param);
				//拷贝教育经历
				queryExecutor.executeInsert("com.wuyizhiye.hr.affair.dao.WorkExperienceTempDao.copyToExperienceTemp", param);
				//设置生日 用于生日墙默认值
				Person personEdit = personDao.getEntityById(employeeOrientation.getApplyPerson().getId());
				if(personEdit.getBirthday()!=null && StringUtils.isEmpty(personEdit.getCalendarType())){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					personEdit.setCalendarType("solar");
					personEdit.setBirthdayEn(df.format(personEdit.getBirthday()));
					personDao.updateEntity(personEdit);
				}
				//新增任职信息
				PersonPosition personPosition = new PersonPosition();
				personPosition.setUUID();
				personPosition.setPosition(employeeOrientation.getMainPosition());
				personPosition.setPrimary(true);
				Person person = new Person();
				person.setId(employeeOrientation.getApplyPerson().getId());
				personPosition.setPerson(person);
				personPosition.setEffectDate(employeeOrientation.getInnerDate());
				personPosition.setJobLevel(employeeOrientation.getMainJobLevel());
				
				//往任职历史表里面写数据
				PositionHistoryBill bill = new PositionHistoryBill();
				Person p = new Person();
				p.setId(employeeOrientation.getApplyPerson().getId());
				bill.setApplyPerson(p);
				bill.setApplyChangeOrg(employeeOrientation.getMainPositionOrg());
				bill.setApplyChangePosition(employeeOrientation.getMainPosition());
				bill.setApplyChangeJoblevel(employeeOrientation.getMainJobLevel());
				bill.setBillNumber(employeeOrientation.getNumber());
				bill.setJobStatus(employeeOrientation.getJobStatus());
				bill.setChangeType(PositionChangeTypeEnum.ENROLL.getValue());
				bill.setPrimary(true);
				bill.setTakeOfficeDate(employeeOrientation.getInnerDate());
				bill.setBillStatus(BillStatusEnum.APPROVED.getValue());
				bill.setIsdisable("1");
				bill.setEffectdate(employeeOrientation.getInnerDate());
				bill.setApplyOrg(employeeOrientation.getMainPositionOrg());
				bill.setApplyPosition(employeeOrientation.getMainPosition());
				bill.setApplyJoblevel(employeeOrientation.getMainJobLevel());
				personPositionService.addEntity(personPosition);
				bill.setCreator(employeeOrientation.getCreator());
				bill.setUpdator(employeeOrientation.getUpdator());
				positionHistoryBillService.updatePositionHistoryByBill(bill);
				positionHistoryBillService.addPositionHistoryByBill(bill);
				msg.put("STATE", "SUCCESS");
				msg.put("MSG", "审批通过！");
				
				/**
				 * 将用户同步到微信企业号 begin
				 */
			    try{
			    	
			    	param.clear();
			    	param.put("positionId", employeeOrientation.getMainPosition().getId());
			    	WeixinOrg worg=this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.WeixinOrgDao.selectByPosition", param, WeixinOrg.class); 
			    	int sex=employeeOrientation.getSex().toString().equals(SexEnum.WOMAN.toString())?1:0;
			    	
			    	String strJson = "{";
			        strJson += "\"userid\":\""+employeeOrientation.getNumber()+"\",";
			        strJson += "\"name\":\""+employeeOrientation.getName()+"\",";
			        strJson += "\"mobile\":\""+employeeOrientation.getPhone()+"\",";
			        
			        if(StringUtils.isNotNull(employeeOrientation.getWorkPhone())) {
			        	strJson += "\"tel\":\""+employeeOrientation.getWorkPhone()+"\",";
			        }
			        if(StringUtils.isNotNull(employeeOrientation.getEmail())) {
			        	strJson += "\"email\":\""+employeeOrientation.getEmail()+"\",";
			        }
			        if(worg!=null){
			        	  strJson += "\"department\":\""+worg.getNumber()+"\",";
			        	  if(worg.getPosition()!=null && worg.getPosition().getName()!=null){
			        		  strJson += "\"position\":\""+worg.getPosition().getName()+"\",";
			        	  }
			        }
			        
			        strJson += "\"gender\":"+sex;
			        strJson += "}";
			        
				    HttpClientUtil.callHttpUrl(SystemUtil.getBase()+"/weixinapi/weixinperson?t=add", URLEncoder.encode(strJson,"utf-8"));
				     
			    }catch(Exception ex){
			    	ex.printStackTrace();
			    }
			    /**
				 * 将用户同步到微信企业号 end
				 */	
			    
			/*} catch (Exception e) {
				e.printStackTrace();
				msg.put("STATE", "FAIL");
				msg.put("MSG", "系统异常！请联系管理员！");
				//throw new Exception("系统异常！请联系管理员！");
			}*/
		}else{
			msg.put("STATE", "FAIL");
			msg.put("MSG", "单据已经不存在，请刷新后重试！");
			//throw new Exception("单据已经不存在，请刷新后重试！");
		}
		return msg;
	}
	
	public void setUserName(String userName,EmployeeOrientation employeeOrientation,String oldName){
		//是否根据员工编码生成登录账号
		String reateByNumFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), CREATE_BY_NUM);
		if("Y".equals(reateByNumFlag)){
			//根据员工编码生成登录账号
			employeeOrientation.setUserName(employeeOrientation.getNumber());
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
			setUserName(oldName+a,employeeOrientation,oldName);
		}else{
			employeeOrientation.setUserName(userName);
		}
	}
}
