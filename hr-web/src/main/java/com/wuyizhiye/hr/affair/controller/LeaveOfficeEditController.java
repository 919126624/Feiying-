package com.wuyizhiye.hr.affair.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
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
import com.wuyizhiye.hr.affair.model.LeaveOffice;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.LeaveOfficeService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
@Controller
@RequestMapping(value="hr/affair/leaveOffice/*")
public class LeaveOfficeEditController extends EditController{
	@Autowired
	private LeaveOfficeService leaveOfficeService ;
	 
	@Autowired
	private EmployeeOrientationService employeeOrientationService;
	@Resource
	private PersonPositionHistoryService personPositionHistoryService ;
	@Autowired 
	PersonService personService;
	
	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;
	
	private final static String BILLNUMBER = ProcessTypeEnum.LEAVE.toString();
	@Override
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		LeaveOffice data = (LeaveOffice) getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					
					try {
						data.setChangeType(PositionChangeTypeEnum.LEAVE);
						String fnumber = GenerateKey.getKeyCode(null, BILLNUMBER);
						data.setNumber(fnumber);
						String res = employeeOrientationService.personHasBillAppvol(data.getId(),data.getApplyPerson().getId(), "SUBMIT");
						if(res!=null){
							getOutputMsg().put("STATE", "FAIL");
							getOutputMsg().put("MSG", "保存失败！该人员有"+res+"单在审批中！");
							outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
							return;
						}else{
							data.setTitle(data.getApplyOrg().getName()+""+data.getApplyPosition().getName()+""+data.getApplyPerson().getName()+"申请离职");
							leaveOfficeService.addEntity(data);
							/*if(BillStatusEnum.APPROVED.equals(data.getBillStatus())){
								//审核
								Map<String,Object> resultMap  = leaveOfficeService.approvalBill(data.getId());
								if(!"SUCCESS".equals(resultMap.get("STATE"))){
									getOutputMsg().put("STATE", "FAIL");
									getOutputMsg().put("MSG", resultMap.get("MSG"));
								}
									
							}*/
							if(workFlowService.existsProcessDefinition("levelProcess")){
								if(BillStatusEnum.SUBMIT.equals(data.getBillStatus())){
									startWorkFlow(data);
								}else if(BillStatusEnum.SAVE.equals(data.getBillStatus())){
									this.processViewService.generateWFProcessView(data
											.getTitle(), data.getId(),
											data.getProcessInstance(), ProcessTypeEnum.LEAVE,
											data.getBillStatus().getValue(), "HR",
											null == data.getApplyPerson() ? ""
													: data.getApplyPerson().getId(),
													null == data.getApplyPerson() ? ""
															: data.getApplyPerson().getName(), "");
								}				
							}else{
								this.processViewService.generateNWFProcessView(data
										.getTitle(), data.getId(),ProcessTypeEnum.LEAVE,
										data.getBillStatus().getValue(), "HR",
										null == data.getApplyPerson() ? ""
												: data.getApplyPerson().getId(),
												null == data.getApplyPerson() ? ""
														: data.getApplyPerson().getName(), "");
							}
						}
					} catch (Exception e) {
						getOutputMsg().put("STATE", "FAIL");
						getOutputMsg().put("MSG", "系统异常！请联系管理员！");
						e.printStackTrace();
					}
					
				}else{
					data.setTitle(data.getApplyOrg().getName()+""+data.getApplyPosition().getName()+data.getApplyPerson().getName()+"申请离职");
					leaveOfficeService.updateEntity(data);
					if(workFlowService.existsProcessDefinition("levelProcess")){
						if(BillStatusEnum.SUBMIT.equals(data.getBillStatus())){
							startWorkFlow(data);
						}else if(BillStatusEnum.SAVE.equals(data.getBillStatus())){
							this.processViewService.generateWFProcessView(data
									.getTitle(), data.getId(),
									data.getProcessInstance(), ProcessTypeEnum.LEAVE,
									data.getBillStatus().getValue(), "HR",
									null == data.getApplyPerson() ? ""
											: data.getApplyPerson().getId(),
											null == data.getApplyPerson() ? ""
													: data.getApplyPerson().getName(), "");
						}
					}else{
						this.processViewService.generateNWFProcessView(data
								.getTitle(), data.getId(),ProcessTypeEnum.LEAVE,
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
	
	protected boolean validate(LeaveOffice data) {
		if(BillStatusEnum.SUBMIT.equals(data.getBillStatus()) &&
				(data.getApplyPerson() !=null && StringUtils.isNotNull(data.getApplyPerson().getId()))){
			
				String msg = employeeOrientationService.personHasBillAppvol(data.getId(),data.getApplyPerson().getId(), "SUBMIT");
				if(msg!=null){
					getOutputMsg().put("MSG", "员工["+data.getApplyPerson().getName()+"]还有在审的调职["+msg+"]单,不能提交！");
					return false;
				}else{
					msg = employeeOrientationService.personHasBillAppvol(data.getId(),data.getApplyPerson().getId(), "APPROVED");
					if(msg!=null){
						getOutputMsg().put("MSG", "员工["+data.getApplyPerson().getName()+"]已经通过审批的["+msg+"]单未生效,不能提交！");
						return false;
					}
				}
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
	private void startWorkFlow(LeaveOffice leaveOffice){
		if(workFlowService.existsProcessDefinition("levelProcess")){
			Map<String,Object> param = new HashMap<String, Object>();
			leaveOffice.setApplyPosition(positionService.getEntityById(leaveOffice.getApplyPosition().getId()));
			param.put("positionType", leaveOffice.getApplyPosition().getJob().getNumber());//入职岗位类型
			param.put("positionId", leaveOffice.getApplyPosition().getId());//职位ID
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), leaveOffice.getId(), leaveOffice.getApplyOrg(), leaveOffice.getTitle(), param,leaveOffice.getApplyPerson()) ;
			variables.setViewPath("hr/affair/leaveOffice/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("levelProcess", leaveOffice.getId(), variables);
			leaveOffice.setProcessInstance(process.getProcessInstanceId());
			leaveOfficeService.updateEntity(leaveOffice);
			
			this.processViewService.generateWFProcessView(leaveOffice
					.getTitle(), leaveOffice.getId(),
					leaveOffice.getProcessInstance(), ProcessTypeEnum.LEAVE,
					leaveOffice.getBillStatus().getValue(), "HR",
					null == leaveOffice.getApplyPerson() ? ""
							: leaveOffice.getApplyPerson().getId(),
							null == leaveOffice.getApplyPerson() ? ""
									: leaveOffice.getApplyPerson().getName(), "");
		}
				
	}
	
	/**
	 * 审批通过调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="approvalBill")
	@ResponseBody
	@Transactional
	public Map<String,Object> approvalBill(){
		String billId = getString("billId");
		LeaveOffice leaveOffice = leaveOfficeService.getEntityById(billId);
		if(leaveOffice!=null){
			try {
				return leaveOfficeService.approvalBill(billId);
				 
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
	 * 批量改变审批状态调用方法
	 * @author Cai.xing
	 * */
	@RequestMapping(value="updateStatu")
	@ResponseBody
	public Map<String,Object> updateStatu(){
		String powersJson = getString("hpStr");
    	List<LeaveOffice> housePowers = null;
    	if(!StringUtils.isEmpty(powersJson)){
    		housePowers = new ArrayList<LeaveOffice>(JSONArray.toCollection(JSONArray.fromObject(powersJson), LeaveOffice.class));
		}
    	try {
    		Iterator<LeaveOffice> its = housePowers.iterator();
    		while(its.hasNext()){
    			LeaveOffice houspower = its.next();
    			LeaveOffice employeeOrientation = leaveOfficeService.getEntityById(houspower.getId());
    			employeeOrientation.setBillStatus(houspower.getBillStatus());
    			leaveOfficeService.updateEntity(employeeOrientation);
    		}
    		getOutputMsg().put("STATE", "SUCCESS");
    		getOutputMsg().put("MSG", "操作成功！");
		} catch (Exception e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "系统异常！");
			e.printStackTrace();
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
		//审批通过后反写教育经历\工作经历及人员信息
		String billId = getString("billId");
		String billStatu = getString("billStatu");
		
		LeaveOffice leaveOffice = leaveOfficeService.getEntityById(billId);
		if(leaveOffice!=null){
			try {
				
				leaveOffice.setBillStatus(BillStatusEnum.valueOf(billStatu));
				leaveOfficeService.updateEntity(leaveOffice);
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
	protected Class<LeaveOffice> getSubmitClass() {
		return LeaveOffice.class;
	}
	@Override
	protected BaseService<LeaveOffice> getService() {
		return leaveOfficeService;
	}
	

	/**
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		LeaveOffice data = (LeaveOffice) getSubmitEntity();
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
		LeaveOffice data = (LeaveOffice) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销单据", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatus(BillStatusEnum.REVOKE);
		leaveOfficeService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销单据成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
