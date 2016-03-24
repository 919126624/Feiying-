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
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
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
import com.wuyizhiye.hr.attendance.model.OverTime;
import com.wuyizhiye.hr.attendance.service.OverTimeService;


/**
 * 加班申请controller
 * @author hyl
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/overTime/*")
public class OverTimeEditController extends EditController {
	@Autowired
	private OverTimeService overTimeService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected Class<OverTime> getSubmitClass() {
		return OverTime.class;
	}

	@Override
	protected BaseService<OverTime> getService() {
		return this.overTimeService;
	}

	@Override
	protected Object getSubmitEntity() {
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
		OverTime data = ((OverTime)entity);
		if(StringUtils.isEmpty(data.getBillNumber())){
			String billNumber = null;
			try {
				//单据编号
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.OVERTIME.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setBillNumber(billNumber);
		}
		if("SUBMIT".equals(data.getBillStatus().getValue())){
			data.setSubmitDate(new Date());
		}
		data.setTitle(data.getApplyPerson().getName()+"申请加班:"+
				data.getTimeTotal()+"小时");
		//BaseUtils.setWho(data, false);
		return  entity ;
	}
	
	@Override
	protected boolean validate(Object data) {
		OverTime overTime = ((OverTime)data);
		if(StringUtils.isEmpty(overTime.getBillNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！单据编号请命名为：OVERTIME");
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
				OverTime overTime = ((OverTime)data);
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					overTime.setCreator(getCurrentUser());
					overTime.setCreateTime(new Date());
					getService().addEntity(overTime);
				}else{
					overTime.setLastUpdateTime(new Date());
					overTime.setUpdator(getCurrentUser());
					getService().updateEntity(overTime);
				}
				if(workFlowService.existsProcessDefinition("ask4LeaveProcess")){
					if(BillStatusEnum.SUBMIT.equals(overTime.getBillStatus())){
						startWorkFlow(overTime);
					}else if((BillStatusEnum.SAVE.equals(overTime.getBillStatus()))){
						this.processViewService.generateWFProcessView(overTime
								.getTitle(), overTime.getId(),
								overTime.getProcessInstance(), ProcessTypeEnum.OVERTIME,
								overTime.getBillStatus().getValue(), "HR",
								null == overTime.getApplyPerson() ? ""
										: overTime.getApplyPerson().getId(),
										null == overTime.getApplyPerson() ? ""
												: overTime.getApplyPerson().getName(), "");
					}
				}else{
					this.processViewService.generateNWFProcessView(overTime
							.getTitle(), overTime.getId(),ProcessTypeEnum.OVERTIME,
							overTime.getBillStatus().getValue(), "HR",
							null == overTime.getApplyPerson() ? ""
									: overTime.getApplyPerson().getId(),
									null == overTime.getApplyPerson() ? ""
											: overTime.getApplyPerson().getName(), "");
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
	
	//加班审批流
	private void startWorkFlow(OverTime data){
		if(workFlowService.existsProcessDefinition("overTimeProcess")){//审批流ID
			Map<String,Object> param = new HashMap<String, Object>();
			//param.put("org", orgService.getEntityById(data.getApplyPerson().getOrg().getId()));
			param.put("positionId", data.getPersonPosition().getId());//申请人岗位ID
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), data.getId(), data.getApplyPerson().getOrg(), data.getTitle(), param,data.getApplyPerson()) ;
			variables.setViewPath("/hr/overTime/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("overTimeProcess", data.getId(), variables);
			data.setProcessInstance(process.getProcessInstanceId());
			overTimeService.updateEntity(data);
			this.processViewService.generateWFProcessView(data
					.getTitle(), data.getId(),
					data.getProcessInstance(),ProcessTypeEnum.OVERTIME,
					data.getBillStatus().getValue(),"HR",
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
		String approveFlag =overTimeService.approveOverTime(ids, approveType);
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
		OverTime data = (OverTime) getSubmitEntity();
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
		OverTime data = (OverTime) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销加班申请！", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setBillStatus(BillStatusEnum.REVOKE);
		overTimeService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销请假成功!");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
