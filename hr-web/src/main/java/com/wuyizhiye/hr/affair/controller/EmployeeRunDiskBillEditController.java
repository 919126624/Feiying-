package com.wuyizhiye.hr.affair.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.JobStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.affair.model.EmployeeRunDiskBill;
import com.wuyizhiye.hr.affair.service.EmployeeRunDiskService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 跑盘员入职
 * @author ouyangyi
 * @since 2013-4-23 下午04:10:48
 */
@Controller
@RequestMapping(value="hr/employeerundiskbill/*")
public class EmployeeRunDiskBillEditController extends EditController{
	
	@Resource
	private EmployeeRunDiskService employeeRunDiskService ;
	
	@Resource
	private PersonPositionHistoryService personPositionHistoryService ;
	
	@Resource
	private PersonService personService;
	
	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;

	@Override
	protected BaseService getService() {
		 
		return employeeRunDiskService;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object getSubmitEntity(){
		Class c = getSubmitClass();
		if(c==null){
			return null;
		}
		String id = getString("id");
		Object entity = null;
		if(!StringUtils.isEmpty(id)){
			entity = getService().getEntityById(id);
		}
		entity = BeanUtils.fillentity(getParamMap(),entity, c);
		EmployeeRunDiskBill bill = ((EmployeeRunDiskBill)entity);
		if(StringUtils.isEmpty(bill.getBillNumber())){
			String billNumber = null;
			try {
				//单据编号
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.RUNDISK.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bill.setBillNumber(billNumber);
		}
		if(StringUtils.isEmpty(bill.getNumber())){
			String number = null;
			try {
				String ppyCode = ParamUtils.getParamValue("YGBH_PPY");
				if(!StringUtils.isEmpty(ppyCode)){
					//修改成 跑盘员生成临时编号 入职后为正式编号 add by li.biao since 2014-2-24
					number = GenerateKey.getKeyCode(null, "YGBH_PPY");
				}else{
					//员工编号
					number = GenerateKey.getKeyCode(null, "YGBH");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bill.setNumber(number);
		}
		if("SUBMIT".equals(bill.getBillStatus().getValue())){
			bill.setSubmitDate(new Date());
		}
		bill.setTitle(bill.getName()+" 申请成为 "+bill.getMainPositionOrg().getName()+"-"+
				bill.getMainJobLevel().getName()+"");
		BaseUtils.setWho(bill, false);
		//身份证
		if(CardTypeEnum.IDCARD.equals(bill.getCardType()) && StringUtils.isNotNull(bill.getIdCard())){
		 String idCard = bill.getIdCard().trim().toUpperCase();
		 bill.setIdCard(idCard);
		}
		return entity;
	}
	

	@Override
	protected boolean validate(Object data) {
		EmployeeRunDiskBill bill = ((EmployeeRunDiskBill)data);
		if(StringUtils.isEmpty(bill.getBillNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！");
			return false;
		}
		if(StringUtils.isEmpty(bill.getNumber())){
			getOutputMsg().put("MSG", "员工编号失败,请检查员工编号配置！");
			return false;
		}
		 
		if(CardTypeEnum.IDCARD.equals(bill.getCardType()) && StringUtils.isNotNull(bill.getIdCard())){
			//身份证校验
			String idCard = bill.getIdCard().trim().toUpperCase();
			bill.setIdCard(idCard);
			if(BillStatusEnum.SUBMIT.equals(bill.getBillStatus())){
				//提交操作时
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("idcard", idCard);
				//判断身份证表是否存在 在岗、跑盘、见习的人员
				List<Person> p1 = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByCondition", param, Person.class);
				 if(p1!=null && p1.size()>0){
					BasicData jobStatus = p1.get(0).getJobStatus();
					 if(jobStatus!=null && (JobStatusEnum.ONDUTY.getValue().equals(jobStatus.getNumber()) 
							 || JobStatusEnum.RUNDISK.getValue().equals(jobStatus.getNumber())
							 || JobStatusEnum.PROBATION.getValue().equals(jobStatus.getNumber()))){
						 getOutputMsg().put("STATE", "FAIL");
						 getOutputMsg().put("MSG", "提交失败,失败原因:系统已经存在身份证为["+idCard+"]的人员！");
						 return false;
					 } 
				 }
				//判断是否存在 待审核 的入职单 
				param.clear();
				param.put("billSta", BillStatusEnum.SUBMIT.toString());
				param.put("idCard",idCard);
				List<EmployeeOrientation> empList = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select",param,EmployeeOrientation.class);
				if(empList!=null && empList.size()>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "保存失败,失败原因:已经存在身份证为["+idCard+"]待审核的入职单！");
					return false;
				}
				//判断是否存在 待审核 的 跑盘申请单
				param.clear();
				param.put("billStatus", BillStatusEnum.SUBMIT.toString());
				param.put("idCard", idCard);
				List<EmployeeRunDiskBill> empRunDiskList = queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao.getEmployeeRunDiskBills",param,EmployeeRunDiskBill.class);
				if(empRunDiskList!=null && empRunDiskList.size()>0){
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "已存在身份证为["+idCard+"]待审核状态的跑盘员申请单");
					return false;
				}
			}
		}
		if(BillStatusEnum.SUBMIT.equals(bill.getBillStatus()) &&
				(bill.getApplyPerson() !=null && StringUtils.isNotNull(bill.getApplyPerson().getId()))){
			PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(bill.getApplyPerson().getId(), true);
			if(ph!=null && bill.getEffectdate()!=null && ph.getEffectdate()!=null && bill.getEffectdate().compareTo(ph.getEffectdate())<0){
				//如果生效日期小于 人员最后异动日期 则不充许保存
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "生效日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Object data = getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					getService().addEntity((CoreEntity)data);
				}else{
					getService().updateEntity((CoreEntity)data);
				}
				EmployeeRunDiskBill bill = ((EmployeeRunDiskBill)data);
				if(workFlowService.existsProcessDefinition("runDiskProcess")){
					if(BillStatusEnum.SUBMIT.equals(bill.getBillStatus())){
						startWorkFlow(bill);
					}else if(BillStatusEnum.SAVE.equals(bill.getBillStatus())){
						this.processViewService.generateWFProcessView(bill
								.getTitle(), bill.getId(),
								bill.getProcessInstance(), ProcessTypeEnum.RUNDISK,
								bill.getBillStatus().getValue(), "HR",
								null == bill.getApplyPerson() ? ""
										: bill.getApplyPerson().getId(),
										bill.getName(), bill
										.getPhoto());
					}
				}else{
					this.processViewService.generateNWFProcessView(bill
							.getTitle(), bill.getId(), ProcessTypeEnum.RUNDISK,
							bill.getBillStatus().getValue(), "HR",
							null == bill.getApplyPerson() ? ""
									: bill.getApplyPerson().getId(),
									bill.getName(), bill
									.getPhoto());
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateEmployeeRunDisk(){
		Class c = getSubmitClass();
		String id = getString("id");
		Object entity = null;
		if(!StringUtils.isEmpty(id)){
			entity = getService().getEntityById(id);
		}
		entity = BeanUtils.fillentity(getParamMap(),entity, c);
		getService().updateEntity((CoreEntity)entity);
	}
	
	//离职审批流
	private void startWorkFlow(EmployeeRunDiskBill bill){
		if(workFlowService.existsProcessDefinition("runDiskProcess")){
			Map<String,Object> param = new HashMap<String, Object>();
			bill.setMainPosition(positionService.getEntityById(bill.getMainPosition().getId()));
			param.put("positionType", bill.getMainPosition().getJob().getNumber());//入职岗位类型
			param.put("positionId", bill.getMainPosition().getId());//调职岗位ID
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), bill.getId(), bill.getMainPositionOrg(), bill.getTitle(), param,bill.getApplyPerson()) ;
			variables.setViewPath("/hr/employeerundiskbill/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("runDiskProcess", bill.getId(), variables);
			bill.setProcessInstance(process.getProcessInstanceId());
			employeeRunDiskService.updateEntity(bill);
			
			this.processViewService.generateWFProcessView(bill
					.getTitle(), bill.getId(),
					bill.getProcessInstance(), ProcessTypeEnum.RUNDISK,
					bill.getBillStatus().getValue(), "HR",
					null == bill.getApplyPerson() ? ""
							: bill.getApplyPerson().getId(),
							bill.getName(), bill
							.getPhoto());
		}
		
	}
	
	/**
	 * 审核
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="approve")
	@Transactional
	public void approve(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String billIds = getString("billIds");
		String approveType = getString("approveType");
		if(StringUtils.isEmpty(billIds)){
			getOutputMsg().put("counter", "0");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			return ;
		} 
		String[] ids = billIds.split(",");
		getOutputMsg().put("counter", ids.length);
		//审核
		String approveFlag = employeeRunDiskService.approveRunDisk(ids,approveType);
		
		if("SUCC".equals(approveFlag)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "操作失败");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@Override
	protected Class getSubmitClass() {
		return EmployeeRunDiskBill.class;
	}
	
	
	@RequestMapping(value="getPersonByIdCard")
	public void getPersonByIdCard(HttpServletRequest request,HttpServletResponse response){
		//根据身份证号码得到对应人的信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idcard", getString("idcard").trim().toUpperCase());
		List<Person> lisP = this.queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select",params, Person.class);
		if(lisP!=null && lisP.size()>0){
			String perosnId =lisP.get(0).getId();
			getOutputMsg().put("person", personService.getEntityById(perosnId));
		    outPrint(response, JSONObject.fromObject(getOutputMsg()));
    	}
	}
	
	/**
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		EmployeeRunDiskBill data = (EmployeeRunDiskBill) getSubmitEntity();
		try{
			if(data.getApplyPerson() !=null && StringUtils.isNotNull(data.getApplyPerson().getId())){
				PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(data.getApplyPerson().getId(), true);
				if(ph!=null && data.getEffectdate()!=null && ph.getEffectdate()!=null && data.getEffectdate().compareTo(ph.getEffectdate())<0){
					//如果生效日期小于 人员最后异动日期 则不充许保存
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "生效日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
					outPrint(response, JSONObject.fromObject(getOutputMsg()));
					return ;
				}
			}
			
			  updateEmployeeRunDisk();
			}catch(Exception e){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "保存失败："+e.getMessage());
				outPrint(response, JSONObject.fromObject(getOutputMsg()));
				return ;
			}
		if(data!=null && !StringUtils.isEmpty(data.getProcessInstance())){
			List<Task> tasks = workFlowService.getCurrentTask(data.getProcessInstance());
			if(tasks!=null){
				for(Task task : tasks){
					if(SystemUtil.getCurrentUser().getId().equals(task.getAssignee())){
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put(task.getTaskDefinitionKey() + "_status" , "true");
						variables.put(task.getTaskDefinitionKey() + "_description", "保存修改并提交");
						workFlowService.complete(task, variables );
					}
				}
			}
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存并提交成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	/**
	 * 撤回单据
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="cancleBill")
	public void cancleBill(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		EmployeeRunDiskBill data = (EmployeeRunDiskBill) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销单据", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatus(BillStatusEnum.REVOKE);
		employeeRunDiskService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销单据成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}

}
