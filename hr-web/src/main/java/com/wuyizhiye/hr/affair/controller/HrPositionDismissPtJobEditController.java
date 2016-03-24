package com.wuyizhiye.hr.affair.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;
import com.wuyizhiye.workflow.enums.ProcessTypeEnum;
import com.wuyizhiye.workflow.service.ProcessViewService;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.EmployeeOrientationService;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 员工组织 职 级异动   -- 晋升
 * @author ouyangyi
 * @since 2013-4-10 上午09:28:08
 */
@Controller
@RequestMapping(value="hr/positiondismissptjob/*")
public class HrPositionDismissPtJobEditController extends EditController{
	
	@Resource
	private PositionHistoryBillService positionHistoryBillService ;

	@Autowired
	private EmployeeOrientationService employeeOrientationService;

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
				billNumber = GenerateKey.getKeyCode(null, ProcessTypeEnum.DISMISS_PARTTIMEJOB.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bill.setBillNumber(billNumber);
		}
		bill.setTitle("申请取消 "+bill.getApplyPersonName()+" "+bill.getApplyOrg().getName()+"-"+
				bill.getApplyJoblevel().getName()+" 的兼职");
		if("SUBMIT".equals(bill.getBillStatus())){
			bill.setSubmitDate(new Date());
		}
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
			/*if(StringUtils.isEmpty(bill.getBillNumber())){
				getOutputMsg().put("MSG", "调职单生成单据编号异常,不能提交！");
				return false;
			}*/
			//提交申请单时 不能有其它已提交的调职申请单
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
		}
				
		return true;
	}
	
	@Override
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Object data = getSubmitEntity();
		PositionHistoryBill phb = (PositionHistoryBill)data;
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					phb.setUUID();
					positionHistoryBillService.addEntity(phb);
				}else{
					positionHistoryBillService.updateEntity(phb);
				}
				
				this.processViewService.generateNWFProcessView(phb
						.getTitle(), phb.getId(), ProcessTypeEnum.DISMISS_PARTTIMEJOB,
						phb.getBillStatus(), "HR",
						null == phb.getApplyPerson() ? ""
								: phb.getApplyPerson().getId(),
								phb.getName(), "");
				
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
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
		String approveFlag = positionHistoryBillService.approvePartTimeJob(ids,approveType);
		
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
		String approveMsg = positionHistoryBillService.backApprovePartTimeJob(billId,approveType);
		
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

}
