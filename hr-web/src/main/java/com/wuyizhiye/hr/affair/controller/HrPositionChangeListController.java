package com.wuyizhiye.hr.affair.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.workflow.service.WorkFlowService;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;
import com.wuyizhiye.hr.affair.service.PositionHistoryBillService;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.utils.BaseUtils;

/**
 * 员工组织 职 级异动   -- 调动
 * @author ouyangyi
 * @since 2013-4-8 下午04:14:18
 */
@Controller
@RequestMapping(value="hr/positionchange/*")
public class HrPositionChangeListController extends ListController{
	static final String ALLOW_BACK_APPROVE = "ALLOW_BACK_APPROVE";//是否允许反审核 
	@Resource
	private PositionHistoryBillService positionHistoryBillService ;
	
	@Resource
	private WorkFlowService workFlowService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		PositionHistoryBill entity = positionHistoryBillService.getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				if(StringUtils.isNotNull(entity.getProcessInstance())){
				  workFlowService.deleteProcessInstance(entity.getProcessInstance(), "", true);
				}else{
					workFlowService.deleteProcessView(entity.getId());
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@Override
	protected CoreEntity createNewEntity() {
		String billIds = getString("billIds");
		String id = getString("id");
		PositionHistoryBill pc = new PositionHistoryBill();
		List<PositionHistoryBill> list = null;
		if(StringUtils.isEmpty(billIds) && StringUtils.isEmpty(id)){
			list = new ArrayList<PositionHistoryBill>();
		}else{
			if(!StringUtils.isEmpty(billIds)){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("billIds", billIds);
				params.put("orderByClause", " phb.FCREATE_TIME desc");
				list = positionHistoryBillService.selectPositionHistoryBills(params);
			}
			if(!StringUtils.isEmpty(id)){
				list = new ArrayList<PositionHistoryBill>();
				 pc = positionHistoryBillService.getEntityById(id);
				 list.add(pc);
			}
		}
		if(list.size()<1){
			BaseUtils.setWho(pc, false);
			list.add(pc);
		}
		getRequest().setAttribute("positionChangeBills", list);
		return pc;
	}
	
	@RequestMapping(value="edit")
	@Dependence(method="list")
	public String edit(ModelMap model,@RequestParam(required=false,value="id")String id){
		model.put("data", createNewEntity());
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	
	@RequestMapping(value="toPositionChangeIndex")
	public String toPositionChangeIndex(){
		return "hr/affair/positionChangeIndex";
	}

	@Override
	protected String getListView() {
		String allBackApprove = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ALLOW_BACK_APPROVE);
		if(StringUtils.isEmpty(allBackApprove)){
			allBackApprove ="N";//默认不允许反审核N
		}
		getRequest().setAttribute("allBackApprove", allBackApprove);
		getRequest().setAttribute("billStatusList", BillStatusEnum.toList());
		return "hr/positionchange/positionChangeList";
	}

	@Override
	protected String getEditView() {
		
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "hr/positionchange/positionChangeEdit";
	}
	/**
	 * 公司或集团下 或者 组织类型为 人力资源(T02)下的人 不需要根据登录人组织过滤
	 * @return
	 */
	private String isFilterByOrg(){
		Org org = SystemUtil.getCurrentOrg();
		if(org ==null || StringUtils.isEmpty(org.getBusinessTypes())){
			return  "N";//公司 和集团
		}
		//业务类型为 人力资源
		if(OrgUtils.isType(org, "T02")){
			return "N";
		}
		return "Y";
	}

	@Override
	protected String getListMapper() {
		 
		return "com.wuyizhiye.hr.dao.PositionHistoryBillDao.selectByExample";
	}

	@Override
	protected BaseService getService() {
		 
		return positionHistoryBillService;
	}

}
