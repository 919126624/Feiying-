package com.wuyizhiye.hr.affair.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.affair.model.Positive;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.PositiveService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
@Controller
@RequestMapping(value="hr/affair/positive/*")
public class PositiveEditController extends EditController{
	@Autowired
	PositiveService positiveService;
	@Autowired
	private EmployeeOrientationService employeeOrientationService;
	@Resource
	private PersonPositionHistoryService personPositionHistoryService ;
	@Autowired 
	PersonService personService;
	@Autowired 
	private BasicDataService basicDataService;
	private final static String BILLNUMBER = ProcessTypeEnum.POSITIVE.toString(); //转正单据
	
	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;
	
	@Override
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Positive data = (Positive) getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					data.setChangeType(PositionChangeTypeEnum.POSITIVE);
					String fnumber;
					try {
						fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
						data.setNumber(fnumber);
						String res = employeeOrientationService.personHasBillAppvol(data.getId(),data.getApplyPerson().getId(), "SUBMIT");
						if(res!=null){
							getOutputMsg().put("STATE", "FAIL");
							getOutputMsg().put("MSG", "保存失败!该人员有"+res+"单在审批中!");
							outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
							return;
						}else{
							data.setTitle(data.getApplyOrg().getName()+""+data.getApplyPerson().getName()+"申请转正");
							positiveService.addEntity(data);
							if(workFlowService.existsProcessDefinition("positiveProcess")){
								if(BillStatusEnum.SUBMIT.equals(data.getBillStatus())){
									startWorkFlow(data);
								}else if(BillStatusEnum.SAVE.equals(data.getBillStatus())){
									this.processViewService.generateWFProcessView(data
											.getTitle(), data.getId(),
											data.getProcessInstance(), ProcessTypeEnum.POSITIVE,
											data.getBillStatus().getValue(), "HR",
											null == data.getApplyPerson() ? ""
													: data.getApplyPerson().getId(),
													null == data.getApplyPerson() ? ""
															: data.getApplyPerson().getName(), "");
								}
							}else{
								this.processViewService.generateNWFProcessView(data
										.getTitle(), data.getId(),ProcessTypeEnum.POSITIVE,
										data.getBillStatus().getValue(), "HR",
										null == data.getApplyPerson() ? ""
												: data.getApplyPerson().getId(),
												null == data.getApplyPerson() ? ""
														: data.getApplyPerson().getName(), "");
							}
						}
					} catch (Exception e) {
						getOutputMsg().put("STATE", "FAIL");
						getOutputMsg().put("MSG", e.getMessage());
						outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
						return;
					}
				}else{
					data.setTitle(data.getApplyOrg().getName()+""+data.getApplyPerson().getName()+"申请转正");
					positiveService.updateEntity(data);
					if(workFlowService.existsProcessDefinition("positiveProcess")){
						if(BillStatusEnum.SUBMIT.equals(data.getBillStatus())&&StringUtils.isEmpty(data.getProcessInstance())){
							startWorkFlow(data);
						}else if(BillStatusEnum.SAVE.equals(data.getBillStatus())){
							this.processViewService.generateWFProcessView(data
									.getTitle(), data.getId(),
									data.getProcessInstance(), ProcessTypeEnum.POSITIVE,
									data.getBillStatus().getValue(), "HR",
									null == data.getApplyPerson() ? ""
											: data.getApplyPerson().getId(),
											null == data.getApplyPerson() ? ""
													: data.getApplyPerson().getName(), "");
						}
					}else{
						this.processViewService.generateNWFProcessView(data
								.getTitle(), data.getId(),ProcessTypeEnum.POSITIVE,
								data.getBillStatus().getValue(), "HR",
								null == data.getApplyPerson() ? ""
										: data.getApplyPerson().getId(),
										null == data.getApplyPerson() ? ""
												: data.getApplyPerson().getName(), "");
					}
					
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
	
	protected boolean validate(Positive data) {
		if(BillStatusEnum.SUBMIT.equals(data.getBillStatus()) &&
				(data.getApplyPerson() !=null && StringUtils.isNotNull(data.getApplyPerson().getId()))){
			PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(data.getApplyPerson().getId(), true);
			if(ph!=null && data.getValidateTime()!=null && ph.getEffectdate()!=null && data.getValidateTime().compareTo(ph.getEffectdate())<0){
				//如果生效日期小于 人员最后异动日期 则不充许保存
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "生效日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
				return false;
			}
		}
		return true;
	}
	
	//离职审批流
	private void startWorkFlow(Positive positive){
		if(workFlowService.existsProcessDefinition("positiveProcess")){
			Map<String,Object> param = new HashMap<String, Object>();
			positive.setApplyPosition(positionService.getEntityById(positive.getApplyPosition().getId()));
			param.put("positionType", positive.getApplyPosition().getJob().getNumber());//入职岗位类型
			param.put("positionId", positive.getApplyPosition().getId());//职位ID
			param.put("billPerson", positive.getApplyPerson().getId());//单据人员ID
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), positive.getId(), positive.getApplyOrg(), positive.getTitle(), param,positive.getApplyPerson()) ;
			variables.setViewPath("/hr/affair/positive/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("positiveProcess", positive.getId(), variables);
			positive.setProcessInstance(process.getProcessInstanceId());
			positiveService.updateEntity(positive);
			
			this.processViewService.generateWFProcessView(positive
					.getTitle(), positive.getId(),
					positive.getProcessInstance(), ProcessTypeEnum.POSITIVE,
					positive.getBillStatus().getValue(), "HR",
					null == positive.getApplyPerson() ? ""
							: positive.getApplyPerson().getId(),
							null == positive.getApplyPerson() ? ""
									: positive.getApplyPerson().getName(), "");
		}
		
	}
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="approvalBill")
	@ResponseBody
	public Map<String,Object> approvalBill(){
		String billId = getString("billId");
		Positive positive = positiveService.getEntityById(billId);
		if(positive!=null){
			try {
			 
				return positiveService.approvalBill(billId);
				 
			} catch (Exception e) {
				e.printStackTrace();
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "系统异常！请联系管理员！");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "单据已经不存在，请刷新后重试！");
		}
		return getOutputMsg();
	}
	 
	
	/**
	 * 改变审批状态调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="changeStatu")
	@ResponseBody
	@Transactional
	public Map<String,Object> changeStatu(){
		String billId = getString("billId");
		String billStatu = getString("billStatu");
		
		Positive positive = positiveService.getEntityById(billId);
		if(positive!=null){
			try {
				
				positive.setBillStatus(BillStatusEnum.valueOf(billStatu));
				positiveService.updateEntity(positive);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", BillStatusEnum.valueOf(billStatu).getLabel()+"成功！");
			} catch (Exception e) {
				e.printStackTrace();
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "系统异常！请联系管理员！");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "单据已经不存在，请刷新后重试！");
		}
		return getOutputMsg();
	}
	@Override
	protected Class<Positive> getSubmitClass() {
		return Positive.class;
	}

	@Override
	protected BaseService<Positive> getService() {
		return positiveService;
	}
	
	/**
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Positive data = (Positive) getSubmitEntity();
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
		this.save(response);
	}
	
	/**
	 * 撤回单据
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="cancleBill")
	public void cancleBill(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Positive data = (Positive) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销单据", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatus(BillStatusEnum.REVOKE);
		positiveService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销单据成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
