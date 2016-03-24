/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.attendance.controller;


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
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.attendance.model.Leave;
import com.wuyizhiye.hr.attendance.service.LeaveService;


/**
 * 请假申请controller
 * @author lambert.huang
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/ask4Leave/*")
public class Ask4LeaveEditController extends EditController {
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;
	@Autowired
	private PersonService personService;
	@Override
	protected Class<Leave> getSubmitClass() {
		return Leave.class;
	}

	@Override
	protected BaseService<Leave> getService() {
		return this.leaveService;
	}

	@Override
	protected Object getSubmitEntity() {
		/*Leave data = (Leave) super.getSubmitEntity();
		return data;*/
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
		Leave data = ((Leave)entity);
		if(StringUtils.isEmpty(data.getBillNumber())){
			String billNumber = null;
			try {
				//单据编号
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.ASK4LEAVE.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setBillNumber(billNumber);
		}
		if("SUBMIT".equals(data.getAsk4LeaveStatus().getValue())){
			data.setSubmitDate(new Date());
		}
		Person ps=personService.getEntityById(data.getApplyPerson().getId());
		data.setTitle(data.getApplyPerson().getName()+"["+ps.getOrg().getName()+"]申请"+data.getAsk4LeaveType().getLabel()+":"+
				data.getLeaveDays()+"天");
		//BaseUtils.setWho(data, false);
		return  entity ;
	}
	
	@Override
	protected boolean validate(Object data) {
		Leave leave = ((Leave)data);
		if(StringUtils.isEmpty(leave.getBillNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！单据编号请命名为：ASK4LEAVE");
			return false;
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
					Leave leave = ((Leave)data);
					leave.setCreator(getCurrentUser());
					getService().addEntity(leave);
				}else{
					Leave leave = ((Leave)data);
					leave.setUpdator(getCurrentUser());
					leave.setLastUpdateTime(new Date());
					getService().updateEntity((Leave)data);
				}
				Leave leave = ((Leave)data);
				if(workFlowService.existsProcessDefinition("ask4LeaveProcess")){
					if(BillStatusEnum.SUBMIT.equals(leave.getAsk4LeaveStatus())){
						startWorkFlow(leave);
					}else if((BillStatusEnum.SAVE.equals(leave.getAsk4LeaveStatus()))){
						this.processViewService.generateWFProcessView(leave
								.getTitle(), leave.getId(),
								leave.getProcessInstance(), ProcessTypeEnum.ASK4LEAVE,
								leave.getAsk4LeaveStatus().getValue(), "HR",
								null == leave.getApplyPerson() ? ""
										: leave.getApplyPerson().getId(),
										null == leave.getApplyPerson() ? ""
												: leave.getApplyPerson().getName(), "");
					}
				}else{
					this.processViewService.generateNWFProcessView(leave
							.getTitle(), leave.getId(),ProcessTypeEnum.ASK4LEAVE,
							leave.getAsk4LeaveStatus().getValue(), "HR",
							null == leave.getApplyPerson() ? ""
									: leave.getApplyPerson().getId(),
									null == leave.getApplyPerson() ? ""
											: leave.getApplyPerson().getName(), "");
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
	
	//请假审批流
	private void startWorkFlow(Leave data){
		if(workFlowService.existsProcessDefinition("ask4LeaveProcess")){//审批流ID
			Map<String,Object> param = new HashMap<String, Object>();
			//param.put("org", orgService.getEntityById(data.getApplyPerson().getOrg().getId()));
			param.put("positionId", data.getPersonPosition().getId());//岗位ID
			param.put("leaveDays", data.getLeaveDays());//请假天数
			Map<String,Object> pms = new HashMap<String, Object>();
			pms.put("id", data.getPersonPosition().getId()); 
			Position ps=this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.PositionDao.getById", pms, Position.class);
			if(ps!=null){
				param.put("positionType", ps.getJob().getNumber());//岗位编码
			}
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), data.getId(), data.getApplyPerson().getOrg(), data.getTitle(), param, data.getApplyPerson()) ;
			variables.setViewPath("/hr/ask4Leave/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("ask4LeaveProcess", data.getId(), variables);
			data.setProcessInstance(process.getProcessInstanceId());
			leaveService.updateEntity(data);
			this.processViewService.generateWFProcessView(data
					.getTitle(), data.getId(),
					data.getProcessInstance(),ProcessTypeEnum.ASK4LEAVE,
					data.getAsk4LeaveStatus().getValue(),"HR",
					null == data.getApplyPerson() ? ""
							: data.getApplyPerson().getId(),
							null == data.getApplyPerson() ? ""
									: data.getApplyPerson().getName(), "");
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
		String approveFlag =leaveService.approveAsk4Leave(ids, approveType);
		
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
	 * 修改单据并提交当前属于自己的节点
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="updateSubmit")
	public void updateSubmit(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Leave data = (Leave) getSubmitEntity();
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
	 * 撤回请假
	 * @param response
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="cancleBill")
	public void cancleBill(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Leave data = (Leave) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销请假", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setAsk4LeaveStatus(BillStatusEnum.REVOKE);
		leaveService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销请假成功!");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
