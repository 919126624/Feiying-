package com.wuyizhiye.hr.affair.controller;

import java.util.Date;
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

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 员工组织 职 级异动   -- 降职
 * @author ouyangyi
 * @since 2013-4-10 上午09:28:08
 */
@Controller
@RequestMapping(value="hr/positiondemotion/*")
public class HrPositionDemotionEditController extends EditController{
	
	@Resource
	private PositionHistoryBillService positionHistoryBillService ;

	@Resource
	private PersonPositionHistoryService personPositionHistoryService ;
	
	@Autowired
	private EmployeeOrientationService employeeOrientationService;

	@Autowired
	private PositionService positionService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;

	@Override
	protected BaseService getService() {
		 
		return positionHistoryBillService;
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
		PositionHistoryBill bill = ((PositionHistoryBill)entity);
		if(StringUtils.isEmpty(bill.getBillNumber())){
			String billNumber = null;
			try {
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.DEMOTION.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bill.setBillNumber(billNumber);
		}
		if("SUBMIT".equals(bill.getBillStatus())){
			bill.setSubmitDate(new Date());
		}
		bill.setTitle("申请将 "+bill.getApplyPersonName()+" 降职为 "+bill.getApplyChangeOrg().getName()+"-"+
				bill.getApplyChangeJoblevel().getName());
		BaseUtils.setWho(bill, false);
		return entity;
	}
	

	@Override
	protected boolean validate(Object data) {
		PositionHistoryBill bill = ((PositionHistoryBill)data);
		if(StringUtils.isEmpty(bill.getBillNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！");
			return false;
		}
		//提交申请单时 不能有其它已提交的调职申请单
		if("SUBMIT".equals(bill.getBillStatus())){
			/*Map<String, Object> params = new HashMap<String, Object>();
			params.put("personId", bill.getApplyPerson().getId());
			params.put("billStatus", BillStatusEnum.SUBMIT.getValue());
			List<PositionHistoryBill> list = positionHistoryBillService.selectPositionHistoryBills(params);
			if(list!=null && list.size()>0){
				getOutputMsg().put("MSG", "员工["+bill.getApplyPersonName()+
						"]还有在审的调职["+PositionChangeTypeEnum.getEnumByValue(list.get(0).getChangeType()).getLabel()+"]单,不能提交！");
				return false;
			}*/
			String msg = employeeOrientationService.personHasBillAppvol(bill.getId(),bill.getApplyPerson().getId(), "SUBMIT");
			if(msg!=null){
				getOutputMsg().put("MSG", "员工["+bill.getApplyPersonName()+"]还有在审的调职["+msg+"]单,不能提交！");
				return false;
			}else{
				msg = employeeOrientationService.personHasBillAppvol(bill.getId(),bill.getApplyPerson().getId(), "APPROVED");
				if(msg!=null){
					getOutputMsg().put("MSG", "员工["+bill.getApplyPersonName()+"]已经通过审批的["+msg+"]单未生效,不能提交！");
					return false;
				}
			}
			if((bill.getApplyPerson() !=null && StringUtils.isNotNull(bill.getApplyPerson().getId()))){
				PersonPositionHistory ph = personPositionHistoryService.getLastPersonPositionHistory(bill.getApplyPerson().getId(), true);
				if(ph!=null && bill.getEffectdate()!=null && ph.getEffectdate()!=null && bill.getEffectdate().compareTo(ph.getEffectdate())<0){
					//如果生效日期小于 人员最后异动日期 则不充许保存
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", "员工["+bill.getApplyPersonName()+"]生效日期小于人员最后异动["+PositionChangeTypeEnum.getEnumByValue(ph.getChangeType()).getLabel()+"]日期 ,不能提交");
					return false;
				}
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
				PositionHistoryBill bill = ((PositionHistoryBill)data);
				if(workFlowService.existsProcessDefinition("demotionProcess")){
					if(BillStatusEnum.SUBMIT.toString().equals(bill.getBillStatus())){
						startWorkFlow(bill);
					}else if(BillStatusEnum.SAVE.equals(bill.getBillStatus())){
						this.processViewService.generateWFProcessView(bill
								.getTitle(), bill.getId(),
								bill.getProcessInstance(), ProcessTypeEnum.DEMOTION,
								bill.getBillStatus(), "HR",
								null == bill.getApplyPerson() ? ""
										: bill.getApplyPerson().getId(),
										null == bill.getApplyPerson() ? ""
												: bill.getApplyPerson().getName(), "");
					}
				}else{
					this.processViewService.generateNWFProcessView(bill
							.getTitle(), bill.getId(),ProcessTypeEnum.DEMOTION,
							bill.getBillStatus(), "HR",
							null == bill.getApplyPerson() ? ""
									: bill.getApplyPerson().getId(),
									null == bill.getApplyPerson() ? ""
											: bill.getApplyPerson().getName(), "");
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
	
	
	//降职审批流
	private void startWorkFlow(PositionHistoryBill personPositionChange){
		if(workFlowService.existsProcessDefinition("demotionProcess")){
			Map<String,Object> param = new HashMap<String, Object>();
			personPositionChange.setApplyPosition(positionService.getEntityById(personPositionChange.getApplyPosition().getId()));
			param.put("positionType", personPositionChange.getApplyPosition().getJob().getNumber());//调职岗位类型
			param.put("positionId", personPositionChange.getApplyPosition().getId());//调职岗位ID
			personPositionChange.setApplyChangePosition(positionService.getEntityById(personPositionChange.getApplyChangePosition().getId()));
			param.put("positionChangeType", personPositionChange.getApplyChangePosition().getJob().getNumber());//调职后岗位类型
			param.put("orgChange", personPositionChange.getApplyChangeOrg().getId());//调职后组织
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), personPositionChange.getId(), personPositionChange.getApplyOrg(), personPositionChange.getTitle(), param,personPositionChange.getApplyPerson()) ;
			variables.setViewPath("hr/positiondemotion/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("demotionProcess", personPositionChange.getId(), variables);
			personPositionChange.setProcessInstance(process.getProcessInstanceId());
			positionHistoryBillService.updateEntity(personPositionChange);
			
			this.processViewService.generateWFProcessView(personPositionChange
					.getTitle(), personPositionChange.getId(),
					personPositionChange.getProcessInstance(), ProcessTypeEnum.DEMOTION,
					personPositionChange.getBillStatus(), "HR",
					null == personPositionChange.getApplyPerson() ? ""
							: personPositionChange.getApplyPerson().getId(),
							null == personPositionChange.getApplyPerson() ? ""
									: personPositionChange.getApplyPerson().getName(), "");
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
		String approveFlag = positionHistoryBillService.approve(ids,approveType);
		
		if("SUCC".equals(approveFlag)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "操作失败");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 反审核
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="backApprove")
	@Transactional
	public void backApprove(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String billId = getString("billId");
		String approveType = getString("approveType");
		if(StringUtils.isEmpty(billId)){
			getOutputMsg().put("MSG", "没有需要审核的数据");
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
			 
		}else{ 
		//反审核
		String approveMsg = positionHistoryBillService.backApprove(billId,approveType);
		
		if("SUCC".equals(approveMsg)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "操作成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", approveMsg);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
		}
	}

	@Override
	protected Class getSubmitClass() {
		return PositionHistoryBill.class;
	}
	
	/**
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		PositionHistoryBill data = (PositionHistoryBill) getSubmitEntity();
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
		PositionHistoryBill data = (PositionHistoryBill) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销单据", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatus(BillStatusEnum.REVOKE.toString());
		positionHistoryBillService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销单据成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}

}
