/**
 * com.wuyizhiye.basedata.basic.controller.BasicDataEditController.java
 */
package com.wuyizhiye.hr.attendance.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.workflow.util.WorkFlowVariables;
import com.wuyizhiye.hr.attendance.model.ClearanceLeave;
import com.wuyizhiye.hr.attendance.service.ClearanceLeaveService;


/**
 * 销假申请controller
 * @author lambert.huang
 *
 * @since 2013-11-18
 */
@Controller
@RequestMapping(value="hr/clearanceLeave/*")
public class ClearanceLeaveEditController extends EditController {
	@Autowired
	private ClearanceLeaveService clearanceLeaveService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ProcessViewService processViewService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	protected Class<ClearanceLeave> getSubmitClass() {
		return ClearanceLeave.class;
	}

	@Override
	protected BaseService<ClearanceLeave> getService() {
		return this.clearanceLeaveService;
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
		ClearanceLeave data = ((ClearanceLeave)entity);
		if(StringUtils.isEmpty(data.getBillNumber())){
			String billNumber = null;
			try {
				//销假单据编号
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.CLEARANCELEAVE.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.setBillNumber(billNumber);
		}
		if("SUBMIT".equals(data.getLeaveClearanceStatus().getValue())){
			data.setSubmitDate(new Date());
		}
		data.setTitle(data.getLeave().getApplyPerson().getName()+"申请销"+data.getLeave().getAsk4LeaveType().getLabel());
		//BaseUtils.setWho(data, false);
		return  entity ;
	}
	@Override
	protected boolean validate(Object data) {
		ClearanceLeave clearanceLeave = ((ClearanceLeave)data);
		if(StringUtils.isEmpty(clearanceLeave.getBillNumber())){
			getOutputMsg().put("MSG", "生成单据编号失败,请检查单据编号配置！单据编号请命名为：CLEARLEAVE");
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
					ClearanceLeave clearanceLeave = ((ClearanceLeave)data);
					clearanceLeave.setCreator(getCurrentUser());
					getService().addEntity(clearanceLeave);
				}else{
					ClearanceLeave clearanceLeave = ((ClearanceLeave)data);
					clearanceLeave.setUpdator(getCurrentUser());
					clearanceLeave.setLastUpdateTime(new Date());
					getService().updateEntity((ClearanceLeave)data);
				}
				ClearanceLeave clearanceLeave = ((ClearanceLeave)data);
				if(BillStatusEnum.SUBMIT.equals(clearanceLeave.getLeaveClearanceStatus())){
					startWorkFlow(clearanceLeave);
				}else if((BillStatusEnum.SAVE.equals(clearanceLeave.getLeaveClearanceStatus())&&workFlowService.existsProcessDefinition("ask4LeaveProcess"))){
					this.processViewService.generateWFProcessView(clearanceLeave
							.getTitle(), clearanceLeave.getId(),
							clearanceLeave.getProcessInstance(),ProcessTypeEnum.CLEARANCELEAVE,
							clearanceLeave.getLeaveClearanceStatus().getValue(),"HR",
							null == clearanceLeave.getLeave().getApplyPerson() ? ""
									: clearanceLeave.getLeave().getApplyPerson().getId(),
									null == clearanceLeave.getLeave().getApplyPerson() ? ""
											: clearanceLeave.getLeave().getApplyPerson().getName(), "");
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
	//销假审批流
	private void startWorkFlow(ClearanceLeave data){
		if(workFlowService.existsProcessDefinition("clearLeaveProcess")){//审批流ID
			Map<String,Object> param = new HashMap<String, Object>();
			//param.put("org", orgService.getEntityById(data.getLeave().getApplyPerson().getOrg().getId()));
			param.put("positionId", data.getLeave().getPersonPosition().getId());//岗位ID
			param.put("actualLeaveDays", data.getActualLeaveDays());//销假天数
			Map<String,Object> pms = new HashMap<String, Object>();
			pms.put("id", data.getLeave().getPersonPosition().getId()); 
			Position ps=this.queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.PositionDao.getById", pms, Position.class);
			if(ps!=null){
				param.put("positionType", ps.getJob().getNumber());//岗位编码
			}
			WorkFlowVariables variables = new WorkFlowVariables(SystemUtil.getCurrentUser(), data.getId(), data.getLeave().getApplyPerson().getOrg(), data.getTitle(), param,data.getLeave().getApplyPerson()) ;
			variables.setViewPath("/hr/clearanceLeave/edit?VIEWSTATE=VIEW&id=${id}");
			ProcessInstance process = workFlowService.startWorkFlow("clearLeaveProcess", data.getId(), variables);
			data.setProcessInstance(process.getProcessInstanceId());
			clearanceLeaveService.updateEntity(data);
			this.processViewService.generateWFProcessView(data
					.getTitle(), data.getId(),
					data.getProcessInstance(),ProcessTypeEnum.CLEARANCELEAVE,
					data.getLeaveClearanceStatus().getValue(),"HR",
					null == data.getLeave().getApplyPerson() ? ""
							: data.getLeave().getApplyPerson().getId(),
							null == data.getLeave().getApplyPerson() ? ""
									: data.getLeave().getApplyPerson().getName(), "");
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
		String approveFlag =clearanceLeaveService.approveClearanceLeave(ids, approveType);
		
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
		ClearanceLeave data = (ClearanceLeave) getSubmitEntity();
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
		ClearanceLeave data = (ClearanceLeave) getSubmitEntity();
		if(!StringUtils.isEmpty(data.getProcessInstance())){
			workFlowService.deleteProcessInstance(data.getProcessInstance(), "撤销销假申请", true);
		}
		//取消提交单据,此时需要修改单据状态并清空processInstance字段
		data.setProcessInstance(null);
		data.setLeaveClearanceStatus(BillStatusEnum.REVOKE);
		clearanceLeaveService.updateEntity(data);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "撤销销假申请成功!");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
